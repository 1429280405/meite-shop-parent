package com.mayiket.base;

import lombok.Data;

@Data
public class BaseResponse<T> {

	private Integer code;
	private String msg;
	private T data;

	public BaseResponse() {

	}

	public BaseResponse(Integer rtnCode, String msg, T data) {
		super();
		this.code = rtnCode;
		this.msg = msg;
		this.data = data;
	}

}