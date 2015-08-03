/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.data.OGLColor;
import com.blackrook.ogl.enums.AttribType;
import com.blackrook.ogl.enums.GLUTFont;
import com.blackrook.ogl.enums.MatrixType;

/**
 * A node that outputs a textual message via {@link OGLGraphics#printf(String, Object...)}.
 * This does not use the depth test at all. 
 * @author Matthew Tropiano
 */
public class OGLTextOverlayNode extends OGLCanvasNodeAdapter
{
	/** Font used by this node. */
	protected GLUTFont font;
	/** Text color used by this node. */
	protected OGLColor textColor;
	/** Raster Position X. */
	protected int rasterX;
	/** Raster Position Y. */
	protected int rasterY;

	/** Output message. */
	protected String message;

	/** Output arguments. */
	protected Object[] args;
	
	public OGLTextOverlayNode()
	{
		font = GLUTFont.BITMAP_8_BY_13;
		textColor = new OGLColor(OGLColor.WHITE);
		rasterX = 0;
		rasterY = 0;
		message = "";
		args = new Object[0];
	}
	
	/**
	 * Returns the font used on this node.
	 */
	public GLUTFont getFont()
	{
		return font;
	}

	/**
	 * Sets the GLUT font to be used to render the text.
	 */
	public void setFont(GLUTFont font)
	{
		this.font = font;
	}

	/**
	 * Gets the current text color used for the raster.
	 */
	public OGLColor getTextColor()
	{
		return textColor;
	}

	/**
	 * Sets the current text color used for the raster.
	 */
	public void setTextColor(OGLColor textColor)
	{
		this.textColor.set(textColor);
	}

	/**
	 * Gets the current raster position, x-coordinate.
	 */
	public int getRasterX()
	{
		return rasterX;
	}

	/**
	 * Gets the current raster position, y-coordinate.
	 */
	public int getRasterY()
	{
		return rasterY;
	}

	/**
	 * Sets the message raster position.
	 * Remember that the bottom-left is (0,0).
	 */
	public void setRaster(int x, int y)
	{
		rasterX = x;
		rasterY = y;
	}
	
	/**
	 * Sets the text on this node.
	 * @param message the formattied message to print.
	 * @param args the arguments for the formatted message.
	 */
	public void setText(String message, Object ... args)
	{
		this.message = message;
		this.args = args;
	}

	/**
	 * Returns the text as a formatted string.
	 */
	public String getText()
	{
		return String.format(message, args);
	}
	
	@Override
	public void display(OGLGraphics g)
	{
		g.attribPush(
				AttribType.DEPTH_BUFFER, 
				AttribType.COLOR_BUFFER,
				AttribType.CURRENT,
				AttribType.ENABLE,
				AttribType.TEXTURE);
		
    	// disable and unbind all that interferes with the raster.
		g.setDepthTestEnabled(false);
    	g.setLightingEnabled(false);
    	g.setTexture1DEnabled(false);
    	g.setTexture2DEnabled(false);

    	g.matrixMode(MatrixType.MODELVIEW);
    	g.matrixPush();
    	g.matrixReset();
    	g.matrixMode(MatrixType.PROJECTION);
    	g.matrixPush();
    	g.matrixReset();

    	g.matrixOrtho(0, g.getCanvasWidth(), 0, g.getCanvasHeight(), -1, 1);
    	g.setColor(OGLColor.WHITE);
    	g.setRasterPosition(getRasterX(), getRasterY(), 0);
    	g.print(getText());

    	g.matrixMode(MatrixType.PROJECTION);
    	g.matrixPop();
    	g.matrixMode(MatrixType.MODELVIEW);
    	g.matrixPop();
    	
    	g.attribPop();
	}
	
}
