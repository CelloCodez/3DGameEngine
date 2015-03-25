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

package com.base.game;

import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.FreeLookTurn;
import com.base.engine.components.FreeLookUpDown;
import com.base.engine.components.FreeMove;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.CapsuleCollider;
import com.base.engine.physics.PlaneCollider;
import com.base.engine.prefabs.PlanePrefab;
import com.base.engine.rendering.Window;

public class PhysicsTestGame extends Game {
	public void Init() {
		
		GameObject lightContainer = new GameObject();
		lightContainer.AddComponent(new DirectionalLight(new Vector3f(1f, 1f, 1f), 0.2f));
		lightContainer.GetTransform().SetRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
		AddObject(lightContainer);
		
		GameObject prefab = new PlanePrefab();
		prefab.GetTransform().SetScale(new Vector3f(5, 1, 5));
		prefab.AddComponent(new PlaneCollider(0, 5, 5));
		AddObject(prefab);
		
		GameObject playerCamera = new GameObject().AddComponent(new FreeLookUpDown(0.5f)).AddComponent(
				new Camera(new Matrix4f().InitPerspective((float) Math.toRadians(70.0f), (float) Window.GetWidth() / (float) Window.GetHeight(), 0.01f, 1000.0f)));
		GameObject player = new GameObject();
		player.AddComponent(new FreeLookTurn(0.5f));
		player.AddComponent(new FreeMove(5.0f));
		player.AddChild(playerCamera);
		player.GetTransform().SetPos(new Vector3f(0, 6, 0));
		player.AddComponent(new CapsuleCollider(2, 1, 3));
		AddObject(player);
	}
}
