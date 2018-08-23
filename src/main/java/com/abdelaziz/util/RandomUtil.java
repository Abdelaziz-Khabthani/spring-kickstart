package com.abdelaziz.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.consts.SecurityConsts;

@Loggable(layer = ApplicationLayer.UTILITY_LAYER)
@Component
public class RandomUtil {

	/**
	 * Generate a password.
	 *
	 * @return the generated password
	 */
	public String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(SecurityConsts.RANDOM_DEF_COUNT);
	}

	/**
	 * Generate an activation key.
	 *
	 * @return the generated activation key
	 */
	public String generateActivationKey() {
		return RandomStringUtils.randomNumeric(SecurityConsts.RANDOM_DEF_COUNT);
	}

	/**
	 * Generate a reset key.
	 *
	 * @return the generated reset key
	 */
	public String generateResetKey() {
		return RandomStringUtils.randomNumeric(SecurityConsts.RANDOM_DEF_COUNT);
	}
}
