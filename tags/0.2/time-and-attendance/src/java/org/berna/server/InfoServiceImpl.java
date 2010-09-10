/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;

import org.berna.client.InfoService;

/**
 *
 * @author Berna
 */
public class InfoServiceImpl extends RemoteServiceServlet implements InfoService {

    public ArrayList<String> getInfo() {
        ArrayList<String> proprieta = new ArrayList<String>();
        String str ="Separator file character: " +  System.getProperty("file.separator");
        proprieta.add(str);
        str ="Java classhpath: " + System.getProperty("java.class.path");
        proprieta.add(str);
        str ="Installation directory for JRE: " + System.getProperty("java.home");
        proprieta.add(str);
        str ="JRE vendor name: " + System.getProperty("java.vendor");
        proprieta.add(str);
        str ="JRE vender URL: " + System.getProperty("java.vendor.url");
        proprieta.add(str);
        str ="JRE version number: " + System.getProperty("java.version");
        proprieta.add(str);
        str ="Line separator: " + System.getProperty("line.separator");
        proprieta.add(str);
        str ="Operating system architecture: " + System.getProperty("os.arch");
        proprieta.add(str);
        str ="Operating system name: " + System.getProperty("os.name");
        proprieta.add(str);
        str ="Operating system version: " + System.getProperty("os.version");
        proprieta.add(str);
        str ="Path separator character: " + System.getProperty("path.separator");
        proprieta.add(str);
        str ="User working directory: " + System.getProperty("user.dir");
        proprieta.add(str);
        str ="User home directory: " + System.getProperty("user.home");
        proprieta.add(str);
        str ="User account name: " + System.getProperty("user.name");
        proprieta.add(str);
        return proprieta;
    }

}
