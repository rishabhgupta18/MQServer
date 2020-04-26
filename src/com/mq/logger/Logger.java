package com.mq.logger;

public class Logger {

	private String className;
	
	public Logger(String classStrName) {
		this.className = classStrName;
	}

	public Logger(Class<? extends Object> className) {
		this.className = className.getSimpleName();
	}

	public void info(String message) {
		System.out.println("[" + className + "] " + message + " ... ");
	}

	public void error(String message) {
		System.err.println("[" + className + "] " + message + " ... ");
	}
	
	public String getName() {
		return className;
	}

}
