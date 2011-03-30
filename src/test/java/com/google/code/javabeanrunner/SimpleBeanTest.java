package com.google.code.javabeanrunner;

import org.junit.runner.RunWith;

import com.google.code.javabeanrunner.JavaBeanRunner.Bean;
import com.google.code.javabeanrunner.JavaBeanRunner.Property;

@RunWith(JavaBeanRunner.class)
@Bean(SimpleBean.class)
public class SimpleBeanTest {
	@Property("value")
	public String value = "";
}
