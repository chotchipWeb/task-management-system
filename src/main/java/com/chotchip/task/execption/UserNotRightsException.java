package com.chotchip.task.execption;

public class UserNotRightsException extends RuntimeException{
    public UserNotRightsException(){
        super("User not rights to this task");
    }
}