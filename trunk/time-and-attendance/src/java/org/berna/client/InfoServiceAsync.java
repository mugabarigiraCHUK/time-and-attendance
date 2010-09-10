/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 *
 * @author Berna
 */
public interface InfoServiceAsync {

    public void getInfo(AsyncCallback<ArrayList<String>> asyncCallback);

}
