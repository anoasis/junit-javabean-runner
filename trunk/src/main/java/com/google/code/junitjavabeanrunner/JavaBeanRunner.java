package com.google.code.junitjavabeanrunner;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
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
	private final Description description;
	
	public JavaBeanRunner(Class<?> testClass)  throws Throwable {
		validateRunWithAnnotation(testClass);
		Object fixture = validateFixtureAnnotation(testClass);
		validateFixture(fixture);
		
		BeanInfo info = Introspector.getBeanInfo(fixture.getClass(), Object.class);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		if (props.length == 0) {
			this.description = Description.EMPTY;
		} else {
			this.description = Description.createSuiteDescription(fixture.getClass());
			for (PropertyDescriptor prop : props) {
				this.description.addChild(Description.createTestDescription(fixture.getClass(), prop.getName()));
			}
		}
	}
	
	private Object validateFixture(Object fixture) throws InitializationError {
		Class<?> fixtureClass = fixture.getClass();
		List<Constructor<?>> ctors = listFixtureConstructors(fixtureClass);
		if (ctors.isEmpty()) {
			throw new InitializationError("Fixture class needs a public constructor");
		}
		Constructor<?> ctor = ctors.get(0);
		try {
			return ctor.newInstance();
		} catch (Throwable t) {
			throw new InitializationError(t);
		}
	}

	private List<Constructor<?>> listFixtureConstructors(Class<?> fixtureClass) throws InitializationError {
		List<Constructor<?>> ctors = new ArrayList<Constructor<?>>();
		
		for (Constructor<?> ctor : fixtureClass.getConstructors()) {
			if (hasParameters(ctor) == false) {
				ctors.add(ctor);
			}
		}
		
		return ctors;
	}
	
	private boolean hasParameters(Constructor<?> ctor) {
		if (ctor.getParameterTypes().length > 0) {
			return true;
		}
		return false;
	}
	
	private boolean hasParameters(Method method) {
		if (method.getParameterTypes().length > 0) {
			return true;
		}
		return false;
	}

	private Object validateFixtureAnnotation(Class<?> testClass) throws InitializationError {
		List<Method> methods = listFixtureMethods(testClass);
		if (methods.size() != 1) {
			throw new InitializationError("Only a single fixture method is permitted");
		}
		Method fixtureMethod = methods.get(0);
		try {
			return fixtureMethod.invoke(testClass);
		} catch (Throwable t) {
			throw new InitializationError(t);
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

		if (Modifier.isPublic(method.getModifiers()) == false) {
			throw new InitializationError("@Fixture method must be public access");
		}
		if (Modifier.isStatic(method.getModifiers()) == false) {
			throw new InitializationError("@Fixture method must be static");
		}
		if (returnType.isPrimitive()) {
			throw new InitializationError("@Fixture method must return an object");
		}
		if (hasParameters(method)) {
			throw new InitializationError("@Fixture method must have no parameters");
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
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	public @interface Fixture {
	}
}
