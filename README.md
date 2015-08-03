# Black Rook OpenGL

Copyright (c) 2014-2015 Black Rook Software. All rights reserved.  
[http://blackrooksoftware.com/projects.htm?name=graphicsogl](http://blackrooksoftware.com/projects.htm?name=graphicsogl)  
[https://github.com/BlackRookSoftware/GraphicsOGL](https://github.com/BlackRookSoftware/GraphicsOGL)

### NOTICE

This library requires the use of third-party libraries. Black Rook Software 
is not responsible for issues regarding these libraries.

This library is currently in **EXPERIMENTAL** status. This library's API
may change many times in different ways over the course of its development!

### Required Libraries

Black Rook Commons 2.20.0+  
[https://github.com/BlackRookSoftware/Common](https://github.com/BlackRookSoftware/Common)

GlueGen  
[http://jogamp.org/git/?p=gluegen.git](http://jogamp.org/git/?p=gluegen.git)

JOGL  
[http://jogamp.org/git/?p=jogl.git](http://jogamp.org/git/?p=jogl.git)

### Introduction

The purpose of the Black Rook OpenGL library is to encapsulate OpenGL's calls (via JOGL)
and structures in an object-oriented fashion that makes them easier to work
with in Java and provide additional means for easy texture and shader loading
and simplifying complex rendering concepts via utility libraries.

This also provides a means for drawing a multi-tier graphics system as well as capturing
input. 

### Library

Contained in this release is a series of classes that encapsulate OpenGL
functions and objects. It is connected to the Java ImageIO Interface
in order to read multiple image file formats and types. Support for additional
file types can be added via adding additional Java ImageIO-compatible decoders
to the classpath.

### Other

This program and the accompanying materials
are made available under the terms of the GNU Lesser Public License v2.1
which accompanies this distribution, and is available at
http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html

A copy of the LGPL should have been included in this release (LICENSE.txt).
If it was not, please contact us for a copy, or to notify us of a distribution
that has not included it. 

JOGL and GlueGen are maintained by [Jogamp.org](http://jogamp.org/). All questions/issues
regarding JOGL and GlueGen should be directed to them.
