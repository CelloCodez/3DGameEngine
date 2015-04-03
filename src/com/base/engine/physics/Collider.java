package com.base.engine.physics;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class Collider {
	
	protected boolean m_changed = false;
	protected RigidBody m_rigidbody;
	protected Transform m_transform;
	protected float m_mass;
	protected com.base.engine.core.Transform m_parentTransform;
	
	public Collider(float mass) {
		m_mass = mass;
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
