package com.magenta.main;

public class Main {
	public static void main(String[] args) {
		// new Boot().run();
		try {
			new Engine(new Game()).run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
