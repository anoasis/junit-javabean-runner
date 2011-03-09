package com.google.code.junitjavabeanrunner;

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
		Fixture fixture = testClass.getAnnotation(Fixture.class);
		if (fixture == null) {
			throw new InitializationError("Missing @Fixture");
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
