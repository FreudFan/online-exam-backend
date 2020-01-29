package edu.sandau.security;

/***
 * 本地账号操作异常
 */
public class LocalAuthOperationException extends RuntimeException {
	public LocalAuthOperationException() {
		super("session操作异常");
	}

	public LocalAuthOperationException(String message) {
		super(message);
	}
}