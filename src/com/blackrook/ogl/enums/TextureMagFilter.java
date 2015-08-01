package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Magnification filters.
 * @author Matthew Tropiano
 */
public enum TextureMagFilter
{
	/** Nearest magnification - color using nearest neighbor (aliased - "pixelates" textures). */
	NEAREST(GL2.GL_NEAREST),
	/** Linear magnification - color using linear interpolation ("smoothes" textures). */
	LINEAR(GL2.GL_LINEAR);
	
	public final int glid;
	private TextureMagFilter(int id) {glid = id;}
}

