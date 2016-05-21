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
 * Reusable logical enumerations. 
 * @author Matthew Tropiano
 */
public enum LogicFunc
{
	/** Always. Always what? I dunno. Just ALWAYS. */
	ALWAYS(GL3.GL_ALWAYS),
	/** Never. Don't even think about it. */
	NEVER(GL3.GL_NEVER),
	EQUAL(GL3.GL_EQUAL),
	NOT_EQUAL(GL3.GL_NOTEQUAL),
	LESS(GL3.GL_LESS),
	GREATER(GL3.GL_GREATER),
	LESS_OR_EQUAL(GL3.GL_LEQUAL),
	GREATER_OR_EQUAL(GL3.GL_GEQUAL);
	
	public final int glValue;
	LogicFunc(int gltype) 
		{glValue = gltype;}

}
