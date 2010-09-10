/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;

/**
 *
 * @author Berna
 */
public class Dump extends LayoutContainer {
    private FormData formData;

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setStyleAttribute("margin", "10px");

        ContentPanel cp = new ContentPanel();
        cp.setHeading("Importa/esporta dati");
        cp.setFrame(true);
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new RowLayout(Orientation.VERTICAL));

        Button save = new Button("Salva");
        save.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                //fa qualcosa
            }
        });
        cp.add(save);

        //cp.add(grid, new RowData(.6, 1));
        FormPanel panel = createForm();
        cp.add(panel, new RowData());
        add(cp);
    }

    private FormPanel createForm() {
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
                panel.submit();
                /*if (!panel.isValid()) {
                return;
                }
                // normally would submit the form but for example no server set up to
                // handle the post
                // panel.submit();
                MessageBox.info("Action", "Dati caricati", null);*/
            }
        });
        panel.addButton(carica);

        panel.setButtonAlign(HorizontalAlignment.CENTER);
        FormButtonBinding binding = new FormButtonBinding(panel);
        binding.addButton(carica);
       
        return panel;
    }
}
