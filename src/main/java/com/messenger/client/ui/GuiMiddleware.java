package com.messenger.client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.function.Consumer;

class GuiMiddleware extends Frame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private Consumer<String> onMessage;

    private TextArea display;
    private TextField input;
    private Button send, connect, quit;

    GuiMiddleware() {
        display = new TextArea();
        input = new TextField();

        send = new Button("Send");
        connect = new Button("Connect");
        quit = new Button("Bye");
    }

    void init() {
        placeWindow();
        buildLayout();
        setHandlers();

        setVisible(true);
        requestFocus();

        input.requestFocus();
    }

    void close() {
        dispose();
    }

    void onMessageEnter(Consumer<String> fn) {
        onMessage = fn;
    }

    void printLine(String text) throws IOException {
        display.append(text + "\n");
    }

    private String flushInput() {
        String text = input.getText();
        input.setText(null);
        input.requestFocus();

        return text;
    }

    private void buildLayout() {
        Panel keys = new Panel();
        keys.setLayout(new GridLayout(1, 2));
        keys.add(quit);
        keys.add(connect);

        Panel actionPanel = new Panel();
        actionPanel.setLayout(new BorderLayout());
        actionPanel.add("West", keys);
        actionPanel.add("Center", input);
        actionPanel.add("East", send);

        setLayout(new BorderLayout());
        add("Center", display);
        add("South", actionPanel);

        quit.setEnabled(false);
        send.setEnabled(false);
    }

    private void placeWindow() {
        setLayout(null);
        setSize(WIDTH, HEIGHT);

        // Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window in the center of the screen
        int x = (screenSize.width - WIDTH) / 2;
        int y = (screenSize.height - HEIGHT) / 2;

        setLocation(x, y);
    }

    private void setHandlers() {
        quit.addActionListener((ActionEvent e) -> {
            input.setText(null);

            quit.setEnabled(false);
            send.setEnabled(false);
            connect.setEnabled(true);
        });
        connect.addActionListener((ActionEvent e) -> {
            String name = flushInput();

            quit.setEnabled(true);
            send.setEnabled(true);
            connect.setEnabled(false);
        });

        send.addActionListener((ActionEvent e) -> {
            String msg = flushInput();

            onMessage.accept(msg);
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                close();
            }
        });
    }

}
