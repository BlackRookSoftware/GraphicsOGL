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
 * Enumeration of OpenGL FrameBuffer types.
 * @author Matthew Tropiano
 */
public enum FrameBufferType
{
	NONE(GL2.GL_NONE),
	FRONT(GL2.GL_FRONT),
	BACK(GL2.GL_BACK),
	LEFT(GL2.GL_LEFT),
	RIGHT(GL2.GL_RIGHT),
	FRONT_LEFT(GL2.GL_FRONT_LEFT),
	FRONT_RIGHT(GL2.GL_FRONT_RIGHT),
	BACK_LEFT(GL2.GL_BACK_LEFT),
	BACK_RIGHT(GL2.GL_BACK_RIGHT),
	FRONT_AND_BACK(GL2.GL_FRONT_AND_BACK),
	AUX0(GL2.GL_AUX0),
	AUX1(GL2.GL_AUX1),
	AUX2(GL2.GL_AUX2),
	AUX3(GL2.GL_AUX3);
	
	public final int glValue;
	private FrameBufferType (int val) {glValue = val;}
}
