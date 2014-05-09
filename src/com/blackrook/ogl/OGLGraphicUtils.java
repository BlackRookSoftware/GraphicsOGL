/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import com.blackrook.commons.Common;
import com.blackrook.ogl.enums.CachingHint;
import com.blackrook.ogl.object.buffer.OGLBuffer;
import com.blackrook.ogl.object.texture.OGLTexture;

/**
 * Utility library for graphics stuff.
 * @author Matthew Tropiano
 */
public final class OGLGraphicUtils
{
	
	
	
	/**
	 * Puts image data into an OGL buffer. 
	 * The buffer's contents are completely replaced.
	 * @param image the image to load.
	 * @param out the OGL buffer to put the data into.
	 */
	public static void putImageData(OGLGraphics g, BufferedImage image, OGLBuffer<?> out)
	{
		int size = OGLTexture.getRawSize(image);
		ByteBuffer buffer = Common.allocDirectByteBuffer(size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		IntBuffer intout = buffer.asIntBuffer();
		
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
	    int[] data = new int[imageWidth*imageHeight];
	    image.getRGB(0, 0, imageWidth, imageHeight, data, 0, imageWidth);
    	intout.put(data);
    	
    	out.setByteCapacity(g, CachingHint.STREAM_DRAW, size);
    	out.sendByteSubData(g, buffer, size, 0);
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
	 * Draws a solid sphere.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param radius	sphere radius.
	 * @param slices	geometry strip slices.
	 * @param stacks	geometry strip stacks.
	 */
	public static void drawSolidSphere(OGLGraphics g, float radius, int slices, int stacks)
	{
		g.getGLUT().glutSolidSphere(radius, slices, stacks);
	}

	/**
	 * Draws a wire sphere (no normal generation).
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param radius	sphere radius.
	 * @param slices	geometry strip slices.
	 * @param stacks	geometry strip stacks.
	 */
	public static void drawWireSphere(OGLGraphics g, float radius, int slices, int stacks)
	{
		g.getGLUT().glutWireSphere(radius, slices, stacks);
	}

	/**
	 * Draws a solid cube.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param size	cube width, length, and depth.
	 */
	public static void drawSolidCube(OGLGraphics g, float size)
	{
		g.getGLUT().glutSolidCube(size);
	}

	/**
	 * Draws a wire cube (no normal generation).
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param size	cube width, length, and depth.
	 */
	public static void drawWireCube(OGLGraphics g, float size)
	{
		g.getGLUT().glutWireCube(size);
	}

	/**
	 * Draws a solid teapot. This is the famed "Utah Teapot."
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param size	size of the teapot.
	 */
	public static void drawSolidTeapot(OGLGraphics g, float size)
	{
		g.getGLUT().glutSolidTeapot(size);
	}

	/**
	 * Draws a wire teapot (no normal information). This is the famed "Utah Teapot."
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param size	size of the teapot.
	 */
	public static void drawWireTeapot(OGLGraphics g, float size)
	{
		g.getGLUT().glutWireTeapot(size);
	}

	/**
	 * Draws a solid torus.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param innerRadius	inner ring radius.
	 * @param outerRadius	outer tube radius.
	 * @param numSides		number of faces around the ring.
	 * @param rings			number of faces around the outer perimeter of the ring.
	 */
	public static void drawSolidTorus(OGLGraphics g, float innerRadius, float outerRadius, int numSides, int rings)
	{
		g.getGLUT().glutSolidTorus(innerRadius,outerRadius,numSides,rings);
	}

	/**
	 * Draws a wire torus (no normal generation).
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param innerRadius	inner ring radius.
	 * @param outerRadius	outer tube radius.
	 * @param numSides		number of faces around the ring.
	 * @param rings			number of faces around the outer perimeter of the ring.
	 */
	public static void drawWireTorus(OGLGraphics g, float innerRadius, float outerRadius, int numSides, int rings)
	{
		g.getGLUT().glutWireTorus(innerRadius,outerRadius,numSides,rings);
	}

	/**
	 * Draws a solid cone.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param radius	cone base radius.
	 * @param height	the height of the cone.
	 * @param slices	the amount of slices.
	 * @param stacks	the amount of stacks.
	 */
	public static void drawSolidCone(OGLGraphics g, float radius, float height, int slices, int stacks)
	{
		g.getGLUT().glutSolidCone(radius,height,slices,stacks);
	}

	/**
	 * Draws a wire cone (no normal generation).
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 * @param radius	cone base radius.
	 * @param height	the height of the cone.
	 * @param slices	the amount of slices.
	 * @param stacks	the amount of stacks.
	 */
	public static void drawWireCone(OGLGraphics g, float radius, float height, int slices, int stacks)
	{
		g.getGLUT().glutWireCone(radius,height,slices,stacks);
	}

	/**
	 * Draws a solid dodecahedron, unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawSolidDodecahedron(OGLGraphics g)
	{
		g.getGLUT().glutSolidDodecahedron();
	}

	/**
	 * Draws a wire dodecahedron (no normal generation), unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawWireDodecahedron(OGLGraphics g)
	{
		g.getGLUT().glutWireDodecahedron();
	}

	/**
	 * Draws a solid icosahedron, unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawSolidIcosahedron(OGLGraphics g)
	{
		g.getGLUT().glutSolidIcosahedron();
	}

	/**
	 * Draws a wire icosahedron (no normal generation), unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawWireIcosahedron(OGLGraphics g)
	{
		g.getGLUT().glutWireIcosahedron();
	}

	/**
	 * Draws a solid octahedron, unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawSolidOctahedron(OGLGraphics g)
	{
		g.getGLUT().glutSolidOctahedron();
	}

	/**
	 * Draws a wire octahedron (no normal generation), unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawWireOctahedron(OGLGraphics g)
	{
		g.getGLUT().glutWireOctahedron();
	}

	/**
	 * Draws a solid tetrahedron, unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawSolidTetrahedron(OGLGraphics g)
	{
		g.getGLUT().glutSolidTetrahedron();
	}

	/**
	 * Draws a wire tetrahedron (no normal generation), unit sized.
	 * Be forewarned that this operation, like most "batch" utility calls,
	 * can be expensive to call. Try encapsulating a call to this method in
	 * a DisplayList, and call the list in the future.
	 */
	public static void drawWireTetrahedron(OGLGraphics g)
	{
		g.getGLUT().glutWireTetrahedron();
	}
	
}
