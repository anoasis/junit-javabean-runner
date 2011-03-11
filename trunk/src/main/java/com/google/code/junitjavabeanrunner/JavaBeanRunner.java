package com.google.code.junitjavabeanrunner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class JavaBeanRunner extends Runner {
	private final Class<?> testClass;
	private final Description description;
	
	public JavaBeanRunner(Class<?> testClass)  throws Throwable {
		this.testClass = testClass;
		this.description = Description.EMPTY;
		
		validateRunWithAnnotation(testClass);
		validateFixtureAnnotation(testClass);
	}
	
	private void validateFixtureAnnotation(Class<?> testClass) throws Throwable {
		List<Method> methods = listFixtureMethods(testClass);
		if (methods.isEmpty()) {
			throw new InitializationError("Missing a @Fixture method");
		}
		if (methods.size() > 1) {
			throw new InitializationError("Too many @Fixture methods");
		}
	}
	
	private List<Method> listFixtureMethods(Class<?> testClass) throws InitializationError {
		List<Method> methods = new ArrayList<Method>();
		
		for (Method method : testClass.getDeclaredMethods()) {
			Fixture fixture = method.getAnnotation(Fixture.class);
			if (fixture != null) {
				validateFixtureMethod(method);
				methods.add(method);
			}
		}
		
		return methods;
	}
	
	private void validateFixtureMethod(Method method) throws InitializationError {
		Class<?> returnType = method.getReturnType();
		if (returnType.isPrimitive()) {
			throw new InitializationError("@Fixture method must return an object");
		}
		if (method.getParameterTypes().length != 0) {
			throw new InitializationError("@Fixture method must have no parameters");
		}
		if (Modifier.isPublic(method.getModifiers()) == false) {
			throw new InitializationError("@Fixture method must be public access");
		}
		if (Modifier.isStatic(method.getModifiers()) == false) {
			throw new InitializationError("@Fixture method must be static");
		}
	}

	private void validateRunWithAnnotation(Class<?> testClass) throws Throwable {
		RunWith runWith = testClass.getAnnotation(RunWith.class);
		if (runWith == null) {
			throw new InitializationError("Missing @RunWith");
		}
		if (runWith.value() != getClass()) {
			throw new InitializationError("Invalid Runner in @RunWith");
		}
	}
	
	@Override
	public Description getDescription() {
		return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		notifier.fireTestStarted(description);
		notifier.fireTestFinished(description);
	}
}
