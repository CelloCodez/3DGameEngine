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
