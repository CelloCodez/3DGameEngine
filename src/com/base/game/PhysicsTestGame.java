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

import com.base.engine.components.DirectionalLight;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector3f;
import com.base.engine.prefabs.CubePrefab;
import com.base.engine.prefabs.PlanePrefab;
import com.base.engine.prefabs.PlayerPrefab;
import com.base.engine.util.SceneLoader;

public class PhysicsTestGame extends Game {
	public void Init() {
		
		SetRootObject(SceneLoader.loadScene("./res/scenes/test/scene.xml"));
		
//		GameObject lightContainer = new GameObject("Lights");
//		lightContainer.AddComponent(new DirectionalLight(new Vector3f(1f, 1f, 1f), 0.2f));
//		lightContainer.GetTransform().SetRot(new Quaternion(new Vector3f(-45, 0, 0)));
//		AddObject(lightContainer);
//		
//		GameObject planePrefab = new PlanePrefab("Plane", 5, 5, 0f);
//		AddObject(planePrefab);
//		
//		GameObject cube = new CubePrefab("FallingCube", 1, 1, 1, 10f);
//		cube.GetTransform().SetPos(new Vector3f(2f, 100f, 2f));
//		cube.GetTransform().SetRot(new Quaternion(new Vector3f(40f, 45f, 15f)));
//		AddObject(cube);
//		
//		GameObject player = new PlayerPrefab("Player", new Vector3f(0, 10, 0));
//		AddObject(player);
	}
}
