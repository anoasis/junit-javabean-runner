package com.google.code.javabeanrunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
class MutationStatement extends Statement {
	private final Object sourceValue;
	private final Object target;
	private final Method getter;
	private final Method setter;
	
	public MutationStatement(Object sourceValue, Object target, Method getter, Method setter) {
		this.sourceValue = sourceValue;
		this.target = target;
		this.getter = getter;
		this.setter = setter;
	}
	
	/**
	 * @throws IllegalArgumentException if preconditions not met
	 */
	@Override
	public void evaluate() throws PreconditionFailureException, AssertionError {
		try {
			Object originValue = getter.invoke(target);
			setter.invoke(target, sourceValue);
			Object targetValue = getter.invoke(target);
			assertEquals(sourceValue, targetValue);
			assertThat(originValue, not(equalTo(targetValue)));
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}
	}
}
