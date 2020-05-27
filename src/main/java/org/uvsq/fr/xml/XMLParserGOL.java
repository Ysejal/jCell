package org.uvsq.fr.xml;

import javafx.scene.paint.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class XMLParserGOL {

    int gridWidth, gridHeight, probCellAlive;
    Color deadColor, aliveColor;

    public XMLParserGOL(String filename)
            throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(getClass().getResource(filename).toString());
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == node.ELEMENT_NODE) {

                Element elem = (Element) node;
                this.gridWidth = Integer.parseInt(
                        elem.getElementsByTagName("gridWidth").item(0).getChildNodes().item(0).getNodeValue());

                this.gridHeight = Integer.parseInt(
                        elem.getElementsByTagName("gridHeight").item(0).getChildNodes().item(0).getNodeValue());

                this.probCellAlive = Integer.parseInt(
                        elem.getElementsByTagName("probCellAlive").item(0).getChildNodes().item(0).getNodeValue());

                this.deadColor = Color
                        .valueOf(elem.getElementsByTagName("deadColor").item(0).getChildNodes().item(0).getNodeValue());

                this.aliveColor = Color.valueOf(
                        elem.getElementsByTagName("aliveColor").item(0).getChildNodes().item(0).getNodeValue());

            }

        }


    }

    public static void createFile(String filename, int height, int width, int probCellAlive) {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("Simulation");
            document.appendChild(root);

            Element config = document.createElement("config");
            root.appendChild(config);

            // GridWidth node
            Element gridWidth = document.createElement("gridWidth");
            gridWidth.appendChild(document.createTextNode("" + width));
            config.appendChild(gridWidth);

            // GridHeight

            Element gridHeight = document.createElement("gridHeight");
            gridHeight.appendChild(document.createTextNode("" + height));
            config.appendChild(gridHeight);

            // ProbCellAlive
            Element prob = document.createElement("probCellAlive");
            prob.appendChild(document.createTextNode("" + probCellAlive));
            config.appendChild(prob);

            // deadColor
            Element dead = document.createElement("deadColor");
            dead.appendChild(document.createTextNode("white"));
            config.appendChild(dead);

            // aliveColor
            Element alive = document.createElement("aliveColor");
            alive.appendChild(document.createTextNode("black"));
            config.appendChild(alive);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // XMLParserGOL.class.getResource().getPath()
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("src/" + filename));

            transformer.transform(domSource, streamResult);

            System.out.println("File created!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int getGridWidth(){
        return gridWidth;
    }

    public int getGridHeight(){
        return gridHeight;
    }

    public int getProbCellAlive(){
        return probCellAlive;
    }

    public Color getAliveColor(){
        return aliveColor;
    }

    public Color getDeadColor(){
        return deadColor;
    }

}