package com.reservoir.core.gen;

import com.reservoir.core.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CodeGenerator {

    private static final String BASE_PACKAGE = "com.reservoir";
    private static final String GEN_PKG = "admin";
    private static final String ENTITY_PACKAGE = BASE_PACKAGE + ".entity." + GEN_PKG;

    private static final String MAPPER_PACKAGE = BASE_PACKAGE + ".gencode."+ GEN_PKG +  ".mapper";
    private static final String SERVICE_PACKAGE = BASE_PACKAGE + ".gencode."+ GEN_PKG +  ".service";
    private static final String SERVICE_IMPL_PACKAGE = BASE_PACKAGE + ".gencode."+ GEN_PKG +  ".service.impl";
    private static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".gencode."+ GEN_PKG +  ".controller";
    private static final String ENTITY_GEN_PACKAGE = BASE_PACKAGE + ".gencode."+ GEN_PKG +  ".entity";
    private static final String CORE_ENTITY_PACKAGE = BASE_PACKAGE + ".core."+ GEN_PKG +  ".entity";

    private static final String MAPPER_DIR = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/gencode/" + GEN_PKG+ "/mapper";
    private static final String SERVICE_DIR = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/gencode/" + GEN_PKG+ "/service";
    private static final String SERVICE_IMPL_DIR = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/gencode/" + GEN_PKG+ "/service/impl";
    private static final String CONTROLLER_DIR = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/gencode/" + GEN_PKG+ "/controller";
    private static final String ENTITY_DIR = "src/main/java/" + BASE_PACKAGE.replace('.', '/') + "/gencode/" + GEN_PKG+ "/entity";

    // 定义全局变量，包含常用的导入语句
    private static final String COMMON_IMPORTS =
            "import jakarta.annotation.Resource;\n" +
                    "import io.swagger.v3.oas.annotations.Operation;\n" +
                    "import com.reservoir.core.entity.Result;\n" +
            "import java.time.LocalDateTime;" +
            "import java.time.LocalDate;";

    public static void main(String[] args) {
        try {
            List<Class<?>> entityClasses = getClasses(ENTITY_PACKAGE);
            generateCode(entityClasses);
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

    private static void generateCode(List<Class<?>> entityClasses) throws Exception {
        for (Class<?> entityClass : entityClasses) {
            String entityName = entityClass.getSimpleName();
            String mapperName = entityName + "Mapper";
            String serviceName = entityName + "Service";
            String serviceImplName = entityName + "ServiceImpl";
            String controllerName = entityName + "Controller";

            String mapperContent = generateMapperContent(entityClass, mapperName);
            String serviceContent = generateServiceContent(entityClass, serviceName);
            String serviceImplContent = generateServiceImplContent(entityClass, serviceImplName, mapperName, serviceName);
            String controllerContent = generateControllerContent(entityClass, controllerName, serviceName);

            writeFile(MAPPER_DIR, mapperName, mapperContent);
            writeFile(SERVICE_DIR, serviceName, serviceContent);
            writeFile(SERVICE_IMPL_DIR, serviceImplName, serviceImplContent);
            writeFile(CONTROLLER_DIR, controllerName, controllerContent);
            generateCode(ENTITY_PACKAGE, ENTITY_DIR);
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

    private static String generateServiceContent(Class<?> entityClass, String serviceName) {
        StringBuilder content = new StringBuilder();
        String entityName = entityClass.getSimpleName();
        content.append("package ").append(SERVICE_PACKAGE).append(";\n\n");
        content.append(COMMON_IMPORTS).append("\n");
        content.append("import ").append(entityClass.getName()).append(";\n");
        content.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n");
        content.append("import java.util.List;\n\n");
        content.append("public interface ").append(serviceName).append(" {\n\n");
        content.append("    Result<?>").append(" getAll();\n");
        content.append("    ").append("Result<?>").append(" getById(Long id);\n");
        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            if (!fieldName.equals("id") && !fieldName.equals("serialVersionUID")) {
                content.append("    Result<?> getBy").append(capitalize(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(");\n");
            }
        }
        content.append("    Result<?> page(int pageNum, int pageSize);\n");
        content.append("    Result<?> save(").append(entityName).append(" ").append(entityName.toLowerCase()).append(");\n");
        content.append("    Result<?> update(").append(entityName).append(" ").append(entityName.toLowerCase()).append(");\n");
        content.append("    Result<?> delete(Long id);\n");
        content.append("}\n");
        return content.toString();
    }

    private static String generateServiceImplContent(Class<?> entityClass, String serviceImplName, String mapperName, String serviceName) {
        StringBuilder content = new StringBuilder();
        String entityName = entityClass.getSimpleName();
        content.append("package ").append(SERVICE_IMPL_PACKAGE).append(";\n\n");
        content.append(COMMON_IMPORTS).append("\n");
        content.append("import ").append(entityClass.getName()).append(";\n");
        content.append("import ").append(MAPPER_PACKAGE).append(".").append(mapperName).append(";\n");
        content.append("import ").append(SERVICE_PACKAGE).append(".").append(serviceName).append(";\n");
        content.append("import org.springframework.stereotype.Service;\n");
        content.append("import java.util.List;\n");
        content.append("import org.slf4j.Logger;\n");
        content.append("import org.slf4j.LoggerFactory;\n");
        content.append("import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;\n");
        content.append("import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n\n");
        content.append("@Service\n");
        content.append("public class ").append(serviceImplName).append(" implements ").append(serviceName).append(" {\n\n");
        content.append("    private static final Logger logger = LoggerFactory.getLogger(").append(serviceImplName).append(".class);\n\n");
        content.append("    @Resource\n");
        content.append("    private ").append(mapperName).append(" ").append(mapperName.toLowerCase()).append(";\n\n");
        content.append("    @Override\n");
        content.append("    public Result<?>").append(" getAll() {\n");
        content.append("        try {\n");
        content.append("            List<").append(entityName).append("> entities = ").append(mapperName.toLowerCase()).append(".selectList(new QueryWrapper<>());\n");
        content.append("            return ").append("Result.success(entities);\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Get all ").append(entityName).append(" failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Get all ").append(entityName).append(" failed\");\n");
        content.append("        }\n");
        content.append("    }\n\n");
        content.append("    @Override\n");
        content.append("    public ").append("Result<?>").append(" getById(Long id) {\n");
        content.append("        try {\n");
        content.append("            ").append(entityName).append(" ").append(entityName.toLowerCase()).append(" = ").append(mapperName.toLowerCase()).append(".selectById(id);\n");
        content.append("            if (").append(entityName.toLowerCase()).append(" == null) {\n");
        content.append("                return ").append("Result.error(404, \"").append(entityName).append(" not found\");\n");
        content.append("            }\n");
        content.append("            return ").append("Result.success(").append(entityName.toLowerCase()).append(");\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Get ").append(entityName).append(" by id failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Get ").append(entityName).append(" by id failed\");\n");
        content.append("        }\n");
        content.append("    }\n\n");
        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            if (!fieldName.equals("id") && !fieldName.equals("serialVersionUID")) {
                content.append("    @Override\n");
                content.append("    public Result<?> getBy").append(capitalize(fieldName)).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n");
                content.append("        try {\n");
                content.append("            QueryWrapper<").append(entityName).append("> queryWrapper = new QueryWrapper<>();\n");
                content.append("            queryWrapper.eq(\"").append(fieldName).append("\", ").append(fieldName).append(");\n");
                content.append("            List<").append(entityName).append("> entities = ").append(mapperName.toLowerCase()).append(".selectList(queryWrapper);\n");
                content.append("            return ").append("Result.success(entities);\n");
                content.append("        } catch (Exception e) {\n");
                content.append("            logger.error(\"Get ").append(entityName).append(" by ").append(fieldName).append(" failed: \", e);\n");
                content.append("            return ").append("Result.error(501, \"Get ").append(entityName).append(" by ").append(fieldName).append(" failed\");\n");
                content.append("        }\n");
                content.append("    }\n\n");
            }
        }
        content.append("    @Override\n");
        content.append("    public Result<?> page(int pageNum, int pageSize) {\n");
        content.append("        try {\n");
        content.append("            Page<").append(entityName).append("> page = new Page<>(pageNum, pageSize);\n");
        content.append("            Page<").append(entityName).append("> pageResult = ").append(mapperName.toLowerCase()).append(".selectPage(page, new QueryWrapper<>());\n");
        content.append("            return ").append("Result.success(pageResult);\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Page query ").append(entityName).append(" failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Page query ").append(entityName).append(" failed\");\n");
        content.append("        }\n");
        content.append("    }\n\n");
        content.append("    @Override\n");
        content.append("    public Result<?> save(").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        content.append("        try {\n");
        content.append("            QueryWrapper<").append(entityName).append("> queryWrapper = new QueryWrapper<>();\n");
        content.append("            queryWrapper.eq(\"id\", ").append(entityName.toLowerCase()).append(".getId());\n");
        content.append("            if (").append(mapperName.toLowerCase()).append(".selectCount(queryWrapper) > 0) {\n");
        content.append("                return ").append("Result.error(409, \"").append(entityName).append(" already exists\");\n");
        content.append("            }\n");
        content.append("            ").append(mapperName.toLowerCase()).append(".insert(").append(entityName.toLowerCase()).append(");\n");
        content.append("            return ").append("Result.success(\"save success!\");\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Save ").append(entityName).append(" failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Save ").append(entityName).append(" failed\");\n");
        content.append("        }\n");
        content.append("    }\n\n");
        content.append("    @Override\n");
        content.append("    public Result<?> update(").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        content.append("        try {\n");
        content.append("            ").append(entityName).append(" existing").append(entityName).append(" = ").append(mapperName.toLowerCase()).append(".selectById(").append(entityName.toLowerCase()).append(".getId());\n");
        content.append("            if (existing").append(entityName).append(" == null) {\n");
        content.append("                return ").append("Result.error(404, \"").append(entityName).append(" not found\");\n");
        content.append("            }\n");
        content.append("            ").append(mapperName.toLowerCase()).append(".updateById(").append(entityName.toLowerCase()).append(");\n");
        content.append("            return ").append("Result.success(\"update success\");\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Update ").append(entityName).append(" failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Update ").append(entityName).append(" failed\");\n");
        content.append("        }\n");
        content.append("    }\n\n");
        content.append("    @Override\n");
        content.append("    public Result<?> delete(Long id) {\n");
        content.append("        try {\n");
        content.append("            ").append(entityName).append(" existing").append(entityName).append(" = ").append(mapperName.toLowerCase()).append(".selectById(id);\n");
        content.append("            if (existing").append(entityName).append(" == null) {\n");
        content.append("                return ").append("Result.error(404, \"").append(entityName).append(" not found\");\n");
        content.append("            }\n");
        content.append("            ").append(mapperName.toLowerCase()).append(".deleteById(id);\n");
        content.append("            return ").append("Result.success(\"delete success\");\n");
        content.append("        } catch (Exception e) {\n");
        content.append("            logger.error(\"Delete ").append(entityName).append(" failed: \", e);\n");
        content.append("            return ").append("Result.error(501, \"Delete ").append(entityName).append(" failed\");\n");
        content.append("        }\n");
        content.append("    }\n");
        content.append("}\n");
        return content.toString();
    }

    private static String generateControllerContent(Class<?> entityClass, String controllerName, String serviceName) {
        StringBuilder content = new StringBuilder();
        String entityName = entityClass.getSimpleName();
        content.append("package ").append(CONTROLLER_PACKAGE).append(";\n\n");
        content.append(COMMON_IMPORTS).append("\n");
        content.append("import ").append(entityClass.getName()).append(";\n");
        content.append("import ").append(SERVICE_PACKAGE).append(".").append(serviceName).append(";\n");
        content.append("import org.springframework.web.bind.annotation.*;\n");
        content.append("import io.swagger.v3.oas.annotations.tags.Tag;\n");
        content.append("import java.util.List;\n\n");
        content.append("@RestController\n");
        content.append("@RequestMapping(\"/").append(entityName.toLowerCase()).append("\")\n");
        content.append("@Tag(name = \"").append(entityName).append(" API\")\n");
        content.append("public class ").append(controllerName).append(" {\n\n");
        content.append("    @Resource\n");
        content.append("    private ").append(serviceName).append(" ").append(serviceName.toLowerCase()).append(";\n\n");
        content.append("    @GetMapping(value = \"getAll\")\n");
        content.append("    @Operation(summary = \"获取所有").append(entityName).append("\", description = \"获取所有").append(entityName).append("记录\")\n");
        content.append("    public Result<?> ").append(" getAll() {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".getAll();\n");
        content.append("    }\n\n");
        content.append("    @GetMapping(value = \"/getById/{id}\")\n");
        content.append("    @Operation(summary = \"根据ID获取").append(entityName).append("\", description = \"根据ID获取").append(entityName).append("记录\")\n");
        content.append("    public ").append("Result<?>").append(" getById(@PathVariable Long id) {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".getById(id);\n");
        content.append("    }\n\n");
        for (Field field : entityClass.getDeclaredFields()) {
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            if (!fieldName.equals("id") && !fieldName.equals("serialVersionUID")) {
                content.append("    @GetMapping(value = \"/getBy").append(capitalize(fieldName)).append("\")\n");
                content.append("    @Operation(summary = \"根据").append(fieldName).append("获取").append(entityName).append("\", description = \"根据").append(fieldName).append("获取").append(entityName).append("记录\")\n");
                content.append("    public Result<?> getBy").append(capitalize(fieldName)).append("(@RequestParam ").append(fieldType).append(" ").append(fieldName).append(") {\n");
                content.append("        return ").append(serviceName.toLowerCase()).append(".getBy").append(capitalize(fieldName)).append("(").append(fieldName).append(");\n");
                content.append("    }\n\n");
            }
        }
        content.append("    @GetMapping(value = \"/page\")\n");
        content.append("    @Operation(summary = \"分页查询").append(entityName).append("\", description = \"分页查询").append(entityName).append("记录\")\n");
        content.append("    public Result<?> page(@RequestParam int pageNum, @RequestParam int pageSize) {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".page(pageNum, pageSize);\n");
        content.append("    }\n\n");
        content.append("    @PostMapping(value = \"/save\")\n");
        content.append("    @Operation(summary = \"保存").append(entityName).append("\", description = \"保存").append(entityName).append("记录\")\n");
        content.append("    public Result<?> save(@RequestBody ").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".save(").append(entityName.toLowerCase()).append(");\n");
        content.append("    }\n\n");
        content.append("    @PutMapping(value = \"/update\")\n");
        content.append("    @Operation(summary = \"更新").append(entityName).append("\", description = \"更新").append(entityName).append("记录\")\n");
        content.append("    public Result<?> update(@RequestBody ").append(entityName).append(" ").append(entityName.toLowerCase()).append(") {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".update(").append(entityName.toLowerCase()).append(");\n");
        content.append("    }\n\n");
        content.append("    @DeleteMapping(value = \"/delete/{id}\")\n");
        content.append("    @Operation(summary = \"删除").append(entityName).append("\", description = \"根据ID删除").append(entityName).append("记录\")\n");
        content.append("    public Result<?> delete(@PathVariable Long id) {\n");
        content.append("        return ").append(serviceName.toLowerCase()).append(".delete(id);\n");
        content.append("    }\n");
        content.append("}\n");
        return content.toString();
    }

    private static void writeFile(String dir, String fileName, String content) {
        String filePath = dir + "/" + fileName + ".java";
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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

        // 导入常用的工具类
        methods.append("import java.time.LocalDate;\n");
        methods.append("import java.time.LocalDateTime;\n");
        methods.append("import java.util.List;\n");
        methods.append("import java.util.ArrayList;\n");
        methods.append("import java.io.File;\n");
        methods.append("import java.io.IOException;\n");
        methods.append("import java.nio.file.Files;\n");
        methods.append("import java.nio.file.Path;\n");
        methods.append("import java.nio.file.Paths;\n");
        methods.append("import java.lang.reflect.Field;\n");
        methods.append("import java.net.URL;\n");
        methods.append("import java.util.Enumeration;\n");
        methods.append("\n");

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