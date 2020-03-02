/*
 * https://stackoverflow.com/questions/4746299/generate-get-xpath-from-xml-node-java
 */

package vavi.xml.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * FragmentContentHandler retrieves all xpath expressions from a xml.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/02/20 umjammer initial version <br>
 */
public class FragmentContentHandler extends DefaultHandler {

    private String xPath = "";

    private XMLReader xmlReader;

    private FragmentContentHandler parent;

    private StringBuilder characters = new StringBuilder();

    private Map<String, Integer> elementNameCount = new HashMap<>();

    private PrintWriter writer;

    public FragmentContentHandler(XMLReader xmlReader, PrintStream ps) {
        this(xmlReader, new PrintWriter(ps));
    }

    public FragmentContentHandler(XMLReader xmlReader, PrintWriter writer) {
        this.xmlReader = xmlReader;
        this.writer = writer;
    }

    private FragmentContentHandler(String xPath, XMLReader xmlReader, PrintWriter writer, FragmentContentHandler parent) {
        this(xmlReader, writer);
        this.xPath = xPath;
        this.parent = parent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        Integer count = elementNameCount.get(qName);
        if (null == count) {
            count = 1;
        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        String childXPath = xPath + "/" + qName + (count > 1 ? "[" + count + "]" : "");

        int attsLength = atts.getLength();
        for (int x = 0; x < attsLength; x++) {
            writer.println(childXPath + "[@" + atts.getQName(x) + "='" + atts.getValue(x) + "']");
        }

        FragmentContentHandler child = new FragmentContentHandler(childXPath, xmlReader, writer, this);
        xmlReader.setContentHandler(child);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        String value = characters.toString().trim();
        if (value.length() > 0) {
//            writer.println(xPath + "='" + characters.toString() + "'");
            writer.println(xPath + "/text()");
        }
        xmlReader.setContentHandler(parent);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        characters.append(ch, start, length);
    }
}
