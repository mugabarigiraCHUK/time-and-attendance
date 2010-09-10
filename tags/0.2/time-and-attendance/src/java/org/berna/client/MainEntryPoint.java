/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Main entry point.
 *
 * @author Berna
 */
public class MainEntryPoint implements EntryPoint {
    public static MainLayout start = null;

    public MainEntryPoint() {
    }

    public void onModuleLoad() {
        start = new MainLayout();
        RootPanel.get().add(start);
    }
}
