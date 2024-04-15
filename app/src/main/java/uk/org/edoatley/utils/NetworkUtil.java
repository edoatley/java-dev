package uk.org.edoatley.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class NetworkUtil {

    public static int nextFreePort() throws IOException {
        try (ServerSocket tempSocket = new ServerSocket(0)) {
            return tempSocket.getLocalPort();
        }
    }
}
