/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

/**
 * Framebuffer object for whatever you wanna do with off-screen rendering.
 * It can bind itself to Texture2Ds and RenderBuffers and stuff.
 * @author Matthew Tropiano
 */
public class OGLFrameBuffer extends OGLObject
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
	 * Constructs a new FrameBuffer object.
	 */
	OGLFrameBuffer(OGLGraphics g)
	{
		super(g);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.clearError();
		g.getGL().glGenFramebuffers(1, glStateNum, 0);
		g.getError();
		return glStateNum[0];
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.clearError();
		g.getGL().glDeleteFramebuffers(1, glStateNum, 0);
		g.getError();
		return true;
	}

	/**
	 * Destroys undeleted buffers abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteFramebuffers(UNDELETED_LENGTH, UNDELETED_IDS, 0);
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
