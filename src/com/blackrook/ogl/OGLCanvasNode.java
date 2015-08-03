/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import com.blackrook.ogl.input.OGLInputConstants;

/**
 * A significant entry point whose methods that get executed upon the
 * graphics system requesting a canvas redraw or upon receiving input.
 * <p>
 * Canvas nodes are added to a {@link OGLSystem}, and drawn in the order
 * in which they were added, and receive input events in the opposite order.
 * @author Matthew Tropiano
 */
public interface OGLCanvasNode extends OGLInputConstants
{
	/**
	 * Displays this node.
	 * @param g the {@link OGLGraphics} object used for issuing commands to OpenGL.
	 */
	public void display(OGLGraphics g);
	
	/**
	 * Called when the system canvas gets resized, or once
	 * this node gets added to the canvas.
	 * @param newWidth	the new width. 
	 * @param newHeight	the new height.
	 */
	public void onCanvasResize(int newWidth, int newHeight);
	
	/**
	 * Checks if this node is enabled.
	 * If the node is enabled, its {@link #display(OGLGraphics)} method is
	 * called on canvas draw (in the order in which they were added), 
	 * and input events are broadcast to the node (in the <i>opposite</i> 
	 * order in which they were added).  
	 */
	public boolean isEnabled();
	
	/**
	 * Returns the length of time it took to render this node, in nanoseconds.
	 * Results of this call should not be considered accurate until the node 
	 * has had {@link #display(OGLGraphics)} called on it.
	 */
	public long getRenderTimeNanos();

	/**
	 * Returns the number of polygonal objects rendered in this layer.
	 */
	public int getPolygonsRendered();

	/**
	 * Called on a key press.
	 * @param keycode the {@link OGLInputConstants} key code.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glKeyPress(int keycode);

	/**
	 * Called on a key release.
	 * @param keycode the {@link OGLInputConstants} key code.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glKeyRelease(int keycode);

	/**
	 * Called on a key being typed.
	 * @param keycode the {@link OGLInputConstants} key code.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glKeyTyped(int keycode);

	/**
	 * Called on a mouse button press.
	 * @param mousebutton the {@link OGLInputConstants} mouse button code.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glMousePress(int mousebutton);

	/**
	 * Called on a mouse button release.
	 * @param mousebutton the {@link OGLInputConstants} mouse button code.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glMouseRelease(int mousebutton);
	
	/**
	 * Called on a mouse wheel move.
	 * @param units the amount (in units) that the wheel was spun.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glMouseWheel(int units);

	/**
	 * Called on a mouse move.
	 * @param unitsX the amount (in pixels) that the mouse was moved on the X axis.
	 * @param coordinateX mouse cursor's new x-coordinate position in the canvas.
	 * @param unitsY the amount (in pixels) that the mouse was moved on the Y axis.
	 * @param coordinateY mouse cursor's new y-coordinate position in the canvas.
	 */
	public void glMouseMove(int unitsX, int coordinateX, int unitsY, int coordinateY);

	/**
	 * Called when the mouse enters the canvas.
	 */
	public void glMouseEnter();

	/**
	 * Called when the mouse exits the canvas.
	 */
	public void glMouseExit();
	
	/**
	 * Called when a gamepad button is pressed.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button pressed.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glGamepadPress(int gamepadId, int gamepadButton);
	
	/**
	 * Called when a gamepad button is released.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button released.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glGamepadRelease(int gamepadId, int gamepadButton);
	
	/**
	 * Called when a gamepad axis value changes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the id of the axis that changed.
	 * @param value the axis value.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glGamepadAxisChange(int gamepadId, int gamepadAxisId, float value);
	
	/**
	 * Called when a gamepad axis is tapped.
	 * A "tap" is defined as an axis being brought out of a defined deadzone
	 * and towards the edge of an axis.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the id of the axis that changed.
	 * @param positive true if the axis value is positive, false if negative.
	 * @return true if this node has handled the event, false otherwise.
	 * Returning true will halt the check for the rest of the nodes.
	 */
	public boolean glGamepadAxisTap(int gamepadId, int gamepadAxisId, boolean positive);
	
}
