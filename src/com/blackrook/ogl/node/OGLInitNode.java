/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.FillMode;
import com.blackrook.ogl.enums.HintType;
import com.blackrook.ogl.enums.HintValue;

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
	private HintValue glPointSmoothHint;
	/** Line smoothing hint. */
	private HintValue glLineSmoothHint;
	/** Polygon smoothing hint. */
	private HintValue glPolygonSmoothHint;
	/** Fog calculation hint. */
	private HintValue glFogCalculationHint;
	/** Perspective correction hint. */
	private HintValue glPerspectiveCorrectionHint;
	/** Generate Mipmapping hint. */
	private HintValue glGenerateMipmapHint;
	/** Texture compression hint. */
	private HintValue glTextureCompressionHint;
	/** Is VSYNC enabled? */
	private boolean vsyncEnabled;
	/** Global Fill mode. */
	private FillMode fillMode;

	/**
	 * Creates a new init node.<br>
	 * By default:
	 * <p>
	 * VSYNC is disabled.<br>
	 * All hints are set to {@link HintValue}.DONT_CARE;<br>
	 * The clear color is (0,0,0,0), RGBA.<br>
	 * The fill mode is FILLED.
	 */
	public OGLInitNode()
	{
		setAllHintsTo(HintValue.DONT_CARE);
		setVsyncEnabled(false);
		setFillMode(FillMode.FILLED);
	}
	
	@Override
	public void doTriggeredFunction(OGLGraphics g)
	{
    	g.setVSync(vsyncEnabled);
		g.setHint(HintType.POINT_SMOOTHING, glPointSmoothHint);
		g.setHint(HintType.LINE_SMOOTHING, glLineSmoothHint);
		g.setHint(HintType.POLYGON_SMOOTHING, glPolygonSmoothHint);
		g.setHint(HintType.FOG, glFogCalculationHint);
		g.setHint(HintType.MIPMAPPING, glGenerateMipmapHint);
		g.setHint(HintType.TEXTURE_COMPRESSION, glTextureCompressionHint);
		g.setHint(HintType.PERSPECTIVE_CORRECTION, glPerspectiveCorrectionHint);
		g.setFillMode(fillMode);
	}

	/**
	 * Get point smooth hint value.
	 */
	public HintValue getPointSmoothHint()
	{
		return glPointSmoothHint;
	}

	/**
	 * Get line smooth hint value.
	 */
	public HintValue getLineSmoothHint()
	{
		return glLineSmoothHint;
	}

	/**
	 * Get polygon smooth hint value.
	 */
	public HintValue getPolygonSmoothHint()
	{
		return glPolygonSmoothHint;
	}

	/**
	 * Get fog color calculation hint.
	 */
	public HintValue getFogCalculationHint()
	{
		return glFogCalculationHint;
	}

	/**
	 * Get mipmap generation hint.
	 */
	public HintValue getGenerateMipmapHint()
	{
		return glGenerateMipmapHint;
	}

	/**
	 * Get texture compression hint.
	 */
	public HintValue getTextureCompressionHint()
	{
		return glTextureCompressionHint;
	}

	/**
	 * Set point smooth hint value.
	 */
	public void setPointSmoothHint(HintValue pointSmoothHint)
	{
		glPointSmoothHint = pointSmoothHint;
	}

	/**
	 * Set line smooth hint value.
	 */
	public void setLineSmoothHint(HintValue lineSmoothHint)
	{
		glLineSmoothHint = lineSmoothHint;
	}

	/**
	 * Set polygon smooth hint value.
	 */
	public void setPolygonSmoothHint(HintValue polygonSmoothHint)
	{
		glPolygonSmoothHint = polygonSmoothHint;
	}

	/**
	 * Set fog calculation hint.
	 */
	public void setFogCalculationHint(HintValue fogCalculationHint)
	{
		glFogCalculationHint = fogCalculationHint;
	}

	/**
	 * Set mipmap generation hint.
	 */
	public void setGenerateMipmapHint(HintValue generateMipmapHint)
	{
		glGenerateMipmapHint = generateMipmapHint;
	}

	/**
	 * Set texture compression hint.
	 */
	public void setTextureCompressionHint(HintValue textureCompressionHint)
	{
		glTextureCompressionHint = textureCompressionHint;
	}

	/**
	 * Get prespective correction hint. 
	 */
	public HintValue getPerspectiveCorrectionHint()
	{
		return glPerspectiveCorrectionHint;
	}

	/**
	 * Set prespective correction hint. 
	 */
	public void setPerspectiveCorrectionHint(HintValue perspectiveCorrectionHint)
	{
		glPerspectiveCorrectionHint = perspectiveCorrectionHint;
	}

	/**
	 * Sets all hint options to...
	 */
	public void setAllHintsTo(HintValue hint)
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
	
	/**
	 * Gets the global filling mode.
	 */
	public FillMode getFillMode()
	{
		return fillMode;
	}
	
}
