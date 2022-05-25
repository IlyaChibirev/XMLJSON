import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        final String xmlFile = "data.xml";
        final String JsonFile = "data.json";

        List<Employee> list = parseXML(xmlFile);
        String jsonXML = (String) listToJson(list);
        writeString(jsonXML, JsonFile);
    }

    public static List<Employee> parseXML(String xmlFileName) {
        try {
            List<Employee> list = new ArrayList<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFileName));

            Node root = doc.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;
                    long id = Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employee.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());
                    list.add(new Employee(id, firstName, lastName, country, age));
                }
            }
            return list;
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static <T> Object listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
