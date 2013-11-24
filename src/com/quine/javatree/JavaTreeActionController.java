/**
 * The MIT License (MIT)
 * 
 * Copyright 2008-2013 Quine Interactive and other contributors
 * www.quineinteractive.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package com.quine.javatree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * JavaTreeActionControler performs required actions for Actions from
 * JavaTreeControlller. This class is used to implements all logic needed in
 * order to provide necessary functionality to Actions of JavaTreeController
 * class.
 * 
 * @author Ivan Dejanovic
 * 
 */

public class JavaTreeActionController {

	/**
	 * Creates standard JavaTreeActionControler
	 */
	public JavaTreeActionController() {

	}

	/**
	 * Creates a default JTree with one root element
	 * 
	 * @return tree
	 */
	public JTree newAction() {

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(
				new JavaTreeNodeObject("main"));

		return new JTree(root);
	}

	/**
	 * Opens a file user selects, read it and construct a tree based on the file
	 * content. Returns reference to a tree or null if error occurred.
	 * 
	 * @return tree
	 */
	public JTree openAction() {

		File file = chooseFile(true);
		if (file == null) {
			JOptionPane
					.showMessageDialog(null,
							"Wrong type of file selected.\nFile extenstion needs to be .jtd.");
			return null;
		}
		Document document = loadDocumentFromFile(file);
		if (document == null) {
			JOptionPane
					.showMessageDialog(null, "Error while parsing document.");
			return null;
		}
		return convertDocumentToJTree(document);
	}

	/**
	 * Construct a DOM object from a tree and saves it to XML file. Returns true
	 * if method was successful, false if error occurred.
	 * 
	 * @param tree
	 * 
	 * @return status
	 */
	public boolean saveAction(JTree tree) {

		Document document = convertJTreeToXML(tree);
		if (document == null) {
			JOptionPane.showMessageDialog(null,
					"Error while converting data from tree to document.");
			return false;
		}
		File file = chooseFile(false);
		if (file == null) {
			JOptionPane
					.showMessageDialog(null,
							"Wrong type of file selected.\nFile extenstion needs to be .jtd.");
			return false;
		}
		return saveDocumentToFile(document, file);
	}

	/**
	 * Add child node to selected node of a tree.
	 * 
	 * @param tree
	 */
	public void addChildAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		addChildToGivenNodeAction(tree, node);
	}

	/**
	 * Add child to given node of a tree.
	 * 
	 * @param tree
	 * @param node
	 */
	public void addChildToGivenNodeAction(JTree tree,
			DefaultMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.insertNodeInto(new DefaultMutableTreeNode(
				new JavaTreeNodeObject()), node, model.getChildCount(node));
	}

	/**
	 * Delete selected node of a tree.
	 * 
	 * @param tree
	 */
	public void deleteNodeAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		deleteGivenNodeAction(tree, node);
	}

	/**
	 * Delete given node of a tree
	 * 
	 * @param tree
	 * @param node
	 */
	public void deleteGivenNodeAction(JTree tree, DefaultMutableTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		model.removeNodeFromParent(node);
	}

	/**
	 * Move up selected node of a tree.
	 * 
	 * @param tree
	 */
	public void moveUpAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		moveGivenNodeUpAction(tree, node);

	}

	/**
	 * Move up given node of a tree.
	 * 
	 * @param tree
	 * @param node
	 */
	public void moveGivenNodeUpAction(JTree tree, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		int index = model.getIndexOfChild(parent, node);

		if (index > 0) {
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, parent, --index);
		}
	}

	/**
	 * Move down selected node of a tree.
	 * 
	 * @param tree
	 */
	public void moveDownAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		moveGivenNodeDownAction(tree, node);
	}

	/**
	 * Move down given node of a tree.
	 * 
	 * @param tree
	 * @param node
	 */
	public void moveGivenNodeDownAction(JTree tree, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		int index = model.getIndexOfChild(parent, node);

		if (index < (model.getChildCount(parent) - 1)) {
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, parent, ++index);
		}
	}

	/**
	 * Move level up selected node of a tree.
	 * 
	 * @param tree
	 */
	public void moveLevelUpAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		moveGivenNodeLevelUpAction(tree, node);
	}

	/**
	 * Move level up given node of a tree.
	 * 
	 * @param tree
	 * @param node
	 */
	public void moveGivenNodeLevelUpAction(JTree tree,
			DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		DefaultMutableTreeNode superParent = (DefaultMutableTreeNode) parent
				.getParent();
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		if (!(parent.isRoot())) {
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, superParent,
					model.getChildCount(superParent));
		}
	}

	/**
	 * Move level down selected node of a tree.
	 * 
	 * @param tree
	 */
	public void moveLevelDownAction(JTree tree) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		moveGivenNodeLevelDownAction(tree, node);
	}

	/**
	 * Move level down given node of a tree.
	 * 
	 * @param tree
	 * @param node
	 */
	public void moveGivenNodeLevelDownAction(JTree tree,
			DefaultMutableTreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node
				.getParent();
		DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode) parent
				.getChildAfter(node);
		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		if (nextNode != null) {
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, nextNode, model.getChildCount(nextNode));
		}
	}

	/**
	 * Displays help.
	 */
	public void HelpAction() {

		JOptionPane.showMessageDialog(null, "Java Tree data organizer.");
	}

	/**
	 * Displays about.
	 */
	public void AboutAction() {

		JOptionPane
				.showMessageDialog(
						null,
						"JavaTree version 1.0.\nCopyright Quine Interactive 2011.\nwww.quineinteractice.com");
	}

	/**
	 * Creates file chooser dialog and returns a selected file
	 * 
	 * @return file
	 */
	private File chooseFile(boolean loadFlag) {

		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".jtd");
			}

			public String getDescription() {
				return "Java Tree document";
			}
		});

		int returnStatus;
		if (loadFlag) {
			returnStatus = fileChooser.showOpenDialog(null);
		} else {
			returnStatus = fileChooser.showSaveDialog(null);
		}

		if (returnStatus != JFileChooser.APPROVE_OPTION)
			return null;

		return fileChooser.getSelectedFile();
	}

	/**
	 * Convert tree it receives as parameter to document.
	 * 
	 * @param tree
	 * 
	 * @return document
	 */
	private Document convertJTreeToXML(JTree tree) {

		Document document = null;

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();

			Element rootElement = document.createElement("JavaTreeXML");
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree
					.getModel().getRoot();
			rootElement.appendChild(createNodeElement(document, rootNode));
			document.appendChild(rootElement);
		} catch (Exception e) {
			e.printStackTrace();
			document = null;
		}

		return document;
	}

	/**
	 * Convert document it receives as parameter to tree.
	 * 
	 * @param document
	 * 
	 * @return tree
	 */
	private JTree convertDocumentToJTree(Document document) {

		Element rootElement = document.getDocumentElement();
		Element node = (Element) rootElement.getFirstChild();

		JTree tree = new JTree(createJavaTreeNode(node));

		return tree;
	}

	/**
	 * Save document to file and return true is successful. Return false if
	 * error occurs.
	 * 
	 * @param document
	 * @param file
	 * 
	 * @return status
	 */
	private boolean saveDocumentToFile(Document document, File file) {

		boolean status = true;

		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();

			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
					"javatree.dtd");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			transformer.transform(new DOMSource(document), new StreamResult(
					new FileOutputStream(file)));
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}

		return status;
	}

	/**
	 * Reads a file and creates a document. Returns document if successful, or
	 * null if error occurs.
	 * 
	 * @param file
	 * 
	 * @return document
	 */
	private Document loadDocumentFromFile(File file) {

		Document document = null;

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(true);
			factory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);
		} catch (Exception e) {
			e.printStackTrace();
			document = null;
		}

		return document;
	}

	/**
	 * Creates an element for a document from a node.
	 * 
	 * @param document
	 * @param node
	 * 
	 * @return element
	 */
	private Element createNodeElement(Document document,
			DefaultMutableTreeNode node) {

		Element element = document.createElement("Node");

		Element nodeTitleElement = document.createElement("Title");
		Text nodeTitleText = document.createTextNode(((JavaTreeNodeObject) node
				.getUserObject()).getTitle());
		nodeTitleElement.appendChild(nodeTitleText);
		element.appendChild(nodeTitleElement);

		Element nodeTextElement = document.createElement("Text");
		Text nodeTextText = document.createTextNode(((JavaTreeNodeObject) node
				.getUserObject()).getText());
		nodeTextElement.appendChild(nodeTextText);
		element.appendChild(nodeTextElement);

		if (!node.isLeaf()) {
			int childrenCount = node.getChildCount();

			for (int index = 0; index < childrenCount; index++) {
				element.appendChild(createNodeElement(document,
						(DefaultMutableTreeNode) node.getChildAt(index)));
			}
		}

		return element;
	}

	/**
	 * Creates a node from a element.
	 * 
	 * @param element
	 * 
	 * @return treeNode
	 */
	private DefaultMutableTreeNode createJavaTreeNode(Element element) {

		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
				new JavaTreeNodeObject());
		NodeList nodeList = null;

		nodeList = element.getChildNodes();

		for (int index = 0; index < nodeList.getLength(); index++) {
			Element child = (Element) nodeList.item(index);
			String tagName = child.getTagName();

			if (tagName.equals("Title")) {
				Text titleText = (Text) child.getFirstChild();
				if (titleText != null) {
					((JavaTreeNodeObject) treeNode.getUserObject())
							.setTitle(titleText.getData().trim());
				} else {
					((JavaTreeNodeObject) treeNode.getUserObject())
							.setTitle("");
				}
			} else if (tagName.equals("Text")) {

				Text textText = (Text) child.getFirstChild();
				if (textText != null) {
					((JavaTreeNodeObject) treeNode.getUserObject())
							.setText(textText.getData().trim());
				} else {
					((JavaTreeNodeObject) treeNode.getUserObject()).setText("");
				}
			} else if (tagName.equals("Node")) {

				treeNode.add(createJavaTreeNode(child));
			}
		}

		return treeNode;
	}
}
