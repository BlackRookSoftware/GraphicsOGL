/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Enumeration of fonts for GLUT.
 * @author Matthew Tropiano
 */
public enum GLUTFont 
{
	BITMAP_8_BY_13(GLUT.BITMAP_8_BY_13),
	BITMAP_9_BY_15(GLUT.BITMAP_9_BY_15),
	BITMAP_HELVETICA_10(GLUT.BITMAP_HELVETICA_10),
	BITMAP_HELVETICA_12(GLUT.BITMAP_HELVETICA_12),
	BITMAP_HELVETICA_18(GLUT.BITMAP_HELVETICA_18),
	BITMAP_TIMES_ROMAN_10(GLUT.BITMAP_TIMES_ROMAN_10),
	BITMAP_TIMES_ROMAN_24(GLUT.BITMAP_TIMES_ROMAN_24);
	
	public final int glutValue;
	private GLUTFont(int glutVal) {this.glutValue = glutVal;}
	
}
