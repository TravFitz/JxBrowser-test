package com.mytim.jxbrowsertest;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.browser.callback.input.PressKeyCallback;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.ProprietaryFeature;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.ui.KeyCode;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import java.io.File;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class MainApp extends Application {
    
    public static Browser browser;
    public static Engine engine;
    public static Stage ms;
    private static Stage jxStage;
    private static BooleanProperty destroyTheBrowser = new SimpleBooleanProperty(false);

    @Override
    public void start(Stage stage) throws Exception {
        ms = stage;
        jxStage = new Stage();
        
        Thread parentThread = Thread.currentThread(); 
        
        System.setProperty("jxbrowser.license.key", "*enter own licence key here*");
 
        BuildBrowserEngine engineT = new BuildBrowserEngine(EngineOptions.newBuilder(RenderingMode.HARDWARE_ACCELERATED)
                    .userDataDir(new File("/home/mytim/.myTiM/data").toPath())
                    .enableProprietaryFeature(ProprietaryFeature.AAC)
                    .enableProprietaryFeature(ProprietaryFeature.H_264)
                    .chromiumDir(new File("/home/mytim/.myTiM/browser").toPath())
                .build());
        
        engineT.setOnSucceeded((eh) -> {
            engine = engineT.getValue();
        });
        Processor.addProcess(null, engineT, parentThread);
        
        BuildBrowserEngine.BUILDING.addListener((ChangeListener)(ObservableValue obv, Object oldv, Object newv) -> {
            if (newv.equals(false)) {
                BuildNewBrowser browse = new BuildNewBrowser();
                browse.setOnSucceeded((eh) -> {
                    browser = browse.getValue();
                });
                Processor.addProcess(null, browse, parentThread);
            }
        });
        
        BuildNewBrowser.BUILDING.addListener((ChangeListener)(ObservableValue obv, Object oldv, Object newv) -> {
            if (newv.equals(false)) {
                browser.navigation().loadUrl("https://www.redbull.tv");

                BrowserView bv = BrowserView.newInstance(browser);

                StackPane bp = new StackPane(bv);
                bp.setPrefSize(800, 800);

                Scene jxScene = new Scene(bp);

                destroyTheBrowser.addListener((ChangeListener) (ObservableValue obV, Object oldV, Object newV) -> {
                    if (newV.equals(true)) {
                        browser.focusedFrame().get().loadHtml("<html><body></body></html>");
                        PauseTransition pt = new PauseTransition(Duration.millis(500));
                        pt.setOnFinished(eh2 -> {
                            destroyBrowser();
                            destroyTheBrowser.setValue(false);
                        });
                        pt.play();
                    }
                });

                browser.set(PressKeyCallback.class, params -> {
                    if (params.event().keyCode() == KeyCode.KEY_CODE_HOME) {
                        destroyTheBrowser.setValue(true);
                        return PressKeyCallback.Response.suppress();
                    }
                    return PressKeyCallback.Response.proceed();
                });
                jxStage.setScene(jxScene);
                stage.hide();
                jxStage.show();
            } 
        });
        
        StackPane sp = new StackPane();
        
        sp.setPrefSize(800,800);
        Scene scene = new Scene(sp);
        
        stage.setScene(scene);
        stage.show();
        
        stage.setOnCloseRequest(event -> System.exit(0));

    }
    
    public static void destroyBrowser() {
        System.out.println("Home button pressed, closing browser");
        browser.remove(PressKeyCallback.class);
        jxStage.setOnCloseRequest(event -> browser.close());
        jxStage.close();
        ms.show();
        System.out.println("Browser has been destroyed");
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
