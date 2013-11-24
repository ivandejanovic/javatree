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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;

/**
 * JavaTreePanel class extends JPanel class and contains in it self a
 * JScrollPane which displays the tree and JTextField and JTextArea which
 * displays the data of currently selected node.
 * 
 * @author Ivan Dejanovic
 * 
 */

public class JavaTreePanel extends JPanel {

	private JTextField textField;
	private JTextArea textArea;
	private JScrollPane treeView;

	/**
	 * Creates TreeLinePanel
	 */
	public JavaTreePanel(JTree tree) {

		// set TreeLine Panel layout
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		// Set up tree in Scroll Pane
		treeView = new JScrollPane(tree);

		// Set up Split Pane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(setTextPanel());
		add(splitPane);
	}

	/**
	 * Sets the tree object of the JavaTreePanel
	 * 
	 * @param t
	 *            the tree to set
	 */
	public void setTree(JTree tree) {
		treeView.setViewportView(tree);
		textField.setText("");
		textArea.setText("");
	}

	/**
	 * @return the textField text
	 */
	public String getTextFieldText() {
		return textField.getText();
	}

	/**
	 * @param text
	 *            to set to TextField
	 */
	public void setTextField(String text) {
		textField.setText(text);
	}

	/**
	 * @return the textArea text
	 */
	public String getTextAreaText() {
		return textArea.getText();
	}

	/**
	 * @param text
	 *            to set to textArea
	 */
	public void setTextArea(String text) {
		textArea.setText(text);
	}

	/**
	 * Creates and sets up textPanel
	 * 
	 * @return textPanel
	 */
	private JPanel setTextPanel() {

		JPanel textPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		textPanel.setLayout(layout);

		textField = new JTextField();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.insets = new Insets(3, 3, 3, 3);
		c.fill = GridBagConstraints.HORIZONTAL;
		textPanel.add(textField, c);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 100;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		textPanel.add(new JScrollPane(textArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), c);

		return textPanel;
	}
}
