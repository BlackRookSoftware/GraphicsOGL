package com.blackrook.ogl.enums;

import com.jogamp.opengl.GL3;

public enum SpriteOrigin 
{
	UPPER_LEFT(GL3.GL_UPPER_LEFT),
	LOWER_LEFT(GL3.GL_LOWER_LEFT);
	
	public final int glValue;
	SpriteOrigin(int glval) 
		{glValue = glval;}

}
