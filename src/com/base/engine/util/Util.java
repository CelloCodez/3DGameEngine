/*
 * Copyright (C) 2014 Benny Bobaganoosh
 * Copyright (C) 2015 CelloCodez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.engine.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Vertex;

public class Util {
	
	public static Vector3f fromLWJGLVector3(org.lwjgl.util.vector.Vector3f vec3) {
		return new Vector3f(vec3.x, vec3.y, vec3.z);
	}
	
	public static Matrix4f lwjglInvert(Matrix4f m) {
		return fromLWJGLMatrix4f((org.lwjgl.util.vector.Matrix4f) toLWJGLMatrix4f(m).invert());
	}
	
	public static org.lwjgl.util.vector.Matrix4f toLWJGLMatrix4f(Matrix4f m) {
		org.lwjgl.util.vector.Matrix4f res = new org.lwjgl.util.vector.Matrix4f();
		res.m00 = m.GetM()[0][0];
		res.m01 = m.GetM()[0][1];
		res.m02 = m.GetM()[0][2];
		res.m03 = m.GetM()[0][3];
		res.m10 = m.GetM()[1][0];
		res.m11 = m.GetM()[1][1];
		res.m12 = m.GetM()[1][2];
		res.m13 = m.GetM()[1][3];
		res.m20 = m.GetM()[2][0];
		res.m21 = m.GetM()[2][1];
		res.m22 = m.GetM()[2][2];
		res.m23 = m.GetM()[2][3];
		res.m30 = m.GetM()[3][0];
		res.m31 = m.GetM()[3][1];
		res.m32 = m.GetM()[3][2];
		res.m33 = m.GetM()[3][3];
		return res;
	}
	
	public static Matrix4f fromLWJGLMatrix4f(org.lwjgl.util.vector.Matrix4f m) {
		Matrix4f res = new Matrix4f();
		float[][] rm = new float[4][4];
		rm[0][0] = m.m00;
		rm[0][1] = m.m01;
		rm[0][2] = m.m02;
		rm[0][3] = m.m03;
		rm[1][0] = m.m10;
		rm[1][1] = m.m11;
		rm[1][2] = m.m12;
		rm[1][3] = m.m13;
		rm[2][0] = m.m20;
		rm[2][1] = m.m21;
		rm[2][2] = m.m22;
		rm[2][3] = m.m23;
		rm[3][0] = m.m30;
		rm[3][1] = m.m31;
		rm[3][2] = m.m32;
		rm[3][3] = m.m33;
		res.SetM(rm);
		return res;
	}
	
	public static Vector2f fromJavaxVector2f(javax.vecmath.Vector2f old) {
		return new Vector2f(old.x, old.y);
	}
	
	public static Vector3f fromJavaxVector3f(javax.vecmath.Vector3f old) {
		return new Vector3f(old.x, old.y, old.z);
	}
	
	public static Quaternion fromJavaxQuat4f(javax.vecmath.Quat4f old) {
		return new Quaternion(old.x, old.y, old.z, old.w);
	}
	
	public static FloatBuffer CreateFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer CreateIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}
	
	public static ByteBuffer CreateByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
	public static IntBuffer CreateFlippedBuffer(int... values) {
		IntBuffer buffer = CreateIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer CreateFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = CreateFloatBuffer(vertices.length * Vertex.SIZE);
		
		for (int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].GetPos().GetX());
			buffer.put(vertices[i].GetPos().GetY());
			buffer.put(vertices[i].GetPos().GetZ());
			buffer.put(vertices[i].GetTexCoord().GetX());
			buffer.put(vertices[i].GetTexCoord().GetY());
			buffer.put(vertices[i].GetNormal().GetX());
			buffer.put(vertices[i].GetNormal().GetY());
			buffer.put(vertices[i].GetNormal().GetZ());
			buffer.put(vertices[i].GetTangent().GetX());
			buffer.put(vertices[i].GetTangent().GetY());
			buffer.put(vertices[i].GetTangent().GetZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer CreateFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = CreateFloatBuffer(4 * 4);
		
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				buffer.put(value.Get(i, j));
		
		buffer.flip();
		
		return buffer;
	}
	
	public static String[] RemoveEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();
		
		for (int i = 0; i < data.length; i++)
			if (!data[i].equals(""))
				result.add(data[i]);
		
		String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static int[] ToIntArray(Integer[] data) {
		int[] result = new int[data.length];
		
		for (int i = 0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
	
}
