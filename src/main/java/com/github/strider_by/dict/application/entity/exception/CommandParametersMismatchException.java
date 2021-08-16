package com.github.strider_by.dict.application.entity.exception;

public class CommandParametersMismatchException extends RuntimeException {
    
    public CommandParametersMismatchException() {
        
    }
    
    public CommandParametersMismatchException(String msg) {
        super(msg);
    }
    
    public CommandParametersMismatchException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public CommandParametersMismatchException(Throwable cause) {
        super(cause);
    }
    
}
