package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertNull;

import java.lang.reflect.Method;

import org.junit.runners.model.Statement;

public class NullStatement extends Statement {
	private final Object bean;
	private final Method accessor;

	public NullStatement(Object bean, Method accessor) {
		this.bean = bean;
		this.accessor = accessor;
	}

	@Override
	public void evaluate() throws Throwable {
		assertNull(accessor.invoke(bean));
	}

}
