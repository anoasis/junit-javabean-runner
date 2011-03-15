package com.google.code.junitjavabeanrunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class JavaBeanRunner extends Runner {
	
	public JavaBeanRunner(Class<?> testClass)  throws Throwable {
		Fixture fixture = testClass.getAnnotation(Fixture.class);
		if (fixture == null) {
			throw new InitializationError("@Fixture annotation not present");
		}
		Class<?> fixtureClass = fixture.value();
		if (fixtureClass.isInterface()) {
			throw new InitializationError("Fixture should not be an interface");
		}
		if (fixtureClass.isArray()) {
			throw new InitializationError("Fixture should not be an array");
		}
		if (fixtureClass.isEnum()) {
			throw new InitializationError("Fixture should not be an enum");
		}
		if (fixtureClass.isPrimitive()) {
			throw new InitializationError("Fixture should not be a primitive");
		}
		try {
			Constructor<?>[] ctors = fixtureClass.getDeclaredConstructors();
			for (Constructor<?> ctor : ctors) {
				int modifiers = ctor.getModifiers();
				if (Modifier.isPublic(modifiers) == false) {
					throw new InitializationError("Constructor should be public");
				}
				if (ctor.getParameterTypes().length != 0) {
					throw new InitializationError("Constructor should have no parameters");
				}
			}
		} catch (SecurityException e) {
			throw new InitializationError(e);
		}
	}
	
	@Override
	public Description getDescription() {
		return Description.EMPTY;
	}

	@Override
	public void run(RunNotifier notifier) {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Fixture {
		Class<?> value();
	}
}
