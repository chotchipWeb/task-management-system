package com.chotchip.task.execption;

public class TaskNotFoundException  extends BaseException{
    public TaskNotFoundException(String taskId) {
        super("Task which id: %s not found".formatted(taskId));
    }
}
