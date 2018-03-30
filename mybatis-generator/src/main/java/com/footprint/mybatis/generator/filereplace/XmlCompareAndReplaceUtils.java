package com.footprint.mybatis.generator.filereplace;

import com.footprint.constants.CommonConstants;
import com.footprint.utils.FpStringBuilder;
import com.footprint.utils.PathUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultCDATA;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文档对比以及替换
 * @author hui.zhou 9:29 2018/2/12
 */
public class XmlCompareAndReplaceUtils {
    private static final Logger logger = LoggerFactory.getLogger(XmlCompareAndReplaceUtils.class);

    static String MAPPER_CLASSPATH = "mybatis/mapper";
    /**
     * 获取新的文档
     * @return
     */
    private static Map<String, Document> parseDocument(){
        logger.info("获取新的mapper文件map-start");
        Map<String, Document> docMap = new HashMap<>();

        String docPath = FpStringBuilder.buildDefault()
                .append(PathUtils.rootClassPath().replace("target/classes", CommonConstants.MAIN_RESOURCE_PATH))
                .append(File.separator)
                .append(MAPPER_CLASSPATH).toString();
        Path path = Paths.get(docPath);
        File[] files = path.toFile().listFiles();
        for (File docFile : files){
            docMap.put(docFile.getName(), getDocument(docFile));
        }
        logger.info("获取新的mapper文件map-end");
        return docMap;
    }

    /**
     * 新的内容替换旧的xml
     * @param projectPathToReplace 需要替换的工程路径
     *                             例如：D:\worksoft\IntelliJIDEA\workspace\lanmao-ec\lanmao-ec\lanmao-ec-parent\lanmao-ec-service
     */
    public static void replaceDoc(String projectPathToReplace){
        Map<String, Document> newDocMap = parseDocument();
        logger.info("新的mapper文件数量为，{}", newDocMap.size());
        String path = FpStringBuilder.buildDefault()
                .append(projectPathToReplace)
                .append(File.separator)
                .append(CommonConstants.MAIN_RESOURCE_PATH)
                .append(File.separator)
                .append(MAPPER_CLASSPATH).toString();
        File dir = Paths.get(path).toFile();
        File[] docs = dir.listFiles();
        for(File docFile : docs){
            Document doc = newDocMap.get(docFile.getName());
            if(Objects.nonNull(doc)){
                try (FileWriter fileWriter = new FileWriter(new File(docFile.getAbsolutePath() + ".temp"))) {
                    //对比两个document，把新增的方法移至新的xml中
                    try {
                        logger.info("生成新的mapper文件并替换,{}", docFile.getName());
                        compareAndReplace(getDocument(docFile), doc);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("mapper替换异常");
                        continue;
                    }

                    //转化成mybatis的Document对象
                    org.mybatis.generator.api.dom.xml.Document mybatisDoc = convertToMybatisDoc(doc);
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write(mybatisDoc.getFormattedContent());
                    writer.flush();
                    writer.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new File(docFile.getAbsolutePath()).delete();
                new File(docFile.getAbsolutePath() + ".temp").renameTo(docFile);
            }
        }
    }

    private static org.mybatis.generator.api.dom.xml.Document convertToMybatisDoc(Document doc) {
        //拷贝文档类型
        DocumentType docType = doc.getDocType();
        org.mybatis.generator.api.dom.xml.Document mybatisDoc
                = new org.mybatis.generator.api.dom.xml.Document(docType.getPublicID(), docType.getSystemID());

        //拷贝根节点
        Element rootElement = doc.getRootElement();
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = rootElement.attributeValue("namespace");
        answer.addAttribute(new Attribute("namespace", namespace));
        mybatisDoc.setRootElement(answer);

        //拷贝子节点
        rootElement.elements().forEach(element -> {
            org.mybatis.generator.api.dom.xml.XmlElement mybatisEle = new XmlElement(element.getName());
            //属性拷贝
            element.attributes().forEach(attribute -> {
                Attribute attr = new Attribute(attribute.getName(), attribute.getValue());
                mybatisEle.addAttribute(attr);
            });

            answer.addElement(mybatisEle);
            //子元素拷贝
            copySubEle(element, mybatisEle);
        });

        return mybatisDoc;
    }

    /**
     * 递归拷贝属性
     * @param element
     * @param mybatisEle
     */
    private static void copySubEle(Element element, XmlElement mybatisEle) {
        element.content().forEach(node -> {
            if(node instanceof Comment){
                return;
            }
            if(node instanceof Text){
                if(!node.getText().trim().equalsIgnoreCase("")){
                    String text = node.getText().trim();
                    if(text.contains(">")){
                        text = text.replaceAll(">", "&gt;");
                    }else if(text.contains("<")){
                        text = text.replaceAll("<", "&lt;");
                    }
                    mybatisEle.addElement(new TextElement(text));
                }
            }else if(node instanceof DefaultCDATA){
                DefaultCDATA cdata = (DefaultCDATA) node;
                mybatisEle.addElement(new TextElement("<![CDATA[" + cdata.getText() + "]]>"));
            }else{
                Element ele = (Element) node;
                XmlElement subMybatisEle = new XmlElement(node.getName());
                ele.attributes().forEach(attribute -> {
                    Attribute attr = new Attribute(attribute.getName(), attribute.getValue());
                    subMybatisEle.addAttribute(attr);
                });

                mybatisEle.addElement(subMybatisEle);
                copySubEle(ele, subMybatisEle);
            }
        });
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

    /**
     * 比较，移动
     * @param src
     * @param desc
     */
    private static void compareAndReplace(Document src, Document desc) {
        Element rootEleSrc = src.getRootElement();
        Element rootEleDesc = desc.getRootElement();
        //替换属性
        rootEleDesc.setAttributes(rootEleSrc.attributes());

        List<Element> elements = rootEleDesc.elements();
        Map<String, Element> idElementMapDesc = new HashMap<>();
        elements.forEach(element -> idElementMapDesc.put(element.attributeValue("id"), element));

        Map<String, Element> idElementMapSrc = new HashMap<>();
        rootEleSrc.elements().forEach(element -> idElementMapSrc.put(element.attributeValue("id"), element));

        //替换子元素属性
        rootEleDesc.elements().forEach(element -> {
            element.setAttributes(idElementMapSrc.get(element.attribute("id").getValue()).attributes());
        });

        //相同element属性覆盖
        idElementMapDesc.forEach((id, ele) -> {
            if(Objects.nonNull(idElementMapDesc.get(id))) {
                ele.setAttributes(idElementMapSrc.get(id).attributes());
            }
        });

        //新增元素移动
        idElementMapSrc.forEach((id, ele) -> {
            if(Objects.isNull(idElementMapDesc.get(id))){
                ele.setParent(null);
                rootEleDesc.add(ele);
            }
        });
    }

    public static void main(String[] args){
        replaceDoc("D:\\worksoft\\IntelliJIDEA\\workspace\\lanmao-ec\\lanmao-ec\\lanmao-ec-parent\\lanmao-ec-service");
    }
}
