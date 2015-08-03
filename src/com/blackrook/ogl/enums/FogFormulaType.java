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
 * Enumeration of fog calculation methods.
 * @author Matthew Tropiano
 */
public enum FogFormulaType
{
	/**
	 * Fog density calculation is linearly calculated
	 * from the start to the end. 
	 */
	LINEAR(GL2.GL_LINEAR),
	/**
	 * Fog density calculation is an exponential
	 * increase from the start to finish. 
	 * Uses density coefficient. 
	 */
	EXPONENT(GL2.GL_EXP),
	/**
	 * Fog density calculation is an exponential
	 * increase from the start to finish. 
	 * Uses density coefficient. 
	 */
	EXPONENT_SQUARED(GL2.GL_EXP2);
	
	public final int glValue;
	private FogFormulaType (int val) {glValue = val;}
}
