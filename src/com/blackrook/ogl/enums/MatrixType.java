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
 * Enumeration of Matrix types. 
 * @author Matthew Tropiano
 */
public enum MatrixType
{
	MODELVIEW(GL2.GL_MODELVIEW, GL2.GL_MODELVIEW_MATRIX),
	PROJECTION(GL2.GL_PROJECTION, GL2.GL_PROJECTION_MATRIX),
	TEXTURE(GL2.GL_TEXTURE, GL2.GL_TEXTURE_MATRIX),
	COLOR(GL2.GL_COLOR, GL2.GL_COLOR_MATRIX);
	
	public final int glValue;
	public final int glReadValue;
	MatrixType(int glValue, int glReadValue) 
	{
		this.glValue = glValue;
		this.glReadValue = glReadValue;
	}

}
