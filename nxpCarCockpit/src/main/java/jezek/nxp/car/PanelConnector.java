package jezek.nxp.car;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import jssc.SerialPortList;

public class PanelConnector extends JPanel {

	private JLabel lblComPort;
	private JTextField txtComPort;
	private JButton btnConnect;
	private JButton btnAutodetect;
	private JRadioButton rdbtnUsbWindows;
	private JRadioButton rdbtnUsbLinux;
	private JRadioButton rdbtnTcp;
	private ButtonGroup buttonGroup;
	private JButton btnDisconnect;
	private CommunicationWrapper communicationWrapper;
	private DataTransformer dataTransformerNewFormat;
	private DataTransformer dataTransformerOldFormat;
	private JRadioButton rdbtnWifi;
	private JCheckBox chckbxUseFakeData;

	

	public PanelConnector(DataTransformer dataTransformerNewFormat, DataTransformer dataTransformerOldFormat) {
		super();
		this.dataTransformerNewFormat = dataTransformerNewFormat;
		this.dataTransformerOldFormat = dataTransformerOldFormat;
		getButtonGroup();
		getRdbtnUsbWindows().doClick();
		initialize();
	}
	
	private void initialize() {
		setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Connection",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gblPanelConnect = new GridBagLayout();
		setLayout(gblPanelConnect);
		GridBagConstraints gbcRdbtnUsbWindows = new GridBagConstraints();
		gbcRdbtnUsbWindows.insets = new Insets(0, 0, 5, 5);
		gbcRdbtnUsbWindows.gridx = 0;
		gbcRdbtnUsbWindows.gridy = 0;
		add(getRdbtnUsbWindows(), gbcRdbtnUsbWindows);
		GridBagConstraints gbcRdbtnUsbLinux = new GridBagConstraints();
		gbcRdbtnUsbLinux.gridwidth = 2;
		gbcRdbtnUsbLinux.insets = new Insets(0, 0, 5, 5);
		gbcRdbtnUsbLinux.gridx = 1;
		gbcRdbtnUsbLinux.gridy = 0;
		add(getRdbtnUsbLinux(), gbcRdbtnUsbLinux);
		GridBagConstraints gbcRdbtnTcp = new GridBagConstraints();
		gbcRdbtnTcp.insets = new Insets(0, 0, 5, 5);
		gbcRdbtnTcp.gridx = 0;
		gbcRdbtnTcp.gridy = 1;
		add(getRdbtnTcp(), gbcRdbtnTcp);
		GridBagConstraints gbcRdbtnWifi = new GridBagConstraints();
		gbcRdbtnWifi.insets = new Insets(0, 0, 5, 5);
		gbcRdbtnWifi.gridx = 1;
		gbcRdbtnWifi.gridy = 1;
		add(getRdbtnWifi(), gbcRdbtnWifi);
		GridBagConstraints gbcLblComPort = new GridBagConstraints();
		gbcLblComPort.insets = new Insets(0, 0, 5, 5);
		gbcLblComPort.anchor = GridBagConstraints.EAST;
		gbcLblComPort.gridx = 0;
		gbcLblComPort.gridy = 2;
		add(getLblComPort(), gbcLblComPort);
		GridBagConstraints gbcTxtComPort = new GridBagConstraints();
		gbcTxtComPort.gridwidth = 2;
		gbcTxtComPort.insets = new Insets(0, 0, 5, 5);
		gbcTxtComPort.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtComPort.gridx = 1;
		gbcTxtComPort.gridy = 2;
		add(getTxtComPort(), gbcTxtComPort);
		GridBagConstraints gbcChckbxUseFakeData = new GridBagConstraints();
		gbcChckbxUseFakeData.insets = new Insets(0, 0, 5, 0);
		gbcChckbxUseFakeData.gridx = 2;
		gbcChckbxUseFakeData.gridy = 1;
		add(getChckbxUseFakeData(), gbcChckbxUseFakeData);
		GridBagConstraints gbcBtnAutodetect = new GridBagConstraints();
		gbcBtnAutodetect.insets = new Insets(0, 0, 5, 5);
		gbcBtnAutodetect.gridx = 0;
		gbcBtnAutodetect.gridy = 3;
		add(getBtnAutodetect(), gbcBtnAutodetect);
		GridBagConstraints gbcBtnConnect = new GridBagConstraints();
		gbcBtnConnect.gridwidth = 2;
		gbcBtnConnect.insets = new Insets(0, 0, 5, 5);
		gbcBtnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnConnect.gridx = 1;
		gbcBtnConnect.gridy = 3;
		add(getBtnConnect(), gbcBtnConnect);
		GridBagConstraints gbcBtnDisconnect = new GridBagConstraints();
		gbcBtnDisconnect.gridwidth = 2;
		gbcBtnDisconnect.insets = new Insets(0, 0, 0, 5);
		gbcBtnDisconnect.gridx = 1;
		gbcBtnDisconnect.gridy = 4;
		add(getBtnDisconnect(), gbcBtnDisconnect);

	}

	public CommunicationWrapper getCommunicationWrapper() {
		if (communicationWrapper == null) {
			if (getRdbtnUsbWindows().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperWinUsb(dataTransformerOldFormat, getTxtComPort().getText());
			} else if (getRdbtnUsbLinux().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperLinuxUsb(dataTransformerOldFormat,
						"/dev/tty" + getTxtComPort().getText());
			} else if (getRdbtnTcp().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperTcp(dataTransformerOldFormat, getTxtComPort().getText());
			} else if (getRdbtnWifi().getModel().isSelected()) {
				try {
					UDPServer server =new UDPServer(Integer.parseInt(getTxtComPort().getText()), dataTransformerNewFormat);
					server.setFakeMode(getChckbxUseFakeData().isSelected());
					communicationWrapper = server;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return communicationWrapper;
	}

	private JLabel getLblComPort() {
		if (lblComPort == null) {
			lblComPort = new JLabel("Com Port:");
		}
		return lblComPort;
	}

	private JTextField getTxtComPort() {
		if (txtComPort == null) {
			txtComPort = new JTextField();
			txtComPort.setText("/dev/ttyACM0");
			txtComPort.setColumns(10);
		}
		return txtComPort;
	}

	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton("Connect");
			btnConnect.addActionListener(e -> {
				getCommunicationWrapper().startRead();
				setBackground(getBackground());
				getBtnConnect().setEnabled(false);
				getBtnDisconnect().setEnabled(true);
			});
		}
		return btnConnect;
	}

	private JButton getBtnAutodetect() {
		if (btnAutodetect == null) {
			btnAutodetect = new JButton("Autodetect");
			btnAutodetect.addActionListener(e -> {
				JOptionPane.showMessageDialog(getRootPane(), "Disconect car!");
				List<String> oldNames = Arrays.asList(SerialPortList.getPortNames());
				JOptionPane.showMessageDialog(getRootPane(), "Connect car again!");
				List<String> newNames = Arrays.asList(SerialPortList.getPortNames());
				String foundName = newNames.stream().filter(name -> !oldNames.contains(name)).findFirst().orElse(null);
				if (foundName != null) {
					getTxtComPort().setText(foundName);
					JOptionPane.showMessageDialog(getRootPane(), "Port sucessfully detected!");
				} else {
					JOptionPane.showMessageDialog(getRootPane(), "Port detection failed!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		return btnAutodetect;
	}

	private JRadioButton getRdbtnUsbWindows() {
		if (rdbtnUsbWindows == null) {
			rdbtnUsbWindows = new JRadioButton("USB windows");
			rdbtnUsbWindows.addItemListener(e -> changeConectionVew());
		}
		return rdbtnUsbWindows;
	}

	private JRadioButton getRdbtnUsbLinux() {
		if (rdbtnUsbLinux == null) {
			rdbtnUsbLinux = new JRadioButton("USB linux");
			rdbtnUsbLinux.addItemListener(e -> changeConectionVew());
		}
		return rdbtnUsbLinux;
	}

	private JRadioButton getRdbtnTcp() {
		if (rdbtnTcp == null) {
			rdbtnTcp = new JRadioButton("TCP");
			rdbtnTcp.addItemListener(e -> changeConectionVew());
		}
		return rdbtnTcp;
	}

	/**
	 * @wbp.nonvisual location=427,707
	 */
	private ButtonGroup getButtonGroup() {
		if (buttonGroup == null) {
			buttonGroup = new ButtonGroup();
			buttonGroup.add(getRdbtnUsbWindows());
			buttonGroup.add(getRdbtnUsbLinux());
			buttonGroup.add(getRdbtnTcp());
			buttonGroup.add(getRdbtnWifi());
		}
		return buttonGroup;
	}

	private void changeConectionVew() {
		getBtnAutodetect().setEnabled(getRdbtnUsbWindows().getModel().isSelected());
		getChckbxUseFakeData().setEnabled(getRdbtnWifi().getModel().isSelected());
		if (getRdbtnUsbWindows().getModel().isSelected()) {
			getLblComPort().setText("Comm port (comm29)");
			getTxtComPort().setText("com29");
		} else if (getRdbtnUsbLinux().getModel().isSelected()) {
			getLblComPort().setText("Comm port /dev/tty");
			getTxtComPort().setText("ACM0");
		} else if (getRdbtnTcp().getModel().isSelected()) {
			getLblComPort().setText("Address (host:port)");
			getTxtComPort().setText("otfeia406a.vsb.cz:40460");
		} else if (getRdbtnWifi().getModel().isSelected()) {
			getLblComPort().setText("Port");
			getTxtComPort().setText("5556");
		}
	}

	private JButton getBtnDisconnect() {
		if (btnDisconnect == null) {
			btnDisconnect = new JButton("Disconnect");
			btnDisconnect.setEnabled(false);
			btnDisconnect.addActionListener(e -> {
				try {
					getCommunicationWrapper().close();
					communicationWrapper = null;
					getBtnDisconnect().setEnabled(false);
					getBtnConnect().setEnabled(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			});
		}
		return btnDisconnect;
	}

	private JRadioButton getRdbtnWifi() {
		if (rdbtnWifi == null) {
			rdbtnWifi = new JRadioButton("WiFi");
			rdbtnWifi.addItemListener(e -> changeConectionVew());
		}
		return rdbtnWifi;
	}
	
	private JCheckBox getChckbxUseFakeData() {
		if (chckbxUseFakeData == null) {
			chckbxUseFakeData = new JCheckBox("Use fake data");
		}
		return chckbxUseFakeData;
	}
}
