/*
 * Copyright (c) 2008 Paul C. Buetow, smstrade@dev.buetow.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * All icons of the icons/ folder are 	under a Creative Commons
 * Attribution-Noncommercial-Share Alike License a CC-by-nc-sa.
 */

package smstrade;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * The class SMain.
 *
 * @author Paul C. Buetow
 */
public class SMain extends SFrame {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The max length of a SMS message */
    private static final int MESSAGE_MAX_LENGTH = 160;

    /** The program version */
    private static final double VERSION = 0.1;

    /** The save file */
    public static final String SAVE_FILE = "jsmstrade.dat";

    /** The default URL */
    public static final String DEFAULT_URL =
        "https://gateway.smstrade.de?key=KEY&to=TO&route=basic&message=";

    /** The options map */
    private HashMap<String,String> options = null;

    /** The text area */
    private JTextArea textArea = new JTextArea();

    /** The send button */
    private JButton sendButton = new JButton("Senden");

    /** The clear button */
    private JButton clearButton = new JButton("Löschen");

    /** The counter label button */
    private JLabel counterLabel = new JLabel(" 160");

    /** The counter text label button */
    private JLabel counterTextLabel = new JLabel("Zeichen");

    /** The button panel */
    private JPanel buttonPanel = new JPanel();

    /** The menu bar */
    private JMenuBar menuBar = new JMenuBar();

    /** The file menu */
    private JMenu fileMenu = new JMenu("Datei");

    /**
     * Instantiates a new SMain object.
     */
    public SMain(HashMap<String,String> options) {
        super("JSMSTrade v" + VERSION);
        this.options = options;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setResizable(false);

        fillMenuBar();
        fillContentPane();
        setVisible(true);
    }

    /**
     * Fills the menu bar
     */
    private void fillMenuBar() {
        menuBar.add(fileMenu);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JMenuItem source = (JMenuItem) ae.getSource();
                String text = source.getText();

                if (text.equals("Einstellungen")) {
                    new SPrefs(SMain.this, options);

                } else if (text.equals("Beenden")) {
                    System.exit(0);
                }
            }
        };

        JMenuItem prefsItem = new JMenuItem("Einstellungen");
        prefsItem.addActionListener(listener);
        fileMenu.add(prefsItem);

        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(listener);
        fileMenu.add(exitItem);

        setJMenuBar(menuBar);
    }

    /**
     * Fills the content pane
     */
    private void fillContentPane() {
        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(textArea);
        contentPanel.add(createButtonPanel());

        textArea.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent ke) {
                updateCounter(ke);
            }

            public void keyReleased(KeyEvent ke) {
                updateCounter(ke);
            }

            public void keyPressed(KeyEvent ke) {
                updateCounter(ke);
            }

            private void updateCounter(KeyEvent ke) {
                int length = textArea.getText().length();

                if (length > MESSAGE_MAX_LENGTH) {
                    ke.consume();

                } else {
                    buttonPanel.remove(2);
                    counterLabel = new JLabel(" "+(160-length));
                    buttonPanel.add(counterLabel, 2);
                    buttonPanel.updateUI();
                    if (length == 0) {
                        clearButton.setEnabled(false);
                        sendButton.setEnabled(false);
                    } else {
                        clearButton.setEnabled(true);
                        sendButton.setEnabled(true);
                    }
                }
            }
        });

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    /**
     * Creates the button panel
     *
     * @return The button panel
     */
    private JPanel createButtonPanel() {
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JButton source = (JButton) ae.getSource();
                String text = source.getText();

                if (text.equals("Senden")) {
                    String message = textArea.getText();
                    textArea.setText("");
                    buttonPanel.remove(2);
                    counterLabel = new JLabel(" 160");
                    buttonPanel.add(counterLabel, 2);
                    buttonPanel.updateUI();
                    clearButton.setEnabled(false);
                    sendButton.setEnabled(false);

                    try {
                        String urlString = options.get("URL")
                                           + URLEncoder.encode(
                                               message, "ISO-8859-1");

                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream is = conn.getInputStream();

                        int ch = 0;
                        while ((ch = is.read()) != -1) {
                        }
                        is.close();

                    } catch (Exception e) {
                        System.err.println(e);
                    }

                } else if (text.equals("Löschen")) {
                    textArea.setText("");
                    buttonPanel.remove(2);
                    counterLabel = new JLabel(" 160");
                    buttonPanel.add(counterLabel, 2);
                    buttonPanel.updateUI();
                    clearButton.setEnabled(false);
                    sendButton.setEnabled(false);
                }
            }
        };

        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(counterLabel);
        buttonPanel.add(counterTextLabel);

        sendButton.addActionListener(listener);
        clearButton.addActionListener(listener);

        clearButton.setEnabled(false);
        sendButton.setEnabled(false);

        return buttonPanel;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        File file = new File(SMain.SAVE_FILE);
        HashMap<String,String> options = null;

        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                options = (HashMap<String,String>) ois.readObject();

            } catch (Exception e) {
                System.err.println(e);
            }

        } else {
            options = new HashMap<String, String>();
        }

        new SMain(options);
    }
}
