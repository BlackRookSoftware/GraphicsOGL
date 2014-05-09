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
 * A shader uniform integer type that uses wave variance 
 * in order to alter its value over time, using a waveform to 
 * dictate where to sample between two values for the final 
 * value to submit on application.
 * @author Matthew Tropiano
 */
public class OGLUniformIntWave extends OGLUniformWave
{
	/** The uniform's value. */
	private int value;
	/** The uniform's second value. */
	private int value2;
	
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformIntWave(String name, Wave wave)
	{
		this(name, wave, 0, 0);
	}

	/**
	 * Creates a new uniform.
	 */
	public OGLUniformIntWave(String name, Wave wave, int value, int value2)
	{
		super(name, wave);
		setValues(value, value2);
	}

	/**
	 * Sets this uniform's first and second values.
	 * @param value		the first value to set.
	 * @param value2	the first value to set.
	 */
	public void setValues(int value, int value2)
	{
		this.value = value;
		this.value2 = value2;
	}
	
	/**
	 * Sets this uniform's value.
	 * @param value	the value to set.
	 */
	public void setValue(int value)
	{
		this.value = value;
	}
	
	/**
	 * Returns this uniform's value.
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * Sets this uniform's second value.
	 * @param value	the value to set.
	 */
	public void setValue2(int value)
	{
		this.value2 = value;
	}
	
	/**
	 * Returns this uniform's second value.
	 */
	public int getValue2()
	{
		return value2;
	}
	
	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		int v = wave.getInterpolatedValue(g.currentTimeMillis(), value, value2);
		g.getGL().glUniform1fARB(getUniformLocation(g, shader), v);
	}

}
