package com.google.code.junitjavabeanrunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.lang.annotation.RetentionPolicy;

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
}