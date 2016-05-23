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
package com.blackrook.ogl;

import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.DataType;
import com.blackrook.ogl.enums.GeometryType;

/**
 * A utility class for setting up buffers with geometric data.
 * @author Matthew Tropiano
 */
public final class OGLGeometryUtils
{
	/**
	 * Creates a set directive.
	 * @param dimensions the width in elements of this vertex set (dimensions).
	 * @param stride the separation in elements between each geometric attribute set.
	 * @param offset the starting offset in each attribute set in elements.
	 * @return the geometry info representing this directive.
	 */
	public static GeometryInfo info(int dimensions, int stride, int offset)
	{
		return new GeometryInfo(dimensions, stride, offset);
	}
	
	/**
	 * Draws geometry using this single buffer, assuming that this
	 * buffer contains geometric data. The data inside this buffer is
	 * expected to be interleaved geometry data.
	 * Vertex attributes are set in the order of provided info.
	 * @param g the graphics context.
	 * @param buffer the buffer to use for drawing.
	 * @param dataType the data type that it contains.
	 * @param geometryType the geometry type.
	 * @param count the vertex count (in the geometric figure).
	 * @param directives the geometry directives.
	 */
	public static void drawGeometry(OGLGraphics g, OGLBuffer buffer, DataType dataType, GeometryType geometryType, int count, GeometryInfo ... directives) 
	{
		g.setBuffer(BufferType.GEOMETRY, buffer);

		int index = 0;
		for (GeometryInfo info : directives)
			info.setState(g, index++, dataType);
		
		g.drawBufferGeometry(geometryType, 0, count);
	
		for (GeometryInfo info : directives)
			info.unsetState(g, --index);

		g.unsetBuffer(BufferType.GEOMETRY);
	}

	/**
	 * A directive that tells OGLGeometryUtils how to draw a buffer full of data.
	 */
	public static class GeometryInfo
	{
		/** Data size (width). */
		protected int width;
		/** Data stride. */
		protected int stride;
		/** Data offset in a buffer. */
		protected int offset;
		
		private GeometryInfo(int width, int stride, int offset)
		{
			this.width = width;
			this.stride = stride;
			this.offset = offset;
		}
		
		/**
		 * Sets the array state using info.
		 * @param g the graphics context to set the state on.
		 * @param index the vertex attribute index.
		 * @param type the buffer type.
		 */
		protected void setState(OGLGraphics g, int index, DataType type)
		{
			g.setVertexAttributeArrayEnabled(index, true);
			g.setBufferPointerVertexAttributes(index, type, false, width, stride, offset);
		}

		/**
		 * Unsets the array state using info.
		 * @param g the graphics context to unset the state on.
		 * @param index the vertex attribute index.
		 */
		protected void unsetState(OGLGraphics g, int index)
		{
			g.setVertexAttributeArrayEnabled(index, false);
		}
		
	}
	
}
