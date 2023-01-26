package com.luk.fflags.domain.fflags;

public class FlagNotFoundException extends RuntimeException{

    public FlagNotFoundException(String message) {
        super(message);
    }
}
