package com.google.code.javabeanrunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.lang.management.LockInfo;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConstructorFactoryTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void constructWithNullConstructorThrowsNullPointerException() {
		exception.expect(NullPointerException.class);
		new ConstructorFactory(null, new HashMap<String, Object>());
	}
	
	@Test
	public void constructWithNullPropertyDescriptorArrayThrowsNullPointerException() throws SecurityException, NoSuchMethodException {
		exception.expect(NullPointerException.class);
		new ConstructorFactory(getClass().getDeclaredConstructor(), null);
	}
	
	@Test
	public void emptyConstructorIsSuccessful() throws Throwable {
		ConstructorFinder finder = new ConstructorFinder(Object.class);
		Constructor<?> ctor = finder.findConstructor();
		
		ConstructorFactory factory = new ConstructorFactory(ctor, new HashMap<String, Object>());
		assertThat(factory.construct(), instanceOf(Object.class));
	}
	
	@Test
	public void annotatedConstructorIsSuccessful() throws Throwable {
		ConstructorFinder finder = new ConstructorFinder(LockInfo.class);
		Constructor<?> ctor = finder.findConstructor();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("className", "foo");
		params.put("identityHashCode", 1);
		
		ConstructorFactory factory = new ConstructorFactory(ctor, params);
		assertThat(factory.construct(), instanceOf(LockInfo.class));
	}
	
	@Test
	public void annotatedConstructorWithMissingArgsThrowsIllegalArgumentException() throws Throwable {
		ConstructorFinder finder = new ConstructorFinder(LockInfo.class);
		Constructor<?> ctor = finder.findConstructor();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("className", "foo");
		
		ConstructorFactory factory = new ConstructorFactory(ctor, params);
		
		exception.expect(IllegalArgumentException.class);
		factory.construct();
	}
	
	@Test
	public void unannotatedConstructorThrowsIllegalArgumentException() throws Throwable {
		Map<String, Object> params = new HashMap<String, Object>();
		
		ConstructorFactory factory = new ConstructorFactory(Boolean.class.getConstructor(boolean.class), params);
		
		exception.expect(IllegalArgumentException.class);
		factory.construct();
	}
}
