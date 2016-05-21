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
package com.blackrook.ogl;

import com.jogamp.opengl.*;

import com.blackrook.ogl.enums.ShaderProgramType;
import com.blackrook.ogl.exception.GraphicsException;

/**
 * GLSL Shader program.
 * @author Matthew Tropiano
 */
public abstract class OGLShaderProgram extends OGLObject
{
	/** List of OpenGL object ids that were not deleted properly. */
	protected static int[] UNDELETED_IDS;
	/** Amount of OpenGL object ids that were not deleted properly. */
	protected static int UNDELETED_LENGTH;
	
	static
	{
		UNDELETED_IDS = new int[INIT_UNALLOC_SIZE];
		UNDELETED_LENGTH = 0;
	}

	/** Shader program type. */
	private ShaderProgramType type;
	/** Compile log. */
	protected String log;

	/**
	 * Protected constructor for the program class.
	 * @param g the OGLGraphics instance to use.
	 */
	OGLShaderProgram(OGLGraphics g, ShaderProgramType type)
	{
		super(g);
		this.type = type;
	}

	/**
	 * Protected shared constructor for the program class.
	 * @param g the OGLGraphics instance to use.
	 * @param streamName the name of this stream.
	 * @param sourceCode the shader source code.
	 */
	protected void construct(OGLGraphics g, String streamName, String sourceCode)
	{
		GL3 gl = g.getGL();
		g.clearError();
		gl.glShaderSource(getGLId(), 1, new String[]{sourceCode}, (int[])null, 0);
		compile(g, streamName);
		g.getError();
	}
	
	// Compiles the program.
	private void compile(OGLGraphics g, String streamName)
	{
		GL3 gl = g.getGL();
		int[] compCheck = new int[1];
        gl.glCompileShader(getGLId());
        log = readLog(g);
        gl.glGetProgramiv(getGLId(), GL3.GL_COMPILE_STATUS, compCheck, 0);
        if (compCheck[0] == 0)
        	throw new GraphicsException("Failed to compile \""+streamName+"\"\n"+log);
	}

	// Reads and returns the log from this program's compilation.
	private String readLog(OGLGraphics g)
	{
		GL3 gl = g.getGL();
		int[] loglen = new int[1];
        gl.glGetProgramiv(getGLId(), GL3.GL_INFO_LOG_LENGTH, loglen, 0);
        if (loglen[0] <= 1)
        	return "";
        byte[] log = new byte[loglen[0]];
        gl.glGetProgramInfoLog(getGLId(), loglen[0], loglen, 0, log, 0);
        return new String(log).substring(0,loglen[0]-1);
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		g.clearError();
		g.getGL().glDeleteShader(getGLId());
		g.getError();
		return true;
	}

	/**
	 * Gets the shader type.
	 */
	public ShaderProgramType getType()
	{
		return type;
	}
	
	/**
	 * Returns the log from this program's compiling.
	 */
	public String getLog()
	{
		return log;
	}

	/**
	 * Destroys undeleted programs abandoned from destroyed Java objects.
	 */
	static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			for (int i = 0; i < UNDELETED_LENGTH; i++)
				g.getGL().glDeleteShader(UNDELETED_IDS[i]);
			UNDELETED_LENGTH = 0;
		}
	}

	// adds the OpenGL Id to the UNDELETED_IDS list.
	private static void finalizeAddId(int id)
	{
		if (UNDELETED_LENGTH == UNDELETED_IDS.length)
		{
			int[] newArray = new int[UNDELETED_IDS.length * 2];
			System.arraycopy(UNDELETED_IDS, 0, newArray, 0, UNDELETED_LENGTH);
			UNDELETED_IDS = newArray;
		}
		UNDELETED_IDS[UNDELETED_LENGTH++] = id;
	}
	
	@Override
	public void finalize() throws Throwable
	{
		if (isAllocated())
			finalizeAddId(getGLId());
		super.finalize();
	}

}
