/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.texture;

import java.io.*;
import java.nio.*;

import javax.media.opengl.*;

import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.OGLGraphicUtils;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;

import java.awt.image.BufferedImage;

/**
 * Basis for all one-dimensional textures.
 * @author Matthew Tropiano
 */
public class OGLTexture1D extends OGLTexture 
{
	/** Texture wrap S. */
	private WrapType wrapS;

	/**
	 * Constructs a new one-dimensional 4-channel texture from a BufferedImage.
	 * @param format 		the InternalFormat to represent this texture as in OpenGL.
	 * @param minFilter		the minification filter.
	 * @param magFilter 	the magnification filter.
	 * @param anisotropy	the anisotropy factor for texture filtering.
	 * @param borderSize	the texture's border size.
	 * @param genMipmaps	true if mipmaps should be generated for this texture, false if not.
	 * @param wrapS			the wrapping type for the s-coordinate axis.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	public OGLTexture1D(OGLGraphics g, InternalFormat format, MinFilter minFilter, MagFilter magFilter,
			float anisotropy, int borderSize, boolean genMipmaps, WrapType wrapS) throws IOException
	{
		super(GL2.GL_TEXTURE_1D,g,format,minFilter,magFilter,anisotropy,borderSize,genMipmaps);
		this.wrapS = wrapS;
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.clearError();
		g.getGL().glGenTextures(1,glStateNum,0);
		g.getError();
		return glStateNum[0];
	}

	/**
	 * Sends the texture into OpenGL's memory.
	 * NOTE: This will leave the first texture unit unbound after this, as it uses
	 * the first unit for the loader target.
	 */
	public void sendData(OGLGraphics g, BufferedImage image)
	{
		if (image.getWidth() > g.getMaxTextureSize() || image.getHeight() > g.getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+g.getMaxTextureSize()+" pixels.");
		
		int oldSize = getEstimatedSize();
		
		Buffer buffer = null;
		g.setTextureUnit(0);
		bindTo(g);

		setDimensions(image.getWidth(), 1, 1, 1);

		if (!hasPowerOfTwoDimensions(image))
		{
			int newWidth = RMath.closestPowerOfTwo(getWidth());
			setDimensions(newWidth, 1, 1, 1);
			buffer = getByteData(OGLGraphicUtils.performResizeBilinear(image, newWidth, 1)); 
		}
		else
		{
			buffer = getByteData(image);	
		}
		
		GL2 gl = g.getGL();
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexImage1D(
				GL2.GL_TEXTURE_1D,
				0,
				getInternalFormat().glid, 
				getWidth(),
				getBorder(),
				GL2.GL_BGRA,
				GL2.GL_UNSIGNED_BYTE,
				buffer
				);

		adjustMemorySize(oldSize, getEstimatedSize());
		unbindFrom(g);
	}
	
	/**
	 * Sends a subset of data to the texture already in OpenGL's memory.
	 * NOTE: This will leave the first texture unit unbound after this, as it uses
	 * the first unit for the loader target.
	 */
	public void sendSubData(OGLGraphics g, int xoffs, BufferedImage image)
	{
		Buffer buffer = getByteData(image);
		g.setTextureUnit(0);
		bindTo(g);
		g.getGL().glTexSubImage1D(
				GL2.GL_TEXTURE_1D,
				0,
				xoffs,
				getWidth(),
				GL2.GL_RGBA,
				GL2.GL_UNSIGNED_BYTE,
				buffer
				);
		unbindFrom(g);
	}

	/**
	 * Gets the texture wrapping type for the s-coordinate axis.
	 */
	public WrapType getWrapS()
	{
		return wrapS;
	}

}
