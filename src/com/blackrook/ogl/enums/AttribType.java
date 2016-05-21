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

import com.blackrook.ogl.OGLGraphics;

/**
 * Attribute types for attribute states.
 * Usually for {@link OGLGraphics#attribPush(AttribType...)}.
 * @author Matthew Tropiano
 */
public enum AttribType
{
	/** 
	 * Color buffer attribute bit.
	 * <p>Governs:
	 * <ul>
	 * <li>Alpha test state, function, and values.</li>
	 * <li>Blending state, function, and values.</li>
	 * <li>GL_DITHER state.</li>
	 * <li>Current drawing buffer(s).</li>
	 * <li>Current logical operation state and function.</li>
	 * <li>Current RGBA/index clear color and write masks.</li> 
	 * </ul> 
	 */
	COLOR_BUFFER(GL3.GL_COLOR_BUFFER_BIT),
	DEPTH_BUFFER(GL3.GL_DEPTH_BUFFER_BIT),
	STENCIL_BUFFER(GL3.GL_STENCIL_BUFFER_BIT),
	;
	
	public final int glValue;
	private AttribType (int val) {glValue = val;}

}
