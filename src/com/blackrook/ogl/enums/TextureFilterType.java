package com.blackrook.ogl.enums;

/**
 * Combined min/mag filter types, for convenience.
 * @author Matthew Tropiano
 */
public enum TextureFilterType
{
	NEAREST(TextureMinFilter.NEAREST,TextureMagFilter.NEAREST),
	LINEAR(TextureMinFilter.LINEAR,TextureMagFilter.LINEAR),
	BILINEAR(TextureMinFilter.BILINEAR,TextureMagFilter.LINEAR),
	TRILINEAR(TextureMinFilter.TRILINEAR,TextureMagFilter.LINEAR);
	
	public final TextureMinFilter min;
	public final TextureMagFilter mag;
	private TextureFilterType(TextureMinFilter min, TextureMagFilter mag) {this.min = min; this.mag = mag;}
}

