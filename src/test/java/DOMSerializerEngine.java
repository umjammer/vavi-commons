/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import vavi.io.OutputEngine;


/**
 * An output engine that serializes a DOM tree using a specified
 * character encoding to the target OutputStream.
 *
 * @author Copyright (c) 2002 Merlin Hughes <merlin@merlin.org>
 */
public class DOMSerializerEngine implements OutputEngine {

    private NodeIterator iterator;
    private String encoding;
    private OutputStreamWriter writer;

    public DOMSerializerEngine(Node root) {
        this(root, "UTF-8");
    }

    public DOMSerializerEngine(Node root, String encoding) {
        this(getIterator(root), encoding);
    }

    private static NodeIterator getIterator(Node node) {
        DocumentTraversal dt = (DocumentTraversal) (node.getNodeType() == Node.DOCUMENT_NODE ? node : node.getOwnerDocument());
        return dt.createNodeIterator(node, NodeFilter.SHOW_ALL, null, false);
    }

    public DOMSerializerEngine(NodeIterator iterator, String encoding) {
        this.iterator = iterator;
        this.encoding = encoding;
    }

    public void initialize(OutputStream out) throws IOException {
        if (writer != null) {
            throw new IOException("Already initialised");
        } else {
            writer = new OutputStreamWriter(out, encoding);
        }
    }

    public void execute() throws IOException {
        if (writer == null) {
            throw new IOException("Not yet initialised");
        } else {
            Node node = iterator.nextNode();
            closeElements(node);
            if (node == null) {
                writer.close();

            } else {
                writeNode(node);
                writer.flush();
            }
        }
    }

    public void finish() throws IOException {
    }

    private void closeElements(Node node) throws IOException {
        // TODO
    }

    private void writeNode(Node node) throws IOException {
        // TODO
    }
}
