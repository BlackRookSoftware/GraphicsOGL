/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.texture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import com.blackrook.commons.Common;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.texture.OGLTexture.InternalFormat;
import com.blackrook.ogl.object.texture.OGLTexture.MagFilter;
import com.blackrook.ogl.object.texture.OGLTexture.MinFilter;
import com.blackrook.ogl.object.texture.OGLTexture.WrapType;

/**
 * A factory class for quick or debug texture generation.
 * @author Matthew Tropiano
 */
public final class OGLTextureFactory
{
	/**
	 * Creates a texture consisting of a solid color.
	 * Texture is a 1x1 32-bit color texture.
	 * @param graphics the OGLGraphics context to use for creating the texture.
	 * @param r the red color component value.
	 * @param g the green color component value.
	 * @param b the blue color component value.
	 * @param a the alpha color component value.
	 * @return a new OGLTexture2D of the created texture.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	public static OGLTexture2D createSolidColorTexture(OGLGraphics graphics, float r, float g, float b, float a)
	{
		OGLTexture2D out = new OGLTexture2D(graphics, 
			InternalFormat.RGBA, MinFilter.NEAREST, MagFilter.NEAREST, 
			1f, 0, false, WrapType.CLAMP, WrapType.CLAMP);
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, Common.colorToARGB(new Color(r, g, b, a)));
		out.sendData(graphics, image);
		return out;
	}
	
}
