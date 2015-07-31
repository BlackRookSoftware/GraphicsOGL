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
 * A shader Uniform class for holding a uniform name plus associated data.
 * @author Matthew Tropiano
 */
public abstract class OGLUniform
{
	/** Uniform name. */
	private String name;

	/**
	 * Creates a new shader program uniform.
	 * @param name uniform name.
	 */
	public OGLUniform(String name)
	{
		this.name = name;
	}
	
	/**
	 * Applies the value of this uniform to a uniform location on the current shader.
	 * @param g	the OGLGraphics context.
	 * @param locationId the uniform location.
	 */
	public abstract void apply(OGLGraphics g, int locationId);

	/**
	 * @return the name of this uniform.
	 */
	public String getName()
	{
		return name;
	}
	
}
