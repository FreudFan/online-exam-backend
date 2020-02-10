package edu.sandau;

import edu.sandau.tomcat.Tomcat8;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlineExamApplication {
    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.install();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Tomcat8());
    }

}
