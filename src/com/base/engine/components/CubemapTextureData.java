/*
 * Copyright (C) 2017 CelloCodez
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

package com.base.engine.components;

import java.nio.ByteBuffer;

/**
 * extra code for cubemap texture loading
 * @author CelloCodez
 *
 */
public class CubemapTextureData {
	
	private ByteBuffer m_img;
	private int m_width;
	private int m_height;
	
	public CubemapTextureData(ByteBuffer img, int width, int height) {
		m_img = img;
		m_width = width;
		m_height = height;
	}
	
	public ByteBuffer getImg() {
		return m_img;
	}
	
	public int getWidth() {
		return m_width;
	}
	
	public int getHeight() {
		return m_height;
	}
	
}
