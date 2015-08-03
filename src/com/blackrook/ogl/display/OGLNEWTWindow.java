/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.display;

import java.awt.Image;

import javax.media.nativewindow.util.Point;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.blackrook.commons.Common;
import com.blackrook.commons.Ticker;
import com.blackrook.ogl.OGLDisplay;
import com.blackrook.ogl.OGLProfile;
import com.blackrook.ogl.OGLSystem;
import com.blackrook.ogl.input.NEWTInputAdapter;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

/**
 * Creates a window that utilizes NEWT.
 * @author Matthew Tropiano
 */
public class OGLNEWTWindow implements OGLDisplay, GLEventListener
{
	/** Quit adapter. */
	protected WindowAdapter QUIT_ADAPTER = new WindowAdapter()
	{
		@Override
		public void windowDestroyed(WindowEvent e)
		{
			if (quitOnClose)
				System.exit(0);
		}
		
		@Override
		public void windowDestroyNotify(WindowEvent e)
		{
			if (quitOnClose)
				System.exit(0);
		}
		
	};

	/** OpenGL System. */
	private GLWindow window;
	/** Window point. */
	private Point windowPoint;
	/** OpenGL System. */
	private OGLSystem system;
	/** Should the JVM quit if the window is closed? */
	private boolean quitOnClose;
	/** Redraw ticker. */
	private RedrawTicker redrawTicker;

	/**
	 * Creates a new OGLNEWTWindow using default dimensions (640x480).
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 */
	public OGLNEWTWindow(OGLProfile caps, String frameName)
	{
		this(caps, frameName, 640, 480);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName	name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLNEWTWindow(OGLProfile caps, String frameName, int width, int height)
	{
		this(caps, frameName, null, width, height);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 */
	public OGLNEWTWindow(OGLProfile caps, String frameName, int width, int height, boolean fullScreen)
	{
		this(caps, frameName, null, width, height, fullScreen, true);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLNEWTWindow(OGLProfile caps, String frameName, Image frameIcon, int width, int height)
	{
		this(caps, frameName, null, width, height, false, true);
	}

	/**
	 * Creates a new OGLNEWTWindow.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 * @param startVisible does this start visible?
	 */
	public OGLNEWTWindow(OGLProfile caps, String frameName, Image frameIcon, int width, int height, boolean fullScreen, boolean startVisible)
	{
		windowPoint = new Point();
		system = new OGLSystem();
		window = GLWindow.create(caps);
		window.addGLEventListener(this);
		window.addWindowListener(QUIT_ADAPTER);
		
		window.setTitle(frameName);
		window.setSize(width, height);
		window.setFullscreen(fullScreen);
		
		NEWTInputAdapter adapter = new NEWTInputAdapter(system);
		window.addKeyListener(adapter);
		window.addMouseListener(adapter);

		window.setVisible(startVisible);
		
		// Create a non-daemon thread that sticks around until the window is closed
		// in order to keep it open.
		new Thread() {
			public void run() {
				while (window.isVisible())
					Common.sleep(100);
			};
		}.start();
	}
	
	/**
	 * Creates a new OGLNEWTWindow using default dimensions (640x480).
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 */
	public OGLNEWTWindow(String frameName)
	{
		this(frameName, null, 640, 480);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName	name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLNEWTWindow(String frameName, int width, int height)
	{
		this(frameName, null, width, height);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 */
	public OGLNEWTWindow(String frameName, int width, int height, boolean fullScreen)
	{
		this(frameName, null, width, height, fullScreen, true);
	}

	/**
	 * Creates a wonderful new OGLNEWTWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLNEWTWindow(String frameName, Image frameIcon, int width, int height)
	{
		this(frameName, null, width, height, false, true);
	}

	/**
	 * Creates a new OGLNEWTWindow.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 * @param startVisible does this start visible?
	 */
	public OGLNEWTWindow(String frameName, Image frameIcon, int width, int height, boolean fullScreen, boolean startVisible)
	{
		this(new OGLProfile(), frameName, frameIcon, width, height, fullScreen, startVisible);
	}

	@Override
	public OGLSystem getSystem()
	{
		return system;
	}

	@Override
	public void display()
	{
		window.display();
	}

	@Override
	public void init(GLAutoDrawable drawable)
	{
	}

	@Override
	public void display(GLAutoDrawable drawable)
	{
		system.display(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable)
	{
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		system.reshape(width, height);
	}

	/**
	 * Self destruct sequence activated. 
	 * This destroys the OGLNEWTWindow.
	 */
	public void destroy()
	{
		window.destroy();
	}

	@Override
	public int getCanvasAbsoluteX()
	{
		if (window == null)
			return -1;
		
		return window.getLocationOnScreen(windowPoint).getX();
	}
	
	@Override
	public int getCanvasAbsoluteY()
	{
		if (window == null)
			return -1;
		
		return window.getLocationOnScreen(windowPoint).getY();
	}
	
	/**
	 * Is this in fullscreen mode?
	 */
	public boolean isFullscreen()
	{
		return window.isFullscreen();
	}

	/**
	 * Returns if this window is visible.
	 */
	public boolean isVisible()
	{
		return window.isVisible();
	}
	
	/**
	 * Sets if this window is visible or not.
	 * Where it is NOT visible, it will not be redrawn.
	 * @param visible
	 */
	public void setVisible(boolean visible)
	{
		window.setVisible(visible);
	}

	/** Should the JVM quit after the window is closed? */
	public void setQuitOnClose(boolean val)
	{
		quitOnClose = val;
	}

	/** Does this kill the JVM if it is closed? */
	public boolean quitsOnClose()
	{
		return quitOnClose;
	}

	/**
	 * Returns the estimated frames per second in this canvas
	 * based on the time to render the visible nodes.
	 */
	public float getFPS()
	{
		double n = (system.getFrameRenderTimeNanos() / 1000000d);
		return n > 0.0 ? (float)(1000 / n) : 0f;
	}

	/**
	 * Starts an auto-redraw thread that attempts to render at a steady
	 * framerate by calling {@link #display()} at a specific rate. If already running,
	 * changes the framerate.
	 * If set to 0, this calls {@link #display()} as many times as it can.
	 * @param fps the target rate in frames per second, or 0 for full bore.
	 * @see #stopAutoRedraw()
	 */
	public void startAutoRedraw(int fps)
	{
		if (redrawTicker == null)
		{
			redrawTicker = new RedrawTicker(fps);
			redrawTicker.start();
		}
		else
			redrawTicker.setUpdatesPerSecond(fps);
	}

	/**
	 * Stops any auto-redraw started by {@link #startAutoRedraw(int)}.
	 */
	public void stopAutoRedraw()
	{
		redrawTicker.stop();
		redrawTicker = null;
	}

	/** Local redraw ticker. */
	private class RedrawTicker extends Ticker
	{
		public RedrawTicker(int updatesPerSecond)
		{
			super("OGLNEWTWindow RedrawTicker", updatesPerSecond);
		}
	
		@Override
		public void doTick(long tick)
		{
			display();
		}
	}
}
