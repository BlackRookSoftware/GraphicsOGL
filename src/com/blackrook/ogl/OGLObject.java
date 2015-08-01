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
 * Common OpenGL object functions for objects on an OpenGL context.
 * @author Matthew Tropiano
 */
public abstract class OGLObject
{
	/** Unallocated id. */
	public static final int ID_NONE = 0;
	/** Unallocated id. */
	protected static final int INIT_UNALLOC_SIZE = 5;
	
	/** OpenGL object id. */
	private int glId;
	/** OpenGL temp variable. */
	protected int[] glStateNum;
	
	/**
	 * Creates a new OGLObject and calls initialize.
	 */
	protected OGLObject(OGLGraphics g)
	{
		glId = ID_NONE;
		glStateNum = new int[1];
		initialize(g);
	}
	
	/**
	 * Frees this object's resources by calling free() and
	 * invalidates this object's context.
	 * Calling this on an invalidated/non-initialized object does nothing.
	 */
	public final void destroy(OGLGraphics g)
	{
		if (isAllocated())
		{
			if (free(g))
				glId = ID_NONE;
		}
	}
	
	/**
	 * Gets the OpenGL id of this object.
	 */
	public final int getGLId()
	{
		return glId;
	}

	/**
	 * Is this object allocated?
	 */
	public boolean isAllocated()
	{
		return glId != ID_NONE;
	}

	/**
	 * Reallocates an id for this object by calling allocate(),
	 * if and only if this object is not validated yet.
	 * Calling this on a valid object does nothing.
	 */
	protected final void initialize(OGLGraphics g)
	{
		if (!isAllocated())
		{
			glId = allocate(g);
			if (!isAllocated())
				throw new GraphicsException("Object could not be allocated.");
		}
	}

	/**
	 * Allocates this object and returns an OpenGL id of the the newly-created
	 * object.
	 */
	protected abstract int allocate(OGLGraphics g);

	/**
	 * Frees this object's resources.
	 * Returns true if free was successful, false otherwise.
	 */
	protected abstract boolean free(OGLGraphics g);
		
}
