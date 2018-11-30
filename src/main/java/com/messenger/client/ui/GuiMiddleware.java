package com.messenger.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

class GuiMiddleware extends Frame {

    private static final String TITLE = "Chat Client";
    private static final int WIDTH = 450;
    private static final int HEIGHT = 600;

    private Consumer<String> onMessage = (String n) -> {
    };
    private Consumer<String> onNick = (String n) -> {
    };
    private Runnable onQuit = () -> {
    };
    private Runnable onInputEnter = () -> {
    };

    private Label header;
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

        setButtonsHandlers();
        setInputHandlers();
        setWindowHandlers();

        setTitle(TITLE);
        setVisible(true);
        requestFocus();

        input.requestFocus();
    }

    void printLine(String text) {
        display.append(text + "\n");
    }

    void close() {
        dispose();
    }

    void onMessageEnter(Consumer<String> fn) {
        onMessage = fn;
    }

    private String flushInput() {
        String text = input.getText();
        input.setText(null);
        input.requestFocus();

        return text;
    }

    private void buildLayout() {
        header = new Label(null, Label.CENTER);
        header.setFont(new Font("Helvetica", Font.PLAIN, 14));

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
        add("North", header);
        add("Center", display);
        add("South", actionPanel);

        quit.setEnabled(false);
        connect.setEnabled(true);
        send.setEnabled(false);
        onInputEnter = () -> handleConnect(null);
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

    private void setButtonsHandlers() {
        quit.addActionListener(this::handleQuit);
        connect.addActionListener(this::handleConnect);
        send.addActionListener(this::handleSend);
    }

    private void handleQuit(ActionEvent e) {
        onInputEnter = () -> handleConnect(null);

        header.setText(null);
        flushInput();
        display.setText(null);

        quit.setEnabled(false);
        connect.setEnabled(true);
        send.setEnabled(false);
    }

    private void handleConnect(ActionEvent e) {
        String name = flushInput();
        if (name == null || name.length() == 0) {
            return;
        }

        header.setText("Nick: " + name);

        onInputEnter = () -> handleSend(null);

        quit.setEnabled(true);
        connect.setEnabled(false);
        send.setEnabled(true);
    }

    private void handleSend(ActionEvent e) {
        String msg = flushInput();

        if (msg != null && msg.length() > 0) {
            onMessage.accept(msg);
        }
    }

    private void setInputHandlers() {
        input.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onInputEnter.run();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });
    }

    private void setWindowHandlers() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                close();
            }
        });
    }

}
