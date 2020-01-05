package edu.sandau;

import edu.sandau.tomcat.Tomcat8;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class OnlineExamApplication {
    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.install();
        Thread tomcat8 = new Thread(new Tomcat8());
        tomcat8.start();
        init();
    }

    private static void init() throws Exception {}

}
