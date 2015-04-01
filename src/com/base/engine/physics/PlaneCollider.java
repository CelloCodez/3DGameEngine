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

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Util;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class PlaneCollider extends GameComponent {
	
	private boolean m_init = true;
	private RigidBody m_rigidbody;
	private Transform m_transform;
	private float m_mass;
	private float m_width;
	private float m_height;
	
	public PlaneCollider(float mass, float width, float height) {
		super();
		m_mass = mass;
		m_width = width;
		m_height = height;
	}
	
	@Override
	public void Update(float delta) {
	}
	
	@Override
	public void PhysicsUpdate(PhysicsEngine physicsEngine) {
		if (physicsEngine != null) {
			if (m_init) {
				m_transform = new Transform();
				m_transform.setIdentity();
				m_transform.set(GetTransform().GetTransformation().toVecmath());
				DefaultMotionState mState = new DefaultMotionState(m_transform);
				CollisionShape shape = new BoxShape(new Vector3f(m_width, 0.2f, m_height));
				RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(m_mass, mState, shape, new Vector3f(0, 0, 0));
				m_rigidbody = new RigidBody(rbci);
				m_rigidbody.setRestitution(0.0f);
				
				m_rigidbody.setCcdMotionThreshold(1f);
				m_rigidbody.setCcdSweptSphereRadius(1f);
				
				physicsEngine.addRigidBody(m_rigidbody);
				
				m_init = false;
			} else {
				Transform newTransform = m_rigidbody.getMotionState().getWorldTransform(m_transform);
				Vector3f newPos = newTransform.origin;
				GetTransform().SetPos(Util.fromJavaxVector3f(newPos));
				Quat4f q = new Quat4f();
				newTransform.getRotation(q);
				GetTransform().SetRot(Util.fromJavaxQuat4f(q));
			}
		}
	}
	
	@Override
	public void AfterPhysicsUpdate(PhysicsEngine physicsEngine) {
		if (physicsEngine != null) {
			if (m_init) {
			} else {
				// update the physics engine's transform with anything changed
				// in the game
				if (GetTransform().HasChanged()) {
					m_rigidbody.getMotionState().setWorldTransform(new Transform(GetTransform().GetTransformation().toVecmath()));
					m_rigidbody.setCenterOfMassTransform(new Transform(GetTransform().GetTransformation().toVecmath()));
					m_rigidbody.activate();
				}
			}
		}
	}
	
}
