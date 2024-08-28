package com.reservoir.core.gen;

import com.reservoir.core.utils.StringUtils;
import lombok.experimental.FieldNameConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FieldCodeGenerator {

    public static void main(String[] args) throws Exception {
        String packagePath = "com.reservoir.entity"; // 替换为你的包路径
        String outputDir = "com/reservoir/gencode/entity"; // 生成的代码保存目录

        generateCode(packagePath, outputDir);
    }

    public static void generateCode(String packagePath, String outputDir) throws Exception {
        // 创建输出目录
        Files.createDirectories(Paths.get(outputDir));

        // 获取包路径下的所有类文件
        List<File> classFiles = getClassFiles(new File("src/main/java/" + packagePath.replace('.', '/')));

        for (File classFile : classFiles) {
            String classContent = new String(Files.readAllBytes(classFile.toPath()));
            String className = getClassName(classFile, packagePath);

            // 获取类的字段并生成方法
            String generatedMethods = generateMethods(className);

            // 插入生成的代码到最后一个 } 之前
            int lastBraceIndex = classContent.lastIndexOf("}");
            String newClassContent = classContent.substring(0, lastBraceIndex) + generatedMethods + "\n}" + classContent.substring(lastBraceIndex + 1);

            // 保存新的类文件
            Path outputPath = Paths.get(outputDir, classFile.getName());
            Files.write(outputPath, newClassContent.getBytes());
        }
    }

    private static List<File> getClassFiles(File directory) {
        List<File> classFiles = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    classFiles.add(file);
                } else if (file.isDirectory()) {
                    classFiles.addAll(getClassFiles(file));
                }
            }
        }
        return classFiles;
    }

    private static String getClassName(File classFile, String packagePath) {
        String relativePath = classFile.getAbsolutePath().substring(classFile.getAbsolutePath().indexOf(packagePath.replace('.', File.separatorChar)));
        return relativePath.replace(File.separatorChar, '.').replace(".java", "");
    }

    private static String generateMethods(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        StringBuilder methods = new StringBuilder();

        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            String methodName = capitalize(fieldName);

            methods.append("    public ").append("static ").append("String").append(" ").append(methodName).append("() {\n");
            methods.append("        return ").append("\"").append(Camelconvert(fieldName)).append("\";\n");
            methods.append("    }\n\n");
        }

        return methods.toString();
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param camelCaseString 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    private static String Camelconvert(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString;
        }

        StringBuilder snakeCaseString = new StringBuilder();
        char[] chars = camelCaseString.toCharArray();

        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                snakeCaseString.append("_").append(Character.toLowerCase(c));
            } else {
                snakeCaseString.append(c);
            }
        }

        // 如果转换后的字符串以 "_" 开头，则去掉开头的 "_"
        if (snakeCaseString.charAt(0) == '_') {
            snakeCaseString.deleteCharAt(0);
        }

        return snakeCaseString.toString();
    }
}