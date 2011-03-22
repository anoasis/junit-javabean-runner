package com.google.code.javabeanrunner;

import java.beans.ConstructorProperties;
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
		Constructor<?>[] ctors = type.getConstructors();
		
		for (Constructor<?> ctor : ctors) {
			if (ctor.getParameterTypes().length == 0) {
				return ctor;
			} else if (ctor.getAnnotation(ConstructorProperties.class) != null) {
				return ctor;
			}
		}
		throw new IllegalArgumentException("No usable constructors");
	}
}
