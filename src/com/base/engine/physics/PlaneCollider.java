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

import com.base.engine.core.Util;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class PlaneCollider extends Collider {
	
	private float m_width;
	private float m_length;
	
	public PlaneCollider(float mass, float width, float length) {
		super(mass);
		m_width = width;
		m_length = length;
	}
	
	@Override
	public void Initialize(PhysicsEngine physicsEngine) {
		m_transform = new Transform();
		m_transform.setIdentity();
		m_transform.set(GetTransform().GetTransformation().toVecmath());
		DefaultMotionState mState = new DefaultMotionState(m_transform);
		CollisionShape shape = new BoxShape(new Vector3f(m_width / 2f, 0.2f, m_length / 2f));
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(m_mass, mState, shape, new Vector3f(0, 0, 0));
		m_rigidbody = new RigidBody(rbci);
		m_rigidbody.setRestitution(0.0f);
		
		physicsEngine.addRigidBody(m_rigidbody);
	}
	
	@Override
	public void UpdateToGame() {
		m_rigidbody.getWorldTransform(m_transform);
		Vector3f newPos = m_transform.origin;
		GetTransform().SetPos(Util.fromJavaxVector3f(newPos));
		Quat4f q = new Quat4f();
		m_transform.getRotation(q);
		GetTransform().SetRot(Util.fromJavaxQuat4f(q));
		GetTransform().Update();
	}
	
	@Override
	public void UpdateToJBullet(PhysicsEngine physicsEngine) {
		if (physicsEngine != null) {
			// update the physics engine's rigidbody with anything changed
			if (m_changed) {
				Recreate(physicsEngine);
				m_changed = false;
			}
			// now update its transform with things changed in the game
			if (GetTransform().HasChanged()) {
				m_rigidbody.getMotionState().setWorldTransform(new Transform(GetTransform().GetTransformation().toVecmath()));
				m_rigidbody.setCenterOfMassTransform(new Transform(GetTransform().GetTransformation().toVecmath()));
				m_rigidbody.activate();
			}
		}
	}
	
	@Override
	public void Recreate(PhysicsEngine physicsEngine) {
		physicsEngine.removeRigidBody(m_rigidbody);
		
		Vector3f linVel = new Vector3f(0, 0, 0);
		m_rigidbody.getLinearVelocity(linVel);
		Vector3f angVel = new Vector3f(0, 0, 0);
		m_rigidbody.getAngularVelocity(angVel);
		float rest = m_rigidbody.getRestitution();
		
		DefaultMotionState mState = new DefaultMotionState(m_transform);
		CollisionShape shape = new BoxShape(new Vector3f(m_width / 2f, 0.2f, m_length / 2f));
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(m_mass, mState, shape, new Vector3f(0, 0, 0));
		m_rigidbody = new RigidBody(rbci);
		m_rigidbody.setLinearVelocity(linVel);
		m_rigidbody.setAngularVelocity(angVel);
		m_rigidbody.setRestitution(rest);
		
		m_rigidbody.setCcdMotionThreshold(1f);
		m_rigidbody.setCcdSweptSphereRadius(0.2f);
		
		physicsEngine.addRigidBody(m_rigidbody);
	}
	
}
