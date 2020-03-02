/*
 * Copyright (c) 2020 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.xml.util;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


/**
 * FragmentContentHandlerTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2020/02/20 umjammer initial version <br>
 */
class FragmentContentHandlerTest {

    @Test
    void test() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();

        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        xr.setContentHandler(new FragmentContentHandler(xr, writer));
        xr.parse(new InputSource(FragmentContentHandlerTest.class.getResourceAsStream("/test.xml")));

        System.setProperty(XPathFactory.DEFAULT_PROPERTY_NAME + ":" + XPathFactory.DEFAULT_OBJECT_MODEL_URI, "org.apache.xpath.jaxp.XPathFactoryImpl");
        XPath xPath= XPathFactory.newInstance().newXPath();

        Arrays.stream(sw.getBuffer().toString().split("\n")).forEach(x -> {
            try {
System.err.println(x);
                InputSource in = new InputSource(new InputStreamReader((FragmentContentHandlerTest.class.getResourceAsStream("/test.xml"))));
                NodeList nodeList = (NodeList) xPath.evaluate(x, in, XPathConstants.NODESET);
                assertNotEquals(0, nodeList.getLength());
            } catch (XPathExpressionException e) {
                System.err.println("↑ " + e.getMessage());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }
}

/* */
