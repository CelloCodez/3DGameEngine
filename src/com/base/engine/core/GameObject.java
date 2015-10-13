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

import java.util.ArrayList;

import com.base.engine.components.GameComponent;
import com.base.engine.physics.Collider;
import com.base.engine.physics.PhysicsEngine;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class GameObject {
	private String m_name;
	private ArrayList<GameObject> m_children;
	private ArrayList<GameComponent> m_components;
	private Transform m_transform;
	private CoreEngine m_engine;
	private Collider m_collider = null;
	
	public GameObject(String name) {
		m_children = new ArrayList<GameObject>();
		m_components = new ArrayList<GameComponent>();
		m_transform = new Transform();
		m_engine = null;
		m_name = name;
	}
	
	public void DebugOutputScene(int level) {
		String out = "-";
		for (int i = 0; i < level; i++) {
			out += "-";
		}
		out += m_name;
		System.out.println(out);
		for (GameObject go : m_children) {
			go.DebugOutputScene(level + 1);
		}
	}
	
	public void SetCollider(Collider col) {
		m_collider = col;
		m_collider.SetParentTransform(m_transform);
		m_collider.SetParent(this);
	}
	
	public Collider GetCollider() {
		return m_collider;
	}
	
	public GameComponent GetComponent(Class<? extends GameComponent> comp) {
		for (GameComponent find : m_components) {
			if (comp.isInstance(find)) {
				return find;
			}
		}
		return null;
	}
	
	public GameObject FindChild(String name) {
		for (GameObject find : m_children) {
			if (find.m_name.equals(name)) {
				return find;
			}
		}
		return null;
	}
	
	public void SetName(String name) {
		m_name = name;
	}
	
	public String GetName() {
		return m_name;
	}
	
	public GameObject AddChild(GameObject child) {
		m_children.add(child);
		child.SetEngine(m_engine);
		child.GetTransform().SetParent(m_transform);
		
		return this;
	}
	
	public GameObject AddComponent(GameComponent component) {
		m_components.add(component);
		component.SetParent(this);
		
		return this;
	}
	
	public void InputAll(float delta) {
		Input(delta);
		
		for (GameObject child : m_children)
			child.InputAll(delta);
	}
	
	public void UpdateAll(float delta) {
		Update(delta);
		
		for (GameObject child : m_children)
			child.UpdateAll(delta);
	}
	
	public void RenderAll(Shader shader, RenderingEngine renderingEngine) {
		Render(shader, renderingEngine);
		
		for (GameObject child : m_children)
			child.RenderAll(shader, renderingEngine);
	}
	
	public void Input(float delta) {
		m_transform.Update();
		
		for (GameComponent component : m_components)
			component.Input(delta);
	}
	
	public void Update(float delta) {
		for (GameComponent component : m_components)
			component.Update(delta);
	}
	
	public void Render(Shader shader, RenderingEngine renderingEngine) {
		for (GameComponent component : m_components)
			component.Render(shader, renderingEngine);
	}
	
	public ArrayList<GameObject> GetAllAttached() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		
		for (GameObject child : m_children)
			result.addAll(child.GetAllAttached());
		
		result.add(this);
		return result;
	}
	
	public Transform GetTransform() {
		return m_transform;
	}
	
	public void SetEngine(CoreEngine engine) {
		if (this.m_engine != engine) {
			this.m_engine = engine;
			
			for (GameComponent component : m_components)
				component.AddToEngine(engine);
			
			for (GameObject child : m_children)
				child.SetEngine(engine);
		}
	}
	
	public void OnIsColliding(GameObject other) {
		for (GameComponent component : m_components)
			component.OnIsColliding(other);
	}
	
	public void OnCollide(GameObject other) {
		for (GameComponent component : m_components)
			component.OnCollide(other);
	}
	
	public void OnSeparate(GameObject other) {
		for (GameComponent component : m_components)
			component.OnSeparate(other);
	}
	
	public void AllColliderUpdateToJBullet(PhysicsEngine physicsEngine) {
		for (GameObject child : m_children) {
			child.AllColliderUpdateToJBullet(physicsEngine);
		}
		
		if (m_collider != null)
			m_collider.UpdateToJBullet(physicsEngine);
	}
	
	public void AllColliderUpdateToGame() {
		for (GameObject child : m_children) {
			child.AllColliderUpdateToGame();
		}
		
		if (m_collider != null)
			m_collider.UpdateToGame();
	}
	
	public void InitializeColliders(PhysicsEngine physicsEngine) {
		for (GameObject child : m_children) {
			child.InitializeColliders(physicsEngine);
		}
		
		if (m_collider != null)
			m_collider.Initialize(physicsEngine);
	}
}
