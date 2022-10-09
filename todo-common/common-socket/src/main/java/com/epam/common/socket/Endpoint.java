package com.epam.common.socket;

public class Endpoint {

    private String ip;

    private int port;

    public Endpoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return ip + ":" + port;
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
