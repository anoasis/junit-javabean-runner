package com.google.code.junitjavabeanrunner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;

public class MutationStatementTest {
	private SimpleBean source;
	private SimpleBean target;
	private Method getter;
	private Method setter;
	
	@Before
	public void setUp() throws Throwable {
		source = mock(SimpleBean.class);
		target = mock(SimpleBean.class);
		getter = SimpleBean.class.getMethod("getValue");
		setter = SimpleBean.class.getMethod("setValue", String.class);
	}
	
	@Test
	public void getterIsInvokedOnSource() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn(null, "value");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
		
		verify(source).getValue();
	}
	
	@Test
	public void getterIsInvokedOnTarget() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn(null, "value");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
		
		verify(target, times(2)).getValue();
	}
	
	@Test
	public void setterIsInvokedOnTarget() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn(null, "value");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
		
		verify(target).setValue("value");
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void evaluateThrowsRuntimeExceptionWhenSourceValueNull() throws Throwable {
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void evaluateThrowsRuntimeExceptionWhenValuesEqual() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn("value");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
	}
	
	@Test(expected=AssertionError.class)
	public void misbehavingSetterThrowsError() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn(null, "eulav");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
		
		verify(target).setValue("value");
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void exceptionalGetterThrowsRuntimeException() throws Throwable {
		when(source.getValue()).thenThrow(new RuntimeException());
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void exceptionalSetterThrowsRuntimeException() throws Throwable {
		when(source.getValue()).thenReturn("value");
		when(target.getValue()).thenReturn(null, "value");
		doThrow(new RuntimeException()).when(target).setValue("value");
		
		Statement stmt = new MutationStatement(source, target, getter, setter);
		stmt.evaluate();
	}
}
