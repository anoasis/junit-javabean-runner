package com.google.code.junitjavabeanrunner;

import org.junit.runner.RunWith;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;
import com.google.code.junitjavabeanrunner.JavaBeanRunner.Property;

@RunWith(JavaBeanRunner.class)
@Fixture(ComplexBean.class)
public class ComplexBeanTest {
	@Property("name")
	public String name = "name";
}
