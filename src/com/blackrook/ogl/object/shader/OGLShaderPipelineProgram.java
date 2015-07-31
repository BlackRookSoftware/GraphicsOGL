/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader;

import javax.media.opengl.*;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.OGLObject;


/**
 * GLSL Shader program.
 * @author Matthew Tropiano
 */
public abstract class OGLShaderPipelineProgram extends OGLObject
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
	protected int glType;
	/** Compile log. */
	protected String log;

	/**
	 * Protected constructor for the program class.
	 * @param g the OGLGraphics instance to use.
	 */
	protected OGLShaderPipelineProgram(OGLGraphics g)
	{
		super(g);
		if (glType != GL2.GL_VERTEX_SHADER && glType != GL2.GL_FRAGMENT_SHADER && glType != GL2.GL_GEOMETRY_PROGRAM_NV)
			throw new GraphicsException("Shader program not designated as vertex, geometry, or fragment.");
	}

	/**
	 * Protected shared constructor for the program class.
	 * @param g the OGLGraphics instance to use.
	 * @param streamName the name of this stream.
	 * @param sourceCode the shader source code.
	 */
	protected void construct(OGLGraphics g, String streamName, String sourceCode)
	{
		GL2 gl = g.getGL();

		g.clearError();
		gl.glShaderSource(getGLId(), 1, new String[]{sourceCode}, (int[])null, 0);
		compile(streamName, g);
		g.getError();
	}
	
	private void compile(String streamName, OGLGraphics g)
	{
		GL2 gl = g.getGL();
		int[] compCheck = new int[1];
        gl.glCompileShader(getGLId());
        log = readLog(g);
        gl.glGetObjectParameterivARB(getGLId(), GL2.GL_OBJECT_COMPILE_STATUS_ARB, compCheck, 0);
        if (compCheck[0] == 0)
        	throw new GraphicsException("Failed to compile \""+streamName+"\"\n"+log);
	}

	/**
	 * Reads and returns the log from this program's compilation.
	 */
	private String readLog(OGLGraphics g)
	{
		GL2 gl = g.getGL();
		int[] loglen = new int[1];
        gl.glGetObjectParameterivARB(getGLId(), GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, loglen, 0);
        if (loglen[0] <= 1)
        	return "";
        byte[] log = new byte[loglen[0]];
        gl.glGetInfoLogARB(getGLId(), loglen[0], loglen, 0, log, 0);
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
	 * Destroys undeleted programs abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			for (int i = 0; i < UNDELETED_LENGTH; i++)
				g.getGL().glDeleteShader(UNDELETED_IDS[i]);
			UNDELETED_LENGTH = 0;
		}
	}

	/**
	 * Returns the log from this program's compiling.
	 */
	public String getLog()
	{
		return log;
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
