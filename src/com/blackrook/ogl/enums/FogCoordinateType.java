package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Enumeration of fog coordinate calculation types.
 * @author Matthew Tropiano
 */
public enum FogCoordinateType
{
	/**
	 * Fog coordinate is taken from fog coordinate attributes. 
	 */
	COORDINATE(GL2.GL_FOG_COORD),
	/**
	 * Fog coordinate is taken from fragment depth (only effective
	 * if the depth buffer is active). 
	 */
	DEPTH(GL2.GL_FRAGMENT_DEPTH);
	
	public final int glValue;
	private FogCoordinateType (int val) {glValue = val;}
}
