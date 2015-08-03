/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

/**
 * Blending functions.
 * @author Matthew Tropiano
 */
public enum BlendFunc
{
	ADDITIVE(BlendArg.SOURCE_ALPHA, BlendArg.ONE),
	MULTIPLICATIVE(BlendArg.DEST_COLOR, BlendArg.ZERO),
	REPLACE(BlendArg.SOURCE_ALPHA, BlendArg.ZERO),
	ALPHA(BlendArg.SOURCE_ALPHA, BlendArg.ONE_MINUS_SOURCE_ALPHA);
	
	public final BlendArg argsrc;
	public final BlendArg argdst;
	BlendFunc(BlendArg src, BlendArg dst) 
		{argsrc = src; argdst = dst;}

}
