/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.data;

import com.blackrook.commons.math.RMath;

/**
 * Contains a set of values that describe a color.
 * @author Matthew Tropiano
 */
public class OGLColor
{
    public static final OGLColor 
    // color hues/components.

    // grays.
    WHITE = 			new OGLColor(1.0f, 1.0f, 1.0f, 1.0f, true),
    LIGHT_GRAY = 		new OGLColor(0.75f, 0.75f, 0.75f, 1.0f, true),
    GRAY = 				new OGLColor(0.5f, 0.5f, 0.5f, 1.0f, true),
    DARK_GRAY = 		new OGLColor(0.25f, 0.25f, 0.25f, 1.0f, true),
    BLACK = 			new OGLColor(0.0f, 0.0f, 0.0f, 1.0f, true),
    
    // primaries, secondaries, tertiaries.
    RED = 				new OGLColor(1.0f, 0.0f, 0.0f, 1.0f, true),
    GREEN = 			new OGLColor(0.0f, 1.0f, 0.0f, 1.0f, true),
    BLUE = 				new OGLColor(0.0f, 0.0f, 1.0f, 1.0f, true),
    
    YELLOW = 			new OGLColor(1.0f, 1.0f, 0.0f, 1.0f, true),
    CYAN = 				new OGLColor(0.0f, 1.0f, 1.0f, 1.0f, true),
    MAGENTA = 			new OGLColor(1.0f, 0.0f, 1.0f, 1.0f, true),
    
    ORANGE = 			new OGLColor(1.0f, 0.5f, 0.0f, 1.0f, true),
    CHARTREUSE = 		new OGLColor(0.5f, 1.0f, 0.0f, 1.0f, true),
    TEAL = 				new OGLColor(0.0f, 1.0f, 0.5f, 1.0f, true),
    AQUAMARINE = 		new OGLColor(0.0f, 0.5f, 1.0f, 1.0f, true),
    VIOLET = 			new OGLColor(0.5f, 0.0f, 1.0f, 1.0f, true),
    FUSCHIA = 			new OGLColor(1.0f, 0.0f, 0.5f, 1.0f, true),

    // other colors
    GARNET = 			new OGLColor(0.5f, 0.0f, 0.0f, 1.0f, true),
    AMBER = 			new OGLColor(0.5f, 0.5f, 0.0f, 1.0f, true),
    PINE = 				new OGLColor(0.0f, 0.5f, 0.0f, 1.0f, true),
    SEA_GREEN = 		new OGLColor(0.0f, 0.5f, 0.5f, 1.0f, true),
    NAVY = 				new OGLColor(0.0f, 0.0f, 0.5f, 1.0f, true),
    ROYAL_PURPLE =		new OGLColor(0.5f, 0.0f, 0.5f, 1.0f, true),
    
    LUMINANCE_BIAS =	new OGLColor(0.3f, 0.59f, 0.11f, 1.0f, true);
	
    /** Flagged as read-only? */
    private boolean readonly;
    
	/** The red component. */
	private float red;
	/** The green component. */
	private float green;
	/** The blue component. */
	private float blue;
	/** The alpha component, commonly used for blending operations. */
	private float alpha;
	
	/**
	 * Makes a new blank Color (RGBA (0,0,0,0)).
	 */
	public OGLColor()
	{
		this(0,0,0,0);
	}
	
	/**
	 * Makes a new color from a 32-bit ARGB integer (like from a BufferedImage).
	 */
	public OGLColor(int argb)
	{
		this.readonly = false;
		set(argb);
	}
	
	/**
	 * Makes a new color from channel components, with the alpha value as 1.
	 * @param red	the red component (0 to 1).
	 * @param green	the green component (0 to 1).
	 * @param blue	the blue component (0 to 1).
	 */
	public OGLColor(float red, float green, float blue)
	{
		this(red,green,blue,1);
	}
	
	/**
	 * Makes a new color from channel components.
	 * @param red	the red component (0 to 1).
	 * @param green	the green component (0 to 1).
	 * @param blue	the blue component (0 to 1).
	 * @param alpha	the alpha component (0 to 1).
	 */
	public OGLColor(float red, float green, float blue, float alpha)
	{
		set(red, green, blue, alpha);
	}

	/**
	 * Makes a new color from channel components.
	 * @param red	the red component (0 to 1).
	 * @param green	the green component (0 to 1).
	 * @param blue	the blue component (0 to 1).
	 * @param alpha	the alpha component (0 to 1).
	 */
	private OGLColor(float red, float green, float blue, float alpha, boolean readonly)
	{
		this(red, green, blue, alpha);
		this.readonly = readonly;
	}

	/**
	 * Makes a new color from an existing color.
	 * @param c	the color to use.
	 */
	public OGLColor(OGLColor c)
	{
		set(c.red, c.green, c.blue, c.alpha);
	}

	/** Gets the red component. */
	public float getRed()
	{
		return red;
	}

	/** Gets the green component. */
	public float getGreen()
	{
		return green;
	}

	/** Gets the blue component. */
	public float getBlue()
	{
		return blue;
	}

	/** Gets the alpha component, commonly used for blending operations. */
	public float getAlpha()
	{
		return alpha;
	}

	/**
	 * Sets this color's info using another color.
	 * @param c	the color to use.
	 */
	public void set(OGLColor c)
	{
		set(c.red, c.green, c.blue, c.alpha);
	}

	/**
	 * Sets this color's info using another color.
	 * @param red	the red component (0 to 1).
	 * @param green	the green component (0 to 1).
	 * @param blue	the blue component (0 to 1).
	 * @param alpha	the alpha component (0 to 1).
	 */
	public void set(float red, float green, float blue, float alpha)
	{
		setRed(red);
		setGreen(green);
		setBlue(blue);
		setAlpha(alpha);
	}

	/**
	 * Sets this color's info using another color.
	 * @param argb	the ARGB formatted 32-bit color value.
	 */
	public void set(int argb)
	{
		setRed(((argb >> 16) & 0x0ff) / 255f);
		setGreen(((argb >> 8) & 0x0ff) / 255f);
		setBlue(((argb >> 0) & 0x0ff) / 255f);
		setAlpha(((argb >> 24) & 0x0ff) / 255f);
	}

	/** Sets the red component. */
	public void setRed(float red)
	{
		if (readonly)
			throw new IllegalStateException("You cannot modify this color.");
		this.red = red;
	}

	/** Sets the green component. */
	public void setGreen(float green)
	{
		if (readonly)
			throw new IllegalStateException("You cannot modify this color.");
		this.green = green;
	}

	/** Sets the blue component. */
	public void setBlue(float blue)
	{
		if (readonly)
			throw new IllegalStateException("You cannot modify this color.");
		this.blue = blue;
	}

	/** Sets the alpha component, commonly used for blending operations. */
	public void setAlpha(float alpha)
	{
		if (readonly)
			throw new IllegalStateException("You cannot modify this color.");
		this.alpha = alpha;
	}

	/**
	 * Returns this color's RGBA info into a float array.
	 */
	public void getRGBA(float[] out)
	{
		out[0] = red;
		out[1] = green;
		out[2] = blue;
		out[3] = alpha;
	}
	
	/**
	 * Returns this color as an ARGB integer value.
	 */
	public int getARGB()
	{
		return getARGB(red, green, blue, alpha);
	}
	
	/**
	 * Multiplies all channels in this color by a scalar. 
	 */
	public void scale(float scalar)
	{
		scale(this,scalar,this);
	}
	
	/**
	 * Adds a color's components to this one.
	 * This changes this color's values.
	 */
	public void add(OGLColor c)
	{
		add(this,c,this);
	}
	
	/**
	 * Subtracts a color's components from this one.
	 * This changes this color's values.
	 */
	public void subtract(OGLColor c)
	{
		subtract(this,c,this);
	}
	
	/**
	 * Multiplies a color's components to this one.
	 * This changes this color's values.
	 */
	public void multiply(OGLColor c)
	{
		multiply(this,c,this);
	}
	
	/**
	 * Blends a color's component's to this one,
	 * using the incoming color's alpha channel as a
	 * blending factor.
	 * This changes this color's values.
	 */
	public void alphaBlend(OGLColor c)
	{
		alphaBlend(this,c,this);
	}
	
	/**
	 * Clamps this color's channel values into the range 0 to 1.
	 */
	public void clamp()
	{
		if (readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		red = RMath.clampValue(red, 0, 1);
		green = RMath.clampValue(green, 0, 1);
		blue = RMath.clampValue(blue, 0, 1);
		alpha = RMath.clampValue(alpha, 0, 1);
	}
	
	/**
	 * Converts RGBA floats to an ARGB integer.
	 * @param red	the red component value (0 to 1).
	 * @param green	the green component value (0 to 1).
	 * @param blue	the blue component value (0 to 1).
	 * @param alpha	the alpha component value (0 to 1).
	 */
	public static int getARGB(float red, float green, float blue, float alpha)
	{
		int out = 0;
		out |= ((int)(RMath.clampValue(blue,0,1) * 255)) << 0; 
		out |= ((int)(RMath.clampValue(green,0,1) * 255)) << 8; 
		out |= ((int)(RMath.clampValue(red,0,1) * 255)) << 16; 
		out |= ((int)(RMath.clampValue(alpha,0,1) * 255)) << 24;
		return out;
	}
	
	/**
	 * Scales a color's channel values by a scalar.
	 * This changes "out's" values.
	 */
	public static void scale(OGLColor a, float scalar, OGLColor out)
	{	
		if (out.readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		out.red = a.red * scalar;
		out.green = a.green * scalar;
		out.blue = a.blue * scalar;
		out.alpha = a.alpha * scalar;
	}

	/**
	 * Adds a color's components to another.
	 * This changes "out's" values.
	 */
	public static void add(OGLColor a, OGLColor b, OGLColor out)
	{
		if (out.readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		out.red = a.red + b.red;
		out.green = a.green + b.green;
		out.blue = a.blue + b.blue;
		out.alpha = a.alpha + b.alpha;
	}

	/**
	 * Subtracts a color's components from another.
	 * This changes "out's" values.
	 * Subtracts b from a.
	 */
	public static void subtract(OGLColor a, OGLColor b, OGLColor out)
	{
		if (out.readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		out.red = a.red - b.red;
		out.green = a.green - b.green;
		out.blue = a.blue - b.blue;
		out.alpha = a.alpha - b.alpha;
	}

	/**
	 * Multiplies a color's components to another.
	 * This changes "out's" values.
	 */
	public static void multiply(OGLColor a, OGLColor b, OGLColor out)
	{
		if (out.readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		out.red = a.red * b.red;
		out.green = a.green * b.green;
		out.blue = a.blue * b.blue;
		out.alpha = a.alpha * b.alpha;
	}
	
	/**
	 * Blends two colors using alpha blending.
	 * This changes "out's" values.
	 */
	public static void alphaBlend(OGLColor a, OGLColor b, OGLColor out)
	{
		if (out.readonly)
			throw new IllegalStateException("You cannot modify the output color.");

		float oma = 1 - b.alpha;
		out.red = oma * a.red + b.red * b.alpha;
		out.green = oma * a.green + b.green * b.alpha;
		out.blue = oma * a.blue + b.blue * b.alpha;
		out.alpha = oma * a.alpha + b.alpha;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof OGLColor)
			return this.equals((OGLColor)obj);
		return super.equals(obj);
	}

	public boolean equals(OGLColor color)
	{
		return 
			red == color.red
			&& green == color.green
			&& blue == color.blue
			&& alpha == color.alpha
		;
	}
	
}
