//package com.reservoir.core.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.UUID;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//@Slf4j
//@Component
//public class RedisFairLock {
//
//    private final RedisTemplate<String, String> redisTemplate;
//    private static final String LOCK_SCRIPT = "return redis.call('set', KEYS[1], ARGV[1], 'NX', 'PX', ARGV[2])";
//    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//    private static final String RENEW_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end";
//
//    private final ThreadLocal<LockInfo> lockInfoThreadLocal = new ThreadLocal<>();
//    private final AtomicBoolean renewThreadRunning = new AtomicBoolean(false);
//    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//
//    public RedisFairLock(RedisTemplate<String, String> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
//        long start = System.currentTimeMillis();
//        LockInfo lockInfo = lockInfoThreadLocal.get();
//        if (lockInfo != null && lockInfo.getLockKey().equals(lockKey)) {
//            // 当前线程已经持有锁，增加重入计数
//            lockInfo.incrementHoldCount();
//            log.debug("Reentrant lock acquired for key: {}", lockKey);
//            return true;
//        } else {
//            String newLockValue = generateUniqueLockValue();
//            while (System.currentTimeMillis() - start < unit.toMillis(waitTime)) {
//                DefaultRedisScript<String> lockScript = new DefaultRedisScript<>(LOCK_SCRIPT, String.class);
//                String result = redisTemplate.execute(lockScript, Collections.singletonList(lockKey), newLockValue, String.valueOf(unit.toMillis(leaseTime)));
//                if ("OK".equals(result)) {
//                    lockInfoThreadLocal.set(new LockInfo(lockKey, newLockValue, leaseTime, unit));
//                    renewThreadRunning.set(true);
//                    startRenewTask(lockKey, newLockValue, leaseTime, unit);
//                    log.info("Lock acquired for key: {}", lockKey);
//                    return true;
//                }
//                Thread.sleep(100); // 等待一段时间后重试
//            }
//        }
//        log.warn("Failed to acquire lock for key: {} after waiting {} {}", lockKey, waitTime, unit);
//        return false;
//    }
//
//    public boolean unlock(String lockKey) {
//        LockInfo lockInfo = lockInfoThreadLocal.get();
//        if (lockInfo == null || !lockInfo.getLockKey().equals(lockKey)) {
//            log.warn("Lock not held by current thread for key: {}", lockKey);
//            return false;
//        }
//
//        if (lockInfo.decrementHoldCount() <= 0) {
//            // 完全释放锁
//            DefaultRedisScript<Long> unlockScript = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
//            Long result = redisTemplate.execute(unlockScript, Collections.singletonList(lockKey), lockInfo.getLockValue());
//            if (result != null && result == 1L) {
//                lockInfoThreadLocal.remove();
//                renewThreadRunning.set(false); // 停止续期线程
//                log.info("Lock released for key: {}", lockKey);
//                return true;
//            }
//        }
//        log.debug("Lock hold count decremented for key: {}", lockKey);
//        return false;
//    }
//
//    private void startRenewTask(String lockKey, String lockValue, long leaseTime, TimeUnit unit) {
//        long renewInterval = unit.toMillis(leaseTime) / 3;
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            try {
//                if (!renewThreadRunning.get()) {
//                    log.debug("Renew thread stopped for key: {}", lockKey);
//                    return;
//                }
//                if (renewLock(lockKey, lockValue, leaseTime, unit)) {
//                    log.info("Lock renewed for key: {}", lockKey);
//                } else {
//                    log.warn("Failed to renew lock for key: {}", lockKey);
//                }
//            } catch (Exception e) {
//                log.error("Error renewing lock for key: {}", lockKey, e);
//            }
//        }, renewInterval, renewInterval, TimeUnit.MILLISECONDS);
//    }
//
//    private boolean renewLock(String lockKey, String lockValue, long leaseTime, TimeUnit unit) {
//        DefaultRedisScript<Long> renewScript = new DefaultRedisScript<>(RENEW_SCRIPT, Long.class);
//        Long result = redisTemplate.execute(renewScript, Collections.singletonList(lockKey), lockValue, String.valueOf(unit.toMillis(leaseTime)));
//        return result != null && result == 1L;
//    }
//
//    private String generateUniqueLockValue() {
//        // 生成唯一锁值，可以使用UUID或其他唯一标识符
//        return UUID.randomUUID().toString();
//    }
//
//    private static class LockInfo {
//        private final String lockKey;
//        private final String lockValue;
//        private final long leaseTime;
//        private final TimeUnit unit;
//        private int holdCount;
//
//        public LockInfo(String lockKey, String lockValue, long leaseTime, TimeUnit unit) {
//            this.lockKey = lockKey;
//            this.lockValue = lockValue;
//            this.leaseTime = leaseTime;
//            this.unit = unit;
//            this.holdCount = 1;
//        }
//
//        public String getLockKey() {
//            return lockKey;
//        }
//
//        public String getLockValue() {
//            return lockValue;
//        }
//
//        public void incrementHoldCount() {
//            holdCount++;
//        }
//
//        public int decrementHoldCount() {
//            return --holdCount;
//        }
//    }
//}