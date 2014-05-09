/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

/**
 * Interface for objects that can be drawn by the OpenGL context.
 * @author Matthew Tropiano
 */
public interface OGLDrawable
{
	/**
	 * Draws this object using an immediate method.
	 * After calling, the commands required to draw this object
	 * should be fully submitted to the OpenGL context.
	 */
	public void drawUsing(OGLGraphics g);
	
}
