package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.awt.Point;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class JavaBeanRunnerTest {
	@Test(expected=InitializationError.class)
	public void missingFixtureAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(MissingFixtureAnnotation.class);
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class MissingFixtureAnnotation {
		public static Object getFixture() {
			return new Object();
		}
	}
}
