/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import java.io.PrintStream;

import com.blackrook.commons.Common;
import com.blackrook.ogl.OGLGraphics;

/**
 * A canvas node that gathers and prints information from OpenGL.
 * Used to just briefly output information once.
 * @author Matthew Tropiano
 */
public class OGLInfoPrinterNode extends OGLTriggeredNode
{
	/** The output stream. */
	protected PrintStream out;
	
	/**
	 * Creates a new OGLInfoPrinterNode that outputs to standard out.
	 */
	public OGLInfoPrinterNode()
	{
		this(System.out);
	}

	/**
	 * Creates a new OGLInfoPrinterNode that outputs to standard out.
	 */
	public OGLInfoPrinterNode(PrintStream out)
	{
		this.out = out;
	}

	@Override
	public void doTriggeredFunction(OGLGraphics g)
	{
		out.println("GL Vendor:     "+g.getGLVendor());
		out.println("GL Version:    "+g.getGLVersion());
		out.println("GL Renderer:   "+g.getGLRenderer());
		out.println("GL Extensions:");
		Common.printWrapped(out, g.getGLExtensions(), 80);
		out.println();
	}

}
