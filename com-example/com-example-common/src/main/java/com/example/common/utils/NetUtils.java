package com.example.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Description 网络工具类
 * @PackagePath com.example.common.utils.NetUtils
 * @Author YINZHIYU
 * @Date 2020/5/8 13:49
 * @Version 1.0.0.0
 **/
public class NetUtils {

    public static String getLocalIP() {
        String localIP = "127.0.0.1";
        try {
            OK:
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        localIP = address.getHostAddress();
                        break OK;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localIP;
    }
}
