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

import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.FreeLook;
import com.base.engine.components.FreeMove;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.CapsuleCollider;
import com.base.engine.prefabs.CubePrefab;
import com.base.engine.prefabs.PlanePrefab;
import com.base.engine.rendering.Window;

public class PhysicsTestGame extends Game {
	public void Init() {
		
		GameObject lightContainer = new GameObject("Lights");
		lightContainer.AddComponent(new DirectionalLight(new Vector3f(1f, 1f, 1f), 0.2f));
		lightContainer.GetTransform().SetRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
		AddObject(lightContainer);
		
		GameObject prefab = new PlanePrefab("Plane", 5, 5);
		AddObject(prefab);
		// This does nothing as the default PlanePrefab mass is 0,
		// but it is just to show how to set things in components
		// and to prove that it works in case of an error.
		prefab.GetCollider().SetMass(1f);
		
		GameObject cube = new CubePrefab("FallingCube", 1, 1, 1);
		cube.GetTransform().SetPos(new Vector3f(2f, 5f, 2f));
		cube.GetCollider().SetMass(2f);
		AddObject(cube);
		
		GameObject playerCamera = new GameObject("Camera").AddComponent(new FreeLook(0.5f, 0)).AddComponent(
				new Camera(new Matrix4f().InitPerspective((float) Math.toRadians(70.0f), (float) Window.GetWidth() / (float) Window.GetHeight(), 0.01f, 1000.0f)));
		GameObject player = new GameObject("Player");
		player.AddComponent(new FreeLook(0, 0.5f));
		player.AddComponent(new FreeMove(5.0f));
		player.GetTransform().SetPos(new Vector3f(0, 6, 0));
		player.SetCollider(new CapsuleCollider(2, 1, 3));
		player.AddChild(playerCamera);
		AddObject(player);
	}
}
