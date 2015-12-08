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

import com.jogamp.opengl.GL;

/**
 * Enumeration of testing function constants.
 * @author Matthew Tropiano
 */
public enum StencilTestFunc
{
	KEEP(GL.GL_KEEP),
	ZERO(GL.GL_ZERO),
	REPLACE(GL.GL_REPLACE),
	INCREMENT(GL.GL_INCR),
	INCREMENT_WRAP(GL.GL_INCR_WRAP),
	DECREMENT(GL.GL_DECR),
	DECREMENT_WRAP(GL.GL_DECR_WRAP),
	INVERT(GL.GL_INVERT);
	
	public final int glValue;
	StencilTestFunc(int gltype) 
		{glValue = gltype;}

}
