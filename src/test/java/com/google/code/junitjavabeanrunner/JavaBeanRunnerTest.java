package com.google.code.junitjavabeanrunner;

import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class JavaBeanRunnerTest {
	@Test(expected=InitializationError.class)
	public void missingFixtureAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(MissingFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void interfaceFixtureThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(InterfaceFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void arrayFixtureThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ArrayFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void enumFixtureThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(EnumFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void primitiveFixtureThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(PrimitiveFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void noPublicConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(PrivateConstructor.class);
	}
	
	@Test(expected=InitializationError.class)
	public void parameterizedConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ParameterizedConstructor.class);
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class MissingFixture {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Comparable.class)
	private static class InterfaceFixture {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Object[].class)
	private static class ArrayFixture {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(RetentionPolicy.class)
	private static class EnumFixture {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(boolean.class)
	private static class PrimitiveFixture {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Class.class)
	private static class PrivateConstructor {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Boolean.class)
	private static class ParameterizedConstructor {
	}
}
