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

package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.rendering.Window;

public class FreeLookUpDown extends GameComponent {
	
	private boolean m_mouseLocked = false;
	private float m_sensitivity;
	private int m_unlockMouseKey;
	
	public FreeLookUpDown(float sensitivity) {
		this(sensitivity, Input.KEY_ESCAPE);
	}
	
	public FreeLookUpDown(float sensitivity, int unlockMouseKey) {
		this.m_sensitivity = sensitivity;
		this.m_unlockMouseKey = unlockMouseKey;
	}
	
	@Override
	public void Input(float delta) {
		Vector2f centerPosition = new Vector2f(Window.GetWidth() / 2, Window.GetHeight() / 2);
		
		if (Input.GetKey(m_unlockMouseKey)) {
			Input.SetCursor(true);
			m_mouseLocked = false;
		}
		if (Input.GetMouseDown(0)) {
			Input.SetMousePosition(centerPosition);
			Input.SetCursor(false);
			m_mouseLocked = true;
		}
		
		if (m_mouseLocked) {
			Vector2f deltaPos = Input.GetMousePosition().Sub(centerPosition);
			
			boolean rotX = deltaPos.GetY() != 0;
			
			if (rotX)
				GetTransform().Rotate(GetTransform().GetRot().GetRight(), (float) Math.toRadians(-deltaPos.GetY() * m_sensitivity));
			
			if (rotX)
				Input.SetMousePosition(centerPosition);
		}
	}
}
