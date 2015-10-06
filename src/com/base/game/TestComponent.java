package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.GameObject;

public class TestComponent extends GameComponent {
	
	@Override
	public void OnCollide(GameObject other) {
		System.out.println("I, " + GetParent().GetName() + ", have just collided with " + other.GetName());
	}
	@Override
	public void OnSeparate(GameObject other) {
		System.out.println("I, " + GetParent().GetName() + ", have just stopped colliding with (separated from) " + other.GetName());
	}
	
}
