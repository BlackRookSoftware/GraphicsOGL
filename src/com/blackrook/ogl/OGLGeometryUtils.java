package com.blackrook.ogl;

import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.DataType;
import com.blackrook.ogl.enums.GeometryType;

/**
 * A utility class for drawing buffers with geometric data.
 * @author Matthew Tropiano
 */
public final class OGLGeometryUtils
{
	/**
	 * Creates a vertex set directive.
	 * @param dimensions the width in elements of this vertex set (dimensions).
	 * @param stride the separation in elements between each geometric attribute set.
	 * @param offset the starting offset in each attribute set in elements.
	 * @return the geometry info representing this directive.
	 */
	public static GeometryInfo vertices(int dimensions, int stride, int offset)
	{
		return new VertexInfo(dimensions, stride, offset);
	}
	
	/**
	 * Creates a texture coordinate directive.
	 * @param texUnit the texture unit that this applies to.
	 * @param dimensions the width in elements of this texture coordinate set (usually 2).
	 * @param stride the separation in elements between each geometric attribute set.
	 * @param offset the starting offset in each attribute set in elements.
	 * @return the geometry info representing this directive.
	 */
	public static GeometryInfo texCoords(int texUnit, int dimensions, int stride, int offset)
	{
		return new TexCoordInfo(texUnit, dimensions, stride, offset);
	}
	
	/**
	 * Creates a surface normal directive. Is 3-dimensional coordinates.
	 * @param stride the separation in elements between each geometric attribute set.
	 * @param offset the starting offset in each attribute set in elements.
	 * @return the geometry info representing this directive.
	 */
	public static GeometryInfo normals(int stride, int offset)
	{
		return new NormalInfo(stride, offset);
	}
	
	/**
	 * Creates a vertex color directive.
	 * @param components the width in elements of this color component set (components).
	 * @param stride the separation in elements between each geometric attribute set.
	 * @param offset the starting offset in each attribute set in elements.
	 * @return the geometry info representing this directive.
	 */
	public static GeometryInfo color(int components, int stride, int offset)
	{
		return new ColorInfo(components, stride, offset);
	}
	
	/**
	 * Draws geometry using this single buffer, assuming that this
	 * buffer contains geometric data. The data inside this buffer is
	 * expected to be interleaved geometry data.
	 * @param g the graphics context.
	 * @param buffer the buffer to use for drawing.
	 * @param geom the geometry type.
	 * @param count the element count (in the geometric figure).
	 * @param directives 
	 */
	public static void drawInterleavedGeometry(OGLGraphics g, OGLBuffer<?> buffer, GeometryType geom, int count, GeometryInfo ... directives) 
	{
		g.setBuffer(BufferType.GEOMETRY, buffer);

		for (GeometryInfo info : directives)
			info.setState(g, buffer.getDataType());
		
		g.getGL().glDrawArrays(geom.glValue, 0, count);
	
		for (GeometryInfo info : directives)
			info.unsetState(g);

		g.unsetBuffer(BufferType.GEOMETRY);
	}

	/** Component type. */
	public enum Component
	{
		VERTICES,
		TEXCOORDS,
		NORMALS,
		COLORS;
	}

	/**
	 * A directive that tells OGLGeometryUtils how to draw a buffer full of data.
	 */
	public abstract static class GeometryInfo
	{
		/** Component type. */
		protected Component component;
		/** Data size (width). */
		protected int width;
		/** Data stride. */
		protected int stride;
		/** Data offset in a buffer. */
		protected long offset;
		
		private GeometryInfo(Component component, int width, int stride, int offset)
		{
			this.component = component;
			this.width = width;
			this.stride = stride;
			this.offset = offset;
		}
		
		/**
		 * Sets the client state using info.
		 * @param g the graphics context to set the state on.
		 * @param type the buffer type.
		 */
		protected abstract void setState(OGLGraphics g, DataType type);
		
		/**
		 * Unsets the client state using info.
		 * @param g the graphics context to unset the state on.
		 */
		protected abstract void unsetState(OGLGraphics g);
		
	}
	
	// Vertex info.
	private static class VertexInfo extends GeometryInfo
	{
		private VertexInfo(int width, int stride, int offset)
		{
			super(Component.VERTICES, width, stride, offset);
		}

		@Override
		protected void setState(OGLGraphics g, DataType type)
		{
			g.setVertexArrayEnabled(true);
			g.getGL().glVertexPointer(width, type.glValue, stride * type.size, offset * type.size);
		}

		@Override
		protected void unsetState(OGLGraphics g)
		{
			g.setVertexArrayEnabled(false);
		}
		
	}
	
	// TexCoord info.
	private static class TexCoordInfo extends GeometryInfo
	{
		/** Data multitexture unit. */
		protected int multiTexUnit;

		private TexCoordInfo(int multiTexUnit, int width, int stride, int offset)
		{
			super(Component.TEXCOORDS, width, stride, offset);
			this.multiTexUnit = multiTexUnit;
		}

		@Override
		protected void setState(OGLGraphics g, DataType type)
		{
			g.setTextureUnit(multiTexUnit);
			g.setTextureCoordArrayEnabled(true);
			g.getGL().glTexCoordPointer(width, type.glValue, stride * type.size, offset * type.size);
		}

		@Override
		protected void unsetState(OGLGraphics g)
		{
			g.setTextureUnit(multiTexUnit);
			g.setTextureCoordArrayEnabled(false);
		}
		
	}

	// Normal info.
	private static class NormalInfo extends GeometryInfo
	{
		private NormalInfo(int stride, int offset)
		{
			super(Component.NORMALS, 3, stride, offset);
		}

		@Override
		protected void setState(OGLGraphics g, DataType type)
		{
			g.setNormalArrayEnabled(true);
			g.getGL().glNormalPointer(type.glValue, stride * type.size, offset * type.size);
		}

		@Override
		protected void unsetState(OGLGraphics g)
		{
			g.setNormalArrayEnabled(false);
		}
		
	}
	
	// Color info.
	private static class ColorInfo extends GeometryInfo
	{
		private ColorInfo(int width, int stride, int offset)
		{
			super(Component.COLORS, width, stride, offset);
		}

		@Override
		protected void setState(OGLGraphics g, DataType type)
		{
			g.setColorArrayEnabled(true);
			g.getGL().glColorPointer(width, type.glValue, stride * type.size, offset * type.size);
		}

		@Override
		protected void unsetState(OGLGraphics g)
		{
			g.setColorArrayEnabled(false);
		}
		
	}
	
}
