/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.berna.client;


import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 *
 * @author Berna
 */
public class MyDateField extends DateField {

    public MyDateField(){
    super();
            if (Config.language == Consts.ITALIANO) {
            this.getDatePicker().getMessages().setTodayText("Oggi");
            this.getDatePicker().getMessages().setCancelText("Annulla");
            this.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/y"));
        } else {
            this.getPropertyEditor().setFormat(DateTimeFormat.getFormat("MM/dd/y"));
        }
    }



}
