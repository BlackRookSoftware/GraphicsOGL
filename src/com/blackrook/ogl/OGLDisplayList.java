/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import com.blackrook.ogl.exception.GraphicsException;

/**
 * Display list functionality for OpenGL calls and objects.
 * @author Matthew Tropiano
 */
public class OGLDisplayList extends OGLObject implements OGLDrawable
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
	 * Creates a new, empty DisplayList.
	 * @throws GraphicsException if memory could not be allocated for the list.
	 */
	OGLDisplayList(OGLGraphics g)
	{
		super(g);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		g.clearError();
		int out = g.getGL().glGenLists(1);
		g.getError();
		return out;
	}
	
	@Override
	protected boolean free(OGLGraphics g)
	{
		g.clearError();
		g.getGL().glDeleteLists(getGLId(), 1);
		g.getError();
		return true;
	}

	/**
	 * Calls this list.
	 */
	public void drawUsing(OGLGraphics g)
	{
		g.getGL().glCallList(getGLId());
	}
	
	/**
	 * Destroys undeleted lists abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			for (int i = 0; i < UNDELETED_LENGTH; i++)
				g.getGL().glDeleteLists(UNDELETED_IDS[i], 1);
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
