/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.query;

import javax.media.opengl.*;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.OGLObject;

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
	/** Reference to current running query. */
	private static OGLOcclusionQuery currentQuery;
	
	/** OpenGL temp variable. */
	protected int[] glStateNum;

	static
	{
		currentQuery = null;
		UNDELETED_IDS = new int[INIT_UNALLOC_SIZE];
		UNDELETED_LENGTH = 0;
	}
	
	/**
	 * Creates a new Occlusion Query object handle.
	 * @param g the graphics context to use.
	 */
	public OGLOcclusionQuery(OGLGraphics g)
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
	 * Starts the occlusion query.
	 * Between startQuery() and endQuery(), geometry is drawn, and the amount of samples
	 * that pass the depth/stencil test get counted, so make sure the depth/stencil test is enabled!
	 * <p>
	 * Two queries cannot overlap each other, or an exception will be thrown! 
	 * @param g the graphics context to use.
	 * @throws GraphicsException if a query is already in progress.
	 */
	public void startQuery(OGLGraphics g)
	{
		if (currentQuery != null)
			throw new GraphicsException("An occlusion query is already active.");
		g.getGL().glBeginQuery(GL2.GL_SAMPLES_PASSED, getGLId());
		currentQuery = this;
	}

	/**
	 * Ends the occlusion query.
	 * Between startQuery() and endQuery(), geometry is drawn, and the amount of samples
	 * that pass the depth/stencil test get counted, so make sure the depth/stencil test is enabled!
	 * <p>
	 * Two queries cannot overlap each other, or an exception will be thrown! 
	 * @param g the graphics context to use.
	 */
	public void endQuery(OGLGraphics g)
	{
		if (currentQuery == null)
			throw new GraphicsException("Attempt to end query without starting one.");
		if (currentQuery != this)
			throw new GraphicsException("Attempt to end different query before ending current.");
		g.getGL().glEndQuery(GL2.GL_SAMPLES_PASSED);
		currentQuery = null;
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
	public static void destroyUndeleted(OGLGraphics g)
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
