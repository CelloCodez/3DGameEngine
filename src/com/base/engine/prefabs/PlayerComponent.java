/*
 * Copyright (C) 2015-2016 CelloCodez
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

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.base.engine.components.Camera;
import com.base.engine.components.GameComponent;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.physics.RaycastOut;
import com.base.engine.rendering.Window;
import com.base.engine.util.Util;
import com.base.game.Main;
import com.base.game.Vars;

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
	
	// devmode variables
	@SuppressWarnings("unused")
	private GameObject m_dev_selectedGO = null;
	private float m_devtemp_mass = 0;
	private int m_dev_upkey;
	private int m_dev_downkey;
	
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
		this.m_dev_upkey = Input.KEY_E;
		this.m_dev_downkey = Input.KEY_Q;
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
	
	@SuppressWarnings("unused")
	@Override
	public void Input(float delta) {
		// looking/camera code
		
		// devmode enable/disable check
		if (Input.GetKeyDown(Input.KEY_F12)) {
			// check for other keys to enable devmode
			if (Input.GetKey(Input.KEY_LCONTROL) && Input.GetKey(Input.KEY_SPACE)) {
				Vars.flipBool("devmode");
				Vars.output("devmode");
				if (Vars.varBool("devmode")) {
					System.out.println("Disabling player physics for devmode");
					m_devtemp_mass = GetParent().GetCollider().GetMass();
					GetParent().GetCollider().SetMass(0);
				} else {
					System.out.println("Enabling player physics");
					GetParent().GetCollider().SetMass(m_devtemp_mass);
				}
			}
		}
		
		if (Input.GetKey(m_unlockMouseKey)) {
			Input.SetCursor(true);
		}
		if (Input.GetMouseDown(0)) {
			if (!Vars.varBool("devmode")) {
				Input.SetCursor(false);
			}
			
			if (Vars.varBool("devmode")) {
				// use mouse position in raycast
				Vector3f camDir = GetParent().FindChild(m_childCameraName).GetTransform().GetTransformedRot().GetForward();
				Vector3f camDirUp = GetParent().FindChild(m_childCameraName).GetTransform().GetTransformedRot().GetUp();
				Vector3f camDirRight = GetParent().FindChild(m_childCameraName).GetTransform().GetTransformedRot().GetRight();
				float mx = ((float) Mouse.getX() * 2 / Window.GetWidth()) - 1;
				float my = ((float) Mouse.getY() * 2 / Window.GetHeight()) - 1;
				System.out.println("mx " + mx);
				System.out.println("my " + my);
				
				// Thank you, this assisted me very much:
				// http://www.opengl-tutorial.org/miscellaneous/clicking-on-objects/picking-with-a-physics-library/
				
				Vector4f rayStartNDC = new Vector4f(((float) mx / (float) Window.GetWidth() - 0.5f) * 2.0f, ((float) my / (float) Window.GetHeight() - 0.5f) * 2.0f, -1.0f, 1.0f);
				Vector4f rayEndNDC = new Vector4f(((float) mx / (float) Window.GetWidth() - 0.5f) * 2.0f, ((float) my / (float) Window.GetHeight() - 0.5f) * 2.0f, 0.0f, 1.0f);
				
				com.base.engine.core.Matrix4f proj = ((Camera) GetParent().FindChild(m_childCameraName).GetComponent(Camera.class)).GetProjectionMatrix();
				Matrix4f invProj = Util.toLWJGLMatrix4f(Util.lwjglInvert(proj));
				com.base.engine.core.Matrix4f view = ((Camera) GetParent().FindChild(m_childCameraName).GetComponent(Camera.class)).GetViewProjection();
				Matrix4f invView = Util.toLWJGLMatrix4f(Util.lwjglInvert(view));
				
				Vector4f rayStartCam = new Vector4f();
				org.lwjgl.util.vector.Matrix4f.transform(invProj, rayStartNDC, rayStartCam);
				rayStartCam.scale(1.0f / rayStartCam.getW());
				Vector4f rayStartWorld = new Vector4f();
				org.lwjgl.util.vector.Matrix4f.transform(invView, rayStartCam, rayStartWorld);
				rayStartWorld.scale(1.0f / rayStartWorld.getW());
				Vector4f rayEndCam = new Vector4f();
				org.lwjgl.util.vector.Matrix4f.transform(invProj, rayEndNDC, rayEndCam);
				rayEndCam.scale(1.0f / rayEndCam.getW());
				Vector4f rayEndWorld = new Vector4f();
				org.lwjgl.util.vector.Matrix4f.transform(invView, rayEndCam, rayEndWorld);
				rayEndWorld.scale(1.0f / rayEndWorld.getW());
				
				org.lwjgl.util.vector.Vector4f rayDirSub = new org.lwjgl.util.vector.Vector4f();
				org.lwjgl.util.vector.Vector4f.sub(rayEndWorld, rayStartWorld, rayDirSub);
				org.lwjgl.util.vector.Vector3f rayDirWorld = new org.lwjgl.util.vector.Vector3f(rayDirSub.x, rayDirSub.y, rayDirSub.z);
				rayDirWorld = rayDirWorld.normalise(rayDirWorld);
				
				Vector3f rayDir = Util.fromLWJGLVector3(rayDirWorld);
				// TODO UGH JSBFK GDJKBKSAJDFHHKG
				// TODO Get mouse picking working...somehow...
				RaycastOut raycast = Main.Physics().Raycast(GetTransform().GetPos(), rayDir, 100f);
				if (raycast.hit) {
					m_dev_selectedGO = raycast.hitObject;
					System.out.println(raycast.hitObject.GetName() + " selected");
				}
			} else {
				RaycastOut raycast = Main.Physics().Raycast(GetTransform().GetPos(), GetParent().FindChild(m_childCameraName).GetTransform().GetTransformedRot().GetForward(), 100f);
				if (raycast.hit) {
					System.out.println(raycast.hitObject.GetName() + " clicked");
				}
			}
		}
		
		// devmode look
		if (Vars.varBool("devmode")) {
			if (Input.GetMouse(1)) {
				if (Input.GetCursor())
					Input.SetCursor(false);
			} else {
				Input.SetCursor(true);
			}
		}
		
		// rotation/look controls
		
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
			Move(GetTransform().GetRot().GetRight(), -movAmt);
		if (Input.GetKey(m_rightKey))
			Move(GetTransform().GetRot().GetRight(), movAmt);
		
		// devmode controls
		if (Vars.varBool("devmode")) {
			// devmode vertical movement/flying
			if (Input.GetKey(m_dev_upkey))
				Move(Y_AXIS, movAmt);
			if (Input.GetKey(m_dev_downkey))
				Move(Y_AXIS, -movAmt);
		}
		
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
		GetTransform().SetPos(GetTransform().GetPos().Add(dir.Mul(amt)));
	}
	
}
