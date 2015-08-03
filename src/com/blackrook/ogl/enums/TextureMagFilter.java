/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Magnification filters.
 * @author Matthew Tropiano
 */
public enum TextureMagFilter
{
	/** Nearest magnification - color using nearest neighbor (aliased - "pixelates" textures). */
	NEAREST(GL2.GL_NEAREST),
	/** Linear magnification - color using linear interpolation ("smoothes" textures). */
	LINEAR(GL2.GL_LINEAR);
	
	public final int glid;
	private TextureMagFilter(int id) {glid = id;}
}

