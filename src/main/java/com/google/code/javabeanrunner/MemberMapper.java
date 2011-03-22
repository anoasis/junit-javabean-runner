package com.google.code.javabeanrunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.code.javabeanrunner.JavaBeanRunner.Property;

class MemberMapper {
	private final Map<String, MemberAdapter> memberMap = new HashMap<String, MemberAdapter>();
	
	public MemberMapper(Class<?> type) {
		init(type);
	}
	
	private void init(Class<?> type) {
		Set<MemberAdapter> set = new HashSet<MemberAdapter>();
		
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
			if (memberMap.containsKey(property.value())) {
				throw new IllegalStateException("Duplication property name: " + property.value());
			}
			memberMap.put(property.value(), member);
		}
	}

	public boolean contains(String name) {
		return memberMap.containsKey(name);
	}
	
	public boolean isEmpty() {
		return memberMap.isEmpty();
	}

	public MemberAdapter get(String name) {
		return memberMap.get(name);
	}
}
