/*
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

package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Time;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class SineFloatComponent extends GameComponent {

	private RenderingEngine m_renderingEngine;

	private Vector3f m_old = null;

	@Override
	public void Update(float delta) {
		if (m_renderingEngine != null) {
			if (m_old == null) {
				m_old = GetTransform().GetPos();
			} else {
				GetTransform().SetPos(
						m_old.Add(new Vector3f(0f, (float) Math.sin(Time
								.GetTime()) * 30f * delta, 0f)));
			}
		}
	}

	@Override
	public void Render(Shader shader, RenderingEngine renderingEngine) {
		this.m_renderingEngine = renderingEngine;
	}

}
