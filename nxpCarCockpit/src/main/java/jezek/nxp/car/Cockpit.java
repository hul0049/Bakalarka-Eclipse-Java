package jezek.nxp.car;

import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortList;

import javax.swing.event.ChangeEvent;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Cockpit extends JFrame {

	private static final Logger logger = LogManager.getLogger(Cockpit.class);

	private static int IMAGE_HEIGHT = 800;
	private static int IMAGE_WIDTH_ZOOM = 4;

	private AnimImage animImage;
	private CommunicationWrapper communicationWrapper;
	private Tfc tfc;
	private ImageBuffer imageBuffer;
	private ImageBuffer2 imageBuffer2;
	private JPanel panelCamerra;
	private JPanel panelSetting;
	private JPanel panelControl;
	private JPanel panelData;
	private JSlider sliderBrightness;
	private JPanel panelSwitches;
	private JToggleButton tglbtnButton2;
	private JToggleButton tglbtnDipswitch1;
	private JToggleButton tglbtnDipswitch2;
	private JToggleButton tglbtnDipswitch3;
	private JToggleButton tglbtnDipswitch4;
	private JToggleButton tglbtnButton1;
	private JPanel panelNumericData;
	private JPanel panelAnalogText;
	private JLabel lblPot;
	private JLabel lblPot1Value;
	private JLabel lblPot_1;
	private JLabel lblPot2Value;
	private JLabel lblFb;
	private JLabel lblFbAvalue;
	private JLabel lblFb_1;
	private JLabel lblFbBvalue;
	private JLabel lblBattery;
	private JLabel lblBatteryValue;
	private JSlider sliderServo0;
	private JSlider sliderServo1;
	private JPanel panelServo0;
	private JPanel panelServo1;
	private JSlider sliderMaxLrServo0;
	private JSlider sliderMaxLrServo1;
	private JSlider sliderPwmMax;
	private JPanel panel;
	private JToggleButton tglbtnLed1;
	private JToggleButton tglbtnLed2;
	private JToggleButton tglbtnLed3;
	private JToggleButton tglbtnLed4;
	private JPanel panelMotorsOnOff;
	private JToggleButton tglbtnPwm;
	private JToggleButton tglbtnServo;
	private JPanel panelStearingTitle;
	private JPanel panelStearing;
	private JPanel panel1;
	private JButton btnSetting;
	private JPanel panelConnect;
	private JLabel lblComPort;
	private JTextField txtComPort;
	private JButton btnConnect;
	private JButton btnAutodetect;
	private JPanel panelRecording;
	private JTextField txtFile;
	private JLabel lblRecordFile;
	private JButton btnSelectFile;
	private JToggleButton tglbtnRecord;
	private JTabbedPane tabbedPane;
	private JPanel panelCockpit;
	private JPanel panelMonitor;
	private AnimImage anmImgMonitor;
	private UDPServer udpServer;
	private JPanel panel2;
	private JButton btnStop;
	private JButton btnStart;
	private JScrollBar scrollBar;
	private JTextField txtUdpPort;
	private DrivingRecord drivingRecord;
	private JButton btnXmlExport;
	private JButton btnXmlImport;
	private JButton btnBinExport;
	private JButton btnBinImport;
	private JPanel panel3;
	private JSlider sliderBrightness2;
	private JLabel lblColumnCount;
	private JSpinner spinnerColumnCount;
	private JLabel[] labelIndex = new JLabel[6];
	private JLabel lblColumnIndex;
	private JLabel lblColumnWidth;
	private JLabel lblColumnZoom;
	private JSpinner[] spinnerColumnWidth = new JSpinner[6];
	private JSpinner[] spinnerColumnZoom = new JSpinner[6];
	private JLabel lblPictureHeight;
	private JSpinner spinnerPictureHeight;
	private JCheckBox chckbxUseFakeData;
	private JButton btnRecreateImage;
	private JLabel lblDataToColumn;
	private JSpinner[] spinnerDataColumn = new JSpinner[6];
	private JButton btnClear;
	private JButton btnHistogram;
	private JButton btnGradient;
	private JRadioButton rdbtnUsbWindows;
	private JRadioButton rdbtnUsbLinux;
	private JRadioButton rdbtnTcp;
	private ButtonGroup buttonGroup;
	private JButton btnDisconnect;
	private JSpinner spnHeightZoom;
	private JLabel lblHeightZoom;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cockpit frame = new Cockpit();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Cockpit() {
		getPanelCockpit().setBackground(Color.RED);
		getButtonGroup();
		initialize();
		getRdbtnUsbWindows().doClick();
	}

	private void initialize() {
		setBounds(100, 100, 996, 690);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		GridBagConstraints gbcTabbedPane = new GridBagConstraints();
		gbcTabbedPane.insets = new Insets(0, 0, 0, 5);
		gbcTabbedPane.fill = GridBagConstraints.BOTH;
		gbcTabbedPane.gridx = 0;
		gbcTabbedPane.gridy = 2;
		getContentPane().add(getTabbedPane());
	}

	private AnimImage getAnimImage() {
		if (animImage == null) {
			animImage = new AnimImage(getImageBuffer());
		}
		return animImage;
	}

	/**
	 * @wbp.nonvisual location=151,739
	 */
	private CommunicationWrapper getCommunicationWrapper() {
		if (communicationWrapper == null) {
			if (getRdbtnUsbWindows().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperWinUsb(getTfc(), getTxtComPort().getText());
			} else if (getRdbtnUsbLinux().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperLinuxUsb(getTfc(),
						"/dev/tty" + getTxtComPort().getText());
			} else if (getRdbtnTcp().getModel().isSelected()) {
				communicationWrapper = new CommunicationWrapperTcp(getTfc(), getTxtComPort().getText());
			}
			// communicationWrapper = new CommunicationWrapperTcp(getTfc(),
			// "192.168.46.193", 40460);
			// getRs485Line().setComPort(getTxtComPort().getText());
		}
		return communicationWrapper;
	}

	private Tfc getTfc() {
		if (tfc == null) {
			tfc = new Tfc();
		}
		return tfc;
	}

	/**
	 * @wbp.nonvisual location=54,739
	 */
	private ImageBuffer getImageBuffer() {
		if (imageBuffer == null) {
			imageBuffer = new ImageBuffer(128, IMAGE_WIDTH_ZOOM, IMAGE_HEIGHT, 1);
			getTfc().addImageBuffer(imageBuffer);
		}
		return imageBuffer;
	}

	private JPanel getPanelCamerra() {
		if (panelCamerra == null) {
			panelCamerra = new JPanel();
			panelCamerra.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Camerra Data",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			panelCamerra.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelCamerra.add(getAnimImage());
		}
		return panelCamerra;
	}

	private JPanel getPanelSetting() {
		if (panelSetting == null) {
			panelSetting = new JPanel();
			panelSetting.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Setting",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelSetting = new GridBagLayout();
			panelSetting.setLayout(gblPanelSetting);
			GridBagConstraints gbcPanelServo0 = new GridBagConstraints();
			gbcPanelServo0.gridwidth = 0;
			gbcPanelServo0.weightx = 1.0;
			gbcPanelServo0.fill = GridBagConstraints.HORIZONTAL;
			gbcPanelServo0.anchor = GridBagConstraints.NORTHWEST;
			gbcPanelServo0.insets = new Insets(0, 0, 5, 5);
			panelSetting.add(getPanelServo0(), gbcPanelServo0);
			GridBagConstraints gbcPanelServo1 = new GridBagConstraints();
			gbcPanelServo1.gridwidth = 0;
			gbcPanelServo1.weightx = 1.0;
			gbcPanelServo1.fill = GridBagConstraints.HORIZONTAL;
			gbcPanelServo1.anchor = GridBagConstraints.NORTHWEST;
			gbcPanelServo1.insets = new Insets(0, 0, 5, 5);
			panelSetting.add(getPanelServo1(), gbcPanelServo1);
			GridBagConstraints gbcSliderPwmMax = new GridBagConstraints();
			gbcSliderPwmMax.weightx = 1.0;
			gbcSliderPwmMax.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderPwmMax.insets = new Insets(0, 0, 0, 5);
			gbcSliderPwmMax.anchor = GridBagConstraints.NORTH;
			panelSetting.add(getSliderPwmMax(), gbcSliderPwmMax);
			GridBagConstraints gbcBtnSetting = new GridBagConstraints();
			gbcBtnSetting.fill = GridBagConstraints.BOTH;
			gbcBtnSetting.weightx = 0.3;
			panelSetting.add(getBtnSetting(), gbcBtnSetting);
		}
		return panelSetting;
	}

	private JPanel getPanelControl() {
		if (panelControl == null) {
			panelControl = new JPanel();
			panelControl.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Contrrol",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelControl = new GridBagLayout();
			panelControl.setLayout(gblPanelControl);
			GridBagConstraints gbcPanel1 = new GridBagConstraints();
			gbcPanel1.insets = new Insets(0, 0, 5, 5);
			gbcPanel1.fill = GridBagConstraints.BOTH;
			gbcPanel1.gridx = 0;
			gbcPanel1.gridy = 0;
			panelControl.add(getPanel1_5(), gbcPanel1);
			GridBagConstraints gbcPanelStearingTitle = new GridBagConstraints();
			gbcPanelStearingTitle.gridwidth = 0;
			gbcPanelStearingTitle.weighty = 1.0;
			gbcPanelStearingTitle.weightx = 1.0;
			gbcPanelStearingTitle.fill = GridBagConstraints.BOTH;
			gbcPanelStearingTitle.gridx = 0;
			gbcPanelStearingTitle.gridy = 2;
			panelControl.add(getPanelStearingTitle(), gbcPanelStearingTitle);
		}
		return panelControl;
	}

	private JPanel getPanelData() {
		if (panelData == null) {
			panelData = new JPanel();
			panelData.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Data",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelData = new GridBagLayout();
			panelData.setLayout(gblPanelData);
			GridBagConstraints gbcPanelNumericData = new GridBagConstraints();
			gbcPanelNumericData.weightx = 1.0;
			gbcPanelNumericData.weighty = 1.0;
			gbcPanelNumericData.insets = new Insets(0, 0, 0, 5);
			gbcPanelNumericData.fill = GridBagConstraints.BOTH;
			panelData.add(getPanel1_1(), gbcPanelNumericData);
			GridBagConstraints gbcPanelCamerra = new GridBagConstraints();
			gbcPanelCamerra.anchor = GridBagConstraints.NORTHWEST;
			panelData.add(getPanelCamerra(), gbcPanelCamerra);
		}
		return panelData;
	}

	private JSlider getSliderBrightness() {
		if (sliderBrightness == null) {
			sliderBrightness = new JSlider();
			sliderBrightness
					.addChangeListener(e -> getImageBuffer().setBrightness(getSliderBrightness().getValue() / 100.0));
			sliderBrightness.setPaintLabels(true);
			sliderBrightness.setBorder(
					new TitledBorder(null, "Brightness", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderBrightness.setMinorTickSpacing(20);
			sliderBrightness.setValue(100);
			sliderBrightness.setMaximum(800);
			sliderBrightness.setMinimum(100);
			sliderBrightness.setMajorTickSpacing(100);
			sliderBrightness.setPaintTicks(true);
			sliderBrightness.setToolTipText("Brightness");
			Dictionary<Integer, JComponent> labels = new Hashtable<>();
			labels.put(100, new JLabel("1"));
			labels.put(200, new JLabel("2"));
			labels.put(300, new JLabel("3"));
			labels.put(400, new JLabel("4"));
			labels.put(500, new JLabel("5"));
			labels.put(600, new JLabel("6"));
			labels.put(700, new JLabel("7"));
			labels.put(800, new JLabel("8"));
			sliderBrightness.setLabelTable(labels);
		}
		return sliderBrightness;
	}

	private JPanel getPanelSwitches() {
		if (panelSwitches == null) {
			panelSwitches = new JPanel();
			GridBagLayout gblPanelSwitches = new GridBagLayout();
			panelSwitches.setLayout(gblPanelSwitches);
			GridBagConstraints gbcTglbtnButton1 = new GridBagConstraints();
			gbcTglbtnButton1.anchor = GridBagConstraints.WEST;
			panelSwitches.add(getTglbtnButton2(), gbcTglbtnButton1);
			GridBagConstraints gbcTglbtnDipswitch1 = new GridBagConstraints();
			gbcTglbtnDipswitch1.anchor = GridBagConstraints.NORTHWEST;
			panelSwitches.add(getTglbtnDipswitch1(), gbcTglbtnDipswitch1);
			GridBagConstraints gbcTglbtnDipswitch2 = new GridBagConstraints();
			gbcTglbtnDipswitch2.anchor = GridBagConstraints.WEST;
			panelSwitches.add(getTglbtnDipswitch2(), gbcTglbtnDipswitch2);
			GridBagConstraints gbcTglbtnDipswitch3 = new GridBagConstraints();
			gbcTglbtnDipswitch3.anchor = GridBagConstraints.WEST;
			panelSwitches.add(getTglbtnDipswitch3(), gbcTglbtnDipswitch3);
			GridBagConstraints gbcTglbtnDipswitch4 = new GridBagConstraints();
			gbcTglbtnDipswitch4.anchor = GridBagConstraints.WEST;
			panelSwitches.add(getTglbtnDipswitch4(), gbcTglbtnDipswitch4);
			GridBagConstraints gbcTglbtnButton2 = new GridBagConstraints();
			gbcTglbtnButton2.anchor = GridBagConstraints.WEST;
			panelSwitches.add(getTglbtnButton1(), gbcTglbtnButton2);
		}
		return panelSwitches;
	}

	private JToggleButton getTglbtnButton2() {
		if (tglbtnButton2 == null) {
			tglbtnButton2 = new JToggleButton("Btn B");
		}
		return tglbtnButton2;
	}

	private JToggleButton getTglbtnDipswitch1() {
		if (tglbtnDipswitch1 == null) {
			tglbtnDipswitch1 = new JToggleButton("");
			tglbtnDipswitch1.setSelectedIcon(
					new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesON.png")));
			tglbtnDipswitch1
					.setIcon(new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesOFF.png")));
			tglbtnDipswitch1.setToolTipText("1");
			tglbtnDipswitch1.setMaximumSize(new Dimension(15, 40));
			tglbtnDipswitch1.setMinimumSize(new Dimension(15, 40));
			tglbtnDipswitch1.setPreferredSize(new Dimension(15, 40));
			tglbtnDipswitch1.setVerticalTextPosition(SwingConstants.BOTTOM);
			tglbtnDipswitch1.setHorizontalTextPosition(SwingConstants.CENTER);
			tglbtnDipswitch1.setContentAreaFilled(false);
			tglbtnDipswitch1.setBorderPainted(false);
			tglbtnDipswitch1.setRolloverEnabled(false);
		}
		return tglbtnDipswitch1;
	}

	private JToggleButton getTglbtnDipswitch2() {
		if (tglbtnDipswitch2 == null) {
			tglbtnDipswitch2 = new JToggleButton("");
			tglbtnDipswitch2.setToolTipText("2");
			tglbtnDipswitch2.setMaximumSize(new Dimension(15, 40));
			tglbtnDipswitch2.setMinimumSize(new Dimension(15, 40));
			tglbtnDipswitch2.setPreferredSize(new Dimension(15, 40));
			tglbtnDipswitch2.setVerticalTextPosition(SwingConstants.BOTTOM);
			tglbtnDipswitch2.setHorizontalTextPosition(SwingConstants.CENTER);
			tglbtnDipswitch2.setContentAreaFilled(false);
			tglbtnDipswitch2.setBorderPainted(false);
			tglbtnDipswitch2.setRolloverEnabled(false);
			tglbtnDipswitch2
					.setIcon(new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesOFF.png")));
			tglbtnDipswitch2.setSelectedIcon(
					new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesON.png")));
		}
		return tglbtnDipswitch2;
	}

	private JToggleButton getTglbtnDipswitch3() {
		if (tglbtnDipswitch3 == null) {
			tglbtnDipswitch3 = new JToggleButton("");
			tglbtnDipswitch3.setToolTipText("3");
			tglbtnDipswitch3.setMaximumSize(new Dimension(15, 40));
			tglbtnDipswitch3.setMinimumSize(new Dimension(15, 40));
			tglbtnDipswitch3.setPreferredSize(new Dimension(15, 40));
			tglbtnDipswitch3.setVerticalTextPosition(SwingConstants.BOTTOM);
			tglbtnDipswitch3.setHorizontalTextPosition(SwingConstants.CENTER);
			tglbtnDipswitch3.setContentAreaFilled(false);
			tglbtnDipswitch3.setBorderPainted(false);
			tglbtnDipswitch3.setRolloverEnabled(false);
			tglbtnDipswitch3
					.setIcon(new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesOFF.png")));
			tglbtnDipswitch3.setSelectedIcon(
					new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesON.png")));
		}
		return tglbtnDipswitch3;
	}

	private JToggleButton getTglbtnDipswitch4() {
		if (tglbtnDipswitch4 == null) {
			tglbtnDipswitch4 = new JToggleButton("");
			tglbtnDipswitch4.setToolTipText("4");
			tglbtnDipswitch4.setMaximumSize(new Dimension(15, 40));
			tglbtnDipswitch4.setMinimumSize(new Dimension(15, 40));
			tglbtnDipswitch4.setPreferredSize(new Dimension(15, 40));
			tglbtnDipswitch4.setVerticalTextPosition(SwingConstants.BOTTOM);
			tglbtnDipswitch4.setHorizontalTextPosition(SwingConstants.CENTER);
			tglbtnDipswitch4.setContentAreaFilled(false);
			tglbtnDipswitch4.setBorderPainted(false);
			tglbtnDipswitch4.setRolloverEnabled(false);
			tglbtnDipswitch4
					.setIcon(new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesOFF.png")));
			tglbtnDipswitch4.setSelectedIcon(
					new ImageIcon(Cockpit.class.getResource("/jezek/nxp/car/resources/DIPSwitchesON.png")));
		}
		return tglbtnDipswitch4;
	}

	private JToggleButton getTglbtnButton1() {
		if (tglbtnButton1 == null) {
			tglbtnButton1 = new JToggleButton("Btn A");
		}
		return tglbtnButton1;
	}

	private JPanel getPanel1_1() {
		if (panelNumericData == null) {
			panelNumericData = new JPanel();
			GridBagLayout gblPanelNumericData = new GridBagLayout();
			gblPanelNumericData.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0 };
			gblPanelNumericData.columnWeights = new double[] { 1.0 };
			panelNumericData.setLayout(gblPanelNumericData);
			GridBagConstraints gbcPanelConnect = new GridBagConstraints();
			gbcPanelConnect.gridx = 0;
			gbcPanelConnect.gridy = 0;
			gbcPanelConnect.gridwidth = 0;
			gbcPanelConnect.insets = new Insets(0, 0, 5, 0);
			gbcPanelConnect.fill = GridBagConstraints.BOTH;
			panelNumericData.add(getPanelConnect(), gbcPanelConnect);
			GridBagConstraints gbcPanelRecording = new GridBagConstraints();
			gbcPanelRecording.insets = new Insets(0, 0, 5, 0);
			gbcPanelRecording.fill = GridBagConstraints.BOTH;
			gbcPanelRecording.gridx = 0;
			gbcPanelRecording.gridy = 1;
			panelNumericData.add(getPanelRecording(), gbcPanelRecording);
			GridBagConstraints gbcSliderBrightness = new GridBagConstraints();
			gbcSliderBrightness.gridx = 0;
			gbcSliderBrightness.gridy = 2;
			gbcSliderBrightness.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderBrightness.anchor = GridBagConstraints.NORTHWEST;
			gbcSliderBrightness.insets = new Insets(0, 0, 5, 0);
			gbcSliderBrightness.gridwidth = 0;
			panelNumericData.add(getSliderBrightness(), gbcSliderBrightness);
			GridBagConstraints gbcPanelSwitches = new GridBagConstraints();
			gbcPanelSwitches.gridx = 0;
			gbcPanelSwitches.gridy = 3;
			gbcPanelSwitches.gridwidth = 0;
			gbcPanelSwitches.insets = new Insets(0, 0, 5, 0);
			gbcPanelSwitches.anchor = GridBagConstraints.NORTH;
			panelNumericData.add(getPanelSwitches(), gbcPanelSwitches);
			GridBagConstraints gbcPanelAnalogText = new GridBagConstraints();
			gbcPanelAnalogText.gridx = 0;
			gbcPanelAnalogText.gridy = 4;
			gbcPanelAnalogText.fill = GridBagConstraints.HORIZONTAL;
			gbcPanelAnalogText.anchor = GridBagConstraints.NORTH;
			panelNumericData.add(getPanel1(), gbcPanelAnalogText);
			Thread numericDataUpdater = new Thread(() -> updateAnalogValues());
			numericDataUpdater.start();
		}
		return panelNumericData;
	}

	private void updateAnalogValues() {
		while (!Thread.currentThread().isInterrupted()) {
			Tfc useedTfc = getTfc();
			getTglbtnButton2().setSelected(useedTfc.getPushButton(1) != 0);
			getTglbtnButton1().setSelected(useedTfc.getPushButton(0) != 0);
			getTglbtnDipswitch1().setSelected((useedTfc.getDIPSwitch() & 1) != 0);
			getTglbtnDipswitch2().setSelected((useedTfc.getDIPSwitch() & 2) != 0);
			getTglbtnDipswitch3().setSelected((useedTfc.getDIPSwitch() & 4) != 0);
			getTglbtnDipswitch4().setSelected((useedTfc.getDIPSwitch() & 8) != 0);
			getLblPot1Value().setText(String.format("%d", useedTfc.ReadPot_i(0)));
			getLblPot2Value().setText(String.format("%d", useedTfc.ReadPot_i(1)));
			getLblFbAvalue().setText(String.format("%fA", useedTfc.ReadFB_f(0)));
			getLblFbBvalue().setText(String.format("%fA", useedTfc.ReadFB_f(1)));
			getLblBatteryValue().setText(String.format("%fV", useedTfc.ReadBatteryVoltage_f()));
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private JPanel getPanel1() {
		if (panelAnalogText == null) {
			panelAnalogText = new JPanel();
			panelAnalogText.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"Analog values", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelAnalogText = new GridBagLayout();
			panelAnalogText.setLayout(gblPanelAnalogText);
			GridBagConstraints gbcLblPot = new GridBagConstraints();
			gbcLblPot.insets = new Insets(5, 5, 5, 5);
			gbcLblPot.gridx = 0;
			gbcLblPot.gridy = 0;
			panelAnalogText.add(getLblPot(), gbcLblPot);
			GridBagConstraints gbcLblPot1Value = new GridBagConstraints();
			gbcLblPot1Value.weightx = 1.0;
			gbcLblPot1Value.fill = GridBagConstraints.BOTH;
			gbcLblPot1Value.ipady = 5;
			gbcLblPot1Value.ipadx = 5;
			gbcLblPot1Value.insets = new Insets(5, 5, 5, 5);
			gbcLblPot1Value.gridx = 1;
			gbcLblPot1Value.gridy = 0;
			panelAnalogText.add(getLblPot1Value(), gbcLblPot1Value);
			GridBagConstraints gbcLblPot_1 = new GridBagConstraints();
			gbcLblPot_1.insets = new Insets(5, 5, 5, 5);
			gbcLblPot_1.gridx = 0;
			gbcLblPot_1.gridy = 1;
			panelAnalogText.add(getLblPot_1(), gbcLblPot_1);
			GridBagConstraints gbcLblPot2Value = new GridBagConstraints();
			gbcLblPot2Value.ipady = 5;
			gbcLblPot2Value.ipadx = 5;
			gbcLblPot2Value.fill = GridBagConstraints.BOTH;
			gbcLblPot2Value.insets = new Insets(5, 5, 5, 5);
			gbcLblPot2Value.gridx = 1;
			gbcLblPot2Value.gridy = 1;
			panelAnalogText.add(getLblPot2Value(), gbcLblPot2Value);
			GridBagConstraints gbcLblFb = new GridBagConstraints();
			gbcLblFb.insets = new Insets(5, 5, 5, 5);
			gbcLblFb.gridx = 0;
			gbcLblFb.gridy = 2;
			panelAnalogText.add(getLblFb(), gbcLblFb);
			GridBagConstraints gbcLblFbAvalue = new GridBagConstraints();
			gbcLblFbAvalue.ipady = 5;
			gbcLblFbAvalue.ipadx = 5;
			gbcLblFbAvalue.fill = GridBagConstraints.BOTH;
			gbcLblFbAvalue.insets = new Insets(5, 5, 5, 5);
			gbcLblFbAvalue.gridx = 1;
			gbcLblFbAvalue.gridy = 2;
			panelAnalogText.add(getLblFbAvalue(), gbcLblFbAvalue);
			GridBagConstraints gbcLblFb_1 = new GridBagConstraints();
			gbcLblFb_1.insets = new Insets(5, 5, 5, 5);
			gbcLblFb_1.gridx = 0;
			gbcLblFb_1.gridy = 3;
			panelAnalogText.add(getLblFb_1(), gbcLblFb_1);
			GridBagConstraints gbcLblFbBvalue = new GridBagConstraints();
			gbcLblFbBvalue.ipady = 5;
			gbcLblFbBvalue.ipadx = 5;
			gbcLblFbBvalue.fill = GridBagConstraints.BOTH;
			gbcLblFbBvalue.insets = new Insets(5, 5, 5, 5);
			gbcLblFbBvalue.gridx = 1;
			gbcLblFbBvalue.gridy = 3;
			panelAnalogText.add(getLblFbBvalue(), gbcLblFbBvalue);
			GridBagConstraints gbcLblBattery = new GridBagConstraints();
			gbcLblBattery.insets = new Insets(5, 5, 5, 5);
			gbcLblBattery.gridx = 0;
			gbcLblBattery.gridy = 4;
			panelAnalogText.add(getLblBattery(), gbcLblBattery);
			GridBagConstraints gbcLblBatteryValue = new GridBagConstraints();
			gbcLblBatteryValue.ipady = 5;
			gbcLblBatteryValue.ipadx = 5;
			gbcLblBatteryValue.anchor = GridBagConstraints.SOUTH;
			gbcLblBatteryValue.fill = GridBagConstraints.HORIZONTAL;
			gbcLblBatteryValue.insets = new Insets(5, 5, 5, 5);
			gbcLblBatteryValue.gridx = 1;
			gbcLblBatteryValue.gridy = 4;
			panelAnalogText.add(getLblBatteryValue(), gbcLblBatteryValue);
		}
		return panelAnalogText;
	}

	private JLabel getLblPot() {
		if (lblPot == null) {
			lblPot = new JLabel("Potentiometer 1:");
			lblPot.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblPot;
	}

	private JLabel getLblPot1Value() {
		if (lblPot1Value == null) {
			lblPot1Value = new JLabel("pot1Value");
			lblPot1Value.setHorizontalAlignment(SwingConstants.CENTER);
			lblPot1Value.setOpaque(true);
			lblPot1Value.setBackground(Color.WHITE);
		}
		return lblPot1Value;
	}

	private JLabel getLblPot_1() {
		if (lblPot_1 == null) {
			lblPot_1 = new JLabel("Potentiometer 2:");
			lblPot_1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblPot_1;
	}

	private JLabel getLblPot2Value() {
		if (lblPot2Value == null) {
			lblPot2Value = new JLabel("pot2Value");
			lblPot2Value.setHorizontalAlignment(SwingConstants.CENTER);
			lblPot2Value.setOpaque(true);
			lblPot2Value.setBackground(Color.WHITE);
		}
		return lblPot2Value;
	}

	private JLabel getLblFb() {
		if (lblFb == null) {
			lblFb = new JLabel("Motor A current:");
			lblFb.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblFb;
	}

	private JLabel getLblFbAvalue() {
		if (lblFbAvalue == null) {
			lblFbAvalue = new JLabel("fb1Value");
			lblFbAvalue.setOpaque(true);
			lblFbAvalue.setBackground(Color.WHITE);
			lblFbAvalue.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblFbAvalue;
	}

	private JLabel getLblFb_1() {
		if (lblFb_1 == null) {
			lblFb_1 = new JLabel("Motor B current:");
			lblFb_1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblFb_1;
	}

	private JLabel getLblFbBvalue() {
		if (lblFbBvalue == null) {
			lblFbBvalue = new JLabel("fb2Value");
			lblFbBvalue.setOpaque(true);
			lblFbBvalue.setBackground(Color.WHITE);
			lblFbBvalue.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblFbBvalue;
	}

	private JLabel getLblBattery() {
		if (lblBattery == null) {
			lblBattery = new JLabel("Battery voltage:");
			lblBattery.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblBattery;
	}

	private JLabel getLblBatteryValue() {
		if (lblBatteryValue == null) {
			lblBatteryValue = new JLabel("batteryValue");
			lblBatteryValue.setOpaque(true);
			lblBatteryValue.setBackground(Color.WHITE);
			lblBatteryValue.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblBatteryValue;
	}

	private JSlider getSliderServo0() {
		if (sliderServo0 == null) {
			sliderServo0 = new JSlider();
			sliderServo0.setMinorTickSpacing(500);
			sliderServo0.setMajorTickSpacing(1000);
			sliderServo0.setMaximum(4096);
			sliderServo0.setValue(Tfc.SERVO_DEFAULT_CENTER);
			sliderServo0.setPaintTicks(true);
			sliderServo0.setPaintLabels(true);
			sliderServo0.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Center",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderServo0.addChangeListener(e -> {
				sliderUpdateTitle(getSliderServo0(), "Center - ");
				getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
						(short) getSliderMaxLrServo0().getValue());

			});
			sliderUpdateTitle(sliderServo0, "Center - ");
			getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
					(short) getSliderMaxLrServo0().getValue());
		}
		return sliderServo0;
	}

	private void sliderUpdateTitle(JSlider slider, String title) {
		((TitledBorder) slider.getBorder()).setTitle(title + slider.getValue());
	}

	private JSlider getSliderServo1() {
		if (sliderServo1 == null) {
			sliderServo1 = new JSlider();
			sliderServo1.setMinorTickSpacing(500);
			sliderServo1.setMajorTickSpacing(1000);
			sliderServo1.setMaximum(4096);
			sliderServo1.setValue(Tfc.SERVO_DEFAULT_CENTER);
			sliderServo1.setPaintTicks(true);
			sliderServo1.setPaintLabels(true);
			sliderServo1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Center",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderServo1.addChangeListener(e -> {
				sliderUpdateTitle(getSliderServo1(), "Center - ");
				getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
						(short) getSliderMaxLrServo1().getValue());
			});
			sliderUpdateTitle(sliderServo0, "Center - ");
			getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
					(short) getSliderMaxLrServo1().getValue());
		}
		return sliderServo1;
	}

	private JPanel getPanelServo0() {
		if (panelServo0 == null) {
			panelServo0 = new JPanel();
			panelServo0.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Servo 0",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelServo0 = new GridBagLayout();
			panelServo0.setLayout(gblPanelServo0);
			GridBagConstraints gbcSliderServo0 = new GridBagConstraints();
			gbcSliderServo0.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderServo0.weightx = 1.0;
			gbcSliderServo0.anchor = GridBagConstraints.NORTHWEST;
			gbcSliderServo0.insets = new Insets(0, 0, 0, 5);
			panelServo0.add(getSliderServo0(), gbcSliderServo0);
			GridBagConstraints gbcSliderMaxLrServo0 = new GridBagConstraints();
			gbcSliderMaxLrServo0.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderMaxLrServo0.weightx = 1.0;
			gbcSliderMaxLrServo0.anchor = GridBagConstraints.NORTHWEST;
			panelServo0.add(getSliderMaxLrServo0(), gbcSliderMaxLrServo0);
		}
		return panelServo0;
	}

	private JPanel getPanelServo1() {
		if (panelServo1 == null) {
			panelServo1 = new JPanel();
			panelServo1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Servo 1",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelServo1 = new GridBagLayout();
			panelServo1.setLayout(gblPanelServo1);
			GridBagConstraints gbcSliderServo1 = new GridBagConstraints();
			gbcSliderServo1.weightx = 1.0;
			gbcSliderServo1.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderServo1.anchor = GridBagConstraints.NORTHWEST;
			gbcSliderServo1.insets = new Insets(0, 0, 0, 5);
			panelServo1.add(getSliderServo1(), gbcSliderServo1);
			GridBagConstraints gbcSliderMaxLrServo1 = new GridBagConstraints();
			gbcSliderMaxLrServo1.weightx = 1.0;
			gbcSliderMaxLrServo1.fill = GridBagConstraints.HORIZONTAL;
			gbcSliderMaxLrServo1.anchor = GridBagConstraints.NORTHWEST;
			panelServo1.add(getSliderMaxLrServo1(), gbcSliderMaxLrServo1);
		}
		return panelServo1;
	}

	private JSlider getSliderMaxLrServo0() {
		if (sliderMaxLrServo0 == null) {
			sliderMaxLrServo0 = new JSlider();
			sliderMaxLrServo0.setMinorTickSpacing(10);
			sliderMaxLrServo0.setMajorTickSpacing(50);
			sliderMaxLrServo0.setPaintTicks(true);
			sliderMaxLrServo0.setPaintLabels(true);
			sliderMaxLrServo0.setValue(200);
			sliderMaxLrServo0.setMaximum(400);
			sliderMaxLrServo0.setMinimum(200);
			sliderMaxLrServo0.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Max LR",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderMaxLrServo0.addChangeListener(e -> {
				sliderUpdateTitle(getSliderMaxLrServo0(), "Max LR - ");
				getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
						(short) getSliderMaxLrServo0().getValue());
			});
			sliderUpdateTitle(sliderMaxLrServo0, "Max LR - ");
			getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
					(short) getSliderMaxLrServo0().getValue());

		}
		return sliderMaxLrServo0;
	}

	private JSlider getSliderMaxLrServo1() {
		if (sliderMaxLrServo1 == null) {
			sliderMaxLrServo1 = new JSlider();
			sliderMaxLrServo1.setMinorTickSpacing(10);
			sliderMaxLrServo1.setMajorTickSpacing(50);
			sliderMaxLrServo1.setPaintTicks(true);
			sliderMaxLrServo1.setPaintLabels(true);
			sliderMaxLrServo1.setValue(200);
			sliderMaxLrServo1.setMaximum(400);
			sliderMaxLrServo1.setMinimum(200);
			sliderMaxLrServo1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Max LR",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderMaxLrServo1.addChangeListener(e -> {
				sliderUpdateTitle(getSliderMaxLrServo1(), "Max LR - ");
				getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
						(short) getSliderMaxLrServo1().getValue());
			});
			sliderUpdateTitle(sliderMaxLrServo1, "Max LR - ");
			getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
					(short) getSliderMaxLrServo1().getValue());
		}
		return sliderMaxLrServo1;
	}

	private JSlider getSliderPwmMax() {
		if (sliderPwmMax == null) {
			sliderPwmMax = new JSlider();
			sliderPwmMax.setMinorTickSpacing(20);
			sliderPwmMax.setMajorTickSpacing(100);
			sliderPwmMax.setPaintTicks(true);
			sliderPwmMax.setPaintLabels(true);
			sliderPwmMax.setMaximum(500);
			sliderPwmMax.setValue(200);
			sliderPwmMax.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "PWM max",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderPwmMax.addChangeListener(e -> {
				sliderUpdateTitle(getSliderPwmMax(), "PWM max - ");
				getTfc().setPWMMax((short) getSliderPwmMax().getValue());
			});
			sliderUpdateTitle(sliderPwmMax, "PWM max - ");
			getTfc().setPWMMax((short) getSliderPwmMax().getValue());
		}
		return sliderPwmMax;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "LEDs",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanel = new GridBagLayout();
			gblPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
			gblPanel.rowHeights = new int[] { 0, 0 };
			gblPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gblPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			panel.setLayout(gblPanel);
			GridBagConstraints gbcTglbtnLed1 = new GridBagConstraints();
			gbcTglbtnLed1.insets = new Insets(0, 0, 0, 5);
			gbcTglbtnLed1.gridx = 0;
			gbcTglbtnLed1.gridy = 0;
			panel.add(getTglbtnLed1(), gbcTglbtnLed1);
			GridBagConstraints gbcTglbtnLed2 = new GridBagConstraints();
			gbcTglbtnLed2.insets = new Insets(0, 0, 0, 5);
			gbcTglbtnLed2.gridx = 1;
			gbcTglbtnLed2.gridy = 0;
			panel.add(getTglbtnLed2(), gbcTglbtnLed2);
			GridBagConstraints gbcTglbtnLed3 = new GridBagConstraints();
			gbcTglbtnLed3.insets = new Insets(0, 0, 0, 5);
			gbcTglbtnLed3.gridx = 2;
			gbcTglbtnLed3.gridy = 0;
			panel.add(getTglbtnLed3(), gbcTglbtnLed3);
			GridBagConstraints gbcTglbtnLed4 = new GridBagConstraints();
			gbcTglbtnLed4.gridx = 3;
			gbcTglbtnLed4.gridy = 0;
			panel.add(getTglbtnLed4(), gbcTglbtnLed4);
		}
		return panel;
	}

	private JToggleButton getTglbtnLed1() {
		if (tglbtnLed1 == null) {
			tglbtnLed1 = new JToggleButton("LED1");
			tglbtnLed1.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getTfc().setLED(0, getTglbtnLed1().getModel().isSelected());
				}
			});
		}
		return tglbtnLed1;
	}

	private JToggleButton getTglbtnLed2() {
		if (tglbtnLed2 == null) {
			tglbtnLed2 = new JToggleButton("LED2");
			tglbtnLed2.addChangeListener(e -> {
				getTfc().setLED(1, getTglbtnLed2().getModel().isSelected());
			});
		}
		return tglbtnLed2;
	}

	private JToggleButton getTglbtnLed3() {
		if (tglbtnLed3 == null) {
			tglbtnLed3 = new JToggleButton("LED3");
			tglbtnLed3.addActionListener(e -> {
				getTfc().setLED(2, getTglbtnLed3().getModel().isSelected());

			});
		}
		return tglbtnLed3;
	}

	private JToggleButton getTglbtnLed4() {
		if (tglbtnLed4 == null) {
			tglbtnLed4 = new JToggleButton("LED4");
			tglbtnLed4.addActionListener(e -> {
				getTfc().setLED(3, getTglbtnLed4().getModel().isSelected());
			});
		}
		return tglbtnLed4;
	}

	private JPanel getPanel1_2() {
		if (panelMotorsOnOff == null) {
			panelMotorsOnOff = new JPanel();
			panelMotorsOnOff.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"Motors on/off", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelMotorsOnOff = new GridBagLayout();
			panelMotorsOnOff.setLayout(gblPanelMotorsOnOff);
			GridBagConstraints gbcTglbtnPwm = new GridBagConstraints();
			gbcTglbtnPwm.anchor = GridBagConstraints.NORTHWEST;
			gbcTglbtnPwm.insets = new Insets(0, 0, 0, 5);
			gbcTglbtnPwm.gridx = 0;
			gbcTglbtnPwm.gridy = 0;
			panelMotorsOnOff.add(getTglbtnPwm(), gbcTglbtnPwm);
			GridBagConstraints gbcTglbtnServo = new GridBagConstraints();
			gbcTglbtnServo.anchor = GridBagConstraints.NORTHWEST;
			gbcTglbtnServo.gridx = 1;
			gbcTglbtnServo.gridy = 0;
			panelMotorsOnOff.add(getTglbtnServo(), gbcTglbtnServo);
		}
		return panelMotorsOnOff;
	}

	private JToggleButton getTglbtnPwm() {
		if (tglbtnPwm == null) {
			tglbtnPwm = new JToggleButton("PWM");
			tglbtnPwm.addChangeListener(
					e -> getTfc().MotorPWMOnOff(getTglbtnPwm().getModel().isSelected() ? (byte) 1 : ((byte) 0)));
		}
		return tglbtnPwm;
	}

	private JToggleButton getTglbtnServo() {
		if (tglbtnServo == null) {
			tglbtnServo = new JToggleButton("Servo");
			tglbtnServo.addChangeListener(
					e -> getTfc().ServoOnOff(getTglbtnServo().getModel().isSelected() ? (byte) 1 : ((byte) 0)));
		}
		return tglbtnServo;
	}

	private JPanel getPanelStearingTitle() {
		if (panelStearingTitle == null) {
			panelStearingTitle = new JPanel();
			panelStearingTitle.setPreferredSize(new Dimension(300, 300));
			panelStearingTitle.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"Stearing", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelStearingTitle.setLayout(new BorderLayout(0, 0));
			panelStearingTitle.add(getPanelStearing());
		}
		return panelStearingTitle;
	}

	private JPanel getPanelStearing() {
		if (panelStearing == null) {
			panelStearing = new JPanel();
			panelStearing.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					getTfc().setServo_i(0, (short) 0);
					getTfc().setMotorPWM_i((short) 0, (short) 0);
				}
			});
			panelStearing.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					double width = getPanelStearing().getWidth();
					double height = getPanelStearing().getHeight();
					double stearing = (e.getX() - width / 2) / (width / 2);
					double speed = (height / 2 - e.getY()) / (height / 2);
					speed = Math.min(speed, 1);
					speed = Math.max(speed, -1);
					stearing = Math.min(stearing, 1);
					stearing = Math.max(stearing, -1);
					int servoPos = (int) (stearing * Tfc.SERVO_MINMAX);
					int pwm = (int) (speed * Tfc.PWM_MINMAX);
					getTfc().setServo_i(0, (short) servoPos);
					getTfc().setMotorPWM_i((short) pwm, (short) pwm);
				}
			});
			panelStearing.setPreferredSize(new Dimension(300, 300));
			panelStearing.setBackground(Color.WHITE);
			panelStearing.setLayout(new GridLayout(1, 0, 0, 0));
		}
		return panelStearing;
	}

	private JPanel getPanel1_5() {
		if (panel1 == null) {
			panel1 = new JPanel();
			panel1.add(getPanel());
			panel1.add(getPanel1_2());
		}
		return panel1;
	}

	private JButton getBtnSetting() {
		if (btnSetting == null) {
			btnSetting = new JButton("Send Setting");
			btnSetting.addActionListener(e -> {
				getTfc().sendSetting();
			});
		}
		return btnSetting;
	}

	private JPanel getPanelConnect() {
		if (panelConnect == null) {
			panelConnect = new JPanel();
			panelConnect.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Connection",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelConnect = new GridBagLayout();
			gblPanelConnect.columnWidths = new int[] { 0, 0, 0 };
			gblPanelConnect.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			gblPanelConnect.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gblPanelConnect.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panelConnect.setLayout(gblPanelConnect);
			GridBagConstraints gbcRdbtnUsbWindows = new GridBagConstraints();
			gbcRdbtnUsbWindows.insets = new Insets(0, 0, 5, 5);
			gbcRdbtnUsbWindows.gridx = 0;
			gbcRdbtnUsbWindows.gridy = 0;
			panelConnect.add(getRdbtnUsbWindows(), gbcRdbtnUsbWindows);
			GridBagConstraints gbcRdbtnUsbLinux = new GridBagConstraints();
			gbcRdbtnUsbLinux.insets = new Insets(0, 0, 5, 0);
			gbcRdbtnUsbLinux.gridx = 1;
			gbcRdbtnUsbLinux.gridy = 0;
			panelConnect.add(getRdbtnUsbLinux(), gbcRdbtnUsbLinux);
			GridBagConstraints gbcRdbtnTcp = new GridBagConstraints();
			gbcRdbtnTcp.insets = new Insets(0, 0, 5, 5);
			gbcRdbtnTcp.gridx = 0;
			gbcRdbtnTcp.gridy = 1;
			panelConnect.add(getRdbtnTcp(), gbcRdbtnTcp);
			GridBagConstraints gbcLblComPort = new GridBagConstraints();
			gbcLblComPort.insets = new Insets(0, 0, 5, 5);
			gbcLblComPort.anchor = GridBagConstraints.EAST;
			gbcLblComPort.gridx = 0;
			gbcLblComPort.gridy = 2;
			panelConnect.add(getLblComPort(), gbcLblComPort);
			GridBagConstraints gbcTxtComPort = new GridBagConstraints();
			gbcTxtComPort.insets = new Insets(0, 0, 5, 0);
			gbcTxtComPort.fill = GridBagConstraints.HORIZONTAL;
			gbcTxtComPort.gridx = 1;
			gbcTxtComPort.gridy = 2;
			panelConnect.add(getTxtComPort(), gbcTxtComPort);
			GridBagConstraints gbcBtnAutodetect = new GridBagConstraints();
			gbcBtnAutodetect.insets = new Insets(0, 0, 5, 5);
			gbcBtnAutodetect.gridx = 0;
			gbcBtnAutodetect.gridy = 3;
			panelConnect.add(getBtnAutodetect(), gbcBtnAutodetect);
			GridBagConstraints gbcBtnConnect = new GridBagConstraints();
			gbcBtnConnect.insets = new Insets(0, 0, 5, 0);
			gbcBtnConnect.fill = GridBagConstraints.HORIZONTAL;
			gbcBtnConnect.gridx = 1;
			gbcBtnConnect.gridy = 3;
			panelConnect.add(getBtnConnect(), gbcBtnConnect);
			GridBagConstraints gbcBtnDisconnect = new GridBagConstraints();
			gbcBtnDisconnect.gridx = 1;
			gbcBtnDisconnect.gridy = 4;
			panelConnect.add(getBtnDisconnect(), gbcBtnDisconnect);
		}
		return panelConnect;
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
				getPanelCockpit().setBackground(getBackground());
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

	private JPanel getPanelRecording() {
		if (panelRecording == null) {
			panelRecording = new JPanel();
			panelRecording.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Recording",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanelRecording = new GridBagLayout();
			gblPanelRecording.columnWidths = new int[] { 0, 0, 0 };
			gblPanelRecording.rowHeights = new int[] { 0, 0, 0, 0 };
			gblPanelRecording.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gblPanelRecording.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panelRecording.setLayout(gblPanelRecording);
			GridBagConstraints gbcLblRecordFile = new GridBagConstraints();
			gbcLblRecordFile.insets = new Insets(0, 0, 5, 5);
			gbcLblRecordFile.anchor = GridBagConstraints.EAST;
			gbcLblRecordFile.gridx = 0;
			gbcLblRecordFile.gridy = 0;
			panelRecording.add(getLblRecordFile(), gbcLblRecordFile);
			GridBagConstraints gbcTxtFile = new GridBagConstraints();
			gbcTxtFile.insets = new Insets(0, 0, 5, 0);
			gbcTxtFile.fill = GridBagConstraints.HORIZONTAL;
			gbcTxtFile.gridx = 1;
			gbcTxtFile.gridy = 0;
			panelRecording.add(getTxtFile(), gbcTxtFile);
			GridBagConstraints gbcBtnSelectFile = new GridBagConstraints();
			gbcBtnSelectFile.insets = new Insets(0, 0, 5, 5);
			gbcBtnSelectFile.gridx = 0;
			gbcBtnSelectFile.gridy = 1;
			panelRecording.add(getBtnSelectFile(), gbcBtnSelectFile);
			GridBagConstraints gbcTglbtnRecord = new GridBagConstraints();
			gbcTglbtnRecord.insets = new Insets(0, 0, 5, 0);
			gbcTglbtnRecord.gridx = 1;
			gbcTglbtnRecord.gridy = 1;
			panelRecording.add(getTglbtnRecord(), gbcTglbtnRecord);
		}
		return panelRecording;
	}

	private JTextField getTxtFile() {
		if (txtFile == null) {
			txtFile = new JTextField();
			txtFile.setText("record.bin");
			txtFile.setColumns(10);
		}
		return txtFile;
	}

	private JLabel getLblRecordFile() {
		if (lblRecordFile == null) {
			lblRecordFile = new JLabel("Record file:");
		}
		return lblRecordFile;
	}

	private JButton getBtnSelectFile() {
		if (btnSelectFile == null) {
			btnSelectFile = new JButton("Select file");
			btnSelectFile.addActionListener(e -> {
				JFileChooser chooser = new JFileChooser(getTxtFile().getText());
				int result = chooser.showSaveDialog(getRootPane());
				if (result == JFileChooser.APPROVE_OPTION) {
					getTxtFile().setText(chooser.getSelectedFile().getAbsolutePath());
				}
			});
		}
		return btnSelectFile;
	}

	private JToggleButton getTglbtnRecord() {
		if (tglbtnRecord == null) {
			tglbtnRecord = new JToggleButton("Record");
			tglbtnRecord.addChangeListener(e -> {
				if (getTglbtnRecord().getModel().isSelected()) {
					try {
						getCommunicationWrapper().startRecording(new FileOutputStream(getTxtFile().getText()));
					} catch (FileNotFoundException e1) {
						logger.info("File open error", e1);
						JOptionPane.showMessageDialog(getRootPane(), "File not found.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					getCommunicationWrapper().stopRecording();
				}
			});
		}
		return tglbtnRecord;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			tabbedPane.addTab("Cockpit", null, getPanelCockpit(), null);
			tabbedPane.addTab("Monitor", null, getPanelMonitor(), null);
		}
		return tabbedPane;
	}

	private JPanel getPanelCockpit() {
		if (panelCockpit == null) {
			panelCockpit = new JPanel();
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.rowWeights = new double[] { 0.0, 0.0 };
			gridBagLayout.columnWeights = new double[] { 1.0 };
			panelCockpit.setLayout(gridBagLayout);
			GridBagConstraints gbcPanelSetting = new GridBagConstraints();
			gbcPanelSetting.insets = new Insets(5, 5, 5, 5);
			gbcPanelSetting.fill = GridBagConstraints.BOTH;
			gbcPanelSetting.gridx = 0;
			gbcPanelSetting.gridy = 0;
			panelCockpit.add(getPanelSetting(), gbcPanelSetting);
			GridBagConstraints gbcPanelData = new GridBagConstraints();
			gbcPanelData.gridheight = 2;
			gbcPanelData.insets = new Insets(5, 0, 5, 0);
			gbcPanelData.fill = GridBagConstraints.BOTH;
			gbcPanelData.gridx = 1;
			gbcPanelData.gridy = 0;
			panelCockpit.add(getPanelData(), gbcPanelData);
			GridBagConstraints gbcPanelControl = new GridBagConstraints();
			gbcPanelControl.weightx = 1.0;
			gbcPanelControl.weighty = 1.0;
			gbcPanelControl.insets = new Insets(0, 5, 5, 5);
			gbcPanelControl.fill = GridBagConstraints.BOTH;
			gbcPanelControl.gridx = 0;
			gbcPanelControl.gridy = 1;
			panelCockpit.add(getPanelControl(), gbcPanelControl);

		}
		return panelCockpit;
	}

	private JPanel getPanelMonitor() {
		if (panelMonitor == null) {
			panelMonitor = new JPanel();
			panelMonitor.setLayout(new BorderLayout(0, 0));
			panelMonitor.add(getAnmImgMonitor(), BorderLayout.CENTER);
			panelMonitor.add(getPanel2(), BorderLayout.EAST);
			panelMonitor.add(getScrollBar(), BorderLayout.WEST);
			panelMonitor.add(getPanel3(), BorderLayout.NORTH);
		}
		return panelMonitor;
	}

	private AnimImage getAnmImgMonitor() {
		if (anmImgMonitor == null) {
			anmImgMonitor = new AnimImage(getImageBuffer2());
			anmImgMonitor.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					getImageBuffer2().selectRow(e.getY());
				}
			});
			anmImgMonitor.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					getImageBuffer2().selectRow(e.getY());
				}
			});
		}
		return anmImgMonitor;
	}

	private ImageBuffer2 getImageBuffer2() {
		if (imageBuffer2 == null) {
			imageBuffer2 = new ImageBuffer2((Integer) getSpinnerPictureHeight().getValue(), getDrivingRecord());
			int columnCoun = (Integer) getSpinnerColumnCount().getValue();
			int[] columnWidths = new int[columnCoun];
			int[] columnZooms = new int[columnCoun];
			for (int i = 0; i < columnCoun; i++) {
				columnWidths[i] = (Integer) getSpinnerColumnWidth(i).getValue();
				columnZooms[i] = (Integer) getSpinnerColumnZoom(i).getValue();
			}
			int[] data2Column = new int[6];
			for (int i = 0; i < data2Column.length; i++) {
				data2Column[i] = (Integer) getSpinnerDataColumn(i).getValue();
			}
			imageBuffer2.setColumns(columnWidths, columnZooms);
			imageBuffer2.setDrawToColumn(data2Column);
			imageBuffer2.setHeightZoom((Integer) getSpnHeightZoom().getValue());
		}
		return imageBuffer2;
	}

	/**
	 * @wbp.nonvisual location=226,717
	 */
	private UDPServer getUdpServer() {
		if (udpServer == null) {
			int port = Integer.parseInt(getTxtUdpPort().getText());
			udpServer = new UDPServer(port, getDrivingRecord());
		}
		return udpServer;
	}

	private JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			GridBagLayout gblPanel2 = new GridBagLayout();
			gblPanel2.columnWidths = new int[] { 117, 0 };
			gblPanel2.rowHeights = new int[] { 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			gblPanel2.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gblPanel2.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
					0.0, 0.0, Double.MIN_VALUE };
			panel2.setLayout(gblPanel2);
			GridBagConstraints gbcTxtUdpPort = new GridBagConstraints();
			gbcTxtUdpPort.insets = new Insets(0, 0, 5, 0);
			gbcTxtUdpPort.fill = GridBagConstraints.HORIZONTAL;
			gbcTxtUdpPort.gridx = 0;
			gbcTxtUdpPort.gridy = 0;
			panel2.add(getTxtUdpPort(), gbcTxtUdpPort);
			GridBagConstraints gbcBtnStart = new GridBagConstraints();
			gbcBtnStart.insets = new Insets(0, 0, 5, 0);
			gbcBtnStart.anchor = GridBagConstraints.NORTHWEST;
			gbcBtnStart.gridx = 0;
			gbcBtnStart.gridy = 1;
			panel2.add(getBtnStart(), gbcBtnStart);
			GridBagConstraints gbcBtnStop = new GridBagConstraints();
			gbcBtnStop.insets = new Insets(0, 0, 5, 0);
			gbcBtnStop.anchor = GridBagConstraints.NORTHWEST;
			gbcBtnStop.gridx = 0;
			gbcBtnStop.gridy = 2;
			panel2.add(getBtnStop(), gbcBtnStop);
			GridBagConstraints gbcChckbxUseFakeData = new GridBagConstraints();
			gbcChckbxUseFakeData.insets = new Insets(0, 0, 5, 0);
			gbcChckbxUseFakeData.gridx = 0;
			gbcChckbxUseFakeData.gridy = 3;
			panel2.add(getChckbxUseFakeData(), gbcChckbxUseFakeData);
			GridBagConstraints gbcBtnXmlExport = new GridBagConstraints();
			gbcBtnXmlExport.insets = new Insets(0, 0, 5, 0);
			gbcBtnXmlExport.gridx = 0;
			gbcBtnXmlExport.gridy = 5;
			panel2.add(getBtnXmlExport(), gbcBtnXmlExport);
			GridBagConstraints gbcBtnXmlImport = new GridBagConstraints();
			gbcBtnXmlImport.insets = new Insets(0, 0, 5, 0);
			gbcBtnXmlImport.gridx = 0;
			gbcBtnXmlImport.gridy = 7;
			panel2.add(getBtnXmlImport(), gbcBtnXmlImport);
			GridBagConstraints gbcBtnClear = new GridBagConstraints();
			gbcBtnClear.insets = new Insets(0, 0, 5, 0);
			gbcBtnClear.gridx = 0;
			gbcBtnClear.gridy = 9;
			panel2.add(getBtnClear(), gbcBtnClear);
			GridBagConstraints gbcBtnBinExport = new GridBagConstraints();
			gbcBtnBinExport.insets = new Insets(0, 0, 5, 0);
			gbcBtnBinExport.gridx = 0;
			gbcBtnBinExport.gridy = 10;
			panel2.add(getBtnBinExport(), gbcBtnBinExport);
			GridBagConstraints gbcBtnBinImport = new GridBagConstraints();
			gbcBtnBinImport.insets = new Insets(0, 0, 5, 0);
			gbcBtnBinImport.gridx = 0;
			gbcBtnBinImport.gridy = 12;
			panel2.add(getBtnBinImport(), gbcBtnBinImport);
			GridBagConstraints gbcBtnHistogram = new GridBagConstraints();
			gbcBtnHistogram.insets = new Insets(0, 0, 5, 0);
			gbcBtnHistogram.gridx = 0;
			gbcBtnHistogram.gridy = 14;
			panel2.add(getBtnHistogram(), gbcBtnHistogram);
			GridBagConstraints gbcBtnGradient = new GridBagConstraints();
			gbcBtnGradient.gridx = 0;
			gbcBtnGradient.gridy = 15;
			panel2.add(getBtnGradient(), gbcBtnGradient);
		}
		return panel2;
	}

	private JButton getBtnStop() {
		if (btnStop == null) {
			btnStop = new JButton("Stop");
			btnStop.addActionListener(e -> getUdpServer().stop());
		}
		return btnStop;
	}

	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = new JButton("Start");
			btnStart.addActionListener(e -> getUdpServer().readData());
		}
		return btnStart;
	}

	private JScrollBar getScrollBar() {
		if (scrollBar == null) {
			scrollBar = new JScrollBar();
			scrollBar.addAdjustmentListener(e -> {
				if (e.getValue() != 0) {
					getImageBuffer2().setScrollPosition(getDrivingRecord().getData().size() - e.getValue());
				}
			});
		}
		return scrollBar;
	}

	private JTextField getTxtUdpPort() {
		if (txtUdpPort == null) {
			txtUdpPort = new JTextField();
			txtUdpPort.setText("5556");
			txtUdpPort.setColumns(10);
		}
		return txtUdpPort;
	}

	/**
	 * @wbp.nonvisual location=284,717
	 */
	private DrivingRecord getDrivingRecord() {
		if (drivingRecord == null) {
			drivingRecord = new DrivingRecord();
			drivingRecord.addPropertyDataListener(evt -> {
				SwingUtilities.invokeLater(() -> {
					recalculateScrollbar();
				});
			});
		}
		return drivingRecord;
	}

	private void recalculateScrollbar() {
		getScrollBar().getModel().setMaximum(drivingRecord.getData().size() + getImageBuffer2().getHeightZoom());
		getScrollBar().getModel().setMinimum(0);
		getScrollBar().getModel().setExtent(getImageBuffer2().getHeight() / getImageBuffer2().getHeightZoom());

	}

	private JButton getBtnXmlExport() {
		if (btnXmlExport == null) {
			btnXmlExport = new JButton("Xml Export");
			btnXmlExport.addActionListener(l -> {
				FileUtils.selectFileAndWrite("", true, new FileFilter() {

					@Override
					public String getDescription() {
						return "XML file";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".xml");
					}
				}, getBtnXmlExport(), f -> {
				}, f -> {
					if (!f.getName().endsWith(".xml")) {
						return new File(f.getAbsolutePath() + ".xml");
					}
					return f;
				}, file -> {
					try {
						JAXBContext jaxbContext = JAXBContext.newInstance(DrivingRecord.class);
						Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
						// output pretty printed
						jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
						jaxbMarshaller.marshal(getDrivingRecord(), file);
					} catch (JAXBException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				});

			});
		}
		return btnXmlExport;
	}

	private JButton getBtnXmlImport() {
		if (btnXmlImport == null) {
			btnXmlImport = new JButton("Xml Import");
			btnXmlImport.addActionListener(l -> {
				JFileChooser fileChooser = new JFileChooser("");
				fileChooser.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return "XML File";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".xml");
					}
				});
				int dialogResult = fileChooser.showOpenDialog(getBtnXmlImport());
				if (JFileChooser.APPROVE_OPTION == dialogResult) {
					try {
						JAXBContext jaxbContext = JAXBContext.newInstance(DrivingRecord.class);
						Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
						DrivingRecord record = (DrivingRecord) jaxbUnmarshaller
								.unmarshal(fileChooser.getSelectedFile());
						getDrivingRecord().setData(record.getData());
					} catch (JAXBException e) {
						e.printStackTrace();
					}

				}
			});
		}
		return btnXmlImport;
	}

	private JButton getBtnBinExport() {
		if (btnBinExport == null) {
			btnBinExport = new JButton("BIN Export");
			btnBinExport.addActionListener(l -> {
				FileUtils.selectFileAndWrite("", true, new FileFilter() {

					@Override
					public String getDescription() {
						return "BIN file";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".bin");
					}
				}, getBtnXmlExport(), f -> {
				}, f -> {
					if (!f.getName().endsWith(".bin")) {
						return new File(f.getAbsolutePath() + ".bin");
					}
					return f;
				}, file -> {
					try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
						byte[] buf = new byte[WifiMonitorData.STRUCT_LENGTH];
						for (WifiMonitorData data : getDrivingRecord().getData()) {
							if (!data.isMissing()) {
								fileOutputStream.write(data.writeToArray(buf));
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				});

			});
		}
		return btnBinExport;
	}

	private JButton getBtnBinImport() {
		if (btnBinImport == null) {
			btnBinImport = new JButton("BIN Import");
			btnBinImport.addActionListener(l -> {
				JFileChooser fileChooser = new JFileChooser("");
				fileChooser.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return "BIN File";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".bin");
					}
				});
				int dialogResult = fileChooser.showOpenDialog(getBtnXmlImport());
				if (JFileChooser.APPROVE_OPTION == dialogResult) {
					try (FileInputStream fileInputStream = new FileInputStream(fileChooser.getSelectedFile())) {
						List<WifiMonitorData> newData = new ArrayList<>();
						getDrivingRecord().clearData();
						byte[] buf = new byte[WifiMonitorData.STRUCT_LENGTH];
						while (fileInputStream.read(buf) != -1) {
							getDrivingRecord().addData(new WifiMonitorData(buf));
						}
						// getDrivingRecord().setData(newData);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			});
		}
		return btnBinImport;
	}

	private JPanel getPanel3() {
		if (panel3 == null) {
			panel3 = new JPanel();
			panel3.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
					"Image config", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagLayout gblPanel3 = new GridBagLayout();
			gblPanel3.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
			gblPanel3.rowHeights = new int[] { 0, 0, 0, 0, 0 };
			panel3.setLayout(gblPanel3);
			GridBagConstraints gbcLblColumnCount = new GridBagConstraints();
			gbcLblColumnCount.insets = new Insets(0, 0, 5, 5);
			gbcLblColumnCount.gridx = 2;
			gbcLblColumnCount.gridy = 0;
			panel3.add(getLblColumnCount(), gbcLblColumnCount);
			GridBagConstraints gbcLblColumnIndex = new GridBagConstraints();
			gbcLblColumnIndex.insets = new Insets(0, 0, 5, 5);
			gbcLblColumnIndex.gridx = 3;
			gbcLblColumnIndex.gridy = 0;
			panel3.add(getLblColumnIndex(), gbcLblColumnIndex);
			for (int i = 0; i < 6; i++) {
				GridBagConstraints gbcLabel1 = new GridBagConstraints();
				gbcLabel1.insets = new Insets(0, 0, 5, 5);
				gbcLabel1.gridx = 4 + i;
				gbcLabel1.gridy = 0;
				panel3.add(getLabelIndex(i), gbcLabel1);
				GridBagConstraints gbcSpinnerColumnWidth = new GridBagConstraints();
				gbcSpinnerColumnWidth.insets = new Insets(0, 0, 5, 5);
				gbcSpinnerColumnWidth.gridx = 4 + i;
				gbcSpinnerColumnWidth.gridy = 1;
				panel3.add(getSpinnerColumnWidth(i), gbcSpinnerColumnWidth);
				GridBagConstraints gbcSpinnerColumnZoom = new GridBagConstraints();
				gbcSpinnerColumnZoom.insets = new Insets(0, 0, 5, 5);
				gbcSpinnerColumnZoom.gridx = 4 + i;
				gbcSpinnerColumnZoom.gridy = 2;
				panel3.add(getSpinnerColumnZoom(i), gbcSpinnerColumnZoom);
				GridBagConstraints gbcSpinnerDataColumn = new GridBagConstraints();
				gbcSpinnerDataColumn.insets = new Insets(0, 0, 5, 5);
				gbcSpinnerDataColumn.gridx = 4 + i;
				gbcSpinnerDataColumn.gridy = 3;
				panel3.add(getSpinnerDataColumn(i), gbcSpinnerDataColumn);
			}
			GridBagConstraints gbcSlider_1 = new GridBagConstraints();
			gbcSlider_1.gridheight = 2;
			gbcSlider_1.insets = new Insets(0, 0, 5, 5);
			gbcSlider_1.gridx = 0;
			gbcSlider_1.gridy = 1;
			gbcSlider_1.gridwidth = 2;
			panel3.add(getSliderBrightness2(), gbcSlider_1);
			GridBagConstraints gbcSpinnerColumnCount = new GridBagConstraints();
			gbcSpinnerColumnCount.insets = new Insets(0, 0, 5, 5);
			gbcSpinnerColumnCount.gridx = 2;
			gbcSpinnerColumnCount.gridy = 1;
			panel3.add(getSpinnerColumnCount(), gbcSpinnerColumnCount);
			GridBagConstraints gbcLblColumnWidth = new GridBagConstraints();
			gbcLblColumnWidth.insets = new Insets(0, 0, 5, 5);
			gbcLblColumnWidth.gridx = 3;
			gbcLblColumnWidth.gridy = 1;
			panel3.add(getLblColumnWidth(), gbcLblColumnWidth);
			GridBagConstraints gbcLblPictureHeight = new GridBagConstraints();
			gbcLblPictureHeight.insets = new Insets(0, 0, 5, 5);
			gbcLblPictureHeight.gridx = 2;
			gbcLblPictureHeight.gridy = 2;
			panel3.add(getLblPictureHeight(), gbcLblPictureHeight);
			GridBagConstraints gbcLblColumnZoom = new GridBagConstraints();
			gbcLblColumnZoom.insets = new Insets(0, 0, 5, 5);
			gbcLblColumnZoom.gridx = 3;
			gbcLblColumnZoom.gridy = 2;
			panel3.add(getLblColumnZoom(), gbcLblColumnZoom);
			GridBagConstraints gbcLblHeightZoom = new GridBagConstraints();
			gbcLblHeightZoom.insets = new Insets(0, 0, 5, 5);
			gbcLblHeightZoom.gridx = 0;
			gbcLblHeightZoom.gridy = 3;
			panel3.add(getLblHeightZoom(), gbcLblHeightZoom);
			GridBagConstraints gbcSpnHeightZoom = new GridBagConstraints();
			gbcSpnHeightZoom.insets = new Insets(0, 0, 5, 5);
			gbcSpnHeightZoom.gridx = 1;
			gbcSpnHeightZoom.gridy = 3;
			panel3.add(getSpnHeightZoom(), gbcSpnHeightZoom);
			GridBagConstraints gbcSpinnerPictureHeight = new GridBagConstraints();
			gbcSpinnerPictureHeight.insets = new Insets(0, 0, 5, 5);
			gbcSpinnerPictureHeight.gridx = 2;
			gbcSpinnerPictureHeight.gridy = 3;
			panel3.add(getSpinnerPictureHeight(), gbcSpinnerPictureHeight);
			GridBagConstraints gbcLblDataToColumn = new GridBagConstraints();
			gbcLblDataToColumn.insets = new Insets(0, 0, 5, 5);
			gbcLblDataToColumn.gridx = 3;
			gbcLblDataToColumn.gridy = 3;
			panel3.add(getLblDataToColumn(), gbcLblDataToColumn);
			GridBagConstraints gbcBtnRecreateImage = new GridBagConstraints();
			gbcBtnRecreateImage.gridx = 11;
			gbcBtnRecreateImage.gridy = 4;
			panel3.add(getBtnRecreateImage(), gbcBtnRecreateImage);
		}
		return panel3;
	}

	private JSlider getSliderBrightness2() {
		if (sliderBrightness2 == null) {
			sliderBrightness2 = new JSlider();
			sliderBrightness2
					.addChangeListener(e -> getImageBuffer2().setBrightness(getSliderBrightness2().getValue() / 100.0));
			sliderBrightness2.setPaintLabels(true);
			sliderBrightness2.setBorder(
					new TitledBorder(null, "Brightness", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sliderBrightness2.setMinorTickSpacing(20);
			sliderBrightness2.setValue(100);
			sliderBrightness2.setMaximum(800);
			sliderBrightness2.setMinimum(100);
			sliderBrightness2.setMajorTickSpacing(100);
			sliderBrightness2.setPaintTicks(true);
			sliderBrightness2.setToolTipText("Brightness");
			Dictionary<Integer, JComponent> labels = new Hashtable<>();
			labels.put(100, new JLabel("1"));
			labels.put(200, new JLabel("2"));
			labels.put(300, new JLabel("3"));
			labels.put(400, new JLabel("4"));
			labels.put(500, new JLabel("5"));
			labels.put(600, new JLabel("6"));
			labels.put(700, new JLabel("7"));
			labels.put(800, new JLabel("8"));
			sliderBrightness2.setLabelTable(labels);
		}
		return sliderBrightness2;
	}

	private JLabel getLblColumnCount() {
		if (lblColumnCount == null) {
			lblColumnCount = new JLabel("Column count");
		}
		return lblColumnCount;
	}

	private JSpinner getSpinnerColumnCount() {
		if (spinnerColumnCount == null) {
			spinnerColumnCount = new JSpinner();
			spinnerColumnCount.setModel(new SpinnerNumberModel(6, 1, 6, 1));
		}
		return spinnerColumnCount;
	}

	private JLabel getLabelIndex(int index) {
		if (labelIndex[index] == null) {
			labelIndex[index] = new JLabel(Integer.toString(index));
		}
		return labelIndex[index];
	}

	private JLabel getLblColumnIndex() {
		if (lblColumnIndex == null) {
			lblColumnIndex = new JLabel("Column index:");
		}
		return lblColumnIndex;
	}

	private JLabel getLblColumnWidth() {
		if (lblColumnWidth == null) {
			lblColumnWidth = new JLabel("Column width");
		}
		return lblColumnWidth;
	}

	private JLabel getLblColumnZoom() {
		if (lblColumnZoom == null) {
			lblColumnZoom = new JLabel("Column zoom");
		}
		return lblColumnZoom;
	}

	private JSpinner getSpinnerColumnWidth(int index) {
		if (spinnerColumnWidth[index] == null) {
			spinnerColumnWidth[index] = new JSpinner();
			if (index == 0) {
				spinnerColumnWidth[index].setModel(new SpinnerNumberModel(WifiMonitorData.NUM_LINE_SCAN,
						WifiMonitorData.NUM_LINE_SCAN, WifiMonitorData.NUM_LINE_SCAN, 1));
				spinnerColumnWidth[index].setEnabled(false);
			} else {
				spinnerColumnWidth[index].setModel(new SpinnerNumberModel(49, 1, 200, 10));
			}
		}
		return spinnerColumnWidth[index];
	}

	private JSpinner getSpinnerColumnZoom(int index) {
		if (spinnerColumnZoom[index] == null) {
			spinnerColumnZoom[index] = new JSpinner();
			spinnerColumnZoom[index].setModel(new SpinnerNumberModel(2, 1, 10, 1));

		}
		return spinnerColumnZoom[index];
	}

	private JLabel getLblPictureHeight() {
		if (lblPictureHeight == null) {
			lblPictureHeight = new JLabel("Picture height");
		}
		return lblPictureHeight;
	}

	private JSpinner getSpinnerPictureHeight() {
		if (spinnerPictureHeight == null) {
			spinnerPictureHeight = new JSpinner();
			spinnerPictureHeight.setModel(new SpinnerNumberModel(500, 10, 900, 25));
		}
		return spinnerPictureHeight;
	}

	private JCheckBox getChckbxUseFakeData() {
		if (chckbxUseFakeData == null) {
			chckbxUseFakeData = new JCheckBox("Use fake data");
			chckbxUseFakeData.setSelected(getUdpServer().isFakeMode());
			chckbxUseFakeData.addActionListener(evt -> {
				getUdpServer().setFakeMode(getChckbxUseFakeData().isSelected());
			});
		}
		return chckbxUseFakeData;
	}

	private JButton getBtnRecreateImage() {
		if (btnRecreateImage == null) {
			btnRecreateImage = new JButton("Recreate Image");
			btnRecreateImage.addActionListener(evt -> {
				AnimImage animImage = getAnmImgMonitor();
				getPanelMonitor().remove(animImage);
				animImage.stopAnim();
				getImageBuffer2().unregisterUpdater();
				imageBuffer2 = null;
				anmImgMonitor = null;
				getPanelMonitor().add(getAnmImgMonitor(), BorderLayout.CENTER);
				getPanelMonitor().revalidate();
				getAnmImgMonitor().setVisible(false);
				getAnmImgMonitor().setVisible(true);
				getAnmImgMonitor().startAnim();
				getImageBuffer2().setChanged();
				recalculateScrollbar();
			});
		}
		return btnRecreateImage;
	}

	private JLabel getLblDataToColumn() {
		if (lblDataToColumn == null) {
			lblDataToColumn = new JLabel("Data to column");
		}
		return lblDataToColumn;
	}

	private JSpinner getSpinnerDataColumn(int index) {
		if (spinnerDataColumn[index] == null) {
			spinnerDataColumn[index] = new JSpinner();
			spinnerDataColumn[index].setModel(new SpinnerNumberModel(index, 0, 5, 1));
		}
		return spinnerDataColumn[index];
	}

	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton("Clear");
			btnClear.addActionListener(evt -> getDrivingRecord().clearData());
		}
		return btnClear;
	}

	private JButton getBtnHistogram() {
		if (btnHistogram == null) {
			btnHistogram = new JButton("Histogram");
			btnHistogram.addActionListener(e -> {
				Integer[] histogram = new Integer[11];
				for (int i = 0; i < histogram.length; i++) {
					histogram[i] = 0;
				}
				for (WifiMonitorData data : getDrivingRecord().getData()) {
					for (int color : data.getImage()) {
						histogram[color / (255 / (histogram.length - 1))]++;
					}
				}
				JOptionPane.showMessageDialog(Cockpit.this, Arrays.asList(histogram).stream()
						.map(v -> String.format("%3d", v)).collect(Collectors.joining(", ")));
			});
		}
		return btnHistogram;
	}

	private JButton getBtnGradient() {
		if (btnGradient == null) {
			btnGradient = new JButton("Gradient");
			btnGradient.addActionListener(e -> {
				for (WifiMonitorData data : getDrivingRecord().getData()) {
					int[] rGradient = new int[data.getImage().length];
					int[] lGradient = new int[data.getImage().length];
					for (int i = 0; i < data.getImage().length - 1; i++) {
						rGradient[i] = data.getImage()[i] - data.getImage()[i + 1];
						lGradient[i + 1] = data.getImage()[i + 1] - data.getImage()[i];
					}
					System.out.println("  " + Arrays.stream(rGradient).mapToObj(v -> String.format("%4d", v))
							.collect(Collectors.joining(",")));
					System.out.println(Arrays.stream(lGradient).mapToObj(v -> String.format("%4d", v))
							.collect(Collectors.joining(",")));
					System.out.println("---------------------------");
				}
			});
		}
		return btnGradient;
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
		}
		return buttonGroup;
	}

	private void changeConectionVew() {
		getBtnAutodetect().setEnabled(getRdbtnUsbWindows().getModel().isSelected());
		if (getRdbtnUsbWindows().getModel().isSelected()) {
			getLblComPort().setText("Comm port (comm29)");
			getTxtComPort().setText("com29");
		} else if (getRdbtnUsbLinux().getModel().isSelected()) {
			getLblComPort().setText("Comm port /dev/tty");
			getTxtComPort().setText("ACM0");
		} else if (getRdbtnTcp().getModel().isSelected()) {
			getLblComPort().setText("Address (host:port)");
			getTxtComPort().setText("otfeia406a.vsb.cz:40460");
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

	private JSpinner getSpnHeightZoom() {
		if (spnHeightZoom == null) {
			spnHeightZoom = new JSpinner();
			spnHeightZoom.setModel(new SpinnerNumberModel(1, 1, 20, 1));
		}
		return spnHeightZoom;
	}

	private JLabel getLblHeightZoom() {
		if (lblHeightZoom == null) {
			lblHeightZoom = new JLabel("Height zoom");
		}
		return lblHeightZoom;
	}
}
