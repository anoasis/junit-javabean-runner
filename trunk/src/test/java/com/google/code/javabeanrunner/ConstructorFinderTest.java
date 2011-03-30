package com.google.code.javabeanrunner;

import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.lang.annotation.RetentionPolicy;
import java.lang.management.LockInfo;

import org.junit.Test;

public class ConstructorFinderTest {
	@Test(expected=IllegalArgumentException.class)
	public void primitiveThrowsIllegalArgumentException() {
		ConstructorFinder finder = new ConstructorFinder(int.class);
		finder.findConstructor();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void arrayThrowsIllegalArgumentException() {
		ConstructorFinder finder = new ConstructorFinder(Object[].class);
		finder.findConstructor();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void enumThrowsIllegalArgumentException() {
		ConstructorFinder finder = new ConstructorFinder(RetentionPolicy.class);
		finder.findConstructor();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void interfaceThrowsIllegalArgumentException() {
		ConstructorFinder finder = new ConstructorFinder(Serializable.class);
		finder.findConstructor();
	}
	
	@Test
	public void annotatedConstructorIsFound() {
		ConstructorFinder finder = new ConstructorFinder(LockInfo.class);
		assertNotNull(finder.findConstructor());
	}
}
