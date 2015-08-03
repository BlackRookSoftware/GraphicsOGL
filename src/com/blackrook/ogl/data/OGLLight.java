/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.data;


/**
 * An object that represents a light source.
 * @author Matthew Tropiano
 */
public class OGLLight
{
	/** The light's position in the OpenGL world. */
	private float[] position;

	private float constantAttenuation;
	/** This Light's linear attenuation. */
	private float linearAttenuation;
	/** This Light's quadratic attenuation. */
	private float quadraticAttenuation;
	
	/** Light ambient color. */
	private OGLColor ambientColor;
	/** Light diffuse color. */
	private OGLColor diffuseColor;
	/** Light specular color. */
	private OGLColor specularColor;
	
	/**
	 * Creates a new Light.
	 */
	public OGLLight()
	{
		position = new float[4];
		setPosition(0,0,0,0);
		setAmbientColor(OGLColor.BLACK);
		setDiffuseColor(OGLColor.WHITE);
		setSpecularColor(OGLColor.BLACK);
		setAttenuation(1, 0, 0);
	}
	
	public final float[] getPosition()
	{
		return position;
	}

	/** Gets the light's eye position in the OpenGL world, X coordinate. */
	public final float getXPosition()
	{
		return position[0];
	}
	
	/** Gets the light's eye position in the OpenGL world, Y coordinate. */
	public final float getYPosition()
	{
		return position[1];
	}
	
	/** Gets the light's eye position in the OpenGL world, Z coordinate. */
	public final float getZPosition()
	{
		return position[2];
	}
	
	/** 
	 * Get's the light's position in the OpenGL world, W coordinate.
	 * 0 if ambient (direction), 1 if source.
	 */
	public final float getWPosition()
	{
		return position[3];
	}
	
	/** Gets this Light's linear attenuation. */
	public final float getLinearAttenuation()
	{
		return linearAttenuation;
	}
	
	/** Gets this Light's quadratic attenuation. */
	public final float getQuadraticAttenuation()
	{
		return quadraticAttenuation;
	}
	
	/** Gets this Light's constant attenuation. */
	public final float getConstantAttenuation()
	{
		return constantAttenuation;
	}

	public final OGLColor getAmbientColor()
	{
		return ambientColor;
	}

	public final OGLColor getDiffuseColor()
	{
		return diffuseColor;
	}
	
	public final OGLColor getSpecularColor()
	{
		return specularColor;
	}
	
	public final void setPosition(float x, float y, float z, float w)
	{
		setXPosition(x);
		setYPosition(y);
		setZPosition(z);
		setWPosition(w);
	}
	
	public final void setXPosition(float x)
	{
		position[0] = x;
	}
	
	public final void setYPosition(float y)
	{
		position[1] = y;
	}
	
	public final void setZPosition(float z)
	{
		position[2] = z;
	}
	
	public final void setWPosition(float w)
	{
		position[3] = w;
	}
	
	public final void setAttenuation(float constant, float linear, float quadratic)
	{
		setConstantAttenuation(constant);
		setLinearAttenuation(linear);
		setQuadraticAttenuation(quadratic);
	}
	
	public final void setLinearAttenuation(float linearAttenuation)
	{
		this.linearAttenuation = linearAttenuation;
	}
	
	public final void setQuadraticAttenuation(float quadraticAttenuation)
	{
		this.quadraticAttenuation = quadraticAttenuation;
	}
	
	public final void setConstantAttenuation(float constantAttenuation)
	{
		this.constantAttenuation = constantAttenuation;
	}

	public final void setAmbientColor(OGLColor ambientColor)
	{
		this.ambientColor = ambientColor;
	}

	public final void setDiffuseColor(OGLColor diffuseColor)
	{
		this.diffuseColor = diffuseColor;
	}
	
	public final void setSpecularColor(OGLColor specularColor)
	{
		this.specularColor = specularColor;
	}
	
}
