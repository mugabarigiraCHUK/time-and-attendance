/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 *
 * @author Berna
 */
public class NumberField extends TextField {

    public NumberField() {
        super();

        Validator _validator = new Validator() {

            public String validate(Field<?> field, String value) {
                boolean b = false;
                b = containsOnlyNumbers(value);
                if (!b) {
                    return "Errore: valor errato";
                }
                return null;
            }
        };
        setValidator(_validator);
    }

    public boolean containsOnlyNumbers(String str) {

        if (str == null || str.length() == 0) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {

            //If we find a non-digit character we return false.
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
} // end class

