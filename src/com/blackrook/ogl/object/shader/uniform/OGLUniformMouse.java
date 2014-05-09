/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader.uniform;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.object.shader.OGLShaderProgram;

/**
 * Shader uniform that passes current canvas mouse coordinates to a GLSL <code>vec2</code>.
 * For this class, {@link #hasChanged()} always returns true.
 * @author Matthew Tropiano
 */
public class OGLUniformMouse extends OGLUniformVec2
{
	/**
	 * Creates this uniform.
	 * @param name the name of this uniform.
	 */
	public OGLUniformMouse(String name)
	{
		super(name, 0f, 0f);
	}

	/** Always returns true. */
	@Override
	public boolean hasChanged()
	{
		return true;
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		setValue(g.getMouseX(), g.getMouseY());
		super.apply(g, shader);
	}
	
}
