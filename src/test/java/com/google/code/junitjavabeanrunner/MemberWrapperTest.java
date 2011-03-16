package com.google.code.junitjavabeanrunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;

public class MemberWrapperTest {
	private Method method;
	private Field field;
	
	@Before
	public void setUp() throws Throwable {
		method = getClass().getMethod("example");
		field = getClass().getField("example");
	}
	
	@Deprecated
	public int example() {
		return 1;
	}
	
	@Deprecated
	public int example = 1;
	
	@Test
	public void memberWrapperAcceptsMethod() throws Throwable {
		MemberWrapper.wrap(method);
	}
	
	@Test
	public void memberWrapperAcceptsField() throws Throwable {
		MemberWrapper.wrap(field);
	}
	
	@Test
	public void memberAnnotationsFound() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(method);
		assertTrue(instanceOf(Deprecated.class).matches(wrapper.getAnnotation(Deprecated.class)));
	}
	
	@Test
	public void fieldAnnotationsFound() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(field);
		assertTrue(instanceOf(Deprecated.class).matches(wrapper.getAnnotation(Deprecated.class)));
	}
	
	@Test
	public void methodType() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(method);
		assertEquals(int.class, wrapper.getType());
	}
	
	@Test
	public void fieldType() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(field);
		assertEquals(int.class, wrapper.getType());
	}
	
	@Test
	public void methodModifiers() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(method);
		assertTrue(Modifier.isPublic(wrapper.getModifiers()));
	}
	
	@Test
	public void fieldModifiers() throws Throwable {
		MemberWrapper wrapper = MemberWrapper.wrap(field);
		assertTrue(Modifier.isPublic(wrapper.getModifiers()));
	}
}
