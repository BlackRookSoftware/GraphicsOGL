/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.blackrook.commons.Ticker;
import com.blackrook.ogl.OGLDisplay;
import com.blackrook.ogl.OGLProfile;
import com.blackrook.ogl.OGLSystem;
import com.blackrook.ogl.input.SwingInputAdapter;

/**
 * The main rendering system for this graphics thing.
 * If this object is spawned, you cannot spawn another.
 * @author Matthew Tropiano
 */
public class OGLSwingWindow extends JFrame implements OGLDisplay, GLEventListener
{
	private static final long serialVersionUID = -1266676846855780095L;
	
	/** Current Graphics Environment. */
	private static GraphicsEnvironment graphEnv;
	/** Current Graphics Device. */
	private static GraphicsDevice graphDev;
	
	static
	{
		graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		graphDev = graphEnv.getDefaultScreenDevice();
	}
	
	protected WindowAdapter QUIT_ADAPTER = new WindowAdapter()
	{
		public void windowClosed(WindowEvent e)
		{
			if (quitOnClose)
				System.exit(0);
		}
	
		public void windowClosing(WindowEvent e)
		{
			if (quitOnClose)
				System.exit(0);
		}
	};
	
	/** GLCanvas canvas. */
	private GLCanvas canvas;
	/** OGLSystem. */
	private OGLSystem system;
	
	/** Are we in full screen mode? */
	private boolean isFullscreen;
	/** Should the JVM quit if the window is closed? */
	private boolean quitOnClose;

	/** Redraw ticker. */
	private RedrawTicker redrawTicker;
	
	/**
	 * Creates a new OGLSwingWindow using default dimensions (640x480).
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 */
	public OGLSwingWindow(OGLProfile caps, String frameName)
	{
		this(caps, frameName, 640, 480);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName	name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLSwingWindow(OGLProfile caps, String frameName, int width, int height)
	{
		this(caps, frameName, null, width, height);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 */
	public OGLSwingWindow(OGLProfile caps, String frameName, int width, int height, boolean fullScreen)
	{
		this(caps, frameName, null, width, height, fullScreen, true);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLSwingWindow(OGLProfile caps, String frameName, Image frameIcon, int width, int height)
	{
		this(caps, frameName, null, width, height, false, true);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param caps OGLProfile of the canvas instance.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 * @param startVisible does this start visible?
	 */
	public OGLSwingWindow(OGLProfile caps, String frameName, Image frameIcon, int width, int height, boolean fullScreen, boolean startVisible)
	{
		super(frameName);
		setIconImage(frameIcon);
		
		setUndecorated(fullScreen);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		addWindowListener(QUIT_ADAPTER);

		system = new OGLSystem();
		canvas = new GLCanvas(caps);
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.addGLEventListener(this);
		canvas.setFocusTraversalKeysEnabled(false);

		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		
		// if "set fullscreen".....
		if (fullScreen && graphDev.isFullScreenSupported())
		{
			try{
				graphDev.setFullScreenWindow(this);
				graphDev.setDisplayMode(new DisplayMode(width, height, 
					graphDev.getDisplayMode().getBitDepth(), 
					graphDev.getDisplayMode().getRefreshRate()));
				isFullscreen = true;
			} catch (Exception e) {
				e.printStackTrace();
				graphDev.setFullScreenWindow(null);
				isFullscreen = false;
			}
		}

		setVisible(startVisible);
		canvas.requestFocus();
		addInputListener(system);
		pack();
	}
	
	/**
	 * Creates a new OGLSwingWindow using default dimensions (640x480).
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 */
	public OGLSwingWindow(String frameName)
	{
		this(frameName, 640, 480);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName	name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLSwingWindow(String frameName, int width, int height)
	{
		this(frameName, null, width, height);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 */
	public OGLSwingWindow(String frameName, int width, int height, boolean fullScreen)
	{
		this(frameName, null, width, height, fullScreen, true);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 */
	public OGLSwingWindow(String frameName, Image frameIcon, int width, int height)
	{
		this(frameName, null, width, height, false, true);
	}

	/**
	 * Creates a new OGLSwingWindow.
	 * The frame does NOT close the program by default close action!
	 * The canvas is bound to one GLEventListener, namely, this class.
	 * @param frameName name of the window's frame.
	 * @param frameIcon the icon for this window.
	 * @param width	width of the canvas in the window.	
	 * @param height height of the canvas in the window.	
	 * @param fullScreen is this gonna be set in full screen mode?
	 * @param startVisible does this start visible?
	 */
	public OGLSwingWindow(String frameName, Image frameIcon, int width, int height, boolean fullScreen, boolean startVisible)
	{
		this(new OGLProfile(), frameName, frameIcon, width, height, fullScreen, true);
	}

	/**
	 * Adds an input listener to this window that pipes input to the OGLSystem.
	 * @param system the system listener.
	 */
	protected void addInputListener(OGLSystem system)
	{
		SwingInputAdapter adapter = new SwingInputAdapter(system);
		canvas.addKeyListener(adapter);
		canvas.addMouseListener(adapter);
		canvas.addMouseWheelListener(adapter);
		canvas.addMouseMotionListener(adapter);
	}
	
	@Override
	public int getCanvasAbsoluteX()
	{
		if (canvas == null)
			return -1;

		int out = 0;
		Component c = canvas;
		while (c != null)
		{
			out += c.getBounds().x;
			c = c.getParent();
		}
		
		return out;
	}
	
	@Override
	public int getCanvasAbsoluteY()
	{
		if (canvas == null)
			return -1;

		int out = 0;
		Component c = canvas;
		while (c != null)
		{
			out += c.getBounds().y;
			c = c.getParent();
		}
		
		return out;
	}
	
	/**
	 * Calls {@link GLCanvas#display()} on the encapsulated canvas.
	 */
	public void display()
	{
		canvas.display();
	}
	
	@Override
	public OGLSystem getSystem()
	{
		return system;
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
	public void init(GLAutoDrawable drawable)
	{
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		system.reshape(width, height);
	}

	@Override
	public void dispose()
	{
		destroy();
	}
	
	@Override
	public void destroy()
	{
		canvas.destroy();
		canvas = null;
		super.dispose();
	}
	
	/**
	 * Is this in fullscreen mode?
	 */
	public boolean isFullscreen()
	{
		return isFullscreen;
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
			super("OGLSwingWindow RedrawTicker", updatesPerSecond);
		}

		@Override
		public void doTick(long tick)
		{
			display();
		}
	}
	
}


