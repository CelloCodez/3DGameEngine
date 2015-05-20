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

import com.base.engine.components.MeshRenderer;
import com.base.engine.core.GameObject;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.PlaneCollider;
import com.base.engine.rendering.Mesh;

public class PlanePrefab extends GameObject {
	
	public PlanePrefab(String name, float width, float length, float mass) {
		super(name);
		Mesh mesh = new Mesh("plane.obj");
		AddComponent(new MeshRenderer(mesh, new DefaultMaterial()));
		GetTransform().SetScale(new Vector3f(width, 1, length));
		SetCollider(new PlaneCollider(mass, width, length));
	}
	
}
