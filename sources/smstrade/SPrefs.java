/* JSMSTrade v0.2
 * Copyright (c) 2008, 2009 Dipl.-Inform. (FH) Paul C. Buetow
 * jsmstrade@dev.buetow.org - http://jsmstrade.buetow.org
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
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * The class SPrefs.
 *
 * @author Paul C. Buetow
 */
public class SPrefs extends SFrame {
    /** The serial version uid */
    private static final long serialVersionUID = 1L;

    /** The options map */
    private HashMap<String,String> options = null;

    /** The text area */
    private JTextArea textArea = new JTextArea();

    /** The button panel */
    private JPanel buttonPanel = new JPanel();

    /** The ok button */
    private JButton okButton = new JButton("OK");

    /** The save button */
    private JButton saveButton = new JButton("Save");

    /**
     * Instantiates a new SMain object.
     */
    public SPrefs(Component parent, HashMap<String,String> options) {
        super("Preferences", parent);
        this.options = options;

        disposeWithParent();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setResizable(false);

        fillContentPane();
        setVisible(true);
    }

    /**
     * Fills the content pane
     */
    private void fillContentPane() {
        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        if (options.containsKey("URL"))
            textArea.setText(options.get("URL"));
        else
            textArea.setText(SMain.DEFAULT_URL);

        textArea.setLineWrap(true);

        contentPanel.add(textArea);
        contentPanel.add(createButtonPanel());
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

                if (text.equals("OK")) {
                    save();
                    dispose();

                } else if (text.equals("Save")) {
                    save();
                }
            }

            private void save() {
                options.put("URL", textArea.getText());

                try {
                    FileOutputStream fos =
                        new FileOutputStream(SMain.SAVE_FILE);
                    ObjectOutputStream oos =
                        new ObjectOutputStream(fos);

                    oos.writeObject(options);

                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        };

        buttonPanel.add(okButton);
        buttonPanel.add(saveButton);

        okButton.addActionListener(listener);
        saveButton.addActionListener(listener);

        return buttonPanel;
    }
}
