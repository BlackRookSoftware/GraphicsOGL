/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL2;

/**
 * Enumeration of render buffer formats.
 * @author Matthew Tropiano
 */
public enum RenderbufferFormat
{
	RGB(GL2.GL_RGB),
	RGBA(GL2.GL_RGBA),
	DEPTH(GL2.GL_DEPTH_COMPONENT),
	STENCIL(GL2.GL_STENCIL_INDEX);
	
	public final int glid;
	private RenderbufferFormat(int id) {glid = id;}
}
