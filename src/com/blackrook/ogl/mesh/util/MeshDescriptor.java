/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.mesh.util;

import com.blackrook.ogl.enums.GeometryType;

/**
 * A descriptor class for generating meshes.
 * @author Matthew Tropiano
 */
public interface MeshDescriptor
{
	/**
	 * Returns the {@link GeometryType} for the mesh.
	 */
	public GeometryType getGeometryType();
	
	/**
	 * Returns the highest sequence number that this descriptor allows.
	 */
	public int getSequenceCount();

	/**
	 * Gets the next position, X-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getPositionX(int sequence);

	/**
	 * Gets the next position, Y-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getPositionY(int sequence);
	
	/**
	 * Gets the next position, Z-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getPositionZ(int sequence);

	/**
	 * Gets the next texture coordinate, S-coordinate.
	 * @param sequence the sequence number for generating this value.
	 * @param layer the multitexture layer.
	 */
	public float getTextureS(int sequence, int layer);

	/**
	 * Gets the next texture coordinate, T-coordinate.
	 * @param sequence the sequence number for generating this value.
	 * @param layer the multitexture layer.
	 */
	public float getTextureT(int sequence, int layer);
	
	/**
	 * Gets the next normal component, X-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getNormalX(int sequence);

	/**
	 * Gets the next normal component, Y-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getNormalY(int sequence);
	
	/**
	 * Gets the next normal component, Z-coordinate.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getNormalZ(int sequence);

	/**
	 * Gets the next color component, red.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getColorRed(int sequence);

	/**
	 * Gets the next color component, green.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getColorGreen(int sequence);
	
	/**
	 * Gets the next color component, blue.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getColorBlue(int sequence);
	
	/**
	 * Gets the next color component, alpha.
	 * @param sequence the sequence number for generating this value.
	 */
	public float getColorAlpha(int sequence);
	
}
