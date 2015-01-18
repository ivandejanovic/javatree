/**
 * The MIT License (MIT)
 *
 * Copyright 2008-2015 Ivan Dejanovic and Quine Interactive
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

import javax.swing.JFrame;

/**
 * JavaTreeFrame class extends JFrame class and is responsible for containing all GUI elements of JavaTree application.
 * 
 * @author Ivan Dejanovic
 * 
 * @version 1.0
 * 
 * @since 1.0
 * 
 */

public class JavaTreeFrame extends JFrame {
    private static final long serialVersionUID = 7499078909170382541L;

    // main application controller
    JavaTreeController        controller;

    // static dimensions of the frame
    private final static int  WIDTH            = 400;
    private final static int  HEIGHT           = 300;

    /**
     * Creates and setup TreeLineFrame
     */
    public JavaTreeFrame() {
        // create and set frame
        setTitle("JavaTree");
        setSize(WIDTH, HEIGHT);

        // create controller
        controller = new JavaTreeController(this);

        // set panel
        setContentPane(controller.getPanel());

        // set JMenuBar
        setJMenuBar(controller.getMenuBar());
    }
}
