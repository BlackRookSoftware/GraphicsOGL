package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Enumeration of render buffer formats.
 * @author Matthew Tropiano
 */
public enum RenderbufferFormat
{
	RGB(GL2.GL_RGB),
	RGBA(GL2.GL_RGBA),
	DEPTH(GL2.GL_DEPTH_COMPONENT),
	STENCIL(GL2.GL_STENCIL_INDEX);
	
	public final int glid;
	private RenderbufferFormat(int id) {glid = id;}
}
