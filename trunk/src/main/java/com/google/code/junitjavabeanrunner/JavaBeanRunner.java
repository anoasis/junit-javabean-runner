package com.google.code.junitjavabeanrunner;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class JavaBeanRunner extends Runner {
	private final Class<?> fixture;
	private final Constructor<?> constructor;
	private final Description description;
	private final Map<String, MemberAdapter> memberMap;
	private final Set<PropertyDescriptor> properties;
	
	public JavaBeanRunner(Class<?> testClass) throws Throwable {
		fixture = findFixture(testClass);
		properties = findMutableProperties(fixture);
		memberMap = findMembers(testClass, properties);
		constructor = findConstructor(fixture);
		if (properties.isEmpty()) {
			description = Description.EMPTY;
		} else {
			description = Description.createSuiteDescription(fixture);
			for (PropertyDescriptor prop : properties) {
				if (memberMap.containsKey(prop.getName()) == false) {
					continue;
				}
				Description childDesc = Description.createTestDescription(fixture, prop.getName());
				MemberAdapter member = memberMap.get(prop.getName());
				Object sourceValue = member.getValue(testClass.newInstance());
				Object target = constructor.newInstance();
				Statement stmt = new MutationStatement(sourceValue, target, prop.getReadMethod(), prop.getWriteMethod());
				stmt.evaluate();
				description.addChild(childDesc);
			}
		}
	}
	
	private Map<String, MemberAdapter> findMembers(Class<?> testClass, Set<PropertyDescriptor> props) throws InitializationError {
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		for (PropertyDescriptor prop : props) {
			map.put(prop.getName(), prop.getPropertyType());
		}
		Set<MemberAdapter> members = new HashSet<MemberAdapter>();
		
		for (Method method : testClass.getDeclaredMethods()) {
			members.add(MemberAdapter.wrap(method));
		}
		for (Field field : testClass.getDeclaredFields()) {
			members.add(MemberAdapter.wrap(field));
		}
		
		Map<String, MemberAdapter> memberMap = new HashMap<String, MemberAdapter>();
		for (MemberAdapter member : members) {
			if (isValidProperty(member, map)) {
				Property prop = member.getAnnotation(Property.class);
				if (memberMap.containsKey(prop.value())) {
					throw new InitializationError("Duplicate property member found: " + member);
				}
				memberMap.put(prop.value(), member);
			}
		}
		
		return memberMap;
	}
	
	private boolean isValidProperty(MemberAdapter member, Map<String, Class<?>> map) throws InitializationError {
		Property prop = member.getAnnotation(Property.class);
		if (prop == null) {
			return false;
		}
		String propName = prop.value();
		if (map.containsKey(propName) == false) {
			throw new InitializationError("@Property value does not match any property name");
		}
		Class<?> propType = map.get(propName);
		if (propType.isAssignableFrom(member.getType()) == false) {
			throw new InitializationError("Member type does not match property type");
		}
		if (member.getType().equals(void.class)) {
			throw new InitializationError("Member type must not return void");
		}
		if (Modifier.isPublic(member.getModifiers()) == false) {
			throw new InitializationError("Member must be public");
		}
		return true;
	}
	
	private Set<PropertyDescriptor> findMutableProperties(Class<?> fixtureClass) throws InitializationError {
		Set<PropertyDescriptor> propSet = new HashSet<PropertyDescriptor>();
		try {
			BeanInfo info = Introspector.getBeanInfo(fixtureClass, Object.class);
			PropertyDescriptor[] props = info.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				if (prop.getWriteMethod() != null) {
					propSet.add(prop);
				}
			}
		} catch (IntrospectionException e) {
			throw new InitializationError(e);
		}
		return propSet;
	}

	private Class<?> findFixture(Class<?> testClass) throws InitializationError {
		Fixture fixture = testClass.getAnnotation(Fixture.class);
		if (fixture == null) {
			throw new InitializationError("Fixture annotation not present");
		}
		return fixture.value();
	}

	private Constructor<?> findConstructor(Class<?> fixtureClass) throws InitializationError {
		if (fixtureClass.isInterface()) {
			throw new InitializationError("Fixture should not be an interface");
		}
		if (fixtureClass.isArray()) {
			throw new InitializationError("Fixture should not be an array");
		}
		if (fixtureClass.isEnum()) {
			throw new InitializationError("Fixture should not be an enum");
		}
		if (fixtureClass.isPrimitive()) {
			throw new InitializationError("Fixture should not be a primitive");
		}
		try {
			return fixtureClass.getConstructor();
		} catch (NoSuchMethodException e) {
			throw new InitializationError(e);
		} catch (SecurityException e) {
			throw new InitializationError(e);
		}
	}
	
	@Override
	public Description getDescription() {
		return description;
	}

	@Override
	public void run(RunNotifier notifier) {
		notifier.fireTestStarted(description);
		for (Description child : description.getChildren()) {
			notifier.fireTestStarted(child);
			notifier.fireTestFinished(child);
		}
		notifier.fireTestFinished(description);
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Fixture {
		Class<?> value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.METHOD})
	public @interface Property {
		String value();
	}
}
