package org.yy.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {

    /**
     * 检验是否为ipv4地址
     * @param ipAddr
     * @return
     */
    public static boolean isValidIpv4Addr(String ipAddr) {
        String regex = "(^((22[0-3]|2[0-1][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})"
                + "([.](25[0-5]|2[0-4][0-9]|[0-1][0-9][0-9]|([0-9]){1,2})){3})$)";

        if (ipAddr == null) {
            System.out.println("ip addresss is null ");
            return false;
        }
        ipAddr = Normalizer.normalize(ipAddr, Form.NFKC);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddr);
        boolean match = matcher.matches();
        if (!match) {
            // System.out.println("invalid ip addresss = " + ipAddr);
        }
        return match;
    }


    /**
     * 通用获取本机IP
     * @return
     */
    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("IP地址获取失败" + e.toString());
        }
        return "";
    }

}
