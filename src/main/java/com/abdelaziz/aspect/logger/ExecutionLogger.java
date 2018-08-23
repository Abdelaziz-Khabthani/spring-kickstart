package com.abdelaziz.aspect.logger;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.util.LoggerUtil;

@Aspect
@Configuration
public class ExecutionLogger {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionLogger.class);

	@Autowired
	private LoggerUtil loggerUtil;
	
	@Around("execution(* (@com.abdelaziz.annotations.Loggable *).*(..))")
	private Object genericLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		if (!LOGGER.isTraceEnabled()) {
			try {
				return proceedingJoinPoint.proceed();
			} catch (Throwable throwable) {
				LOGGER.error("[ERROR] ", throwable);
			}
		}

		String logMessage = null;
		try {
			MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
			Method method = signature.getMethod();
			Class<?> declaringClass = method.getDeclaringClass();
			Loggable loggable = declaringClass.getAnnotation(Loggable.class);

			logMessage = loggerUtil.buildLogMesssage(proceedingJoinPoint.getTarget().getClass().getName(), proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getArgs(), loggable.layer());
		} catch (Throwable throwable) {
			LOGGER.error("[ASPECT ERROR] Something went wrong when creating log message", throwable);
			return proceedingJoinPoint.proceed();
		}

		if (logMessage == null) {
			return proceedingJoinPoint.proceed();
		}

		try {
			LOGGER.trace("BEGIN: " + logMessage.toString());
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object result = proceedingJoinPoint.proceed();
			stopWatch.stop();
			LOGGER.trace("END: [SUCCESS] [" + stopWatch.getTotalTimeMillis() + "ms] [" + result + "] " + logMessage.toString());

			return result;
		} catch (Throwable throwable) {
			LOGGER.error("END: [ERROR] " + logMessage.toString(), throwable);
			throw throwable;
		}
	}

	// Logging repository layer manually, because of a known limitaion with JDK
	// reflection
	@Around("execution(* org.springframework.data.repository.Repository+.*(..))")
	private Object repositoryLayerLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		if (!LOGGER.isTraceEnabled()) {
			try {
				return proceedingJoinPoint.proceed();
			} catch (Throwable throwable) {
				LOGGER.error("[ERROR] ", throwable);
			}
		}

		String logMessage = null;
		try {
			// Check if there is an interface that has the @Loggable annotation
			boolean isLoggable = false;
			String repositoryInterfaceName = null;

			for (Class<?> currentInterface : proceedingJoinPoint.getTarget().getClass().getInterfaces()) {
				if (currentInterface.getAnnotationsByType(Loggable.class).length != 0) {
					isLoggable = true;
					repositoryInterfaceName = currentInterface.getName();
					break;
				}
			}
			if (!isLoggable) {
				return proceedingJoinPoint.proceed();
			}

			logMessage = loggerUtil.buildLogMesssage(repositoryInterfaceName, proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getArgs(), ApplicationLayer.REPOSITORY_LAYER);

		} catch (Throwable throwable) {
			LOGGER.error("[ASPECT ERROR] Something went wrong when creating log message or deciding if a Repository class is Loggable", throwable);
			return proceedingJoinPoint.proceed();
		}

		if (logMessage == null) {
			return proceedingJoinPoint.proceed();
		}

		try {
			LOGGER.trace("BEGIN: " + logMessage.toString());
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object result = proceedingJoinPoint.proceed();
			stopWatch.stop();
			LOGGER.trace("END: [SUCCESS] [" + stopWatch.getTotalTimeMillis() + "ms] [" + result + "] " + logMessage.toString());

			return result;
		} catch (Throwable throwable) {
			LOGGER.error("END: [ERROR] " + logMessage.toString(), throwable);
			throw throwable;
		}

	}
}
