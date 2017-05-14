/*
 * Copyright (C) 2017 CelloCodez
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

package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class SkyboxComponent extends GameComponent {
	private RenderingEngine m_renderingEngine;
	
	private Vector3f m_offs = null;
	
	@Override
	public void Update(float delta) {
		if (m_renderingEngine != null) {
			if (m_offs == null) {
				m_offs = GetTransform().GetPos().Sub(m_renderingEngine.GetMainCamera().GetTransform().GetPos());
				return;
			}
			GetTransform().SetPos(m_renderingEngine.GetMainCamera().GetTransform().GetTransformedPos().Add(m_offs));
		}
	}
	
	@Override
	public void Render(Shader shader, RenderingEngine renderingEngine) {
		this.m_renderingEngine = renderingEngine;
	}
}
