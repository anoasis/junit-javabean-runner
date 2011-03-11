package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Point;

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
	public void missingRunWithAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(Point.class);
	}
	
	@Test(expected=InitializationError.class)
	public void wrongRunnerInRunWithAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(JavaBeanRunnerTest.class);
	}
	
	@Test(expected=InitializationError.class)
	public void missingFixtureAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(MissingFixtureAnnotation.class);
	}
	
	@Test(expected=InitializationError.class)
	public void morethanOneFixtureAnnotationThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(TooManyFixtureAnnotations.class);
	}
	
	@Test(expected=InitializationError.class)
	public void voidFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(VoidFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void primitiveFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(PrimitiveFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void fixtureMethodWithParametersThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(FixtureMethodWithParameters.class);
	}
	
	@Test(expected=InitializationError.class)
	public void exceptionInFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ExceptionInFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void privateAccessFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(PrivateFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void protectedAccessFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ProtectedFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void defaultAccessFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(DefaultFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void nonStaticFixtureMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(NonStaticFixtureMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void privateAccessConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(PrivateAccessConstructorFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void protectedAccessConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ProtectedAccessConstructorFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void defaultAccessConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(DefaultAccessConstructorFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void constructorWithParametersThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ConstructorWithParametersFixture.class);
	}
	
	@Test(expected=InitializationError.class)
	public void exceptionInConstructorThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ExceptionInConstructorFixture.class);
	}
	
	@Test
	public void nonBeanHasEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(Empty.class);
		assertTrue(runner.getDescription().isEmpty());
	}
	
	@Test
	public void nonBeanDescriptionHasNoChildren() throws Throwable {
		Runner runner = new JavaBeanRunner(Empty.class);
		assertTrue(runner.getDescription().getChildren().isEmpty());
	}
	
	@Test
	public void beanHasNonEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(Simple.class);
		assertFalse(runner.getDescription().isEmpty());
	}
	
	@Test
	public void beanDescriptionHasChildren() throws Throwable {
		Runner runner = new JavaBeanRunner(Simple.class);
		assertFalse(runner.getDescription().getChildren().isEmpty());
	}
	
	@Test
	public void nonBeanRunStartsAndFinishes() throws Throwable {
		RunNotifier notifier = mock(RunNotifier.class);
		
		Runner runner = new JavaBeanRunner(Empty.class);
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
			return new EmptyFixture();
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class Simple {
		@Fixture
		public static SimpleBean getFixture() {
			return new SimpleBean();
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
	
	@RunWith(JavaBeanRunner.class)
	private static class ExceptionInConstructorFixture {
		public ExceptionInConstructorFixture() {
			throw new RuntimeException();
		}
		
		public ExceptionInConstructorFixture(Object param) {
			
		}
		
		@Fixture
		public static Object getFixture() {
			return new ExceptionInConstructorFixture(new Object());
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	private static class ExceptionInFixture {
		@Fixture
		public static Object getFixture() {
			throw new RuntimeException();
		}
	}
	
	public static class EmptyFixture {
		
	}
}
