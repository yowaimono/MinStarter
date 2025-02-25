package com.reservoir.core.gen;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MapperGenerator {

    private static final String ENTITY_PACKAGE = "com.reservoir.entity";
    private static final String MAPPER_PACKAGE = "com.reservoir.mappertest";
    private static final String MAPPER_DIR = "src/main/java/com/reservoir/gencode/mapper";

    public static void main(String[] args) {
        try {
            List<Class<?>> entityClasses = getClasses(ENTITY_PACKAGE);
            generateMappers(entityClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private static void generateMappers(List<Class<?>> entityClasses) {
        for (Class<?> entityClass : entityClasses) {
            String entityName = entityClass.getSimpleName();
            String mapperName = entityName + "Mapper";
            String mapperContent = generateMapperContent(entityClass, mapperName);
            writeMapperFile(mapperName, mapperContent);
        }
    }

    private static String generateMapperContent(Class<?> entityClass, String mapperName) {
        StringBuilder content = new StringBuilder();
        content.append("package ").append(MAPPER_PACKAGE).append(";\n\n");
        content.append("import ").append(entityClass.getName()).append(";\n");
        content.append("import com.baomidou.mybatisplus.core.mapper.BaseMapper;\n");
        content.append("import org.apache.ibatis.annotations.Mapper;\n\n");
        content.append("@Mapper\n");
        content.append("public interface ").append(mapperName).append(" extends BaseMapper<").append(entityClass.getSimpleName()).append("> {\n\n");
        content.append("    // Custom methods for ").append(entityClass.getSimpleName()).append(" entity\n");
        content.append("}\n");
        return content.toString();
    }

    private static void writeMapperFile(String mapperName, String content) {
        String filePath = MAPPER_DIR + "/" + mapperName + ".java";
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}