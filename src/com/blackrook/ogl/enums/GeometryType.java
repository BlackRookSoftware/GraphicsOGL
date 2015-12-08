/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.*;

/**
 * Geometry type 
 * @author Matthew Tropiano
 */
public enum GeometryType
{
	POINTS(GL2.GL_POINTS, true)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount;
		}
	},
	LINES(GL2.GL_LINES, true)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount/2;
		}
	},
	LINE_STRIP(GL2.GL_LINE_STRIP, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return 1;
		}
	},
	LINE_LOOP(GL2.GL_LINE_LOOP, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return 1;
		}
	},
	TRIANGLES(GL2.GL_TRIANGLES, true)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount / 3;
		}
	},
	TRIANGLE_STRIP(GL2.GL_TRIANGLE_STRIP, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount - 2;
		}
	},
	TRIANGLE_FAN(GL2.GL_TRIANGLE_FAN, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount - 2;
		}
	},
	QUADS(GL2.GL_QUADS, true)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return elementCount / 4;
		}
	},
	QUAD_STRIP(GL2.GL_QUAD_STRIP, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return (elementCount - 2) / 2;
		}
	},
	POLYGON(GL2.GL_POLYGON, false)
	{
		@Override
		public int calculatePolygonCount(int elementCount)
		{
			return 1;
		}
	};
	
	public final int glValue;
	private final boolean batchable; 
	
	GeometryType(int gltype, boolean batchable) 
		{glValue = gltype; this.batchable = batchable;}

	/**
	 * Is this geometry type able to be put together in 
	 * geometry batches without ruining how it appears or is drawn.
	 */
	public boolean isBatchable()
	{
		return batchable;
	}
	
	/**
	 * Calculates the polygon count by how many elements/vertices it contained.
	 */
	public abstract int calculatePolygonCount(int elementCount);
	
}
