/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

/**
 * This interface describes everything that can or must be bound
 * to the current OpenGL context.
 * @author Matthew Tropiano
 */
public interface OGLBindable
{
	/**
	 * Binds this object to the specified OGLGraphics context.
	 */
	public void bindTo(OGLGraphics g);

	/**
	 * Unbinds this object from the specified OGLGraphics context.
	 */
	public void unbindFrom(OGLGraphics g);
}
