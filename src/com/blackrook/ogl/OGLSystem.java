/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.blackrook.commons.list.List;
import com.blackrook.ogl.input.OGLInputConstants;
import com.blackrook.ogl.object.buffer.OGLBuffer;
import com.blackrook.ogl.object.framebuffer.OGLFrameBuffer;
import com.blackrook.ogl.object.framebuffer.OGLFrameRenderBuffer;
import com.blackrook.ogl.object.list.OGLDisplayList;
import com.blackrook.ogl.object.query.OGLOcclusionQuery;
import com.blackrook.ogl.object.shader.OGLShaderPipelineProgram;
import com.blackrook.ogl.object.shader.OGLShaderProgram;
import com.blackrook.ogl.object.texture.OGLTexture;

/**
 * The system that draws stuff.
 * This is meant to be wrapped in a {@link GLEventListener}, where {@link #display(GLAutoDrawable)}
 * gets called with the passed {@link GLAutoDrawable}.
 * @author Matthew Tropiano
 */
public final class OGLSystem
{
	/** OpenGL graphics context. */
	private OGLGraphics glGraphics;
	/** System listener list. */
	private List<OGLCanvasNode> canvasNodeList;
	/** GLMouse X */
	private int glMouseX;
	/** GLMouse Y */
	private int glMouseY;
	/** GL width */
	private int glWidth;
	/** GL height */
	private int glHeight;
	
	/* ========= Render time variables ======= */
	private long lastFrameNanos;
	private long frameRenderTimeNanos;
	private long renderTimeNanos;
	private int polygonCount;
	/* ======================================= */
	
	/**
	 * Creates a new OGLSystem.
	 */
	public OGLSystem()
	{
		glMouseX = 0;
		glMouseY = 0;
		glWidth = 0;
		glHeight = 0;
		canvasNodeList = new List<OGLCanvasNode>(4);
	}
	
	/**
	 * Displays everything in this system on the provided drawable.
	 */
	public void display(GLAutoDrawable drawable)
	{
		if (glGraphics == null)
			glGraphics = new OGLGraphics(this, drawable);

		long rendertime = 0L;
		int polys = 0;
	
		glGraphics.beginFrame();
		
	    for (int i = 0; i < canvasNodeList.size(); i++)
	    {
	    	OGLCanvasNode node = canvasNodeList.getByIndex(i);
	    	if (node != null && node.isEnabled())
	    	{
	    		node.display(glGraphics);
	    		glGraphics.getError();
	    		rendertime += node.getRenderTimeNanos();
	    		polys += node.getPolygonsRendered();
	    	}
	    }
	    
	    frameRenderTimeNanos = System.nanoTime() - lastFrameNanos;
	    lastFrameNanos = System.nanoTime();
	
	    renderTimeNanos = rendertime;
	    polygonCount = polys;
	    
	    // Clean up abandoned objects.
	    OGLBuffer.destroyUndeleted(glGraphics);
	    OGLDisplayList.destroyUndeleted(glGraphics);
	    OGLFrameBuffer.destroyUndeleted(glGraphics);
	    OGLFrameRenderBuffer.destroyUndeleted(glGraphics);
	    OGLOcclusionQuery.destroyUndeleted(glGraphics);
	    OGLShaderProgram.destroyUndeleted(glGraphics);
	    OGLShaderPipelineProgram.destroyUndeleted(glGraphics);
	    OGLTexture.destroyUndeleted(glGraphics);
	}

	/**
	 * Tells all attached nodes to resize themselves.
	 * @param width the new width.
	 * @param height the new height.
	 */
	public void reshape(int width, int height)
	{
		glWidth = width;
		glHeight = height;
	    for (OGLCanvasNode sl : canvasNodeList)
	    	sl.onCanvasResize(width, height);
	}

	/**
	 * Adds a Canvas Node to this Canvas.
	 */
	public void addNode(OGLCanvasNode node)
	{
		canvasNodeList.add(node);
		node.onCanvasResize(glWidth, glHeight);
	}

	/**
	 * Removes a Canvas Node from this Canvas.
	 */
	public boolean removeNode(OGLCanvasNode node)
	{
		return canvasNodeList.remove(node);
	}

	/**
	 * Returns the length of time it took to render this frame.
	 * This is NOT the same as {@link #getRenderTimeNanos()}, as it takes
	 * time between frames (real time) into consideration. The method
	 * {@link #getFPS()} uses this information.
	 * <p>Results of this call should not be considered accurate until the node 
	 * has had {@link #display(GLAutoDrawable)} called on it twice.
	 */
	public long getFrameRenderTimeNanos()
	{
		return frameRenderTimeNanos;
	}

	/**
	 * Returns the length of time it took to render each individual node
	 * in nanoseconds, accumulated from the visible nodes.
	 * Results of this call should not be considered accurate until the node 
	 * has had {@link #display(GLAutoDrawable)} called on it.
	 */
	public long getRenderTimeNanos()
	{
		return renderTimeNanos;
	}

	/**
	 * Returns the number of polygonal objects rendered in this canvas, 
	 * gathered from the visible nodes.
	 */
	public int getPolygonsRendered()
	{
		return polygonCount;
	}

	/**
	 * Returns the estimated frames per second in this canvas
	 * based on the time to render the visible nodes.
	 */
	public float getFPS()
	{
		double n = (frameRenderTimeNanos / 1000000d);
		return n > 0.0 ? (float)(1000 / n) : 0f;
	}

	public int getMouseX()
	{
		return glMouseX;
	}

	public int getMouseY()
	{
		return glMouseY;
	}

	/**
	 * Returns the view width form last resize.
	 */
	public float getWidth()
	{
		return glWidth;
	}

	/**
	 * Returns the view height form last resize.
	 */
	public float getHeight()
	{
		return glHeight;
	}

	/**
	 * Broadcasts mouse movement to the canvas nodes.
	 */
	public void sendMouseMovement(int changeX, int positionX, int changeY, int positionY)
	{
		glMouseX = positionX;
		glMouseY = positionY;
		
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			listener.glMouseMove(changeX, positionX, changeY, positionY);
		}
	
	}

	/**
	 * Broadcasts the mouse entering this canvas to the canvas nodes. 
	 */
	public void sendMouseEntered()
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled())
				 listener.glMouseEnter();
		}
	}

	/**
	 * Broadcasts the mouse exiting this canvas to the canvas nodes. 
	 */
	public void sendMouseExited()
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled())
				 listener.glMouseExit();
		}
	}

	/**
	 * Broadcasts mouse button presses to the canvas nodes. 
	 * @param mouseButton a mouse button constant in {@link OGLInputConstants}.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendMousePress(int mouseButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glMousePress(mouseButton))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts mouse button releases to the canvas nodes. 
	 * @param mouseButton a mouse button constant in {@link OGLInputConstants}.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendMouseRelease(int mouseButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glMouseRelease(mouseButton))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts mouse wheel movements to the canvas nodes. 
	 * @param units the amount of mouse wheel movement.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendMouseWheel(int units)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glMouseWheel(units))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts key presses to the canvas nodes. 
	 * @param keyCode the key constant to broadcast to reflect the pressed key (see {@link OGLInputConstants}).
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendKeyPress(int keyCode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glKeyPress(keyCode))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts key releases to the canvas nodes. 
	 * @param keyCode the key constant to broadcast to reflect the released key (see {@link OGLInputConstants}).
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendKeyRelease(int keyCode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glKeyRelease(keyCode))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts key typing to the canvas nodes. 
	 * @param keyCode the key constant to broadcast to reflect the typed key (see {@link OGLInputConstants}).
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendKeyTyped(int keyCode)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glKeyTyped(keyCode))
				return true;
		}
		return false;
	}

	/**
	 * Broadcasts a button press from a gamepad to the canvas nodes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button pressed.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendGamepadPress(int gamepadId, int gamepadButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glGamepadPress(gamepadId, gamepadButton))
				return true;
		}
		return false;
	}
	
	/**
	 * Broadcasts a button release from a gamepad to the canvas nodes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadButton the gamepad button released.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendGamepadRelease(int gamepadId, int gamepadButton)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glGamepadRelease(gamepadId, gamepadButton))
				return true;
		}
		return false;
	}
	
	/**
	 * Broadcasts an axis change from a gamepad to the canvas nodes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the gamepad axis that changed.
	 * @param value the gamepad axis value.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendGamepadAxis(int gamepadId, int gamepadAxisId, float value)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glGamepadAxisChange(gamepadId, gamepadAxisId, value))
				return true;
		}
		return false;
	}
	
	/**
	 * Broadcasts an axis tap from a gamepad to the canvas nodes.
	 * @param gamepadId the id of the gamepad.
	 * @param gamepadAxisId the gamepad axis that changed.
	 * @param positive if true, positive value, if false, negative.
	 * @return true if this input was handled by this system, false if not (so that others can, if necessary).
	 */
	public boolean sendGamepadAxisTap(int gamepadId, int gamepadAxisId, boolean positive)
	{
		// goes backwards. last to be drawn is closest to the screen.
		for (int i = canvasNodeList.size()-1; i >= 0; i--)
		{
			OGLCanvasNode listener = canvasNodeList.getByIndex(i);
			if (listener != null && listener.isEnabled() && listener.glGamepadAxisTap(gamepadId, gamepadAxisId, positive))
				return true;
		}
		return false;
	}
	
}
