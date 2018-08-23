package com.abdelaziz.consts;

public class SecurityConsts {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;
    
	public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

	public static final int RANDOM_DEF_COUNT = 20;

	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	public static final String ROLE_USER = "ROLE_USER";

	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	
    public static final String SYSTEM_ACCOUNT = "system";
    
    public static final String ANONYMOUS_USER = "anonymoususer";
    
    public static final String AUTHORIZATION_HEADER = "Authorization";
}
