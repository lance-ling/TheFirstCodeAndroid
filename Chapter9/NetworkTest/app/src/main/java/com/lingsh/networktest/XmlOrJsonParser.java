package com.lingsh.networktest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * XML/JSON数据格式解析
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/25 10:17
 **/


public class XmlOrJsonParser {

    private static final String TAG = "XmlOrJsonParser";
    private static final String APP = "app";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String VERSION = "version";

    static String xmlTestData = "<apps>\n" +
            "    <app>\n" +
            "        <id>1</id>\n" +
            "        <name>Google Maps</name>\n" +
            "        <version>1.0</version>\n" +
            "    </app>\n" +
            "    <app>\n" +
            "        <id>2</id>\n" +
            "        <name>Chrome</name>\n" +
            "        <version>2.1</version>\n" +
            "    </app>\n" +
            "    <app>\n" +
            "        <id>3</id>\n" +
            "        <name>Google Play</name>\n" +
            "        <version>2.3</version>\n" +
            "    </app>\n" +
            "</apps>";

    static String jsonTestData = "[\n" +
            "  {\n" +
            "    \"id\": \"1\",\n" +
            "    \"name\": \"Google Maps\",\n" +
            "    \"version\": \"1.0\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"2\",\n" +
            "    \"name\": \"Chrome\",\n" +
            "    \"version\": \"2.1\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"3\",\n" +
            "    \"name\": \"Google Play\",\n" +
            "    \"version\": \"2.3\"\n" +
            "  }\n" +
            "]";

    /**
     * Pull解析方式
     * <p>
     * 1. 获取XmlPullParserFactory实例 单例
     * 2. 通过XmlPullParserFactory实例得到XmlPullParser对象
     * 3. 调用XmlPullParser对象的setInput方法 填入XML数据进行解析
     * 4. 通过parser对象的getEventType方法得到当前的解析事件
     * 5. 以XmlPullParser.END_DOCUMENT为解析结尾标识
     * * 1. 通过getName()方法得到当前节点的名字
     * * 2. 调用next()方法获取下一个解析事件
     * * 3. 调用nextText()方法获取节点内具体内容
     * * 4. 以XmlPullParser.END_TAG为标识表示开始解析某个节点
     * * 5. 以XmlPullParser.END_TAG为标识完成解析某个节点
     *
     * @param xmlData 被解析的XML数据
     */
    public static void parserXMLByPull(String xmlData) {
        Log.d(TAG, "parserXMLByPull: ");

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlData));
            int eventType = parser.getEventType();
            String nodeName;
            String id = "";
            String name = "";
            String version = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG: {
                        if (ID.equals(nodeName)) {
                            id = parser.nextText();
                        } else if (NAME.equals(nodeName)) {
                            name = parser.nextText();
                        } else if (VERSION.equals(nodeName)) {
                            version = parser.nextText();
                        }
                    }
                    break;
                    case XmlPullParser.END_TAG: {
                        if (APP.equals(nodeName)) {
                            log(id, name, version);
                        }
                    }
                    break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * SAX解析方式
     * <p>
     * 1. 新建一个类继承DefaultHandler 重写父类的方法
     * * 1. startDocument() 开始解析XML
     * * 2. startElement() 开始解析XML某个节点
     * * 3. characters() 获取节点内容
     * * 4. endElement() 完成解析某个节点
     * * 5. endDocument() 完成整个XML解析之后
     * 2. 在自建类内部
     * * 1. 节点名称:String 节点内容:StringBuilder
     * * 2. 在startDocumnet() 初始化节点内容实例
     * * 3. 在startElement() 记录当前节点名称
     * * 4. 在characters() 根据当前的节点名判断将内容添加到哪一个StringBuilder对象
     * * 5. 在endElement() 重置StringBuilder对象
     * 3. 先创建SAXParserFactory对象
     * 4. SAXParserFactory对象.newSAXParser().getXMLReader() 获取XMLReader对象
     * 5. 实例化自建类 置入XMLReader对象的setContentHandler()方法
     * 6. 调用XMLReader对象对parse()方法
     *
     * @param xmlData 被解析的xml数据
     */
    public static void parserXMLBySAX(String xmlData) {
        Log.d(TAG, "parserXMLBySAX: ");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(new ContentHandler());
            reader.parse(new InputSource(new StringReader(xmlData)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在自建类内部
     * * 1. 节点名称:String 节点内容:StringBuilder
     * * 2. 在startDocumnet() 初始化节点内容实例
     * * 3. 在startElement() 记录当前节点名称
     * * 4. 在characters() 根据当前的节点名判断将内容添加到哪一个StringBuilder对象
     * * 5. 在endElement() 重置StringBuilder对象
     */
    static class ContentHandler extends DefaultHandler {

        private String nodeName;
        private StringBuilder id;
        private StringBuilder name;
        private StringBuilder version;

        @Override
        public void startDocument() throws SAXException {
            id = new StringBuilder();
            name = new StringBuilder();
            version = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            nodeName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (ID.equals(nodeName)) {
                id.append(ch, start, length);
            } else if (NAME.equals(nodeName)) {
                name.append(ch, start, length);
            } else if (VERSION.equals(nodeName)) {
                version.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (APP.equals(localName)) {
                log(id.toString().trim(), name.toString().trim(), version.toString().trim());
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }
    }

    /**
     * DOM解析方式
     * <p>
     * 1. 获取DocumentBuilderFactory实例 单例
     * 2. 通过DocumentBuilderFactory实例得到DocumentBuilder对象
     * 3. 调用DocumentBuilder对象的parse()方法进行解析
     * 4. 调用DocumentBuilder对象.getElementByTagName(String) 获得所有指定名称的节点
     * 5. 遍历节点
     * *   1. .item(int) 取得指定位置节点
     * *   2. .item(int).getChildNodes() 获得位置节点的子节点列表
     * *   3. 通过Node.ELEMENT_NODE判断是否属于元素节点
     * *   4. 是则可以强转为Element元素
     * *   5. 通过.getNodeName()判断 再调用.getFirstChild().getNodeValue()方法获取值
     *
     * @param xmlData
     */
    public static void parserXMLByDom(String xmlData) {
        Log.d(TAG, "parserXMLByDom: ");

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            NodeList apps = document.getElementsByTagName(APP);
            int length = apps.getLength();
            String id = "";
            String name = "";
            String version = "";
            for (int i = 0; i < length; i++) {
                Element child = (Element) apps.item(i);
                id = child.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue();
                name = child.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
                version = child.getElementsByTagName(VERSION).item(0).getFirstChild().getNodeValue();
                log(id, name, version);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用JSONObject
     * <p>
     * 1. 将数据传入JSONArray对象中
     * 2. 循环遍历JSONArray对象 取出每个JSONObject对象
     * 3. 通过JSONObject对象的getString()方法取出键值
     *
     * @param jsonData 被解析的JSON数据
     */
    public static void parserJsonByJsonObject(String jsonData) {
        Log.d(TAG, "parserJsonByJsonObject: ");

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            String id;
            String name;
            String version;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                id = jsonObject.getString(ID);
                name = jsonObject.getString(NAME);
                version = jsonObject.getString(VERSION);
                log(id, name, version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void log(String id, String name, String version) {
        Log.d(TAG, String.format("id: %s\tname: %s\tversion: %s\t", id, name, version));
    }

    /**
     * 使用GSON
     * <p>
     * 1. 添加gson依赖
     * 2. 实例化Gson对象
     * *   1. Gson对象.fromJson() 可以将JSON数据直接解析成Bean对象
     * *   2. 借助new TypeToken<List<Bean>>(){}.getType() 可以解析JSON数组成Bean对象列表
     *
     * @param jsonData 被解析的JSON数据
     */
    public static void parserJsonByGson(String jsonData) {
        Gson gson = new Gson();
        List<AppBean> appBeans = (List<AppBean>) gson.fromJson(jsonData,
                new TypeToken<List<AppBean>>() {
                }.getType());
        for (AppBean bean : appBeans) {
            log(bean.getId(), bean.getName(), bean.getVersion());
        }
    }

    class AppBean {
        String id;
        String version;
        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
