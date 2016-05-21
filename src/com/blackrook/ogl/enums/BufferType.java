/*******************************************************************************
 * Copyright (c) 2014, 2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * Contributors:
 *     Matt Tropiano - initial API and implementation
 *******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL3;

/**
 * Type enumerant for VBO, Texture Buffer, or other one-dimensional buffer types.
 * @author Matthew Tropiano
 */
public enum BufferType
{
	/** Buffer holds GEOMETRY information (internally, this is GL_ARRAY_BUFFER). */
	GEOMETRY(GL3.GL_ARRAY_BUFFER),
	/** Buffer holds ELEMENT INDEX information (internally, this is GL_ELEMENT_ARRAY_BUFFER). */
	INDICES(GL3.GL_ELEMENT_ARRAY_BUFFER),
	/** Buffer contains unpacked data (raw pixel data to be sent to OpenGL or read from OpenGL to an application). */
	PIXEL(GL3.GL_PIXEL_UNPACK_BUFFER),
	/** Buffer contains packed data (raw data specific to OpenGL implementation). */
	DATA(GL3.GL_PIXEL_PACK_BUFFER),
	/** Buffer texture name data (for shaders). */
	TEXTURE(GL3.GL_TEXTURE_BUFFER),
	/** Buffer uniform data (for shaders). */
	UNIFORM(GL3.GL_UNIFORM_BUFFER);
	
	public final int glValue;
	BufferType(int gltype) 
		{glValue = gltype;}

}
