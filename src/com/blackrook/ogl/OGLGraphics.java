/*******************************************************************************
 * Copyright (c) 2014 - 2016 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * Contributors:
 *     Matt Tropiano - initial API and implementation
 *******************************************************************************/
package com.blackrook.ogl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.blackrook.commons.Common;
import com.blackrook.commons.hash.CaseInsensitiveHash;
import com.blackrook.commons.math.Matrix4F;
import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.data.OGLColor;
import com.blackrook.ogl.enums.AccessType;
import com.blackrook.ogl.enums.AttachPoint;
import com.blackrook.ogl.enums.BlendArg;
import com.blackrook.ogl.enums.BlendFunc;
import com.blackrook.ogl.enums.BufferType;
import com.blackrook.ogl.enums.CachingHint;
import com.blackrook.ogl.enums.DataType;
import com.blackrook.ogl.enums.FaceSide;
import com.blackrook.ogl.enums.FillMode;
import com.blackrook.ogl.enums.FrameBufferType;
import com.blackrook.ogl.enums.GeometryType;
import com.blackrook.ogl.enums.HintType;
import com.blackrook.ogl.enums.HintValue;
import com.blackrook.ogl.enums.LogicFunc;
import com.blackrook.ogl.enums.RenderbufferFormat;
import com.blackrook.ogl.enums.ShaderProgramType;
import com.blackrook.ogl.enums.SpriteOrigin;
import com.blackrook.ogl.enums.StencilTestFunc;
import com.blackrook.ogl.enums.TextureCubeFace;
import com.blackrook.ogl.enums.TextureFormat;
import com.blackrook.ogl.enums.TextureMagFilter;
import com.blackrook.ogl.enums.TextureMinFilter;
import com.blackrook.ogl.enums.TextureWrapType;
import com.blackrook.ogl.exception.GraphicsException;

/**
 * A graphics toolkit-type of implementation of OpenGL.
 * All calls made to this object change the attached context.
 * @author Matthew Tropiano
 */
public class OGLGraphics
{	
	/** GL 3.0 Interface. */
	private GL3 gl;
	/** GLU Interface. */
	private GLU glu;
	
	/** Graphics system instance that this belongs to. */
	private OGLSystem glSystem;
		
	// ======== State Info ========

	/** OpenGL renderer name. */
	private String glRenderer;
	/** OpenGL version name. */
	private String glVersion;
	/** OpenGL vendor name. */
	private String glVendor;
	/** OpenGL list of extensions. */
	private String glExtensions;
	/** Are we running NVidia architecture in this piece? */
	private boolean isNVidia;
	/** Are we running ATi architecture in this piece? */
	private boolean isATi;
	/** Are we running S3 architecture, and if so, WHY? */
	private boolean isS3;
	/** Are we running Matrox architecture? */
	private boolean isMatrox;
	/** Are we running Intel architecture? */
	private boolean isIntel;
	/** Extension list used for extension lookup. */
	private CaseInsensitiveHash extensionList;

	// === A bunch of extension checks for Shading, Multitexturing, and other crap.
	/** Flag for presence of occlusion query extension. */
	private boolean occlusionQueryExtensionPresent;
	/** Flag for presence of vertex shader extension. */
	private boolean vertexShaderExtensionPresent;
	/** Flag for presence of fragment shader extension. */
	private boolean fragmentShaderExtensionPresent;
	/** Flag for presence of geometry shader extension. */
	private boolean geometryShaderExtensionPresent;
	/** Flag for presence of render buffer extension. */
	private boolean renderBufferExtensionPresent;
	/** Flag for presence of vertex buffer extension. */
	private boolean vertexBufferExtensionPresent;
	/** Flag for presence of non-power-of-two texture support. */
	private boolean nonPowerOfTwoTextures;
	/** Flag for presence of point smoothing ability. */
	private boolean pointSmoothingPresent;
	/** Flag for presence of point sprite extension. */
	private boolean pointSpritesPresent;
	
	/** Maximum texture units. */
	private int maxTextureUnits;
	/** Maximum texture size. */
	private int maxTextureSize;
	/** Maximum texture buffer size. */
	private int maxTextureBufferSize;
	/** Maximum renderbuffer size. */
	private int maxRenderBufferSize;
	/** Maximum renderbuffer color attachments. */
	private int maxRenderBufferColorAttachments;
	/** Bit depth of the occlusion query sample counter. */
	private int queryCounterBitDepth;
	/** Minimum point size range. */
	private float minPointSize;
	/** Maximum point size range. */
	private float maxPointSize;
	/** Minimum line width range. */
	private float minLineWidth;
	/** Maximum line width range. */
	private float maxLineWidth;
	
	/** The current frame rendered. */
	private long currentFrame;
	/** The current millisecond at the beginning of the frame. */
	private long currentMilliseconds;
	/** The current nanosecond at the beginning of the frame. */
	private long currentNanos;
	/** The current state of the "blit bit" set at the beginning of the frame. */
	private boolean currentBlitBit;
	/** Time between frames. */
	private float currentTimeStepMillis;

	/** Last frame nanotime. */
	private long lastTimeNanos;
	/** Time between frames. */
	private long currentTimeStepNanos;
	
	/** Current running occlusion query. */
	private OGLOcclusionQuery currentOcclusionQuery;

	/** Completely ignore error checking requests? */
	private boolean errorIgnoring;
	
	/**
	 * Creates a new OGLGraphics context.
	 * @param system the source system.
	 * @param drawable the JOGL auto-drawable context.
	 */
	OGLGraphics(OGLSystem system, GLAutoDrawable drawable)
	{
		glSystem = system;

		gl = (GL3)drawable.getGL();
		glu = new GLU();
		
		getError();
		
		glRenderer = gl.glGetString(GL3.GL_RENDERER);
		glVersion = gl.glGetString(GL3.GL_VERSION);
		glVendor = gl.glGetString(GL3.GL_VENDOR);
		glExtensions = gl.glGetString(GL3.GL_EXTENSIONS);

		getError();
		
		if (glExtensions == null)
			throw new GraphicsException("Couldn't get list of extensions from OpenGL.");
		String[] exts = glExtensions.toLowerCase().split("\\s+");
		extensionList = new CaseInsensitiveHash();
		for (String s : exts)
			extensionList.put(s);

		setArch();
		setExtVars();
		
		currentFrame = 0L;
		currentTimeStepMillis = -1f;
		currentTimeStepNanos = -1L;
	}

	/**
	 * Called at the beginning of each display() call for each frame.
	 */
	final void beginFrame()
	{
		currentMilliseconds = System.currentTimeMillis();
		currentNanos = System.nanoTime();
		
		if (currentTimeStepMillis < 0.0f)
		{
			currentTimeStepMillis = 0.0f;
			currentTimeStepNanos = 0L;
		}
		else
		{
			long n = currentNanos - lastTimeNanos;
			currentTimeStepNanos = n;
			currentTimeStepMillis = (float)((double)(n)/1000000d);
		}

		lastTimeNanos = currentNanos;
		currentBlitBit = !currentBlitBit;
		currentFrame++;
	}
	
	/**
	 * Called on frame end - does object cleanup.
	 */
	final void endFrame() 
	{
	    // Clean up abandoned objects.
	    OGLBuffer.destroyUndeleted(this);
	    OGLFrameBuffer.destroyUndeleted(this);
	    OGLRenderBuffer.destroyUndeleted(this);
	    OGLOcclusionQuery.destroyUndeleted(this);
	    OGLShader.destroyUndeleted(this);
	    OGLShaderProgram.destroyUndeleted(this);
	    OGLTexture.destroyUndeleted(this);
	}

	/**
	 * Gets the current GL context.
	 */
	final GL3 getGL()
	{
		return gl;
	}

	/**
	 * Gets the current GLU context.
	 */
	final GLU getGLU()
	{
		return glu;
	}

	/** Returns a reference to the parent {@link OGLSystem} that created this. */
	public final OGLSystem getSystem()
	{
		return glSystem;
	}

	/**
	 * Sets the architecture flags.
	 */
	protected void setArch()
	{
		if (glRenderer == null)
			return;
		String rend = new String(glRenderer.toLowerCase());
		isNVidia = rend.contains("nvidia"); 
		isATi = rend.contains("ati"); 
		isS3 = rend.contains("s3"); 
		isMatrox = rend.contains("matrox");
		isIntel = rend.contains("intel");
	}
	
	/**
	 * Sets the extension flags and values.
	 */
	protected void setExtVars()
	{
		occlusionQueryExtensionPresent = extensionIsPresent("gl_arb_occlusion_query");
		vertexShaderExtensionPresent = extensionIsPresent("gl_arb_vertex_program");
		fragmentShaderExtensionPresent = extensionIsPresent("gl_arb_fragment_program");
		geometryShaderExtensionPresent = 
			extensionIsPresent("gl_ext_geometry_program4") || 
			extensionIsPresent("gl_nv_geometry_shader4") || 
			extensionIsPresent("gl_arb_geometry_shader4");
		renderBufferExtensionPresent = extensionIsPresent("gl_ext_framebuffer_object");
		vertexBufferExtensionPresent = extensionIsPresent("gl_arb_vertex_buffer_object");
		nonPowerOfTwoTextures =
			extensionIsPresent("GL_ARB_texture_non_power_of_two") ||
			extensionIsPresent("GL_texture_rectangle_ext") ||
			extensionIsPresent("GL_texture_rectangle_nv") ||
			extensionIsPresent("GL_texture_rectangle_arb");
		pointSmoothingPresent = extensionIsPresent("gl_arb_point_smooth");
		pointSpritesPresent = extensionIsPresent("gl_arb_point_sprite");

		maxTextureUnits = getGLInt(GL3.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
		maxTextureSize = getGLInt(GL3.GL_MAX_TEXTURE_SIZE);
		maxTextureBufferSize = getGLInt(GL3.GL_MAX_TEXTURE_BUFFER_SIZE);
		maxRenderBufferSize = getGLInt(GL3.GL_MAX_RENDERBUFFER_SIZE);
		maxRenderBufferColorAttachments = getGLInt(GL3.GL_MAX_COLOR_ATTACHMENTS);
		
		Cache cache = getCache();
		if (occlusionQueryExtensionPresent)
		{
			gl.glGetQueryiv(GL3.GL_SAMPLES_PASSED, GL3.GL_QUERY_COUNTER_BITS, cache.intState, 0);
			queryCounterBitDepth = cache.intState[0];
		}
		getGLFloats(GL3.GL_POINT_SIZE_RANGE, cache.floatState);
		minPointSize = cache.floatState[0];
		maxPointSize = cache.floatState[1];
		getGLFloats(GL3.GL_LINE_WIDTH_RANGE, cache.floatState);
		minLineWidth = cache.floatState[0];
		maxLineWidth = cache.floatState[1];
	}

	/**
	 * Queries OpenGL for an integer value.
	 * @param glEnum the OpenGL enumerant.
	 */
	protected final int getGLInt(int glEnum)
	{
		Cache cache = getCache();
		gl.glGetIntegerv(glEnum, cache.intState, 0);
		return cache.intState[0];
	}
	
	/**
	 * Queries OpenGL for a list of integer values.
	 * @param glEnum the OpenGL enumerant.
	 * @param out the array to dump the retrieved info.
	 * @throws ArrayIndexOutOfBoundsException if there 
	 * is not enough room in the destination array for the info.
	 */
	protected final void getGLInts(int glEnum, int[] out)
	{
		Cache cache = getCache();
		cache.intsize(out.length);
		gl.glGetIntegerv(glEnum, cache.intState, 0);
		System.arraycopy(cache.intState, 0, out, 0, out.length);
	}
	
	/**
	 * Queries OpenGL for a floating-point value.
	 * @param glEnum the OpenGL enumerant.
	 */
	protected final float getGLFloat(int glEnum)
	{
		Cache cache = getCache();
		gl.glGetFloatv(glEnum, cache.floatState, 0);
		return cache.floatState[0];
	}
	
	/**
	 * Queries OpenGL for a list of floating-point values.
	 * @param glEnum the OpenGL enumerant.
	 * @param out the array to dump the retrieved info.
	 * @throws ArrayIndexOutOfBoundsException if there 
	 * is not enough room in the destination array for the info.
	 */
	protected final void getGLFloats(int glEnum, float[] out)
	{
		Cache cache = getCache();
		cache.floatsize(out.length);
		gl.glGetFloatv(glEnum, cache.floatState, 0);
		System.arraycopy(cache.floatState, 0, out, 0, out.length);
	}
	
	/**
	 * Enables/disables an OpenGL state bit.
	 * @param glEnum the OpenGL enumerant.
	 * @param flag if true, enable. if false, disable.
	 */
	protected final void glFlagSet(int glEnum, boolean flag)
	{
		if (flag)
			gl.glEnable(glEnum);
		else
			gl.glDisable(glEnum);
	}
	
	/**
	 * Converts a Java boolean to an OpenGL GL_TRUE or GL_FALSE value.
	 */
	protected final int toGLBool(boolean val)
	{
		return val ? GL3.GL_TRUE : GL3.GL_FALSE;
	}
	
	/**
	 * Checks if an OpenGL extension is present.
	 * If you keep calling this method for the same extension, you are
	 * better off saving the results of the first call and using that, since
	 * the list of present extensions never change during runtime. 
	 */
	public boolean extensionIsPresent(String extName)
	{
		return extensionList.contains(extName.toLowerCase());
	}

	/**
	 * Gets the system milliseconds time, synced to the beginning of the current frame.
	 */
	public long currentTimeMillis()
	{
		return currentMilliseconds;
	}

	/**
	 * Gets the system nanosecond time, synced to the beginning of the current frame.
	 */
	public long currentNanos()
	{
		return currentNanos;
	}

	/**
	 * Gets the amount of milliseconds passed between this frame and the last one.
	 * If this is the first frame, this is 0. If this is BEFORE the first frame,
	 * this is -1f.
	 */
	public float currentTimeStepMillis()
	{
		return currentTimeStepMillis;
	}
	
	/**
	 * Gets the amount of milliseconds passed between this frame and the last one.
	 * If this is the first frame, this is 0. If this is BEFORE the first frame,
	 * this is -1f.
	 */
	public float currentTimeStepNanos()
	{
		return currentTimeStepNanos;
	}
	
	/**
	 * Current blitting bit.
	 * This will alternate between true and false each frame.
	 */
	public boolean currentBlit()
	{
		return currentBlitBit;
	}
	
	/**
	 * The current frame rendered (number).
	 */
	public long currentFrame()
	{
		return currentFrame;
	}
	
	/**
	 * Get max texture size in texels.
	 */
	public final int getMaxTextureSize()
	{
		return maxTextureSize;
	}
	
	/**
	 * Get max texture buffer size in bytes.
	 */
	public int getMaxTextureBufferSize() 
	{
		return maxTextureBufferSize;
	}

	/**
	 * Get the maximum size of a render buffer object in pixels.
	 */
	public final int getMaxRenderBufferSize()
	{
		return maxRenderBufferSize;
	}

	/**
	 * Get the maximum amount of color buffer attachments for a render buffer.
	 */
	public final int getMaxRenderBufferColorAttachments()
	{
		return maxRenderBufferColorAttachments;
	}

	/**
	 * Gets the bit depth of the occlusion query sample counter.
	 */
	public final int getQueryCounterBitDepth()
	{
		return queryCounterBitDepth;
	}

	/**
	 * Gets the minimum size a point can be rendered.
	 */
	public final float getMinPointSize()
	{
		return minPointSize;
	}

	/**
	 * Gets the maximum size a point can be rendered.
	 */
	public final float getMaxPointSize()
	{
		return maxPointSize;
	}

	/**
	 * Gets the minimum width for line geometry.
	 */
	public final float getMinLineWidth()
	{
		return minLineWidth;
	}

	/**
	 * Gets the maximum width for line geometry.
	 */
	public final float getMaxLineWidth()
	{
		return maxLineWidth;
	}

	/** Returns the rendering device of this GL system. */
	public final String getGLRenderer()
	{
		return glRenderer;
	}

	/** Returns the version of this GL system. */
	public final String getGLVersion()
	{
		return glVersion;
	}

	/** Returns the vendor name of this GL system. */
	public final String getGLVendor()
	{
		return glVendor;
	}

	/** Returns a string of all of the extensions supported by this system. */
	public final String getGLExtensions()
	{
		return glExtensions;
	}

	/** Returns a string of all of the extensions supported by this system. */
	public final int getGLExtensionCount()
	{
		return extensionList.size();
	}

	/**
	 * Returns true if occlusion query extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsOcclusionQueries()
	{
		return occlusionQueryExtensionPresent;
	}

	/**
	 * Returns true if vertex shader extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsVertexShaders()
	{
		return vertexShaderExtensionPresent;
	}

	/**
	 * Returns true if fragment shader extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsFragmentShaders()
	{
		return fragmentShaderExtensionPresent;
	}

	/**
	 * Returns true if geometry shader extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsGeometryShaders()
	{
		return geometryShaderExtensionPresent;
	}

	/**
	 * Returns true if render buffer extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsRenderBuffers()
	{
		return renderBufferExtensionPresent;
	}

	/**
	 * Returns true if vertex buffer extensions are present for the video device.
	 * False otherwise.
	 */
	public final boolean supportsVertexBuffers()
	{
		return vertexBufferExtensionPresent;
	}

	/**
	 * Returns true if this device supports non-power-of-two textures.
	 * False otherwise.
	 */
	public final boolean supportsNonPowerOfTwoTextures()
	{
		return nonPowerOfTwoTextures;
	}

	/**
	 * Returns true if this device supports smooth points.
	 * False otherwise.
	 */
	public final boolean supportsPointSmoothing()
	{
		return pointSmoothingPresent;
	}

	/**
	 * Returns true if this device supports point sprites.
	 * False otherwise.
	 */
	public final boolean supportsPointSprites()
	{
		return pointSpritesPresent;
	}

	/** Are we running NVidia architecture? */
	public final boolean isNVidia()
	{
		return isNVidia;
	}

	/** Are we running ATi architecture? */
	public final boolean isATi()
	{
		return isATi;
	}

	/** Are we running S3 architecture? */
	public final boolean isS3()
	{
		return isS3;
	}

	/** Are we running Matrox architecture? */
	public final boolean isMatrox()
	{
		return isMatrox;
	}

	/** Are we running Intel architecture? */
	public final boolean isIntel()
	{
		return isIntel;
	}

	/**
	 * Gets the current X position of the mouse.
	 * Convenience method for <code>getCanvas().getMouseX()</code>.
	 */
	public float getMouseX()
	{
		return glSystem.getMouseX();
	}

	/**
	 * Gets the current Y position of the mouse.
	 * Convenience method for <code>getCanvas().getMouseY()</code>.
	 */
	public float getMouseY()
	{
		return glSystem.getMouseY();
	}

	/**
	 * Gets the width of the canvas that this is built from.
	 * Convenience method for <code>(float)getCanvas().getWidth()</code>.
	 */
	public float getCanvasWidth()
	{
		return glSystem.getWidth();
	}

	/**
	 * Gets the height of the canvas that this is built from.
	 * Convenience method for <code>(float)getCanvas().getHeight()</code>.
	 */
	public float getCanvasHeight()
	{
		return glSystem.getHeight();
	}

	/**
	 * Gets the aspect ratio of the canvas that this is built from.
	 */
	public float getCanvasAspect()
	{
		return getCanvasWidth() / getCanvasHeight();
	}

	/**
	 * Clears the error bits for the GL Error flags.
	 */
	public void clearError()
	{
		if (errorIgnoring)
			return;
		while (gl.glGetError() != GL3.GL_NO_ERROR);
	}

	/**
	 * Tests for an OpenGL error via glGetError(). 
	 * If one is raised, this throws a GraphicsException with the error message.
	 */
	public void getError()
	{
		if (errorIgnoring)
			return;
		int error = gl.glGetError();
		if (error != GL3.GL_NO_ERROR)
			throw new GraphicsException("OpenGL raised error: "+glu.gluErrorString(error));
	}

	/**
	 * Gets if this completely ignores OpenGL error detection.
	 * If true, this could be reducing the amount of OpenGL calls this makes.
	 * @return true if so, false if not.
	 * @see #setErrorIgnoring(boolean)
	 */
	public boolean isErrorIgnoring() 
	{
		return errorIgnoring;
	}

	/**
	 * Sets if this completely ignores OpenGL error detection.
	 * If true, this could reduce the amount of OpenGL calls this makes.
	 * @param errorIgnoring if true, {@link #clearError()} and {@link #getError()} do nothing. Else, they do stuff.
	 */
	public void setErrorIgnoring(boolean errorIgnoring)
	{
		this.errorIgnoring = errorIgnoring;
	}

	/**
	 * Sets if this has VSync enabled.
	 * @param enabled if true, enables. false disables.
	 */
	public void setVSync(boolean enabled)
	{
		gl.setSwapInterval(enabled ? 1 : 0);
	}

	/**
	 * Sets an OpenGL hint.
	 * @param type the hint type to set.
	 * @param value the value to set for the provided hint.
	 */
	public void setHint(HintType type, HintValue value)
	{
		gl.glHint(type.glValue, value.glValue);
	}

	/**
	 * Sets the OpenGL viewport (Note: (0,0) is the lower-left corner).
	 * If any value is below zero, it is clamped to zero.
	 * @param x			x-coordinate origin of the screen.
	 * @param y			y-coordinate origin of the screen.
	 * @param width		the width of the viewport in pixels.
	 * @param height	the height of the viewport in pixels.
	 */
	public void setViewport(int x, int y, int width, int height)
	{
		gl.glViewport(Math.max(0,x), Math.max(0,y), Math.max(0,width), Math.max(0,height));
	}

	/**
	 * Convenience method for setting the viewport to the canvas's dimensions.<br/>
	 * Equivalent to: <code>setViewport(0, 0, (int)getCanvasWidth(), (int)getCanvasHeight())</code>
	 */
	public void setViewportToCanvas()
	{
		setViewport(0, 0, (int)getCanvasWidth(), (int)getCanvasHeight());
	}

	/**
	 * Sets the clear color.
	 * The color buffer is filled with this color upon clear.
	 * @param c the color to use.
	 */
	public void setClearColor(OGLColor c)
	{
		setClearColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * Sets the clear color.
	 * The color buffer is filled with this color upon clear.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setClearColor(float red, float green, float blue, float alpha)
	{
		gl.glClearColor(red, green, blue, alpha);
	}

	/**
	 * Set face winding to determine the front face.
	 */
	public void setFaceFront(FaceSide.Direction faceFront)
	{
		gl.glFrontFace(faceFront.glValue);
	}

	/**
	 * Set polygon fill mode.
	 */
	public void setFillMode(FillMode fillMode)
	{
	   	gl.glPolygonMode(FaceSide.FRONT_AND_BACK.glValue, fillMode.glValue);
	}

	/**
	 * Set front polygon fill mode.
	 */
	public void setFrontFillMode(FillMode fillMode)
	{
	   	gl.glPolygonMode(FaceSide.FRONT.glValue, fillMode.glValue);
	}

	/**
	 * Set back polygon fill mode.
	 */
	public void setBackFillMode(FillMode fillMode)
	{
	   	gl.glPolygonMode(FaceSide.BACK.glValue, fillMode.glValue);
	}

	/**
	 * Sets the reference unit size for the diameter of Point geometry.
	 * This input value will be CLAMPED within the minimum and maximum point sizes.
	 */
	public void setPointSize(float size)
	{
		gl.glPointSize(RMath.clampValue(size, minPointSize, maxPointSize));
	}

	/**
	 * Sets the point sprite coordinate origin.
	 * @param origin the sprite's origin point.
	 */
	public void setPointSpriteCoordOrigin(SpriteOrigin origin)
	{
		gl.glPointParameteri(GL3.GL_POINT_SPRITE_COORD_ORIGIN, origin.glValue);
	}

	/**
	 * Sets the threshold value to which point sizes are clamped if they exceed the specified value.
	 * @param size the size value.
	 */
	public void setPointFadeThreshold(float size)
	{
		gl.glPointParameterf(GL3.GL_POINT_FADE_THRESHOLD_SIZE, size);
	}

	/**
	 * Sets the width of line geometry.
	 * @param width the width of the line in pixels.
	 */
	public void setLineWidth(float width)
	{
		gl.glLineWidth(RMath.clampValue(width, minLineWidth, maxLineWidth));
	}

	/**
	 * Tells the OpenGL implementation to finish all pending commands in finite time.
	 * This ensures that the next commands are executed immediately.
	 * Has little to no effect on a double-buffered setup.
	 * Not to be confused with {@link #finish()}.
	 * @see #finish()
	 */
	public void flush()
	{
		gl.glFlush();
	}
	
	/**
	 * Tells the OpenGL implementation to finish all pending commands.
	 * OpenGL commands are usually pipelined for performance reasons. This ensures
	 * that OpenGL finishes all pending commands so that what you expect in the framebuffer
	 * is the last command executed, then resumes this thread.
	 * This best called right before a screenshot is taken.
	 */
	public void finish()
	{
		gl.glFinish();
	}
	
	/**
	 * Sets if each color component gets written to the color buffer.
	 * @param red	will the red component be written to the buffer?
	 * @param green	will the green component be written to the buffer?
	 * @param blue	will the blue component be written to the buffer?
	 * @param alpha	will the alpha component be written to the buffer?
	 */
	public void setColorMask(boolean red, boolean green, boolean blue, boolean alpha)
	{
		gl.glColorMask(red, green, blue, alpha);
	}

	/**
	 * Sets if all of the components of the color buffer get written to.
	 */
	public void setColorBufferWriteEnabled(boolean flag)
	{
		setColorMask(flag, flag, flag, flag);
	}

	/**
	 * Enables/Disables line smoothing.
	 * "Line smoothing" is a fancy term for anti-aliasing.
	 */
	public void setLineSmoothingEnabled(boolean enabled)
	{
		glFlagSet(GL3.GL_LINE_SMOOTH, enabled);
	}

	/**
	 * Clears a bunch of framebuffers.
	 * @param color		clear the color buffer?
	 * @param depth		clear the depth buffer?
	 * @param stencil	clear the stencil buffer?
	 */
	public void clearFrameBuffers(boolean color, boolean depth, boolean stencil)
	{
		gl.glClear(
			(color? GL3.GL_COLOR_BUFFER_BIT : 0) | 
			(depth? GL3.GL_DEPTH_BUFFER_BIT : 0) | 
			(stencil? GL3.GL_STENCIL_BUFFER_BIT : 0)
		);
	}

	/**
	 * Sets the current buffer to read from for pixel read/copy operations.
	 * By default, this is the BACK buffer in double-buffered contexts.
	 * @param b	the buffer to read from now on.
	 * @throws IllegalArgumentException if b is NONE or FRONT_AND_BACK.
	 */
	public void setFrameBufferRead(FrameBufferType b)
	{
		if (b == FrameBufferType.NONE)
			throw new IllegalArgumentException("The read buffer can't be NONE");
		gl.glReadBuffer(b.glValue);
	}

	/**
	 * Sets the current buffer to write to for pixel drawing/rasterizing operations.
	 * By default, this is the BACK buffer in double-buffered contexts.
	 * @param b	the buffer to write to from now on.
	 * @throws IllegalArgumentException if b is NONE or FRONT_AND_BACK.
	 */
	public void setFrameBufferWrite(FrameBufferType b)
	{
		if (b == FrameBufferType.NONE)
			throw new IllegalArgumentException("The read buffer can't be NONE");
		gl.glDrawBuffer(b.glValue);
	}

	/**
	 * Reads from the current-bound frame buffer into a target buffer.
	 * @param imageData	the buffer to write the RGBA pixel data to (must be direct).
	 * @param x the starting screen offset, x-coordinate (0 is left).
	 * @param y the starting screen offset, y-coordinate (0 is bottom).
	 * @param width the capture width in pixels.
	 * @param height the capture height in pixels.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void readFrameBuffer(Buffer imageData, int x, int y, int width, int height)
	{
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer.");
		
		gl.glReadPixels(x, y, width, height, GL3.GL_BGRA, GL3.GL_UNSIGNED_BYTE, imageData);
	}

	/**
	 * Sets the current pixel packing alignment value (GL-to-application).
	 * This is used for pulling pixel data from an OpenGL buffer into a format
	 * that the application can recognize/manipulate.
	 * @param alignment the alignment in bytes.
	 */
	public void setPixelPackAlignment(int alignment)
	{
		gl.glPixelStorei(GL3.GL_PACK_ALIGNMENT, alignment);
	}

	/**
	 * Gets the current pixel packing alignment value (GL-to-application).
	 * This is used for pulling pixel data from an OpenGL buffer into a format
	 * that the application can recognize/manipulate.
	 */
	public int getPixelPackAlignment()
	{
		return getGLInt(GL3.GL_PACK_ALIGNMENT);
	}

	/**
	 * Sets the current pixel unpacking alignment value (application-to-GL).
	 * This is used for pulling pixel data from an OpenGL buffer into a format
	 * that the application can recognize/manipulate.
	 * @param alignment the alignment in bytes.
	 */
	public void setPixelUnpackAlignment(int alignment)
	{
		gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, alignment);
	}

	/**
	 * Gets the current pixel unpacking alignment value (application-to-GL).
	 * This is used for pulling pixel data from an OpenGL buffer into a format
	 * that the application can recognize/manipulate.
	 */
	public int getPixelUnpackAlignment()
	{
		return getGLInt(GL3.GL_UNPACK_ALIGNMENT);
	}

	/**
	 * Sets if the depth test is enabled or not for incoming fragments.
	 * @param flag	the new value of the flag.
	 */
	public void setDepthTestEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_DEPTH_TEST,flag);
	}

	/**
	 * Sets depth clear value.
	 * If the depth buffer gets cleared, this is the value that is written
	 * to all of the pixels in the buffer.
	 */
	public void setDepthClear(float val)
	{
		gl.glClearDepth(val);
	}

	/**
	 * Set depth comparison function.
	 */
	public void setDepthFunc(LogicFunc func)
	{
		gl.glDepthFunc(func.glValue);
	}

	/** Sets if the depth buffer is enabled for writing. */
	public void setDepthMask(boolean flag)
	{
		gl.glDepthMask(flag);
	}

	/** Sets the stencil mask. */
	public void setStencilMask(int mask)
	{
		gl.glStencilMask(mask);
	}

	/**
	 * Sets if the stencil test is enabled or not for incoming fragments.
	 * @param flag	the new value of the flag.
	 */
	public void setStencilTestEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_STENCIL_TEST,flag);
	}

	/**
	 * Sets the stencil function for the stencil test.
	 * @param func		the comparision function to use.
	 * @param ref		the reference value.
	 * @param refmask	the stencil mask.
	 */
	public void setStencilTestFunc(LogicFunc func, int ref, int refmask)
	{
		gl.glStencilFunc(func.glValue, ref, refmask);
	}

	/**
	 * Sets the functions for what to do for each incoming fragment.
	 * @param stencilFail		function to perform if the stencil test fails.
	 * @param stencilDepthFail  function to perform if the stencil test passes, but the depth test fails (if enabled).
	 * @param stencilDepthPass  function to perform if the fragment passes, both the depth and stencil test.
	 */
	public void setStencilTestDepthFail(StencilTestFunc stencilFail, StencilTestFunc stencilDepthFail, StencilTestFunc stencilDepthPass)
	{
		gl.glStencilOp(stencilFail.glValue, stencilDepthFail.glValue, stencilDepthPass.glValue);
	}

	/**
	 * Sets if the scissor test is enabled
	 */
	public void setScissorTestEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_SCISSOR_TEST,flag);
	}

	/**
	 * Sets the bounds of the scissor test area.
	 * @param x			the lower left corner, x-coordinate.
	 * @param y			the lower left corner, y-coordinate.
	 * @param width		the width of scissor area from the lower-left corner.
	 * @param height	the height of scissor area from the lower-left corner.
	 */
	public void setScissorBounds(int x, int y, int width, int height)
	{
		gl.glScissor(x, y, width, height);
	}

	/**
	 * Sets if blending is enabled.
	 */
	public void setBlendingEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_BLEND,flag);
	}

	/**
	 * Sets the current blending function.
	 * @param source		the source fragment argument.
	 * @param destination	the destination fragment argument.
	 */
	public void setBlendingFunc(BlendArg source, BlendArg destination)
	{
		gl.glBlendFunc(source.glValue, destination.glValue);
	}

	/**
	 * Sets the current blending function.
	 * @param func	the function to set the fragment arguments.
	 */
	public void setBlendingFunc(BlendFunc func)
	{
		setBlendingFunc(func.argsrc, func.argdst);
	}

	/**
	 * Sets if face culling is enabled. 
	 */
	public void setFaceCullingEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_CULL_FACE,flag);
	}

	/**
	 * Sets the face side(s) that are culled if face culling is enabled.
	 */
	public void setFaceCullingSide(FaceSide side)
	{
		gl.glCullFace(side.glValue);
	}

	/**
	 * Draws an OGLDrawable object to the current context.
	 * Basically calls <code>drawable.drawUsing(this)</code>.
	 */
	public void draw(OGLDrawable drawable)
	{
		drawable.drawUsing(this);
	}

	/**
	 * Destroys an object, freeing it from OpenGL.
	 * @param object the object to free.
	 */
	public void destroy(OGLObject object)
	{
		object.destroy(this);
	}
	
	/**
	 * Sets if 1D texturing is enabled or not.
	 */
	public void setTexture1DEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_TEXTURE_1D,flag);
	}

	/**
	 * Sets if 2D texturing is enabled or not.
	 */
	public void setTexture2DEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_TEXTURE_2D,flag);
	}

	/**
	 * Sets if cube map texturing is enabled or not.
	 */
	public void setTextureCubeEnabled(boolean flag)
	{
		glFlagSet(GL3.GL_TEXTURE_CUBE_MAP,flag);
	}

	/**
	 * Sets the current "active" texture unit for texture bindings and texture environment settings.
	 * @param unit the texture unit to switch to.
	 */
	public void setTextureUnit(int unit)
	{
		if (unit < 0 || unit >= maxTextureUnits)
			throw new GraphicsException("Illegal texture unit. Must be from 0 to "+(maxTextureUnits-1)+".");
		gl.glActiveTexture(GL3.GL_TEXTURE0 + unit);
	}
	
	/**
	 * Creates a new texture object.
	 * @return a new, uninitialized texture object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLTexture createTexture()
	{
		return new OGLTexture(this);
	}
	
	/**
	 * Binds a 1D texture object to the current active texture unit.
	 * @param texture the texture to bind. Null unbinds the current texture.
	 */
	public void setTexture1D(OGLTexture texture)
	{
		if (texture == null)
			unsetTexture1D();
		else
			gl.glBindTexture(GL3.GL_TEXTURE_1D, texture.getGLId());
	}
	
	/**
	 * Sets the current filtering for the current 1D texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 */
	public void setTextureFiltering1D(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL3.GL_TEXTURE_1D, GL3.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_1D, GL3.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL3.GL_TEXTURE_1D, GL3.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
	}
	
	/**
	 * Sets the current wrapping for the current 1D texture.
	 * @param wrapS the wrapping mode, S-axis.
	 */
	public void setTextureWrapping1D(TextureWrapType wrapS)
	{
		gl.glTexParameteri(GL3.GL_TEXTURE_1D, GL3.GL_TEXTURE_WRAP_S, wrapS.glid);
	}

	/**
	 * Sends a texture into OpenGL's memory for the current 1D texture.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param format the internal format.
	 * @param width the texture width in texels.
	 * @param border the texel border to add, if any.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureData1D(Buffer imageData, int texlevel, TextureFormat format, int width, int border)
	{
		if (width > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");
		
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 
		
		clearError();
		gl.glTexImage1D(
			GL3.GL_TEXTURE_1D,
			texlevel,
			format.glid, 
			width,
			border,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}

	/**
	 * Sends a subset of data to the currently-bound 1D texture already in OpenGL's memory.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param width the texture width in texels.
	 * @param xoffs the texel offset.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureSubData1D(Buffer imageData, int texlevel, int width, int xoffs)
	{
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 

		clearError();
		gl.glTexSubImage1D(
			GL3.GL_TEXTURE_1D,
			texlevel,
			xoffs,
			width,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}

	/**
	 * Unbinds a one-dimensional texture from the current texture unit.
	 */
	public void unsetTexture1D()
	{
		gl.glBindTexture(GL3.GL_TEXTURE_1D, 0);
	}

	/**
	 * Binds a 2D texture object to the current active texture unit.
	 * @param texture the texture to bind. Null unbinds the current texture.
	 */
	public void setTexture2D(OGLTexture texture)
	{
		if (texture == null)
			unsetTexture2D();
		else
			gl.glBindTexture(GL3.GL_TEXTURE_2D, texture.getGLId());
	}

	/**
	 * Sets the current filtering for the current 2D texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 */
	public void setTextureFiltering2D(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
	}
	
	/**
	 * Sets the current wrapping for the current 2D texture.
	 * @param wrapS the wrapping mode, S-axis.
	 * @param wrapT the wrapping mode, T-axis.
	 */
	public void setTextureWrapping2D(TextureWrapType wrapS, TextureWrapType wrapT)
	{
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, wrapT.glid);
	}
	
	/**
	 * Sends a texture into OpenGL's memory for the current 2D texture.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param format the internal format.
	 * @param width the texture width in texels.
	 * @param height the texture height in texels.
	 * @param border the texel border to add, if any.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureData2D(Buffer imageData, int texlevel, TextureFormat format, int width, int height, int border)
	{
		if (width > getMaxTextureSize() || height > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");

		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 
		
		clearError();
		gl.glTexImage2D(
			GL3.GL_TEXTURE_2D,
			texlevel,
			format.glid, 
			width,
			height,
			border,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}
	
	/**
	 * Sends a subset of data to the currently-bound 2D texture already in OpenGL's memory.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param width the texture width in texels.
	 * @param height the texture height in texels.
	 * @param xoffs the texel offset.
	 * @param yoffs the texel offset.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureSubData2D(Buffer imageData, int texlevel, int width, int height, int xoffs, int yoffs)
	{
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 

		clearError();
		gl.glTexSubImage2D(
			GL3.GL_TEXTURE_2D,
			texlevel,
			xoffs,
			yoffs,
			width,
			height,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}
	
	/**
	 * Copies the contents of the current read frame buffer into a two-dimensional texture.
	 * @param texture the texture object.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param xoffset the offset in pixels on this texture (x-coordinate) to put this texture data.
	 * @param yoffset the offset in pixels on this texture (y-coordinate) to put this texture data.
	 * @param srcX the screen-aligned x-coordinate of what to grab from the buffer (0 is the left side of the screen).
	 * @param srcY the screen-aligned y-coordinate of what to grab from the buffer (0 is the bottom of the screen).
	 * @param width the width of the screen in pixels to grab.
	 * @param height the height of the screen in pixels to grab.
	 */
	public void copyBufferToCurrentTexture2D(OGLTexture texture, int texlevel, int xoffset, int yoffset, int srcX, int srcY, int width, int height)
	{
		gl.glCopyTexSubImage2D(GL3.GL_TEXTURE_2D, texlevel, xoffset, yoffset, srcX, srcY, width, height);
	}

	/**
	 * Unbinds a two-dimensional texture from the current texture unit.
	 */
	public void unsetTexture2D()
	{
		gl.glBindTexture(GL3.GL_TEXTURE_2D, 0);
	}

	/**
	 * Sets the 2D multisample texture object to the current active texture unit.
	 * @param format the internal format.
	 * @param width the texture width in texels.
	 * @param height the texture height in texels.
	 * @param samples the number of samples per pixel.
	 * @param fixedSampleLocations if true, the positions of the samples do not change over the texture.
	 */
	public void setTextureMultisample2D(TextureFormat format, int width, int height, int samples, boolean fixedSampleLocations)
	{
		gl.glTexImage2DMultisample(GL3.GL_TEXTURE_2D_MULTISAMPLE, samples, format.getGLValue(), width, height, fixedSampleLocations);
	}

	/**
	 * Binds a texture cube object to the current active texture unit.
	 * @param texture the texture to bind. Null unbinds the current texture.
	 */
	public void setTextureCube(OGLTexture texture)
	{
		if (texture == null)
			unsetTextureCube();
		else
			gl.glBindTexture(GL3.GL_TEXTURE_CUBE_MAP, texture.getGLId());
	}

	/**
	 * Sets the current filtering for the current CubeMap texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 */
	public void setTextureFilteringCube(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
	}

	/**
	 * Sets the current wrapping for the current CubeMap texture.
	 * @param wrapS the wrapping mode, S-axis.
	 * @param wrapT the wrapping mode, T-axis.
	 * @param wrapR the wrapping mode, R-axis.
	 */
	public void setTextureWrappingCube(TextureWrapType wrapS, TextureWrapType wrapT, TextureWrapType wrapR)
	{
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_T, wrapT.glid);
		gl.glTexParameteri(GL3.GL_TEXTURE_CUBE_MAP, GL3.GL_TEXTURE_WRAP_R, wrapR.glid);
	}
	
	/**
	 * Sends a texture into OpenGL's memory for the current CubeMap texture.
	 * @param face the cube face to set.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param format the internal format.
	 * @param width the texture width in texels.
	 * @param height the texture height in texels.
	 * @param border the texel border to add, if any.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureDataCube(TextureCubeFace face, Buffer imageData, int texlevel, TextureFormat format, int width, int height, int border)
	{
		if (width > getMaxTextureSize() || height > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");
		
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 

		clearError();
		gl.glTexImage2D(
			face.glValue,
			texlevel,
			format.glid, 
			width,
			height,
			border,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}
	
	/**
	 * Sends a subset of data to the currently-bound CubeMap texture already in OpenGL's memory.
	 * @param face the cube face to set.
	 * @param imageData the BGRA image to send.
	 * @param texlevel the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param width the texture width in texels.
	 * @param height the texture height in texels.
	 * @param xoffs the texel offset.
	 * @param yoffs the texel offset.
	 * @throws GraphicsException if the buffer provided is not direct.
	 */
	public void setTextureSubDataCube(TextureCubeFace face, Buffer imageData, int texlevel, int width, int height, int xoffs, int yoffs)
	{
		if (!imageData.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 

		clearError();
		gl.glTexSubImage2D(
			face.glValue,
			texlevel,
			xoffs,
			yoffs,
			width,
			height,
			GL3.GL_BGRA,
			GL3.GL_UNSIGNED_BYTE,
			imageData
		);
		getError();
	}
	
	/**
	 * Unbinds a texture cube from the current texture unit.
	 */
	public void unsetTextureCube()
	{
		gl.glBindTexture(GL3.GL_TEXTURE_CUBE_MAP,0);
	}

	/**
	 * Creates a new shader program object (vertex, fragment, etc.).
	 * @param type the program type. if not a valid program type, this throws an exception.
	 * @param file the source file to read for compiling.
	 * @return the instantiated program.
	 * @throws NullPointerException if file is null.
	 * @throws IOException if the source of the source code can't be read.
	 * @throws FileNotFoundException if the source file does not exist.
	 */
	public OGLShaderProgram createShaderProgram(ShaderProgramType type, File file) throws IOException
	{
		switch (type)
		{
			case VERTEX:
				return new OGLShaderProgramVertex(this, file);
			case GEOMETRY:
				return new OGLShaderProgramGeometry(this, file);
			case FRAGMENT:
				return new OGLShaderProgramFragment(this, file);
			default:
				throw new GraphicsException("Bad shader program type.");
		}
	}
	
	/**
	 * Creates a new shader program object (vertex, fragment, etc.).
	 * @param type the program type. if not a valid program type, this throws an exception.
	 * @param streamName the name of the stream (can appear in exceptions).
	 * @param in the input stream.
	 * @return the instantiated program.
	 * @throws NullPointerException if file is null.
	 * @throws IOException if the source of the source code can't be read.
	 * @throws FileNotFoundException if the source file does not exist.
	 */
	public OGLShaderProgram createShaderProgram(ShaderProgramType type, String streamName, InputStream in) throws IOException
	{
		switch (type)
		{
			case VERTEX:
				return new OGLShaderProgramVertex(this, streamName, in);
			case GEOMETRY:
				return new OGLShaderProgramGeometry(this, streamName, in);
			case FRAGMENT:
				return new OGLShaderProgramFragment(this, streamName, in);
			default:
				throw new GraphicsException("Bad shader program type.");
		}
	}
	
	/**
	 * Creates a new shader program object (vertex, fragment, etc.).
	 * @param type the program type. if not a valid program type, this throws an exception.
	 * @param streamName the name of the originating stream (can appear in exceptions).
	 * @param sourceCode the code to compile.
	 * @return the instantiated program.
	 * @throws NullPointerException if file is null.
	 * @throws IOException if the source of the source code can't be read.
	 * @throws FileNotFoundException if the source file does not exist.
	 */
	public OGLShaderProgram createShaderProgram(ShaderProgramType type, String streamName, String sourceCode) throws IOException
	{
		switch (type)
		{
			case VERTEX:
				return new OGLShaderProgramVertex(this, streamName, sourceCode);
			case GEOMETRY:
				return new OGLShaderProgramGeometry(this, streamName, sourceCode);
			case FRAGMENT:
				return new OGLShaderProgramFragment(this, streamName, sourceCode);
			default:
				throw new GraphicsException("Bad shader program type.");
		}
	}
	
	/**
	 * Creates a new shader object.
	 * @param programs the programs to attach.
	 * @return a new, linked shader object.
	 * @throws GraphicsException if the object could not be created, or compilation/linking failed.
	 */
	public OGLShader createShader(OGLShaderProgram ... programs)
	{
		return new OGLShader(this, programs);
	}

	/**
	 * Binds a shader to the current context.
	 * @param shader the shader to bind. Null unbinds the current shader.
	 */
	public void setShader(OGLShader shader)
	{
		if (shader == null)
			unsetShader();
		else
			gl.glUseProgram(shader.getGLId());
	}

	/**
	 * Sets a uniform integer value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value the value to set.
	 */
	public void setShaderUniformInt(int locationId, int value)
	{
		gl.glUniform1i(locationId, value);
	}
	
	/**
	 * Sets a uniform integer value array on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param values the values to set.
	 */
	public void setShaderUniformIntArray(int locationId, int ... values)
	{
		gl.glUniform1iv(locationId, values.length, values, 0);
	}
	
	/**
	 * Sets a uniform unsigned integer value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value the value to set.
	 */
	public void setShaderUniformIntUnsigned(int locationId, int value)
	{
		gl.glUniform1ui(locationId, value);
	}
	
	/**
	 * Sets a uniform unsigned integer value array on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param values the values to set.
	 */
	public void setShaderUniformIntUnsignedArray(int locationId, int ... values)
	{
		gl.glUniform1uiv(locationId, values.length, values, 0);
	}
	
	/**
	 * Sets a uniform float value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value the value to set.
	 */
	public void setShaderUniformFloat(int locationId, float value)
	{
		gl.glUniform1f(locationId, value);
	}
	
	/**
	 * Sets a uniform float array value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param values the values to set.
	 */
	public void setShaderUniformFloatArray(int locationId, float ... values)
	{
		gl.glUniform1fv(locationId, values.length, values, 0);
	}
	
	/**
	 * Sets a uniform value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value0 the first value to set.
	 * @param value1 the second value to set.
	 */
	public void setShaderUniformVec2(int locationId, float value0, float value1)
	{
		gl.glUniform2fv(locationId, 1, new float[]{value0, value1}, 0);
	}
	
	/**
	 * Sets a uniform value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value0 the first value to set.
	 * @param value1 the second value to set.
	 */
	public void setShaderUniformVec3(int locationId, float value0, float value1, float value2)
	{
		gl.glUniform3fv(locationId, 1, new float[]{value0, value1, value2}, 0);
	}
	
	/**
	 * Sets a uniform value on the currently-bound shader.
	 * @param locationId the uniform location.
	 * @param value0 the first value to set.
	 * @param value1 the second value to set.
	 * @param value2 the third value to set.
	 * @param value3 the fourth value to set.
	 */
	public void setShaderUniformVec4(int locationId, float value0, float value1, float value2, float value3)
	{
		gl.glUniform4fv(locationId, 1, new float[]{value0, value1, value2, value3}, 0);
	}
	
	/**
	 * Sets a uniform value on the currently bound shader.
	 * @param locationId the uniform location.
	 * @param matrix the matrix value.
	 */
	public void setShaderUniformMatrix(int locationId, Matrix4F matrix)
	{
		// Don't transpose - Matrix4Fs are column-major.
		gl.glUniformMatrix4fv(locationId, 1, false, matrix.getArray(), 0);
	}
	
	/**
	 * Sets a uniform value on the currently bound shader.
	 * @param locationId the uniform location.
	 * @param matrices the array of matrices.
	 */
	public void setShaderUniformMatrix(int locationId, Matrix4F[] matrices)
	{
		Cache cache = getCache();
		cache.floatsize(16 * matrices.length);
		for (int i = 0; i < matrices.length; i++)
			matrices[i].getFloats(cache.floatState, 16 * i);
			
		// Don't transpose - Matrix4Fs are column-major.
		gl.glUniformMatrix4fv(locationId, matrices.length, false, cache.floatState, 0);
	}
	
	/**
	 * Sets a uniform VEC2 with the current canvas dimensions. 
	 * X is width in pixels, Y is height in pixels.
	 * @param locationId the uniform location.
	 */
	public void setShaderUniformCanvas(int locationId)
	{
		setShaderUniformVec2(locationId, getCanvasWidth(), getCanvasHeight());
	}
	
	/**
	 * Sets a uniform VEC2 with the current mouse coordinates. 
	 * X is X-coordinate on canvas, Y is Y-coordinate on canvas.
	 * @param locationId the uniform location.
	 */
	public void setShaderUniformMouse(int locationId)
	{
		setShaderUniformVec2(locationId, getMouseX(), getMouseY());
	}
	
	/**
	 * Unbinds a shader from the current context.
	 */
	public void unsetShader()
	{
		gl.glUseProgram(0);
	}

	/**
	 * Creates a new render buffer object.
	 * @return a new, uninitialized render buffer object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLRenderBuffer createFrameRenderBuffer()
	{
		return new OGLRenderBuffer(this);
	}

	/**
	 * Binds a FrameRenderBuffer to the current context.
	 * @param frameRenderBuffer the render buffer to bind to the current render buffer. Null unbinds the current render buffer.
	 */
	public void setFrameRenderBuffer(OGLRenderBuffer frameRenderBuffer)
	{
		if (frameRenderBuffer == null)
			unsetFrameRenderBuffer();
		else
			gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, frameRenderBuffer.getGLId());
	}

	/**
	 * Sets a render buffer's internal format and size.
	 * @param format the buffer format.
	 * @param width the width in pixel data.
	 * @param height the height in pixel data.
	 */
	public void setFrameRenderBufferSize(RenderbufferFormat format, int width, int height)
	{
		if (width < 1 || height < 1)
			throw new GraphicsException("Render buffer size cannot be less than 1 in any dimension.");
		gl.glRenderbufferStorage(GL3.GL_RENDERBUFFER, format.glid, width, height);
	}

	/**
	 * Unbinds a FrameRenderBuffer from the current context.
	 */
	public void unsetFrameRenderBuffer()
	{
		gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);
	}

	/**
	 * Creates a new framebuffer object.
	 * @return a new, uninitialized framebuffer object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLFrameBuffer createFrameBuffer()
	{
		return new OGLFrameBuffer(this);
	}

	/**
	 * Binds a FrameBuffer for rendering.
	 * @param frameBuffer the framebuffer to set as the current one. Null unbinds the current framebuffer.
	 */
	public void setFrameBuffer(OGLFrameBuffer frameBuffer)
	{
		if (frameBuffer == null)
			unsetFrameBuffer();
		else
			gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, frameBuffer.getGLId());
	}

	/**
	 * Tests for frame buffer completeness on the bound framebuffer. 
	 * If incomplete, this throws a GraphicsException with the error message.
	 */
	public void checkFrameBufferStatus()
	{
		int status = gl.glCheckFramebufferStatus(GL3.GL_FRAMEBUFFER);
		String errorString = null;
		if (status != GL3.GL_FRAMEBUFFER_COMPLETE) 
		{
			switch (status)
			{
				case GL3.GL_FRAMEBUFFER_UNSUPPORTED:
					errorString = "Framebuffer object format is unsupported by the video hardware.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
					errorString = "Incomplete attachment.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
					errorString = "Incomplete missing attachment.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
					errorString = "Incomplete dimensions.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
					errorString = "Incomplete formats.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
					errorString = "Incomplete draw buffer.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
					errorString = "Incomplete read buffer.";
					break;
				case GL3.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
					errorString = "Incomplete multisample buffer.";
					break;
				default:
					errorString = "Framebuffer object status is invalid due to unknown error.";
					break;
			}
			throw new GraphicsException("OpenGL raised error: "+errorString);
		}
	}

	/**
	 * Attaches a texture to this frame buffer for rendering directly to a texture.
	 * @param attachPoint the attachment source point.
	 * @param texture the texture to attach this to.
	 */
	public void attachFramebufferTexture2D(AttachPoint attachPoint, OGLTexture texture)
	{
		clearError();
		gl.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, attachPoint.glVal, GL3.GL_TEXTURE_2D, texture.getGLId(), 0);
		getError();
	}
	
	/**
	 * Detaches a texture from this frame buffer.
	 * @param attachPoint the attachment source point.
	 */
	public void detachFramebufferTexture2D(AttachPoint attachPoint)
	{
		clearError();
		gl.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER, attachPoint.glVal, GL3.GL_TEXTURE_2D, 0, 0);
		getError();
	}
	
	/**
	 * Attaches a render buffer to the current frame buffer.
	 * @param attachPoint the attachment source point.
	 * @param renderBuffer the render buffer to attach this to.
	 */
	public void attachRenderBuffer(AttachPoint attachPoint, OGLRenderBuffer renderBuffer)
	{
		clearError();
		gl.glFramebufferRenderbuffer(GL3.GL_FRAMEBUFFER, attachPoint.glVal, GL3.GL_RENDERBUFFER, renderBuffer.getGLId());
		getError();
	}
	
	/**
	 * Detaches a render buffer from the current frame buffer.
	 * @param attachPoint the attachment source point.
	 */
	public void detachFrameRenderBuffer(AttachPoint attachPoint)
	{
		clearError();
		gl.glFramebufferRenderbuffer(GL3.GL_FRAMEBUFFER, attachPoint.glVal, GL3.GL_RENDERBUFFER, 0);
		getError();
	}
	
	/**
	 * Unbinds a FrameBuffer for rendering.
	 * The current buffer will then be the default target buffer.
	 */
	public void unsetFrameBuffer()
	{
		gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
	}

	/**
	 * Creates a new occlusion query.
	 * @return a new occlusion query object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLOcclusionQuery createQuery()
	{
		return new OGLOcclusionQuery(this);
	}
	
	/**
	 * Starts the occlusion query.
	 * Between startQuery() and endQuery(), geometry is drawn, and the amount of samples
	 * that pass the depth/stencil test get counted, so make sure the depth/stencil test is enabled!
	 * <p>
	 * Two queries cannot overlap each other, or an exception will be thrown! 
	 * @param query the query to start.
	 * @throws GraphicsException if a query is already in progress.
	 */
	public void startQuery(OGLOcclusionQuery query)
	{
		if (currentOcclusionQuery != null)
			throw new GraphicsException("An occlusion query is already active.");
		gl.glBeginQuery(GL3.GL_SAMPLES_PASSED, query.getGLId());
		currentOcclusionQuery = query;
	}

	/**
	 * Ends the occlusion query.
	 * Between startQuery() and endQuery(), geometry is drawn, and the amount of samples
	 * that pass the depth/stencil test get counted, so make sure the depth/stencil test is enabled!
	 * <p>
	 * Two queries cannot overlap each other, or an exception will be thrown! 
	 */
	public void endQuery()
	{
		if (currentOcclusionQuery == null)
			throw new GraphicsException("Attempt to end query without starting one.");
		gl.glEndQuery(GL3.GL_SAMPLES_PASSED);
		currentOcclusionQuery = null;
	}

	/**
	 * Creates a new buffer object.
	 * @return a new, uninitialized buffer object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLBuffer createBuffer()
	{
		return new OGLBuffer(this);
	}
	
	/**
	 * Binds a buffer to the current context.
	 * @param type the buffer type to bind.
	 * @param buffer the buffer to bind. Null unbinds the currently bound buffer type.
	 */
	public void setBuffer(BufferType type, OGLBuffer buffer)
	{
		if (buffer == null)
			unsetBuffer(type);
		else
			gl.glBindBuffer(type.glValue, buffer.getGLId());
	}

	/**
	 * Sets the capacity of the current buffer (sends no data).
	 * @param type the buffer type binding.
	 * @param cachingHint the caching hint on this buffer's data.
	 * @param dataType the data type.
	 * @param elements the amount of elements of the data type.
	 */
	public void setBufferCapacity(BufferType type, DataType dataType, CachingHint cachingHint, int elements)
	{
		clearError();
		gl.glBufferData(type.glValue, elements * dataType.size, null, cachingHint.glValue);
		getError();
	}
	
	/**
	 * Sets the data of the current buffer.
	 * @param type the buffer type binding.
	 * @param cachingHint the caching hint on this buffer's data.
	 * @param data the data to send.
	 */
	public void setBufferData(BufferType type, DataType dataType, CachingHint cachingHint, Buffer data)
	{
		if (!data.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 

		clearError();
		gl.glBufferData(type.glValue, dataType.size * data.capacity(), data, cachingHint.glValue);
		getError();
	}
	
	/**
	 * Sets a subsection of data to the current buffer.
	 * @param type the buffer type binding.
	 * @param data the data to send.
	 * @param size the amount of data to send.
	 * @param offset the offset into the buffer to copy.
	 */
	public void setBufferSubData(BufferType type, DataType dataType, Buffer data, int size, int offset)
	{
		if (!data.isDirect())
			throw new GraphicsException("Data must be a direct buffer."); 
		
		clearError();
		gl.glBufferSubData(type.glValue, dataType.size * offset, dataType.size * size, data);
		getError();
	}
	
	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public ByteBuffer mapByteBuffer(BufferType type, AccessType accessType)
	{
		return gl.glMapBuffer(type.glValue, accessType.glValue);
	}

	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public ShortBuffer mapShortBuffer(BufferType type, AccessType accessType)
	{
		return mapByteBuffer(type, accessType).asShortBuffer();
	}

	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public IntBuffer mapIntBuffer(BufferType type, AccessType accessType)
	{
		return mapByteBuffer(type, accessType).asIntBuffer();
	}

	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public LongBuffer mapLongBuffer(BufferType type, AccessType accessType)
	{
		return mapByteBuffer(type, accessType).asLongBuffer();
	}

	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public FloatBuffer mapFloatBuffer(BufferType type, AccessType accessType)
	{
		return mapByteBuffer(type, accessType).asFloatBuffer();
	}

	/**
	 * Maps the internal data of the current OGLBuffer to a local buffer for
	 * quick modification/read. 
	 * <p>
	 * Please note that the returned Buffer is special in how 
	 * it is used by OpenGL according to the AccessType.
	 * </p>
	 * @param accessType an access hint for the returned buffer.
	 */
	public DoubleBuffer mapDoubleBuffer(BufferType type, AccessType accessType)
	{
		return mapByteBuffer(type, accessType).asDoubleBuffer();
	}

	/**
	 * Unmaps a buffer after it has been mapped and manipulated/read by the calling
	 * client application. Please note that the Buffer that was mapped from this OGLBuffer
	 * will be completely invalidated upon unmapping it.
	 * @return true if unmap successful, false if data corruption occurred on unmap.
	 */
	public boolean unmapBuffer(BufferType type)
	{
		return gl.glUnmapBuffer(type.glValue);
	}

	/**
	 * Unbinds the current buffer.
	 */
	public void unsetBuffer(BufferType type)
	{
		gl.glBindBuffer(type.glValue, 0);
	}

	// TODO: Add Vertex attrib binders.
	
	/**
	 * Draws geometry using the current bound, enabled coordinate arrays/buffers as data.
	 * @param geometryType the geometry type - tells how to interpret the data.
	 * @param offset the starting offset in the bound buffers (in elements).
	 * @param elementCount the number of elements to draw using bound buffers.
	 * NOTE: an element is in terms of array elements, so if the bound buffers describe the coordinates of 4 vertices,
	 * <code>elementCount</code> should be 4.
	 * @see #setBuffer(BufferType, OGLBuffer)
	 */
	public void drawBufferGeometry(GeometryType geometryType, int offset, int elementCount)
	{
		gl.glDrawArrays(geometryType.glValue, offset, elementCount);
		getError();
	}

	/**
	 * Draws geometry using the current bound, enabled coordinate arrays/buffers as data, plus
	 * an element buffer to describe the ordering.
	 * @param geometryType the geometry type - tells how to interpret the data.
	 * @param dataType the data type of the indices in the {@link BufferType#INDICES}-bound buffer (must be an unsigned type).
	 * @param count the amount of element indices to interpret in the {@link BufferType#INDICES}-bound buffer.
	 * @param offset the starting offset in the index buffer (in elements).
	 * @see #setBuffer(BufferType, OGLBuffer)
	 */
	public void drawBufferGeometryElements(GeometryType geometryType, DataType dataType, int count, int offset)
	{
		gl.glDrawElements(geometryType.glValue, count, dataType.glValue, dataType.size * offset);
		getError();
	}	
	
	/**
	 * Draws geometry using the current bound, enabled coordinate arrays/buffers as data, plus
	 * an element buffer to describe the ordering.
	 * @param geometryType the geometry type - tells how to interpret the data.
	 * @param dataType the data type of the indices in the {@link BufferType#INDICES}-bound buffer (must be an unsigned type).
	 * @param startIndex the starting index into the {@link BufferType#INDICES}-bound buffer.
	 * @param endIndex the ending index in the range.
	 * @param count the amount of element indices to read.
	 * @see #setBuffer(BufferType, OGLBuffer)
	 */
	public void drawBufferGeometryElementRange(GeometryType geometryType, DataType dataType, int startIndex, int endIndex, int count)
	{
		gl.glDrawRangeElements(geometryType.glValue, startIndex, endIndex, count, dataType.glValue, 0L);
		getError();
	}	
	
	private static final String CACHE_NAME = "$$"+Cache.class.getCanonicalName();

	// Get the cache.
	private Cache getCache()
	{
		Cache out;
		if ((out = (Cache)Common.getLocal(CACHE_NAME)) == null)
			Common.setLocal(CACHE_NAME, out = new Cache());
		return out;
	}

	private static final class Cache
	{
		private int[] intState;
		private float[] floatState;

		private Cache()
		{
			intsize(32);
			floatsize(32);
		}
		
		void intsize(int len)
		{
			if (len > intState.length)
				intState = new int[len];
		}

		void floatsize(int len)
		{
			if (len > floatState.length)
				floatState = new float[len];
		}
		
	}
	
}
