package com.est.workflow.process.exception;

public class WfTaskIsAlreadyRecievedException extends Exception {

	@Override
	public String getMessage() {
		return "任务已经被接收，不能重复接收！";
	}

}
