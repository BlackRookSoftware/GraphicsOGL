/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.input;

import com.blackrook.ogl.OGLInputAdapter;
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
public class NEWTInputAdapter extends OGLInputAdapter implements MouseListener, KeyListener, OGLInputConstants
{
	/** Previous mouse coordinates, X. */
	private int prevMouseX;
	/** Previous mouse coordinates, Y. */
	private int prevMouseY;

	/**
	 * Creates a new NEWTInputAdapter.
	 */
	public NEWTInputAdapter(OGLSystem system)
	{
		super(system);
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
		
		fireMouseMove(dx, x, dy, y);
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
				fireMousePress(MOUSE_LEFT);
				break;
			case MouseEvent.BUTTON2:
				fireMousePress(MOUSE_CENTER);
				break;
			case MouseEvent.BUTTON3:
				fireMousePress(MOUSE_RIGHT);
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
				fireMouseRelease(MOUSE_LEFT);
				break;
			case MouseEvent.BUTTON2:
				fireMouseRelease(MOUSE_CENTER);
				break;
			case MouseEvent.BUTTON3:
				fireMouseRelease(MOUSE_RIGHT);
				break;
			case MouseEvent.BUTTON4:
				fireMouseRelease(MOUSE_BACK);
				break;
			case MouseEvent.BUTTON5:
				fireMouseRelease(MOUSE_FORWARD);
				break;
			case MouseEvent.BUTTON6:
				fireMouseRelease(MouseEvent.BUTTON6);
				break;
			case MouseEvent.BUTTON7:
				fireMouseRelease(MouseEvent.BUTTON7);
				break;
			case MouseEvent.BUTTON8:
				fireMouseRelease(MouseEvent.BUTTON8);
				break;
			case MouseEvent.BUTTON9:
				fireMouseRelease(MouseEvent.BUTTON9);
				break;
			default:
				fireMouseRelease(MOUSE_UNDEFINED);
				break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseEvent event)
	{
		fireMouseWheel(event.getWheelRotation());
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		fireMouseEntered();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		fireMouseExited();
	}

	@Override
	public void keyPressed(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		fireKeyPress(event.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		fireKeyRelease(event.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent event)
	{
		// The input constants are equivalent to KEY_*, so, don't convert.
		fireKeyTyped(event.getKeyCode());
	}

}
