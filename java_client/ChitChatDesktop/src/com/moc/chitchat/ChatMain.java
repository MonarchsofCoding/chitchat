package com.moc.chitchat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_PREF_SIZE;
import static javax.swing.ScrollPaneConstants.*;

/**
 * Created by spiros on 22/01/17.
 */
public class ChatMain extends JFrame {
    JMenuBar menuBar;
    JMenu menu,submenu;
    JMenuItem menuItem;
    JPanel panel1,panel2;
    JSplitPane jSplitPane;
    JList userlist;
    JTextField search,message_sender;

    public ChatMain() {

        JFrame chat = new JFrame("kwstas");
        chat.setBackground(Color.darkGray);
        chat.setPreferredSize(new Dimension(800,600));

        panel1 = new JPanel();
        panel2 = new JPanel();




        //panel1.setSize(200,200);
        GridBagLayout layout = new GridBagLayout();
        panel1.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx=0;
        gbc.gridy=0;
        TextArea message_display = new TextArea();
        message_display.setEditable(false);
        message_display.setSize(300,200);
        panel1.add(message_display,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        message_sender = new JTextField(40);
        message_sender.setPreferredSize(new Dimension(200,50));
        panel1.add(message_sender,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        JButton btn = new JButton();
        btn.setText("Send");
        gbc.anchor = GridBagConstraints.LINE_END;
        panel1.add(btn,gbc);


        ///////PANEL 2///////////

        //panel2.setPreferredSize(new Dimension(300,300));
        GridBagLayout layout2 = new GridBagLayout();
        panel2.setLayout(layout2);
        GridBagConstraints gbc2 = new GridBagConstraints();

        gbc2.fill = GridBagConstraints.VERTICAL;
        gbc2.gridx=0;
        gbc2.gridy=0;
        search = new JTextField(8);
        search.setMinimumSize(new Dimension(200,200));
        panel2.add(search,gbc2);

        gbc2.gridx=0;
        gbc2.gridy=1;
        JButton searchbtn = new JButton();
        searchbtn.setText("Search");
        panel2.add(searchbtn,gbc2);

       
        gbc2.gridx=0;
        gbc2.gridy=2;
        DefaultListModel model = new DefaultListModel();
        userlist = new JList(model);
        ScrollPane searchresults = new ScrollPane();
        searchresults.add(userlist);

        panel2.add(searchresults,gbc2);

        gbc2.gridx=0;
        gbc2.gridy=3;
        JButton startchatbtn = new JButton();
        startchatbtn.setText("Connect");
        gbc2.anchor = GridBagConstraints.LINE_END;
        panel2.add(startchatbtn,gbc2);

        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panel2,panel1);
        jSplitPane.setDividerLocation(200);
        chat.add(jSplitPane);

        jSplitPane.setVisible(true);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setLocationRelativeTo(null);
        chat.pack();
        chat.setVisible(true);

        final int[] l = {0};
        JList<Messages> mylist = new JList<Messages>();
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String k = message_sender.getText();
                Messages msg = new Messages();
                if(l[0]%2==0){
                    msg.setName("SPIROS-");
                    msg.setMessage(k);

                }
                else {
                    msg.setName("Sabrina-");
                    msg.setMessage(k);

                }
                l[0]++;
                mylist.setCellRenderer(new ListCellRenderer<Messages>() {
                    @Override
                    public Component getListCellRendererComponent(JList<? extends Messages> list, Messages value, int index, boolean isSelected, boolean cellHasFocus) {
                        return null;
                    }
                });

                message_display.append(msg.getName()+"--->  "+msg.getMessage()+"\n");
                message_sender.setText("");
            }
        });

        searchbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                model.addElement("Spiros ");


            }
        });

        menuBar = new JMenuBar();
        menu = new JMenu("Logout");
        menuBar.add(menu);
        menuItem = new JMenuItem("MENU ITEM", KeyEvent.VK_T);
        menu.add(menuItem);
        chat.setJMenuBar(menuBar);

    }

}