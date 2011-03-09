package com.google.code.junitjavabeanrunner;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runners.model.Statement;

public class NullStatementTest {
	@Test
	public void nullPropertyPasses() throws Throwable {
		SimpleBean bean = new SimpleBean();
		
		Method accessor = SimpleBean.class.getMethod("getValue");
		Statement stmt = new NullStatement(bean, accessor);
		
		stmt.evaluate();
	}
	
	@Test(expected=Throwable.class)
	public void nonNullPropertyThrowsThrowable() throws Throwable {
		SimpleBean bean = new SimpleBean();
		bean.setValue("value");
		
		Method accessor = SimpleBean.class.getMethod("getValue");
		Statement stmt = new NullStatement(bean, accessor);
		
		stmt.evaluate();
	}
	
}
