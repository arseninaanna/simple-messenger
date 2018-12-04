package com.messenger.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

class GuiMiddleware extends Frame {

    private static final String EOL = "\r\n";

    private static final String TITLE = "Chat Client";
    private static final int WIDTH = 450;
    private static final int HEIGHT = 600;

    private Consumer<String> onMessage = (String n) -> {
    };
    private Consumer<String> onNick = (String n) -> {
    };
    private Runnable onQuit = () -> {
    };
    private Runnable onClose = () -> {
    };

    private Runnable onInputEnter = () -> {
    };

    private Label chatHeader;
    private TextArea chat;
    private TextField input;
    private Button send, connect, quit;

    GuiMiddleware() {
        chat = new TextArea();
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

    void printMessage(String text) {
        chat.append(text + EOL);
    }

    void printLog(String text) {
    }

    void showModal(String title, String text) {
        Dialog modal = new Dialog(this);
        modal.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        modal.setLayout(new BorderLayout());
        modal.setTitle(title);

        Label message = new Label(text, Label.CENTER);
        modal.add(message);

        Rectangle tbounds = this.getBounds();
        Rectangle bounds = new Rectangle();
        bounds.setSize(300, 100);
        bounds.x = (int) (tbounds.x + 0.5 * tbounds.width - 0.5 * bounds.width);
        bounds.y = (int) (tbounds.y + 0.5 * tbounds.height - 0.5 * bounds.height);
        modal.setBounds(bounds);

        modal.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                modal.dispose();
            }
        });

        modal.setResizable(false);
        modal.setVisible(true);
        modal.requestFocusInWindow();
    }

    void connected(String nick) {
        chatHeader.setText("Nick: " + nick);

        onInputEnter = () -> handleSend(null);
        input.setEnabled(true);
        input.requestFocus();

        quit.setEnabled(true);
        send.setEnabled(true);
    }

    void quieted(boolean clear) {
        if(clear) {
            chatHeader.setText(null);
            chat.setText(null);
        }

        quit.setEnabled(false);
        send.setEnabled(false);

        onInputEnter = () -> handleConnect(null);
        input.setEnabled(true);
        input.requestFocus();

        connect.setEnabled(true);
    }

    void block() {
        connect.setEnabled(false);
        quit.setEnabled(false);
        input.setEnabled(false);
        send.setEnabled(false);
    }

    void stop() {
        dispose();
    }

    void onMessageEnter(Consumer<String> fn) {
        onMessage = fn;
    }

    void onNickEnter(Consumer<String> fn) {
        onNick = fn;
    }

    void onQuit(Runnable fn) {
        onQuit = fn;
    }

    void onClose(Runnable fn) {
        onClose = fn;
    }

    private String flushInput() {
        String text = input.getText();
        input.setText(null);
        input.requestFocus();

        return text;
    }

    private void buildLayout() {
        chatHeader = new Label(null, Label.CENTER);
        chatHeader.setFont(new Font("Helvetica", Font.PLAIN, 14));

        Panel keyControls = new Panel();
        keyControls.setLayout(new GridLayout(1, 2));
        keyControls.add(quit);
        keyControls.add(connect);

        Panel actionPanel = new Panel();
        actionPanel.setLayout(new BorderLayout());
        actionPanel.add("West", keyControls);
        actionPanel.add("Center", input);
        actionPanel.add("East", send);

        Panel chatPanel = new Panel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.add("North", chatHeader);
        chatPanel.add("Center", chat);
        chatPanel.add("South", actionPanel);

        setLayout(new BorderLayout());
        add("Center", chatPanel);

        chat.setEditable(false);
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

        quit.setEnabled(false);
        send.setEnabled(false);

        input.setEnabled(false);
        flushInput();

        onQuit.run();
    }

    private void handleConnect(ActionEvent e) {
        String name = flushInput();
        if (name == null || name.length() == 0) {
            return;
        }

        connect.setEnabled(false);
        input.setEnabled(false);

        onNick.accept(name);
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
                onClose.run();
            }
        });

        chat.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (input.isEnabled()) {
                    input.requestFocus();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            }

        });
    }

}
