package com.google.code.javabeanrunner;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.beans.PropertyDescriptor;
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
	private final PropertyDataSource dataSource;
	private final PropertyDescriptor property;
	private final Object target;
	private final Method getter;
	private final Method setter;
	
	public MutationStatement(PropertyDataSource dataSource, PropertyDescriptor property, Object target) {
		this.dataSource = dataSource;
		this.property = property;
		this.target = target;
		this.getter = property.getReadMethod();
		this.setter = property.getWriteMethod();
	}
	
	/**
	 * @throws IllegalArgumentException if preconditions not met
	 */
	@Override
	public void evaluate() throws PreconditionFailureException, AssertionError {
		if (dataSource.contains(property.getName()) == false) {
			throw new PreconditionFailureException("No property annotation for " + property.getName());
		}
		try {
			Object originValue = getter.invoke(target);
			setter.invoke(target, dataSource.valueOf(property.getName()));
			Object targetValue = getter.invoke(target);
			assertEquals(dataSource.valueOf(property.getName()), targetValue);
			if(!(originValue instanceof Collection<?>||originValue instanceof Map<?,?>)){
				assertThat(originValue, not(equalTo(targetValue)));
			}
		} catch (Exception e) {
			throw new PreconditionFailureException(e);
		}
	}
}
