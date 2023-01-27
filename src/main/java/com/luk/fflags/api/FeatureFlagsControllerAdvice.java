package com.luk.fflags.api;

import com.luk.fflags.domain.fflags.FlagNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class FeatureFlagsControllerAdvice {

    @ExceptionHandler(FlagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void handle(FlagNotFoundException e) {
        log.debug(e.getMessage(), e);
    }
}
