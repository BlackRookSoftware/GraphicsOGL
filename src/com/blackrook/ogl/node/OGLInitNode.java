/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import javax.media.opengl.*;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.FillMode;
import com.blackrook.ogl.enums.HintType;

/**
 * A one-time triggered node that performs usually one-time
 * OpenGL setup commands, like clear color, VSYNC, hints, or whatever.
 * <p>If you change anything on this node before it is triggered, you
 * will have to set it to be triggered again in order to take effect.
 * @author Matthew Tropiano
 */
public class OGLInitNode extends OGLTriggeredNode
{
	/** Point smoothing hint. */
	private HintType glPointSmoothHint;
	/** Line smoothing hint. */
	private HintType glLineSmoothHint;
	/** Polygon smoothing hint. */
	private HintType glPolygonSmoothHint;
	/** Fog calculation hint. */
	private HintType glFogCalculationHint;
	/** Perspective correction hint. */
	private HintType glPerspectiveCorrectionHint;
	/** Generate Mipmapping hint. */
	private HintType glGenerateMipmapHint;
	/** Texture compression hint. */
	private HintType glTextureCompressionHint;
	/** Is VSYNC enabled? */
	private boolean vsyncEnabled;
	/** Global Fill mode. */
	private FillMode fillMode;
	

	/**
	 * Creates a new init node.<br>
	 * By default:
	 * <p>
	 * VSYNC is disabled.<br>
	 * All hints are set to {@link HintType}.DONT_CARE;<br>
	 * The clear color is (0,0,0,0), RGBA.<br>
	 * The fill mode is FILLED.
	 */
	public OGLInitNode()
	{
		setAllHintsTo(HintType.DONT_CARE);
		setVsyncEnabled(false);
		setFillMode(FillMode.FILLED);
	}
	
	@Override
	public void doTriggeredFunction(OGLGraphics g)
	{
    	GL2 gl = g.getGL();
    	gl.setSwapInterval(vsyncEnabled ? 1 : 0);
		gl.glHint(GL2.GL_POINT_SMOOTH_HINT, glPointSmoothHint.glValue);
		gl.glHint(GL2.GL_LINE_SMOOTH_HINT, glLineSmoothHint.glValue);
		gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, glPolygonSmoothHint.glValue);
		gl.glHint(GL2.GL_FOG_HINT, glFogCalculationHint.glValue);
		gl.glHint(GL2.GL_GENERATE_MIPMAP_HINT, glGenerateMipmapHint.glValue);
		gl.glHint(GL2.GL_TEXTURE_COMPRESSION_HINT, glTextureCompressionHint.glValue);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, glPerspectiveCorrectionHint.glValue);
		g.setFillMode(fillMode);
	}

	/**
	 * Get point smooth hint value.
	 */
	public HintType getPointSmoothHint()
	{
		return glPointSmoothHint;
	}

	/**
	 * Get line smooth hint value.
	 */
	public HintType getLineSmoothHint()
	{
		return glLineSmoothHint;
	}

	/**
	 * Get polygon smooth hint value.
	 */
	public HintType getPolygonSmoothHint()
	{
		return glPolygonSmoothHint;
	}

	/**
	 * Get fog color calculation hint.
	 */
	public HintType getFogCalculationHint()
	{
		return glFogCalculationHint;
	}

	/**
	 * Get mipmap generation hint.
	 */
	public HintType getGenerateMipmapHint()
	{
		return glGenerateMipmapHint;
	}

	/**
	 * Get texture compression hint.
	 */
	public HintType getTextureCompressionHint()
	{
		return glTextureCompressionHint;
	}

	/**
	 * Set point smooth hint value.
	 */
	public void setPointSmoothHint(HintType pointSmoothHint)
	{
		glPointSmoothHint = pointSmoothHint;
	}

	/**
	 * Set line smooth hint value.
	 */
	public void setLineSmoothHint(HintType lineSmoothHint)
	{
		glLineSmoothHint = lineSmoothHint;
	}

	/**
	 * Set polygon smooth hint value.
	 */
	public void setPolygonSmoothHint(HintType polygonSmoothHint)
	{
		glPolygonSmoothHint = polygonSmoothHint;
	}

	/**
	 * Set fog calculation hint.
	 */
	public void setFogCalculationHint(HintType fogCalculationHint)
	{
		glFogCalculationHint = fogCalculationHint;
	}

	/**
	 * Set mipmap generation hint.
	 */
	public void setGenerateMipmapHint(HintType generateMipmapHint)
	{
		glGenerateMipmapHint = generateMipmapHint;
	}

	/**
	 * Set texture compression hint.
	 */
	public void setTextureCompressionHint(HintType textureCompressionHint)
	{
		glTextureCompressionHint = textureCompressionHint;
	}

	/**
	 * Get prespective correction hint. 
	 */
	public HintType getPerspectiveCorrectionHint()
	{
		return glPerspectiveCorrectionHint;
	}

	/**
	 * Set prespective correction hint. 
	 */
	public void setPerspectiveCorrectionHint(HintType perspectiveCorrectionHint)
	{
		glPerspectiveCorrectionHint = perspectiveCorrectionHint;
	}

	/**
	 * Sets all hint options to...
	 */
	public void setAllHintsTo(HintType hint)
	{
		setFogCalculationHint(hint);
		setGenerateMipmapHint(hint);
		setLineSmoothHint(hint);
		setPerspectiveCorrectionHint(hint);
		setPointSmoothHint(hint);
		setPolygonSmoothHint(hint);
		setTextureCompressionHint(hint);
	}

	/**
	 * Should this canvas await next vertical refresh?
	 */
	public void setVsyncEnabled(boolean vsyncEnabled)
	{
		this.vsyncEnabled = vsyncEnabled;
	}

	/**
	 * Is this canvas awaiting next vertical refresh?
	 */
	public boolean getVsyncEnabled()
	{
		return vsyncEnabled;
	}

	/**
	 * Sets the global filling mode.
	 * @param fillMode the fill mode to use.
	 */
	public void setFillMode(FillMode fillMode)
	{
		this.fillMode = fillMode;
	}
}
