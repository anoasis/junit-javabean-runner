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

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;

@SuppressWarnings("unused")
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
		new JavaBeanRunner(MissingFixtureAnnotation.class);
	}
	
	@Test(expected=Throwable.class)
	public void morethanOneFixtureAnnotationThrowsThrowable() throws Throwable {
		new JavaBeanRunner(TooManyFixtureAnnotations.class);
	}
	
	@Test(expected=Throwable.class)
	public void voidFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(VoidFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void primitiveFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(PrimitiveFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void fixtureMethodWithParametersThrowsThrowable() throws Throwable {
		new JavaBeanRunner(FixtureMethodWithParameters.class);
	}
	
	@Test(expected=Throwable.class)
	public void privateAccessFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(PrivateFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void protectedAccessFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(ProtectedFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void defaultAccessFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(DefaultFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void nonStaticFixtureMethodThrowsThrowable() throws Throwable {
		new JavaBeanRunner(NonStaticFixtureMethod.class);
	}
	
	@Test(expected=Throwable.class)
	public void privateAccessConstructorThrowsThrowable() throws Throwable {
		new JavaBeanRunner(PrivateAccessConstructorFixture.class);
	}
	
	@Test(expected=Throwable.class)
	public void protectedAccessConstructorThrowsThrowable() throws Throwable {
		new JavaBeanRunner(ProtectedAccessConstructorFixture.class);
	}
	
	@Test(expected=Throwable.class)
	public void defaultAccessConstructorThrowsThrowable() throws Throwable {
		new JavaBeanRunner(DefaultAccessConstructorFixture.class);
	}
	
	@Test(expected=Throwable.class)
	public void constructorWithParametersThrowsThrowable() throws Throwable {
		new JavaBeanRunner(ConstructorWithParametersFixture.class);
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
	private static class MissingFixtureAnnotation {
		public static Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class Empty {
		@Fixture
		public static Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class TooManyFixtureAnnotations {
		@Fixture
		public static Object getFixture() {
			return new Object();
		}
		
		@Fixture
		public static Object getAnotherFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class VoidFixtureMethod {
		@Fixture
		public static void getFixture() {
			
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class PrimitiveFixtureMethod {
		@Fixture
		public static int getFixture() {
			return Integer.MAX_VALUE;
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class FixtureMethodWithParameters {
		@Fixture
		public static Object getFixture(Object param) {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class PrivateFixtureMethod {
		@Fixture
		private static Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class ProtectedFixtureMethod {
		@Fixture
		protected static Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class DefaultFixtureMethod {
		@Fixture
		static Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class NonStaticFixtureMethod {
		@Fixture
		public Object getFixture() {
			return new Object();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class PrivateAccessConstructorFixture {
		private PrivateAccessConstructorFixture() {
			
		}
		
		@Fixture
		public static Object getFixture() {
			return new PrivateAccessConstructorFixture();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class ProtectedAccessConstructorFixture {
		protected ProtectedAccessConstructorFixture() {
			
		}
		
		@Fixture
		public static Object getFixture() {
			return new ProtectedAccessConstructorFixture();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class DefaultAccessConstructorFixture {
		DefaultAccessConstructorFixture() {
			
		}
		
		@Fixture
		public static Object getFixture() {
			return new DefaultAccessConstructorFixture();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class ConstructorWithParametersFixture {
		public ConstructorWithParametersFixture(Object param) {
			
		}
		
		@Fixture
		public static Object getFixture() {
			return new ConstructorWithParametersFixture(new Object());
		}
	}
}
