/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import com.blackrook.ogl.input.OGLInputConstants;

/**
 * A class that adapts a method of input and pipes commands into
 * the OGLCanvas, so that they are broadcast to all of the graphics nodes.
 * @author Matthew Tropiano
 */
public abstract class OGLInputAdapter
{
	/** Reference to OGLCanvas. */
	private OGLSystem system;
	
	/**
	 * Creates a new input adapter wrapped around an OGLSystem.
	 * @param system
	 */
	public OGLInputAdapter(OGLSystem system)
	{
		this.system = system;
	}
	
	/**
	 * Broadcasts a mouse movement event.
	 * @param changeX the change in mouse position X.
	 * @param positionX the new mouse position X.
	 * @param changeY the change in mouse position Y.
	 * @param positionY the new mouse position Y.
	 */
	protected final void fireMouseMove(int changeX, int positionX, int changeY, int positionY)
	{
		system.mouseMovement(changeX, positionX, changeY, positionY);
	}

	/**
	 * Broadcasts mouse button presses. 
	 * @param mouseButton a mouse button constant in {@link OGLInputConstants}.
	 */
	protected final void fireMousePress(int mouseButton)
	{
		system.mousePress(mouseButton);
	}

	/**
	 * Broadcasts mouse button releases. 
	 * @param mouseButton a mouse button constant in {@link OGLInputConstants}.
	 */
	protected final void fireMouseRelease(int mouseButton)
	{
		system.mouseRelease(mouseButton);
	}

	/**
	 * Broadcasts mouse wheel movements. 
	 * @param units the amount of mouse wheel movement. 
	 * negative = away from user, positive = towards the user.
	 */
	protected final void fireMouseWheel(int units)
	{
		system.mouseWheel(units);
	}

	/**
	 * Broadcasts the mouse entering this canvas. 
	 */
	protected final void fireMouseEntered()
	{
		system.mouseEntered();
	}

	/**
	 * Broadcasts the mouse exiting this canvas. 
	 */
	protected final void fireMouseExited()
	{
		system.mouseExited();
	}

	/**
	 * Broadcasts key presses. 
	 * @param keyCode the key constant to broadcast to reflect the pressed key (see {@link OGLInputConstants}).
	 */
	protected final void fireKeyPress(int keyCode)
	{
		system.keyPress(keyCode);
	}

	/**
	 * Broadcasts key releases. 
	 * @param keyCode the key constant to broadcast to reflect the released key (see {@link OGLInputConstants}).
	 */
	protected final void fireKeyRelease(int keyCode)
	{
		system.keyRelease(keyCode);
	}

	/**
	 * Broadcasts key typing. 
	 * @param keyCode the key constant to broadcast to reflect the typed key (see {@link OGLInputConstants}).
	 */
	protected final void fireKeyTyped(int keyCode)
	{
		system.keyTyped(keyCode);
	}
	
	/**
	 * Broadcasts gamepad button presses.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button pressed.
	 */
	protected final void fireGamepadPress(int gamepadId, int gamepadButton)
	{
		system.gamepadPress(gamepadId, gamepadButton);
	}
	
	/**
	 * Broadcasts gamepad button releases.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button released.
	 */
	protected final void fireGamepadRelease(int gamepadId, int gamepadButton)
	{
		system.gamepadRelease(gamepadId, gamepadButton);
	}
	
	/**
	 * Broadcasts gamepad axis changes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the id of the axis that changed.
	 * @param value the axis value.
	 */
	protected final void fireGamepadAxis(int gamepadId, int gamepadAxisId, float value)
	{
		system.gamepadAxis(gamepadId, gamepadAxisId, value);
	}
	
	/**
	 * Broadcasts gamepad axis taps.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the id of the axis that changed.
	 * @param positive if true, the axis value is positive, if false, negative.
	 */
	protected final void fireGamepadAxisTap(int gamepadId, int gamepadAxisId, boolean positive)
	{
		system.gamepadAxisTap(gamepadId, gamepadAxisId, positive);
	}
	
}
