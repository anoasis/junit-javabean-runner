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
		try {
			sourceValue = getter.invoke(source);
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}

		Object targetValue;
		try {
			setter.invoke(target, sourceValue);
			targetValue = getter.invoke(target);
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}
		
		assertEquals(sourceValue, targetValue);
	}
}
