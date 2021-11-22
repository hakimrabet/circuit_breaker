package com.hamrasta;

@SuppressWarnings("serial")
class ShakyServiceException extends RuntimeException {

    public ShakyServiceException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}