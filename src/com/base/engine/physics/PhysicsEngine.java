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

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import com.base.engine.core.GameObject;
import com.base.engine.util.InterchangeablePair;
import com.base.engine.util.Util;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsEngine {
	
	private DiscreteDynamicsWorld m_dynamicsWorld;
	private BroadphaseInterface m_broadphase;
	private DefaultCollisionConfiguration m_collisionConfiguration;
	private CollisionDispatcher m_dispatcher;
	private SequentialImpulseConstraintSolver m_solver;
	/**
	 * Used to tell when to call functions like OnCollide, OnSeparate; used for checking if objects are physically colliding
	 */
	private List<InterchangeablePair<GameObject, GameObject>> m_lastCollisions;
	
	public PhysicsEngine() {
		m_broadphase = new DbvtBroadphase();
		m_collisionConfiguration = new DefaultCollisionConfiguration();
		m_dispatcher = new CollisionDispatcher(m_collisionConfiguration);
		m_solver = new SequentialImpulseConstraintSolver();
		m_dynamicsWorld = new DiscreteDynamicsWorld(m_dispatcher, m_broadphase, m_solver, m_collisionConfiguration);
		m_dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		
		m_lastCollisions = new ArrayList<InterchangeablePair<GameObject, GameObject>>();
	}
	
	/**
	 * Returns a RaycastOut for whatever is hit with a raycast at pos, direction dir, distance dist
	 * @param pos
	 * @param dir
	 * @param dist
	 * @return
	 */
	public RaycastOut Raycast(com.base.engine.core.Vector3f pos, com.base.engine.core.Vector3f dir, float dist) {
		return Raycast(pos, pos.Add(dir.Normalized().Mul(dist)));
	}
	
	/**
	 * Returns a RaycastOut for whatever is hit between start and end
	 * @param start
	 * @param end
	 * @return
	 */
	public RaycastOut Raycast(com.base.engine.core.Vector3f start, com.base.engine.core.Vector3f end) {
		ClosestRayResultCallback res = new ClosestRayResultCallback(start.toVecmath(), end.toVecmath());
		m_dynamicsWorld.rayTest(start.toVecmath(), end.toVecmath(), res);
		RaycastOut out = new RaycastOut(false, null, null, null);
		if (res.hasHit()) {
			out = new RaycastOut(true, ((GameObject) res.collisionObject.getUserPointer()), Util.fromJavaxVector3f(res.hitPointWorld), Util.fromJavaxVector3f(res.hitNormalWorld));
		}
		return out;
	}
	
	/**
	 * Returns true if the game objects 'a' and 'b' are colliding in the physics engine.
	 * GameObject order does not matter (if you swap 'a' and 'b' the same exact output is given)
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean Collides(GameObject a, GameObject b) {
		return m_lastCollisions.contains(new InterchangeablePair<GameObject, GameObject>(a, b));
	}
	
	public void DoPhysicsEvents() {
		List<InterchangeablePair<GameObject, GameObject>> curPairs = new ArrayList<InterchangeablePair<GameObject, GameObject>>();
		
		int numManifolds = m_dynamicsWorld.getDispatcher().getNumManifolds();
		for (int i = 0; i < numManifolds; i++) {
			PersistentManifold contactManifold = m_dynamicsWorld.getDispatcher().getManifoldByIndexInternal(i);
			if (contactManifold.getNumContacts() > 0) {
				GameObject a = (GameObject) ((CollisionObject) contactManifold.getBody0()).getUserPointer();
				GameObject b = (GameObject) ((CollisionObject) contactManifold.getBody1()).getUserPointer();
				
				// to help ensure that OnIsColliding isn't called twice for the same two game objects
				if (curPairs.contains(new InterchangeablePair<GameObject, GameObject>(a, b))) {
					// already used
				} else {
					curPairs.add(new InterchangeablePair<GameObject, GameObject>(a, b));
				}
			}
		}
		
		for (InterchangeablePair<GameObject, GameObject> pair : curPairs) {
			if (m_lastCollisions.contains(pair)) {
				// has been colliding
				// OnIsColliding
				pair.getLeft().OnIsColliding(pair.getRight());
				pair.getRight().OnIsColliding(pair.getLeft());
				m_lastCollisions.remove(pair);
			} else {
				// has just collided
				// OnCollide
				pair.getLeft().OnCollide(pair.getRight());
				pair.getRight().OnCollide(pair.getLeft());
				m_lastCollisions.remove(pair);
			}
		}
		
		for (InterchangeablePair<GameObject, GameObject> pair : m_lastCollisions) {
			if (curPairs.contains(pair)) {
				// something wrong has happened!
				throw new RuntimeException("Pair of colliding GameObjects has been removed, but still exists!");
			} else {
				// has just stopped colliding
				// OnSeparate
				pair.getLeft().OnSeparate(pair.getRight());
				pair.getRight().OnSeparate(pair.getLeft());
			}
		}
		
		m_lastCollisions.clear();
		for (InterchangeablePair<GameObject, GameObject> pair : curPairs) {
			m_lastCollisions.add(pair);
		}
	}
	
	public void setGravity(com.base.engine.core.Vector3f gravity) {
		m_dynamicsWorld.setGravity(new Vector3f(gravity.GetX(), gravity.GetY(), gravity.GetZ()));
	}
	
	public void Update(float delta) {
		m_dynamicsWorld.stepSimulation(delta, 7);
	}
	
	public void addRigidBody(RigidBody rigidBody, GameObject parent) {
		// ((GameObject) rigidBody.getUserPointer()) is the GameObject that the RigidBody belongs to
		rigidBody.setUserPointer(parent);
		m_dynamicsWorld.addRigidBody(rigidBody);
	}
	
	public void removeRigidBody(RigidBody rigidBody) {
		m_dynamicsWorld.removeRigidBody(rigidBody);
	}
	
}
