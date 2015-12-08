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

import com.jogamp.opengl.*;

/**
 * Texture coordinate generation constants.
 * @author Matthew Tropiano
 */
public enum TextureCoordType
{
	/** Texture S coordinate (width). */
	S(GL2.GL_S),
	/** Texture T coordinate (height). */
	T(GL2.GL_T),
	/** Texture R coordinate (depth). */
	R(GL2.GL_R),
	/** Texture Q coordinate (I don't know). */
	Q(GL2.GL_Q);
	
	public final int glValue;
	TextureCoordType(int gltype) 
		{glValue = gltype;}

}
