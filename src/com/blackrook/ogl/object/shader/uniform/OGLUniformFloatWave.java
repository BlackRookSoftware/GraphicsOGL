/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader.uniform;

import com.blackrook.commons.math.wave.Wave;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.object.shader.OGLShaderProgram;

/**
 * A shader uniform floating-point type that uses wave variance 
 * in order to alter its value over time, using a waveform to 
 * dictate where to sample between two values for the final 
 * value to submit on application.
 * @author Matthew Tropiano
 */
public class OGLUniformFloatWave extends OGLUniformWave
{
	/** The uniform's value. */
	private float value;
	/** The uniform's second value. */
	private float value2;
	
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformFloatWave(String name, Wave wave)
	{
		this(name, wave, 0f, 0f);
	}

	/**
	 * Creates a new uniform.
	 */
	public OGLUniformFloatWave(String name, Wave wave, float value, float value2)
	{
		super(name, wave);
		setValues(value, value2);
	}

	/**
	 * Sets this uniform's first and second values.
	 * @param value		the first value to set.
	 * @param value2	the first value to set.
	 */
	public void setValues(float value, float value2)
	{
		this.value = value;
		this.value2 = value2;
	}
	
	/**
	 * Sets this uniform's value.
	 * @param value	the value to set.
	 */
	public void setValue(float value)
	{
		this.value = value;
	}
	
	/**
	 * Returns this uniform's value.
	 */
	public float getValue()
	{
		return value;
	}
	
	/**
	 * Sets this uniform's second value.
	 * @param value	the value to set.
	 */
	public void setValue2(float value)
	{
		this.value2 = value;
	}
	
	/**
	 * Returns this uniform's second value.
	 */
	public float getValue2()
	{
		return value2;
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		float v = wave.getInterpolatedValue(g.currentTimeMillis(), value, value2);
		g.getGL().glUniform1fARB(getUniformLocation(g, shader), v);
	}

}
