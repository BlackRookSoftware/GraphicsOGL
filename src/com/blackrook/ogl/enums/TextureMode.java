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

import com.jogamp.opengl.*;

/**
 * Texture environment mode constants.
 * @author Matthew Tropiano
 */
public enum TextureMode
{
	DECAL(GL2.GL_DECAL),
	/** Texels replace fragment information. */
	REPLACE(GL2.GL_REPLACE),
	/** Texels are multiplied with fragment color/material information. */
	MODULATE(GL2.GL_MODULATE),
	/** Texels are blended with fragment color/material information using the current blend function. */
	BLEND(GL2.GL_BLEND),
	/** Texels are added to fragment color/material information. */
	ADD(GL2.GL_ADD);
	
	public final int glValue;
	TextureMode(int gltype) 
		{glValue = gltype;}

}
