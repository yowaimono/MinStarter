package com.reservoir.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {




    /**
     * 获取类的所有字段（包括继承的字段）
     *
     * @param clazz 目标类
     * @return 字段列表
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    /**
     * 获取类的所有方法（包括继承的方法）
     *
     * @param clazz 目标类
     * @return 方法列表
     */
    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                methods.add(method);
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    /**
     * 获取类的所有注解
     *
     * @param clazz 目标类
     * @return 注解列表
     */
    public static List<Annotation> getClassAnnotations(Class<?> clazz) {
        List<Annotation> annotations = new ArrayList<>();
        for (Annotation annotation : clazz.getAnnotations()) {
            annotations.add(annotation);
        }
        return annotations;
    }

    /**
     * 获取字段上的所有注解
     *
     * @param field 目标字段
     * @return 注解列表
     */
    public static List<Annotation> getFieldAnnotations(Field field) {
        List<Annotation> annotations = new ArrayList<>();
        for (Annotation annotation : field.getAnnotations()) {
            annotations.add(annotation);
        }
        return annotations;
    }

    /**
     * 获取方法上的所有注解
     *
     * @param method 目标方法
     * @return 注解列表
     */
    public static List<Annotation> getMethodAnnotations(Method method) {
        List<Annotation> annotations = new ArrayList<>();
        for (Annotation annotation : method.getAnnotations()) {
            annotations.add(annotation);
        }
        return annotations;
    }

    /**
     * 判断类是否具有某个注解
     *
     * @param clazz     目标类
     * @param annotationClass 注解类
     * @return 是否具有该注解
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    /**
     * 判断字段是否具有某个注解
     *
     * @param field     目标字段
     * @param annotationClass 注解类
     * @return 是否具有该注解
     */
    public static boolean hasAnnotation(Field field, Class<? extends Annotation> annotationClass) {
        return field.isAnnotationPresent(annotationClass);
    }

    /**
     * 判断方法是否具有某个注解
     *
     * @param method     目标方法
     * @param annotationClass 注解类
     * @return 是否具有该注解
     */
    public static boolean hasAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }
    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    public static List<Class<?>> getClassList(String packageName) throws IOException, ClassNotFoundException {
        // 第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        // 循环迭代下去
        while (dirs.hasMoreElements()) {
            // 获取下一个元素
            URL url = dirs.nextElement();
            // 得到协议的名称
            String protocol = url.getProtocol();
            // 如果是以文件的形式保存在服务器上
            if ("file".equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                //以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
            } else if ("jar".equals(protocol)) {
                // 如果是jar包文件
                // 定义一个JarFile
                JarFile jar;
                // 获取jar
                jar = ((JarURLConnection) url.openConnection()).getJarFile();
                // 从此jar包 得到一个枚举类
                Enumeration<JarEntry> entries = jar.entries();
                // 同样的进行循环迭代
                while (entries.hasMoreElements()) {
                    // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                    JarEntry jarEntry = entries.nextElement();
                    String jarEntryName = jarEntry.getName();

                    // 如果是以/开头的
                    if (jarEntryName.charAt(0) == '/') {
                        // 获取后面的字符串
                        jarEntryName = jarEntryName.substring(1);
                    }
                    // 如果前半部分和定义的包名相同
                    if (jarEntryName.startsWith(packageDirName)) {
                        int idx = jarEntryName.lastIndexOf('/');
                        // 如果以"/"结尾 是一个包
                        if (idx != -1) {
                            // 获取包名 把"/"替换成"."
                            packageName = jarEntryName.substring(0, idx).replace('/', '.');
                        }
                        // 如果可以迭代下去 并且是一个包
                        if ((idx != -1) || recursive) {
                            // 如果是一个.class文件 而且不是目录
                            if (jarEntryName.endsWith(".class") && !jarEntry.isDirectory()) {
                                // 去掉后面的".class" 获取真正的类名
                                String className = jarEntryName.substring(packageName.length() + 1, jarEntryName
                                        .length() - 6);
                                // 添加到classes
                                classes.add(Class.forName(packageName + '.' + className));
                            }
                        }
                    }
                }
            }
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean
            recursive, List<Class<?>> classes) throws ClassNotFoundException {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        if (dirFiles == null || dirFiles.length == 0) {
            return;
        }
        //循环所有文件
        for (File file : dirFiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(),
                        recursive, classes);
            } else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                //添加到集合中去
                classes.add(Class.forName(packageName + '.' + className));
            }
        }
    }

}