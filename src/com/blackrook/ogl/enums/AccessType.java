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
 * Access type enumeration.
 * @author Matthew Tropiano
 */
public enum AccessType
{
	/** 
	 * Buffer is filled with current data on map, 
	 * but not written back on unmap. 
	 */
	READ(GL3.GL_READ_ONLY),
	/** 
	 * Buffer is filled with undefined data on map, 
	 * writes back its contents on unmap. 
	 */
	WRITE(GL3.GL_WRITE_ONLY),
	/** 
	 * Buffer is filled with current data on map, 
	 * writes back its contents on unmap. 
	 */
	READ_AND_WRITE(GL3.GL_READ_WRITE);
	
	public final int glValue;
	private AccessType (int val) {glValue = val;}

}
