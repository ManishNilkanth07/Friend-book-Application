package com.webkorps.friendBook.exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String userIsNotAuthenticated) {
        super(userIsNotAuthenticated);
    }
}
