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
