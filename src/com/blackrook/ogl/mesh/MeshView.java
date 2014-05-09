package com.blackrook.ogl.mesh;

import java.nio.FloatBuffer;

import com.blackrook.ogl.OGLDrawable;
import com.blackrook.ogl.enums.GeometryType;

/**
 * An interface for gathering data on a model mesh for a frame.
 * @author Matthew Tropiano
 */
public abstract class MeshView implements OGLDrawable
{

	/**
	 * Get the geometry type for the primitive data in this mesh.
	 */
	public abstract GeometryType getGeometryType();

	/**
	 * Returns the number of elements (usually vertices) that make
	 * up the full geometry of this mesh.
	 */
	public abstract int getElementCount();

	/**
	 * Gets the element component information for a vertex.
	 * @param element the element number to reference.
	 * @return the index to retrieve.
	 */
	public abstract int getIndex(int element);

	/**
	 * Gets the element component information for a vertex.
	 * @param index the element index.
	 * @param dimension the desired dimension.
	 * @return the value of the chosen vertex component.
	 */
	public abstract float getVertex(int index, int dimension);

	/**
	 * Gets the element component information for a texture coordinate.
	 * @param index the element index.
	 * @param unit the texture unit.
	 * @param dimension the desired dimension.
	 * @return the value of the chosen texture coordinate component.
	 */
	public abstract float getTextureCoordinate(int index, int unit, int dimension);

	/**
	 * Gets the element component information for a normal.
	 * @param index the element index.
	 * @param component the desired component (0, 1, 2) for (x, y, z).
	 * @return the value of the chosen color normal.
	 */
	public abstract float getNormal(int index, int component);

	/**
	 * Gets the element component information for a color.
	 * @param index the element index.
	 * @param component the desired component (0, 1, 2, 3) for (red, green, blue, alpha).
	 * @return the value of the chosen color component.
	 */
	public abstract float getColor(int index, int component);

	/**
	 * Gets vertex mesh for this drawable in geometry coordinates.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to.
	 * @param dimensions the dimensions of the data to generate coordinates for.
	 * @return the final offset into the buffer after the copy. The offset should be the starting
	 * position for the next drawable call.
	 */
	public int getVertices(FloatBuffer target, int targetOffset, int dimensions)
	{
		getVertices(target, targetOffset, dimensions, 0, dimensions);
		return targetOffset + (getElementCount() * dimensions); 
	}

	/**
	 * Gets vertex mesh for this drawable in geometry coordinates for the purposes of drawing them interleaved.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to (not taking offset into account).
	 * @param dimensions the dimensions of the data to generate coordinates for.
	 * @param offset the offset into the width in which the coordinates are written.
	 * @param width the total width of a single set of elements in indices.
	 */
	public void getVertices(FloatBuffer target, int targetOffset, int dimensions, int offset, int width)
	{
		targetOffset += offset;
		for (int i = 0; i < getElementCount(); i++)
		{
			int index = getIndex(i);
			for (int n = 0; n < dimensions; n++)
				target.put(targetOffset + (i * width) + n, getVertex(index, n));
		}
	}

	/**
	 * Gets texture coordinates for this drawable in geometry coordinates.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param unit the multitexture unit to get the coordinate data for.
	 * @param targetOffset the offset into the buffer from which the data will be copied to.
	 * @param dimensions the dimensions of the data to generate coordinates for.
	 * @return the final offset into the buffer after the copy. The offset should be the starting
	 * position for the next drawable call.
	 */
	public int getTextureCoordinates(FloatBuffer target, int unit, int targetOffset, int dimensions)
	{
		getTextureCoordinates(target, unit, targetOffset, dimensions, 0, dimensions);
		return targetOffset + (getElementCount() * dimensions); 
	}

	/**
	 * Gets texture coordinates for this drawable in geometry coordinates for the purposes of drawing them interleaved.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param unit the multitexture unit to get the coordinate data for.
	 * @param targetOffset the offset into the buffer from which the data will be copied to (not taking offset into account).
	 * @param dimensions the dimensions of the data to generate coordinates for.
	 * @param offset the offset into the width in which the coordinates are written.
	 * @param width the total width of a single set of elements in indices.
	 */
	public void getTextureCoordinates(FloatBuffer target, int unit, int targetOffset, int dimensions, int offset, int width)
	{
		targetOffset += offset;
		for (int i = 0; i < getElementCount(); i++)
		{
			int index = getIndex(i);
			for (int n = 0; n < dimensions; n++)
				target.put(targetOffset + (i * width) + n, getTextureCoordinate(index, unit, n));
		}
	}

	/**
	 * Gets normal vectors for this drawable in geometry coordinates.
	 * Normals are always three-dimensional.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to.
	 * @return the final offset into the buffer after the copy. The offset should be the starting
	 * position for the next drawable call.
	 */
	public int getNormals(FloatBuffer target, int targetOffset)
	{
		getNormals(target, targetOffset, 0, 3);
		return targetOffset + (getElementCount() * 3); 
	}

	/**
	 * Gets normal vectors for this drawable in geometry coordinates for the purposes of drawing them interleaved.
	 * Normals are always three-dimensional.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to (not taking offset into account).
	 * @param offset the offset into the width in which the components are written.
	 * @param width the total width of a single set of elements in indices.
	 */
	public void getNormals(FloatBuffer target, int targetOffset, int offset, int width)
	{
		targetOffset += offset;
		for (int i = 0; i < getElementCount(); i++)
		{
			int index = getIndex(i);
			for (int n = 0; n < 3; n++)
				target.put(targetOffset + (i * width) + n, getNormal(index, n));
		}
	}

	/**
	 * Gets colors for this drawable in separate components.
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to.
	 * @param components the number of color components (usually 4).
	 * @return the final offset into the buffer after the copy. The offset should be the starting
	 * position for the next drawable call.
	 */
	public int getColors(FloatBuffer target, int targetOffset, int components)
	{
		getColors(target, targetOffset, components, 0, components);
		return targetOffset + (getElementCount() * components); 
	}

	/**
	 * Gets colors for this drawable in separate components for the purposes of drawing them interleaved.
	 * Colors are always four-dimensional (r, g, b, a).
	 * The copied data should be drawable as the geometry type from getGeometryType().
	 * @param target the target buffer for the data.
	 * @param targetOffset the offset into the buffer from which the data will be copied to (not taking offset into account).
	 * @param components the number of color components in the color.
	 * @param offset the offset into the width in which the components are written.
	 * @param width the total width of a single set of elements in indices.
	 */
	public void getColors(FloatBuffer target, int targetOffset, int components, int offset, int width)
	{
		targetOffset += offset;
		for (int i = 0; i < getElementCount(); i++)
		{
			int index = getIndex(i);
			for (int n = 0; n < components; n++)
				target.put(targetOffset + (i * width) + n, getColor(index, n));
		}
	}


}
