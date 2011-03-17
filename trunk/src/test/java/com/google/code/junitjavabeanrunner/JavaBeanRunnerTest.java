package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.JUnit4;
import org.junit.runners.model.InitializationError;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;
import com.google.code.junitjavabeanrunner.JavaBeanRunner.Property;

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
	
	@Test(expected=InitializationError.class)
	public void objectThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(ObjectTest.class);
	}
	
	@Test
	public void emptyBeanHasEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(Empty.class);
		assertEquals(Description.EMPTY, runner.getDescription());
	}
	
	@Test
	public void readOnlyBeanHasEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(ReadOnly.class);
		assertEquals(Description.EMPTY, runner.getDescription());
	}
	
	@Test
	public void beanHasNonEmptyDescription() throws Throwable {
		Runner runner = new JavaBeanRunner(Simple.class);
		assertFalse(Description.EMPTY.equals(runner.getDescription()));
	}
	
	@Test
	public void descriptionHasOneChildPerProperty() throws Throwable {
		Runner runner = new JavaBeanRunner(Simple.class);
		assertEquals(1, runner.getDescription().getChildren().size());
	}
	
	@Test(expected=InitializationError.class)
	public void nonPublicPropertyMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(NonPublicPropertyMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void voidPropertyMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(VoidPropertyMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void propertyMethodWithInvalidNameThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(InvalidNamePropertyMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void nonMatchingPropertyMethodThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(NonMatchingPropertyMethod.class);
	}
	
	@Test(expected=InitializationError.class)
	public void nonPublicPropertyFieldThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(NonPublicPropertyField.class);
	}
	
	@Test(expected=InitializationError.class)
	public void propertyFieldWithInvalidNameThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(InvalidNamePropertyField.class);
	}
	
	@Test(expected=InitializationError.class)
	public void nonMatchingPropertyFieldThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(NonMatchingPropertyField.class);
	}
	
	@Test(expected=InitializationError.class)
	public void duplicatePropertyMembersThrowsInitializationError() throws Throwable {
		new JavaBeanRunner(DuplicatePropertyMembers.class);
	}
	
	@Test
	public void goodBeanHasSuccessfulRun() throws Throwable {
		Runner runner = new JavaBeanRunner(Simple.class);
		Description desc = runner.getDescription();
		
		RunNotifier notifier = mock(RunNotifier.class);
		runner.run(notifier);
		
		verify(notifier).fireTestStarted(desc);
		for (Description child : desc.getChildren()) {
			verify(notifier).fireTestStarted(child);
			verify(notifier).fireTestFinished(child);
		}
		verify(notifier).fireTestFinished(desc);
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
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(EmptyBean.class)
	private static class Empty {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	public static class Simple {
		@Property("value")
		public String value = "value";
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(Object.class)
	private static class ObjectTest {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(ReadOnlyBean.class)
	private static class ReadOnly {
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class NonPublicPropertyMethod {
		@Property("value")
		private String getName() {
			return "name";
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class VoidPropertyMethod {
		@Property("value")
		public void getName() {
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class InvalidNamePropertyMethod {
		@Property("foo")
		public String getName() {
			return "name";
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class NonMatchingPropertyMethod {
		@Property("value")
		public Integer getValue() {
			return new Integer(1);
		}
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class NonPublicPropertyField {
		@Property("value")
		private String name = "name";
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class InvalidNamePropertyField {
		@Property("foo")
		public String name = "name";
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class NonMatchingPropertyField {
		@Property("value")
		public Integer name = new Integer(1);
	}
	
	@RunWith(JavaBeanRunner.class)
	@Fixture(SimpleBean.class)
	private static class DuplicatePropertyMembers {
		@Property("value")
		public String value = "value";
		@Property("value")
		public String getValue() {
			return value;
		}
	}
}
