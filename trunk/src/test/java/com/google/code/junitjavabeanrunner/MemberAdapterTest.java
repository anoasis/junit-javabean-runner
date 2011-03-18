package com.google.code.junitjavabeanrunner;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MemberAdapterTest {
	private MemberAdapter member;
	
	@Parameters
	public static List<Object[]> getParameters() throws Throwable {
		List<Object[]> params = new ArrayList<Object[]>();
		
		params.add(new Object[]{MemberAdapter.wrap(MemberAdapterTest.class.getMethod("example"))});
		params.add(new Object[]{MemberAdapter.wrap(MemberAdapterTest.class.getField("example"))});
		
		return params;
	}
	
	public MemberAdapterTest(MemberAdapter member) {
		this.member = member;
	}
	
	@Deprecated
	public int example() {
		return 1;
	}
	
	@Deprecated
	public int example = 1;
	
	@Test
	public void memberAnnotationsFound() throws Throwable {
		assertTrue(instanceOf(Deprecated.class).matches(member.getAnnotation(Deprecated.class)));
	}
	
	@Test
	public void memberType() throws Throwable {
		assertEquals(int.class, member.getType());
	}
	
	@Test
	public void memberModifiers() throws Throwable {
		assertTrue(Modifier.isPublic(member.getModifiers()));
	}
	
	@Test
	public void memberValue() throws Throwable {
		assertEquals(1, member.getValue(this));
	}
}
