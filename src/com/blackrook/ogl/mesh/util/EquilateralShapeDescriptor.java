/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.mesh.util;

import com.blackrook.commons.math.RMath;
import com.blackrook.commons.math.geometry.Point2F;
import com.blackrook.ogl.enums.GeometryType;

/**
 * A mesh descriptor for creating meshes with equal sides.
 * @author Matthew Tropiano
 */
public class EquilateralShapeDescriptor implements MeshDescriptor
{
	/**
	 * Texture mode for applying texture coordinates.
	 */
	public static enum TextureGen
	{
		/** Apply coordinates from the generated coordinates. */
		FACE_BOUNDS,
		/** Apply coordinates from a minimum absolute (X, Y) to maximum absolute (X, Y). */
		OBJECT_BOUNDS;
	}
	
	/** The number of sides to the shape to generate. */
	private int sides;
	/** Radius. */
	private double radius;
	/** The radian offset for the first vertex in the shape. */
	private double offsetRadians;
	/** How to generate textures on this descriptor. */
	private TextureGen textureGen;
	
	/** Generated points. */
	private Point2F[] generatedPoints;
	/** Minimum point. */
	private Point2F minPoint;
	/** Maximum point. */
	private Point2F maxPoint;
	
	/**
	 * Creates a new equilateral mesh descriptor, radius 1. 
	 * @param sides the amount of sides for the resultant shape.
	 */
	public EquilateralShapeDescriptor(int sides)
	{
		this(sides, 1.0, 0.0, null);
	}
	
	/**
	 * Creates a new equilateral mesh descriptor. 
	 * @param sides the amount of sides for the resultant shape.
	 * @param radius the radius of the mesh's shape.
	 */
	public EquilateralShapeDescriptor(int sides, double radius)
	{
		this(sides, radius, 0.0, null);
	}
	
	/**
	 * Creates a new equilateral mesh descriptor. 
	 * @param sides the amount of sides for the resultant shape.
	 * @param radius the radius of the mesh's shape.
	 * @param offsetRadians the radian offset for the first vertex on the shape. 
	 * 			The VERY first vertex in the shape is in the center. 
	 * @param textureGen the texture generator type to use. can be null for no coordinates.
	 */
	public EquilateralShapeDescriptor(int sides, double radius, double offsetRadians, TextureGen textureGen)
	{
		if (sides < 3)
			throw new IllegalArgumentException("Sides cannot be less than 3.");

		this.sides = sides;
		this.radius = radius;
		this.offsetRadians = offsetRadians;
		this.textureGen = textureGen;
		
		this.generatedPoints = new Point2F[sides];
		for (int i = 0; i < sides; i++)
			this.generatedPoints[i] = new Point2F();
		
		minPoint = new Point2F();
		maxPoint = new Point2F();
		
		setPointsForEquilateralShape(offsetRadians, radius, generatedPoints, minPoint, maxPoint);
	}
	
	/**
	 * Returns the amount of sides in this descriptor.
	 */
	public int getSides()
	{
		return sides;
	}

	/**
	 * Returns the radius used.
	 */
	public double getRadius()
	{
		return radius;
	}
	
	/**
	 * Returns the texture generator type.
	 */
	public TextureGen getTextureGenType()
	{
		return textureGen;
	}
	
	/**
	 * Returns the offset radians.
	 */
	public double getOffsetRadians()
	{
		return offsetRadians;
	}

	@Override
	public GeometryType getGeometryType()
	{
		return GeometryType.TRIANGLE_FAN;
	}

	@Override
	public int getSequenceCount()
	{
		return sides;
	}

	@Override
	public float getPositionX(int sequence)
	{
		return generatedPoints[sequence].x;
	}

	@Override
	public float getPositionY(int sequence)
	{
		return generatedPoints[sequence].y;
	}

	@Override
	public float getPositionZ(int sequence)
	{
		return 0f;
	}

	@Override
	public float getTextureS(int sequence, int layer)
	{
		if (textureGen != null) switch (textureGen)
		{
			case FACE_BOUNDS:
				return (float)RMath.getInterpolationFactor(generatedPoints[sequence].x, minPoint.x, maxPoint.x);
			case OBJECT_BOUNDS:
				return (float)RMath.getInterpolationFactor(generatedPoints[sequence].x, -radius, radius);
		}
		return 0f;
	}

	@Override
	public float getTextureT(int sequence, int layer)
	{
		if (textureGen != null) switch (textureGen)
		{
			case FACE_BOUNDS:
				return (float)(1d - RMath.getInterpolationFactor(generatedPoints[sequence].y, minPoint.y, maxPoint.y));
			case OBJECT_BOUNDS:
				return (float)(1d - RMath.getInterpolationFactor(generatedPoints[sequence].y, -radius, radius));
		}
		return 0f;
}

	@Override
	public float getNormalX(int sequence)
	{
		return 0f;
	}

	@Override
	public float getNormalY(int sequence)
	{
		return 0f;
	}

	@Override
	public float getNormalZ(int sequence)
	{
		return 1f;
	}

	@Override
	public float getColorRed(int sequence)
	{
		return 1f;
	}

	@Override
	public float getColorGreen(int sequence)
	{
		return 1f;
	}

	@Override
	public float getColorBlue(int sequence)
	{
		return 1f;
	}

	@Override
	public float getColorAlpha(int sequence)
	{
		return 1f;
	}
	
	/**
	 * Sets the points for an equilateral shape based on the number of input points.
	 * @param startRadians the starting radians for the first point.
	 * @param radius the radius of the shape.
	 * @param points the input points to set.
	 * @param min the minimums of all of the points.
	 * @param max the maximums of all of the points.
	 */
	private static void setPointsForEquilateralShape(double startRadians, double radius, Point2F[] points, Point2F min, Point2F max)
	{
		double radianDelta = (float)((Math.PI*2)/points.length);
		min.x = Float.MAX_VALUE;
		min.y = Float.MAX_VALUE;
		max.x = -Float.MAX_VALUE;
		max.y = -Float.MAX_VALUE;
		
		for (int i = 0; i < points.length; i++)
		{
			double rads = startRadians + (radianDelta*i);
			float x = (float)(Math.cos(rads) * radius);
			float y = (float)(Math.sin(rads) * radius);
			points[i].x = x;
			points[i].y = y;
			min.x = Math.min(min.x, x);
			min.y = Math.min(min.y, y);
			max.x = Math.max(max.x, x);
			max.y = Math.max(max.y, y);
		}
		
	}
	
}
