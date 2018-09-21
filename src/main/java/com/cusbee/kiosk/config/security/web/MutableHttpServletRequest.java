package com.cusbee.kiosk.config.security.web;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;
    private final Map<String, String> customParameters;

    public MutableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
        this.customParameters = new HashMap<>();
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return customParameters.getOrDefault(name, ((HttpServletRequest) getRequest()).getHeader(name));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());
        Enumeration<String> headerNames = ((HttpServletRequest) getRequest()).getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String n = headerNames.nextElement();
            set.add(n);
        }
        return Collections.enumeration(set);
    }

    @Override
    public Map<String, String[]> getParameterMap()
    {
        Map<String, String[]> map = new LinkedHashMap<>();
        map.putAll(super.getParameterMap());
        customParameters.forEach((key, value) -> map.put(key, new String[]{value}));

        return Collections.unmodifiableMap(map);
    }

    @Override
    public String getParameter(String name) {
        return customParameters.getOrDefault(name, getRequest().getParameter(name));
    }

    public void putHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    public void putParameter(String name, String value) {
        customParameters.put(name, value);
    }
}