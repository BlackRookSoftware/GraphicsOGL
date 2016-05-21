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
 * Hint enum types for GL3 Hints.
 * @author Matthew Tropiano
 */
public enum HintValue
{
	/** Don't care about the quality. */
	DONT_CARE(GL3.GL_DONT_CARE),
	/** Use the best performing method. */
	FASTEST(GL3.GL_FASTEST),
	/** Use the best quality method. */
	NICEST(GL3.GL_NICEST);

	public final int glValue;
	HintValue(int gltype) 
		{glValue = gltype;}

}
