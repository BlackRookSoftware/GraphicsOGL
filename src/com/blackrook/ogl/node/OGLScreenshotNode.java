/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import java.io.File;
import java.io.IOException;

import com.blackrook.commons.Common;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.jogamp.opengl.util.awt.Screenshot;

/**
 * A special type of triggered node that takes a screenshot
 * and puts it in a file of your choosing. Make sure to put this
 * at the end of your rendering path, or you will likely get an 
 * "incomplete" screenshot.
 * @author Matthew Tropiano
 */
public class OGLScreenshotNode extends OGLTriggeredNode
{
	/** Sets the output file for the next screenshot. */
	private File screenShotFile;

	/**
	 * Creates a new screenshot node, with no output file set.
	 */
	public OGLScreenshotNode()
	{
		this(null);
	}

	/**
	 * Creates a new screenshot node.
	 * @param screenShotFile the initial screenshot file.
	 */
	public OGLScreenshotNode(File screenShotFile)
	{
		setScreenShotFile(screenShotFile);
	}

	/**
	 * Gets the file used for the screenshot. Can be null.
	 */
	public File getScreenShotFile()
	{
		return screenShotFile;
	}

	/**
	 * Sets the file used for the screenshot. Can be null.
	 * If it is null, no screenshot is made if triggered.
	 */
	public void setScreenShotFile(File screenShotFile)
	{
		this.screenShotFile = screenShotFile;
	}

	@Override
	public void doTriggeredFunction(OGLGraphics g)
	{
		try {
			if (screenShotFile != null && Common.createPathForFile(screenShotFile))
				Screenshot.writeToFile(screenShotFile, (int)g.getCanvasWidth(), (int)g.getCanvasHeight());
        	} catch (IOException e) {
        	throw new GraphicsException(e.getMessage());
		}
	}

}
