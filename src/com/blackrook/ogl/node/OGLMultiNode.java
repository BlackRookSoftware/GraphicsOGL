/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import com.blackrook.commons.list.List;
import com.blackrook.ogl.OGLCanvasNode;
import com.blackrook.ogl.OGLGraphics;

/**
 * An listener node that contains other {@link OGLCanvasNode}s.
 * Contains methods to invoke before they are displayed and after they are displayed.
 * @author Matthew Tropiano
 */
public class OGLMultiNode implements OGLCanvasNode
{
	/** Canvas listener list. */
	private List<OGLCanvasNode> canvasNodeList;

	/** Is this layer (and its children) enabled? */
	private boolean enabled;
	/** Is this layer enabled for accepting input? */
	private boolean acceptsInput;

	/** Render time in nanos. */
	protected long renderTimeNanos;
	/** Polygons Rendered */
	protected int polygonsRendered;

	/**
	 * The default constructor.
	 */
	public OGLMultiNode()
	{
		canvasNodeList = new List<OGLCanvasNode>(4);
		enabled = true;
		acceptsInput = true;
	}

	/**
	 * Adds a CanvasNode to this system.
	 */
	public void addCanvasNode(OGLCanvasNode l)
	{
		canvasNodeList.add(l);
	}

	/**
	 * Removes a CanvasListener from this system.
	 */
	public boolean removeCanvasNode(OGLCanvasNode l)
	{
		return canvasNodeList.remove(l);
	}

	@Override
	public void onCanvasResize(int new_width, int new_height)
	{
		preCanvasResize(new_width, new_height);
		for (OGLCanvasNode sys : canvasNodeList)
			sys.onCanvasResize(new_width, new_height);
		postCanvasResize(new_width, new_height);
	}

	@Override
	public void display(OGLGraphics g)
	{
		polygonsRendered = 0;
		long nanos = System.nanoTime();
		preNodeDisplay(g);
		for (OGLCanvasNode sys : canvasNodeList)
		{
			if (sys.isEnabled())
			{
				sys.display(g);
				polygonsRendered += sys.getPolygonsRendered();
			}
		}
		postNodeDisplay(g);
		renderTimeNanos = System.nanoTime() - nanos;
	}

	/**
	 * Called by canvasResized() before all of the attached listeners are canvasResized().
	 * Does nothing by default.
	 */
	public void preCanvasResize(int new_width, int new_height)
	{
		// Do nothing.
	}
	
	/**
	 * Called by canvasResized() after all of the attached listeners are canvasResized().
	 * Does nothing by default.
	 */
	public void postCanvasResize(int new_width, int new_height)
	{
		// Do nothing.
	}
	
	/**
	 * Called by display() before all of the attached listeners are displayed.
	 * Does nothing by default.
	 */
	public void preNodeDisplay(OGLGraphics g)
	{
		// Do nothing.
	}
	
	/**
	 * Called by display() after all of the attached listeners are displayed.
	 * Does nothing by default.
	 */
	public void postNodeDisplay(OGLGraphics g)
	{
		// Do nothing.
	}
	
	@Override
	public boolean glKeyPress(int keycode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glKeyPress(keycode))
				return true;
		}
		return false;
	}

	@Override
	public boolean glKeyRelease(int keycode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glKeyRelease(keycode))
				return true;
		}
		return false;
	}

	@Override
	public boolean glKeyTyped(int keycode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glKeyTyped(keycode))
				return true;
		}
		return false;
	}

	@Override
	public boolean glMousePress(int mousebutton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glMousePress(mousebutton))
				return true;
		}
		return false;
	}

	@Override
	public boolean glMouseRelease(int mousebutton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glMouseRelease(mousebutton))
				return true;
		}
		return false;
	}

	@Override
	public boolean glMouseWheel(int units)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glMouseWheel(units))
				return true;
		}
		return false;
	}

	@Override
	public void glMouseMove(int unitsX, int coordinateX, int units, int coordinate)
	{
		for (OGLCanvasNode node : canvasNodeList)
			node.glMouseMove(unitsX, coordinateX, units, coordinate);
	}

	@Override
	public void glMouseEnter()
	{
		for (OGLCanvasNode node : canvasNodeList)
			node.glMouseEnter();
	}

	@Override
	public void glMouseExit()
	{
		for (OGLCanvasNode node : canvasNodeList)
			node.glMouseExit();
	}

	@Override
	public boolean glGamepadPress(int gamepadId, int gamepadButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glGamepadPress(gamepadId, gamepadButton))
				return true;
		}
		return false;
	}

	@Override
	public boolean glGamepadRelease(int gamepadId, int gamepadButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glGamepadRelease(gamepadId, gamepadButton))
				return true;
		}
		return false;
	}

	@Override
	public boolean glGamepadAxisChange(int gamepadId, int gamepadAxisId, float value)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glGamepadAxisChange(gamepadId, gamepadAxisId, value))
				return true;
		}
		return false;
	}

	@Override
	public boolean glGamepadAxisTap(int gamepadId, int gamepadAxisId, boolean positive)
	{
		// goes backwards. last to be drawn is closest to the screen.
		if (acceptsInput) for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode node = canvasNodeList.getByIndex(i);
			if (node != null && node.isEnabled() && node.glGamepadAxisTap(gamepadId, gamepadAxisId, positive))
				return true;
		}
		return false;
	}

	@Override
	public int getPolygonsRendered()
	{
		return polygonsRendered;
	}

	@Override
	public long getRenderTimeNanos()
	{
		return renderTimeNanos;
	}

	@Override
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Sets if this node (and all of its contained nodes) is enabled.
	 * @see #isEnabled()
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * Gets if this node is enabled for accepting input.
	 */
	public boolean isAcceptingInput()
	{
		return acceptsInput;
	}

	/**
	 * Sets if this and its children are enabled for accepting input.
	 */
	public void setAcceptingInput(boolean acceptsInput)
	{
		this.acceptsInput = acceptsInput;
	}

}
