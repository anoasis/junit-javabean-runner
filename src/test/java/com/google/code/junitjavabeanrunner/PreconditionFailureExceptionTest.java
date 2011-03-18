package com.google.code.junitjavabeanrunner;

import static org.junit.Assert.*;

import org.junit.Test;

public class PreconditionFailureExceptionTest {

	@Test
	public void testPreconditionFailureCause() {
		Throwable t = new Exception();
		Exception e = new PreconditionFailureException(t);
		assertEquals(t, e.getCause());
	}

	@Test
	public void testPreconditionFailureMessage() {
		String m = "message";
		Exception e = new PreconditionFailureException(m);
		assertEquals(m, e.getMessage());
	}

}
