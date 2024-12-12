package com.chotchip.task.execption;

public class UserNotRightsException extends BaseException{
    public UserNotRightsException(){
        super("User not rights to this task");
    }
}
