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
 * Target to bind buffer objects to.
 * @author Matthew Tropiano
 */
public enum BindingTarget
{
	NONE(0),
	VERTEX(GL2.GL_VERTEX_ARRAY),
	NORMAL(GL2.GL_NORMAL_ARRAY),
	TEXTURE_COORD(GL2.GL_TEXTURE_COORD_ARRAY),
	COLOR(GL2.GL_COLOR_ARRAY);
	
	final int glValue;
	private BindingTarget (int val) {glValue = val;}
}
