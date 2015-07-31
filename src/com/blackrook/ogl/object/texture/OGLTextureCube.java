/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.texture;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import javax.media.opengl.*;

import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.TextureCubeFace;
import com.blackrook.ogl.exception.GraphicsException;

/**
 * Object for cube map textures.
 * @author Matthew Tropiano
 */
public class OGLTextureCube extends OGLTexture
{
	/** Texture wrap S. */
	private WrapType wrapS;
	/** Texture wrap T. */
	private WrapType wrapT;
	/** Texture wrap R. */
	private WrapType wrapR;

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
	 * @param wrapR			the wrapping type for the r-coordinate axis.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	public OGLTextureCube(OGLGraphics g, InternalFormat format, MinFilter minFilter, MagFilter magFilter,
			float anisotropy, int borderSize, boolean genMipmaps, WrapType wrapS, WrapType wrapT, WrapType wrapR)
	{
		super(GL2.GL_TEXTURE_CUBE_MAP,g,format,minFilter,magFilter,anisotropy,borderSize,genMipmaps);
		this.wrapS = wrapS;
		this.wrapT = wrapT;
		this.wrapR = wrapR;
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
	public void sendData(OGLGraphics g, TextureCubeFace face, BufferedImage image)
	{
		if (image.getWidth() > g.getMaxTextureSize() || image.getHeight() > g.getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+g.getMaxTextureSize()+" pixels.");
		
		int oldSize = getEstimatedSize();
		
		setDimensions(image.getWidth(), image.getHeight(), 1, 6);
	
		Buffer buffer = getByteData(image);
		g.setTextureUnit(0);
		bindTo(g);
		if (!hasPowerOfTwoDimensions(image))
		{
			int newWidth = RMath.closestPowerOfTwo(getWidth());
			int newHeight = RMath.closestPowerOfTwo(getHeight());
			Buffer newb = ByteBuffer.allocate(newWidth*newHeight*4); 
			g.getGLU().gluScaleImage(
					GL2.GL_RGBA, 
					image.getWidth(), 
					image.getHeight(), 
					GL2.GL_UNSIGNED_BYTE,
					buffer, 
					newWidth, 
					newHeight, 
					GL2.GL_UNSIGNED_BYTE,
					newb
					);
			
			setDimensions(newWidth, newHeight, 1, 6);
			buffer = newb;
		}

		GL gl = g.getGL();
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, wrapT.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, wrapR.glid);
		gl.glTexImage2D(
				face.glValue,
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
	 * Loads a texture with data.
	 * This will bind the texture as current before loading it with info
	 * and will stay bound.
	 * @param px	the image to use for the Positive X cube face.
	 * @param py	the image to use for the Positive Y cube face.
	 * @param pz	the image to use for the Positive Z cube face.
	 * @param nx	the image to use for the Negative X cube face.
	 * @param ny	the image to use for the Negative Y cube face.
	 * @param nz	the image to use for the Negative Z cube face.
	 */
	public void sendData(OGLGraphics g, BufferedImage px, BufferedImage py, 
			BufferedImage pz, BufferedImage nx, BufferedImage ny, BufferedImage nz)
	{
		sendData(g, TextureCubeFace.PX, px);
		sendData(g, TextureCubeFace.PY, py);
		sendData(g, TextureCubeFace.PZ, pz);
		sendData(g, TextureCubeFace.NX, nx);
		sendData(g, TextureCubeFace.NY, ny);
		sendData(g, TextureCubeFace.NZ, nz);
	}

	/**
	 * Sends a subset of data to the texture already in OpenGL's memory.
	 * NOTE: This will leave the first texture unit unbound after this, as it uses
	 * the first unit for the loader target.
	 */
	public void sendSubData(OGLGraphics g, TextureCubeFace face, int xoffs, int yoffs, BufferedImage image)
	{
		Buffer buffer = getByteData(image);
		g.setTextureUnit(0);
		bindTo(g);
		g.getGL().glTexSubImage2D(
				face.glValue,
				0,
				xoffs,
				yoffs,
				getWidth(),
				getHeight(),
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

	/**
	 * Gets the texture wrapping type for the t-coordinate axis.
	 */
	public WrapType getWrapT()
	{
		return wrapT;
	}

	/**
	 * Gets the texture wrapping type for the r-coordinate axis.
	 */
	public WrapType getWrapR()
	{
		return wrapR;
	}

}
