package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.GameObject;

public class TestComponent extends GameComponent {
	
	@Override
	public void OnIsColliding(GameObject other) {
		System.out.println("I, " + GetParent().GetName() + ", am colliding with " + other.GetName());
	}
	
}
