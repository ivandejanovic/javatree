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

/**
 * JavaTreeNodeObject is a POJO class used as object property of
 * DefaultMutableTreeNode in JavaTree application.
 * 
 * @author Ivan Dejanovic
 * 
 */

public class JavaTreeNodeObject {

	private String title;
	private String text;

	/**
	 * Creates JavaTreeNodeObject and initialize title to "New Node", text to
	 * empty string.
	 */
	public JavaTreeNodeObject() {

		title = "New Node";
		text = "";
	}

	/**
	 * Creates JavaTreeNodeObject and initialize title to title, text to empty
	 * string.
	 * 
	 * 
	 * @param title
	 */
	public JavaTreeNodeObject(String title) {

		this.title = title;
		text = "";
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Method used by JTree class to show Node from a model. Implemented using
	 * getTitle method.
	 * 
	 * @return title
	 */
	@Override
	public String toString() {
		return getTitle();
	}
}
