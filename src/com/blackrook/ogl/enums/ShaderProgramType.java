package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Shader program type.
 * @author Matthew Tropiano
 */
public enum ShaderProgramType
{
	/** Vertex shader program. */
	VERTEX(GL2.GL_VERTEX_PROGRAM_ARB, GL2.GL_VERTEX_SHADER),
	/** Geometry shader program. */
	GEOMETRY(GL2.GL_GEOMETRY_PROGRAM_NV, GL2.GL_GEOMETRY_SHADER_ARB),
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
