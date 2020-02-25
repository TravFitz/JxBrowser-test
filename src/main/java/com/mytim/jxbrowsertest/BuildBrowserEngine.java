
package com.mytim.jxbrowsertest;

import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

public class BuildBrowserEngine extends Task<Engine>{
    public static final BooleanProperty BUILDING = new SimpleBooleanProperty(false);
    
    private final EngineOptions eos;
    
    public BuildBrowserEngine(EngineOptions eo) {
        this.eos = eo;
        BuildBrowserEngine.BUILDING.set(true);
    }

    @Override
    protected Engine call() throws Exception {
        return Engine.newInstance(eos);
    }
    
    public static boolean isBuilding() {
        return BUILDING.getValue();
    }

    @Override
    protected void succeeded() {
        BUILDING.set(false);
        super.succeeded();
    }
}
