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

