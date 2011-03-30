package com.google.code.javabeanrunner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.javabeanrunner.JavaBeanRunner.Property;

class PropertyDataSource {
	private final Object source;
	private final Map<String, Object> memberMap = new HashMap<String, Object>();
	
	public PropertyDataSource(Object source) {
		this.source = source;
		init(source.getClass());
	}
	
	private void init(Class<?> type) {
		List<MemberAdapter> set = new ArrayList<MemberAdapter>();
		
		for (Field field : type.getDeclaredFields()) {
			set.add(MemberAdapter.wrap(field));
		}
		
		for (Method method : type.getDeclaredMethods()) {
			set.add(MemberAdapter.wrap(method));
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
				continue;
			}
			try {
				memberMap.put(property.value(), member.value(source));
			} catch (Exception e) {
				continue;
			}
		}
	}

	public boolean contains(String name) {
		return memberMap.containsKey(name);
	}
	
	public boolean isEmpty() {
		return memberMap.isEmpty();
	}
	
	public Object valueOf(String name) {
		return memberMap.get(name);
	}
}
