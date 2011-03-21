package com.google.code.javabeanrunner;

import java.lang.reflect.Constructor;

class ConstructorFinder {
	private final Class<?> type;
	
	public ConstructorFinder(Class<?> type) {
		this.type = type;
	}
	
	public Constructor<?> findConstructor() throws IllegalArgumentException {
		if (type.isInterface()) {
			throw new IllegalArgumentException("Fixture should not be an interface");
		}
		if (type.isArray()) {
			throw new IllegalArgumentException("Fixture should not be an array");
		}
		if (type.isEnum()) {
			throw new IllegalArgumentException("Fixture should not be an enum");
		}
		if (type.isPrimitive()) {
			throw new IllegalArgumentException("Fixture should not be a primitive");
		}
		try {
			return type.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
