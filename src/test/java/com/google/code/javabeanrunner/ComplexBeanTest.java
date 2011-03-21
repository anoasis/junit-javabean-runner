package com.google.code.javabeanrunner;

import org.junit.runner.RunWith;

import com.google.code.javabeanrunner.JavaBeanRunner;
import com.google.code.javabeanrunner.JavaBeanRunner.Fixture;
import com.google.code.javabeanrunner.JavaBeanRunner.Property;

@RunWith(JavaBeanRunner.class)
@Fixture(ComplexBean.class)
public class ComplexBeanTest {
	@Property("name")
	public String name = "name";
}
