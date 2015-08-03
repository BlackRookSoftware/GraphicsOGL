/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.enums;

import javax.media.opengl.GL2;

/**
 * Describes a list of possible framebuffer attachments.
 * @author Matthew Tropiano
 */
public enum AttachPoint
{
	COLOR0(GL2.GL_COLOR_ATTACHMENT0),
	COLOR1(GL2.GL_COLOR_ATTACHMENT1),
	COLOR2(GL2.GL_COLOR_ATTACHMENT2),
	COLOR3(GL2.GL_COLOR_ATTACHMENT3),
	COLOR4(GL2.GL_COLOR_ATTACHMENT4),
	COLOR5(GL2.GL_COLOR_ATTACHMENT5),
	COLOR6(GL2.GL_COLOR_ATTACHMENT6),
	COLOR7(GL2.GL_COLOR_ATTACHMENT7),
	COLOR8(GL2.GL_COLOR_ATTACHMENT8),
	COLOR9(GL2.GL_COLOR_ATTACHMENT9),
	COLOR10(GL2.GL_COLOR_ATTACHMENT10),
	COLOR11(GL2.GL_COLOR_ATTACHMENT11),
	COLOR12(GL2.GL_COLOR_ATTACHMENT12),
	COLOR13(GL2.GL_COLOR_ATTACHMENT13),
	COLOR14(GL2.GL_COLOR_ATTACHMENT14),
	COLOR15(GL2.GL_COLOR_ATTACHMENT15),
	DEPTH(GL2.GL_DEPTH_ATTACHMENT),
	STENCIL(GL2.GL_STENCIL_ATTACHMENT),
	DEPTH_STENCIL(GL2.GL_DEPTH_STENCIL_ATTACHMENT);
	
	public final int glVal;
	AttachPoint(int gltype) {glVal = gltype;}
	
}
