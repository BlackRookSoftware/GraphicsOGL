/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.node;

import com.blackrook.ogl.OGLGraphics;

/**
 * A canvas node that performs a one-time set of graphics commands.
 * Can be reset so that the one-time function can be performed again.
 * @author Matthew Tropiano
 */
public abstract class OGLTriggeredNode extends OGLCanvasNodeAdapter
{
	/** If true, the triggered function is called. */
	protected boolean trigger;
	
	/**
	 * Creates a new triggered node where the trigger starts set.
	 * In other words, this will call the triggered function once,
	 * and not again until triggered.
	 */
	public OGLTriggeredNode()
	{
		this(true);
	}

	/**
	 * Creates a new triggered node where the trigger starts
	 * set or unset, according to the programmer.
	 * @param triggerFlag the initial state of the trigger.
	 */
	public OGLTriggeredNode(boolean triggerFlag)
	{
		trigger = triggerFlag;
	}

	@Override
	public void display(OGLGraphics g)
	{
		if (trigger)
		{
			trigger = false;
			doTriggeredFunction(g);
		}
	}

	/**
	 * Sets the trigger on this node.
	 */
	public void setTrigger()
	{
		trigger = true;
	}
	
	/**
	 * This is the method called by display for when the triggered
	 * method needs to run.
	 * @param g the OGLGraphics context. 
	 */
	public abstract void doTriggeredFunction(OGLGraphics g);

}
