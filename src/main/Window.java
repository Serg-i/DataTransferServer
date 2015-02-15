package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import validation.PortVerifier;

public class Window {
	private static final String TITLE = "Server";
	private static final String START_BUTTON_TITLE = "Start server";
	private static final String PORT_INPUT_LABEL = "Port";
	private static final String SELECT_FILDER_LABEL = "Output folder";
	private static final int DEFAULT_PORT = 2154;
	private int portNumber = 2154;
	private String outDir;
	private static JButton startServerBut;
	private static JButton selectFolderButton;
	public static JTextArea area;
	private JTextField portInput;
	private JLabel portInputLabel;
	private JTextField selectedDirectory;
	GridLayout bar;

	Window() {
		JFrame mainFrame = new JFrame(TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(300, 500);
		mainFrame.setLayout(new BorderLayout());

		JPanel portPanel = new JPanel();
		portPanel.setLayout(new GridLayout(2, 2));

		area = new JTextArea();
		JScrollPane sp = new JScrollPane(area);
		area.setEditable(false);
		portInput = new JTextField((new Integer(portNumber)).toString());
		portInput.setInputVerifier(new PortVerifier());
		portInputLabel = new JLabel(PORT_INPUT_LABEL);
		startServerBut = new JButton(START_BUTTON_TITLE);
		selectFolderButton = new JButton(SELECT_FILDER_LABEL);
		selectedDirectory = new JTextField();
		selectedDirectory.setEditable(false);
		portPanel.add(portInputLabel);
		portPanel.add(portInput);
		portPanel.add(selectFolderButton);
		portPanel.add(selectedDirectory);

		mainFrame.add(portPanel, BorderLayout.NORTH);
		mainFrame.add(sp);
		mainFrame.add(startServerBut, BorderLayout.SOUTH);

		mainFrame.setAlwaysOnTop(true);
		mainFrame.setVisible(true);
		startServerBut.setEnabled(false);

		startServerBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				portNumber = 0;
				try {
					portNumber = (portInput.getText().length() > 0) ? Integer
							.parseInt(portInput.getText()) : DEFAULT_PORT;
					outDir = selectedDirectory.getText();
					Server.getServer(portNumber,outDir).start();
				} catch (NumberFormatException e) {
					Window.log(portInput.getText()
							+ " can't transfer to number ");
				}
			}
		});
		selectFolderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					selectedDirectory.setText(file.getPath());
				}
			}
		});
	}

	public static void log(String message) {
		area.append(message);
	}

	public static void blockButton() {
		startServerBut.setEnabled(false);
	}

	public static void unblockButton() {
		startServerBut.setEnabled(true);
	}

}
