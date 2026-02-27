package com.yanzhenjie.andserver.exception;

import com.yanzhenjie.andserver.RequestMethod;

public class MethodNotSupported extends BaseException {
    public MethodNotSupported(RequestMethod requestMethod) {
        super(405, String.format("The %1$s method is not supported.", requestMethod.getValue()));
    }
}
