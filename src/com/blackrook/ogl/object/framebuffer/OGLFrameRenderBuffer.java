/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.framebuffer;

import javax.media.opengl.*;

import com.blackrook.ogl.OGLBindable;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.OGLObject;

/**
 * A buffer for off-screen rendering that can be bound to FrameBuffers.
 * @author Matthew Tropiano
 */
public class OGLFrameRenderBuffer extends OGLObject implements OGLBindable
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

	public enum Format
	{
		RGB(GL2.GL_RGB),
		RGBA(GL2.GL_RGBA),
		DEPTH(GL2.GL_DEPTH_COMPONENT),
		STENCIL(GL2.GL_STENCIL_INDEX);
		
		public final int glid;
		private Format(int id) {glid = id;}
	}
	
	/** OpenGL temp variable. */
	protected int[] glStateNum;
	/** Internal format of the buffer. */
	protected Format format;
	/** Buffer width. */
	protected int width;
	/** Buffer height. */
	protected int height;

	/**
	 * Constructs a new RenderBuffer object.
	 */
	public OGLFrameRenderBuffer(OGLGraphics g, Format format, int width, int height)
	{
		super(g);
		if (width < 1 || height < 1)
			throw new GraphicsException("Render buffer size cannot be less than 1 in any dimension.");
		this.format = format;
		this.width = width;
		this.height = height;
		bindTo(g);
		g.getGL().glRenderbufferStorage(GL2.GL_RENDERBUFFER, format.glid, width, height);
		unbindFrom(g);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.clearError();
		g.getGL().glGenRenderbuffers(1, glStateNum, 0);
		g.getError();
		return glStateNum[0];
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.clearError();
		g.getGL().glDeleteRenderbuffers(1,glStateNum,0);
		g.getError();
		return true;
	}

	/**
	 * Gets the width of this render buffer.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets the height of this render buffer.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Gets the internal format of this render buffer.
	 */
	public Format getFormat()
	{
		return format;
	}

	@Override
	public void bindTo(OGLGraphics g)
	{
		g.getGL().glBindRenderbuffer(GL2.GL_RENDERBUFFER, getGLId());
	}

	@Override
	public void unbindFrom(OGLGraphics g)
	{
		g.getGL().glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
	}
	
	/**
	 * Destroys undeleted buffers abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteRenderbuffers(UNDELETED_LENGTH, UNDELETED_IDS, 0);
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
