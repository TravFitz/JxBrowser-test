/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mytim.jxbrowsertest;

import com.teamdev.jxbrowser.browser.Browser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

public class BuildNewBrowser extends Task<Browser>{
    
    public static final BooleanProperty BUILDING = new SimpleBooleanProperty(false); 

    @Override
    protected Browser call() throws Exception {
        BUILDING.set(true);
        return MainApp.engine.newBrowser();
    }

    @Override
    protected void succeeded() {
        BUILDING.set(false);
        super.succeeded(); 
    }
}
