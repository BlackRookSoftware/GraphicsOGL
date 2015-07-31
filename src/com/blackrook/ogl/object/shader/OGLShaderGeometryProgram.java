/*******************************************************************************
 * Copyright (c) 2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.object.shader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.*;

import com.blackrook.commons.Common;
import com.blackrook.ogl.OGLGraphics;

/**
 * Geometry shader program.
 * @author Matthew Tropiano
 */
public class OGLShaderGeometryProgram extends OGLShaderPipelineProgram
{
	/**
	 * Creates a new geometry shader program.
	 * @param g the OGLGraphics instance to use.
	 * @param file the input file.
	 * @throws NullPointerException if file is null.
	 * @throws IOException if the source of the source code can't be read.
	 * @throws FileNotFoundException if the source file does not exist.
	 */
	public OGLShaderGeometryProgram(OGLGraphics g, File file) throws IOException
	{
		super(g);
		FileInputStream fi = new FileInputStream(file);
		String sourceCode = Common.getTextualContents(fi, "UTF-8");
		construct(g, file.getPath(), sourceCode);
		fi.close();
	}

	/**
	 * Creates a new geometry shader program.
	 * @param g the OGLGraphics instance to use.
	 * @param streamName the name of this stream.
	 * @param in the input stream.
	 * @throws NullPointerException if streamName or in is null.
	 * @throws IOException if the source of the source code can't be read.
	 */
	public OGLShaderGeometryProgram(OGLGraphics g, String streamName, InputStream in) throws IOException
	{
		super(g);
		String sourceCode = Common.getTextualContents(in, "UTF-8");
		construct(g, streamName, sourceCode);
	}

	/**
	 * Creates a new geometry shader program.
	 * @param g the OGLGraphics instance to use.
	 * @param streamName the name of the originating stream.
	 * @param sourceCode the code to compile.
	 * @throws NullPointerException if streamName or in is null.
	 */
	public OGLShaderGeometryProgram(OGLGraphics g, String streamName, String sourceCode)
	{
		super(g);
		construct(g, streamName, sourceCode);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		glType = GL2.GL_GEOMETRY_PROGRAM_NV;
		return g.getGL().glCreateShader(glType);
	}
	
}
