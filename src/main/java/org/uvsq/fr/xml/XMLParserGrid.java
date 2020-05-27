package org.uvsq.fr.xml;

import org.uvsq.fr.simulation.Cell;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XMLParserGrid {

    public Cell[][] cellGrid;

    public XMLParserGrid(String filename, int height, int width)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(getClass().getResource(filename).toString());
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        cellGrid = new Cell[height][width];

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == node.ELEMENT_NODE) {

                Element elem = (Element) node;

                for (int counter = 0; counter < 50; counter++) {
                    cellGrid[counter] = extractTab(
                            elem.getElementsByTagName("line" + counter).item(0).getChildNodes().item(0).getNodeValue());
                }

            }

        }
    }

    public static void createFile(String filename, Cell[][] grid, int height, int width) {
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("grid");
            document.appendChild(root);

            Element lines = document.createElement("lines");
            root.appendChild(lines);

            for (int i = 0; i < height; i++) {
                Element line = document.createElement("line" + (i));
                line.appendChild(document.createTextNode(extractString(grid[i], width)));
                lines.appendChild(line);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // XMLParserGOL.class.getResource().getPath()
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("src/" + filename));

            transformer.transform(domSource, streamResult);

            System.out.println("File created!");

        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private Cell[] extractTab(String line) {
        Cell cellTab[] = new Cell[50];

        for (int i = 0; i < 50; i++) {
            cellTab[i] = new Cell();

        }
        return cellTab;

    }

    private static String extractString(Cell[] tab, int width) {
        String str = "";

        for (int i = 0; i < width; i++) {
            str += tab[i].getState();
        }
        return str;
    }
}