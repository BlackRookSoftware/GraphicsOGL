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
 * Attribute types for attribute states
 * @author Matthew Tropiano
 */
public enum ClientAttribType
{
	ALL((int)(GL2.GL_ALL_CLIENT_ATTRIB_BITS & 0xffffffff)),
	PIXEL_STORE(GL2.GL_CLIENT_PIXEL_STORE_BIT),
	VERTEX_ARRAY(GL2.GL_CLIENT_VERTEX_ARRAY_BIT);

	public final int glValue;
	private ClientAttribType (int val) {glValue = val;}

}
