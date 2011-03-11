package com.google.code.junitjavabeanrunner;

import org.junit.runner.RunWith;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Fixture;

@RunWith(JavaBeanRunner.class)
public class SimpleBeanTest {
	@Fixture
	public static SimpleBean getFixture() {
		SimpleBean bean = new SimpleBean();
		bean.setValue("value");
		
		return bean;
	}
}
