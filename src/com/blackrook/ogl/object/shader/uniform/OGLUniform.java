/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader.uniform;

import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.object.shader.OGLShaderProgram;

/**
 * A shader Uniform class for holding a uniform name plus associated data.
 * @author Matthew Tropiano
 */
public abstract class OGLUniform
{
	/** Uniform name. */
	private String name;
	/** Boolean flag for whether this uniform has changed or not. */
	private boolean changed;

	/**
	 * Creates a new shader program uniform.
	 * @param name uniform name.
	 */
	public OGLUniform(String name)
	{
		this.name = name;
		this.changed = true;
	}
	
	/**
	 * Applies the value of this uniform to a shader.
	 * @param g			the OGLGraphics context.
	 * @param shader	the shader program to apply this to.
	 */
	public void set(OGLGraphics g, OGLShaderProgram shader)
	{
		apply(g, shader);
		setChanged(false);
	}

	/**
	 * Applies the value of this uniform to a shader.
	 * @param g			the OGLGraphics context.
	 * @param shader	the shader program to apply this to.
	 */
	protected abstract void apply(OGLGraphics g, OGLShaderProgram shader);

	/**
	 * @return the name of this uniform.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Convenience function for finding the uniform's program location.
	 */
	protected int getUniformLocation(OGLGraphics g, OGLShaderProgram shader)
	{
		return g.getGL().glGetUniformLocationARB(shader.getGLId(), name);
	}
	
	/**
	 * Sets if this value has changed, so that hasChanged() returns
	 * true or false. After application to the bound shader, the flag
	 * should be set to false, using this. 
	 */
	protected void setChanged(boolean changed)
	{
		this.changed = changed;
	}
	
	/**
	 * Returns true if the value of the uniform has changed
	 * and should be updated at shader binding, false otherwise.
	 */
	public boolean hasChanged()
	{
		return changed;
	}
	
}
