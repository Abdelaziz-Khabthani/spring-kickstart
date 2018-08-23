package com.abdelaziz.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.abdelaziz.consts.ApplicationLayer;

@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface Loggable {
	ApplicationLayer layer();
}