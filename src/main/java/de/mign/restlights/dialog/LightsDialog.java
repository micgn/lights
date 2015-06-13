package de.mign.restlights.dialog;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import de.mign.restlights.StatusSubscriber;

@SuppressWarnings("serial")
public class LightsDialog extends JFrame implements StatusSubscriber {

    private Startable startable;
    final JTextArea logArea;

    public LightsDialog() {
        super("Extreme Feedback Controller");
        setSize(1218, 493);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container container = getContentPane();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{62, 95, 20, 886, 167, 0};
        gridBagLayout.rowHeights = new int[]{30, 30, 0, 30, 13, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0,
            Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0,
            Double.MIN_VALUE};
        getContentPane().setLayout(gridBagLayout);

        logArea = new JTextArea("");
        logArea.setTabSize(4);
        logArea.setLineWrap(true);
        logArea.setEditable(false);
        logArea.setRows(15);
        logArea.setAutoscrolls(true);
        logArea.setSize(300, 300);
        GridBagConstraints gbc_result = new GridBagConstraints();
        gbc_result.insets = new Insets(0, 0, 5, 5);
        gbc_result.fill = GridBagConstraints.BOTH;
        gbc_result.gridwidth = 3;
        gbc_result.anchor = GridBagConstraints.WEST;
        gbc_result.gridx = 1;
        gbc_result.gridy = 1;
        container.add(logArea, gbc_result);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 4;
        gbc.gridy = 1;
        JLabel label = new JLabel(new ImageIcon(getClass().getResource(
                "/de/mign/restlights/dialog/lights.jpg")), JLabel.LEFT);
        getContentPane().add(label, gbc);

        JButton startBtn = new JButton("Start");

        GridBagConstraints gbc_startBtn = new GridBagConstraints();
        gbc_startBtn.anchor = GridBagConstraints.NORTHWEST;
        gbc_startBtn.insets = new Insets(0, 0, 5, 5);
        gbc_startBtn.gridx = 1;
        gbc_startBtn.gridy = 3;
        container.add(startBtn, gbc_startBtn);

        JButton stopBtn = new JButton("Stop");
        stopBtn.setHorizontalAlignment(SwingConstants.LEFT);
        stopBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                startable.stop();
            }
        });
        GridBagConstraints gbc_stopBtn = new GridBagConstraints();
        gbc_stopBtn.insets = new Insets(0, 0, 5, 5);
        gbc_stopBtn.gridx = 2;
        gbc_stopBtn.gridy = 3;
        getContentPane().add(stopBtn, gbc_stopBtn);

        startBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                startable.start();
            }
        });
    }

    private List<String> logTextList = new ArrayList<String>();

    @Override
    public void say(String s) {
        addToLogText("INFO", s);
    }

    @Override
    public void error(String m) {
        addToLogText("ERROR", m);
    }

    private DateFormat df = new SimpleDateFormat("HH:mm:ss");

    private void addToLogText(String type, String s) {
        String line = df.format(new Date());
        line += " " + type + " ";
        line += s;

        if (logTextList.size() > 20) {
            logTextList.remove(0);
        }
        logTextList.add(line);

        String text = "";
        for (String str : logTextList) {
            text += str + "\n";
        }
        logArea.setText(text);
    }

    public void setStartable(Startable startable) {
        this.startable = startable;
    }
}
