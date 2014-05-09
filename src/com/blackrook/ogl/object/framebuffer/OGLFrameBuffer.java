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
import com.blackrook.ogl.object.OGLObject;
import com.blackrook.ogl.object.texture.OGLTexture2D;

/**
 * Framebuffer object for whatever the hell you wanna do with off-screen rendering.
 * It can bind itself to Texture2Ds and RenderBuffers and stuff.
 * @author Matthew Tropiano
 */
public class OGLFrameBuffer extends OGLObject implements OGLBindable
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

	private int[] glStateNum;

	/**
	 * Describes a list of possible frame buffer attachments.
	 * @author Matthew Tropiano
	 */
	public enum AttachPoint
	{
		COLOR0(GL2.GL_COLOR_ATTACHMENT0),
		COLOR1(GL2.GL_COLOR_ATTACHMENT1),
		COLOR2(GL2.GL_COLOR_ATTACHMENT2),
		COLOR3(GL2.GL_COLOR_ATTACHMENT3),
		COLOR4(GL2.GL_COLOR_ATTACHMENT4),
		COLOR5(GL2.GL_COLOR_ATTACHMENT5),
		COLOR6(GL2.GL_COLOR_ATTACHMENT6),
		COLOR7(GL2.GL_COLOR_ATTACHMENT7),
		COLOR8(GL2.GL_COLOR_ATTACHMENT8),
		COLOR9(GL2.GL_COLOR_ATTACHMENT9),
		COLOR10(GL2.GL_COLOR_ATTACHMENT10),
		COLOR11(GL2.GL_COLOR_ATTACHMENT11),
		COLOR12(GL2.GL_COLOR_ATTACHMENT12),
		COLOR13(GL2.GL_COLOR_ATTACHMENT13),
		COLOR14(GL2.GL_COLOR_ATTACHMENT14),
		COLOR15(GL2.GL_COLOR_ATTACHMENT15),
		DEPTH(GL2.GL_DEPTH_ATTACHMENT),
		STENCIL(GL2.GL_STENCIL_ATTACHMENT);
		
		public final int glVal;
		AttachPoint(int gltype) 
			{glVal = gltype;}
	}
	
	/**
	 * Constructs a new FrameBuffer object.
	 */
	public OGLFrameBuffer(OGLGraphics g)
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
		g.getGL().glDeleteFramebuffers(1,glStateNum,0);
		g.getError();
		return true;
	}

	/**
	 * Attaches a texture to this frame buffer for rendering directly to a texture.
	 * NOTE: This will leave the current frame buffer unbound after this, as it is
	 * used for a target.
	 * @param g the OGLGraphics context.
	 * @param attachPoint the attachment source point for this texture.
	 * @param texture the texture to attach this to.
	 */
	public void attachToTexture2D(OGLGraphics g, AttachPoint attachPoint, OGLTexture2D texture)
	{
		bindTo(g);
		g.getGL().glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, attachPoint.glVal, GL2.GL_TEXTURE_2D, texture.getGLId(), 0);
		unbindFrom(g);
	}
	
	/**
	 * Attaches a texture to this frame buffer for rendering directly to a texture.
	 * NOTE: This will leave the current frame buffer unbound after this, as it is
	 * used for a target.
	 * @param g the OGLGraphics context.
	 * @param attachPoint the attachment source point for this texture.
	 */
	public void detachFromTexture2D(OGLGraphics g, AttachPoint attachPoint)
	{
		bindTo(g);
		g.getGL().glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, attachPoint.glVal, GL2.GL_TEXTURE_2D, 0, 0);
		unbindFrom(g);
	}
	
	/**
	 * Attaches a texture to this frame buffer for rendering directly to a texture.
	 * NOTE: This will leave the current frame buffer unbound after this, as it is
	 * used for a target.
	 * @param g the OGLGraphics context.
	 * @param attachPoint the attachment source point for this texture.
	 * @param renderBuffer the texture to attach this to.
	 */
	public void attachToRenderBuffer(OGLGraphics g, AttachPoint attachPoint, OGLFrameRenderBuffer renderBuffer)
	{
		bindTo(g);
		g.getGL().glFramebufferRenderbuffer(GL2.GL_FRAMEBUFFER, attachPoint.glVal, GL2.GL_RENDERBUFFER, renderBuffer.getGLId());
		unbindFrom(g);
	}
	
	/**
	 * Attaches a texture to this frame buffer for rendering directly to a texture.
	 * NOTE: This will leave the current frame buffer unbound after this, as it is
	 * used for a target.
	 * @param g the OGLGraphics context.
	 * @param attachPoint the attachment source point for this texture.
	 */
	public void detachFromRenderBuffer(OGLGraphics g, AttachPoint attachPoint)
	{
		bindTo(g);
		g.getGL().glFramebufferRenderbuffer(GL2.GL_FRAMEBUFFER, attachPoint.glVal, GL2.GL_RENDERBUFFER, 0);
		unbindFrom(g);
	}
	
	@Override
	public void bindTo(OGLGraphics g)
	{
		g.getGL().glBindFramebuffer(GL2.GL_FRAMEBUFFER, getGLId());
	}

	@Override
	public void unbindFrom(OGLGraphics g)
	{
		g.getGL().glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
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
