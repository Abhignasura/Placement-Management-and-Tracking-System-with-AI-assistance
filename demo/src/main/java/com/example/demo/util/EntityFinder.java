package com.example.demo.util;

import com.example.demo.exception.ApiException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EntityFinder {

    public <T> T get(Optional<T> optional, String message) {

        return optional.orElseThrow(() -> new ApiException(message));
    }
}