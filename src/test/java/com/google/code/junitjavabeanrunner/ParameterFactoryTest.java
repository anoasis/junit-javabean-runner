package com.google.code.junitjavabeanrunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ParameterFactoryTest {
	private boolean booleanTest;
	private byte byteTest;
	private char charTest;
	private short shortTest;
	private int intTest;
	private long longTest;
	private float floatTest;
	private double doubleTest;
	private Object objectTest;
	
	private ParameterFactory factory;
	
	@Before
	public void setUp() {
		this.factory = new ParameterFactory();
	}
	
	@Test
	public void booleanInstanceNotDefault() {
		assertThat(booleanTest, is(not(factory.newInstance(boolean.class))));
	}
	
	@Test
	public void byteInstanceNotDefault() {
		assertThat(byteTest, is(not(factory.newInstance(byte.class))));
	}
	
	@Test
	public void charInstanceNotDefault() {
		assertThat(charTest, is(not(factory.newInstance(char.class))));
	}
	
	@Test
	public void shortInstanceNotDefault() {
		assertThat(shortTest, is(not(factory.newInstance(short.class))));
	}
	
	@Test
	public void intInstanceNotDefault() {
		assertThat(intTest, is(not(factory.newInstance(int.class))));
	}
	
	@Test
	public void longInstanceNotDefault() {
		assertThat(longTest, is(not(factory.newInstance(long.class))));
	}
	
	@Test
	public void floatInstanceNotDefault() {
		assertThat(floatTest, is(not(factory.newInstance(float.class))));
	}
	
	@Test
	public void doubleInstanceNotDefault() {
		assertThat(doubleTest, is(not(factory.newInstance(double.class))));
	}
	
	@Test
	public void objectInstanceNotDefault() {
		assertThat(objectTest, is(not(factory.newInstance(Object.class))));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void uninstantiableObjectInstanceThrowsIllegalArgumentException() {
		factory.newInstance(Class.class);
	}
	
	@Test
	public void objectWithNotArgPublicConstructorIsInstantiable() {
		assertTrue(factory.isInstantiatable(Object.class));
	}
	
	@Test
	public void objectWithoutNotArgPublicConstructorIsInstantiable() {
		assertFalse(factory.isInstantiatable(Class.class));
	}
}
