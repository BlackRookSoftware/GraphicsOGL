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
 * Shader program type.
 * @author Matthew Tropiano
 */
public enum ShaderProgramType
{
	/** Vertex shader program. */
	VERTEX(GL3.GL_VERTEX_SHADER),
	/** Geometry shader program. */
	GEOMETRY(GL3.GL_GEOMETRY_SHADER),
	/** Fragment shader program. */
	FRAGMENT(GL3.GL_FRAGMENT_SHADER);
	
	public final int glValue;
	private ShaderProgramType(int glShaderType) 
	{
		this.glValue = glShaderType;
	}

}
