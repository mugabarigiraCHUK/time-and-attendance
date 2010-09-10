
package org.berna.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Berna
 */
public class Utils {

    static ArrayList listToArray(List l) {
        ArrayList array = null;
        if(l!=null) {
            array = new ArrayList();
            Iterator it = l.iterator();
            while (it.hasNext()) {
                Object object = it.next();
                array.add(object);
            }
        }
        return array;
    }

}