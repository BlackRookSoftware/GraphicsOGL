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
 * Enumeration for shading types.
 * @author Matthew Tropiano
 */
public enum LightShadeType
{
	/** Use smooth (Gouraud) shading on polygons. */
	SMOOTH(GL2.GL_SMOOTH),
	/** Use flat shading on polygons (one face, one color). */
	FLAT(GL2.GL_FLAT);
	
	
	public final int glValue;
	LightShadeType(int gltype) 
		{glValue = gltype;}
}

