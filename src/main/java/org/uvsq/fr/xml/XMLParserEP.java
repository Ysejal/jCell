package org.uvsq.fr.xml;

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

public class XMLParserEP {
    int gridWidth, gridHeight, probCellHealthy, probCellInfected, probCellVaccinated;

    public XMLParserEP(String filename)
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

                this.probCellHealthy = Integer.parseInt(
                        elem.getElementsByTagName("probCellHealthy").item(0).getChildNodes().item(0).getNodeValue());

                this.probCellInfected = Integer.parseInt(
                        elem.getElementsByTagName("probCellInfected").item(0).getChildNodes().item(0).getNodeValue());

                this.probCellVaccinated = Integer.parseInt(
                        elem.getElementsByTagName("probCellVaccinated").item(0).getChildNodes().item(0).getNodeValue());

            }

        }
        System.out.println("FINI PARSER EPIDEMIC");

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

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getProbCellHealthy() {
        return probCellHealthy;
    }

    public int getProbCellInfected() {
        return probCellInfected;
    }

    public int getProbCellVaccinated() {
        return probCellVaccinated;
    }
}