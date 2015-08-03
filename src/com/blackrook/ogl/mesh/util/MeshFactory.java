/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.mesh.util;

import com.blackrook.ogl.mesh.PolygonMesh;

/**
 * A factory class for creating OGLMesh objects.
 * @author Matthew Tropiano
 */
public final class MeshFactory
{
	/**
	 * Creates a polygonal mesh by a mesh descriptor.
	 * @param descriptor the mesh descriptor to do.
	 * @return a new polygonal mesh from the descriptor.
	 */
	public static PolygonMesh createMesh(MeshDescriptor descriptor)
	{
		return createMesh(descriptor, 1);
	}
	
	/**
	 * Creates a polygonal mesh by a mesh descriptor.
	 * @param descriptor the mesh desciptor to do.
	 * @return a new polygonal mesh from the descriptor.
	 */
	public static PolygonMesh createMesh(MeshDescriptor descriptor, int textureLayers)
	{
		PolygonMesh out = new PolygonMesh(descriptor.getGeometryType(), descriptor.getSequenceCount(), textureLayers);
		for (int s = 0; s < descriptor.getSequenceCount(); s++)
		{
			out.setVertex(s, 
				descriptor.getPositionX(s), 
				descriptor.getPositionY(s),
				descriptor.getPositionZ(s)
				);
			for (int i = 0; i < textureLayers; i++)
				out.setTextureCoordinate(i, s,
					descriptor.getTextureS(s, i),
					descriptor.getTextureT(s, i)
					);
			out.setNormal(s,
				descriptor.getNormalX(s), 
				descriptor.getNormalY(s),
				descriptor.getNormalZ(s)
				);
			out.setColor(s,
				descriptor.getColorRed(s), 
				descriptor.getColorGreen(s), 
				descriptor.getColorBlue(s), 
				descriptor.getColorAlpha(s) 
				);
		}
		return out;
	}
		
}
