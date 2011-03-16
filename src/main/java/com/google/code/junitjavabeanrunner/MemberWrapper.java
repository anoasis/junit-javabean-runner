package com.google.code.junitjavabeanrunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public abstract class MemberWrapper {
	private final Member member;
	
	public MemberWrapper(Member member) {
		this.member = member;
	}

	public static MemberWrapper wrap(Method method) {
		return new MethodWrapper(method);
	}

	public static MemberWrapper wrap(Field field) {
		return new FieldWrapper(field);
	}
	
	public abstract <T extends Annotation> T getAnnotation(Class<T> annotation);
	public abstract Class<?> getType();
	
	public final int getModifiers() {
		return member.getModifiers();
	}
	
	private static class MethodWrapper extends MemberWrapper {
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
	}
	
	private static class FieldWrapper extends MemberWrapper {
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
	}
}
	
