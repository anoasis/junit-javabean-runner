package com.google.code.javabeanrunner;

import org.junit.runner.RunWith;

import com.google.code.javabeanrunner.JavaBeanRunner;
import com.google.code.javabeanrunner.JavaBeanRunner.Bean;
import com.google.code.javabeanrunner.JavaBeanRunner.Property;

@RunWith(JavaBeanRunner.class)
@Bean(ComplexBean.class)
public class ComplexBeanTest {
	@Property("name")
	public String name = "name";
	@Property("number")
	public int number = 1;
}
