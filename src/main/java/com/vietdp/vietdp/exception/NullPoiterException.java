package com.vietdp.vietdp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.ACCEPTED)
public class NullPoiterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 507670480353248835L;

	public NullPoiterException(String msg) {
		super(msg);
	}

}
