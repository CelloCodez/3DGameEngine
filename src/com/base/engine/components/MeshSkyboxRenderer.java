/*
 * Copyright (C) 2016, 2017 CelloCodez
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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;

public class MeshSkyboxRenderer extends GameComponent {
	
	private String[] m_faces;
	
	public MeshSkyboxRenderer(String[] faces) {
		this.m_faces = faces;
	}
	
	/**
	 * face order: right, left, top, bottom, back, front
	 * @param faces
	 * @return
	 */
	@SuppressWarnings("unused")
	private int loadCubemap() {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < 6; i++) {
			CubemapTextureData cubemap = Texture.LoadTextureAsCubemap(m_faces[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, cubemap.getWidth(), cubemap.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, cubemap.getImg());
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		return texID;
	}
	
	@Override
	public void Render(Shader shader, RenderingEngine renderingEngine) {
		//		GL11.glDisable(GL11.GL_CULL_FACE);
		//		shader.Bind();
		//		shader.UpdateUniforms(GetTransform(), m_material, renderingEngine);
		//		//		shader.SetUniformDirectionalLight("R_directionalLight", new DirectionalLight(new Vector3f(1, 1, 1), 10.0f));
		//		m_mesh.Draw();
		//		//		shader.UpdateUniforms(GetTransform(), m_material, renderingEngine);
		//		GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
