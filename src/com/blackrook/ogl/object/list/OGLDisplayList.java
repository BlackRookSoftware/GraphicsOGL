/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.list;

import javax.media.opengl.*;

import com.blackrook.ogl.OGLDrawable;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.OGLObject;


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
	
	/** Current id of the display list in . */
	private static OGLDisplayList currentList; 
	
	static
	{
		currentList = null;
		UNDELETED_IDS = new int[INIT_UNALLOC_SIZE];
		UNDELETED_LENGTH = 0;
	}
	
	/**
	 * Creates a new DisplayList, compiling it using the procedures in an 
	 * @throws GraphicsException if memory could not be allocated for the list.
	 */
	public OGLDisplayList(OGLGraphics g, OGLDrawable d)
	{
		super(g);
		startList(g);
		d.drawUsing(g);
		endList(g);
	}

	/**
	 * Creates a new, empty DisplayList.
	 * @throws GraphicsException if memory could not be allocated for the list.
	 */
	public OGLDisplayList(OGLGraphics g)
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
	 * Initializes the list for compiling draw commands.
	 * While one list is started, another one cannot be.
	 * NOTE: Not all calls to OGLGraphics can be encapsulated in a list!
	 * @throws GraphicsException if another list has already been started, but not ended.
	 */
	public void startList(OGLGraphics g)
	{
		if (currentList != null)
			throw new GraphicsException("A list has already been started.");
		g.getGL().glNewList(getGLId(), GL2.GL_COMPILE);
		currentList = this;
	}
	
	/**
	 * Ends the current list for compiling draw commands.
	 * Cannot end a list without starting one.
	 * @throws GraphicsException if another list has not been started, 
	 * or this is called when a different list was started.
	 */
	public void endList(OGLGraphics g)
	{
		if (currentList == null)
			throw new GraphicsException("Attempt to end list without starting one.");
		if (currentList != this)
			throw new GraphicsException("Attempt to end different list before ending current.");
		g.getGL().glEndList();
		currentList = null;
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
