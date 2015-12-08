/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL2;

/**
 * Shader program type.
 * @author Matthew Tropiano
 */
public enum ShaderProgramType
{
	/** Vertex shader program. */
	VERTEX(GL2.GL_VERTEX_PROGRAM_ARB, GL2.GL_VERTEX_SHADER),
	/** Geometry shader program. */
	GEOMETRY(GL2.GL_GEOMETRY_PROGRAM_NV, 0x8ddb),
	/** Fragment shader program. */
	FRAGMENT(GL2.GL_FRAGMENT_PROGRAM_ARB, GL2.GL_FRAGMENT_SHADER);
	
	public final int glValue;
	public final int glShaderType;
	private ShaderProgramType(int glValue, int glShaderType) 
	{
		this.glValue = glValue;
		this.glShaderType = glShaderType;
	}

}
