package com.chotchip.task.execption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email){
        super("User with such not found %s, enter a correct email ".formatted(email));
    }
}
