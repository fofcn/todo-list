package com.epam.common.web.common;

import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class MockRequestAttributes extends ServletRequestAttributes {

    public MockRequestAttributes(HttpServletRequest request) {
        super(request);
    }

    @Override
    protected void updateAccessedSessionAttributes() {

    }

    @Override
    public Object getAttribute(String name, int scope) {
        return null;
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {

    }

    @Override
    public void removeAttribute(String name, int scope) {

    }

    @Override
    public String[] getAttributeNames(int scope) {
        return new String[0];
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback, int scope) {

    }

    @Override
    public Object resolveReference(String key) {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Object getSessionMutex() {
        return null;
    }
}
