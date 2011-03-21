package com.google.code.javabeanrunner;

class PreconditionFailureException extends IllegalStateException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4819429469124437155L;

	public PreconditionFailureException(Throwable cause) {
		super(cause);
	}
	
	public PreconditionFailureException(String message) {
		super(message);
	}
}
