package com.google.code.junitjavabeanrunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Property;

class MemberFinder {
	private final Class<?> type;
	
	public MemberFinder(Class<?> type) {
		this.type = type;
	}

	public Map<String, MemberAdapter> findMembers() throws IllegalStateException {
		Set<MemberAdapter> set = new HashSet<MemberAdapter>();
		Map<String, MemberAdapter> map = new HashMap<String, MemberAdapter>();
		
		for (Method method : type.getDeclaredMethods()) {
			set.add(MemberAdapter.wrap(method));
		}
		
		for (Field field : type.getDeclaredFields()) {
			set.add(MemberAdapter.wrap(field));
		}
		
		for (MemberAdapter member : set) {
			Property property = member.getAnnotation(Property.class);
			if (property == null) {
				continue;
			}
			if (member.getType().equals(void.class)) {
				continue;
			}
			if (member.getParameterTypes().length > 0) {
				continue;
			}
			if (Modifier.isPublic(member.getModifiers()) == false) {
				continue;
			}
			if (map.containsKey(property.value())) {
				throw new IllegalStateException("Duplication property name: " + property.value());
			}
			map.put(property.value(), member);
		}
		
		return map;
	}
}
