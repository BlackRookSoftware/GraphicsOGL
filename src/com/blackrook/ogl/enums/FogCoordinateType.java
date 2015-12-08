/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL2;

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
