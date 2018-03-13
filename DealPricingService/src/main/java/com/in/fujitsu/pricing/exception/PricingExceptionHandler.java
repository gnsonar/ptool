package com.in.fujitsu.pricing.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.in.fujitsu.pricing.dto.ErrorResponse;

@RestControllerAdvice
public class PricingExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(PricingExceptionHandler.class);

	@ExceptionHandler(SystemException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse systemExceptionHandler(Exception ex, WebRequest request) {
		return constructErrorResponse(ex, request);
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse serviceExceptionHandler(Exception ex, WebRequest request) {
		return constructErrorResponse(ex, request);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse userNotFoundExceptionHandler(Exception ex, WebRequest request) {
		return constructErrorResponse(ex, request);
	}

	@ExceptionHandler(UnauthorizedAccessException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse unauthorizedAccessExceptionHandler(Exception ex, WebRequest request) {
		return constructErrorResponse(ex, request);
	}

	@ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse genericExceptionHandler(Exception ex, WebRequest request) {
		return constructErrorResponse(ex, request);
	}

	private ErrorResponse constructErrorResponse(Exception ex, WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setMessage(ex.getMessage());
		error.setStackTrace(getStackMsg(ex));
		error.setPath(request.getDescription(false));
		if(null != request.getUserPrincipal()){
			error.setUser(request.getUserPrincipal().getName());
		}
		LOGGER.error("Error message : {}", error);
		return error;
	}

	private String getStackMsg(Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString()).append("\n");
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (StackTraceElement trace : stackTrace) {
			sb.append(trace.getClassName() + "." + trace.getMethodName() + ":" + trace.getLineNumber() + "\n");
		}
		return sb.toString();
	}

}
