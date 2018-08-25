package com.abdelaziz.util;

import org.springframework.stereotype.Component;

import com.abdelaziz.consts.ApplicationLayer;

@Component
public class LoggerUtil {

	public String buildLogMesssage(String className, String methodName, Object[] args, ApplicationLayer layer) {
		StringBuffer logMessage = new StringBuffer();
		logMessage.append("[" + layer + "] ");
		logMessage.append(className);
		logMessage.append(".");
		logMessage.append(methodName);
		logMessage.append("(");

		for (int i = 0; i < args.length; i++) {
			logMessage.append(args[i]).append(",");
		}

		if (args.length > 0) {
			logMessage.deleteCharAt(logMessage.length() - 1);
		}

		logMessage.append(")");
		return logMessage.toString();
	}
}
