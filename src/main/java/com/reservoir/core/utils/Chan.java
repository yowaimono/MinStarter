package com.reservoir.core.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;

public class Chan<T> {
    private final BlockingQueue<T> queue;
    private volatile boolean closed = false;

    public Chan(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }

    // 发送数据到 chan
    public void send(T item) throws InterruptedException {
        if (!closed) {
            queue.put(item);
        } else {
            throw new IllegalStateException("Channel is closed");
        }
    }

    // 从 chan 接收数据
    public T recv() throws InterruptedException {
        return queue.take();
    }

    // 关闭 chan
    public void close() {
        closed = true;
        try {
            queue.put(null); // 假设 null 不是一个有效的数据项
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Chan<String> chan = new Chan<>(10);

        // 发送线程
        CompletableFuture.runAsync(() -> {
            try {
                chan.send("Hello");
                chan.send("World");
                chan.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 接收线程
        CompletableFuture.runAsync(() -> {
            try {
                String item;
                while ((item = chan.recv()) != null) {
                    System.out.println(item);
                }
                System.out.println("Chan closed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 确保主线程等待一段时间，以便任务完成
        Thread.sleep(1000);
    }
}