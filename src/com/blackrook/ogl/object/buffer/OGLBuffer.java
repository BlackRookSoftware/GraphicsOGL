/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.blackrook.ogl.OGLBindable;
import com.blackrook.ogl.OGLGraphics;
import com.blackrook.ogl.enums.AccessType;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.CachingHint;
import com.blackrook.ogl.enums.DataType;
import com.blackrook.ogl.object.OGLObject;

/**
 * Defines an OpenGL buffer.
 * @author Matthew Tropiano
 */
public abstract class OGLBuffer<B extends Buffer> extends OGLObject implements OGLBindable
{
	/** List of OpenGL object ids that were not deleted properly. */
	protected static int[] UNDELETED_IDS;
	/** Amount of OpenGL object ids that were not deleted properly. */
	protected static int UNDELETED_LENGTH;
	
	static
	{
		UNDELETED_IDS = new int[INIT_UNALLOC_SIZE];
		UNDELETED_LENGTH = 0;
	}

	/** OpenGL temp variable. */
	private int[] glStateNum;
	
	/** Data that this buffer contains. */
	private BufferType type;
	/** Internal type of this buffer. */
	private DataType dataType;
	/** Size (in elements) of this buffer. */
	private int capacity;

	/**
	 * Creates an OpenGL Buffer of a certain type.
	 * @param g	the graphics context to use.
	 */
	protected OGLBuffer(OGLGraphics g, BufferType type, DataType dataType)
	{
		super(g);
		this.type = type;
		this.dataType = dataType;
		capacity = 0;
	}
	
	/**
	 * Sets the size of this internal buffer, in elements, without sending it data.
	 * The data inside the buffer after allocation is undefined.
	 * @param g the graphics context.
	 * @param hint the caching hint for this OGLBuffer's data.
	 * @param capacity the size this buffer in elements, NOT BYTES.
	 */
	public void setCapacity(OGLGraphics g, CachingHint hint, int capacity)
	{
		bindTo(g);
		g.getGL().glBufferData(type.glValue, capacity * dataType.size, null, hint.glValue);
		g.getError();
		unbindFrom(g);
		this.capacity = capacity;
	}
	
	/**
	 * Sets the size of this internal buffer, in bytes, without sending it data.
	 * The data inside the buffer after allocation is undefined.
	 * @param g the graphics context.
	 * @param hint the caching hint for this OGLBuffer's data.
	 * @param capacity the size this buffer in bytes, NOT ELEMENTS.
	 */
	public void setByteCapacity(OGLGraphics g, CachingHint hint, int capacity)
	{
		bindTo(g);
		g.getGL().glBufferData(type.glValue, capacity, null, hint.glValue);
		g.getError();
		unbindFrom(g);
		this.capacity = capacity / dataType.size;
	}

	/**
	 * Returns the capacity (in elements) of this buffer.
	 */
	public int capacity()
	{
		return capacity;
	}

	/**
	 * Returns the buffer's type.
	 */
	public BufferType getType()
	{
		return type;
	}

	/**
	 * Returns the internal data type of this buffer.
	 */
	public DataType getDataType()
	{
		return dataType;
	}

	/**
	 * Sends data to this buffer using a source buffer for data.
	 * The size of the buffer is set using the source buffer.
	 * @param g the graphics context.
	 * @param hint the caching hint for this OGLBuffer's data.
	 * @param buffer the data to load into the OGLBuffer.
	 * @param elements the number of elements to actually send from the buffer.
	 */
	public void sendData(OGLGraphics g, CachingHint hint, Buffer buffer, int elements)
	{
		bindTo(g);
		g.getGL().glBufferData(type.glValue, elements * dataType.size, buffer, hint.glValue);
		g.getError();
		unbindFrom(g);
		this.capacity = buffer.capacity();
	}
	
	/**
	 * Sends data to this buffer, replacing its contents.
	 * The size of the buffer and its Caching Hint should already be set.
	 * @param g the graphics context.
	 * @param buffer the data to load into the OGLBuffer.
	 * @param offset the starting offset into the OGLBuffer for the data (in elements, NOT BYTES).
	 * @param elements the number of elements to actually send from the buffer.
	 */
	public void sendSubData(OGLGraphics g, Buffer buffer, int elements, int offset)
	{
		bindTo(g);
		g.getGL().glBufferSubData(type.glValue, offset * dataType.size, elements * dataType.size, buffer);
		g.getError();
		unbindFrom(g);
	}
	
	/**
	 * Sends data to this buffer using a source buffer for data.
	 * The size of the buffer is set using <code>size</size>.
	 * @param g the graphics context.
	 * @param hint the caching hint for this OGLBuffer's data.
	 * @param buffer the data to load into the OGLBuffer.
	 * @param size the number of bytes to actually send from the buffer, NOT ELEMENTS.
	 */
	public void sendByteData(OGLGraphics g, CachingHint hint, Buffer buffer, int size)
	{
		bindTo(g);
		g.getGL().glBufferData(type.glValue, size, buffer, hint.glValue);
		g.getError();
		unbindFrom(g);
		this.capacity = buffer.capacity() / dataType.size;
	}
	
	/**
	 * Sends data to this buffer, replacing its contents.
	 * The size of the buffer and its Caching Hint should already be set.
	 * @param g the graphics context.
	 * @param buffer the data to load into the OGLBuffer.
	 * @param size the number of bytes to actually send from the buffer, NOT ELEMENTS.
	 * @param offset the starting offset into the OGLBuffer for the data (in bytes, NOT ELEMENTS).
	 */
	public void sendByteSubData(OGLGraphics g, Buffer buffer, int size, int offset)
	{
		bindTo(g);
		g.getGL().glBufferSubData(type.glValue, offset * dataType.size, size, buffer);
		g.getError();
		unbindFrom(g);
	}
	
	@Override
	protected int allocate(OGLGraphics g)
	{
		glStateNum = new int[1];
		g.getGL().glGenBuffers(1, glStateNum, 0);
		return glStateNum[0];
	}
	
	@Override
	protected boolean free(OGLGraphics g)
	{
		glStateNum[0] = getGLId();
		g.getGL().glDeleteBuffers(1, glStateNum, 0);
		return true;
	}

	/**
	 * Maps the internal data of a OGLBuffer to a Java-native buffer.
	 * mapBuffer() should call this.
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType:
	 * </p>
	 * <ul>
     * <li>If READ, the mapped buffer will contain the buffer's current info.
	 * Nothing gets written back on unmap.
	 * <li>If WRITE, the mapped buffer will have undefined contents.
	 * The buffer's contents get written to the buffer object on unmap.
	 * <li>If READ_AND_WRITE, the mapped buffer will contain the buffer's current info.
	 * The buffer's contents get written to the buffer object on unmap.
	 * </ul> 
	 * @param g the graphics context.
	 * @param accessType an access hint for the returned buffer.
	 */
	protected ByteBuffer map(OGLGraphics g, AccessType accessType)
	{
		bindTo(g);
		ByteBuffer out = g.getGL().glMapBuffer(type.glValue, accessType.glValue);
		unbindFrom(g);
		return out;
	}

	/**
	 * Maps the internal data of a OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType:
	 * </p>
	 * <ul>
     * <li>If READ, the mapped buffer will contain the buffer's current info.
	 * Nothing gets written back on unmap.
	 * <li>If WRITE, the mapped buffer will have undefined contents.
	 * The buffer's contents get written to the buffer object on unmap.
	 * <li>If READ_AND_WRITE, the mapped buffer will contain the buffer's current info.
	 * The buffer's contents get written to the buffer object on unmap.
	 * </ul> 
	 * @param g the graphics context.
	 * @param accessType an access hint for the returned buffer.
	 */
	public abstract B mapBuffer(OGLGraphics g, AccessType accessType);

	/**
	 * Unmaps a buffer after it has been mapped and manipulated/read by the calling
	 * client application. Please note that the Buffer that was mapped from this OGLBuffer
	 * will be completely invalidated upon unmapping it.
	 * @param g the graphics context.
	 * @return true if unmap successful, false if data corruption occurred on unmap.
	 */
	public boolean unmapBuffer(OGLGraphics g)
	{
		bindTo(g);
		boolean out = g.getGL().glUnmapBuffer(type.glValue);
		unbindFrom(g);
		return out;
	}
	
	@Override
	public void bindTo(OGLGraphics g)
	{
		g.getGL().glBindBuffer(type.glValue, getGLId());
	}
	
	@Override
	public void unbindFrom(OGLGraphics g)
	{
		g.getGL().glBindBuffer(type.glValue, 0);
	}
	
	/**
	 * Destroys undeleted buffers abandoned from destroyed Java objects.
	 */
	public static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			g.getGL().glDeleteBuffers(UNDELETED_LENGTH, UNDELETED_IDS, 0);
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
	
	@Override
	public void finalize() throws Throwable
	{
		if (isAllocated())
			finalizeAddId(getGLId());
		super.finalize();
	}

}
