/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import com.blackrook.ogl.OGLCanvasNode;
import com.blackrook.ogl.OGLGraphics;



/**
 * An abstract canvas node class that basically overrides most
 * of {@link OGLCanvasNode} methods and does NOTHING with them.
 * It's merely a convenience class in the vein of "Adapter" classes
 * that helps reduce code clutter.
 * <p>
 * The input methods are overridden and return <code>false</code>. <br>
 * The methods {@link #getPolygonsRendered()} and {@link #getRenderTimeNanos()} return 0.<br>
 * The methods {@link #display(OGLGraphics)} and {@link #onCanvasResize(int, int)} do nothing unless overridden.<br>
 * The method {@link #isEnabled()} returns <code>true</code>.
 * @author Matthew Tropiano
 */
public abstract class OGLCanvasNodeAdapter implements OGLCanvasNode
{

	@Override
	public void display(OGLGraphics g)
	{
		// Do nothing.
	}

	@Override
	public void onCanvasResize(int newWidth, int newHeight)
	{
		// Do nothing.
	}

	@Override
	public int getPolygonsRendered()
	{
		return 0;
	}

	@Override
	public long getRenderTimeNanos()
	{
		return 0L;
	}

	@Override
	public boolean glKeyPress(int keycode)
	{
		return false;
	}

	@Override
	public boolean glKeyRelease(int keycode)
	{
		return false;
	}

	@Override
	public boolean glKeyTyped(int keycode)
	{
		return false;
	}

	@Override
	public void glMouseMove(int unitsX, int coordinateX, int unitsY, int coordinateY)
	{
	}

	@Override
	public boolean glMousePress(int mousebutton)
	{
		return false;
	}

	@Override
	public boolean glMouseRelease(int mousebutton)
	{
		return false;
	}

	@Override
	public boolean glMouseWheel(int units)
	{
		return false;
	}

	@Override
	public void glMouseEnter()
	{
	}

	@Override
	public void glMouseExit()
	{
	}

	@Override
	public boolean glGamepadPress(int gamepadId, int gamepadButton)
	{
		return false;
	}

	@Override
	public boolean glGamepadRelease(int gamepadId, int gamepadButton)
	{
		return false;
	}

	@Override
	public boolean glGamepadAxisChange(int gamepadId, int gamepadAxisId, float value)
	{
		return false;
	}

	@Override
	public boolean glGamepadAxisTap(int gamepadId, int gamepadAxisId, boolean positive)
	{
		return false;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

}
