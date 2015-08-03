/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import java.io.UnsupportedEncodingException;

import javax.media.opengl.*;

import com.blackrook.commons.Common;
import com.blackrook.commons.hash.HashMap;
import com.blackrook.ogl.exception.*;

/**
 * The main shader program class.
 * @author Matthew Tropiano
 */
public class OGLShader extends OGLObject
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
	
	/** Map type names. */
	private static final HashMap<Integer, String> TYPENAMES = new HashMap<Integer, String>() 
	{{
		put(GL2.GL_FLOAT, "float");
		put(GL2.GL_FLOAT_VEC2, "vec2");
		put(GL2.GL_FLOAT_VEC3, "vec3");
		put(GL2.GL_FLOAT_VEC4, "vec4");
		put(GL2.GL_DOUBLE, "double");
		//put(GL2.GL_DOUBLE_VEC2, "dvec2");
		//put(GL2.GL_DOUBLE_VEC3, "dvec3");
		//put(GL2.GL_DOUBLE_VEC4, "dvec4");
		put(GL2.GL_INT, "int");
		put(GL2.GL_INT_VEC2, "ivec2");
		put(GL2.GL_INT_VEC3, "ivec3");
		put(GL2.GL_INT_VEC4, "ivec4");
		put(GL2.GL_UNSIGNED_INT, "unsigned int");
		put(GL2.GL_UNSIGNED_INT_VEC2, "uvec2");
		put(GL2.GL_UNSIGNED_INT_VEC3, "uvec3");
		put(GL2.GL_UNSIGNED_INT_VEC4, "uvec4");
		put(GL2.GL_BOOL, "bool");
		put(GL2.GL_BOOL_VEC2, "bvec2");
		put(GL2.GL_BOOL_VEC3, "bvec3");
		put(GL2.GL_BOOL_VEC4, "bvec4");
		put(GL2.GL_FLOAT_MAT2, "mat2");
		put(GL2.GL_FLOAT_MAT3, "mat3");
		put(GL2.GL_FLOAT_MAT4, "mat4");
		put(GL2.GL_FLOAT_MAT2x3, "mat2x3");
		put(GL2.GL_FLOAT_MAT2x4, "mat2x4");
		put(GL2.GL_FLOAT_MAT3x2, "mat3x2");
		put(GL2.GL_FLOAT_MAT3x4, "mat3x4");
		put(GL2.GL_FLOAT_MAT4x2, "mat4x2");
		put(GL2.GL_FLOAT_MAT4x3, "mat4x3");
		//put(GL2.GL_DOUBLE_MAT2, "dmat2");
		//put(GL2.GL_DOUBLE_MAT3, "dmat3");
		//put(GL2.GL_DOUBLE_MAT4, "dmat4");
		//put(GL2.GL_DOUBLE_MAT2x3, "dmat2x3");
		//put(GL2.GL_DOUBLE_MAT2x4, "dmat2x4");
		//put(GL2.GL_DOUBLE_MAT3x2, "dmat3x2");
		//put(GL2.GL_DOUBLE_MAT3x4, "dmat3x4");
		//put(GL2.GL_DOUBLE_MAT4x2, "dmat4x2");
		//put(GL2.GL_DOUBLE_MAT4x3, "dmat4x3");
		put(GL2.GL_SAMPLER_1D, "sampler1D");
		put(GL2.GL_SAMPLER_2D, "sampler2D");
		put(GL2.GL_SAMPLER_3D, "sampler3D");
		put(GL2.GL_SAMPLER_CUBE, "samplerCube");
		put(GL2.GL_SAMPLER_1D_SHADOW, "sampler1DShadow");
		put(GL2.GL_SAMPLER_2D_SHADOW, "sampler2DShadow");
		put(GL2.GL_SAMPLER_1D_ARRAY, "sampler1DArray");
		put(GL2.GL_SAMPLER_2D_ARRAY, "sampler2DArray");
		put(GL2.GL_SAMPLER_1D_ARRAY_SHADOW, "sampler1DArrayShadow");
		put(GL2.GL_SAMPLER_2D_ARRAY_SHADOW, "sampler2DArrayShadow");
		put(GL2.GL_SAMPLER_2D_MULTISAMPLE, "sampler2DMS");
		put(GL2.GL_SAMPLER_2D_MULTISAMPLE_ARRAY, "sampler2DMSArray");
		put(GL2.GL_SAMPLER_CUBE_SHADOW, "samplerCubeShadow");
		put(GL2.GL_SAMPLER_BUFFER, "samplerBuffer");
		put(GL2.GL_SAMPLER_2D_RECT, "sampler2DRect");
		put(GL2.GL_SAMPLER_2D_RECT_SHADOW, "sampler2DRectShadow");
		put(GL2.GL_INT_SAMPLER_1D, "isampler1D");
		put(GL2.GL_INT_SAMPLER_2D, "isampler2D");
		put(GL2.GL_INT_SAMPLER_3D, "isampler3D");
		put(GL2.GL_INT_SAMPLER_CUBE, "isamplerCube");
		put(GL2.GL_INT_SAMPLER_1D_ARRAY, "isampler1DArray");
		put(GL2.GL_INT_SAMPLER_2D_ARRAY, "isampler2DArray");
		put(GL2.GL_INT_SAMPLER_2D_MULTISAMPLE, "isampler2DMS");
		put(GL2.GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, "isampler2DMSArray");
		put(GL2.GL_INT_SAMPLER_BUFFER, "isamplerBuffer");
		put(GL2.GL_INT_SAMPLER_2D_RECT, "isampler2DRect");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_1D, "usampler1D");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_2D, "usampler2D");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_3D, "usampler3D");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_CUBE, "usamplerCube");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_1D_ARRAY, "usampler2DArray");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_2D_ARRAY, "usampler2DArray");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE, "usampler2DMS");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, "usampler2DMSArray");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_BUFFER, "usamplerBuffer");
		put(GL2.GL_UNSIGNED_INT_SAMPLER_2D_RECT, "usampler2DRect");
		put(GL2.GL_IMAGE_1D, "image1D");
		put(GL2.GL_IMAGE_2D, "image2D");
		put(GL2.GL_IMAGE_3D, "image3D");
		put(GL2.GL_IMAGE_2D_RECT, "image2DRect");
		put(GL2.GL_IMAGE_CUBE, "imageCube");
		put(GL2.GL_IMAGE_BUFFER, "imageBuffer");
		put(GL2.GL_IMAGE_1D_ARRAY, "image1DArray");
		put(GL2.GL_IMAGE_2D_ARRAY, "image2DArray");
		put(GL2.GL_IMAGE_2D_MULTISAMPLE, "image2DMS");
		put(GL2.GL_IMAGE_2D_MULTISAMPLE_ARRAY, "image2DMSArray");
		put(GL2.GL_INT_IMAGE_1D, "iimage1D");
		put(GL2.GL_INT_IMAGE_2D, "iimage2D");
		put(GL2.GL_INT_IMAGE_3D, "iimage3D");
		put(GL2.GL_INT_IMAGE_2D_RECT, "iimage2DRect");
		put(GL2.GL_INT_IMAGE_CUBE, "iimageCube");
		put(GL2.GL_INT_IMAGE_BUFFER, "iimageBuffer");
		put(GL2.GL_INT_IMAGE_1D_ARRAY, "iimage1DArray");
		put(GL2.GL_INT_IMAGE_2D_ARRAY, "iimage2DArray");
		put(GL2.GL_INT_IMAGE_2D_MULTISAMPLE, "iimage2DMS");
		put(GL2.GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY, "iimage2DMSArray");
		put(GL2.GL_UNSIGNED_INT_IMAGE_1D, "uimage1D");
		put(GL2.GL_UNSIGNED_INT_IMAGE_2D, "uimage2D");
		put(GL2.GL_UNSIGNED_INT_IMAGE_3D, "uimage3D");
		put(GL2.GL_UNSIGNED_INT_IMAGE_2D_RECT, "uimage2DRect");
		put(GL2.GL_UNSIGNED_INT_IMAGE_CUBE, "uimageCube");
		put(GL2.GL_UNSIGNED_INT_IMAGE_BUFFER, "uimageBuffer");
		put(GL2.GL_UNSIGNED_INT_IMAGE_1D_ARRAY, "uimage1DArray");
		put(GL2.GL_UNSIGNED_INT_IMAGE_2D_ARRAY, "uimage2DArray");
		put(GL2.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE, "uimage2DMS");
		put(GL2.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY, "uimage2DMSArray");
		put(GL2.GL_UNSIGNED_INT_ATOMIC_COUNTER, "atomic_uint");
	}};
	
	/** Vertex program. */
	private OGLShaderProgramVertex vertexProgram;
	/** Geometry program. */
	private OGLShaderProgramGeometry geometryProgram;
	/** Fragment program. */
	private OGLShaderProgramFragment fragmentProgram;
	
	/* == After link == */
	
	/** The shader log. */
	private String log;
	/** Uniform location hash. */
	private Uniform[] uniformLocationList;
	/** Uniform hash. */
	private HashMap<String, Uniform> uniformMap;
	
	/**
	 * Creates a new Shader. 
	 * Each program can be null and is just left absent in the complete program.
	 * @param programs the programs to attach.
	 */
	OGLShader(OGLGraphics g, OGLShaderProgram ... programs)
	{
		super(g);
		this.vertexProgram = null;
		this.geometryProgram = null;
		this.fragmentProgram = null;

		GL2 gl = g.getGL();

		g.clearError();
		
		// Get programs.
		for (OGLShaderProgram program : programs)
		{
			if (program != null) switch (program.getType())
			{
				case VERTEX:
					if (vertexProgram != null)
						throw new GraphicsException("Vertex program already provided.");
					else
						vertexProgram = (OGLShaderProgramVertex)program;
					break;
					
				case FRAGMENT:
					if (fragmentProgram != null)
						throw new GraphicsException("Fragment program already provided.");
					else
						fragmentProgram = (OGLShaderProgramFragment)program;
					break;
					
				case GEOMETRY:
					if (geometryProgram != null)
						throw new GraphicsException("Geometry program already provided.");
					else
						geometryProgram = (OGLShaderProgramGeometry)program;
					break;
			}
		}
		
		if (Common.coalesce(vertexProgram, fragmentProgram, geometryProgram) == null)
			throw new GraphicsException("All provided programs are null!");
		
		// attach the programs.
		if (vertexProgram != null)
			gl.glAttachShader(getGLId(), vertexProgram.getGLId());
		if (geometryProgram != null)
			gl.glAttachShader(getGLId(), geometryProgram.getGLId());
		if (fragmentProgram != null)
			gl.glAttachShader(getGLId(), fragmentProgram.getGLId());

		// did they link properly?
		link(g);
		g.getError();
		
		uniforms(g);
		g.getError();
	}

	// Links the programs.
	private void link(OGLGraphics g) throws GraphicsException
	{
		GL2 gl = g.getGL();
	    gl.glLinkProgramARB(getGLId());
	    log = readLog(g);
	    gl.glGetObjectParameterivARB(getGLId(), GL2.GL_OBJECT_LINK_STATUS_ARB, glStateNum, 0);
	    if (glStateNum[0] == 0)
	    	throw new GraphicsException("Failed to link together program "+getGLId()+".\n"+log);
	}

	// Pulls log data from shader compiler.
	private String readLog(OGLGraphics g)
	{
		GL2 gl = g.getGL();
		int program = getGLId();
	    gl.glGetObjectParameterivARB(program, GL2.GL_OBJECT_INFO_LOG_LENGTH_ARB, glStateNum, 0);
        if (glStateNum[0] <= 1)
        	return "";
	    byte[] log = new byte[glStateNum[0]];
	    gl.glGetInfoLogARB(program, glStateNum[0], glStateNum, 0, log, 0);
	    return new String(log).substring(0, glStateNum[0]-1);
	}
	
	// Gets the uniform data.
	private void uniforms(OGLGraphics g)
	{
		GL2 gl = g.getGL();
		gl.glGetProgramiv(getGLId(), GL2.GL_ACTIVE_UNIFORMS, glStateNum, 0);
		int uniformCount = glStateNum[0];
		
		int[] length = new int[1];
		int[] size = new int[1];
		int[] type = new int[1];
		final int NAMESIZE = 512;
		byte[] name = new byte[NAMESIZE];
		
		uniformLocationList = new Uniform[uniformCount];
		uniformMap = new HashMap<String, Uniform>(uniformCount, 1.0f);
		
		for (int i = 0; i < uniformCount; i++)
		{
			gl.glGetActiveUniform(getGLId(), i, NAMESIZE, length, 0, size, 0, type, 0, name, 0);
			
			uniformLocationList[i] = new Uniform();
			uniformLocationList[i].locationId = i;
			uniformLocationList[i].length = length[0];
			try {
				uniformLocationList[i].name = new String(name, 0, uniformLocationList[i].length, "UTF-8");
			} catch (UnsupportedEncodingException e) {/* should not happen. */}
			uniformLocationList[i].size = size[0];
			uniformLocationList[i].type = type[0];
			uniformLocationList[i].typeName = TYPENAMES.get(type[0]);
			
			uniformMap.put(uniformLocationList[i].name, uniformLocationList[i]);
		}
		g.clearError();
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		return g.getGL().glCreateProgram();
	}

	@Override
	protected boolean free(OGLGraphics g)
	{
		g.clearError();
		g.getGL().glDeleteProgram(getGLId());
		g.getError();
		return true;
	}
	
	/**
	 * Returns the log from this program's linking.
	 */
	public String getLog()
	{
		return log;
	}
	
	/**
	 * @return the number of uniforms on this shader.
	 */
	public int getUniformCount()
	{
		return uniformLocationList.length;
	}

	/**
	 * Gets a {@link Uniform} by its location id.
	 * @param locationId the location id.
	 * @return the corresponding uniform or null if not found.
	 */
	public Uniform getUniform(int locationId)
	{
		if (locationId < 0 || locationId >= uniformLocationList.length)
			return null;
		return uniformLocationList[locationId];
	}
	
	/**
	 * Gets a {@link Uniform} by uniform name.
	 * @param name the uniform name.
	 * @return the corresponding uniform or null if not found.
	 */
	public Uniform getUniform(String name)
	{
		return uniformMap.get(name);
	}
	
	/**
	 * Destroys undeleted shader programs abandoned from destroyed Java objects.
	 */
	static void destroyUndeleted(OGLGraphics g)
	{
		if (UNDELETED_LENGTH > 0)
		{
			for (int i = 0; i < UNDELETED_LENGTH; i++)
				g.getGL().glDeleteObjectARB(UNDELETED_IDS[i]);
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
	
	/**
	 * Uniform for a shader. 
	 */
	public static class Uniform
	{
		/** Location id. */
		private int locationId;
		/** Uniform name. */
		private String name;
		/** Uniform character length. */
		private int length;
		/** Uniform size (in positions). */
		private int size;
		/** Uniform type (GL value). */
		private int type;
		/** Uniform type. */
		private String typeName;
		
		/** @return the uniform location id. */
		public int getLocationId() 
		{
			return locationId;
		}
		
		/** @return the uniform name. */
		public String getName() 
		{
			return name;
		}
		
		/** @return the length of the uniform. */
		public int getLength()
		{
			return length;
		}
		
		/** @return the size of the uniform. */
		public int getSize() 
		{
			return size;
		}
		
		/** @return the GL type id. */
		public int getType() 
		{
			return type;
		}
		
		/** @return the type name. */
		public String getTypeName() 
		{
			return typeName;
		}
	}

}

