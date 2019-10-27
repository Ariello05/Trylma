package Other;

import GUI.Model;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ServerCreator {
    private JButton runButton;
    private JButton exitButton;
    private JTextField portTextField;
    private JButton fetchLocalHostIpButton;
    private JLabel Status;
    private JTextField statusTextField;
    private JTextArea logTextArea;
    private JTextField connectedTextField;
    private JComboBox socketsComboBox;
    private JTextField ipTextField;
    private JPanel serverconfig;
    private Server cur_serv;
    private ArrayList<String> log;
    private static ServerCreator sh;
    private ServerState ss;
    int count = 0;

    public static ServerCreator getInstance() {
        if (sh == null) {
            sh = new ServerCreator();
        }
        return sh;
    }

    public static void main(String[] args) throws Exception {
        ServerCreator.getInstance();
    }

    public ServerCreator() {
        Model.setServerRunning(true);

        log = new ArrayList<>();
        setState(ServerState.OFFLINE);
        JFrame frame = new JFrame("ServerCreator");
        frame.setContentPane(serverconfig);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();

                if (cur_serv != null) try {
                    cur_serv.close();
                } catch (ServerNotInitializedException e1) {
                    //e1.printStackTrace(); Won`t happen
                }

                System.out.println("Printing all messages");
                for (String s : log) {
                    System.out.println(s);
                }
            }
        });
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sh.clear();
                    sh.setState(ServerState.WAITING);
                    int port, count;
                    try {
                        port = Integer.parseInt(portTextField.getText());
                        count = Integer.parseInt(socketsComboBox.getSelectedItem().toString());
                        Server server = new Server(sh);
                        if (cur_serv != null) {
                            cur_serv.endGame();
                            cur_serv.close();
                            cur_serv = null;
                        }
                        cur_serv = server;

                        cur_serv.setNewGame(port, ipTextField.getText(), count);
                        cur_serv.start();

                        Model.addRunningPorts(port);

                    } catch (NumberFormatException nfe) {
                        sh.setState(ServerState.OFFLINE);
                        setLogMessage("INPUT1: " + nfe.getMessage() + "\nInvalid format of port[0000-49999]");
                    }
                } catch (Exception er2) {
                    sh.setState(ServerState.OFFLINE);
                    try {
                        cur_serv.close();
                    } catch (ServerNotInitializedException e1) {
                        //e1.printStackTrace(); Won`t happen
                    }
                    cur_serv = null;

                    setLogMessage("ERROR2: " + er2.getMessage() + "\nProbably invalid port or/and ip");
                }
            }
        });
        fetchLocalHostIpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ipTextField.setText("LOCALHOST");
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void clear() {
        count = 0;
        connectedTextField.setText(Integer.toString(count));
    }


    public int getConnectedAmount() {
        return count;
    }

    public ServerState getState() {
        String text = statusTextField.getText();

        switch (text) {
            case "OFFLINE":
                return ServerState.OFFLINE;

            case "WAITING":
                return ServerState.WAITING;

            case "RUNNING":
                return ServerState.RUNNING;

            case "EXITED":
                return ServerState.EXITED;
        }
        return null;
    }

    public void setState(ServerState ss) {
        switch (ss) {
            case OFFLINE:
                statusTextField.setText("OFFLINE");
                break;
            case WAITING:
                statusTextField.setText("WAITING");
                break;
            case RUNNING:
                statusTextField.setText("RUNNING");
                break;
            case EXITED:
                statusTextField.setText("EXITED");
                break;
        }
    }

    public void increment() {
        count++;
        connectedTextField.setText(Integer.toString(count));
    }

    private void setLogMessage(String text) {
        log.add(text);
        logTextArea.setText(text);
    }

    public void setLogMessage(String text, Server s) {
        if (s != cur_serv)
            return;

        if (text.equals("Server closed action"))
            setState(ServerState.OFFLINE);

        log.add(text);
        logTextArea.setText(text);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        serverconfig = new JPanel();
        serverconfig.setLayout(new GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        serverconfig.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "SERVER", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, serverconfig.getFont()), new Color(-4473925)));
        runButton = new JButton();
        runButton.setText("Run");
        serverconfig.add(runButton, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exitButton = new JButton();
        exitButton.setText("Exit");
        serverconfig.add(exitButton, new GridConstraints(8, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logTextArea = new JTextArea();
        logTextArea.setAutoscrolls(true);
        logTextArea.setEditable(false);
        logTextArea.setInheritsPopupMenu(false);
        logTextArea.setLineWrap(false);
        logTextArea.setText("Waiting");
        logTextArea.setToolTipText("Log");
        serverconfig.add(logTextArea, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 100), null, 0, false));
        statusTextField = new JTextField();
        statusTextField.setEditable(false);
        statusTextField.setEnabled(true);
        statusTextField.setText("Offline");
        statusTextField.setToolTipText("Offline,Waiting,Running");
        serverconfig.add(statusTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        serverconfig.add(panel1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(204, 30), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Sockets");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Port");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        portTextField = new JTextField();
        portTextField.setText("9007");
        panel1.add(portTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        socketsComboBox = new JComboBox();
        socketsComboBox.setAutoscrolls(false);
        socketsComboBox.setEditable(false);
        socketsComboBox.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("1");
        defaultComboBoxModel1.addElement("2");
        defaultComboBoxModel1.addElement("3");
        defaultComboBoxModel1.addElement("6");
        socketsComboBox.setModel(defaultComboBoxModel1);
        panel1.add(socketsComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        connectedTextField = new JTextField();
        connectedTextField.setEditable(false);
        connectedTextField.setText("0");
        serverconfig.add(connectedTextField, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        Status = new JLabel();
        Status.setHorizontalAlignment(0);
        Status.setHorizontalTextPosition(0);
        Status.setText("Status");
        Status.setVerticalAlignment(0);
        Status.setVerticalTextPosition(0);
        Status.putClientProperty("html.disable", Boolean.FALSE);
        serverconfig.add(Status, new GridConstraints(4, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(198, 20), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(0);
        label3.setText("Connected");
        serverconfig.add(label3, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(198, 16), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(0);
        label4.setHorizontalTextPosition(0);
        label4.setText("Setup");
        label4.setVerticalAlignment(0);
        label4.setVerticalTextPosition(0);
        label4.putClientProperty("html.disable", Boolean.FALSE);
        serverconfig.add(label4, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(198, 15), null, 0, false));
        fetchLocalHostIpButton = new JButton();
        fetchLocalHostIpButton.setText("FetchLocalHostIp");
        serverconfig.add(fetchLocalHostIpButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(126, 23), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        serverconfig.add(panel2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(184, 15), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("IP");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ipTextField = new JTextField();
        ipTextField.setText("localhost");
        ipTextField.setToolTipText("IP x.x.x.x");
        panel2.add(ipTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setForeground(new Color(-16315055));
        separator1.setOrientation(0);
        serverconfig.add(separator1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        separator2.setForeground(new Color(-16315055));
        separator2.setOrientation(0);
        serverconfig.add(separator2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return serverconfig;
    }
}
