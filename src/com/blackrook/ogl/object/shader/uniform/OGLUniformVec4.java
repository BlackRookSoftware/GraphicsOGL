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
 * A shader uniform four-component vector type. 
 * @author Matthew Tropiano
 */
public class OGLUniformVec4 extends OGLUniform
{
	/** The uniform's value. */
	private float[] value;
	
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformVec4(String name)
	{
		this(name, 0, 0, 0, 0);
	}

	/**
	 * Creates a new uniform.
	 * @param valueX	the first component value to set.
	 * @param valueY	the second component value to set.
	 * @param valueZ	the third component value to set.
	 * @param valueW	the fourth component value to set.
	 */
	public OGLUniformVec4(String name, float valueX, float valueY, float valueZ, float valueW)
	{
		super(name);
		value = new float[4];
		setValue(valueX, valueY, valueZ, valueW);
	}

	/**
	 * Sets this uniform's values.
	 * @param valueX	the first component value to set.
	 * @param valueY	the second component value to set.
	 * @param valueZ	the third component value to set.
	 * @param valueW	the fourth component value to set.
	 */
	public void setValue(float valueX, float valueY, float valueZ, float valueW)
	{
		if (valueX != value[0] || valueY != value[1] || valueZ != value[2] || valueW != value[3])
		{
			value[0] = valueX;
			value[1] = valueY;
			value[2] = valueZ;
			value[3] = valueW;
			setChanged(true);
		}
	}
	
	/**
	 * Returns this uniform's first component value.
	 */
	public float getValueX()
	{
		return value[0];
	}
	
	/**
	 * Returns this uniform's second component value.
	 */
	public float getValueY()
	{
		return value[1];
	}
	
	/**
	 * Returns this uniform's third component value.
	 */
	public float getValueZ()
	{
		return value[2];
	}
	
	/**
	 * Returns this uniform's fourth component value.
	 */
	public float getValueW()
	{
		return value[3];
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		g.getGL().glUniform4fvARB(getUniformLocation(g, shader),1,value,0);
	}

}
