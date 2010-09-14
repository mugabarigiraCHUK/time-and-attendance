package org.berna.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AnagraficaPersonaFisica extends LayoutContainer {

    private PersonaFisicaServiceAsync dstoreSvc = GWT.create(PersonaFisicaService.class);
    ListStore<BeanModel> store = new ListStore<BeanModel>();
    ArrayList<PersonaFisica> personeFisiche = new ArrayList<PersonaFisica>();
    CheckBoxSelectionModel<BeanModel> sm = new CheckBoxSelectionModel<BeanModel>();
    private Status status = new Status();

    @Override
    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setLayout(new CenterLayout());

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

        configs.add(sm.getColumn());

        ColumnConfig column = new ColumnConfig();
        column.setId("nome");
        column.setHeader("Nome");
        column.setWidth(220);

        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cognome");
        column.setHeader("Cognome");
        column.setWidth(220);

        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        column = new ColumnConfig();
        column.setId("cf");
        column.setHeader("Codice Fiscale");
        column.setWidth(220);

        text = new TextField<String>();
        text.setAllowBlank(false);
        column.setEditor(new CellEditor(text));
        configs.add(column);

        DateField dateField = new DateField();
        dateField.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/y"));
        column = new ColumnConfig();
        column.setId("dataNascita");
        column.setHeader("Data nascita");
        column.setWidth(120);
        column.setEditor(new CellEditor(dateField));
        column.setDateTimeFormat(DateTimeFormat.getMediumDateFormat());
        configs.add(column);

        if (Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            caricaDati();
        } else {
            caricaDatiConProprietario();
        }
        status.setBusy("Caricamento dati in corso...");

        ColumnModel cm = new ColumnModel(configs);

        ContentPanel cp = new ContentPanel();
        cp.setHeading("Anagrafica persone fisiche");
        cp.setFrame(true);
        //cp.setIcon(Resources.ICONS.table());
        cp.setSize(Consts.LarghezzaFinestra, Consts.AltezzaFinestra);
        cp.setLayout(new FitLayout());

        final EditorGrid<BeanModel> grid = new EditorGrid<BeanModel>(store, cm);
        grid.setSelectionModel(sm);
        grid.setAutoExpandColumn("nome");
        grid.setBorders(true);
        grid.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
        cp.add(grid);

        ToolBar toolBar = new ToolBar();
        Button addButton = new Button("Aggiungi persona");
        addButton.setIcon(IconHelper.create("/resources/grafica/x16/add2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                BeanModelLookup beanmodel = BeanModelLookup.get();
                BeanModelFactory factory = beanmodel.getFactory(PersonaFisica.class);
                Date data = new DateWrapper().clearTime().asDate();
                PersonaFisica persona = new PersonaFisica("NuovoNome", "NuovoCognome", "NuovoCF", data, Login.loggedUser.getId());
                BeanModel model = factory.createModel(persona);
                grid.stopEditing();
                personeFisiche.add(persona);
                store.insert(model, 0);
                grid.startEditing(store.indexOf(model), 0);

            }
        });
        toolBar.add(addButton);
        toolBar.add(new SeparatorToolItem());

        Button remove = new Button("Rimuovi selezionati");
        remove.setIcon(IconHelper.create("/resources/grafica/x16/delete2.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        remove.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!(sm.getSelectedItems()).isEmpty()) {
                    MessageBox.confirm("Confirm", "Sei sicuro di voler eliminare gli elemtni selezionati?", cancellazione);
                } else {
                    MessageBox.alert("Errore", "Selezionare uno o più elementi", cancellazione);
                }
            }
            //conferma per l'eliminazione
            final Listener<MessageBoxEvent> cancellazione = new Listener<MessageBoxEvent>() {

                @Override
                public void handleEvent(MessageBoxEvent ce) {
                    Button btn = ce.getButtonClicked();
                    if (btn.getText().equals("Yes")) {
                        List<BeanModel> modelsDaRimuovere = sm.getSelectedItems();
                        ArrayList<PersonaFisica> personeDaRimuovere = new ArrayList();
                        if (modelsDaRimuovere != null) {
                            Iterator it = modelsDaRimuovere.iterator();
                            while (it.hasNext()) {
                                Object model = it.next();
                                store.remove((BeanModel) model);
                                PersonaFisica persona = ((BeanModel) model).getBean();
                                personeDaRimuovere.add(persona);
                                personeFisiche.remove(persona);
                            }
                            cancellaDati(personeDaRimuovere);
                        }
                    }
                }
            };
        });
        toolBar.add(remove);
        cp.setTopComponent(toolBar);

        ToolBar statusBar = new ToolBar();
        status.setWidth(350);
        statusBar.add(status);
        cp.setBottomComponent(statusBar);

        cp.setButtonAlign(HorizontalAlignment.CENTER);
        Button resetButton = new Button("Reset", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                store.rejectChanges();
            }
        });
        resetButton.setIcon(IconHelper.create("/resources/grafica/x16/undo.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(resetButton);

        Button saveButton = new Button("Save", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                store.commitChanges();
                salvaDati(personeFisiche);
            }
        });
        saveButton.setIcon(IconHelper.create("/resources/grafica/x16/save.png", Consts.ICON_WIDTH_SMALL, Consts.ICON_HEIGHT_SMALL));
        cp.addButton(saveButton);

        add(cp);
    }

    public void caricaDati() {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(PersonaFisicaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                personeFisiche = result;
                BeanModelFactory factory = BeanModelLookup.get().getFactory(PersonaFisica.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object personaFisica = it.next();
                        BeanModel personaFisicaModel = factory.createModel(personaFisica);
                        store.add(personaFisicaModel);
                    }
                }
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        personeFisiche.clear();
        store.removeAll();
        dstoreSvc.carica(callback);
    }

    public void caricaDatiConProprietario() {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(PersonaFisicaService.class);
        }

        AsyncCallback<ArrayList> callback = new AsyncCallback<ArrayList>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(ArrayList result) {
                personeFisiche = result;
                BeanModelFactory factory = BeanModelLookup.get().getFactory(PersonaFisica.class);
                if (result != null) {
                    Iterator it = result.iterator();
                    while (it.hasNext()) {
                        Object personaFisica = it.next();
                        BeanModel personaFisicaModel = factory.createModel(personaFisica);
                        store.add(personaFisicaModel);
                    }
                }
                status.setStatus("Dati caricati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        personeFisiche.clear();
        store.removeAll();
        dstoreSvc.carica(Login.loggedUser.getId(), callback);
    }

    private void salvaDati(ArrayList<PersonaFisica> personeFisiche) {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(PersonaFisicaService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(Void result) {
                //I dati vengono ricaricati per ottenere gli ID assegnati dal DataStore alle entità appena aggiunte
                if (Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
                    caricaDati();
                } else {
                    caricaDatiConProprietario();
                }
            }
        };
        // Make the call to the stock price service.
        dstoreSvc.salva(personeFisiche, callback);
    }

    private void cancellaDati(ArrayList personeFisiche) {
        // Initialize the service proxy.
        if (dstoreSvc == null) {
            dstoreSvc = GWT.create(PersonaFisicaService.class);
        }
        AsyncCallback<Void> callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                status.setStatus("Problemi di comunicazione col server", baseStyle);
            }

            @Override
            public void onSuccess(Void result) {
                status.setStatus("Dati cancellati con successo", baseStyle);
            }
        };
        // Make the call to the stock price service.
        dstoreSvc.cancella(personeFisiche, callback);
    }
}
