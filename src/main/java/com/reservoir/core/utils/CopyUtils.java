package com.reservoir.core.utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CopyUtils {

    /**
     * 浅拷贝对象
     *
     * @param obj  要拷贝的对象
     * @return 拷贝后的对象
     * @throws CloneNotSupportedException 如果对象不支持克隆
     */
    public static Object shallowCopy(Object obj) throws CloneNotSupportedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (obj instanceof Cloneable) {
            return obj.getClass().getMethod("clone").invoke(obj);
        } else {
            throw new CloneNotSupportedException("Object does not implement Cloneable");
        }
    }

    /**
     * 深拷贝对象
     *
     * @param obj  要拷贝的对象
     * @return 拷贝后的对象
     * @throws IOException           如果序列化或反序列化失败
     * @throws ClassNotFoundException 如果反序列化时找不到类
     */
    public static Object deepCopy(Serializable obj) throws IOException, ClassNotFoundException {
        if (obj == null) {
            return null;
        }

        // 序列化对象到字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();

        // 从字节数组反序列化对象
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object copy = ois.readObject();
        ois.close();

        return copy;
    }

    /**
     * 拷贝数组
     *
     * @param <T>   数组元素类型
     * @param array 要拷贝的数组
     * @return 拷贝后的数组
     */
    public static <T> T[] copyArray(T[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.copyOf(array, array.length);
    }

    /**
     * 拷贝数组
     *
     * @param array 要拷贝的数组
     * @param newLength 新数组的长度
     * @return 拷贝后的数组
     */
    public static Object[] copyArray(Object[] array, int newLength) {
        if (array == null) {
            return null;
        }
        return Arrays.copyOf(array, newLength);
    }

    /**
     * 拷贝数组
     *
     * @param src     源数组
     * @param srcPos  源数组开始位置
     * @param dest    目标数组
     * @param destPos 目标数组开始位置
     * @param length  拷贝长度
     */
    public static void copyArray(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }



    /**
     * 浅拷贝集合
     *
     * @param <T>        集合元素类型
     * @param collection 要拷贝的集合
     * @return 拷贝后的集合
     */
    public static <T> List<T> shallowCopyCollection(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        return new ArrayList<>(collection);
    }

    /**
     * 深拷贝集合
     *
     * @param <T>        集合元素类型
     * @param collection 要拷贝的集合
     * @return 拷贝后的集合
     * @throws IOException           如果序列化或反序列化失败
     * @throws ClassNotFoundException 如果反序列化时找不到类
     */
    public static <T extends Serializable> List<T> deepCopyCollection(Collection<T> collection) throws IOException, ClassNotFoundException {
        if (collection == null) {
            return null;
        }

        List<T> copyList = new ArrayList<>();
        for (T item : collection) {
            copyList.add((T) deepCopy(item));
        }
        return copyList;
    }
}
