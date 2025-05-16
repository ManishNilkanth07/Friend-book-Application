package com.webkorps.friendBook.exceptions;

public class FileStoreException extends RuntimeException {
    public FileStoreException(String failedToUploadPhoto) {
        super(failedToUploadPhoto);
    }
}
