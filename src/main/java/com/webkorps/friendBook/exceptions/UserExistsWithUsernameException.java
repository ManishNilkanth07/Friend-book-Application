package com.webkorps.friendBook.exceptions;

public class UserExistsWithUsernameException extends RuntimeException {

    public UserExistsWithUsernameException(String message) {
        super(message);
    }


}
