package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Texture wrapping types.
 * @author Matthew Tropiano
 */
public enum TextureWrapType
{
	/** 
	 * Texture coordinates wrap to the other side. 
	 * Edge colors interpolate accordingly. 
	 */
	TILE(GL2.GL_REPEAT),
	/** 
	 * Texture coordinates clamp to [0,1]. 
	 * Edge colors are interpolated with the border color. 
	 */
	CLAMP(GL2.GL_CLAMP),
	/** 
	 * Texture coordinates clamp to [0,1]. 
	 * Edge colors are interpolated with the edge texel's color. 
	 */
	CLAMP_TO_EDGE(GL2.GL_CLAMP_TO_EDGE);

	public final int glid;
	private TextureWrapType(int id) {glid = id;}
}

