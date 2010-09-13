/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.berna.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;

public class MainLayout extends LayoutContainer {

    Login login = new Login();
    AnagraficaPersonaFisica anP;
    AnagraficaUtente anU = null;
    AnagraficaAzienda anA = null;
    CreazioneLavoratore crLav = null;
    GestionePresenze gePres = new GestionePresenze();
    GestioneTimbrature geTimb = null;
    Info info = null;
    Dump dump = null;
    AnagraficaLavoratore anL = null;
    public BorderLayoutData centerDataMain = null;
    public CardLayout cardLayout = null;
    public ContentPanel center = null;
    public ContentPanel menu = null;

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        final BorderLayout layoutMain = new BorderLayout();
        setLayout(layoutMain);
        setStyleAttribute("padding", "10px");

        centerDataMain = new BorderLayoutData(LayoutRegion.CENTER);
        centerDataMain.setMargins(new Margins(0, 10, 5, 0));

        add(login, centerDataMain);
    }

    public void initPanel() {

        final BorderLayout borderLayout = new BorderLayout();
        ContentPanel mainPanel = new ContentPanel();
        mainPanel.setLayout(borderLayout);
        mainPanel.setHeading("Programma presenze");
        mainPanel.setSize(1240, 770);

        cardLayout = new CardLayout();
        center = new ContentPanel();
        center.setLayout(cardLayout);
        center.setHeaderVisible(disabled);
        center.setFrame(true);
        center.add(gePres);
        center.setSize(500, 500);

        RowLayout rowLayout = new RowLayout(Orientation.VERTICAL);
        menu = new ContentPanel();
        menu.setLayout(rowLayout);
        menu.setFrame(false);
        menu.setHeaderVisible(false);

        ContentPanel intestazione = new ContentPanel();
        final BorderLayout intestazioneLayout = new BorderLayout();
        intestazione.setLayout(intestazioneLayout);
        BorderLayoutData sinistra = new BorderLayoutData(LayoutRegion.WEST, 300);
        //BorderLayoutData centro = new BorderLayoutData(LayoutRegion.CENTER);
        BorderLayoutData destra = new BorderLayoutData(LayoutRegion.EAST, 200);
        intestazione.setHeaderVisible(false);
        Label label = new Label("Programma Gestione Presenze");
        intestazione.add(label, sinistra);
        Image image=new Image();
        image.setUrl("/resources/grafica/iubar.jpg");
        intestazione.add(image, destra);


        popolaToolbar();

        BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 60);
        northData.setMargins(new Margins(5, 5, 0, 5));

        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(5, 5, 5, 0));

        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 181);
        westData.setCollapsible(true);
        westData.setMargins(new Margins(5, 5, 5, 5));

        mainPanel.add(intestazione, northData);
        mainPanel.add(menu, westData);
        mainPanel.add(center, centerData);

        removeAll();
        add(mainPanel, centerDataMain);
        layout();
    }

    private void popolaToolbar() {
        //Per l'HRAdmin e l'AppAdmin 1
        if (Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button anPersonaFisica = new Button("Anagrafica persone fisiche");
            anPersonaFisica.setIcon(IconHelper.create("/resources/grafica/x24/users2.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anPersonaFisica.setHeight(Consts.AltezzaBottone);
            anPersonaFisica.setWidth(Consts.LarghezzaBottone);
            anPersonaFisica.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anP == null) {
                        anP = new AnagraficaPersonaFisica();
                        center.add(anP);
                        cardLayout.setActiveItem(anP);
                    } else {
                        cardLayout.setActiveItem(anP);
                        anP.caricaDati();
                    }
                }
            });
            menu.add(anPersonaFisica);

            Button anLavoratore = new Button("Anagrafica lavoratori");
            anLavoratore.setIcon(IconHelper.create("/resources/grafica/x24/businessmen.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anLavoratore.setHeight(Consts.AltezzaBottone);
            anLavoratore.setWidth(Consts.LarghezzaBottone);
            anLavoratore.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anL == null) {
                        anL = new AnagraficaLavoratore();
                        center.add(anL);
                        cardLayout.setActiveItem(anL);
                    } else {
                        cardLayout.setActiveItem(anL);
                        anL.caricaAziende();
                    }
                }
            });
            menu.add(anLavoratore);
        }
        //Solo per l'AppAdmin 2
        if (Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button anAzienda = new Button("Anagrafica aziende");
            anAzienda.setIcon(IconHelper.create("/resources/grafica/x24/factory.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anAzienda.setHeight(Consts.AltezzaBottone);
            anAzienda.setWidth(Consts.LarghezzaBottone);
            anAzienda.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anA == null) {
                        anA = new AnagraficaAzienda();
                        center.add(anA);
                        cardLayout.setActiveItem(anA);
                    } else {
                        cardLayout.setActiveItem(anA);
                        anA.caricaDati();
                    }
                }
            });
            menu.add(anAzienda);

            Button anUtente = new Button("Anagrafica utenti");
            anUtente.setIcon(IconHelper.create("/resources/grafica/x24/id_cards.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anUtente.setHeight(Consts.AltezzaBottone);
            anUtente.setWidth(Consts.LarghezzaBottone);
            anUtente.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anU == null) {
                        anU = new AnagraficaUtente();
                        center.add(anU);
                        cardLayout.setActiveItem(anU);
                    } else {
                        cardLayout.setActiveItem(anU);
                        anU.caricaAziende();
                    }
                }
            });
            menu.add(anUtente);
        }
        //Per l'HRAdmin e l'AppAdmin 3
        if (Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button crLavoratore = new Button("Creazione lavoratore");
            crLavoratore.setIcon(IconHelper.create("/resources/grafica/x24/businessman.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            crLavoratore.setHeight(Consts.AltezzaBottone);
            crLavoratore.setWidth(Consts.LarghezzaBottone);
            crLavoratore.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (crLav == null) {
                        crLav = new CreazioneLavoratore();
                        center.add(crLav);
                        cardLayout.setActiveItem(crLav);
                    } else {
                        cardLayout.setActiveItem(crLav);
                        crLav.caricaDatiPersoneFisiche();
                    }
                }
            });
            menu.add(crLavoratore);
        }
        //Per tutti i livelli 4
        if (Login.loggedUser.getUserrole().equals("LAVORATORE") || Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button gePresenze = new Button("Gestione presenze");
            gePresenze.setIcon(IconHelper.create("/resources/grafica/x24/calendar.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            gePresenze.setHeight(Consts.AltezzaBottone);
            gePresenze.setWidth(Consts.LarghezzaBottone);
            gePresenze.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (gePres == null) {
                        gePres = new GestionePresenze();
                        center.add(gePres);
                        cardLayout.setActiveItem(gePres);
                    } else {
                        cardLayout.setActiveItem(gePres);
                        gePres.caricaAziende();
                    }
                }
            });
            menu.add(gePresenze);
        }
        //Per l'HRAdmin e l'AppAdmin 5
        if (Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button timbrature = new Button("Timbrature");
            timbrature.setIcon(IconHelper.create("/resources/grafica/x24/clock.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            timbrature.setHeight(Consts.AltezzaBottone);
            timbrature.setWidth(Consts.LarghezzaBottone);
            timbrature.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (geTimb == null) {
                        geTimb = new GestioneTimbrature();
                        center.add(geTimb);
                        cardLayout.setActiveItem(geTimb);
                    } else {
                        cardLayout.setActiveItem(geTimb);
                        geTimb.caricaDati();
                    }
                }
            });
            menu.add(timbrature);

            Button bottonedump = new Button("Esporta");
            bottonedump.setIcon(IconHelper.create("/resources/grafica/x24/server_into.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            bottonedump.setHeight(Consts.AltezzaBottone);
            bottonedump.setWidth(Consts.LarghezzaBottone);
            bottonedump.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (dump == null) {
                        dump = new Dump();
                        center.add(dump);
                        cardLayout.setActiveItem(dump);
                    } else {
                        cardLayout.setActiveItem(dump);
                        dump.caricaDati();
                    }
                }
            });
            menu.add(bottonedump);

            Button help = new Button("Aiuto");
            help.setIcon(IconHelper.create("/resources/grafica/x24/help2.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            help.setHeight(Consts.AltezzaBottone);
            help.setWidth(Consts.LarghezzaBottone);
            help.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    String url = "http://www.iubar.it";
                    String name = "Help";
                    String features = null;
                    Window.open(url, name, features);
                }
            });
            menu.add(help);
        }
        //Per tutti i livelli 6
        if (Login.loggedUser.getUserrole().equals("LAVORATORE") || Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button bottoneInfo = new Button("Informazioni di sistema");
            bottoneInfo.setIcon(IconHelper.create("/resources/grafica/x24/information.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            bottoneInfo.setHeight(Consts.AltezzaBottone);
            bottoneInfo.setWidth(Consts.LarghezzaBottone);
            bottoneInfo.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (info == null) {
                        info = new Info();
                        center.add(info);
                        cardLayout.setActiveItem(info);
                    } else {
                        cardLayout.setActiveItem(info);
                    }
                }
            });
            menu.add(bottoneInfo);
        }


/*
        //Per tutti i livelli
        if (Login.loggedUser.getUserrole().equals("LAVORATORE") || Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {
            Button anPersonaFisica = new Button("Anagrafica persone fisiche");
            anPersonaFisica.setIcon(IconHelper.create("/resources/grafica/x24/users2.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anPersonaFisica.setHeight(Consts.AltezzaBottone);
            anPersonaFisica.setWidth(Consts.LarghezzaBottone);
            anPersonaFisica.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anP == null) {
                        anP = new AnagraficaPersonaFisica();
                        center.add(anP);
                        cardLayout.setActiveItem(anP);
                    } else {
                        cardLayout.setActiveItem(anP);
                        anP.caricaDati();
                    }
                }
            });
            menu.add(anPersonaFisica);

            Button bottoneInfo = new Button("Informazioni di sistema");
            bottoneInfo.setIcon(IconHelper.create("/resources/grafica/x24/information.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            bottoneInfo.setHeight(Consts.AltezzaBottone);
            bottoneInfo.setWidth(Consts.LarghezzaBottone);
            bottoneInfo.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (info == null) {
                        info = new Info();
                        center.add(info);
                        cardLayout.setActiveItem(info);
                    } else {
                        cardLayout.setActiveItem(info);
                    }
                }
            });
            menu.add(bottoneInfo);

            Button help = new Button("Aiuto");
            help.setIcon(IconHelper.create("/resources/grafica/x24/help2.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            help.setHeight(Consts.AltezzaBottone);
            help.setWidth(Consts.LarghezzaBottone);
            help.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    String url = "http://www.iubar.it";
                    String name = "Help";
                    String features = null;
                    Window.open(url, name, features);
                }
            });
            menu.add(help);

        }

        //Per l'HRAdmin e l'AppAdmin
        if (Login.loggedUser.getUserrole().equals("HR_ADMIN") || Login.loggedUser.getUserrole().equals("APP_ADMIN")) {

            Button crLavoratore = new Button("Creazione lavoratore");
            crLavoratore.setIcon(IconHelper.create("/resources/grafica/x24/businessman.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            crLavoratore.setHeight(Consts.AltezzaBottone);
            crLavoratore.setWidth(Consts.LarghezzaBottone);
            crLavoratore.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (crLav == null) {
                        crLav = new CreazioneLavoratore();
                        center.add(crLav);
                        cardLayout.setActiveItem(crLav);
                    } else {
                        cardLayout.setActiveItem(crLav);
                        crLav.caricaDatiPersoneFisiche();
                    }
                }
            });
            menu.add(crLavoratore);

            Button bottonedump = new Button("DUMP");
            bottonedump.setIcon(IconHelper.create("/resources/grafica/x24/server_into.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            bottonedump.setHeight(Consts.AltezzaBottone);
            bottonedump.setWidth(Consts.LarghezzaBottone);
            bottonedump.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (dump == null) {
                        dump = new Dump();
                        center.add(dump);
                        cardLayout.setActiveItem(dump);
                    } else {
                        cardLayout.setActiveItem(dump);
                        //dump.caricaDatiPersoneFisiche();
                    }
                }
            });
            menu.add(bottonedump);

            Button anLavoratore = new Button("Anagrafica lavoratori");
            anLavoratore.setIcon(IconHelper.create("/resources/grafica/x24/businessmen.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anLavoratore.setHeight(Consts.AltezzaBottone);
            anLavoratore.setWidth(Consts.LarghezzaBottone);
            anLavoratore.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anL == null) {
                        anL = new AnagraficaLavoratore();
                        center.add(anL);
                        cardLayout.setActiveItem(anL);
                    } else {
                        cardLayout.setActiveItem(anL);
                        anL.caricaAziende();
                    }
                }
            });
            menu.add(anLavoratore);

            Button gePresenze = new Button("Gestione presenze");
            gePresenze.setIcon(IconHelper.create("/resources/grafica/x24/calendar.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            gePresenze.setHeight(Consts.AltezzaBottone);
            gePresenze.setWidth(Consts.LarghezzaBottone);
            gePresenze.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (gePres == null) {
                        gePres = new GestionePresenze();
                        center.add(gePres);
                        cardLayout.setActiveItem(gePres);
                    } else {
                        cardLayout.setActiveItem(gePres);
                        gePres.caricaAziende();
                    }
                }
            });
            menu.add(gePresenze);
        }

        //Solo per l'AppAdmin
        if (Login.loggedUser.getUserrole().equals("APP_ADMIN")) {

            Button anUtente = new Button("Anagrafica utenti");
            anUtente.setIcon(IconHelper.create("/resources/grafica/x24/id_cards.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anUtente.setHeight(Consts.AltezzaBottone);
            anUtente.setWidth(Consts.LarghezzaBottone);
            anUtente.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anU == null) {
                        anU = new AnagraficaUtente();
                        center.add(anU);
                        cardLayout.setActiveItem(anU);
                    } else {
                        cardLayout.setActiveItem(anU);
                        anU.caricaAziende();
                    }
                }
            });
            menu.add(anUtente);

            Button anAzienda = new Button("Anagrafica aziende");
            anAzienda.setIcon(IconHelper.create("/resources/grafica/x24/factory.png", Consts.ICON_WIDTH, Consts.ICON_HEIGHT));
            anAzienda.setHeight(Consts.AltezzaBottone);
            anAzienda.setWidth(Consts.LarghezzaBottone);
            anAzienda.addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    if (anA == null) {
                        anA = new AnagraficaAzienda();
                        center.add(anA);
                        cardLayout.setActiveItem(anA);
                    } else {
                        cardLayout.setActiveItem(anA);
                        anA.caricaDati();
                    }
                }
            });
            menu.add(anAzienda);
        }*/
    }
}
