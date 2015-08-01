package com.blackrook.ogl;
/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/


import com.blackrook.ogl.exception.GraphicsException;

/**
 * Standard texture class.
 * @author Matthew Tropiano
 */
public class OGLTexture extends OGLObject
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
	 * Creates a new blank texture object.
	 * @param targetName the texture target name.
	 * @param g the OGLGraphics context.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	OGLTexture(OGLGraphics g)
	{
		super(g);
	}
	
	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.clearError();
		g.getGL().glGenTextures(1, glStateNum, 0);
		g.getError();
		return glStateNum[0];
	}
	
	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.clearError();
		g.getGL().glDeleteTextures(1, glStateNum, 0);
		g.getError();
		return true;
	}
	
	/**
	 * Destroys undeleted texture objects abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteTextures(UNDELETED_LENGTH, UNDELETED_IDS, 0);
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
		{
			finalizeAddId(getGLId());
		}
		super.finalize();
	}

}
