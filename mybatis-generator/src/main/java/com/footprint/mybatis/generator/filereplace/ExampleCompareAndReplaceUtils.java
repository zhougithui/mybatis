package com.footprint.mybatis.generator.filereplace;

import com.footprint.constants.CommonConstants;
import com.footprint.utils.FpStringBuilder;
import com.footprint.utils.PathUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * java entity example对比以及替换包名
 * @author hui.zhou 9:29 2018/2/12
 */
public class ExampleCompareAndReplaceUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExampleCompareAndReplaceUtils.class);
    static String MAPPER_CLASSPATH = "mybatis/mapper";

    /**
     * 获取新的文档
     * @return
     */
    private static Map<String, File> parseJavaFile(){
        Map<String, File> docMap = new HashMap<>();

        String docPath = FpStringBuilder.buildDefault()
                .append(PathUtils.rootClassPath().replace("target/classes", CommonConstants.MAIN_RESOURCE_PATH))
                .append(File.separator)
                .append("mybatis").toString();
        Path path = Paths.get(docPath);
        File[] files = path.toFile().listFiles();
        for (File docFile : files){
            if(docFile.isFile() && docFile.getName().contains("Example")){
                docMap.put(docFile.getName(), docFile);
            }
        }
        return docMap;
    }

    /**
     * 新的内容替换旧的java
     * @param projectPathToReplace 需要替换的工程路径
     *                             例如：D:\worksoft\IntelliJIDEA\workspace\lanmao-ec\lanmao-ec\lanmao-ec-parent\lanmao-ec-service
     */
    public static void replaceExampleFile(String projectPathToReplace){
        Map<String, File> newJavaFileMap = parseJavaFile();
        logger.info("新的example文件数量为，{}", newJavaFileMap.size());
        String path = FpStringBuilder.buildDefault()
                .append(projectPathToReplace)
                .append(File.separator)
                .append(CommonConstants.MAIN_RESOURCE_PATH)
                .append(File.separator)
                .append(MAPPER_CLASSPATH).toString();
        File dir = Paths.get(path).toFile();
        File[] docs = dir.listFiles();
        for(File docFile : docs){
            File javaFile = newJavaFileMap.get(docFile.getName().replace("Mapper.xml", "Example.java"));
            if(Objects.nonNull(javaFile)){
                Document doc = getDocument(docFile);
                for(Element element : doc.getRootElement().elements()){
                    if("BaseResultMap".equals(element.attribute("id").getValue())){
                        String packageName = element.attribute("type").getValue() + "Example";
                        String packageLine = "package " + packageName.substring(0, packageName.lastIndexOf(".")) + ";";

                        //替换包名
                        FpStringBuilder sb = FpStringBuilder.buildDefault()
                                .append(projectPathToReplace)
                                .append(File.separator)
                                .append(CommonConstants.MAIN_SRC_PATH)
                                .append(File.separator)
                                .append(packageName.replace(".", File.separator))
                                .append(CommonConstants.JAVA_SUFFIX);
                        String javaFilePath =  sb.toString();
                        try {
                            Files.delete(Paths.get(javaFilePath));

                            File file = new File(javaFilePath);
                            logger.info("替换example文件，{}", packageName);
                            FileWriter fileWriter = new FileWriter(file);
                            BufferedWriter writer = new BufferedWriter(fileWriter);

                            FileReader fileReader = new FileReader(javaFile);
                            BufferedReader reader = new BufferedReader(fileReader);

                            String data = reader.readLine();
                            while (Objects.nonNull(data)){
                                if(data.startsWith("package")){
                                    writer.write(packageLine);
                                }else{
                                    writer.write(data);
                                }

                                data = reader.readLine();
                                if(Objects.nonNull(data)){
                                    writer.newLine();
                                }
                            }

                            writer.flush();

                            writer.close();
                            fileWriter.close();

                            reader.close();
                            fileReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    private static Document getDocument(File docFile) {
        //创建解析器
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(docFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }

}
