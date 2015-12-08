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
 * Enum for fill modes.
 * @author Matthew Tropiano
 */
public enum FillMode
{
	/** Points rendered only. */
	POINTS(GL2.GL_POINT),
	/** Lines/edges rendered only. */
	LINES(GL2.GL_LINE),
	/** Filled polygons. */
	FILLED(GL2.GL_FILL);

	public final int glValue;
	FillMode(int gltype) 
		{glValue = gltype;}
}
