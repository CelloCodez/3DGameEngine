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

import com.base.engine.components.GameComponent;
import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class PlayerComponent extends GameComponent {
	private static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	// looking/camera variables
	private float m_sensitivity;
	private int m_unlockMouseKey;
	private String m_childCameraName;
	
	// movement variables
	private float m_speed;
	private int m_forwardKey;
	private int m_backKey;
	private int m_leftKey;
	private int m_rightKey;
	private int m_jumpKey;
	
	/**
	 * @param sensitivity Mouse Sensitivity
	 * @param speed Default Movement Speed
	 */
	public PlayerComponent(float sensitivity, float speed) {
		this.m_sensitivity = sensitivity;
		this.m_childCameraName = "PrefabPlayerCamera";
		this.m_speed = speed;
		this.m_forwardKey = Input.KEY_W;
		this.m_backKey = Input.KEY_S;
		this.m_leftKey = Input.KEY_A;
		this.m_rightKey = Input.KEY_D;
		this.m_jumpKey = Input.KEY_SPACE;
	}
	
	public void SetSensitivity(float sensitivity) {
		this.m_sensitivity = sensitivity;
	}
	
	public void SetUnlockMouseKey(int unlockMouseKey) {
		this.m_unlockMouseKey = unlockMouseKey;
	}
	
	public void SetChildCameraName(String childCameraName) {
		m_childCameraName = childCameraName;
	}
	
	@Override
	public void Input(float delta) {
		// looking/camera code
		
		if (Input.GetKey(m_unlockMouseKey)) {
			Input.SetCursor(true);
		}
		if (Input.GetMouseDown(0)) {
			Input.SetCursor(false);
		}
		
		if (!Input.GetCursor()) {
			Vector2f deltaPos = Input.GetMouseDynamicPosition();
			boolean rotY = deltaPos.GetX() != 0;
			boolean rotX = deltaPos.GetY() != 0;
			
			if (rotY)
				GetTransform().Rotate(Y_AXIS, (float) Math.toRadians(deltaPos.GetX() * m_sensitivity));
			if (rotX)
				GetParent().FindChild(m_childCameraName).GetTransform()
						.Rotate(GetParent().FindChild(m_childCameraName).GetTransform().GetRot().GetRight(), (float) Math.toRadians(-deltaPos.GetY() * m_sensitivity));
		}
		
		// movement code
		
		float movAmt = m_speed * delta;
		
		if (Input.GetKey(m_forwardKey))
			Move(GetTransform().GetRot().GetForward(), movAmt);
		if (Input.GetKey(m_backKey))
			Move(GetTransform().GetRot().GetForward(), -movAmt);
		if (Input.GetKey(m_leftKey))
			Move(GetTransform().GetRot().GetLeft(), movAmt);
		if (Input.GetKey(m_rightKey))
			Move(GetTransform().GetRot().GetRight(), movAmt);
		
		// makeshift jumping
		if (Input.GetKeyDown(m_jumpKey))
			GetParent().GetCollider().GetRigidbody().applyCentralImpulse(new javax.vecmath.Vector3f(0, 50, 0));
		
		// lastly, restrict rotation so that the player doesn't fall over
		// and restrict linear velocity so the player doesn't get pushed
		
		// cancel any velocity that could make it fall over
		javax.vecmath.Vector3f curVel = new javax.vecmath.Vector3f(0, 0, 0);
		GetParent().GetCollider().GetRigidbody().getAngularVelocity(curVel);
		GetParent().GetCollider().GetRigidbody().setAngularVelocity(new javax.vecmath.Vector3f(0, curVel.y, 0));
		
		// cancel movement velocity (things can't push player)
		// mainly so falling objects don't send you flying sideways when they hit you
		// Note: the Y-axis of the linear velocity must be kept though or else gravity won't work
		curVel = new javax.vecmath.Vector3f(0, 0, 0);
		GetParent().GetCollider().GetRigidbody().getLinearVelocity(curVel);
		GetParent().GetCollider().GetRigidbody().setLinearVelocity(new javax.vecmath.Vector3f(0, curVel.y, 0));
		
		// keep from falling over
		// thank you very much:
		// https://www.youtube.com/watch?v=R4y7fJpY2_Y
		// you saved my life XD
		GetParent().GetCollider().GetRigidbody().setSleepingThresholds(0.0f, 0.0f);
		GetParent().GetCollider().GetRigidbody().setAngularFactor(0.0f);
	}
	
	public void Move(Vector3f dir, float amt) {
		GetParent().GetCollider().Translate(dir.Mul(amt));
	}
	
}
