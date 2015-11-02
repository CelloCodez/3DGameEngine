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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.util.Util;

public class MeshColliderHelper {
	
	private Vector3f[] m_verts;
	private int[] m_indices;
	
	public MeshColliderHelper(IndexedModel model) {
		m_verts = model.GetPositions().toArray(new Vector3f[model.GetPositions().size()]);
		m_indices = Util.ToIntArray(model.GetIndices().toArray(new Integer[model.GetIndices().size()]));
	}
	
	public ByteBuffer GetVertexBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(m_verts.length * 12).order(ByteOrder.nativeOrder());
		for (int i = 0; i < m_verts.length; i++) {
			bb.putFloat(m_verts[i].GetX());
			bb.putFloat(m_verts[i].GetY());
			bb.putFloat(m_verts[i].GetZ());
		}
		bb.flip();
		return bb;
	}
	
	public ByteBuffer GetIndexBuffer() {
		ByteBuffer bb = ByteBuffer.allocateDirect(m_indices.length * 4).order(ByteOrder.nativeOrder());
		for (int i = 0; i < m_indices.length; i++) {
			bb.putInt(m_indices[i]);
		}
		bb.flip();
		return bb;
	}
	
}
