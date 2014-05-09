/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader;

import javax.media.opengl.*;

import com.blackrook.commons.Common;
import com.blackrook.ogl.OGLBindable;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.*;
import com.blackrook.ogl.object.OGLObject;
import com.blackrook.ogl.object.shader.uniform.OGLUniform;

/**
 * The main shader program class.
 * @author Matthew Tropiano
 */
public class OGLShaderProgram extends OGLObject implements OGLBindable
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

	/** Vertex program. */
	protected OGLShaderVertexProgram vProg;
	/** Geometry program. */
	protected OGLShaderGeometryProgram gProg;
	/** Fragment program. */
	protected OGLShaderFragmentProgram fProg;
	/** List of bound uniforms. */
	protected OGLUniform[] uniforms;
	
	/* == After link == */
	
	/** The shader log. */
	protected String log;
	/** Map of  */
	
	/**
	 * Creates a new Shader.
	 * @param vp the vertex program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderVertexProgram vp)
	{
		this(g, vp, null, null);
	}
	
	/**
	 * Creates a new Shader.
	 * @param gp the geometry program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderGeometryProgram gp)
	{
		this(g, null, gp, null);
	}
	
	/**
	 * Creates a new Shader.
	 * @param fp the fragment program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderFragmentProgram fp)
	{
		this(g, null, null, fp);
	}
	
	/**
	 * Creates a new Shader. Each program can be null and is just
	 * left absent in the complete program.
	 * @param vp the vertex program.
	 * @param fp the fragment program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderVertexProgram vp, OGLShaderFragmentProgram fp)
	{
		this(g, vp, null, fp);
	}
	
	/**
	 * Creates a new Shader. Each program can be null and is just
	 * left absent in the complete program.
	 * @param vp the vertex program.
	 * @param gp the fragment program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderVertexProgram vp, OGLShaderGeometryProgram gp)
	{
		this(g, vp, gp, null);
	}
	
	/**
	 * Creates a new Shader. Each program can be null and is just
	 * left absent in the complete program.
	 * @param gp the geometry program.
	 * @param fp the fragment program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderGeometryProgram gp, OGLShaderFragmentProgram fp)
	{
		this(g, null, gp, fp);
	}
	
	/**
	 * Creates a new Shader. Each program can be null and is just
	 * left absent in the complete program.
	 * @param vp the vertex program.
	 * @param gp the geometry program.
	 * @param fp the fragment program.
	 */
	public OGLShaderProgram(OGLGraphics g, OGLShaderVertexProgram vp, OGLShaderGeometryProgram gp, OGLShaderFragmentProgram fp)
	{
		super(g);
		vProg = vp;
		gProg = gp;
		fProg = fp;

		GL2 gl = g.getGL();

		g.clearError();
		
		if (Common.coalesce(vp, gp, fp) == null)
			throw new GraphicsException("All provided programs are null!");
		
		// attach the programs.
		if (vProg != null)
			gl.glAttachObjectARB(getGLId(), vProg.getGLId());
		if (gProg != null)
			gl.glAttachObjectARB(getGLId(), gProg.getGLId());
		if (fProg != null)
			gl.glAttachObjectARB(getGLId(), fProg.getGLId());

		// did they link properly? (unf, unf)
		link(g);
		g.getError();
	}
	
	/**
	 * Sets the shader uniforms that this shader uses.
	 */
	public void setUniforms(OGLUniform ... uniforms)
	{
		this.uniforms = uniforms;
	}
	
	/**
	 * Links the Fragment and Vertex programs.
	 * @throws GraphicsException	if the programs failed to link.
	 */
	private void link(OGLGraphics g) throws GraphicsException
	{
		GL2 gl = g.getGL();
		int[] compCheck = new int[1];
		
	    gl.glLinkProgramARB(getGLId());
	    
	    log = readLog(g);

	    gl.glGetObjectParameterivARB(getGLId(), GL2.GL_OBJECT_LINK_STATUS_ARB, compCheck, 0);
	    if (compCheck[0] == 0)
	    	throw new GraphicsException("Failed to link together program "+getGLId()+".\n"+log);
	}

	// Pulls log data from shader compiler.
	private String readLog(OGLGraphics g)
	{
		GL2 gl = g.getGL();
		int[] loglen = new int[1];
		int program = getGLId();
	    gl.glGetObjectParameterivARB(program, GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, loglen, 0);
        if (loglen[0] <= 1)
        	return "";
	    byte[] log = new byte[loglen[0]];
	    gl.glGetInfoLogARB(program, loglen[0], loglen, 0, log, 0);
	    return new String(log).substring(0,loglen[0]-1);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		return g.getGL().glCreateProgramObjectARB();
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		g.clearError();
		g.getGL().glDeleteObjectARB(getGLId());
		g.getError();
		return true;
	}
	
	/**
	 * Binds this shader program to the specified OGLGraphics context.
	 * This also will re-apply any changes made to the uniforms on this shader,
	 * so in order to apply changes to the context, re-bind the shader. It will
	 * automatically figure out what to re-apply and skip based on whether the uniform
	 * values change or not.
	 */
	public void bindTo(OGLGraphics g)
	{
		g.getGL().glUseProgramObjectARB(getGLId());
		if (uniforms != null) for (OGLUniform u : uniforms)
			if (u.hasChanged())
				u.set(g, this);
	}

	@Override
	public void unbindFrom(OGLGraphics g)
	{
		g.getGL().glUseProgramObjectARB(0);
	}

	/**
	 * Returns the log from this program's linking.
	 */
	public String getLog()
	{
		return log;
	}
	
	/**
	 * Destroys undeleted shader programs abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			for (int i = 0; i < UNDELETED_LENGTH; i++)
				g.getGL().glDeleteObjectARB(UNDELETED_IDS[i]);
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

