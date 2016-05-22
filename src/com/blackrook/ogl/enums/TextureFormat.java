/*******************************************************************************
 * Copyright (c) 2014, 2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * Contributors:
 *     Matt Tropiano - initial API and implementation
 *******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL3;

/**
 * Texture internal storage format.
 */
public enum TextureFormat
{
	/*
 		GL_RGBA32F, 
 		GL_RGBA32I, 
 		GL_RGBA32UI, 
 		GL_RGBA16, 
 		GL_RGBA16F, 
 		GL_RGBA16I, 
 		GL_RGBA16UI, 
 		GL_RGBA8, 
 		GL_RGBA8UI, 
 		GL_SRGB8_ALPHA8, 
 		GL_RGB10_A2, 
 		GL_RGB10_A2UI, 
 		GL_R11F_G11F_B10F, 
 		GL_RG32F, 
 		GL_RG32I, 
 		GL_RG32UI, 
 		GL_RG16, 
 		GL_RG16F, 
 		GL_RGB16I, 
 		GL_RGB16UI, 
 		GL_RG8, 
 		GL_RG8I, 
 		GL_RG8UI, 
 		GL_R32F, 
 		GL_R32I,
 		GL_R32UI, 
 		GL_R16F, 
 		GL_R16I, 
 		GL_R16UI, 
 		GL_R8, 
 		GL_R8I, 
 		GL_R8UI, 
 		GL_RGBA16_SNORM, 
 		GL_RGBA8_SNORM, 
 		GL_RGB32F, 
 		GL_RGB32I, 
 		GL_RGB32UI, 
 		GL_RGB16_SNORM, 
 		GL_RGB16F, 
 		GL_RGB16I, 
 		GL_RGB16UI, 
 		GL_RGB16,
 		GL_RGB8_SNORM, 
 		GL_RGB8, 
 		GL_RGB8I, 
 		GL_RGB8UI,
 		GL_SRGB8, 
 		GL_RGB9_E5, 
 		GL_RG16_SNORM, 
 		GL_RG8_SNORM, 
 		GL_COMPRESSED_RG_RGTC2, 
 		GL_COMPRESSED_SIGNED_RG_RGTC2, 
 		GL_R16_SNORM, 
 		GL_R8_SNORM, 
 		GL_COMPRESSED_RED_RGTC1,
 		GL_COMPRESSED_SIGNED_RED_RGTC1, 
 		GL_DEPTH_COMPONENT32F, 
 		GL_DEPTH_COMPONENT24, 
 		GL_DEPTH_COMPONENT16, 
 		GL_DEPTH32F_STENCIL8,
 		 GL_DEPTH24_STENCIL8	 
	 */
	
	/** Grayscale, No alpha, default bit depth. */
	LUMINANCE(GL3.GL_LUMINANCE, false, 1),
	/** RGB, No alpha, default bit depth. */
	RGB(GL3.GL_RGB, false, 3),
	/** RGBA, default bit depth. */
	RGBA(GL3.GL_RGBA, false, 4),
	/** RGBA, forced 16-bit. */
	RGBA4(GL3.GL_RGB4, false, 2),
	/** RGBA, forced 32-bit. */
	RGBA8(GL3.GL_RGB8, false, 4),
	/** RGB, No alpha, compressed. */
	COMPRESSED_RGB_DXT1(GL3.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, true, 0.375f),
	/** RGBA, compressed, one-bit alpha. */
	COMPRESSED_RGBA_DXT1(GL3.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT, true, 0.5f),
	/** RGBA, compressed, lossy alpha. */
	COMPRESSED_RGBA_DXT3(GL3.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, true, 1),
	/** RGBA, compressed, lossy alpha (second version). */
	COMPRESSED_RGBA_DXT5(GL3.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT, true, 1);
	
	public final int glid;
	public final boolean compressed;
	public final float sizeFactor;
	private TextureFormat(int id, boolean c, float factor) {glid = id; compressed = c; sizeFactor = factor;}
	public boolean isCompressed() {return compressed;}
	public int getGLValue() {return glid;}
}

