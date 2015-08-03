/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
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

