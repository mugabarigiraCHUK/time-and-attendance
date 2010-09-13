/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Berna
 */
public class Info extends LayoutContainer {

    private InfoServiceAsync dstoreSvc = GWT.create(InfoService.class);
    private ContentPanel panel = null;
    private Label label = new Label("Caricamento dati in corso...");

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new CenterLayout());

        panel = new ContentPanel();
        panel.setBodyStyle("padding: 6px");
        panel.setFrame(true);
        panel.setHeading("Informazioni di sistema");
        panel.setWidth(400);
        panel.setHeight(500);
        panel.add(label);
        caricaDati();
        add(panel);
    }

    private void caricaDati() {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(InfoService.class);
        }
        AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(ArrayList<String> result) {
                if (result != null) {
                    String str = "";
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        str = str + (String) it.next() + "<br><br>";
                    }
                    label.setText(str);
                }
            }
        };
        // Make the call to the stock price service.
        dstoreSvc.getInfo(callback);
    }
}
