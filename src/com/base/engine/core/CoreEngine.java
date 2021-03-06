/*
 * Copyright (C) 2014 Benny Bobaganoosh
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

package com.base.engine.core;

import java.util.HashMap;

import com.base.engine.physics.PhysicsEngine;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;
import com.base.game.Vars;

public class CoreEngine {
	private boolean m_isRunning;
	private Game m_game;
	private RenderingEngine m_renderingEngine;
	private PhysicsEngine m_physicsEngine;
	private int m_width;
	private int m_height;
	private double m_frameTime;
	
	public static HashMap<String, String> VARS = new HashMap<String, String>();
	
	public CoreEngine(int width, int height, double framerate, Game game) {
		this.m_isRunning = false;
		this.m_game = game;
		this.m_width = width;
		this.m_height = height;
		this.m_frameTime = 1.0 / framerate;
		
		// Check to initialize VARS
		if (Vars.VARS_NEEDINIT) {
			Vars.VARS_NEEDINIT = false;
			// Initialize VARS
			Vars.initVars();
		}
		
		game.SetEngine(this);
	}
	
	public void CreateWindow(String title, boolean vsync) {
		Window.CreateWindow(m_width, m_height, title, vsync);
		this.m_renderingEngine = new RenderingEngine();
		this.m_physicsEngine = new PhysicsEngine();
	}
	
	public void Start() {
		if (m_isRunning)
			return;
		
		Run();
	}
	
	public void Stop() {
		if (!m_isRunning)
			return;
		
		m_isRunning = false;
	}
	
	private void Run() {
		m_isRunning = true;
		
		int frames = 0;
		double frameCounter = 0;
		
		m_game.Init();
		m_game.SetEngine(this);
		
		m_game.InitializeColliders(m_physicsEngine);
		
		double lastTime = Time.GetTime();
		double unprocessedTime = 0;
		
		while (m_isRunning) {
			boolean render = false;
			
			double startTime = Time.GetTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while (unprocessedTime > m_frameTime) {
				render = true;
				
				unprocessedTime -= m_frameTime;
				
				if (Window.IsCloseRequested())
					Stop();
				
				m_physicsEngine.Update((float) m_frameTime);
				// Update the physics engine's results to the game
				m_game.UpdateCollidersToGame();
				
				m_game.Input((float) m_frameTime);
				Input.Update();
				
				m_physicsEngine.DoPhysicsEvents();
				
				m_game.Update((float) m_frameTime);
				// Update the game's transforms to the physics engine
				m_game.UpdateCollidersToJBullet(m_physicsEngine);
				
				if (frameCounter >= 1.0) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				m_game.Render(m_renderingEngine);
				Window.Render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		CleanUp();
	}
	
	private void CleanUp() {
		Window.Dispose();
	}
	
	public RenderingEngine GetRenderingEngine() {
		return m_renderingEngine;
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return m_physicsEngine;
	}
	
}
