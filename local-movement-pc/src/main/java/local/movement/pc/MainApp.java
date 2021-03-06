package local.movement.pc;

import local.movement.common.AppProperties;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MainApp extends Application {

    @Getter private static MainApp instance;
    @Getter private static Stage primaryStage;
    @Getter private static ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setDaemon(true);
            return thread;
        }
    });

    public MainApp() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle(AppProperties.TITLE);
        initScene();
    }

    @Override
    public void stop() {
        executorService.shutdown();
    }

    private void initScene() throws IOException {
        Parent rootPane = ViewLoader.loadView("view/MainView.fxml");
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
