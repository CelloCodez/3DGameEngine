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
import com.base.engine.core.Vector3f;

public class RaycastOut {
	
	public boolean hit = false;
	public GameObject hitObject;
	public Vector3f hitPoint;
	public Vector3f hitNormal;
	
	public RaycastOut(boolean hit, GameObject hitObject, Vector3f hitPoint, Vector3f hitNormal) {
		this.hit = hit;
		this.hitObject = hitObject;
		this.hitPoint = hitPoint;
		this.hitNormal = hitNormal;
	}
	
}
