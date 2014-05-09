/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.texture;

import java.nio.*;

import javax.media.opengl.*;

import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.OGLGraphicUtils;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;

import java.awt.image.BufferedImage;

/**
 * Basis for all two-dimensional, RGBA textures.
 * @author Matthew Tropiano
 */
public class OGLTexture2D extends OGLTexture 
{
	/** Texture wrap S. */
	private WrapType wrapS;
	/** Texture wrap T. */
	private WrapType wrapT;
	
	/**
	 * Constructs a new two-dimensional 4-channel texture from a BufferedImage.
	 * @param format 		the InternalFormat to represent this texture as in OpenGL.
	 * @param minFilter		the minification filter.
	 * @param magFilter		the magnification filter.
	 * @param anisotropy	the anisotropy factor for texture filtering.
	 * @param borderSize	the texture's border size.
	 * @param genMipmaps	true if mipmaps should be generated for this texture, false if not.
	 * @param wrapS			the wrapping type for the s-coordinate axis.
	 * @param wrapT			the wrapping type for the t-coordinate axis.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	public OGLTexture2D(OGLGraphics g, InternalFormat format, MinFilter minFilter, MagFilter magFilter,
			float anisotropy, int borderSize, boolean genMipmaps, WrapType wrapS, WrapType wrapT)
	{
		super(GL2.GL_TEXTURE_2D,g,format,minFilter,magFilter,anisotropy,borderSize,genMipmaps);
		this.wrapS = wrapS;
		this.wrapT = wrapT;
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

		if (!hasPowerOfTwoDimensions(image))
		{
			int newWidth = RMath.closestPowerOfTwo(getWidth());
			int newHeight = RMath.closestPowerOfTwo(getHeight());
			setDimensions(newWidth, newHeight, 1, 1);
			buffer = getByteData(OGLGraphicUtils.performResizeBilinear(image, newWidth, newHeight)); 
		}
		else
		{
			buffer = getByteData(image);	
			setDimensions(image.getWidth(), image.getHeight(), 1, 1);
		}

		GL gl = g.getGL();
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, wrapT.glid);
		gl.glTexImage2D(
				GL2.GL_TEXTURE_2D,
				0,
				getInternalFormat().glid, 
				getWidth(),
				getHeight(),
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
	public void sendSubData(OGLGraphics g, int xoffs, int yoffs, BufferedImage image)
	{
		Buffer buffer = getByteData(image);
		g.setTextureUnit(0);
		bindTo(g);
		g.getGL().glTexSubImage2D(
				GL2.GL_TEXTURE_2D,
				0,
				xoffs,
				yoffs,
				getWidth(),
				getHeight(),
				GL2.GL_BGRA,
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

	/**
	 * Gets the texture wrapping type for the t-coordinate axis.
	 */
	public WrapType getWrapT()
	{
		return wrapT;
	}

	/**
	 * Binds this texture to the specified OGLGraphics context.
	 * Relies on the current texture unit for dictating which unit
	 * to bind to.
	 * <p> See {@link OGLGraphics#setTextureUnit(int)}
	 */
	public void bindTo(OGLGraphics g)
	{
		g.getGL().glBindTexture(GL2.GL_TEXTURE_2D, getGLId());
	}

	/**
	 * Unbinds this texture from the specified OGLGraphics context.
	 * Relies on the current texture unit for dictating which unit
	 * to unbind from.
	 * <p> See {@link OGLGraphics#setTextureUnit(int)}
	 */
	public void unbindFrom(OGLGraphics g)
	{
		g.getGL().glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}


}
