package com.VietBlog.exception;

public class UserAlreadyBlockedException extends RuntimeException {

    public UserAlreadyBlockedException() {
        super();
    }

    public UserAlreadyBlockedException(String message) {
        super(message);
    }

    public UserAlreadyBlockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyBlockedException(Throwable cause) {
        super(cause);
    }
}