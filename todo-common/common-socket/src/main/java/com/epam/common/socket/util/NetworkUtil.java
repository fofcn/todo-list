package com.epam.common.socket.util;

import com.epam.common.core.exception.ExceptionUtil;
import org.apache.commons.lang3.NotImplementedException;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtil {

    private NetworkUtil() {

    }

    private static List<NetworkInterface> listPhysicalInterface(List<NetworkInterface> interfaces) {
        throw new NotImplementedException();
    }

    private static List<NetworkInterface> listInterface() {
        List<NetworkInterface> networkInterfaces = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                networkInterfaces.add(networkInterface);
            }
        } catch (SocketException e) {
            ExceptionUtil.ignore(e);
        }

        return networkInterfaces;
    }
}
