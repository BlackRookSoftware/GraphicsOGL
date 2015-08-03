/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.blackrook.commons.Common;
import com.blackrook.ogl.enums.ShaderProgramType;

/**
 * Geometry shader program.
 * @author Matthew Tropiano
 */
public class OGLShaderProgramGeometry extends OGLShaderProgram
{
	/**
	 * Creates a new geometry shader program.
	 * @param g the OGLGraphics instance to use.
	 * @param file the input file.
	 * @throws NullPointerException if file is null.
	 * @throws IOException if the source of the source code can't be read.
	 * @throws FileNotFoundException if the source file does not exist.
	 */
	OGLShaderProgramGeometry(OGLGraphics g, File file) throws IOException
	{
		super(g, ShaderProgramType.GEOMETRY);
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
	OGLShaderProgramGeometry(OGLGraphics g, String streamName, InputStream in) throws IOException
	{
		super(g, ShaderProgramType.GEOMETRY);
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
	OGLShaderProgramGeometry(OGLGraphics g, String streamName, String sourceCode)
	{
		super(g, ShaderProgramType.GEOMETRY);
		construct(g, streamName, sourceCode);
	}

	@Override
	protected int allocate(OGLGraphics g)
	{
		return g.getGL().glCreateShader(ShaderProgramType.GEOMETRY.glShaderType);
	}
	
}
