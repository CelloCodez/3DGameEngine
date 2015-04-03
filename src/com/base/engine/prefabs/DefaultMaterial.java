package com.base.engine.prefabs;

import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;

public class DefaultMaterial extends Material {
	
	public DefaultMaterial() {
		super(new Texture("defaultTexture.png"), 1, 8, new Texture("default_normal.png"), new Texture("default_disp.png"), 0.02f, -0.4f);
	}
	
}
