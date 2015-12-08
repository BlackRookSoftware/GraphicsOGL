/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.*;

/**
 * Texture coordinate generation constants.
 * @author Matthew Tropiano
 */
public enum TextureGenMode
{
	/** Coordinates are created relative to the object. */
	OBJECT(GL2.GL_OBJECT_LINEAR),
	/** Coordinates are created using the eye vector. */
	EYE(GL2.GL_EYE_LINEAR),
	/** Coordinates are created using geometry normals for sphere maps. */
	SPHERE(GL2.GL_SPHERE_MAP),
	/** Coordinates are created using geometry normals for sphere maps, as if the environment were reflected. */
	REFLECTION(GL2.GL_REFLECTION_MAP),
	/** Coordinates are created using geometry normals (cube map). */
	NORMAL(GL2.GL_NORMAL_MAP);
	
	public final int glValue;
	TextureGenMode(int gltype) 
		{glValue = gltype;}

}
