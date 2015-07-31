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
 * A shader uniform floating-point type. 
 * @author Matthew Tropiano
 */
public class OGLUniformFloat extends OGLUniform
{
	/** The uniform's value. */
	private float value;
	
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformFloat(String name)
	{
		this(name, 0);
	}

	/**
	 * Creates a new uniform.
	 */
	public OGLUniformFloat(String name, float value)
	{
		super(name);
		setValue(value);
	}

	/**
	 * Sets this uniform's value.
	 * @param value	the value to set.
	 */
	public void setValue(float value)
	{
		if (value != this.value)
		{
			this.value = value;
			setChanged(true);
		}
	}
	
	/**
	 * Returns this uniform's value.
	 */
	public float getValue()
	{
		return value;
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		g.getGL().glUniform1fARB(shader.getUniformLocation(g, getName()), value);
	}

}
