package com.wallet.blockchain_wallet.client.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * Ukradzonie ze stacka, ale zdaje się działać bezbłędnie
 * https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
 * .
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtils {

    /**
     * .
     * Też ze stacka:
     * https://stackoverflow.com/questions/434718/sockets-discover-port-availability-using-java
     */
    public static boolean isPortAvailable(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
