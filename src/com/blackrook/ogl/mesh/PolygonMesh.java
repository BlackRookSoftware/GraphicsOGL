/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.mesh;

import java.nio.FloatBuffer;

import javax.media.opengl.*;

import com.blackrook.commons.list.List;
import com.blackrook.commons.math.geometry.Point2F;
import com.blackrook.commons.math.geometry.Point3F;
import com.blackrook.ogl.OGLGeometryUtils;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.OGLMesh;
import com.blackrook.ogl.OGLGeometryUtils.GeometryInfo;
import com.blackrook.ogl.data.OGLColor;
import com.blackrook.ogl.enums.AccessType;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.CachingHint;
import com.blackrook.ogl.enums.GeometryType;
import com.blackrook.ogl.object.buffer.OGLFloatBuffer;

/**
 * A drawable polygon type that holds a sequence of points, texture coordinates, 
 * colors and normals to be rendered in a special way. Omitting one of these 
 * components will not use it in the draw method (e.g. if the colors are omitted,
 * it would be as though the color information was not passed to begin with).
 * The draw() method of this object will draw this polygon in immediate mode or using a
 * VBO, if the extension is present.
 * @author Matthew Tropiano
 */
public class PolygonMesh implements OGLMesh
{
	protected static final Point2F BLANK_POINT2F = new Point2F(0f,0f);
	protected static final Point3F BLANK_POINT3F = new Point3F(0f,0f,0f);
	protected static final int[] BLANK_DIMENSIONS = new int[0];
	
	/** The polygon's geometry type. */
	private GeometryType geometryType;
	/** Vertex Count. */
	private int vertexCount;
	/** Texture layers. */
	private int textureLayers;
	
	/** The list of geometric points. */
	private Point3F[] vertices;
	/** The list of texture coordinates. */
	private Point2F[][] textureCoordinates;
	/** The list of colors. */
	private OGLColor[] colors;
	/** The list of normals. */
	private Point3F[] normals;
	
	/** A trigger to rebuild the VBO that holds this polygon's data. */
	private boolean rebuildTrigger;
	/** Polygon view. */
	private PolygonView view;
	
	/** Number of floats. */
	private int geometryBufferCapacity;
	/** Element width. */
	private int geometryElementWidth;
	
	/**
	 * Creates a new polygon.
	 * @param gtype the geometry type, used for drawing.
	 * @param vertexCount the amount of vertices in this polygon.
	 * @param textureLayers	the amount of texture layers on this polygon.
	 */
	public PolygonMesh(GeometryType gtype, int vertexCount, int textureLayers)
	{
		this.geometryType = gtype;
		this.vertexCount = vertexCount;
		this.textureLayers = textureLayers;
		this.geometryBufferCapacity = 0;
		this.geometryElementWidth = 0;
	}
	
	/**
	 * Checks if the vertex array was allocated and if not,
	 * creates it.
	 */
	protected void checkVertexAllocation()
	{
		if (vertices != null) return;

		geometryElementWidth += 3;
		geometryBufferCapacity += vertexCount * 3;
		vertices = new Point3F[vertexCount];
		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new Point3F();
	}
	
	/**
	 * Checks if the texture coordinate array was allocated and if not,
	 * creates it.
	 */
	protected void checkTextureAllocation()
	{
		if (textureCoordinates != null) return;

		geometryElementWidth += (textureLayers * 2);
		geometryBufferCapacity += (textureLayers * vertexCount) * 2;
		textureCoordinates = new Point2F[textureLayers][vertexCount];
		for (int i = 0; i < textureLayers; i++)
			for (int j = 0; j < vertexCount; j++)
				textureCoordinates[i][j] = new Point2F();
	}
	
	/**
	 * Checks if the color array was allocated and if not,
	 * creates it. If this happens, the default color is NOT USED on draw.
	 */
	protected void checkColorAllocation()
	{
		if (colors != null) return;
		
		geometryElementWidth += 4;
		geometryBufferCapacity += vertexCount * 4;
		colors = new OGLColor[vertexCount];
		for (int i = 0; i < vertexCount; i++)
			colors[i] = new OGLColor(OGLColor.WHITE);
	}
	
	/**
	 * Checks if the normal array was allocated and if not,
	 * creates it.
	 */
	protected void checkNormalAllocation()
	{
		if (normals != null) return;
		
		geometryElementWidth += 3;
		geometryBufferCapacity += vertexCount * 3;
		normals = new Point3F[vertexCount];
		for (int i = 0; i < vertexCount; i++)
			normals[i] = new Point3F();
	}
	
	/**
	 * Triggers a rebuild of the internal geometry buffer.
	 */
	protected void triggerRebuild()
	{
		rebuildTrigger = true;
	}
	
	/**
	 * Sets the geometry type for this polygon. 
	 */
	public void setGeometryType(GeometryType gtype)
	{
		geometryType = gtype;
	}

	/**
	 * Sets the component values of a specific vertex.
	 */
	public void setVertex(int index, float x, float y, float z)
	{
		checkVertexAllocation();
		vertices[index].set(x, y, z);
		triggerRebuild();
	}
	
	/**
	 * Sets the component values of a specific texture coordinate.
	 * Equivalent to <code>setTextureCoordinate(0, index, s, t)</code>.
	 */
	public void setTextureCoordinate(int index, float s, float t)
	{
		setTextureCoordinate(0, index, s, t);
	}

	/**
	 * Sets the component values of a specific texture coordinate.
	 */
	public void setTextureCoordinate(int layer, int index, float s, float t)
	{
		checkTextureAllocation();
		textureCoordinates[layer][index].set(s, t);
		triggerRebuild();
	}

	/**
	 * Sets the component values of a specific vertex's color.
	 */
	public void setColor(int index, float r, float g, float b, float a)
	{
		checkColorAllocation();
		colors[index].set(r, g, b, a);
		triggerRebuild();
	}
	
	/**
	 * Sets the component values of a specific vertex's color.
	 */
	public void setColor(int index, OGLColor color)
	{
		checkColorAllocation();
		colors[index].set(color);
		triggerRebuild();
	}
	
	/**
	 * Sets the component values of a specific normal.
	 */
	public void setNormal(int index, float x, float y, float z)
	{
		checkNormalAllocation();
		normals[index].set(x, y, z);
		triggerRebuild();
	}
	
	@Override
	public MeshView getView()
	{
		if (view == null)
			view = new PolygonView();
		return view;
	}

	public class PolygonView extends MeshView
	{
		/** The buffer that holds geometry. */
		private OGLFloatBuffer geometryBuffer;
		/** The geometry drawing directives. */
		private GeometryInfo[] geometryInfo;

		public PolygonView()
		{
			geometryBuffer = null;
			geometryInfo = null;
		}
		
		@Override
		public int getIndex(int element)
		{
			return element;
		}

		@Override
		public int getElementCount()
		{
			return vertexCount;
		}

		@Override
		public GeometryType getGeometryType()
		{
			return geometryType;
		}

		@Override
		public float getVertex(int index, int dimension)
		{
			Point3F v = (vertices == null)
				? BLANK_POINT3F
				: vertices[index];

			switch (dimension)
			{
				case 0:
					return v.x;
				case 1:
					return v.y;
				case 2:
					return v.z;
			}
			return 0f;
		}

		@Override
		public float getTextureCoordinate(int index, int layer, int dimension)
		{
			Point2F texcoord = (textureCoordinates == null)
				? BLANK_POINT2F
				: textureCoordinates[layer][index];
			
			switch (dimension)
			{
				case 0:
					return texcoord.x;
				case 1:
					return texcoord.y;
			}
			return 0f;
		}

		@Override
		public float getColor(int index, int component)
		{
			OGLColor color = (colors == null)
				? OGLColor.WHITE
				: colors[index];

			switch (component)
			{
				case 0:
					return color.getRed();
				case 1:
					return color.getGreen();
				case 2:
					return color.getBlue();
				case 3:
					return color.getAlpha();
			}
			return 0f;
		}

		@Override
		public float getNormal(int index, int component)
		{
			Point3F normal = (normals == null)
				? BLANK_POINT3F
				: normals[index];
			
			switch (component)
			{
				case 0:
					return normal.x;
				case 1:
					return normal.y;
				case 2:
					return normal.z;
			}
			return 0f;
		}

		@Override
		public void drawUsing(OGLGraphics g)
		{
			if (g.supportsVertexBuffers())
				drawUsingVBO(g);
			else
				drawUsingImmediateMode(g);
		}

		/**
		 * Draws this object in immediate mode.
		 * @param g the graphics context to use.
		 */
		public void drawUsingImmediateMode(OGLGraphics g)
		{
			Point3F normal = null;
			Point3F vertex = null;
			OGLColor color = null;
			GL2 gl = g.getGL();
			gl.glBegin(geometryType.glValue);
			for (int n = 0; n < vertices.length; n++)
			{
				if (normals != null)
					normal = normals[n];
				if (vertices != null)
					vertex = vertices[n];
				if (colors != null)
					color = colors[n];
				if (textureCoordinates != null) for (int m = 0; m < textureLayers; m++)
				{
					if (m > 0)
						gl.glMultiTexCoord2d(m-1, textureCoordinates[m][n].x, textureCoordinates[m][n].y);
					else
						gl.glTexCoord2d(textureCoordinates[m][n].x, textureCoordinates[m][n].y);
				}
				if (colors != null)
					g.setColor(color);
				if (normals != null)
					gl.glNormal3d(normal.x, normal.y, normal.z);
				if (vertices != null)
					gl.glVertex3d(vertex.x, vertex.y, vertex.z);
			}
			gl.glEnd();
		}

		/**
		 * Draws this object using VBOs.
		 * @param g the graphics context to use.
		 */
		public void drawUsingVBO(OGLGraphics g)
		{
			if (rebuildTrigger)
			{
				rebuildBuffer(g);
				rebuildTrigger = false;
		
				List<GeometryInfo> list = new List<GeometryInfo>(8);
				
				int vwidth = (vertices != null) ? 3 : 0;
				int twidth = (textureLayers > 0) ? textureLayers * 2 : 0;
				int nwidth = (normals != null) ? 3 : 0;
		
				if (vertices != null)
					list.add(OGLGeometryUtils.vertices(3, geometryElementWidth, 0));
				if (textureLayers > 0) for (int i = 0; i < textureLayers; i++)
					list.add(OGLGeometryUtils.texCoords(i, 2, geometryElementWidth, vwidth + (2 * i)));
				if (normals != null)
					list.add(OGLGeometryUtils.normals(geometryElementWidth, vwidth + twidth));
				if (colors != null)
					list.add(OGLGeometryUtils.color(4, geometryElementWidth, vwidth + twidth + nwidth));
				
				geometryInfo = new GeometryInfo[list.size()];
				list.toArray(geometryInfo);
			}
		
			if (geometryBuffer != null)
				OGLGeometryUtils.drawInterleavedGeometry(g, geometryBuffer, getGeometryType(), getElementCount(), geometryInfo);
		}

		/**
		 * Rebuilds the internal geometry buffer for this.
		 * @param g the graphics context to use.
		 */
		protected void rebuildBuffer(OGLGraphics g)
		{
			if (geometryBuffer == null)
				geometryBuffer = new OGLFloatBuffer(g, BufferType.GEOMETRY);
			geometryBuffer.setCapacity(g, CachingHint.STATIC_DRAW, geometryBufferCapacity);
		
			int offs = 0;
			FloatBuffer fb = geometryBuffer.mapBuffer(g, AccessType.WRITE);
			if (vertices != null)
			{
				getVertices(fb, 0, 3, offs, geometryElementWidth);
				offs += 3;
			}
			if (textureCoordinates != null)
			{
				for (int i = 0; i < textureLayers; i++)
					getTextureCoordinates(fb, i, 0, 2, offs, geometryElementWidth);
				offs += (textureLayers*2);
			}
			if (normals != null)
			{
				getNormals(fb, 0, offs, geometryElementWidth);
				offs += 3;
			}
			if (colors != null)
			{
				getColors(fb, 0, 4, offs, geometryElementWidth);
				offs += 4;
			}
			geometryBuffer.unmapBuffer(g);
		}
		
	}
	
}
