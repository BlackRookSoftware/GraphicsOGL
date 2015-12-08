/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import com.jogamp.opengl.*;

import com.blackrook.ogl.OGLGraphics;

/**
 * Attribute types for attribute states.
 * Usually for {@link OGLGraphics#attribPush(AttribType...)}.
 * @author Matthew Tropiano
 */
public enum AttribType
{
	/** 
	 * Accumulation buffer attribute bit.
	 * <p>Governs current accumulation buffer clear value. 
	 */
	ACCUM_BUFFER(GL2.GL_ACCUM_BUFFER_BIT),
	/** 
	 * Color buffer attribute bit.
	 * <p>Governs:
	 * <ul>
	 * <li>Alpha test state, function, and values.</li>
	 * <li>Blending state, function, and values.</li>
	 * <li>GL_DITHER state.</li>
	 * <li>Current drawing buffer(s).</li>
	 * <li>Current logical operation state and function.</li>
	 * <li>Current RGBA/index clear color and write masks.</li> 
	 * </ul> 
	 */
	COLOR_BUFFER(GL2.GL_COLOR_BUFFER_BIT),
	/**
	 * Current state attribute bit.
	 * <p>Governs:
	 * <ul>
	 * <li>Current RGBA color or color index.</li>
	 * <li>Current lighting normal and texture coordinate.</li>
	 * <li>Current raster position, GL_CURRENT_RASTER_POSITION_VALID, and GL_EDGE_FLAG.</li>
	 * <li>GL_DEPTH_BUFFER_BITGL_DEPTH_TEST state, depth buffer function, depth buffer clear value, and GL_DEPTH_WRITEMASK state.</li>
	 * </ul>   
	 */
	CURRENT(GL2.GL_CURRENT_BIT),
	DEPTH_BUFFER(GL2.GL_DEPTH_BUFFER_BIT),
	/**
	 * Current ENABLE state.
	 * <p>Governs:
	 * <ul>
	 * <li>GL_ALPHA_TEST, GL_AUTO_NORMAL, and GL_BLEND state.</li>
	 * <li>User-defined clipping plane state.</li>
	 * <li>GL_COLOR_MATERIAL, GL_CULL_FACE, GL_DEPTH_TEST, GL_DITHER, GL_FOG, 
	 * GL_LIGHTi, GL_LIGHTING, GL_LINE_SMOOTH, GL_LINE_STIPPLE, GL_LOGIC_OP, 
	 * GL_MAP1_x, GL_MAP2_x, GL_NORMALIZE, GL_POINT_SMOOTH, GL_POLYGON_SMOOTH, 
	 * GL_POLYGON_STIPPLE, GL_SCISSOR_TEST, GL_STENCIL_TEST, GL_TEXTURE_1D, 
	 * GL_TEXTURE_2D, and GL_TEXTURE_GEN_x states.</li>
	 * </ul>
	 */
	ENABLE(GL2.GL_ENABLE_BIT),
	EVAL(GL2.GL_EVAL_BIT),
	FOG(GL2.GL_FOG_BIT),
	HINT(GL2.GL_HINT_BIT),
	LIGHTING(GL2.GL_LIGHTING_BIT),
	LINE(GL2.GL_LINE_BIT),
	LIST(GL2.GL_LIST_BIT),
	PIXEL_MODE(GL2.GL_PIXEL_MODE_BIT),
	POINT(GL2.GL_POINT_BIT),
	POLYGON(GL2.GL_POLYGON_BIT),
	POLYGON_STIPPLE(GL2.GL_POLYGON_STIPPLE_BIT),
	SCISSOR(GL2.GL_SCISSOR_BIT),
	STENCIL_BUFFER(GL2.GL_STENCIL_BUFFER_BIT),
	TEXTURE(GL2.GL_TEXTURE_BIT),
	TRANSFORM(GL2.GL_TRANSFORM_BIT),
	VIEWPORT(GL2.GL_VIEWPORT_BIT);

	public final int glValue;
	private AttribType (int val) {glValue = val;}

}
