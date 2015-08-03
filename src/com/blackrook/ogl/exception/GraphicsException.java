/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.exception;

/**
 * This is commonly thrown when a objects couldn't be allocated,
 * or a sequence of operations raise an error.
 * @author Matthew Tropiano
 */
public class GraphicsException extends RuntimeException {

	private static final long serialVersionUID = -4698043866886434891L;

	public GraphicsException()
	{
		super("A graphics exception has occurred.");
	}

	public GraphicsException(String message)
	{
		super(message);
	}
}
