/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.buffer;

import java.nio.ByteBuffer;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.AccessType;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.DataType;

/**
 * Defines an OpenGL byte buffer.
 * @author Matthew Tropiano
 */
public class OGLByteBuffer extends OGLBuffer<ByteBuffer>
{
	/**
	 * Creates a new OGLByteBuffer
	 * @param g the graphics context.
	 * @param type the type of data contained within this buffer.
	 */
	public OGLByteBuffer(OGLGraphics g, BufferType type)
	{
		super(g, type, DataType.BYTE);
	}

	@Override
	public ByteBuffer mapBuffer(OGLGraphics g, AccessType accessType)
	{
		return map(g, accessType);
	}

}
