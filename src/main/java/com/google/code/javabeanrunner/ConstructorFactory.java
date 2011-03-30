package com.google.code.javabeanrunner;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ConstructorFactory {
	private final Constructor<?> ctor;
	private final Map<String, Object> params;
	
	public ConstructorFactory(Constructor<?> ctor, Map<String, Object> params) {
		if (ctor == null) {
			throw new NullPointerException();
		}
		if (params == null) {
			throw new NullPointerException();
		}
		
		this.ctor = ctor;
		this.params = params;
	}

	public Object construct() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (ctor.getParameterTypes().length == 0) {
			return ctor.newInstance();
		} else {
			ConstructorProperties props = ctor.getAnnotation(ConstructorProperties.class);
			if (props != null) {
				String[] propNames = props.value();
				Object[] args = new Object[propNames.length];
				for (int i = 0; i < propNames.length; i++) {
					if (params.containsKey(propNames[i]) == false) {
						throw new IllegalArgumentException("Missing property for " + propNames[i]);
					}
					args[i] = params.get(propNames[i]);
				}
				return ctor.newInstance(args);
			} else {
				throw new IllegalArgumentException("Unconstructable at this time");
			}
		}
	}
}
