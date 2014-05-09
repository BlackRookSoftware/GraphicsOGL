/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;


/**
 * An interface that describes all classes that hold an exposable
 * {@link OGLSystem} instance and can influence how the context is rendered. 
 * @author Matthew Tropiano
 */
public interface OGLDisplay
{
	/**
	 * Returns the {@link OGLSystem} at the heart of this displayable.
	 */
	public OGLSystem getSystem();
	
	/**
	 * Returns the absolute X-coordinate of the visible canvas (on screen, in pixels).
	 */
	public int getCanvasAbsoluteX();
	
	/**
	 * Returns the absolute Y-coordinate of the visible canvas (on screen, in pixels).
	 */
	public int getCanvasAbsoluteY();
	
	/**
	 * Calls {@link #display()} on the next frame with no interpolant
	 * for the next frame.
	 */
	public void display();

	/**
	 * Is this in fullscreen mode?
	 */
	public boolean isFullscreen();

	/**
	 * Returns the estimated frames per second in this canvas
	 * based on the time to render the visible nodes.
	 */
	public float getFPS();

	/**
	 * Starts an auto-redraw thread that attempts to render at a steady
	 * framerate by calling {@link #display()} at a specific rate. If already running,
	 * changes the framerate.
	 * If set to 0, this calls {@link #display()} as many times as it can.
	 * @param fps the target rate in frames per second, or 0 for full bore.
	 * @see #stopAutoRedraw()
	 */
	public void startAutoRedraw(int fps);

	/**
	 * Stops any auto-redraw started by {@link #startAutoRedraw(int)}.
	 */
	public void stopAutoRedraw();

	/**
	 * This destroys this object.
	 */
	public void destroy();

}
