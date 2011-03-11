package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.runners.model.Statement;

/**
 * Invokes the getter on the source
 * Invokes the getter on the target
 * If same, error
 * Otherwise, invokes the setter on the target
 * Then the getter on the target
 * Then compares the values.
 */
public class MutationStatement extends Statement {
	private final Object source;
	private final Object target;
	private final Method getter;
	private final Method setter;
	
	public MutationStatement(Object source, Object target, Method getter, Method setter) {
		this.source = source;
		this.target = target;
		this.getter = getter;
		this.setter = setter;
	}
	
	/**
	 * @throws IllegalArgumentException if preconditions not met
	 */
	@Override
	public void evaluate() throws PreconditionFailureException, AssertionError {
		Object sourceValue;
		Object targetValue;
		try {
			sourceValue = getter.invoke(source);
			targetValue = getter.invoke(target);
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}

		if (sourceValue == null) {
			throw new PreconditionFailureException("Source value should be null");
		}
		
		if (sourceValue.equals(targetValue)) {
			throw new PreconditionFailureException("Source and target values should be unequal");
		}
		
		try {
			setter.invoke(target, sourceValue);
			assertEquals(sourceValue, getter.invoke(target));
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}
	}
}
