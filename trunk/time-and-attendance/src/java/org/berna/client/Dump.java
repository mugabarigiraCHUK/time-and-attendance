/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import java.util.List;

/**
 *
 * @author Berna
 */
public class Dump extends LayoutContainer {

    private FormData formData;
    private ContentPanel panel = null;
    ComboBox<BeanModel> comboAziende = null;
    List<String> aziendeDiCompetenza = null;
    SimpleComboBox<String> comboMese = null;
    SimpleComboBox<String> comboAnno = null;
    ListStore<BeanModel> storeAziende = new ListStore<BeanModel>();

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new CenterLayout());

        panel = new ContentPanel();
        panel.setBodyStyle("padding: 6px");
        panel.setFrame(true);
        panel.setHeading("Esporta presenze");
        panel.setWidth(500);
        panel.setHeight(250);
        FormPanel form = createForm();
        panel.add(form);
        add(panel);
    }

    private FormPanel createForm() {
        final FormPanel panel = new FormPanel();
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        String url = "/DownloadPresenzeServlet";
        panel.setAction(url);
        panel.setEncoding(Encoding.MULTIPART);
        panel.setMethod(Method.POST);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setWidth(480);

        comboAziende = new ComboBox<BeanModel>();
        comboAziende.setEmptyText("Seleziona un'azienda...");
        comboAziende.setEditable(false);
        comboAziende.setDisplayField("denominazione");
        comboAziende.setFieldLabel("Azienda");
        comboAziende.setWidth(200);
        comboAziende.setStore(storeAziende);
        comboAziende.setTypeAhead(true);
        comboAziende.setTriggerAction(TriggerAction.ALL);
        panel.add(comboAziende, formData);

        comboMese = new SimpleComboBox<String>();
        comboMese.setFieldLabel("Mese");
        comboMese.setForceSelection(true);
        comboMese.setEditable(false);
        comboMese.setWidth(150);
        comboMese.setTriggerAction(TriggerAction.ALL);
        comboMese.setEmptyText("Seleziona un mese...");
        comboMese.add("01");
        comboMese.add("02");
        comboMese.add("03");
        comboMese.add("04");
        comboMese.add("05");
        comboMese.add("06");
        comboMese.add("07");
        comboMese.add("08");
        comboMese.add("09");
        comboMese.add("10");
        comboMese.add("11");
        comboMese.add("12");
        panel.add(comboMese, formData);

        comboAnno = new SimpleComboBox<String>();
        comboAnno.setFieldLabel("Anno");
        comboAnno.setForceSelection(true);
        comboAnno.setEditable(false);
        comboAnno.setWidth(150);
        comboAnno.setTriggerAction(TriggerAction.ALL);
        comboAnno.setEmptyText("Seleziona un anno...");
        comboAnno.add("2010");
        comboAnno.add("2011");
        comboAnno.add("2012");
        comboAnno.add("2013");
        comboAnno.add("2014");
        comboAnno.add("2015");
        comboAnno.add("2016");
        comboAnno.add("2017");
        panel.add(comboAnno, formData);

        Button reset = new Button("Reset");
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.reset();
            }
        });
        panel.addButton(reset);

        Button carica = new Button("Download");
        carica.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.submit();
            }
        });
        panel.addButton(carica);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(carica);

        return panel;
    }


    /*
    private FormPanel createForm2() {
        final FormPanel panel = new FormPanel();
        panel.setFrame(true);
        panel.setHeaderVisible(false);
        //String url = GWT.getModuleBaseURL() + "fileupload";
        String url = "/UploadServlet";
        panel.setAction(url);
        //panel.setAction(GWT.getModuleBaseURL() + "fileupload");
        panel.setEncoding(Encoding.MULTIPART);
        panel.setMethod(Method.POST);
        panel.setButtonAlign(HorizontalAlignment.CENTER);
        panel.setWidth(650);

        Radio radio = new Radio();
        radio.setBoxLabel("Esporta dati");
        radio.setValue(true);

        Radio radio2 = new Radio();
        radio2.setBoxLabel("Importa dati");

        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setFieldLabel("Seleziona operazione");
        radioGroup.add(radio);
        radioGroup.add(radio2);
        //radioGroup.addPlugin(plugin);
        panel.add(radioGroup, formData);

        FileUploadField file = new FileUploadField();
        file.setName("import-file");
        file.setAllowBlank(false);
        file.setFieldLabel("File");
        panel.add(file, formData);

        Button reset = new Button("Reset");
        reset.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                panel.reset();
            }
        });
        panel.addButton(reset);

        Button carica = new Button("Carica");
        carica.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //panel.submit();
                //if (!panel.isValid()) {
                //return;
                //}
                // normally would submit the form but for example no server set up to
                // handle the post
                // panel.submit();
                //MessageBox.info("Action", "Dati caricati", null);
            }
        });
        panel.addButton(carica);

        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(carica);

        return panel;
    }*/
    
} //end class
