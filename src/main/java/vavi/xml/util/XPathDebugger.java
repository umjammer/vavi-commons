/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * XPathDebugger.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/03/14 umjammer initial version <br>
 */
public class XPathDebugger {

    private XPathDebugger() {}

    /** */
    public static void debug(InputSource is) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            DefaultHandler handler = new FragmentContentHandler(xr, System.err);
            xr.setContentHandler(handler);
            xr.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /** */
    private static void buildEntryList(List<String> entries, String parentXPath, Element parent) {
        NamedNodeMap attrs = parent.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            // TODO: escape attr value
            entries.add(parentXPath + "[@" + attr.getName() + "='" + attr.getValue() + "']");
        }
        Map<String, Integer> nameMap = new HashMap<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Text) {
                if (parentXPath.toLowerCase().matches(".+/(script|body|head)\\[[^/.]+?\\]$")) {
                    entries.add(parentXPath + "='...'");
                } else {
                    // TODO: escape child value
                    entries.add(parentXPath + "='" + ((Text) child).getData().replace("\n", "\\n") + "'");
                }
            } else if (child instanceof Element) {
                String childName = child.getNodeName();
                Integer nameCount = nameMap.get(childName);
                nameCount = nameCount == null ? 1 : nameCount + 1;
                nameMap.put(child.getNodeName(), nameCount);
                buildEntryList(entries, parentXPath + "/" + childName + "[" + nameCount + "]", (Element) child);
            }
        }
    }

    /** list xPath */
    public static List<String> getEntryList(InputSource is) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringComments(true);
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);

            ArrayList<String> entries = new ArrayList<>();
            Element root = doc.getDocumentElement();
            buildEntryList(entries, "/" + root.getNodeName() + "[1]", root);
            return entries;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

/* */
