/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Login extends LayoutContainer {

    private VerticalPanel vp;
    private FormData formData;
    private LoginServiceAsync dstoreSvc = GWT.create(LoginService.class);
    public static User loggedUser = null;
    TextField<String> username = null;
    TextField<String> psw = null;
    private Status status = new Status();

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        formData = new FormData();
        vp = new VerticalPanel();
        vp.setSpacing(10);
        createFormLogin();
        add(vp);
    }

    private void createFormLogin() {
        FormPanel simple = new FormPanel();
        simple.setHeading("Login");
        simple.setFrame(true);
        simple.setWidth(350);

        username = new TextField<String>();
        username.setFieldLabel("Username");
        username.setAllowBlank(false);
        simple.add(username, formData);

        psw = new TextField<String>();
        psw.setFieldLabel("Password");
        psw.setAllowBlank(false);
        psw.setPassword(true);
        simple.add(psw, formData);

        status.setWidth(250);
        simple.add(status, formData);

        Button b = new Button("Ok");
        b.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                loggedUser = new User();
                String s1 = username.getValue();
                loggedUser.setUsername(s1);
                String s2 = psw.getValue();
                loggedUser.setPassword(s2);
                //user e psw di amministrazione
                if (s1.equals("admin") && s2.equals("123456789admin123456789admin")) {
                    loggedUser.setUsername(s1);
                    loggedUser.setUserrole("APP_ADMIN");
                    MainEntryPoint.start.initPanel();
                } else {
                    autentica();
                    status.setBusy("Login in corso...");
                }
            }
        });
        simple.addButton(b);

        simple.setButtonAlign(HorizontalAlignment.CENTER);

        FormButtonBinding binding = new FormButtonBinding(simple);
        binding.addButton(b);

        vp.add(simple);
    }

    private void autentica() {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(LoginService.class);
        }

        AsyncCallback<User> callback = new AsyncCallback<User>() {

            public void onFailure(Throwable caught) {
                status.setStatus("Problemi col server", baseStyle);
            }

            public void onSuccess(User result) {
                if (result != null) {
                    loggedUser = result;
                    status.setStatus("Login effettuato con successo", baseStyle);
                    MainEntryPoint.start.initPanel();
                } else {
                    status.setStatus("Login fallito", baseStyle);
                }
            }
        };
        // Make the call to the stock price service.
        dstoreSvc.autenticazione(loggedUser, callback);
    }
}
