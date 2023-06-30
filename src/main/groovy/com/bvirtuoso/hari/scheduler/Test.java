package com.bvirtuoso.hari.scheduler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.TimeZone;

public class Test {
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress.isLoopbackAddress() || !inetAddress.getHostAddress().contains(":")) {
                        continue;
                    }
                    if (isPreferredIPv6Address(inetAddress)) {
                        System.out.println("Preferred IPv6 Address: " + inetAddress.getHostAddress());
                        return; // Assuming you want to retrieve only the first preferred address found
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPreferredIPv6Address(InetAddress inetAddress) {
        // Add your preferred conditions for selecting the IPv6 address
        // Example: Select the address with a specific prefix or pattern
        String hostAddress = inetAddress.getHostAddress();
        return hostAddress.startsWith("2405:201");
    }
}
