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

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsEngine {
	
	private DiscreteDynamicsWorld m_dynamicsWorld;
	private BroadphaseInterface m_broadphase;
	private DefaultCollisionConfiguration m_collisionConfiguration;
	private CollisionDispatcher m_dispatcher;
	private SequentialImpulseConstraintSolver m_solver;
	
	public PhysicsEngine() {
		m_broadphase = new DbvtBroadphase();
		m_collisionConfiguration = new DefaultCollisionConfiguration();
		m_dispatcher = new CollisionDispatcher(m_collisionConfiguration);
		m_solver = new SequentialImpulseConstraintSolver();
		m_dynamicsWorld = new DiscreteDynamicsWorld(m_dispatcher, m_broadphase, m_solver, m_collisionConfiguration);
		m_dynamicsWorld.setGravity(new Vector3f(0, -5, 0));
	}
	
	public void setGravity(com.base.engine.core.Vector3f gravity) {
		m_dynamicsWorld.setGravity(new Vector3f(gravity.GetX(), gravity.GetY(), gravity.GetZ()));
	}
	
	public void Update(float delta) {
		m_dynamicsWorld.stepSimulation(delta, 12);
	}
	
	public void addRigidBody(RigidBody rigidBody) {
		m_dynamicsWorld.addRigidBody(rigidBody);
	}
	
	public void removeRigidBody(RigidBody rigidBody) {
		m_dynamicsWorld.removeRigidBody(rigidBody);
	}
	
}
