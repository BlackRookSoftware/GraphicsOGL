/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import javax.media.opengl.*;

/**
 * Enumeration of Matrix types. 
 * @author Matthew Tropiano
 */
public enum MatrixType
{
	MODELVIEW(GL2.GL_MODELVIEW),
	PROJECTION(GL2.GL_PROJECTION),
	TEXTURE(GL2.GL_TEXTURE),
	COLOR(GL2.GL_COLOR);
	
	public final int glValue;
	MatrixType(int gltype) {glValue = gltype;}

}
