package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Point;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JavaBeanRunnerTest {
	@Test(expected=Throwable.class)
	public void missingRunWithAnnotationThrowsThrowable() throws Throwable {
		new JavaBeanRunner(Point.class);
	}
	
	@Test(expected=Throwable.class)
	public void wrongRunnerInRunWithAnnotationThrowsThrowable() throws Throwable {
		new JavaBeanRunner(JavaBeanRunnerTest.class);
	}
	
	@Test(expected=Throwable.class)
	public void missingFixtureAnnotationThrowsThrowable() throws Throwable {
		new JavaBeanRunner(MissingFixture.class);
	}
	
	@Test
	public void nonBeanHasEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(Empty.class);
		assertEquals(Description.EMPTY, runner.getDescription());
	}
	
	@Test
	public void nonBeanRunStartsAndFinishes() throws Throwable {
		Runner runner = new JavaBeanRunner(Empty.class);
		
		RunNotifier notifier = mock(RunNotifier.class);
		
		runner.run(notifier);
		
		verify(notifier).fireTestStarted(Description.EMPTY);
		verify(notifier).fireTestFinished(Description.EMPTY);
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class MissingFixture {
		
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Object.class)
	private static class Empty {
		
	}
}
