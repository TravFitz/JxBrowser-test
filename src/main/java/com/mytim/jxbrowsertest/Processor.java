/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mytim.jxbrowsertest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import javafx.concurrent.Task;

public class Processor {
    
    private static final ExecutorService POOL = Executors.newCachedThreadPool();
    
    public static void addProcess(Runnable toRun,Task toTask, Thread testThread) {
        try {
            if (Thread.currentThread() == testThread) {
                if (toRun != null) {
                    POOL.submit(toRun);
                } else {
                    POOL.submit(toTask);
                }
            } else {
                if (toRun != null) {
                    POOL.execute(toRun);
                } else {
                    POOL.execute(toTask);
                }
            }
        } catch (NullPointerException npe) {
            System.out.println("Task was unable to multithread due to: "+npe.toString());
        } catch (RejectedExecutionException ree) {
            System.out.println("Task Failed to add to new thread due to "+ree);
        }
    }
}
