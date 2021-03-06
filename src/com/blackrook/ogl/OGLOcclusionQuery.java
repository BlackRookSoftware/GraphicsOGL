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

/**
 * An encapsulation of an occlusion query object for OpenGL.
 * @author Matthew Tropiano
 */
public class OGLOcclusionQuery extends OGLObject
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
	
	/**
	 * Creates a new Occlusion Query object handle.
	 * @param g the graphics context to use.
	 */
	OGLOcclusionQuery(OGLGraphics g)
	{
		super(g);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.clearError();
		g.getGL().glGenQueries(1, glStateNum, 0);
		g.getError();
		return glStateNum[0];
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.clearError();
		g.getGL().glDeleteQueries(1,glStateNum,0);
		g.getError();
		return true;
	}
	
	/**
	 * Returns true if this query's results are available, false otherwise.
	 * @param g the graphics context to use.
	 */
	public boolean isReady(OGLGraphics g)
	{
		g.getGL().glGetQueryObjectiv(getGLId(), GL2.GL_QUERY_RESULT_AVAILABLE, glStateNum, 0);
		return glStateNum[0] == GL.GL_TRUE;
	}

	/**
	 * Gets how many samples have passed the query.
	 * If 0, what was drawn was completely occluded.
	 * Is only accurate if {@link OGLOcclusionQuery#isReady(OGLGraphics)} returns true.
	 * @param g the graphics context to use.
	 */
	public int getSamplesPassed(OGLGraphics g)
	{
		g.getGL().glGetQueryObjectiv(getGLId(), GL2.GL_QUERY_RESULT, glStateNum, 0);
		return glStateNum[0];
	}

	/**
	 * Destroys undeleted query objects abandoned from destroyed Java objects.
	 */
	static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteQueries(UNDELETED_LENGTH, UNDELETED_IDS, 0);
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
