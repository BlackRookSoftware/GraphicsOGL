/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import com.blackrook.ogl.mesh.MeshView;

/**
 * A special type of OGLDrawable that offers up a method for extracting mesh data.
 * @author Matthew Tropiano 
 */
public interface OGLMesh
{
	/**
	 * Returns a view for rendering this model.
	 * This may create an entirely new view, so you are better off
	 * calling this once and storing the reference someplace if your mesh is static. 
	 */
	public MeshView getView();

}
