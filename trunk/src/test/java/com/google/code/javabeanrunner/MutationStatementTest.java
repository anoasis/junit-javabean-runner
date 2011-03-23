package com.google.code.javabeanrunner;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.model.Statement;

import com.google.code.javabeanrunner.MutationStatement;
import com.google.code.javabeanrunner.PreconditionFailureException;
import com.google.code.javabeanrunner.JavaBeanRunner.Property;

public class MutationStatementTest {
	private SimpleBean target;
	private Method getter;
	private Method setter;
	private PropertyDataSource source;
	private PropertyDescriptor property;
	
	@Before
	public void setUp() throws Throwable {
		target = mock(SimpleBean.class);
		source = new PropertyDataSource(this);
		getter = SimpleBean.class.getMethod("getValue");
		setter = SimpleBean.class.getMethod("setValue", String.class);
		
		property = mock(PropertyDescriptor.class);
		when(property.getReadMethod()).thenReturn(getter);
		when(property.getWriteMethod()).thenReturn(setter);
		when(property.getName()).thenReturn("value");
	}
	
	@Property("value")
	public String value = "value";
	
	@Test
	public void getterIsInvokedOnTarget() throws Throwable {
		when(target.getValue()).thenReturn(null, "value");
		
		Statement stmt = new MutationStatement(source, property, target);
		stmt.evaluate();
		
		verify(target, times(2)).getValue();
	}
	
	@Test
	public void setterIsInvokedOnTarget() throws Throwable {
		when(target.getValue()).thenReturn(null, "value");
		
		Statement stmt = new MutationStatement(source, property, target);
		stmt.evaluate();
		
		verify(target).setValue("value");
	}
	
	@Test(expected=AssertionError.class)
	public void misbehavingSetterThrowsError() throws Throwable {
		when(target.getValue()).thenReturn(null, "eulav");
		
		Statement stmt = new MutationStatement(source, property, target);
		stmt.evaluate();
		
		verify(target).setValue("value");
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void exceptionalGetterThrowsRuntimeException() throws Throwable {
		when(target.getValue()).thenThrow(new RuntimeException());
		
		Statement stmt = new MutationStatement(source, property, target);
		stmt.evaluate();
	}
	
	@Test(expected=PreconditionFailureException.class)
	public void exceptionalSetterThrowsRuntimeException() throws Throwable {
		when(target.getValue()).thenReturn(null, "value");
		doThrow(new RuntimeException()).when(target).setValue("value");
		
		Statement stmt = new MutationStatement(source, property, target);
		stmt.evaluate();
	}
}
