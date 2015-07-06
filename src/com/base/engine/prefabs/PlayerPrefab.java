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

import com.base.engine.components.Camera;
import com.base.engine.components.FreeLook;
import com.base.engine.components.FreeMove;
import com.base.engine.core.GameObject;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.CapsuleCollider;
import com.base.engine.rendering.Window;

public class PlayerPrefab extends GameObject {
	
	public PlayerPrefab(String name, Vector3f position) {
		super(name);
		GameObject playerCamera = new GameObject("PrefabPlayerCamera").AddComponent(new FreeLook(0.5f, 0)).AddComponent(
				new Camera(new Matrix4f().InitPerspective((float) Math.toRadians(70.0f), (float) Window.GetWidth() / (float) Window.GetHeight(), 0.01f, 1000.0f)));
		AddComponent(new FreeLook(0, 0.5f));
		AddComponent(new FreeMove(6.0f));
		AddComponent(new PlayerRestrictRotation());
		GetTransform().SetPos(position);
		SetCollider(new CapsuleCollider(10, 1f, 3f));
		AddChild(playerCamera);
	}
	
}
