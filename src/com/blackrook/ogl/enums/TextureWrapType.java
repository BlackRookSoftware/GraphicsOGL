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
 * Texture wrapping types.
 * @author Matthew Tropiano
 */
public enum TextureWrapType
{
	/** 
	 * Texture coordinates wrap to the other side. 
	 * Edge colors interpolate accordingly. 
	 */
	TILE(GL3.GL_REPEAT),
	/** 
	 * Texture coordinates clamp to [0,1]. 
	 * Edge colors are interpolated with the edge texel's color. 
	 */
	CLAMP_TO_EDGE(GL3.GL_CLAMP_TO_EDGE);

	public final int glid;
	private TextureWrapType(int id) {glid = id;}
}

