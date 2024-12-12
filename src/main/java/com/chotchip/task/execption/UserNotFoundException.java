package com.chotchip.task.execption;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String email){
        super("User with such %s not found, enter a correct email ".formatted(email));
    }
    public UserNotFoundException(Long id){
        super("User with such %d not found, enter a correct id ".formatted(id));
    }
}
