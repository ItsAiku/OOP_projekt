package com.example.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Random;

public class MainGUI extends Application {

    // ── Game state ────────────────────────────────────────────────────────────
    private StartUp startup;
    private Tegevus tegevus;
    private KapitalCheck check;
    private Random rand;
    private boolean gameRunning;

    // ── UI components ─────────────────────────────────────────────────────────
    private Label kapitalLabel;
    private Label kliendidLabel;
    private Label töötajadCountLabel;
    private Label tuluLabel;
    private TextArea logArea;
    private VBox töötajadBox;
    private Button palkamineBtn;
    private Button turundusBtn;
    private Button rerollBtn;
    private Button perkidBtn;
    private Button skipBtn;
    private Button müüBtn;
    private Label statusBar;

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final String BG_DARK   = "#0d1117";
    private static final String BG_CARD   = "#161b22";
    private static final String BG_HOVER  = "#1f2937";
    private static final String ACCENT    = "#58a6ff";
    private static final String GREEN     = "#3fb950";
    private static final String YELLOW    = "#d29922";
    private static final String PURPLE    = "#bc8cff";
    private static final String RED       = "#f85149";
    private static final String TEAL      = "#39d353";
    private static final String TEXT_MAIN = "#e6edf3";
    private static final String TEXT_DIM  = "#8b949e";

    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void start(Stage stage) {
        initGame();
        redirectSystemOut();

        // ── Root ──────────────────────────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_DARK + ";");
        root.setPadding(new Insets(14));

        // ── Title bar ─────────────────────────────────────────────────────────
        root.setTop(buildTitleBar());

        // ── Content ───────────────────────────────────────────────────────────
        HBox content = new HBox(14);
        content.setFillHeight(true);
        content.setPadding(new Insets(10, 0, 10, 0));
        HBox.setHgrow(content, Priority.ALWAYS);

        VBox leftCol = buildLeftColumn();
        VBox rightCol = buildRightColumn();
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        content.getChildren().addAll(leftCol, rightCol);
        root.setCenter(content);

        // ── Status bar ────────────────────────────────────────────────────────
        statusBar = new Label("Klahvikäsud:  1-Palka  2-Turundus  3-Reroll  4-Perkid  5-Skip  6-Müü");
        statusBar.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 11px; -fx-font-family: monospace;");
        statusBar.setMaxWidth(Double.MAX_VALUE);
        statusBar.setPadding(new Insets(6, 4, 2, 4));
        root.setBottom(statusBar);

        // ── Keyboard shortcuts ────────────────────────────────────────────────
        root.setOnKeyPressed(e -> {
            if (!gameRunning) return;
            switch (e.getCode()) {
                case DIGIT1: case NUMPAD1: handlePalkamine(); break;
                case DIGIT2: case NUMPAD2: handleTurundus();  break;
                case DIGIT3: case NUMPAD3: handleReroll();    break;
                case DIGIT4: case NUMPAD4: handlePerkid();    break;
                case DIGIT5: case NUMPAD5: handleSkip();      break;
                case DIGIT6: case NUMPAD6: handleMüü();       break;
                default: break;
            }
        });

        // ── Scene ─────────────────────────────────────────────────────────────
        Scene scene = new Scene(root, 1000, 650);
        scene.setFill(Color.web(BG_DARK));
        stage.setScene(scene);
        stage.setTitle("Startup Simulaator");
        stage.setMinWidth(750);
        stage.setMinHeight(520);
        stage.show();
        root.requestFocus();

        // ── Start flow ────────────────────────────────────────────────────────
        promptFirstWorker();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI BUILDERS
    // ═════════════════════════════════════════════════════════════════════════

    private HBox buildTitleBar() {
        Label icon = new Label("🚀");
        icon.setStyle("-fx-font-size: 24px;");

        Label title = new Label("Startup Simulaator");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_MAIN + "; -fx-font-family: 'Segoe UI', sans-serif;");

        Label sub = new Label("— juhi oma idufirmat edu poole");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: " + TEXT_DIM + ";");
        sub.setPadding(new Insets(6, 0, 0, 8));

        HBox bar = new HBox(10, icon, title, sub);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 0, 8, 4));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #30363d;");

        VBox wrapper = new VBox(bar, sep);
        return bar;
    }

    /** Left column: stats card + workers list */
    private VBox buildLeftColumn() {
        VBox col = new VBox(12);
        col.setPrefWidth(260);
        col.setMinWidth(200);
        col.setMaxWidth(320);

        // Stats card
        VBox statsCard = card("📊  Ettevõtte Seis");
        statsCard.setStyle(statsCard.getStyle() + " -fx-border-color: " + ACCENT + "44;");

        kapitalLabel       = statLine("💰 Kapital",     "10 000 €", GREEN);
        kliendidLabel      = statLine("👥 Kliendid",    "0",        ACCENT);
        töötajadCountLabel = statLine("🧑‍💼 Töötajad",   "0",        YELLOW);
        tuluLabel          = statLine("📈 Tulu/klient", "20 €",     PURPLE);

        statsCard.getChildren().addAll(
                kapitalLabel, sep(), kliendidLabel, sep(),
                töötajadCountLabel, sep(), tuluLabel
        );

        // Workers card
        VBox workersCard = card("🧑‍💼  Töötajad");
        VBox.setVgrow(workersCard, Priority.ALWAYS);

        töötajadBox = new VBox(6);
        ScrollPane scroll = new ScrollPane(töötajadBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        scroll.setPrefHeight(200);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        workersCard.getChildren().add(scroll);
        VBox.setVgrow(workersCard, Priority.ALWAYS);

        col.getChildren().addAll(statsCard, workersCard);
        return col;
    }

    /** Right column: action buttons + log */
    private VBox buildRightColumn() {
        VBox col = new VBox(12);
        HBox.setHgrow(col, Priority.ALWAYS);

        // Actions area
        VBox actionsCard = card("⚡  Tegevused  (klahvid 1–6)");
        actionsCard.setMaxHeight(260);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        palkamineBtn = actionBtn("👤  Palka töötaja",      "Maksumus: uue töötaja palk/kuu",  GREEN,  "1");
        turundusBtn  = actionBtn("📣  Turunduskampaania",   "Maksumus: 2–10% kapitalist",      ACCENT, "2");
        rerollBtn    = actionBtn("🎲  Töökuse Reroll",      "Maksumus: 200 €",                 YELLOW, "3");
        perkidBtn    = actionBtn("✨  Perkid",              "Maksumus: 500 €  (üllatus!)",     PURPLE, "4");
        skipBtn      = actionBtn("⏭  Skip",                "Maksumus: 0 € — jäta kuu vahele", TEXT_DIM,"5");
        müüBtn       = actionBtn("💸  Müü firma",           "Lõpeta mäng praeguse kapitaliga", RED,    "6");

        grid.add(palkamineBtn, 0, 0);  grid.add(turundusBtn, 1, 0);
        grid.add(rerollBtn,    0, 1);  grid.add(perkidBtn,   1, 1);
        grid.add(skipBtn,      0, 2);  grid.add(müüBtn,      1, 2);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setFillWidth(true);
            cc.setPercentWidth(50);
            grid.getColumnConstraints().add(cc);
        }

        actionsCard.getChildren().add(grid);

        // Wire up mouse clicks
        palkamineBtn.setOnAction(e -> handlePalkamine());
        turundusBtn.setOnAction(e  -> handleTurundus());
        rerollBtn.setOnAction(e    -> handleReroll());
        perkidBtn.setOnAction(e    -> handlePerkid());
        skipBtn.setOnAction(e      -> handleSkip());
        müüBtn.setOnAction(e       -> handleMüü());

        // Log area
        VBox logCard = card("📋  Mängu Logi");
        VBox.setVgrow(logCard, Priority.ALWAYS);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle(
            "-fx-control-inner-background: #0d1117;" +
            "-fx-text-fill: " + TEXT_MAIN + ";" +
            "-fx-font-family: 'Consolas', monospace;" +
            "-fx-font-size: 12px;" +
            "-fx-border-color: transparent;"
        );
        VBox.setVgrow(logArea, Priority.ALWAYS);
        logCard.getChildren().add(logArea);
        VBox.setVgrow(logCard, Priority.ALWAYS);

        VBox.setVgrow(actionsCard, Priority.NEVER);
        col.getChildren().addAll(actionsCard, logCard);
        return col;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  GAME ACTION HANDLERS
    // ═════════════════════════════════════════════════════════════════════════

    private void handlePalkamine() {
        if (!gameRunning) return;
        check.kontrolliKapitali(startup);

        if (!check.isTöötajaPalkamine()) {
            showError("Pole piisavalt kapitali töötaja palkamiseks!");
            return;
        }

        Optional<String> result = inputDialog(
            "Töötaja Palkamine",
            "Uus töötaja lisatakse firmale. Palk arvestatakse iga kuu.",
            "Töötaja nimi:",
            ""
        );

        result.ifPresent(nimi -> {
            try {
                if (nimi.trim().isEmpty())
                    throw new IllegalArgumentException("Töötaja nimi ei tohi olla tühi!");

                payTurn();
                // Follows same pattern as Main.java: Tegevus.töötajaPalkamine + lisaTöötaja
                tegevus.töötajaPalkamine(startup, nimi.trim());
                Töötaja uus = new Töötaja(nimi.trim(), startup.getTöötajad().size());
                startup.lisaTöötaja(uus);

                log("✅ Palkasid töötaja " + uus.getNimi() + " | palk: " + uus.getPalk()
                    + " € | töökus: " + uus.getTöökus());
                endTurn();

            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });
    }

    private void handleTurundus() {
        if (!gameRunning) return;
        check.kontrolliKapitali(startup);

        if (!check.isTurundusKampaania()) {
            showError("Pole piisavalt kapitali turunduskampaania jaoks!");
            return;
        }

        int eelmine = startup.getKlientideArv();
        payTurn();
        tegevus.turundusKampaania(startup);
        int muutus = startup.getKlientideArv() - eelmine;
        log("📣 Turunduskampaania! " + (muutus >= 0 ? "+" : "") + muutus + " klienti.");
        endTurn();
    }

    private void handleReroll() {
        if (!gameRunning) return;
        check.kontrolliKapitali(startup);

        if (!check.isTöökuseReroll()) {
            showError("Pole piisavalt kapitali rerolliks! (vajad vähemalt 200 €)");
            return;
        }
        if (startup.getTöötajad().isEmpty()) {
            showError("Sul pole ühtegi töötajat, keda rerollida.");
            return;
        }

        ChoiceDialog<String> dlg = new ChoiceDialog<>();
        dlg.setTitle("Töökuse Reroll — 200 €");
        dlg.setHeaderText("Vali töötaja, kelle töökust soovid rerollida.");
        dlg.setContentText("Töötaja:");
        styleDialog(dlg.getDialogPane());

        for (Töötaja t : startup.getTöötajad()) {
            dlg.getItems().add(t.getNimi() + "  (töökus: " + t.getTöökus() + ")");
        }
        if (!dlg.getItems().isEmpty()) dlg.setSelectedItem(dlg.getItems().get(0));

        Optional<String> result = dlg.showAndWait();
        result.ifPresent(valitud -> {
            try {
                // Extract just the name (before the spacing)
                String nimi = valitud.split("  \\(")[0].trim();
                payTurn();
                tegevus.töökuseReroll(startup, nimi);

                double newTöökus = startup.getTöötajad().stream()
                    .filter(t -> t.getNimi().equals(nimi))
                    .mapToDouble(Töötaja::getTöökus)
                    .findFirst().orElse(-1);

                log("🎲 Reroll: " + nimi + " uus töökus: " + newTöökus);
                endTurn();
            } catch (Exception ex) {
                showError("Reroll ebaõnnestus: " + ex.getMessage());
            }
        });
    }

    private void handlePerkid() {
        if (!gameRunning) return;
        check.kontrolliKapitali(startup);

        if (!check.isPerkid()) {
            showError("Pole piisavalt kapitali perkide jaoks! (vajad vähemalt 500 €)");
            return;
        }

        payTurn();
        int number = rand.nextInt(100) + 1;
        log("✨ Perkid aktiveeritud…");
        // tegevus.perkid prints to System.out; redirected stream catches it in logArea
        tegevus.perkid(number, startup);
        endTurn();
    }

    private void handleSkip() {
        if (!gameRunning) return;
        payTurn();
        log("⏭  Skip — jätsid kuu vahele.");
        endTurn();
    }

    private void handleMüü() {
        if (!gameRunning) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Müü Firma");
        confirm.setHeaderText("Kas oled kindel, et soovid firmast loobuda?");
        confirm.setContentText("Müüd firma " + startup.getKapital() + " € eest. Mäng lõpeb.");
        styleDialog(confirm.getDialogPane());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            log("💸 Müüsid firma " + startup.getKapital() + " € eest! Mäng on läbi.");
            endGame("Palju õnne! Müüsid firma\n" + startup.getKapital() + " € eest.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  TURN LOGIC
    // ═════════════════════════════════════════════════════════════════════════

    /** Deduct salaries — called at the start of every turn, matching Main.java logic */
    private void payTurn() {
        int totalPalk = 0;
        for (Töötaja t : startup.getTöötajad()) {
            totalPalk += t.getPalk();
            startup.setKapital(startup.getKapital() - t.getPalk());
        }
        if (totalPalk > 0) log("💼 Palgad makstud: −" + totalPalk + " €");
    }

    /** Recalculate stats, earn revenue, handle churn — runs after every action */
    private void endTurn() {
        // Recalculate average töökus and resulting revenue per client
        if (!startup.getTöötajad().isEmpty()) {
            double sum = 0;
            for (Töötaja t : startup.getTöötajad()) sum += t.getTöökus();
            double avg = sum / startup.getTöötajad().size();
            startup.setTuluKliendiKohta((int)(startup.getBaseTuluKliendiKohta() * avg));
        }

        // 5 % client churn per month
        startup.setKliendid((int)(startup.getKlientideArv() * 0.95));

        // Earn revenue from clients
        int tulu = startup.getKlientideArv() * startup.getTuluKliendiKohta();
        startup.suurendaKapital(tulu);
        log("💵 Teenitud tulu: +" + tulu + " €\n──────────────────────────");

        updateUI();

        if (startup.getKapital() <= 0) {
            log("❌ Kapital sai otsa! Mäng läbi.");
            endGame("Kapital sai otsa!\nMäng on läbi.");
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  UI HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private void updateUI() {
        kapitalLabel.setText("💰  " + startup.getKapital() + " €");
        kliendidLabel.setText("👥  " + startup.getKlientideArv());
        töötajadCountLabel.setText("🧑‍💼  " + startup.getTöötajad().size());
        tuluLabel.setText("📈  " + startup.getTuluKliendiKohta() + " €/klient");

        töötajadBox.getChildren().clear();
        for (Töötaja t : startup.getTöötajad()) {
            HBox row = new HBox(8);
            row.setPadding(new Insets(5, 8, 5, 8));
            row.setStyle("-fx-background-color: " + BG_HOVER + "; -fx-background-radius: 6;");

            Label name = new Label(t.getNimi());
            name.setStyle("-fx-text-fill: " + TEXT_MAIN + "; -fx-font-weight: bold; -fx-font-size: 11px;");
            HBox.setHgrow(name, Priority.ALWAYS);

            Label details = new Label(t.getPalk() + " €  |  töök: " + t.getTöökus());
            details.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 11px; -fx-font-family: monospace;");

            row.getChildren().addAll(name, details);
            töötajadBox.getChildren().add(row);

            // Hover effect (mouse event)
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #2d333b; -fx-background-radius: 6;"));
            row.setOnMouseExited(e  -> row.setStyle("-fx-background-color: " + BG_HOVER + "; -fx-background-radius: 6;"));
        }
    }

    /** Ask for the very first worker's name before the game begins */
    private void promptFirstWorker() {
        log("Tere tulemast Startup Simulaatorisse!");
        log("Algkapital: 10 000 €");
        log("──────────────────────────");

        String nimi = "Töötaja";
        while (true) {
            Optional<String> result = inputDialog(
                "Alustame!",
                "Tere tulemast Startup Simulaatorisse!\n\nSisesta oma esimese töötaja nimi, et mäng käivitada.",
                "Töötaja nimi:",
                ""
            );

            if (result.isEmpty()) {
                // User closed dialog — use default to avoid stuck state
                nimi = "Töötaja 1";
                break;
            }
            if (result.get().trim().isEmpty()) {
                showError("Nimi ei tohi olla tühi. Proovi uuesti.");
            } else {
                nimi = result.get().trim();
                break;
            }
        }

        Töötaja esimene = new Töötaja(nimi, 1);
        startup.lisaTöötaja(esimene);
        log("👤 Esimene töötaja: " + nimi + " | palk: " + esimene.getPalk()
            + " € | töökus: " + esimene.getTöökus());
        log("──────────────────────────");
        updateUI();
    }

    private void endGame(String message) {
        gameRunning = false;
        disableActions();
        showInfo(message);
    }

    private void disableActions() {
        for (Button b : new Button[]{palkamineBtn, turundusBtn, rerollBtn, perkidBtn, skipBtn, müüBtn}) {
            b.setDisable(true);
            b.setOpacity(0.4);
        }
        statusBar.setText("Mäng on lõppenud.");
    }

    private void log(String msg) {
        Platform.runLater(() -> {
            logArea.appendText(msg + "\n");
            logArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Viga");
        alert.setHeaderText("Midagi läks valesti");
        alert.setContentText(msg);
        styleDialog(alert.getDialogPane());
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        styleDialog(alert.getDialogPane());
        alert.showAndWait();
    }

    /** Generic text-input dialog with consistent styling */
    private Optional<String> inputDialog(String title, String header, String prompt, String def) {
        TextInputDialog dlg = new TextInputDialog(def);
        dlg.setTitle(title);
        dlg.setHeaderText(header);
        dlg.setContentText(prompt);
        styleDialog(dlg.getDialogPane());

        // Allow submitting with Enter (keyboard event)
        dlg.getEditor().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                dlg.getEditor().getScene().getWindow().hide();
            }
        });

        return dlg.showAndWait();
    }

    /** Apply dark theme to any dialog pane */
    private void styleDialog(DialogPane pane) {
        pane.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-border-color: #30363d;" +
            "-fx-border-width: 1;"
        );
        //pane.lookup(".content.label") != null;
        pane.getStylesheets(); // no-op call to keep the chain
        // Style every label inside the dialog
        pane.lookupAll(".label").forEach(node -> {
            if (node instanceof Label l) {
                l.setStyle("-fx-text-fill: " + TEXT_MAIN + ";");
            }
        });
        pane.lookupAll(".text-field").forEach(node -> {
            if (node instanceof TextField tf) {
                tf.setStyle("-fx-control-inner-background: " + BG_DARK + "; -fx-text-fill: " + TEXT_MAIN + "; -fx-border-color: #30363d;");
            }
        });
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  WIDGET BUILDERS
    // ═════════════════════════════════════════════════════════════════════════

    /** Styled card container */
    private VBox card(String heading) {
        Label title = new Label(heading);
        title.setStyle(
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_DIM + ";" +
            "-fx-font-family: 'Segoe UI', sans-serif;"
        );
        title.setPadding(new Insets(0, 0, 8, 0));

        Separator s = new Separator();
        s.setStyle("-fx-background-color: #30363d;");
        s.setPadding(new Insets(0, 0, 10, 0));

        VBox card = new VBox(6, title, s);
        card.setPadding(new Insets(14));
        card.setStyle(
            "-fx-background-color: " + BG_CARD + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: #21262d;" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;"
        );
        DropShadow ds = new DropShadow(8, Color.web("#00000066"));
        card.setEffect(ds);
        return card;
    }

    /** Single coloured stat label */
    private Label statLine(String label, String value, String color) {
        Label l = new Label(value);
        l.setStyle(
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Consolas', monospace;"
        );
        l.setUserData(label); // store label name for future reference
        return l;
    }

    /** Action button with colour, badge key, and hover animation */
    private Button actionBtn(String text, String desc, String color, String key) {
        Label keyBadge = new Label("[" + key + "]");
        keyBadge.setStyle(
            "-fx-text-fill: " + TEXT_DIM + ";" +
            "-fx-font-size: 10px;" +
            "-fx-font-family: monospace;"
        );

        Label main = new Label(text);
        main.setStyle("-fx-text-fill: " + TEXT_MAIN + "; -fx-font-size: 13px; -fx-font-weight: bold;");
        main.setWrapText(true);

        Label sub = new Label(desc);
        sub.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 10px;");
        sub.setWrapText(true);

        VBox inner = new VBox(2, main, sub);

        HBox content = new HBox(8, keyBadge, inner);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMouseTransparent(true);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMaxHeight(Double.MAX_VALUE);
        btn.setPadding(new Insets(12, 14, 12, 14));
        GridPane.setHgrow(btn, Priority.ALWAYS);
        GridPane.setFillWidth(btn, true);

        String base = "-fx-background-color: " + BG_HOVER + ";" +
                      "-fx-border-color: " + color + "55;" +
                      "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + color + "22;" +
                       "-fx-border-color: " + color + ";" +
                       "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";

        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));   // mouse event
        btn.setOnMouseExited(e  -> btn.setStyle(base));    // mouse event

        return btn;
    }

    private Separator sep() {
        Separator s = new Separator();
        s.setStyle("-fx-background-color: #21262d;");
        return s;
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  INITIALISATION HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    private void initGame() {
        startup     = new StartUp(10000);
        tegevus     = new Tegevus();
        check       = new KapitalCheck();
        rand        = new Random();
        gameRunning = true;
    }

    /**
     * Redirect System.out so that Tegevus.perkid() println calls are
     * captured and displayed in the log TextArea instead of the console.
     */
    private void redirectSystemOut() {
        PrintStream ps = new PrintStream(new OutputStream() {
            private final StringBuilder buf = new StringBuilder();
            @Override
            public void write(int b) {
                char c = (char) b;
                if (c == '\n') {
                    String line = buf.toString();
                    buf.setLength(0);
                    if (!line.isBlank()) logArea.appendText("   " + line + "\n"); // indent perks output
                } else {
                    buf.append(c);
                }
            }
            @Override
            public void write(byte[] b, int off, int len) {
                String text = new String(b, off, len).trim();
                if (!text.isBlank()) logArea.appendText("   " + text + "\n");
            }
        });
        System.setOut(ps);
    }
}
