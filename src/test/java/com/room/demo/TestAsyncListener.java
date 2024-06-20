package com.room.demo;


import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class TestAsyncListener implements AsyncListener {
    @Override
    public void onComplete (AsyncEvent asyncEvent) throws IOException {
        /* Do nothing */
    }

    @Override
    public void onError (AsyncEvent asyncEvent) throws IOException {
        /* Do nothing */
    }

    @Override
    public void onStartAsync (AsyncEvent asyncEvent) throws IOException {
        /* Do nothing */
    }

    @Override
    public void onTimeout (AsyncEvent asyncEvent) throws IOException {
        /* Handle timeout */
        ServletResponse response = asyncEvent.getAsyncContext().getResponse();
        response.getWriter().print("Timeout Error in Processing");
    }
}