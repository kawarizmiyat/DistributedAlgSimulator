package com.test;

import com.simulator.EventHandler;
import com.simulator.SimSystem;

public class Main extends EventHandler {
	private static SimSystem s;
	public static void main(String [] args) {
		s = new SimSystem();
		System.out.println("program terminates");
	}
}

