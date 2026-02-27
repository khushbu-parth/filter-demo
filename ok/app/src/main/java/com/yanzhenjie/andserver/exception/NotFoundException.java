package com.yanzhenjie.andserver.exception;

public class NotFoundException extends BaseException {
    public NotFoundException(String str) {
        super(404, String.format("The resource [%1$s] does not exist.", str));
    }
}
