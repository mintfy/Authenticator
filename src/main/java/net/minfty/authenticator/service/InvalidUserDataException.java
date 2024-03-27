package net.minfty.authenticator.service;

public class InvalidUserDataException extends Exception{
    public InvalidUserDataException(String message) {
        super(message);
    }
}
