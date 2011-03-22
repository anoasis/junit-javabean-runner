package com.google.code.javabeanrunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.code.javabeanrunner.JavaBeanRunner.Property;

public class MemberFinderTest {
	@Test
	public void noMembersFindsNoUsableMembers() {
		MemberMapper finder = new MemberMapper(NoMembers.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void methodWithoutAnnotationFindsNoUsableMembers() {
		MemberMapper finder = new MemberMapper(SingleMethodWithoutAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void methodAnnotationFindsOneUsableMember() {
		MemberMapper finder = new MemberMapper(SingleMethodAnnotation.class);
		assertNotNull(finder.get("name"));
	}
	
	@Test
	public void singleVoidMethodAnnotationFindsNoUsableMember() {
		MemberMapper finder = new MemberMapper(SingleVoidMethodAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleParameterizedMethodAnnotationFindsNoUsableMember() {
		MemberMapper finder = new MemberMapper(SingleParameterizedMethodAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleNonPublicMethodAnnotationFindsNoUsableMember() {
		MemberMapper finder = new MemberMapper(SingleNonPublicMethodAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void singleNonPublicFieldAnnotationFindsNoUsableMember() {
		MemberMapper finder = new MemberMapper(SingleNonPublicFieldAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void fieldWithoutAnnotationFindsNoUsableMembers() {
		MemberMapper finder = new MemberMapper(SingleFieldWithoutAnnotation.class);
		assertTrue(finder.isEmpty());
	}
	
	@Test
	public void fieldAnnotationFindsOneUsableMember() {
		MemberMapper finder = new MemberMapper(SingleFieldAnnotation.class);
		assertNotNull(finder.get("name"));
	}
	
	@Test(expected=IllegalStateException.class)
	public void duplicateMembersThrowsIllegalStateException() {
		MemberMapper finder = new MemberMapper(DuplicateMemberAnnotation.class);
		assertNotNull(finder.get("name"));
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
