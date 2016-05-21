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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.blackrook.commons.Common;
import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.CachingHint;
import com.blackrook.ogl.enums.DataType;

/**
 * Utility library for graphics stuff.
 * @author Matthew Tropiano
 */
public final class OGLGraphicUtils
{
	
	/**
	 * Returns true if an image has power-of-two dimensions. 
	 * @param image the image to check.
	 */
	public static boolean hasPowerOfTwoDimensions(BufferedImage image)
	{
		return RMath.isPowerOfTwo(image.getWidth()) && RMath.isPowerOfTwo(image.getHeight());
	}
	
	/**
	 * Gets the byte data for a texture in BGRA color information per pixel.
	 * @param image the input image.
	 * @return a new direct {@link ByteBuffer} of the image's byte data.
	 */
	public static Buffer getByteData(BufferedImage image)
	{
		ByteBuffer out = Common.allocDirectByteBuffer(getRawSize(image));
		out.order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer intout = out.asIntBuffer();
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
	    int[] data = new int[imageWidth * imageHeight];
	    image.getRGB(0, 0, imageWidth, imageHeight, data, 0, imageWidth);
		intout.put(data);
	    return out;
	}

	/**
	 * Converts color byte data to a BufferedImage.
	 * @param imageData the input BGRA byte data.
	 * @param width the width of the resultant image.
	 * @param height the height of the resultant image.
	 * @return a new direct {@link ByteBuffer} of the image's byte data.
	 */
	public static BufferedImage setImageData(IntBuffer imageData, int width, int height)
	{
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    int[] data = new int[imageData.capacity()];
	    imageData.get(data);
	    out.setRGB(0, 0, width, height, data, 0, width);
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
		return imageWidth * imageHeight * 4;
	}

	/**
	 * Puts image data into an OGL buffer. 
	 * The buffer's contents are completely replaced.
	 * @param image the image to load.
	 * @param out the OGL buffer to put the data into.
	 */
	public static void putImageData(OGLGraphics g, BufferedImage image, OGLBuffer out)
	{
		int size = getRawSize(image);
		Buffer data = getByteData(image);
    	g.setBufferCapacity(BufferType.PIXEL, DataType.UNSIGNED_BYTE, CachingHint.STREAM_DRAW, size);
    	g.setBufferSubData(BufferType.PIXEL, DataType.UNSIGNED_BYTE, data, size, 0);
	}

	/**
	 * Resizes an image using nearest filtering.
	 * @param source	the source image.
	 * @param newWidth	the new image width.
	 * @param newHeight	the new image height.
	 */
	public static BufferedImage performResize(BufferedImage source, int newWidth, int newHeight)
	{
		BufferedImage out = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = out.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.drawImage(source, 0, 0, newWidth, newHeight, null);
		g2d.dispose();
		return out;
	}

	/**
	 * Resizes an image using bilinear filtering.
	 * @param source	the source image.
	 * @param newWidth	the new image width.
	 * @param newHeight	the new image height.
	 */
	public static BufferedImage performResizeBilinear(BufferedImage source, int newWidth, int newHeight)
	{
		BufferedImage out = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = out.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(source, 0, 0, newWidth, newHeight, null);
		g2d.dispose();
		return out;
	}

	/**
	 * Resizes an image using trilinear filtering.
	 * @param source	the source image.
	 * @param newWidth	the new image width.
	 * @param newHeight	the new image height.
	 * @return the an output image where the contents are flipped vertically.
	 */
	public static BufferedImage performResizeTrilinear(BufferedImage source, int newWidth, int newHeight)
	{
		BufferedImage out = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = out.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(source, 0, 0, newWidth, newHeight, null);
		g2d.dispose();
		return out;
	}

	/**
	 * Flips an image across one or two axes.
	 * @param source the source image.
	 * @param flipX if true, flips horizontally.
	 * @param flipY if true, flips vertically.
	 * @return an output image where the contents are flipped according to parameters.
	 */
	public static BufferedImage performFlip(BufferedImage source, boolean flipX, boolean flipY)
	{
		int width = source.getWidth();
		int height = source.getHeight();
		BufferedImage out = new BufferedImage(width, source.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = out.createGraphics();
		g2d.setComposite(AlphaComposite.Src);
		AffineTransform transform = g2d.getTransform();
		transform.concatenate(AffineTransform.getScaleInstance(flipX ? -1.0 : 1.0, flipY ? -1.0 : 1.0));
		transform.concatenate(AffineTransform.getTranslateInstance(flipX ? -width : 0, flipY ? -height : 0));
		g2d.setTransform(transform);
		g2d.drawImage(source, 0, 0, width, height, null);
		g2d.dispose();
		return out;
	}
	
}
