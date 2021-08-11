package com.github.strider_by.dict.dao.exception;

public class BrokenIntegrityException extends RuntimeException {
    
    public BrokenIntegrityException() {
        
    }
    
    public BrokenIntegrityException(String msg) {
        super(msg);
    }
    
    public BrokenIntegrityException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public BrokenIntegrityException(Throwable cause) {
        super(cause);
    }
    
}
