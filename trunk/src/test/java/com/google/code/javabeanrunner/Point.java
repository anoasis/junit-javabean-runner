package com.google.code.javabeanrunner;

import java.beans.ConstructorProperties;

public class Point {
	private final int x;
	private final int y;
	
	@ConstructorProperties({"x", "y"})
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
