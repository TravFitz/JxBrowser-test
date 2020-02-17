package com.mytim.jxbrowsertest;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.setProperty("jxbrowser.license.key", "*insert test key here*");
 
        Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED).build());
        
        Browser browser = engine.newBrowser();
        
        browser.navigation().loadUrl("https://iview.abc.net.au");
        
        BrowserView bv = BrowserView.newInstance(browser);
        
        BorderPane bp = new BorderPane(bv);
        bp.setPrefSize(800, 800);
        Pane root = new Pane();
        
        root.setPrefSize(850,800);
        
        root.getChildren().add(bp);
        bv.setLayoutX(0);
        Pane closejx = new Pane();
        closejx.setPrefSize(50,50);
        closejx.setStyle("-fx-background-color: black;");
        closejx.setOnMouseClicked((eh) -> {
            browser.close();
            engine.close();
        });
        closejx.setLayoutX(800);
        closejx.setLayoutY(0);
        
        Pane close = new Pane();
        close.setPrefSize(50,50);
        close.setStyle("-fx-background-color: blue;");
        close.setOnMouseClicked((eh) -> {
            stage.close();
        });
        close.setLayoutX(800);
        close.setLayoutY(50);
        
        root.getChildren().addAll(closejx,close);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.show();
        
        stage.setOnCloseRequest(event -> engine.close());
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
