/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.buffer;

import java.nio.DoubleBuffer;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.AccessType;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.DataType;

/**
 * Defines an OpenGL 64-bit floating-point buffer.
 * @author Matthew Tropiano
 */
public class OGLDoubleBuffer extends OGLBuffer<DoubleBuffer>
{
	/**
	 * Creates a new OGLDoubleBuffer
	 * @param g the graphics context.
	 * @param type the type of data contained within this buffer.
	 */
	public OGLDoubleBuffer(OGLGraphics g, BufferType type)
	{
		super(g, type, DataType.DOUBLE);
	}

	@Override
	public DoubleBuffer mapBuffer(OGLGraphics g, AccessType accessType)
	{
		return map(g, accessType).asDoubleBuffer();
	}

}
