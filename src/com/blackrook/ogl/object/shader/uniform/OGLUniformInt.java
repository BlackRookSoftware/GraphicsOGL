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
 * A shader uniform integer type. 
 * @author Matthew Tropiano
 */
public class OGLUniformInt extends OGLUniform
{
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformInt(String name)
	{
		this(name, 0);
	}

	/**
	 * Creates a new uniform.
	 */
	public OGLUniformInt(String name, int value)
	{
		super(name);
		setValue(value);
	}

	/**
	 * Sets this uniform's value.
	 * @param value	the value to set.
	 */
	public void setValue(int value)
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
	public int getValue()
	{
		return value;
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		g.getGL().glUniform1iARB(getUniformLocation(g, shader), value);
	}

}
