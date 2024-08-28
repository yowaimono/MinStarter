package com.reservoir.core.gen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapperGenCode {

    private static final String BASE_PACKAGE = "com.reservoir";
    private static final String ENTITY_PACKAGE = BASE_PACKAGE + ".entity";
    private static final String VO_PACKAGE = BASE_PACKAGE + ".vo";
    private static final String MAPPER_OUTPUT_DIR = "src/main/java/com/reservoir/gencode/mapstruct";

    public static void main(String[] args) {
        try {
            List<Class<?>> entityClasses = getClasses(ENTITY_PACKAGE);
            List<Class<?>> voClasses = getClasses(VO_PACKAGE);

            generateMapperUtils(entityClasses, voClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            for (File file : directory.listFiles()) {
                if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    private static boolean matches(Class<?> entity, Class<?> vo) {
        for (Field voField : vo.getDeclaredFields()) {
            boolean found = false;
            for (Field entityField : entity.getDeclaredFields()) {
                if (voField.getName().equals(entityField.getName()) &&
                        voField.getType().equals(entityField.getType())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private static void generateMapperUtils(List<Class<?>> entityClasses, List<Class<?>> voClasses) throws IOException, ClassNotFoundException {
        String mapperPackage = BASE_PACKAGE + ".gencode.mapstruct";
        String mapperClassName = "MapperUtils";
        String fileName = MAPPER_OUTPUT_DIR + "/" + mapperClassName + ".java";

        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();

        Set<String> importedClasses = new HashSet<>();
        StringBuilder codeBuilder = new StringBuilder();
        StringBuilder importBuilder = new StringBuilder();

        codeBuilder.append("package " + mapperPackage + ";\n\n");
        codeBuilder.append("import java.util.ArrayList;\n" +
                "import java.util.List;\n\n");
        codeBuilder.append("public class " + mapperClassName + " {\n\n");

        for (Class<?> entityClass : entityClasses) {
            String entityClassName = entityClass.getSimpleName();

            for (Class<?> voClass : voClasses) {
                if (matches(entityClass, voClass)) {
                    generateMappingMethods(codeBuilder, entityClassName, voClass.getSimpleName(), importedClasses, importBuilder);
                }
            }
        }

        codeBuilder.append("\n\n}");

        // Insert imports at the beginning
        codeBuilder.insert(codeBuilder.indexOf("\n") + 1, importBuilder.toString());

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(codeBuilder.toString());
        }
    }

    private static void generateMappingMethods(StringBuilder codeBuilder, String entityClassName, String voClassName, Set<String> importedClasses, StringBuilder importBuilder) throws IOException, ClassNotFoundException {
        String entityFullClassName = ENTITY_PACKAGE + "." + entityClassName;
        String voFullClassName = VO_PACKAGE + "." + voClassName;

        if (!importedClasses.contains(entityFullClassName)) {
            importBuilder.append("import " + entityFullClassName + ";\n");
            importedClasses.add(entityFullClassName);
        }

        if (!importedClasses.contains(voFullClassName)) {
            importBuilder.append("import " + voFullClassName + ";\n");
            importedClasses.add(voFullClassName);
        }

        codeBuilder.append("\n");

        // Single object mapping
        codeBuilder.append("    /**\n");
        codeBuilder.append("     * Converts a " + entityClassName + " to a " + voClassName + ".\n");
        codeBuilder.append("     * @param " + entityClassName.toLowerCase() + " the " + entityClassName + " to convert\n");
        codeBuilder.append("     * @return the converted " + voClassName + "\n");
        codeBuilder.append("     */\n");
        codeBuilder.append("    public static " + voClassName + " " + capitalize(entityClassName) + "To" + voClassName + "(" + entityClassName + " " + entityClassName.toLowerCase() + ") {\n");
        codeBuilder.append("        " + voClassName + " vo = new " + voClassName + "();\n");
        for (Field entityField : Class.forName(entityFullClassName).getDeclaredFields()) {
            try {
                Field voField = Class.forName(voFullClassName).getDeclaredField(entityField.getName());
                codeBuilder.append("        vo.set" + capitalize(entityField.getName()) + "(" + entityClassName.toLowerCase() + ".get" + capitalize(entityField.getName()) + "());\n");
            } catch (NoSuchFieldException e) {
                // Ignore fields that do not exist in the target class
            }
        }
        codeBuilder.append("        return vo;\n");
        codeBuilder.append("    }\n\n");

        codeBuilder.append("    /**\n");
        codeBuilder.append("     * Converts a " + voClassName + " to a " + entityClassName + ".\n");
        codeBuilder.append("     * @param " + voClassName.toLowerCase() + " the " + voClassName + " to convert\n");
        codeBuilder.append("     * @return the converted " + entityClassName + "\n");
        codeBuilder.append("     */\n");
        codeBuilder.append("    public static " + entityClassName + " " + capitalize(voClassName) + "To" + entityClassName + "(" + voClassName + " " + voClassName.toLowerCase() + ") {\n");
        codeBuilder.append("        " + entityClassName + " entity = new " + entityClassName + "();\n");
        for (Field voField : Class.forName(voFullClassName).getDeclaredFields()) {
            try {
                Field entityField = Class.forName(entityFullClassName).getDeclaredField(voField.getName());
                codeBuilder.append("        entity.set" + capitalize(voField.getName()) + "(" + voClassName.toLowerCase() + ".get" + capitalize(voField.getName()) + "());\n");
            } catch (NoSuchFieldException e) {
                // Ignore fields that do not exist in the target class
            }
        }
        codeBuilder.append("        return entity;\n");
        codeBuilder.append("    }\n\n");

        // List mapping
        codeBuilder.append("    /**\n");
        codeBuilder.append("     * Converts a list of " + entityClassName + " to a list of " + voClassName + ".\n");
        codeBuilder.append("     * @param " + entityClassName.toLowerCase() + "List the list of " + entityClassName + " to convert\n");
        codeBuilder.append("     * @return the list of converted " + voClassName + "\n");
        codeBuilder.append("     */\n");
        codeBuilder.append("    public static List<" + voClassName + "> " + capitalize(entityClassName) + "To" + voClassName + "(List<" + entityClassName + "> " + entityClassName.toLowerCase() + "List) {\n");
        codeBuilder.append("        if (" + entityClassName.toLowerCase() + "List == null) {\n");
        codeBuilder.append("            return null;\n");
        codeBuilder.append("        }\n");
        codeBuilder.append("        List<" + voClassName + "> voList = new ArrayList<>();\n");
        codeBuilder.append("        for (" + entityClassName + " " + entityClassName.toLowerCase() + " : " + entityClassName.toLowerCase() + "List) {\n");
        codeBuilder.append("            voList.add(" + capitalize(entityClassName) + "To" + voClassName + "(" + entityClassName.toLowerCase() + "));\n");
        codeBuilder.append("        }\n");
        codeBuilder.append("        return voList;\n");
        codeBuilder.append("    }\n\n");

        codeBuilder.append("    /**\n");
        codeBuilder.append("     * Converts a list of " + voClassName + " to a list of " + entityClassName + ".\n");
        codeBuilder.append("     * @param " + voClassName.toLowerCase() + "List the list of " + voClassName + " to convert\n");
        codeBuilder.append("     * @return the list of converted " + entityClassName + "\n");
        codeBuilder.append("     */\n");
        codeBuilder.append("    public static List<" + entityClassName + "> " + capitalize(voClassName) + "To" + entityClassName + "(List<" + voClassName + "> " + voClassName.toLowerCase() + "List) {\n");
        codeBuilder.append("        if (" + voClassName.toLowerCase() + "List == null) {\n");
        codeBuilder.append("            return null;\n");
        codeBuilder.append("        }\n");
        codeBuilder.append("        List<" + entityClassName + "> entityList = new ArrayList<>();\n");
        codeBuilder.append("        for (" + voClassName + " " + voClassName.toLowerCase() + " : " + voClassName.toLowerCase() + "List) {\n");
        codeBuilder.append("            entityList.add(" + capitalize(voClassName) + "To" + entityClassName + "(" + voClassName.toLowerCase() + "));\n");
        codeBuilder.append("        }\n");
        codeBuilder.append("        return entityList;\n");
        codeBuilder.append("    }\n\n");
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}