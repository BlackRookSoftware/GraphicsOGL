/*******************************************************************************
 * Copyright (c) 2014, 2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * Contributors:
 *     Matt Tropiano - initial API and implementation
 *******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL3;

/**
 * Enum for face sides.
 * @author Matthew Tropiano
 */
public enum FaceSide
{
	/** Front polygon face. */
	FRONT(GL3.GL_FRONT),
	/** Back polygon face. */
	BACK(GL3.GL_BACK),
	/** Front and back polygon faces. */
	FRONT_AND_BACK(GL3.GL_FRONT_AND_BACK);
	
	public final int glValue;
	FaceSide(int gltype) 
		{glValue = gltype;}
	
	/**
	 * Direction of front faces.
	 * @author Matthew Tropiano
	 */
	public static enum Direction
	{
		COUNTERCLOCKWISE(GL3.GL_CCW),
		CLOCKWISE(GL3.GL_CW);

		public final int glValue;
		Direction(int gltype) 
			{glValue = gltype;}
	}
	
}
