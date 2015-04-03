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

import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class FreeLook extends GameComponent {
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	private float m_sensitivityX;
	private float m_sensitivityY;
	private int m_unlockMouseKey;
	
	public FreeLook(float sensitivity) {
		this(sensitivity, sensitivity, Input.KEY_ESCAPE);
	}
	
	public FreeLook(float sensitivityX, float sensitivityY) {
		this(sensitivityX, sensitivityY, Input.KEY_ESCAPE);
	}
	
	public FreeLook(float sensitivityX, float sensitivityY, int unlockMouseKey) {
		this.m_sensitivityX = sensitivityX;
		this.m_sensitivityY = sensitivityY;
		this.m_unlockMouseKey = unlockMouseKey;
	}
	
	@Override
	public void Input(float delta) {
		if (Input.GetKey(m_unlockMouseKey)) {
			Input.SetCursor(true);
		}
		if (Input.GetMouseDown(0)) {
			Input.SetCursor(false);
		}
		
		if (!Input.GetCursor()) {
			Vector2f deltaPos = Input.GetMouseDynamicPosition();
			boolean rotY = deltaPos.GetX() != 0;
			boolean rotX = deltaPos.GetY() != 0;
			
			if (rotY)
				GetTransform().Rotate(Y_AXIS, (float) Math.toRadians(deltaPos.GetX() * m_sensitivityY));
			if (rotX)
				GetTransform().Rotate(GetTransform().GetRot().GetRight(), (float) Math.toRadians(-deltaPos.GetY() * m_sensitivityX));
		}
	}
}
