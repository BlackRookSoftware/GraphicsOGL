/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * GLU Quadric encapsulation class.
 * A quadric is a set of encapsulated parameters that contain how GLU draws
 * complex geometric shapes. All calls to the "draw" methods draw geometry to
 * the supplied graphics context using the current settings in the quadric.
 * <p>
 * Geometry created by the quadric can be expensive to create. The calls are better
 * encapsulated in a display list for better efficiency.
 * <p>
 * Remember to destroy this class when you are done with it! Java's finalize() will not
 * do it!
 * @author Matthew Tropiano
 */
public class OGLQuadric
{
	/** Internal handle to a Quadric object in GLU. */
	protected GLUquadric quadric;

	/** Quadric drawing style enumeration. */
	public static enum DrawStyle
	{
		POINT(GLU.GLU_POINT),
		LINE(GLU.GLU_LINE),
		FILL(GLU.GLU_FILL),
		SILHOUETTE(GLU.GLU_SILHOUETTE);
		
		final int gluVal;
		private DrawStyle(int glVal) {this.gluVal = glVal;}
	}
	
	/** Quadric normal generation style enumeration. */
	public static enum NormalStyle
	{
		NONE(GLU.GLU_NONE),
		FLAT(GLU.GLU_FLAT),
		SMOOTH(GLU.GLU_SMOOTH);
		
		final int gluVal;
		private NormalStyle(int glVal) {this.gluVal = glVal;}
	}
	
	/** Quadric Orientation enumeration. */
	public static enum Orientation
	{
		OUTSIDE(GLU.GLU_OUTSIDE),
		INSIDE(GLU.GLU_INSIDE);
		
		final int gluVal;
		private Orientation(int glVal) {this.gluVal = glVal;}
	}
	
	/** Current drawing style. */
	protected DrawStyle drawStyle;
	/** Current normal style. */
	protected NormalStyle normalStyle;
	/** Current orientation of normals. */
	protected Orientation orientation;
	/** Current switch state for texture coordinate generation. */
	protected boolean textureGeneration;
	
	/**
	 * Creates a new Quadric object.
	 * @param g the graphics context to use.
	 */
	public OGLQuadric(OGLGraphics g)
	{
		GLU glu = g.getGLU();
		quadric = glu.gluNewQuadric();
		setDrawStyle(g, DrawStyle.FILL);
		setNormalStyle(g, NormalStyle.SMOOTH);
		setOrientation(g, Orientation.OUTSIDE);
		setTextureGenerationEnabled(g, false);
	}

	/**
	 * Gets the current drawing style of this quadric.
	 */
	public DrawStyle getDrawStyle()
	{
		return drawStyle;
	}

	/**
	 * Sets the current drawing style of this quadric.
	 */
	public void setDrawStyle(OGLGraphics g, DrawStyle drawStyle)
	{
		if (drawStyle == null)
			drawStyle = DrawStyle.FILL;
		g.getGLU().gluQuadricDrawStyle(quadric, drawStyle.gluVal);
		this.drawStyle = drawStyle;
	}

	/**
	 * Gets the current normal generation style of this quadric.
	 */
	public NormalStyle getNormalStyle()
	{
		return normalStyle;
	}

	/**
	 * Sets the current normal generation style of this quadric.
	 */
	public void setNormalStyle(OGLGraphics g, NormalStyle normalStyle)
	{
		if (normalStyle == null)
			normalStyle = NormalStyle.SMOOTH;
		g.getGLU().gluQuadricNormals(quadric, normalStyle.gluVal);
		this.normalStyle = normalStyle;
	}

	/**
	 * Gets the current normal vector orientation style of this quadric.
	 */
	public Orientation getOrientation()
	{
		return orientation;
	}

	/**
	 * Sets the current normal vector orientation style of this quadric.
	 */
	public void setOrientation(OGLGraphics g, Orientation orientation)
	{
		if (orientation == null)
			orientation = Orientation.OUTSIDE;
		g.getGLU().gluQuadricOrientation(quadric, orientation.gluVal);
		this.orientation = orientation;
	}

	/**
	 * Gets if texture coordinate generation is enabled on this quadric.
	 * True if so, false otherwise.
	 */
	public boolean isTextureGenerationEnabled()
	{
		return textureGeneration;
	}

	/**
	 * Gets if texture coordinate generation is enabled on this quadric.
	 * True if so, false otherwise.
	 */
	public void setTextureGenerationEnabled(OGLGraphics g, boolean textureGeneration)
	{
		g.getGLU().gluQuadricTexture(quadric, textureGeneration);
		this.textureGeneration = textureGeneration;
	}
	
	/**
	 * Destroys/invalidates this quadric.
	 * Subsequent calls to this quadric's methods will throw NullPointerExceptions.
	 */
	public void destroy(OGLGraphics g)
	{
		g.getGLU().gluDeleteQuadric(quadric);
		quadric = null;
	}
	
	/**
	 * Returns true if the internal handle to 
	 * this quadric has not been destroyed.
	 * @return true if not destroyed, false if so.
	 */
	public boolean isAllocated()
	{
		return quadric != null;
	}
	
	/**
	 * Draws a cylinder to the graphics context using this quadric.
	 * @param g the target graphics context.
	 * @param baseRadius the radius of the bottom of the cylinder in units.
	 * @param topRadius the radius of the top of the cylinder in units.
	 * @param height the height of the cylinder in units.
	 * @param slices the amount of sides on the cylinder. 
	 * @param stacks the amount of subdivisions along the cylinder's height.
	 */
	public void drawCylinder(OGLGraphics g, double baseRadius, double topRadius, double height, int slices, int stacks)
	{
		g.getGLU().gluCylinder(quadric, baseRadius, topRadius, height, slices, stacks);
	}
	
	/**
	 * Draws a sphere to the graphics context using this quadric.
	 * @param g the target graphics context.
	 * @param radius the radius of the sphere in units.
	 * @param slices the amount of sides on the sphere. 
	 * @param stacks the amount of subdivisions along the sphere's height.
	 */
	public void drawSphere(OGLGraphics g, double radius, int slices, int stacks)
	{
		g.getGLU().gluSphere(quadric, radius, slices, stacks);
	}
	
	/**
	 * Draws a flat disk to the graphics context using this quadric.
	 * @param g the target graphics context.
	 * @param internalRadius the radius of the inside hole of the disk. 0 = no hole.
	 * @param externalRadius the radius of the top of the cylinder in units.
	 * @param slices the amount of sides on the disk. 
	 * @param stacks the amount of subdivisions along the disk's surface, going around.
	 */
	public void drawDisk(OGLGraphics g, double internalRadius, double externalRadius, int slices, int stacks)
	{
		g.getGLU().gluDisk(quadric, internalRadius, externalRadius, slices, stacks);
	}
	
	/**
	 * Draws a partial flat disk to the graphics context using this quadric.
	 * @param g the target graphics context.
	 * @param internalRadius the radius of the inside hole of the disk. 0 = no hole.
	 * @param externalRadius the radius of the top of the cylinder in units.
	 * @param slices the amount of sides on the disk. 
	 * @param stacks the amount of subdivisions along the disk's surface, going around.
	 * @param startAngle the starting angle of the disk.
	 * @param angle the angle sweep of the disk.
	 */
	public void drawPartialDisk(OGLGraphics g, double internalRadius, double externalRadius, int slices, int stacks, double startAngle, double angle)
	{
		g.getGLU().gluPartialDisk(quadric, internalRadius, externalRadius, slices, stacks, startAngle, angle);
	}
	
}
