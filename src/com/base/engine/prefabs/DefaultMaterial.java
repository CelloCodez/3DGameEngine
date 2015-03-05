package com.base.engine.prefabs;

import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;

public class DefaultMaterial extends Material {
	
	public DefaultMaterial() {
		super(new Texture("bricks2.jpg"), 1, 8, new Texture("bricks2_normal.png"), new Texture("bricks2_disp.jpg"), 0.04f, -1.0f);
	}
	
}
