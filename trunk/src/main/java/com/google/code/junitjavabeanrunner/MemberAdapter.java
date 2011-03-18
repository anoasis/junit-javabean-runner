package com.google.code.junitjavabeanrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

abstract class MemberAdapter {
	private final Member member;
	
	public MemberAdapter(Member member) {
		this.member = member;
	}

	public static MemberAdapter wrap(Method method) {
		return new MethodWrapper(method);
	}

	public static MemberAdapter wrap(Field field) {
		return new FieldWrapper(field);
	}
	
	public abstract <T extends Annotation> T getAnnotation(Class<T> annotation);
	public abstract Class<?> getType();
	public abstract Object getValue(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	public Class<?>[] getParameterTypes() {
		return new Class<?>[0];
	}
	
	@Override
	public final String toString() {
		return member.toString();
	}
	
	public final int getModifiers() {
		return member.getModifiers();
	}
	
	private static class MethodWrapper extends MemberAdapter {
		private final Method method;
		
		public MethodWrapper(Method method) {
			super(method);
			this.method = method;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotation) {
			return method.getAnnotation(annotation);
		}

		@Override
		public Class<?> getType() {
			return method.getReturnType();
		}

		@Override
		public Object getValue(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			return method.invoke(target);
		}
		
		@Override
		public Class<?>[] getParameterTypes() {
			return method.getParameterTypes();
		}
	}
	
	private static class FieldWrapper extends MemberAdapter {
		private final Field field;
	
		public FieldWrapper(Field field) {
			super(field);
			this.field = field;
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotation) {
			return field.getAnnotation(annotation);
		}

		@Override
		public Class<?> getType() {
			return field.getType();
		}

		@Override
		public Object getValue(Object target) throws IllegalArgumentException, IllegalAccessException {
			return field.get(target);
		}
	}
}
	
