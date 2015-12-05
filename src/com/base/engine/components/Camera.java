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

package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Window;

public class Camera extends GameComponent {
	
	private Matrix4f m_projection;
	
	private float m_fovInDegrees = 70;
	
	public Camera(Matrix4f projection) {
		this.m_projection = projection;
	}
	
	public Camera(float fovInDegrees, float zNear, float zFar) {
		this(new Matrix4f().InitPerspective((float) Math.toRadians(fovInDegrees), (float) Window.GetWidth() / (float) Window.GetHeight(), zNear, zFar));
		this.m_fovInDegrees = fovInDegrees;
	}
	
	public Matrix4f GetViewProjection() {
		Matrix4f cameraRotation = GetTransform().GetTransformedRot().Conjugate().ToRotationMatrix();
		Vector3f cameraPos = GetTransform().GetTransformedPos().Mul(-1);
		
		Matrix4f cameraTranslation = new Matrix4f().InitTranslation(cameraPos.GetX(), cameraPos.GetY(), cameraPos.GetZ());
		
		return m_projection.Mul(cameraRotation.Mul(cameraTranslation));
	}
	
	@Override
	public void AddToEngine(CoreEngine engine) {
		engine.GetRenderingEngine().AddCamera(this);
	}
	
	public float GetFovInDegrees() {
		return m_fovInDegrees;
	}
	
	public Matrix4f GetProjectionMatrix() {
		return m_projection;
	}
	
}
