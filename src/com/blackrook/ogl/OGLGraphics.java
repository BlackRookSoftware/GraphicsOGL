/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.blackrook.commons.hash.CaseInsensitiveHash;
import com.blackrook.commons.math.Matrix4F;
import com.blackrook.commons.math.RMath;
import com.blackrook.ogl.data.*;
import com.blackrook.ogl.enums.*;
import com.blackrook.ogl.exception.GraphicsException;
import com.blackrook.ogl.object.framebuffer.OGLFrameBuffer;
import com.blackrook.ogl.object.framebuffer.OGLFrameRenderBuffer;
import com.blackrook.ogl.object.shader.OGLShaderProgram;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * A graphics toolkit-type of implementation of OpenGL.
 * All calls made to this object change the attached context.
 * @author Matthew Tropiano
 */
public class OGLGraphics
{	
	/** GL 1.0 - 3.0 Interface. */
	private GL2 gl;
	/** GLU Interface. */
	private GLU glu;
	/** GLUT Interface. */
	private GLUT glut;
	
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
	
	/** Maximum bindable lights. */
	private int maxLights;
	/** Maximum texture image units. */
	private int maxTextureImageUnits;
	/** Maximum texture size. */
	private int maxTextureSize;
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
	
	private int[] INT_STATE;
	private float[] FLOAT_STATE;
	
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
	
	/** Current display list. */
	private OGLDisplayList currentDisplayList; 
	/** Current running occlusion query. */
	private OGLOcclusionQuery currentOcclusionQuery;

	/**
	 * Creates a new OGLGraphics context.
	 * @param system the source system.
	 * @param drawable the JOGL auto-drawable context.
	 */
	OGLGraphics(OGLSystem system, GLAutoDrawable drawable)
	{
		glSystem = system;

		gl = (GL2)drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		
		getError();
		
		glRenderer = gl.glGetString(GL2.GL_RENDERER);
		glVersion = gl.glGetString(GL2.GL_VERSION);
		glVendor = gl.glGetString(GL2.GL_VENDOR);
		glExtensions = gl.glGetString(GL2.GL_EXTENSIONS);

		getError();
		
		if (glExtensions == null)
			throw new GraphicsException("Couldn't get list of extensions from OpenGL.");
		String[] exts = glExtensions.toLowerCase().split("\\s+");
		extensionList = new CaseInsensitiveHash();
		for (String s : exts)
			extensionList.put(s);

		INT_STATE = new int[32];
		FLOAT_STATE = new float[32];

		setArch();
		setExtVars();
		
		currentFrame = 0L;
		currentTimeStepMillis = -1f;
	}

	/**
	 * Called at the beginning of each display() call for each
	 * frame.
	 * @param interpolant the interpolant factor to use.
	 */
	void beginFrame()
	{
		currentMilliseconds = System.currentTimeMillis();
		currentNanos = System.nanoTime();
		
		if (currentTimeStepMillis < 0.0f)
			currentTimeStepMillis = 0.0f; 
		else
		{
			long n = currentNanos - lastTimeNanos;
			currentTimeStepMillis = (float)((double)(n)/1000000d); 
		}

		lastTimeNanos = currentNanos;
		currentBlitBit = !currentBlitBit;
		currentFrame++;
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

		maxLights = getGLInt(GL2.GL_MAX_LIGHTS);
		maxTextureImageUnits = getGLInt(GL2.GL_MAX_TEXTURE_IMAGE_UNITS_ARB);
		maxTextureSize = getGLInt(GL2.GL_MAX_TEXTURE_SIZE);
		maxRenderBufferSize = getGLInt(GL2.GL_MAX_RENDERBUFFER_SIZE);
		maxRenderBufferColorAttachments = getGLInt(GL2.GL_MAX_COLOR_ATTACHMENTS);
		if (occlusionQueryExtensionPresent)
		{
			gl.glGetQueryiv(GL2.GL_SAMPLES_PASSED, GL2.GL_QUERY_COUNTER_BITS, INT_STATE, 0);
			queryCounterBitDepth = INT_STATE[0];
		}
		getGLFloats(GL2.GL_POINT_SIZE_RANGE, FLOAT_STATE);
		minPointSize = FLOAT_STATE[0];
		maxPointSize = FLOAT_STATE[1];
		getGLFloats(GL2.GL_LINE_WIDTH_RANGE, FLOAT_STATE);
		minLineWidth = FLOAT_STATE[0];
		maxLineWidth = FLOAT_STATE[1];
	}

	/**
	 * Queries OpenGL for an integer value.
	 * @param glEnum the OpenGL enumerant.
	 */
	protected final int getGLInt(int glEnum)
	{
		gl.glGetIntegerv(glEnum, INT_STATE, 0);
		return INT_STATE[0];
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
		gl.glGetIntegerv(glEnum, INT_STATE, 0);
		System.arraycopy(INT_STATE, 0, out, 0, Math.min(INT_STATE.length, out.length));
	}
	
	/**
	 * Queries OpenGL for a floating-point value.
	 * @param glEnum the OpenGL enumerant.
	 */
	protected final float getGLFloat(int glEnum)
	{
		gl.glGetFloatv(glEnum, FLOAT_STATE, 0);
		return FLOAT_STATE[0];
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
		gl.glGetFloatv(glEnum, FLOAT_STATE, 0);
		System.arraycopy(FLOAT_STATE, 0, out, 0, Math.min(FLOAT_STATE.length, out.length));
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
	 * Enables/disables an OpenGL client state bit.
	 * @param glEnum the OpenGL enumerant.
	 * @param flag if true, enable. if false, disable.
	 */
	protected final void glClientFlagSet(int glEnum, boolean flag)
	{
		if (flag)
			gl.glEnableClientState(glEnum);
		else
			gl.glDisableClientState(glEnum);
	}
	
	/**
	 * Converts a Java boolean to an OpenGL GL_TRUE or GL_FALSE value.
	 */
	protected final int toGLBool(boolean val)
	{
		return val ? GL2.GL_TRUE : GL2.GL_FALSE;
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
	 * Returns the maximum amount of lights. 
	 */
	public final int getMaxLights()
	{
		return maxLights;
	}

	/**
	 * Get the maximum amount of texture units in a multitexturing pipeline.
	 */
	public final int getMaxTextureImageUnits()
	{
		return maxTextureImageUnits;
	}

	/**
	 * Get max texture size in pixels.
	 */
	public final int getMaxTextureSize()
	{
		return maxTextureSize;
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

	/**
	 * Gets the current GL context.
	 */
	public final GL2 getGL()
	{
		return gl;
	}

	/**
	 * Gets the current GLU context.
	 */
	public final GLU getGLU()
	{
		return glu;
	}

	/**
	 * Gets the current GLUT context.
	 */
	public final GLUT getGLUT()
	{
		return glut;
	}

	/** Returns a reference to the parent {@link OGLSystem} that created this. */
	public final OGLSystem getSystem()
	{
		return glSystem;
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
		while (gl.glGetError() != GL2.GL_NO_ERROR);
	}

	/**
	 * Tests for an OpenGL error via glGetError(). 
	 * If one is raised, this throws a GraphicsException with the error message.
	 */
	public void getError()
	{
		int error = gl.glGetError();
		if (error != GL2.GL_NO_ERROR)
			throw new GraphicsException("OpenGL raised error: "+glu.gluErrorString(error));
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
	 * Set light shading type.
	 */
	public void setShadeType(LightShadeType shade)
	{
		gl.glShadeModel(shade.glValue);
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
	 * Sets the maximum size for the diameter of Point geometry when
	 * it is attenuated by point distance from the "camera".
	 */
	public void setPointAttenuationMaximum(float size)
	{
		gl.glPointParameterf(GL2.GL_POINT_SIZE_MAX, size);
	}

	/**
	 * Sets the minimum size for the diameter of Point geometry when
	 * it is attenuated by point distance from the "camera".
	 */
	public void setPointAttenuationMinimum(float size)
	{
		gl.glPointParameterf(GL2.GL_POINT_SIZE_MIN, size);
	}

	/**
	 * Sets the attenuation formula to use when changing the sizes
	 * of points based on their location in space.
	 * @param constant the formula constant coefficient.
	 * @param linear the formula linear coefficient.
	 * @param quadratic the formula quadratic coefficient.
	 */
	public void setPointAttenuationFormula(float constant, float linear, float quadratic)
	{
		FLOAT_STATE[0] = constant;
		FLOAT_STATE[1] = linear;
		FLOAT_STATE[2] = quadratic;
		gl.glPointParameterfv(GL2.GL_POINT_DISTANCE_ATTENUATION, FLOAT_STATE, 0);
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
	 * Sets the current matrix for matrix operations.
	 * Note that other commands may change this mode automatically.
	 * @param mode the matrix mode to set.
	 */
	public void matrixMode(MatrixType mode)
	{
		gl.glMatrixMode(mode.glValue);
	}

	/**
	 * Loads the identity matrix into the current selected matrix.
	 */
	public void matrixReset()
	{
		gl.glLoadIdentity();
	}

	/**
	 * Loads a matrix's contents into the current selected matrix.
	 */
	public void matrixLoad(Matrix4F matrix)
	{
		gl.glLoadMatrixf(matrix.getArray(), 0);
	}

	/**
	 * Multiplies a matrix into the current selected matrix.
	 */
	public void matrixMultiply(Matrix4F matrix)
	{
		gl.glMultMatrixf(matrix.getArray(), 0);
	}

	/**
	 * Pushes a copy of the current matrix onto the current selected stack.
	 */
	public void matrixPush()
	{
		gl.glPushMatrix();
	}

	/**
	 * Pops the current matrix off of the current selected stack.
	 */
	public void matrixPop()
	{
		gl.glPopMatrix();
	}

	/**
	 * Translates the current matrix by a set of units.
	 * This is applied via multiplication with the current matrix.
	 */
	public void matrixTranslate(float x, float y, float z)
	{
		gl.glTranslatef(x, y, z);
	}

	/**
	 * Rotates the current matrix by an amount of DEGREES around the X-Axis.
	 * This is applied via multiplication with the current matrix.
	 */
	public void matrixRotateX(float degrees)
	{
		gl.glRotatef(degrees, 1, 0, 0);
	}

	/**
	 * Rotates the current matrix by an amount of DEGREES around the Y-Axis.
	 * This is applied via multiplication with the current matrix.
	 */
	public void matrixRotateY(float degrees)
	{
		gl.glRotatef(degrees, 0, 1, 0);
	}

	/**
	 * Rotates the current matrix by an amount of DEGREES around the Z-Axis.
	 * This is applied via multiplication with the current matrix.
	 */
	public void matrixRotateZ(float degrees)
	{
		gl.glRotatef(degrees, 0, 0, 1);
	}

	/**
	 * Scales the current matrix by a set of scalars that 
	 * correspond to each axis.
	 * This is applied via multiplication with the current matrix.
	 */
	public void matrixScale(float x, float y, float z)
	{
		gl.glScalef(x, y, z);
	}

	/**
	 * Multiplies the current matrix by a symmetric perspective projection matrix.
	 * @param fov front of view angle in degrees.
	 * @param aspect the aspect ratio, usually view width over view height.
	 * @param near the near clipping plane on the Z-Axis.
	 * @param far the far clipping plane on the Z-Axis.
	 */
	public void matrixPerpective(float fov, float aspect, float near, float far)
	{
		glu.gluPerspective(fov, aspect, near, far);
	}

	/**
	 * Multiplies the current matrix by a frustum projection matrix.
	 * @param left the left clipping plane on the X-Axis.
	 * @param right the right clipping plane on the X-Axis.
	 * @param bottom the bottom clipping plane on the Y-Axis.
	 * @param top the upper clipping plane on the Y-Axis.
	 * @param near the near clipping plane on the Z-Axis.
	 * @param far the far clipping plane on the Z-Axis.
	 */
	public void matrixFrustum(float left, float right, float bottom, float top, float near, float far)
	{
		gl.glFrustum(left, right, bottom, top, near, far);
	}

	/**
	 * Multiplies the current matrix by an orthographic projection matrix.
	 * @param left the left clipping plane on the X-Axis.
	 * @param right the right clipping plane on the X-Axis.
	 * @param bottom the bottom clipping plane on the Y-Axis.
	 * @param top the upper clipping plane on the Y-Axis.
	 * @param near the near clipping plane on the Z-Axis.
	 * @param far the far clipping plane on the Z-Axis.
	 */
	public void matrixOrtho(float left, float right, float bottom, float top, float near, float far)
	{
		gl.glOrtho(left, right, bottom, top, near, far);
	}

	/**
	 * Pushes a series of attributes onto the attribute stack.
	 * @param attribs the list of attributes to preserve.
	 */
	public void attribPush(AttribType ... attribs)
	{
		int bits = 0;
		for (AttribType at : attribs)
			bits |= at.glValue;
		gl.glPushAttrib(bits);
	}

	/**
	 * Restores attributes from the attribute stack.
	 */
	public void attribPop()
	{
		gl.glPopAttrib();
	}

	/**
	 * Pushes a series of attributes onto the attribute stack.
	 * @param attribs the list of attributes to preserve.
	 */
	public void clientAttribPush(ClientAttribType ... attribs)
	{
		int bits = 0;
		for (ClientAttribType cat : attribs)
			bits |= cat.glValue;
		gl.glPushClientAttrib(bits);
	}

	/**
	 * Restores attributes from the attribute stack.
	 */
	public void clientAttribPop()
	{
		gl.glPopClientAttrib();
	}

	/**
	 * Sets the current color used for drawing polygons and other geometry.
	 * @param c the color to use.
	 */
	public void setColor(OGLColor c)
	{
		setColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	/**
	 * Sets the current color used for drawing polygons and other geometry.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setColor(float red, float green, float blue, float alpha)
	{
		gl.glColor4f(red, green, blue, alpha);
	}

	/**
	 * Sets the current color used for drawing polygons and other geometry using
	 * an ARGB integer.
	 * @param argb the 32-bit color as an integer.
	 */
	public void setColorARGB(int argb)
	{
		gl.glColor4ub(
			(byte)((argb >>> 16) & 0x0ff),
			(byte)((argb >>> 8) & 0x0ff),
			(byte)(argb & 0x0ff),
			(byte)((argb >>> 24) & 0x0ff)
		);
	}

	/**
	 * Sets if lighting is enabled.
	 */
	public void setLightingEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_LIGHTING, flag);
	}

	/**
	 * Sets if certain lights are enabled.
	 */
	public void setLightEnabled(int sourceId, boolean flag)
	{
		glFlagSet(GL2.GL_LIGHT0 + sourceId, flag);
	}

	/**
	 * Sets the current light used for illuminating polygons and other geometry.
	 * This light will set all properties.
	 * @param sourceId the light source id. this cannot exceed the maximum number of lights
	 * that OpenGL can handle.
	 * @param light the Light to use.
	 */
	public void setLight(int sourceId, OGLLight light)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		
		setLightPosition(sourceId, light.getXPosition(), light.getYPosition(), light.getZPosition(), light.getWPosition());
		setLightAmbientColor(sourceId, light.getAmbientColor());
		setLightDiffuseColor(sourceId, light.getDiffuseColor());
		setLightSpecularColor(sourceId, light.getSpecularColor());
		setLightAttenuation(sourceId, light.getConstantAttenuation(), light.getLinearAttenuation(), light.getQuadraticAttenuation());
	}

	/**
	 * Sets the current light attenuation used for illuminating polygons and other geometry.
	 * This alters light intensity at varying distances from the light.
	 * @param sourceId the light source id. this cannot exceed the maximum number of lights
	 * that OpenGL can handle.
	 * @param constant the constant coefficient.
	 * @param linear the linear coefficient.
	 * @param quadratic the quadratic coefficient.
	 */
	public void setLightAttenuation(int sourceId, float constant, float linear, float quadratic)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		
		gl.glLightf(GL2.GL_LIGHT0 + sourceId, GL2.GL_CONSTANT_ATTENUATION, constant);
		gl.glLightf(GL2.GL_LIGHT0 + sourceId, GL2.GL_LINEAR_ATTENUATION, linear);
		gl.glLightf(GL2.GL_LIGHT0 + sourceId, GL2.GL_QUADRATIC_ATTENUATION, quadratic);
	}

	/**
	 * Sets the color for a specular component for a light. 
	 * @param sourceId the light source id.
	 * @param color the color to use.
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightSpecularColor(int sourceId, OGLColor color)
	{
		setLightSpecularColor(sourceId, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the color for a specular component for a light. 
	 * @param sourceId the light source id.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightSpecularColor(int sourceId, float red, float green, float blue, float alpha)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glLightfv(GL2.GL_LIGHT0 + sourceId, GL2.GL_AMBIENT, FLOAT_STATE, 0);
	}

	/**
	 * Sets the color for a diffuse component for a light. 
	 * @param sourceId the light source id.
	 * @param color the color to use.
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightDiffuseColor(int sourceId, OGLColor color)
	{
		setLightDiffuseColor(sourceId, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the color for a diffuse component for a light. 
	 * @param sourceId the light source id.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightDiffuseColor(int sourceId, float red, float green, float blue, float alpha)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glLightfv(GL2.GL_LIGHT0 + sourceId, GL2.GL_DIFFUSE, FLOAT_STATE, 0);
	}

	/**
	 * Sets the color for a ambient component for a light. 
	 * @param sourceId the light source id.
	 * @param color the color to use.
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightAmbientColor(int sourceId, OGLColor color)
	{
		setLightAmbientColor(sourceId, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the color for a ambient component for a light. 
	 * @param sourceId the light source id.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightAmbientColor(int sourceId, float red, float green, float blue, float alpha)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glLightfv(GL2.GL_LIGHT0 + sourceId, GL2.GL_SPECULAR, FLOAT_STATE, 0);
	}

	/**
	 * Sets the position of a light source. 
	 * @param sourceId the light source id.
	 * @param x the x-axis position.
	 * @param y the y-axis position.
	 * @param z the z-axis position.
	 * @param w if 0, the light is a directional one. If nonzero, positional.
	 * @throws GraphicsException if the specified sourceId is not a valid one.
	 * @see OGLGraphics#getMaxLights()
	 */
	public void setLightPosition(int sourceId, float x, float y, float z, float w)
	{
		if (sourceId < 0 || sourceId >= maxLights)
			throw new GraphicsException("Not a valid light id.");
		FLOAT_STATE[0] = x;
		FLOAT_STATE[1] = y;
		FLOAT_STATE[2] = z;
		FLOAT_STATE[3] = w;
		gl.glLightfv(GL2.GL_LIGHT0 + sourceId, GL2.GL_POSITION, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material used for drawing polygons and other geometry.
	 * Depending on what colors are set on the Material object, not all of the
	 * material calls will be made. This applies the Material properties to both
	 * polygon sides, and will remain doing so until this is changed.
	 * @param material the material to use.
	 */
	public void setMaterial(OGLMaterial material)
	{
		setMaterial(FaceSide.FRONT_AND_BACK, material);
	}

	/**
	 * Sets the current material used for drawing polygons and other geometry,
	 * and will remain doing so until this is changed.
	 * Depending on what colors are set on the Material object, not all of the
	 * material calls will be made. 
	 * @param faceside the face side to apply these properties to.
	 * @param material the material to use.
	 */
	public void setMaterial(FaceSide faceside, OGLMaterial material)
	{
		if (material.getAmbientColor() != null)
			setMaterialAmbientColor(faceside, material.getAmbientColor());
		if (material.getDiffuseColor() != null)
			setMaterialDiffuseColor(faceside, material.getDiffuseColor());
		if (material.getSpecularColor() != null)
			setMaterialSpecularColor(faceside, material.getSpecularColor());
		if (material.getEmissionColor() != null)
			setMaterialEmissionColor(faceside, material.getEmissionColor());
		setMaterialShininessFactor(faceside, material.getShininess());
	}

	/**
	 * Sets the current material ambient color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param color the color to use.
	 */
	public void setMaterialAmbientColor(FaceSide faceside, OGLColor color)
	{
		setMaterialAmbientColor(faceside, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the current material ambient color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setMaterialAmbientColor(FaceSide faceside, float red, float green, float blue, float alpha)
	{
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glMaterialfv(faceside.glValue, GL2.GL_AMBIENT, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material diffuse color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param color the color to use.
	 */
	public void setMaterialDiffuseColor(FaceSide faceside, OGLColor color)
	{
		setMaterialDiffuseColor(faceside, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the current material diffuse color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setMaterialDiffuseColor(FaceSide faceside, float red, float green, float blue, float alpha)
	{
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glMaterialfv(faceside.glValue, GL2.GL_DIFFUSE, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material specular color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param color	the color to use.
	 */
	public void setMaterialSpecularColor(FaceSide faceside, OGLColor color)
	{
		setMaterialSpecularColor(faceside, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the current material specular color used for drawing polygons and other geometry.
	 * @param faceside the face side to apply these properties to.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setMaterialSpecularColor(FaceSide faceside, float red, float green, float blue, float alpha)
	{
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glMaterialfv(faceside.glValue, GL2.GL_SPECULAR, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material emission color used for drawing polygons and other geometry.
	 * @param faceside	the face side to apply these properties to.
	 * @param color			the color to use.
	 */
	public void setMaterialEmissionColor(FaceSide faceside, OGLColor color)
	{
		color.getRGBA(FLOAT_STATE);
		gl.glMaterialfv(faceside.glValue, GL2.GL_EMISSION, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material emission color used for drawing polygons and other geometry.
	 * @param faceside	the face side to apply these properties to.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setMaterialEmissionColor(FaceSide faceside, float red, float green, float blue, float alpha)
	{
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glMaterialfv(faceside.glValue, GL2.GL_EMISSION, FLOAT_STATE, 0);
	}

	/**
	 * Sets the current material shininess factor used for drawing polygons and other geometry.
	 * As this number gets higher,
	 * @param faceside the face side to apply these properties to.
	 * @param f the factor.
	 */
	public void setMaterialShininessFactor(FaceSide faceside, float f)
	{
		gl.glMaterialf(faceside.glValue, GL2.GL_SHININESS, f);		
	}

	/**
	 * Sets if fog rendering is enabled or disabled. 
	 */
	public void setFogEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_FOG, flag);
	}
	
	/**
	 * Sets the origin of the calculation of the fog coordinate value that
	 * dictates "where" in the fog it is.
	 * @param coord the coordinate type.
	 */
	public void setFogCoordinateSource(FogCoordinateType coord)
	{
		gl.glFogi(GL2.GL_FOG_COORD_SRC, coord.glValue);
	}

	/**
	 * Sets most fog attributes at once.
	 * @param color the color of the fog.
	 * @param formula the formula to use.
	 * @param density the density factor to use.
	 * @param start the unit of space for the fog start (before that is no fog).
	 * @param end the unit of space for the fog end (after that is solid color).
	 * @see OGLGraphics#setFogColor(OGLColor)
	 * @see OGLGraphics#setFogFormula(FogFormulaType)
	 * @see OGLGraphics#setFogDensity(float)
	 * @see OGLGraphics#setFogStart(float)
	 * @see OGLGraphics#setFogEnd(float)
	 */
	public void setFog(OGLColor color, FogFormulaType formula, float density, float start, float end)
	{
		setFogColor(color);
		setFogFormula(formula);
		setFogDensity(density);
		setFogStart(start);
		setFogEnd(end);
	}
	
	/**
	 * Sets the color of the fog.
	 * @param color the color of the fog.
	 */
	public void setFogColor(OGLColor color)
	{
		setFogColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	/**
	 * Sets the color of the fog.
	 * @param red the red component of the color to use (0 to 1).
	 * @param green the green component of the color to use (0 to 1).
	 * @param blue the blue component of the color to use (0 to 1).
	 * @param alpha the alpha component of the color to use (0 to 1).
	 */
	public void setFogColor(float red, float green, float blue, float alpha)
	{
		FLOAT_STATE[0] = red;
		FLOAT_STATE[1] = green;
		FLOAT_STATE[2] = blue;
		FLOAT_STATE[3] = alpha;
		gl.glFogfv(GL2.GL_FOG_COLOR, FLOAT_STATE, 0);
	}

	/**
	 * Sets the distance calculation formula for calculating fog cover. 
	 * @param formula the formula to use.
	 */
	public void setFogFormula(FogFormulaType formula)
	{
		gl.glFogi(GL2.GL_FOG_MODE, formula.glValue);
	}
	
	/**
	 * Sets the density factor for calculating fog.
	 * Only works for the exponential formulas.
	 * @param density the density factor to use.
	 */
	public void setFogDensity(float density)
	{
		gl.glFogf(GL2.GL_FOG_DENSITY, density);
	}
	
	/**
	 * Sets the starting point for calculating fog.
	 * The value passed in is from the eye.
	 * @param start the unit of space for the fog start (before that is no fog).
	 */
	public void setFogStart(float start)
	{
		gl.glFogf(GL2.GL_FOG_START, start);
	}
	
	/**
	 * Sets the starting point for calculating fog.
	 * The value passed in is from the eye.
	 * @param end the unit of space for the fog end (after that is solid color).
	 */
	public void setFogEnd(float end)
	{
		gl.glFogf(GL2.GL_FOG_END, end);
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
	 * Enables/Disables smooth point geometry.
	 */
	public void setPointSmoothingEnabled(boolean enabled)
	{
		glFlagSet(GL2.GL_POINT_SMOOTH, enabled);
	}

	/**
	 * Enables/Disables point sprite conversion.
	 * Internally, OpenGL will convert point geometry into billboarded quads or
	 * actual polygonal information internally. 
	 */
	public void setPointSpritesEnabled(boolean enabled)
	{
		glFlagSet(GL2.GL_POINT_SPRITE, enabled);
	}

	/**
	 * Enables/Disables line smoothing.
	 * "Line smoothing" is a fancy term for anti-aliasing.
	 */
	public void setLineSmoothingEnabled(boolean enabled)
	{
		glFlagSet(GL2.GL_LINE_SMOOTH, enabled);
	}

	/**
	 * Clears a bunch of framebuffers.
	 * @param color		clear the color buffer?
	 * @param depth		clear the depth buffer?
	 * @param accum		clear the accumulation buffer?
	 * @param stencil	clear the stencil buffer?
	 */
	public void clearBuffers(boolean color, boolean depth, boolean accum, boolean stencil)
	{
		gl.glClear(
				(color? GL2.GL_COLOR_BUFFER_BIT : 0) | 
				(accum? GL2.GL_ACCUM_BUFFER_BIT : 0) | 
				(depth? GL2.GL_DEPTH_BUFFER_BIT : 0) | 
				(stencil? GL2.GL_STENCIL_BUFFER_BIT : 0)
				);
	}

	/**
	 * Sets the current buffer to read from for pixel read/copy operations.
	 * By default, this is the BACK buffer in double-buffered contexts.
	 * @param b	the buffer to read from now on.
	 * @throws IllegalArgumentException if b is NONE or FRONT_AND_BACK.
	 */
	public void setBufferRead(FrameBufferType b)
	{
		if (b == FrameBufferType.FRONT_AND_BACK || b == FrameBufferType.NONE)
			throw new IllegalArgumentException("The read buffer can't be NONE nor FRONT AND BACK");
		gl.glReadBuffer(b.glValue);
	}

	/**
	 * Sets the current buffer to write to for pixel drawing/rasterizing operations.
	 * By default, this is the BACK buffer in double-buffered contexts.
	 * @param b	the buffer to write to from now on.
	 * @throws IllegalArgumentException if b is NONE or FRONT_AND_BACK.
	 */
	public void setBufferWrite(FrameBufferType b)
	{
		if (b == FrameBufferType.FRONT_AND_BACK || b == FrameBufferType.NONE)
			throw new IllegalArgumentException("The read buffer can't be NONE nor FRONT AND BACK");
		gl.glDrawBuffer(b.glValue);
	}

	/**
	 * Sets if the depth test is enabled or not for incoming fragments.
	 * @param flag	the new value of the flag.
	 */
	public void setDepthTestEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_DEPTH_TEST,flag);
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
		glFlagSet(GL2.GL_STENCIL_TEST,flag);
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
	public void setStencilTestDepthFail(
			StencilTestFunc stencilFail, StencilTestFunc stencilDepthFail, StencilTestFunc stencilDepthPass)
	{
		gl.glStencilOp(stencilFail.glValue, stencilDepthFail.glValue, stencilDepthPass.glValue);
	}

	/**
	 * Sets if the scissor test is enabled
	 */
	public void setScissorTestEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_SCISSOR_TEST,flag);
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
		glFlagSet(GL2.GL_BLEND,flag);
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
		glFlagSet(GL2.GL_CULL_FACE,flag);
	}

	/**
	 * Sets the face side(s) that are culled if face culling is enabled.
	 */
	public void setFaceCullingSide(FaceSide side)
	{
		gl.glCullFace(side.glValue);
	}

	/**
	 * Sets the next raster position for drawing bitmaps.
	 * Remember, (0,0) is the lower left edge of the window.
	 * @param x	the screen x-coordinate.
	 * @param y	the screen y-coordinate.
	 * @param z	the screen z-coordinate.
	 */
	public void setRasterPosition(int x, int y, float z)
	{
		gl.glRasterPos3f(x, y, z);
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
	 * Prints a message to the screen in the style of C's printf using
	 * the GLUTFont.BITMAP_8_BY_13 font.
	 * Remember to set color, and raster position before executing this method.
	 * This will change light enabling and some client array states.
	 * @param formatString the printf-formatted string to print.
	 * @param args printf arguments.
	 */
	public void printf(String formatString, Object ... args)
	{
		printf(GLUTFont.BITMAP_8_BY_13, formatString, args);
	}

	/**
	 * Prints a message to the screen in the style of C's printf.
	 * Remember to set color, then raster position before executing this method.
	 * This will change light enabling and some client array states.
	 * @param font the GLUTFont to use for printing this string.
	 * @param formatString the printf-formatted string to print (see {@link String#format(String, Object...)}).
	 * @param args printf arguments.
	 */
	public void printf(GLUTFont font, String formatString, Object ... args)
	{
		print(font, String.format(formatString, args));
	}

	/**
	 * Prints a message to the screen using the GLUTFont.BITMAP_8_BY_13 font.
	 * Remember to set color, and raster position before executing this method.
	 * This will change light enabling and some client array states.
	 * @param message the message string to print.
	 */
	public void print(String message)
	{
		print(GLUTFont.BITMAP_8_BY_13, message);
	}

	/**
	 * Prints a message to the screen.
	 * Remember to set color, and raster position before executing this method.
	 * This will change light enabling and some client array states.
	 * @param font the GLUTFont to use for printing this string.
	 * @param message the message string to print.
	 */
	public void print(GLUTFont font, String message)
	{
		glut.glutBitmapString(font.glutValue, message);
	}

	/**
	 * Draws a Bitmap at the current raster position and increments the raster position.
	 * @param b			the Bitmap to draw ((0,0) is the lower-left).
	 * @param offsetX	the offset from the current raster position, x-coordinate.
	 * @param offsetY	the offset from the current raster position, y-coordinate.
	 * @param incX		what to increment the raster position x-coordinate by after the draw.
	 * @param incY  	what to increment the raster position y-coordinate by after the draw.
	 */
	public void drawBitmap(OGLBitmap b, float offsetX, float offsetY, float incX, float incY)
	{
		gl.glBitmap(b.getWidth(), b.getHeight(), offsetX, offsetY, incX, incY, b.getBytes(), 0);
	}

	/**
	 * Sets if 1D texturing is enabled or not.
	 */
	public void setTexture1DEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_1D,flag);
	}

	/**
	 * Sets if 2D texturing is enabled or not.
	 */
	public void setTexture2DEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_2D,flag);
	}

	/**
	 * Sets if cube map texturing is enabled or not.
	 */
	public void setTextureCubeEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_CUBE_MAP,flag);
	}

	/**
	 * Sets the Level Of Detail bias for automatic texture mipmapping.
	 * @param bias	the bias value.
	 */
	public void setTextureLODBias(float bias)
	{
		gl.glTexEnvf(GL2.GL_TEXTURE_FILTER_CONTROL, GL2.GL_TEXTURE_LOD_BIAS, bias);
	}

	/**
	 * Sets the texture environment mode to use for texel fragment coloring.
	 * This is usually REPLACE, by default.
	 * @param mode	the texture mode.
	 */
	public void setTextureEnvironment(TextureMode mode)
	{
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, mode.glValue);
	}

	/**
	 * Sets if texture coordinates are to be generated across point geometry
	 * dimensions. Useful for Point Sprites, obviously.
	 */
	public void setPointSpriteTexCoordGeneration(boolean val)
	{
		gl.glTexEnvi(GL2.GL_POINT_SPRITE, GL2.GL_COORD_REPLACE, toGLBool(val));
	}

	/**
	 * Sets if texture coordinates are to be automatically generated
	 * for the S coordinate axis (usually width).
	 */
	public void setTexGenSEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_GEN_S,flag);
	}

	/**
	 * Sets if texture coordinates are to be automatically generated
	 * for the T coordinate axis (usually height).
	 */
	public void setTexGenTEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_GEN_T,flag);
	}

	/**
	 * Sets if texture coordinates are to be automatically generated
	 * for the R coordinate axis (usually depth).
	 */
	public void setTexGenREnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_GEN_R,flag);
	}

	/**
	 * Sets if texture coordinates are to be automatically generated
	 * for the Q coordinate axis (I have no idea what the hell this could be).
	 */
	public void setTexGenQEnabled(boolean flag)
	{
		glFlagSet(GL2.GL_TEXTURE_GEN_Q,flag);
	}

	/**
	 * Sets how texture coordinates are to be automatically generated.
	 * @param coord		the texture coordinate to set the mode for.
	 * @param mode		the generation function.
	 */
	public void setTexGenMode(TextureCoordType coord, TextureGenMode mode)
	{
		gl.glTexGeni(coord.glValue, GL2.GL_TEXTURE_GEN_MODE, mode.glValue);
	}

	/**
	 * Sets the eye plane equation for generating coordinates using the eye method.
	 * @param coord	the texture coordinate to set the mode for.
	 * @param a		the plane A coordinate coefficient.
	 * @param b		the plane B coordinate coefficient.
	 * @param c		the plane C coordinate coefficient.
	 * @param d		the plane D coordinate coefficient.
	 */
	public void setTexGenEyePlane(TextureCoordType coord, float a, float b, float c, float d)
	{
		FLOAT_STATE[0] = a;
		FLOAT_STATE[1] = b;
		FLOAT_STATE[2] = c;
		FLOAT_STATE[3] = d;
		gl.glTexGenfv(coord.glValue, GL2.GL_EYE_PLANE, FLOAT_STATE, 0);
	}

	/**
	 * Sets the object plane equation for generating coordinates using the object method.
	 * @param coord	the texture coordinate to set the mode for.
	 * @param a		the plane A coordinate coefficient.
	 * @param b		the plane B coordinate coefficient.
	 * @param c		the plane C coordinate coefficient.
	 * @param d		the plane D coordinate coefficient.
	 */
	public void setTexGenObjectPlane(TextureCoordType coord, float a, float b, float c, float d)
	{
		FLOAT_STATE[0] = a;
		FLOAT_STATE[1] = b;
		FLOAT_STATE[2] = c;
		FLOAT_STATE[3] = d;
		gl.glTexGenfv(coord.glValue, GL2.GL_OBJECT_PLANE, FLOAT_STATE, 0);
	}

	/**
	 * Sets if normal vectors are generated automatically when geometry is submitted to
	 * the OpenGL geometry pipeline.
	 */
	public void setAutoNormalGen(boolean flag)
	{
		glFlagSet(GL2.GL_AUTO_NORMAL, flag);
	}

	/**
	 * Sets the current "active" texture unit for texture bindings and texture environment settings.
	 * @param unit the texture unit to switch to.
	 */
	public void setTextureUnit(int unit)
	{
		if (unit < 0 || unit > maxTextureImageUnits)
			throw new GraphicsException("Illegal texture unit. Must be from 0 to "+(maxTextureImageUnits-1)+".");
		gl.glActiveTexture(GL2.GL_TEXTURE0 + unit);
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
	 * @param texture the texture to bind.
	 */
	public void setTexture1D(OGLTexture texture)
	{
		gl.glBindTexture(GL2.GL_TEXTURE_1D, texture.getGLId());
	}
	
	/**
	 * Sets the current filtering for the current 1D texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 * @param genMipmaps if this generates mipmaps automatically.
	 */
	public void setTextureFiltering1D(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy, boolean genMipmaps)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
    	gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_GENERATE_MIPMAP, genMipmaps ? GL2.GL_TRUE : GL2.GL_FALSE);
	}
	
	/**
	 * Sets the current wrapping for the current 1D texture.
	 * @param wrapS the wrapping mode, S-axis.
	 */
	public void setTextureWrapping1D(TextureWrapType wrapS)
	{
		gl.glTexParameteri(GL2.GL_TEXTURE_1D, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
	}

	/**
	 * Sends a texture into OpenGL's memory for the current 1D texture.
	 * @param image the image to send.
	 * @param format the internal format.
	 * @param border the pixel border to add, if any.
	 */
	public void setTextureData1D(BufferedImage image, TextureFormat format, int border)
	{
		if (image.getWidth() > getMaxTextureSize() || image.getHeight() > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");
		
		Buffer buffer = null;
		int imgWidth = image.getWidth();
		
		if (!OGLGraphicUtils.hasPowerOfTwoDimensions(image))
		{
			imgWidth = RMath.closestPowerOfTwo(image.getWidth());
			buffer = OGLGraphicUtils.getByteData(OGLGraphicUtils.performResizeBilinear(image, imgWidth, 1)); 
		}
		else
		{
			buffer = OGLGraphicUtils.getByteData(image);	
		}
		
		clearError();
		gl.glTexImage1D(
			GL2.GL_TEXTURE_1D,
			0,
			format.glid, 
			imgWidth,
			border,
			GL2.GL_BGRA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
		getError();
	}

	/**
	 * Sends a subset of data to the currently-bound 1D texture already in OpenGL's memory.
	 * @param image the image to send.
	 * @param xoffs the texel offset.
	 */
	public void setTextureSubData1D(BufferedImage image, int xoffs)
	{
		Buffer buffer = OGLGraphicUtils.getByteData(image);
		clearError();
		gl.glTexSubImage1D(
			GL2.GL_TEXTURE_1D,
			0,
			xoffs,
			image.getWidth(),
			GL2.GL_RGBA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
		getError();
	}

	/**
	 * Unbinds a one-dimensional texture from the current texture unit.
	 */
	public void unsetTexture1D()
	{
		gl.glBindTexture(GL2.GL_TEXTURE_1D, 0);
	}

	/**
	 * Binds a 2D texture object to the current active texture unit.
	 * @param texture the texture to bind.
	 */
	public void setTexture2D(OGLTexture texture)
	{
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getGLId());
	}

	/**
	 * Sets the current filtering for the current 2D texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 * @param genMipmaps if this generates mipmaps automatically.
	 */
	public void setTextureFiltering2D(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy, boolean genMipmaps)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, genMipmaps ? GL2.GL_TRUE : GL2.GL_FALSE);
	}
	
	/**
	 * Sets the current wrapping for the current 2D texture.
	 * @param wrapS the wrapping mode, S-axis.
	 * @param wrapT the wrapping mode, T-axis.
	 */
	public void setTextureWrapping2D(TextureWrapType wrapS, TextureWrapType wrapT)
	{
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, wrapT.glid);
	}
	
	/**
	 * Sends a texture into OpenGL's memory for the current 2D texture.
	 * @param image the image to send.
	 * @param format the internal format.
	 * @param border the pixel border to add, if any.
	 */
	public void setTextureData2D(BufferedImage image, TextureFormat format, int border)
	{
		if (image.getWidth() > getMaxTextureSize() || image.getHeight() > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");
		
		Buffer buffer = null;
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		if (!OGLGraphicUtils.hasPowerOfTwoDimensions(image))
		{
			imgWidth = RMath.closestPowerOfTwo(image.getWidth());
			imgHeight = RMath.closestPowerOfTwo(image.getHeight());
			buffer = OGLGraphicUtils.getByteData(OGLGraphicUtils.performResizeBilinear(image, imgWidth, imgHeight)); 
		}
		else
		{
			buffer = OGLGraphicUtils.getByteData(image);	
		}

		gl.glTexImage2D(
			GL2.GL_TEXTURE_2D,
			0,
			format.glid, 
			imgWidth,
			imgHeight,
			border,
			GL2.GL_BGRA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
	}
	
	/**
	 * Sends a subset of data to the currently-bound 2D texture already in OpenGL's memory.
	 * @param image the image to send.
	 * @param xoffs the texel offset.
	 * @param yoffs the texel offset.
	 */
	public void setTextureSubData2D(BufferedImage image, int xoffs, int yoffs)
	{
		Buffer buffer = OGLGraphicUtils.getByteData(image);
		gl.glTexSubImage2D(
			GL2.GL_TEXTURE_2D,
			0,
			xoffs,
			yoffs,
			image.getWidth(),
			image.getHeight(),
			GL2.GL_BGRA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
	}
	
	/**
	 * Unbinds a two-dimensional texture from the current texture unit.
	 */
	public void unsetTexture2D()
	{
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}

	/**
	 * Binds a texture cube object to the current active texture unit.
	 * @param texture the texture to bind.
	 */
	public void setTextureCube(OGLTexture texture)
	{
		gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, texture.getGLId());
	}

	/**
	 * Sets the current filtering for the current CubeMap texture.
	 * @param minFilter the minification filter.
	 * @param magFilter the magnification filter.
	 * @param anisotropy the anisotropic filtering (2.0 or greater to enable, 1.0 is "off").
	 * @param genMipmaps if this generates mipmaps automatically.
	 */
	public void setTextureFilteringCube(TextureMinFilter minFilter, TextureMagFilter magFilter, float anisotropy, boolean genMipmaps)
	{
		anisotropy = anisotropy < 1.0f ? 1.0f : anisotropy;
    	gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER, magFilter.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MIN_FILTER, minFilter.glid);
		gl.glTexParameterf(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropy);
    	gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_GENERATE_MIPMAP, genMipmaps ? GL2.GL_TRUE : GL2.GL_FALSE);
	}

	/**
	 * Sets the current wrapping for the current CubeMap texture.
	 * @param wrapS the wrapping mode, S-axis.
	 * @param wrapT the wrapping mode, T-axis.
	 * @param wrapR the wrapping mode, R-axis.
	 */
	public void setTextureWrappingCube(TextureWrapType wrapS, TextureWrapType wrapT, TextureWrapType wrapR)
	{
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, wrapS.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, wrapT.glid);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, wrapR.glid);
	}
	
	/**
	 * Sends a texture into OpenGL's memory for the current CubeMap texture.
	 * @param face the cube face to set.
	 * @param image the image to send.
	 * @param format the internal format.
	 * @param border the pixel border to add, if any.
	 */
	public void setTextureDataCube(TextureCubeFace face, BufferedImage image, TextureFormat format, int border)
	{
		if (image.getWidth() > getMaxTextureSize() || image.getHeight() > getMaxTextureSize())
			throw new GraphicsException("Texture is too large. Maximum size is "+getMaxTextureSize()+" pixels.");
		
		Buffer buffer = null;
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();

		if (!OGLGraphicUtils.hasPowerOfTwoDimensions(image))
		{
			imgWidth = RMath.closestPowerOfTwo(image.getWidth());
			imgHeight = RMath.closestPowerOfTwo(image.getHeight());
			buffer = OGLGraphicUtils.getByteData(OGLGraphicUtils.performResizeBilinear(image, imgWidth, imgHeight)); 
		}
		else
		{
			buffer = OGLGraphicUtils.getByteData(image);	
		}

		gl.glTexImage2D(
			face.glValue,
			0,
			format.glid, 
			imgWidth,
			imgHeight,
			border,
			GL2.GL_BGRA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
	}
	
	/**
	 * Sends a subset of data to the currently-bound CubeMap texture already in OpenGL's memory.
	 * @param face the cube face to set.
	 * @param image the image to send.
	 * @param xoffs the texel offset.
	 * @param yoffs the texel offset.
	 */
	public void setTextureSubDataCube(TextureCubeFace face, BufferedImage image, int xoffs, int yoffs)
	{
		Buffer buffer = OGLGraphicUtils.getByteData(image);
		gl.glTexSubImage2D(
			face.glValue,
			0,
			xoffs,
			yoffs,
			image.getWidth(),
			image.getHeight(),
			GL2.GL_BGRA,
			GL2.GL_UNSIGNED_BYTE,
			buffer
		);
	}
	
	/**
	 * Unbinds a texture cube from the current texture unit.
	 */
	public void unsetTextureCube()
	{
		gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP,0);
	}

	/**
	 * Binds a shader to the current context.
	 * @param shader the shader to bind.
	 */
	public void setShaderProgram(OGLShaderProgram shader)
	{
		gl.glUseProgramObjectARB(shader.getGLId());
	}
	
	/**
	 * Unbinds a shader from the current context.
	 */
	public void unsetShaderProgram()
	{
		gl.glUseProgramObjectARB(0);
	}

	/**
	 * Binds a FrameBuffer for rendering.
	 * @param frameBuffer the framebuffer to set as the current one.
	 */
	public void setFrameBuffer(OGLFrameBuffer frameBuffer)
	{
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, frameBuffer.getGLId());
	}

	/**
	 * Unbinds a FrameBuffer for rendering.
	 * The current buffer will then be the default target buffer.
	 */
	public void unsetFrameBuffer()
	{
		gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
	}

	/**
	 * Binds a FrameRenderBuffer from the current context.
	 * @param frameRenderBuffer the render buffer to bind to the current framebuffer.
	 */
	public void setFrameRenderBuffer(OGLFrameRenderBuffer frameRenderBuffer)
	{
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, frameRenderBuffer.getGLId());
	}

	/**
	 * Unbinds a FrameRenderBuffer from the current context.
	 */
	public void unsetFrameRenderBuffer()
	{
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, 0);
	}

	/**
	 * Creates a new display list.
	 * @return a new, uninitialized display list object.
	 * @throws GraphicsException if the object could not be created.
	 */
	public OGLDisplayList createList()
	{
		return new OGLDisplayList(this);
	}
	
	/**
	 * Initializes a display list for compiling draw commands.
	 * While one list is started, another one cannot be.
	 * NOTE: Not all calls to OGLGraphics can be encapsulated in a list!
	 * @param list the list object to start.
	 * @throws GraphicsException if another list has already been started, but not ended.
	 */
	public void startList(OGLDisplayList list)
	{
		if (currentDisplayList != null)
			throw new GraphicsException("A list has already been started.");
		clearError();
		gl.glNewList(list.getGLId(), GL2.GL_COMPILE);
		getError();
		currentDisplayList = list;
	}
	
	/**
	 * Ends the current list for compiling draw commands.
	 * Cannot end a list without starting one.
	 * @throws GraphicsException if another list has not been started, 
	 * or this is called when a different list was started.
	 */
	public void endList()
	{
		if (currentDisplayList == null)
			throw new GraphicsException("Attempt to end list without starting one.");
		gl.glEndList();
		currentDisplayList = null;
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
		gl.glBeginQuery(GL2.GL_SAMPLES_PASSED, query.getGLId());
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
		gl.glEndQuery(GL2.GL_SAMPLES_PASSED);
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
	 * @param buffer the buffer to bind.
	 */
	public void setBuffer(BufferType type, OGLBuffer buffer)
	{
		gl.glBindBuffer(type.glValue, buffer.getGLId());
	}

	/**
	 * Sets the capacity of the current buffer (sends no data).
	 * @param type the buffer type binding.
	 * @param cachingHint the caching hint on this buffer's data.
	 * @param dataType the data type.
	 * @param elements the amount of elements of the data type.
	 */
	public void setBufferCapacity(BufferType type, CachingHint cachingHint, DataType dataType, int elements)
	{
		gl.glBufferData(type.glValue, elements * dataType.size, null, cachingHint.glValue);
		getError();
	}
	
	/**
	 * Sets the data of the current buffer.
	 * @param type the buffer type binding.
	 * @param cachingHint the caching hint on this buffer's data.
	 * @param data the data to send.
	 */
	public void setBufferData(BufferType type, CachingHint cachingHint, Buffer data)
	{
		gl.glBufferData(type.glValue, data.capacity(), data, cachingHint.glValue);
		getError();
	}
	
	/**
	 * Sets a subsection of data to the current buffer.
	 * @param type the buffer type binding.
	 * @param data the data to send.
	 * @param size the amount of data to send.
	 * @param offset the offset into the buffer to copy.
	 */
	public void setBufferSubData(BufferType type, Buffer data, int size, int offset)
	{
		gl.glBufferSubData(type.glValue, offset, size, data);
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

	/**
	 * Copies the contents of the current read frame buffer into a two-dimensional texture.
	 * @param texture	the texture object.
	 * @param texlevel	the mipmapping level to copy this into (0 is normal, no mipmapping).
	 * @param xoffset	the offset in pixels on this texture (x-coordinate) to put this texture data.
	 * @param yoffset	the offset in pixels on this texture (y-coordinate) to put this texture data.
	 * @param srcX		the screen-aligned x-coordinate of what to grab from the buffer (0 is the left side of the screen).
	 * @param srcY		the screen-aligned y-coordinate of what to grab from the buffer (0 is the bottom of the screen).
	 * @param width		the width of the screen in pixels to grab.
	 * @param height	the height of the screen in pixels to grab.
	 */
	public void copyBufferToCurrentTexture2D(OGLTexture texture, int texlevel, int xoffset, int yoffset, int srcX, int srcY, int width, int height)
	{
		gl.glCopyTexSubImage2D(GL2.GL_TEXTURE_2D, texlevel, xoffset, yoffset, srcX, srcY, width, height);
	}

	/**
	 * Tests for frame buffer completeness on the bound buffer. 
	 * If incomplete, this throws a GraphicsException with the error message.
	 */
	public void checkFrameBufferStatus()
	{
		int status = gl.glCheckFramebufferStatus(GL2.GL_FRAMEBUFFER);
		String errorString = null;
		if (status != GL2.GL_FRAMEBUFFER_COMPLETE) 
		{
			switch (status)
			{
				case GL2.GL_FRAMEBUFFER_UNSUPPORTED:
					errorString = "Framebuffer object format is unsupported by the video hardware.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
					errorString = "Incomplete attachment.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
					errorString = "Incomplete missing attachment.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS:
					errorString = "Incomplete dimensions.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_FORMATS:
					errorString = "Incomplete formats.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
					errorString = "Incomplete draw buffer.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
					errorString = "Incomplete read buffer.";
					break;
				case GL2.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
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
	 * Enables or disables the processing of bound vertex arrays and/or buffers.
	 */
	public void setVertexArrayEnabled(boolean flag)
	{
		glClientFlagSet(GL2.GL_VERTEX_ARRAY,flag);
	}

	/**
	 * Enables or disables the processing of bound vertex arrays and/or buffers at a specific attrib index.
	 */
	public void setVertexAttribArrayEnabled(int index, boolean flag)
	{
		if (flag)
			gl.glEnableVertexAttribArray(index);
		else
			gl.glDisableVertexAttribArray(index);
	}

	/**
	 * Enables or disables the processing of bound texture coordinate arrays.
	 */
	public void setTextureCoordArrayEnabled(boolean flag)
	{
		glClientFlagSet(GL2.GL_TEXTURE_COORD_ARRAY,flag);
	}

	/**
	 * Enables or disables the processing of bound vertex color arrays.
	 */
	public void setColorArrayEnabled(boolean flag)
	{
		glClientFlagSet(GL2.GL_COLOR_ARRAY,flag);
	}

	/**
	 * Enables or disables the processing of bound surface normal arrays.
	 */
	public void setNormalArrayEnabled(boolean flag)
	{
		glClientFlagSet(GL2.GL_NORMAL_ARRAY,flag);
	}

	/**
	 * Draws geometry using the current bound, enabled coordinate arrays/buffers as data.
	 * @param geo			the geometry type - tells how to interpret the data.
	 * @param offset		the starting offset in the bound arrays.
	 * @param numElements	the number of elements to draw using bound arrays.
	 * 						NOTE: an element is in terms of array elements, so if
	 * 						the bound arrays describe the coordinates of 4 vertices,
	 * 						numElements should be 4.
	 */
	public void drawUsingBoundArrays(GeometryType geo, int offset, int numElements)
	{
		gl.glDrawArrays(geo.glValue, offset, numElements);
	}

}
