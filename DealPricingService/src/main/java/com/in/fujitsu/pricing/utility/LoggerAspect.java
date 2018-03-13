package com.in.fujitsu.pricing.utility;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggerAspect {

	@Around("execution(public * com.in.fujitsu.pricing.controller.impl.*ControllerImpl.*(..))")
	public Object logAroundForControllerImpl(final ProceedingJoinPoint joinPoint) throws Throwable {
		return logMessage(joinPoint);
	}

	@Around("execution(* com.in.fujitsu.pricing.service..*(..))  "
			+ "|| execution(* com.in.fujitsu.pricing.utility..*(..)) "
			+ "|| execution(* com.in.fujitsu.pricing..*Helper.*(..))  "
			+ "|| execution(* com.in.fujitsu.pricing..*Calculator.*(..))" )
	public Object logAroundForServiceAndUtil(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object returnVal = null;
		final Logger logger = this.getLog(joinPoint);
		final String methodName = joinPoint.getSignature().getName();
		logger.info("Enter method [{}]", methodName);
		returnVal = joinPoint.proceed();
		logger.info("Exit method [{}] ", methodName);
		return returnVal;
	}

	private Object logMessage(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object returnVal = null;
		final Logger logger = this.getLog(joinPoint);
		final String methodName = joinPoint.getSignature().getName();
		try {
			final Object[] args = joinPoint.getArgs();
			final String arguments;
			if (args == null || args.length == 0) {
				arguments = "";
			} else {
				arguments = Arrays.deepToString(args);
			}
			logger.info("Enter method [{}] with arguments [{}]", methodName, arguments);
			returnVal = joinPoint.proceed();
			logger.info("Exit method [{}] with return value [{}].", methodName,
					(returnVal != null ? returnVal.toString() : "null"));
			return returnVal;
		} finally {
		}
	}


	protected Logger getLog(final JoinPoint joinPoint) {
		final Object target = joinPoint.getTarget();
		if (target != null) {
			return LoggerFactory.getLogger(target.getClass());
		}
		return LoggerFactory.getLogger(getClass());
	}
}