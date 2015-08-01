package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Minification filters.
 * @author Matthew Tropiano
 */
public enum TextureMinFilter
{
	/** 
	 * Nearest minification - color using nearest neighbor (aliased - bad approximation). 
	 */
	NEAREST(GL2.GL_NEAREST),
	/** 
	 * Linear minification - color using cluster average (okay approximation). 
	 */
	LINEAR(GL2.GL_LINEAR),
	/** 
	 * Bilinear minification - color using cluster average and next mipmap's
	 * nearest neighbor (better approximation). 
	 */
	BILINEAR(GL2.GL_LINEAR_MIPMAP_NEAREST),
	/** 
	 * Trilinear minification - color using cluster average and next mipmap's 
	 * cluster average (best approximation).
	 * Also called "bicubic" or "cubic." 
	 */
	TRILINEAR(GL2.GL_LINEAR_MIPMAP_LINEAR);
	
	public final int glid;
	private TextureMinFilter(int id) {glid = id;}
}

