/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Texture internal storage format.
 */
public enum TextureFormat
{
	/** Grayscale, No alpha, default bit depth. */
	LUMINANCE(GL2.GL_LUMINANCE, false, 1),
	/** White, Alpha only, default bit depth. */
	INTENSITY(GL2.GL_INTENSITY, false, 1),
	/** RGB, No alpha, default bit depth. */
	RGB(GL2.GL_RGB, false, 3),
	/** RGBA, default bit depth. */
	RGBA(GL2.GL_RGBA, false, 4),
	/** RGBA, forced 16-bit. */
	RGBA4(GL2.GL_RGB4, false, 2),
	/** RGBA, forced 32-bit. */
	RGBA8(GL2.GL_RGB8, false, 4),
	/** RGB, No alpha, compressed. */
	COMPRESSED_RGB_DXT1(GL2.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, true, 0.375f),
	/** RGBA, compressed, one-bit alpha. */
	COMPRESSED_RGBA_DXT1(GL2.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT, true, 0.5f),
	/** RGBA, compressed, lossy alpha. */
	COMPRESSED_RGBA_DXT3(GL2.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, true, 1),
	/** RGBA, compressed, lossy alpha (second version). */
	COMPRESSED_RGBA_DXT5(GL2.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT, true, 1);
	
	public final int glid;
	public final boolean compressed;
	public final float sizeFactor;
	private TextureFormat(int id, boolean c, float factor) {glid = id; compressed = c; sizeFactor = factor;}
	public boolean isCompressed() {return compressed;}
	public int getGLValue() {return glid;}
}

