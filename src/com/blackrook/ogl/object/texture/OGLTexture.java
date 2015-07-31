/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.texture;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.media.opengl.*;

import com.blackrook.commons.Common;
import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.OGLObject;

/**
 * Standard texture class.
 * @author Matthew Tropiano
 */
public abstract class OGLTexture extends OGLObject
{
	/** Total estimated texture memory. */
	private static int totalTextureMemory;
	
	/** List of OpenGL object ids that were not deleted properly. */
	protected static int[] UNDELETED_IDS;
	/** Amount of OpenGL object ids that were not deleted properly. */
	protected static int UNDELETED_LENGTH;
	
	static
	{
		UNDELETED_IDS = new int[INIT_UNALLOC_SIZE];
		UNDELETED_LENGTH = 0;
	}

	/**
	 * Magnification filters.
	 */
	public static enum MagFilter
	{
		/** Nearest magnification - color using nearest neighbor (aliased - "pixelates" textures). */
		NEAREST(GL2.GL_NEAREST),
		/** Linear magnification - color using linear interpolation ("smoothes" textures). */
		LINEAR(GL2.GL_LINEAR);
		
		public final int glid;
		private MagFilter(int id) {glid = id;}
	}

	/**
	 * Minification filters.
	 */
	public static enum MinFilter
	{
		/** 
		 * Nearest minification - color using nearest neighbor (aliased - bad approximation). 
		 */
		NEAREST(GL2.GL_NEAREST),
		/** 
		 * Linear minification - color using cluster average (okay approximation). 
		 */
		LINEAR(GL2.GL_LINEAR),
		/** 
		 * Bilinear minification - color using cluster average and next mipmap's
		 * nearest neighbor (better approximation). 
		 */
		BILINEAR(GL2.GL_LINEAR_MIPMAP_NEAREST),
		/** 
		 * Trilinear minification - color using cluster average and next mipmap's 
		 * cluster average (best approximation).
		 * Also called "bicubic" or "cubic." 
		 */
		TRILINEAR(GL2.GL_LINEAR_MIPMAP_LINEAR);
		
		public final int glid;
		private MinFilter(int id) {glid = id;}
	}

	/**
	 * Combined min/mag filter types, for convenience.
	 */
	public static enum FilterType
	{
		NEAREST(MinFilter.NEAREST,MagFilter.NEAREST),
		LINEAR(MinFilter.LINEAR,MagFilter.LINEAR),
		BILINEAR(MinFilter.BILINEAR,MagFilter.LINEAR),
		TRILINEAR(MinFilter.TRILINEAR,MagFilter.LINEAR);
		
		public final MinFilter min;
		public final MagFilter mag;
		private FilterType(MinFilter min, MagFilter mag) {this.min = min; this.mag = mag;}
	}
	
	/**
	 * Texture wrapping types.
	 */
	public static enum WrapType
	{
		/** 
		 * Texture coordinates wrap to the other side. 
		 * Edge colors interpolate accordingly. 
		 */
		TILE(GL2.GL_REPEAT),
		/** 
		 * Texture coordinates clamp to [0,1]. 
		 * Edge colors are interpolated with the border color. 
		 */
		CLAMP(GL2.GL_CLAMP),
		/** 
		 * Texture coordinates clamp to [0,1]. 
		 * Edge colors are interpolated with the edge texel's color. 
		 */
		CLAMP_TO_EDGE(GL2.GL_CLAMP_TO_EDGE);

		public final int glid;
		private WrapType(int id) {glid = id;}
	}

	/**
	 * Texture internal storage format.
	 */
	public static enum InternalFormat
	{
		/** Grayscale, No alpha, default bit depth. */
		LUMINANCE(GL2.GL_LUMINANCE, false, 1),
		/** White, Alpha only, default bit depth. */
		INTENSITY(GL2.GL_INTENSITY, false, 1),
		/** RGB, No alpha, default bit depth. */
		RGB(GL2.GL_RGB, false, 3),
		/** RGBA, default bit depth. */
		RGBA(GL2.GL_RGBA, false, 4),
		/** RGBA, forced 16-bit. */
		RGBA4(GL2.GL_RGB4, false, 2),
		/** RGBA, forced 32-bit. */
		RGBA8(GL2.GL_RGB8, false, 4),
		/** RGB, No alpha, compressed. */
		COMPRESSED_RGB_DXT1(GL2.GL_COMPRESSED_RGB_S3TC_DXT1_EXT, true, 0.375f),
		/** RGBA, compressed, one-bit alpha. */
		COMPRESSED_RGBA_DXT1(GL2.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT, true, 0.5f),
		/** RGBA, compressed, lossy alpha. */
		COMPRESSED_RGBA_DXT3(GL2.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT, true, 1),
		/** RGBA, compressed, lossy alpha (second version). */
		COMPRESSED_RGBA_DXT5(GL2.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT, true, 1);
		
		public final int glid;
		public final boolean compressed;
		public final float sizeFactor;
		private InternalFormat(int id, boolean c, float factor) {glid = id; compressed = c; sizeFactor = factor;}
		public boolean isCompressed() {return compressed;}
		public int getOpenGLParam() {return glid;}
	}

	/** OpenGL temp variable. */
	protected int[] glStateNum;
	
	/** Width in pixels of the source picture. */
	private int width;
	/** Height in pixels of the source picture. */
	private int height;
	/** Depth in pixels of the source picture. */
	private int depth;
	/** Dimension for use with texture cubes. */
	private int faces;

	/** This texture's internal format. */
	private InternalFormat internalFormat;
	/** Minification Filtering mode for texture. */
	private MinFilter minFilterMode;
	/** Magnification Filtering mode for texture. */
	private MagFilter magFilterMode;
	/** Texture anisotropic filtering (in passes). */
	private float anisotropy;
	/** This texture's border thickness. */
	private int borderThickness;
	/** Does this texture generate new mipmaps each data update? */
	private boolean genMipmaps;

	/**
	 * Creates a new blank texture of equal size and height.
	 * @param targetName	the texture target name.
	 * @param g				the OGLGraphics context.
	 * @param format		the texture's intended internal format.
	 * @param minFilter		the minification filter.
	 * @param magFilter		the magnification filter.
	 * @param anisotropy	the anisotropy factor for texture filtering. 
	 * 						must be 2.0 or greater, or else it will not be set.
	 * @param borderSize	the texture's border size. must be 0 or greater.
	 * @param genMipmaps	true if mipmaps should be generated for this texture, false if not.
	 * @throws GraphicsException if an exception occurs during this object's creation.
	 */
	protected OGLTexture(int targetName, OGLGraphics g, InternalFormat format, MinFilter minFilter, MagFilter magFilter, 
			float anisotropy, int borderSize, boolean genMipmaps)
	{
		super(g);
		this.minFilterMode = minFilter;
		this.magFilterMode = magFilter;
		this.internalFormat = format;
		this.anisotropy = anisotropy;
		this.borderThickness = borderSize;
		this.genMipmaps = genMipmaps;
		
		this.width = 0;
		this.height = 0;
		this.depth = 0;
		this.faces = 0;
		
		g.clearError();
		GL gl = g.getGL();
		gl.glBindTexture(targetName,getGLId());
    	gl.glTexParameteri(targetName, GL2.GL_GENERATE_MIPMAP, genMipmaps ? GL2.GL_TRUE : GL2.GL_FALSE);
    	gl.glTexParameteri(targetName, GL2.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(targetName, GL2.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		if (anisotropy >= 2.0)
			gl.glTexParameterf(targetName, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
		g.getError();
	}
	
	/**
	 * Sets this texture's dimensions. Should be set when the texture gets
	 * data submitted to it.
	 * @param width		the width of the texture in pixels.
	 * @param height	the height of the texture in pixels.
	 * @param depth		the depth of the texture in pixels.
	 * @param faces		the number of faces.
	 */
	protected void setDimensions(int width, int height, int depth, int faces)
	{
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.faces = faces;
	}
	
	/**
	 * Converts a color byte to a corresponding float value.
	 * @param b		the byte.
	 * @return		the new float between 0 and 1 inclusively.
	 */
	protected float colorByteToFloat(byte b)
	{
		return (b < 0 ? b+256 : b) / 255.0f;
	}

	/**
	 * Converts a color float to a corresponding byte value.
	 * @param x		the float.
	 * @return		the new float between 0 and 1 inclusively.
	 */
	protected byte colorFloatToByte(float x)
	{
		float f = Math.round(x * 255);
		return (byte)(f > 127.0f ? f-256 : f);
	}
	
	/**
	 * This will adjust the total memory used by all textures
	 * using this texture's change in size.
	 * @param oldSize old texture size in bytes.
	 * @param newSize new texture size in bytes.
	 */
	protected static final void adjustMemorySize(int oldSize, int newSize)
	{
		totalTextureMemory += (newSize - oldSize);
	}
	
	/**
	 * Gets the minification filtering mode for this texture.
	 */
	public MinFilter getMinFilteringMode()
	{
		return minFilterMode;
	}

	/**
	 * Gets the magnification filtering mode for this texture.
	 */
	public MagFilter getMagFilteringMode()
	{
		return magFilterMode;
	}

	/**
	 * Get anisotropic filtering for this texture.
	 */
	public float getAnisotropy()
	{
		return anisotropy;
	}

	/**
	 * Gets the texture's border thickness.
	 */
	public int getBorder()
	{
		return borderThickness;
	}

	/**
	 * Get this texture's internal format.
	 */
	public InternalFormat getInternalFormat()
	{
		return internalFormat;
	}

	/**
	 * Gets this texture's width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Gets this texture's height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Gets this texture's depth.
	 */
	public int getDepth()
	{
		return depth;
	}
	
	/**
	 * Gets this texture's number of total faces (if cube; usually this is 1).
	 */
	public int getFaces()
	{
		return faces;
	}
	
	/**
	 * Returns this texture's estimated size in bytes (as it is loaded in OpenGL).
	 * This is calculated by its dimensions and internal format.
	 * @return this texture's estimated size in bytes.
	 */
	public int getEstimatedSize()
	{
		int size = (width * height * depth * faces);
		if (genMipmaps)
		{
			int w = width > 1 ? width / 2 : 1;
			int h = height > 1 ? height / 2 : 1;
			int d = depth > 1 ? depth / 2 : 1;
			while (w > 1 || h > 1 || d > 1)
			{
				size += (w * h * d * faces);
				w = w > 1 ? w / 2 : 1;
				h = h > 1 ? h / 2 : 1;
				d = d > 1 ? d / 2 : 1;
			}
		}
		return (int)(size * internalFormat.sizeFactor);
	}

	/**
	 * Does this texture generate new mipmaps each data update?
	 */
	public boolean generatesMipmaps()
	{
		return genMipmaps;
	}

	/**
	 * Returns true if this texture has power-of-two dimensions. 
	 */
	public boolean hasPowerOfTwoDimensions(BufferedImage image)
	{
		return RMath.isPowerOfTwo(width) && RMath.isPowerOfTwo(height) && RMath.isPowerOfTwo(depth);
	}
	
	/**
	 * Gets the total amount of texture memory in use (in bytes).
	 * This is a rough estimate based on internal formats and
	 * texture dimensions.
	 */
	public static int getTotalTextureMemory()
	{
		return totalTextureMemory;
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.clearError();
		g.getGL().glDeleteTextures(1,glStateNum,0);
		g.getError();
		totalTextureMemory -= getEstimatedSize();
		setDimensions(0, 0, 0, 0);
		return true;
	}
	
	/**
	 * Destroys undeleted texture objects abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteTextures(UNDELETED_LENGTH, UNDELETED_IDS, 0);
			UNDELETED_LENGTH = 0;
		}
	}

	// adds the OpenGL Id to the UNDELETED_IDS list.
	private static void finalizeAddId(int id)
	{
		if (UNDELETED_LENGTH == UNDELETED_IDS.length)
		{
			int[] newArray = new int[UNDELETED_IDS.length * 2];
			System.arraycopy(UNDELETED_IDS, 0, newArray, 0, UNDELETED_LENGTH);
			UNDELETED_IDS = newArray;
		}
		UNDELETED_IDS[UNDELETED_LENGTH++] = id;
	}

	/**
	 * Gets the byte data for a texture in BGRA color information per pixel.
	 * @param image the input image.
	 * @return a new {@link ByteBuffer} of the image's byte data.
	 */
	public static Buffer getByteData(BufferedImage image)
	{
		ByteBuffer out = Common.allocDirectByteBuffer(getRawSize(image));
		out.order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer intout = out.asIntBuffer();
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
	    int[] data = new int[imageWidth*imageHeight];
	    image.getRGB(0, 0, imageWidth, imageHeight, data, 0, imageWidth);
    	intout.put(data);
	    return out;
	}
	
	/**
	 * Returns the raw size in bytes that this image will need for byte
	 * buffer/array storage.
	 */
	public static int getRawSize(BufferedImage image)
	{
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		return imageWidth*imageHeight*4;
	}

	@Override
	public void finalize() throws Throwable
	{
		if (isAllocated())
		{
			finalizeAddId(getGLId());
			totalTextureMemory -= getEstimatedSize();
		}
		super.finalize();
	}

}
