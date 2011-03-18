package com.google.code.junitjavabeanrunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.code.junitjavabeanrunner.JavaBeanRunner.Property;

class MemberFinder {
	private final Class<?> type;
	
	public MemberFinder(Class<?> type) {
		this.type = type;
	}

	public Set<MemberAdapter> findMembers() {
		Set<MemberAdapter> members = new HashSet<MemberAdapter>();
		
		for (Method method : type.getDeclaredMethods()) {
			members.add(MemberAdapter.wrap(method));
		}
		
		for (Field field : type.getDeclaredFields()) {
			members.add(MemberAdapter.wrap(field));
		}
		
		Iterator<MemberAdapter> iter = members.iterator();
		while (iter.hasNext()) {
			MemberAdapter member = iter.next();
			Property property = member.getAnnotation(Property.class);
			if (property == null) {
				iter.remove();
			}
			if (member.getType().equals(void.class)) {
				iter.remove();
			}
			if (member.getParameterTypes().length > 0) {
				iter.remove();
			}
		}
		
		return members;
	}
}
