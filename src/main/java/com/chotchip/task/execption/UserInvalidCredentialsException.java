package com.chotchip.task.execption;

public class UserInvalidCredentialsException extends BaseException{
    public UserInvalidCredentialsException() {
        super("Invalid credentials");
    }
    public UserInvalidCredentialsException(String email){
        super("Email: %s is busy".formatted(email));
    }
}
