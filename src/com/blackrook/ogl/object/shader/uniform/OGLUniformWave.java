/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader.uniform;

import com.blackrook.commons.math.wave.Wave;

/**
 * A special type of uniform that alters its value over time,
 * using a waveform to dictate where to sample between two values
 * for the final value to submit on application.
 * @author Matthew Tropiano
 */
public abstract class OGLUniformWave extends OGLUniform
{
	/** The wave object responsible for sampling the final value. */
	protected Wave wave;
	
	/**
	 * Creates a new shader program uniform.
	 * @param name uniform name.
	 * @param wave the wave object to use.
	 */
	public OGLUniformWave(String name, Wave wave)
	{
		super(name);
		this.wave = wave;
	}

	/**
	 * Gets the wave object responsible for sampling the final value.
	 */
	public Wave getWave()
	{
		return wave;
	}

	/**
	 * Sets the wave object responsible for sampling the final value.
	 */
	public void setWave(Wave wave)
	{
		this.wave = wave;
	}

	/**
	 * Always returns true.
	 */
	@Override
	public boolean hasChanged()
	{
		return true;
	}
	
}
