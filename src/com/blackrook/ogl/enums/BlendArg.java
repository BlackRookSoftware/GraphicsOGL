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
 * Enumeration of blend source/destination arguments.
 * @author Matthew Tropiano
 */
public enum BlendArg
{

	ZERO(GL3.GL_ZERO),
	ONE(GL3.GL_ONE),
	SOURCE_COLOR(GL3.GL_SRC_COLOR),
	ONE_MINUS_SOURCE_COLOR(GL3.GL_ONE_MINUS_SRC_COLOR),
	DEST_COLOR(GL3.GL_DST_COLOR),
	ONE_MINUS_DEST_COLOR(GL3.GL_ONE_MINUS_DST_COLOR),
	SOURCE_ALPHA(GL3.GL_SRC_ALPHA),
	ONE_MINUS_SOURCE_ALPHA(GL3.GL_ONE_MINUS_SRC_ALPHA),
	DEST_ALPHA(GL3.GL_DST_ALPHA),
	ONE_MINUS_DEST_ALPHA(GL3.GL_ONE_MINUS_DST_ALPHA),
	SOURCE_ALPHA_SATURATE(GL3.GL_SRC_ALPHA_SATURATE);
	
	public final int glValue;
	BlendArg(int gltype) {glValue = gltype;}

}
