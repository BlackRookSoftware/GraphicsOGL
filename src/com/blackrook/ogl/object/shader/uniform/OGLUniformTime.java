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
 * Shader uniform that counts time (in seconds) since first bind.
 * Its maximum value is <code>2^23</code>, so that it
 * wraps around in full seconds seamlessly.
 * Setting the value on this shader sets the time to a value. 
 * The value is always incrementing and changes each bind.
 * For this class, {@link #hasChanged()} always returns true.
 * @author Matthew Tropiano
 */
public class OGLUniformTime extends OGLUniformFloat
{
	/** Uniform's maximum value. */
	public static final int MAX_VALUE = 0x007FFFFF;
	/** Starting milliseconds. */
	private long startMillis;
	
	/**
	 * Creates this uniform.
	 * @param name the name of this uniform.
	 */
	public OGLUniformTime(String name)
	{
		super(name, 0f);
		startMillis = -1L;
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
		long t = g.currentTimeMillis();
		if (startMillis == -1L)
			startMillis = t;
		else if (t - startMillis > MAX_VALUE)
		{
			startMillis = t;
			t -= MAX_VALUE;
		}
		setValue((t - startMillis) / 1000f);
		super.apply(g, shader);
	}
	
}
