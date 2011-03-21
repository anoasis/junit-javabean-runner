package com.google.code.javabeanrunner;

class ParameterFactory {

	public boolean newInstance(Class<Boolean> type) {
		return Boolean.TRUE;
	}

	public byte newInstance(Class<Byte> type) {
		return Byte.MAX_VALUE;
	}

	public char newInstance(Class<Character> type) {
		return Character.MAX_VALUE;
	}
	
	public short newInstance(Class<Short> type) {
		return Short.MAX_VALUE;
	}
	
	public int newInstance(Class<Integer> type) {
		return Integer.MAX_VALUE;
	}
	
	public long newInstance(Class<Long> type) {
		return Long.MAX_VALUE;
	}
	
	public float newInstance(Class<Float> type) {
		return Float.MAX_VALUE;
	}
	
	public double newInstance(Class<Double> type) {
		return Double.MAX_VALUE;
	}

	public Object newInstance(Class<?> type) {
		try {
			return type.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public boolean isInstantiatable(Class<?> type) {
		try {
			type.getConstructor();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
