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

package com.base.engine.prefabs;

import javax.vecmath.Vector3f;

import com.base.engine.components.GameComponent;

public class PlayerRestrictRotation extends GameComponent {
	
	public PlayerRestrictRotation() {
	}
	
	@Override
	public void Update(float delta) {
		// good enough until I can figure out more functions for quaternions to keep them upright...
		Vector3f curVel = new Vector3f(0, 0, 0);
		GetParent().GetCollider().GetRigidbody().getAngularVelocity(curVel);
		GetParent().GetCollider().GetRigidbody().setAngularVelocity(new Vector3f(0, curVel.y, 0));
	}
}
