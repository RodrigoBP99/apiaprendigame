package com.aprendigame.apredigameapi.exception;

public class AutenticationError extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AutenticationError(String msg) {
		super(msg);
	}

}
