/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.input;

import com.blackrook.ogl.OGLSystem;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

/**
 * An input adapter that listens to NEWT events to pass along to the OGL framework.
 * This does NOT submit Gamepad events, since there aren't any listeners for such actions
 * in NEWT.
 * @author Matthew Tropiano
 */
public class NEWTInputAdapter implements MouseListener, KeyListener, OGLInputConstants
{
	/** Reference to target system. */
	private OGLSystem system;
	/** Previous mouse coordinates, X. */
	private int prevMouseX;
	/** Previous mouse coordinates, Y. */
	private int prevMouseY;

	/**
	 * Creates a new NEWTInputAdapter.
	 */
	public NEWTInputAdapter(OGLSystem system)
	{
		this.system = system;
		prevMouseX = -1;
		prevMouseY = -1;
	}
	
	// perform mouse move crap.
	private void move(int x, int y)
	{
		if (prevMouseX < 0)
			prevMouseX = x;
		if (prevMouseY < 0)
			prevMouseY = y;
		
		int dx = x - prevMouseX;
		int dy = y - prevMouseY;
		
		prevMouseX = x;
		prevMouseY = y;
		
		system.sendMouseMovement(dx, x, dy, y);
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		system.sendMouseEntered();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		system.sendMouseExited();
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		move(event.getX(), event.getY());
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		move(event.getX(), event.getY());
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		int b = event.getButton();
		switch (b)
		{
			case MouseEvent.BUTTON1:
				system.sendMousePress(MOUSE_LEFT);
				break;
			case MouseEvent.BUTTON2:
				system.sendMousePress(MOUSE_CENTER);
				break;
			case MouseEvent.BUTTON3:
				system.sendMousePress(MOUSE_RIGHT);
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		int b = event.getButton();
		switch (b)
		{
			case MouseEvent.BUTTON1:
				system.sendMouseRelease(MOUSE_LEFT);
				break;
			case MouseEvent.BUTTON2:
				system.sendMouseRelease(MOUSE_CENTER);
				break;
			case MouseEvent.BUTTON3:
				system.sendMouseRelease(MOUSE_RIGHT);
				break;
			case MouseEvent.BUTTON4:
				system.sendMouseRelease(MOUSE_BACK);
				break;
			case MouseEvent.BUTTON5:
				system.sendMouseRelease(MOUSE_FORWARD);
				break;
			case MouseEvent.BUTTON6:
				system.sendMouseRelease(MOUSE_BUTTON6);
				break;
			case MouseEvent.BUTTON7:
				system.sendMouseRelease(MOUSE_BUTTON7);
				break;
			case MouseEvent.BUTTON8:
				system.sendMouseRelease(MOUSE_BUTTON8);
				break;
			case MouseEvent.BUTTON9:
				system.sendMouseRelease(MOUSE_BUTTON9);
				break;
			default:
				system.sendMouseRelease(MOUSE_UNDEFINED);
				break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseEvent event)
	{
		system.sendMouseWheel(event.getWheelRotation());
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		system.sendKeyPress(event.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		system.sendKeyRelease(event.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		system.sendKeyTyped(event.getKeyCode());
	}

}
