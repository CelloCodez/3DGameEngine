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

package com.base.engine.physics;

import com.base.engine.core.GameObject;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class Collider {
	private GameObject m_parent;
	
	protected boolean m_changed = false;
	protected RigidBody m_rigidbody;
	protected Transform m_transform;
	protected float m_mass;
	protected com.base.engine.core.Transform m_parentTransform;
	
	public Collider(float mass) {
		m_mass = mass;
	}
	
	public void SetParent(GameObject parent) {
		this.m_parent = parent;
	}
	
	public GameObject GetParent() {
		return m_parent;
	}
	
	public void SetParentTransform(com.base.engine.core.Transform transform) {
		m_parentTransform = transform;
	}
	
	public com.base.engine.core.Transform GetTransform() {
		return m_parentTransform;
	}
	
	public void Initialize(PhysicsEngine physicsEngine) {
	}
	
	public void UpdateToGame() {
	}
	
	public void UpdateToJBullet(PhysicsEngine physicsEngine) {
	}
	
	public void SetTrigger(boolean trigger) {
		if (trigger) {
			m_rigidbody.setCollisionFlags(m_rigidbody.getCollisionFlags() | CollisionFlags.NO_CONTACT_RESPONSE);
		} else {
			m_rigidbody.setCollisionFlags(m_rigidbody.getCollisionFlags() & ~CollisionFlags.NO_CONTACT_RESPONSE);
		}
	}
	
	public void ApplyCentralForce(com.base.engine.core.Vector3f force) {
		m_rigidbody.applyCentralForce(force.toVecmath());
		m_rigidbody.activate();
	}
	
	public void Translate(com.base.engine.core.Vector3f vec) {
		m_rigidbody.translate(vec.toVecmath());
		m_rigidbody.activate();
	}
	
	public void SetMass(float mass) {
		m_mass = mass;
		m_changed = true;
	}
	
	public RigidBody GetRigidbody() {
		return m_rigidbody;
	}
	
	public Transform GetPhysicsTransform() {
		return m_transform;
	}
	
	public float GetMass() {
		return m_mass;
	}
	
	public void Recreate(PhysicsEngine physicsEngine) {
	}
	
}
