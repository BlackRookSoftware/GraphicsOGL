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

import com.jogamp.opengl.GL2;

/**
 * Hint enum types for GL Hints.
 * @author Matthew Tropiano
 */
public enum HintType
{
	/** Point smoothing hints. */
	POINT_SMOOTHING(GL2.GL_POINT_SMOOTH_HINT),
	/** Line smoothing hints. */
	LINE_SMOOTHING(GL2.GL_LINE_SMOOTH_HINT),
	/** Polygon smoothing hints. */
	POLYGON_SMOOTHING(GL2.GL_POLYGON_SMOOTH_HINT),
	/** Fog rendering hints. */
	FOG(GL2.GL_FOG_HINT),
	/** Mipmap generation hints. */
	MIPMAPPING(GL2.GL_GENERATE_MIPMAP_HINT),
	/** Texture compression hint. */
	TEXTURE_COMPRESSION(GL2.GL_TEXTURE_COMPRESSION_HINT),
	/** Perspective compression hint. */
	PERSPECTIVE_CORRECTION(GL2.GL_PERSPECTIVE_CORRECTION_HINT);
	
	public final int glValue;
	HintType(int gltype) 
		{glValue = gltype;}

}
