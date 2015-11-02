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

import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.util.Util;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class MeshCollider extends Collider {
	
	private IndexedModel m_indexedModel;
	
	public MeshCollider(float mass, IndexedModel model) {
		super(mass);
		m_indexedModel = model;
	}
	
	@Override
	public void Initialize(PhysicsEngine physicsEngine) {
		m_transform = new Transform();
		m_transform.set(GetTransform().GetTransformationIdentityScale().toVecmath());
		DefaultMotionState mState = new DefaultMotionState(m_transform);
		
		MeshColliderHelper helper = new MeshColliderHelper(m_indexedModel);
		TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray(m_indexedModel.GetIndices().size() / 3, helper.GetIndexBuffer(), 12, m_indexedModel.GetPositions().size(),
				helper.GetVertexBuffer(), 12);
		GImpactMeshShape shape = new GImpactMeshShape(indexVertexArrays);
		shape.updateBound();
		
		Vector3f inertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(m_mass, inertia);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(m_mass, mState, shape, inertia);
		m_rigidbody = new RigidBody(rbci);
		m_rigidbody.setRestitution(0.0f);
		
		m_rigidbody.setCcdMotionThreshold(1f);
		m_rigidbody.setCcdSweptSphereRadius(0.2f);
		
		physicsEngine.addRigidBody(m_rigidbody, GetParent());
	}
	
	@Override
	public void UpdateToGame() {
		// apply changes made in the physics engine
		m_transform = ((DefaultMotionState) m_rigidbody.getMotionState()).graphicsWorldTrans;
		Vector3f newPos = m_transform.origin;
		GetTransform().SetPos(Util.fromJavaxVector3f(newPos));
		Quat4f q = new Quat4f();
		m_transform.getRotation(q);
		//		m_rigidbody.getOrientation(q);
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
				m_rigidbody.getMotionState().setWorldTransform(new Transform(GetTransform().GetTransformationIdentityScale().toVecmath()));
				m_rigidbody.setCenterOfMassTransform(new Transform(GetTransform().GetTransformationIdentityScale().toVecmath()));
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
		
		MeshColliderHelper helper = new MeshColliderHelper(m_indexedModel);
		TriangleIndexVertexArray indexVertexArrays = new TriangleIndexVertexArray(m_indexedModel.GetIndices().size() / 3, helper.GetIndexBuffer(), 12, m_indexedModel.GetPositions().size(),
				helper.GetVertexBuffer(), 12);
		GImpactMeshShape shape = new GImpactMeshShape(indexVertexArrays);
		shape.updateBound();
		
		Vector3f inertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(m_mass, inertia);
		RigidBodyConstructionInfo rbci = new RigidBodyConstructionInfo(m_mass, mState, shape, inertia);
		m_rigidbody = new RigidBody(rbci);
		m_rigidbody.setLinearVelocity(linVel);
		m_rigidbody.setAngularVelocity(angVel);
		m_rigidbody.setRestitution(rest);
		
		m_rigidbody.setCcdMotionThreshold(1f);
		m_rigidbody.setCcdSweptSphereRadius(0.2f);
		
		physicsEngine.addRigidBody(m_rigidbody, GetParent());
	}
	
}
