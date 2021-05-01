package local.movement.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AppProperties {

    public static class Localisation {
        private static final String BUNDLE_NAME = "localization.Messages";
        public static final ResourceBundle messages = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static final String TITLE = "Local Movement";
    @Getter
    private static int bufferLength = 8192;
    @Getter
    @Setter
    private static int port = 22022;

    //todo whaaat
    @Getter
    private static ExecutorService executorService =
            Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = Executors.defaultThreadFactory().newThread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });

}
