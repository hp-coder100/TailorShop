package com.cts.hemant.tailorshop.exception;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionModel {

	private final String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
	private String statusCode;
	private String message;
	private String url;
	private String methodType;
	
	
}
