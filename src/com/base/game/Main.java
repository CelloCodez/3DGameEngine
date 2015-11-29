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

import com.base.engine.core.CoreEngine;
import com.base.engine.physics.PhysicsEngine;

public class Main {
	
	private static CoreEngine engine;
	
	public static void main(String[] args) {
		engine = new CoreEngine(1200, 800, 60, new LoadSceneGame());
		engine.CreateWindow("Modded 3D Game Engine", true);
		engine.Start();
	}
	
	public static PhysicsEngine Physics() {
		return engine.getPhysicsEngine();
	}
	
}
