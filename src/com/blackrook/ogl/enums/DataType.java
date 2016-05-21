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
 * Enumeration of DataTypes.
 * @author Matthew Tropiano
 */
public enum DataType
{
	BYTE(GL3.GL_BYTE, 1),
	UNSIGNED_BYTE(GL3.GL_UNSIGNED_BYTE, 1),
	SHORT(GL3.GL_SHORT, 2),
	UNSIGNED_SHORT(GL3.GL_UNSIGNED_SHORT, 2),
	FLOAT(GL3.GL_FLOAT, 4),
	INTEGER(GL3.GL_INT, 4),
	UNSIGNED_INTEGER(GL3.GL_UNSIGNED_INT, 4),
	DOUBLE(GL3.GL_DOUBLE, 8);
	
	public final int glValue;
	public final int size;
	DataType(int val, int size) 
		{glValue = val; this.size = size;}
}
