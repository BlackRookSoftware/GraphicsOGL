/*******************************************************************************
 * Copyright (c) 2014, 2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * Contributors:
 *     Matt Tropiano - initial API and implementation
 *******************************************************************************/
package com.blackrook.ogl;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

/**
 * Extension of {@link GLCapabilities} that uses the default device for
 * its base profile.
 * @author Matthew Tropiano
 */
public class OGLProfile extends GLCapabilities
{

	public OGLProfile()
	{
		super(GLProfile.getDefault(GLProfile.getDefaultDevice()));
	}

}
