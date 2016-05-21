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
 * Enumeration of OpenGL FrameBuffer types.
 * @author Matthew Tropiano
 */
public enum FrameBufferType
{
	NONE(GL3.GL_NONE),
	FRONT(GL3.GL_FRONT),
	BACK(GL3.GL_BACK),
	LEFT(GL3.GL_LEFT),
	RIGHT(GL3.GL_RIGHT),
	FRONT_LEFT(GL3.GL_FRONT_LEFT),
	FRONT_RIGHT(GL3.GL_FRONT_RIGHT),
	BACK_LEFT(GL3.GL_BACK_LEFT),
	BACK_RIGHT(GL3.GL_BACK_RIGHT),
	;
	
	public final int glValue;
	private FrameBufferType (int val) {glValue = val;}
}
