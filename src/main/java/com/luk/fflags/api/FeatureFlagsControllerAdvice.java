package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FlagNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FeatureFlagsControllerAdvice {

    @ExceptionHandler(FlagNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    void handle(FlagNotFoundException e){

    }
}
