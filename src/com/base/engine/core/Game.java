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

package com.base.engine.core;

import com.base.engine.physics.PhysicsEngine;
import com.base.engine.rendering.RenderingEngine;

public abstract class Game {
	private GameObject m_root;
	
	public void Init() {
	}
	
	public void DebugOutputScene() {
		if (m_root == null)
			m_root = new GameObject("_root");
		
		m_root.DebugOutputScene(0);
	}
	
	public void Input(float delta) {
		GetRootObject().InputAll(delta);
	}
	
	public void Update(float delta) {
		GetRootObject().UpdateAll(delta);
	}
	
	public void Render(RenderingEngine renderingEngine) {
		renderingEngine.Render(GetRootObject());
	}
	
	public void AddObject(GameObject object) {
		GetRootObject().AddChild(object);
	}
	
	private GameObject GetRootObject() {
		if (m_root == null)
			m_root = new GameObject("_root");
		
		return m_root;
	}
	
	/**
	 * Mainly for loading scenes
	 * @param newRoot
	 */
	protected void SetRootObject(GameObject newRoot) {
		if (newRoot == null) {
			m_root = new GameObject("_root");
		} else {
			m_root = newRoot;
		}
	}
	
	public void SetEngine(CoreEngine engine) {
		GetRootObject().SetEngine(engine);
	}
	
	public void UpdateCollidersToJBullet(PhysicsEngine physicsEngine) {
		GetRootObject().AllColliderUpdateToJBullet(physicsEngine);
	}
	
	public void UpdateCollidersToGame() {
		GetRootObject().AllColliderUpdateToGame();
	}
	
	public void InitializeColliders(PhysicsEngine physicsEngine) {
		GetRootObject().InitializeColliders(physicsEngine);
	}
}
