package com.google.code.javabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.code.javabeanrunner.JavaBeanRunner.Property;

public class PropertyDataSourceTest {
	@Test
	public void noMembersFindsNoUsableMembers() {
		PropertyDataSource finder = new PropertyDataSource(NoMembers.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void methodWithoutAnnotationFindsNoUsableMembers() {
		PropertyDataSource finder = new PropertyDataSource(new SingleMethodWithoutAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void methodAnnotationFindsOneUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleMethodAnnotation());
		assertEquals("name", finder.valueOf("name"));
	}
	
	@Test
	public void singleVoidMethodAnnotationFindsNoUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleVoidMethodAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleParameterizedMethodAnnotationFindsNoUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleParameterizedMethodAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleNonPublicMethodAnnotationFindsNoUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleNonPublicMethodAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleNonPublicFieldAnnotationFindsNoUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleNonPublicFieldAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void fieldWithoutAnnotationFindsNoUsableMembers() {
		PropertyDataSource finder = new PropertyDataSource(new SingleFieldWithoutAnnotation());
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void fieldAnnotationFindsOneUsableMember() {
		PropertyDataSource finder = new PropertyDataSource(new SingleFieldAnnotation());
		assertEquals("name", finder.valueOf("name"));
	}
	
	@Test(expected=IllegalStateException.class)
	public void duplicateMembersThrowsIllegalStateException() {
		PropertyDataSource finder = new PropertyDataSource(new DuplicateMemberAnnotation());
		assertEquals("name", finder.valueOf("name"));
	}
	
	public static class NoMembers {
	}
	
	public static class SingleMethodAnnotation {
		@Property("name")
		public String getName() {
			return "name";
		}
	}
	
	public static class DuplicateMemberAnnotation {
		@Property("name")
		public String getName() {
			return "name";
		}
		
		@Property("name")
		public String name = "name";
	}
	
	public static class SingleVoidMethodAnnotation {
		@Property("name")
		public void getName() {
		}
	}
	
	public static class SingleParameterizedMethodAnnotation {
		@Property("name")
		public String getName(String param) {
			return "name";
		}
	}
	
	public static class SingleMethodWithoutAnnotation {
		public String getName() {
			return "name";
		}
	}
	
	public static class SingleNonPublicMethodAnnotation {
		@Property("name")
		String getName() {
			return "name";
		}
	}
	
	public static class SingleNonPublicFieldAnnotation {
		@Property("name")
		String name = "name";
	}
	
	public static class SingleFieldAnnotation {
		@Property("name")
		public String name = "name";
	}
	
	public static class SingleFieldWithoutAnnotation {
		public String name = "name";
	}
}
