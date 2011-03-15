package com.google.code.junitjavabeanrunner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
