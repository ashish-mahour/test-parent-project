package com.test.gateway.models;

public class ErrorResponse {
	private String message;
	private String traceID;
	private String spanID;

	public String getTraceID() {
		return traceID;
	}

	public void setTraceID(String traceID) {
		this.traceID = traceID;
	}

	public String getSpanID() {
		return spanID;
	}

	public void setSpanID(String spanID) {
		this.spanID = spanID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorResponse(String message, String traceID, String spanID) {
		super();
		this.message = message;
		this.traceID = traceID;
		this.spanID = spanID;
	}

	public ErrorResponse(String message) {
		super();
		this.message = message;
	}
}
