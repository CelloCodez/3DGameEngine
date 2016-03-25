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

package com.base.game;

import java.util.HashMap;

public class Vars {
	
	public static HashMap<String, String> VARS = new HashMap<String, String>();
	public static boolean VARS_NEEDINIT = true;
	
	public static void initVars() {
		VARS.put("devmode", "0"); // devmode (developer mode)
	}
	
	public static void setVar(String name, String value) {
		VARS.put(name, value);
	}
	
	public static void setVarBool(String name, boolean value) {
		VARS.put(name, value ? "1" : "0");
	}
	
	public static String var(String name) {
		return VARS.get(name);
	}
	
	public static boolean varBool(String name) {
		String v = VARS.get(name).toLowerCase();
		return (v.equals("1") || v.equals("true"));
	}
	
	/**
	 * 'Flips' / inverts a boolean variable
	 * 	@param name
	 */
	public static void flipBool(String name) {
		VARS.put(name, varBool(name) ? "0" : "1");
	}
	
	public static void output(String name) {
		System.out.println(name + " is " + var(name));
	}
	
}
