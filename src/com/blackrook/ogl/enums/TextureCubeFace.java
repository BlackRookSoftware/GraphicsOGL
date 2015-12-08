/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL;

/**
 * Texture coordinate generation constants.
 * @author Matthew Tropiano
 */
public enum TextureCubeFace
{
	/** Cube +X face. */
	PX(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X),
	/** Cube +Y face. */
	PY(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
	/** Cube +Z face. */
	PZ(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
	/** Cube -X face. */
	NX(GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
	/** Cube -Y face. */
	NY(GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y),
	/** Cube -Z face. */
	NZ(GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
	
	public final int glValue;
	TextureCubeFace(int gltype) 
		{glValue = gltype;}

}
