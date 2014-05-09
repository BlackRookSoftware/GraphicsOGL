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
 * A shader uniform four-component vector type that uses wave variance 
 * in order to alter its value over time, using a waveform to 
 * dictate where to sample between two values for the final 
 * value to submit on application.
 * @author Matthew Tropiano
 */
public class OGLUniformVec4Wave extends OGLUniformWave
{
	/** The uniform's value. */
	private float[] value;
	/** The uniform's second value. */
	private float[] value2;
	
	// temp array for interp.
	private float[] temp;
	
	/**
	 * Creates a new uniform.
	 */
	public OGLUniformVec4Wave(String name, Wave wave)
	{
		this(name, wave, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	/**
	 * Creates a new uniform.
	 * @param valueX	the first component value to set.
	 * @param valueY	the second component value to set.
	 * @param valueZ	the third component value to set.
	 * @param valueW	the fourth component value to set.
	 * @param valueX2	the first component value to set on the second vector.
	 * @param valueY2	the second component value to set on the second vector.
	 * @param valueZ2	the third component value to set on the second vector.
	 * @param valueW2	the fourth component value to set on the second vector.
	 */
	public OGLUniformVec4Wave(String name, Wave wave, 
			float valueX, float valueY, float valueZ, float valueW,
			float valueX2, float valueY2, float valueZ2, float valueW2)
	{
		super(name, wave);
		value = new float[4];
		value2 = new float[4];
		temp = new float[4];
		setValues(valueX, valueY, valueZ, valueW, valueX2, valueY2, valueZ2, valueW2);
	}

	/**
	 * Sets this uniform's values.
	 * @param valueX	the first component value to set.
	 * @param valueY	the second component value to set.
	 * @param valueZ	the third component value to set.
	 * @param valueW	the fourth component value to set.
	 * @param valueX2	the first component value to set on the second vector.
	 * @param valueY2	the second component value to set on the second vector.
	 * @param valueZ2	the third component value to set on the second vector.
	 * @param valueW2	the fourth component value to set on the second vector.
	 */
	public void setValues(float valueX, float valueY, float valueZ, float valueW,
			float valueX2, float valueY2, float valueZ2, float valueW2)
	{
		value[0] = valueX;
		value[1] = valueY;
		value[2] = valueZ;
		value[3] = valueW;
		value2[0] = valueX2;
		value2[1] = valueY2;
		value2[2] = valueZ2;
		value2[3] = valueW2;
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
		value[0] = valueX;
		value[1] = valueY;
		value[2] = valueZ;
		value[3] = valueW;
	}

	/**
	 * Sets this uniform's secondary values.
	 * @param valueX2	the first secondary component value to set.
	 * @param valueY2	the second secondary component value to set.
	 * @param valueZ2	the third secondary component value to set.
	 * @param valueW2	the fourth component value to set on the second vector.
	 */
	public void setValue2(float valueX2, float valueY2, float valueZ2, float valueW2)
	{
		value2[0] = valueX2;
		value2[1] = valueY2;
		value2[2] = valueZ2;
		value2[3] = valueW2;
	}

	/**
	 * Returns a reference to this uniform's value array.
	 */
	public float[] getValue()
	{
		return value;
	}

	/**
	 * Returns a reference to this uniform's value array.
	 */
	public float[] getValue2()
	{
		return value2;
	}

	@Override
	public void apply(OGLGraphics g, OGLShaderProgram shader)
	{
		wave.getInterpolatedValue(g.currentTimeMillis(), value, value2, temp);
		g.getGL().glUniform4fvARB(getUniformLocation(g, shader), 1, temp, 0);
	}

}
