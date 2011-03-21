package com.google.code.javabeanrunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.code.javabeanrunner.MemberFinder;
import com.google.code.javabeanrunner.JavaBeanRunner.Property;

public class MemberFinderTest {
	@Test
	public void noMembersFindsNoUsableMembers() {
		MemberFinder finder = new MemberFinder(NoMembers.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void methodWithoutAnnotationFindsNoUsableMembers() {
		MemberFinder finder = new MemberFinder(SingleMethodWithoutAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void methodAnnotationFindsOneUsableMember() {
		MemberFinder finder = new MemberFinder(SingleMethodAnnotation.class);
		assertEquals(1, finder.findMembers().size());
	}
	
	@Test
	public void singleVoidMethodAnnotationFindsNoUsableMember() {
		MemberFinder finder = new MemberFinder(SingleVoidMethodAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void singleParameterizedMethodAnnotationFindsNoUsableMember() {
		MemberFinder finder = new MemberFinder(SingleParameterizedMethodAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void singleNonPublicMethodAnnotationFindsNoUsableMember() {
		MemberFinder finder = new MemberFinder(SingleNonPublicMethodAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void singleNonPublicFieldAnnotationFindsNoUsableMember() {
		MemberFinder finder = new MemberFinder(SingleNonPublicFieldAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void fieldWithoutAnnotationFindsNoUsableMembers() {
		MemberFinder finder = new MemberFinder(SingleFieldWithoutAnnotation.class);
		assertTrue(finder.findMembers().isEmpty());
	}
	
	@Test
	public void fieldAnnotationFindsOneUsableMember() {
		MemberFinder finder = new MemberFinder(SingleFieldAnnotation.class);
		assertEquals(1, finder.findMembers().size());
	}
	
	@Test(expected=IllegalStateException.class)
	public void duplicateMembersThrowsIllegalStateException() {
		MemberFinder finder = new MemberFinder(DuplicateMemberAnnotation.class);
		finder.findMembers();
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
