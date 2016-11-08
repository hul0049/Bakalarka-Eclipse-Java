package jezek.nxp.car;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.image.ImageProducer;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import javax.swing.border.BevelBorder;
import java.awt.FlowLayout;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortList;

import javax.swing.event.ChangeEvent;
import javax.swing.JToggleButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTextField;

public class Cockpit extends JFrame {
	
	
	private static final Logger logger = LogManager.getLogger(Cockpit.class);
	
	private AnimImage animImage;
	private Rs485Line rs485Line;
	private ImageBuffer imageBuffer;
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
	private JPanel panel_1;
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
		getContentPane().setBackground(Color.RED);

		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 996, 690);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbcPanelSetting = new GridBagConstraints();
		gbcPanelSetting.insets = new Insets(5, 5, 5, 5);
		gbcPanelSetting.fill = GridBagConstraints.BOTH;
		gbcPanelSetting.gridx = 0;
		gbcPanelSetting.gridy = 0;
		getContentPane().add(getPanelSetting(), gbcPanelSetting);
		GridBagConstraints gbcPanelData = new GridBagConstraints();
		gbcPanelData.gridheight = 2;
		gbcPanelData.insets = new Insets(5, 0, 5, 5);
		gbcPanelData.fill = GridBagConstraints.BOTH;
		gbcPanelData.gridx = 1;
		gbcPanelData.gridy = 0;
		getContentPane().add(getPanelData(), gbcPanelData);
		GridBagConstraints gbcPanelControl = new GridBagConstraints();
		gbcPanelControl.weightx = 1.0;
		gbcPanelControl.weighty = 1.0;
		gbcPanelControl.insets = new Insets(0, 5, 5, 5);
		gbcPanelControl.fill = GridBagConstraints.BOTH;
		gbcPanelControl.gridx = 0;
		gbcPanelControl.gridy = 1;
		getContentPane().add(getPanelControl(), gbcPanelControl);
	}

	private AnimImage getAnimImage() {
		if (animImage == null) {
			animImage = new AnimImage(128 * 2, 500, getImageBuffer());
		}
		return animImage;
	}

	/**
	 * @wbp.nonvisual location=151,739
	 */
	private Rs485Line getRs485Line() {
		if (rs485Line == null) {
			rs485Line = new Rs485Line("com30");
		}
		return rs485Line;
	}

	/**
	 * @wbp.nonvisual location=54,739
	 */
	private ImageBuffer getImageBuffer() {
		if (imageBuffer == null) {
			imageBuffer = new ImageBuffer(128, 500, 1);
			getRs485Line().getTfc().addImageBuffer(imageBuffer);
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
			GridBagConstraints gbcPanel_1 = new GridBagConstraints();
			gbcPanel_1.insets = new Insets(0, 0, 5, 5);
			gbcPanel_1.fill = GridBagConstraints.BOTH;
			gbcPanel_1.gridx = 0;
			gbcPanel_1.gridy = 0;
			panelControl.add(getPanel_1_5(), gbcPanel_1);
			GridBagConstraints gbcPanelStearingTitle = new GridBagConstraints();
			gbcPanelStearingTitle.gridwidth = 0;
			gbcPanelStearingTitle.weighty = 1.0;
			gbcPanelStearingTitle.weightx = 1.0;
			gbcPanelStearingTitle.fill = GridBagConstraints.BOTH;
			gbcPanelStearingTitle.gridx = 0;
			gbcPanelStearingTitle.gridy = 2;
			panelControl.add(getPanel_1_3(), gbcPanelStearingTitle);
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
			panelData.add(getPanel_1_1(), gbcPanelNumericData);
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

	private JPanel getPanel_1_1() {
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
			panelNumericData.add(getPanel_1(), gbcPanelAnalogText);
			Thread numericDataUpdater = new Thread(() -> updateAnalogValues());
			numericDataUpdater.start();
		}
		return panelNumericData;
	}

	private void updateAnalogValues() {
		while (!Thread.currentThread().isInterrupted()) {
			Tfc tfc = getRs485Line().getTfc();
			getTglbtnButton2().setSelected(tfc.getPushButton(1) != 0);
			getTglbtnButton1().setSelected(tfc.getPushButton(0) != 0);
			getTglbtnDipswitch1().setSelected((tfc.getDIPSwitch() & 1) != 0);
			getTglbtnDipswitch2().setSelected((tfc.getDIPSwitch() & 2) != 0);
			getTglbtnDipswitch3().setSelected((tfc.getDIPSwitch() & 4) != 0);
			getTglbtnDipswitch4().setSelected((tfc.getDIPSwitch() & 8) != 0);
			getLblPot1Value().setText(String.format("%d", tfc.ReadPot_i(0)));
			getLblPot2Value().setText(String.format("%d", tfc.ReadPot_i(1)));
			getLblFbAvalue().setText(String.format("%fA", tfc.ReadFB_f(0)));
			getLblFbBvalue().setText(String.format("%fA", tfc.ReadFB_f(1)));
			getLblBatteryValue().setText(String.format("%fV", tfc.ReadBatteryVoltage_f()));
//			((TitledBorder)getPanelCamerra().getBorder()).setTitle("Camerra Data FPS: " + getAnimImage().getFps());
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private JPanel getPanel_1() {
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
				getRs485Line().getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
						(short) getSliderMaxLrServo0().getValue());

			});
			sliderUpdateTitle(sliderServo0, "Center - ");
			getRs485Line().getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
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
				getRs485Line().getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
						(short) getSliderMaxLrServo1().getValue());
			});
			sliderUpdateTitle(sliderServo0, "Center - ");
			getRs485Line().getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
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
				getRs485Line().getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
						(short) getSliderMaxLrServo0().getValue());
			});
			sliderUpdateTitle(sliderMaxLrServo0, "Max LR - ");
			getRs485Line().getTfc().setServoCalibration(0, (short) getSliderServo0().getValue(),
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
				getRs485Line().getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
						(short) getSliderMaxLrServo1().getValue());
			});
			sliderUpdateTitle(sliderMaxLrServo1, "Max LR - ");
			getRs485Line().getTfc().setServoCalibration(1, (short) getSliderServo1().getValue(),
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
				getRs485Line().getTfc().setPWMMax((short) getSliderPwmMax().getValue());
			});
			sliderUpdateTitle(sliderPwmMax, "PWM max - ");
			getRs485Line().getTfc().setPWMMax((short) getSliderPwmMax().getValue());
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
					getRs485Line().getTfc().setLED(0, getTglbtnLed1().getModel().isSelected());
				}
			});
		}
		return tglbtnLed1;
	}

	private JToggleButton getTglbtnLed2() {
		if (tglbtnLed2 == null) {
			tglbtnLed2 = new JToggleButton("LED2");
			tglbtnLed2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getRs485Line().getTfc().setLED(1, getTglbtnLed2().getModel().isSelected());
				}
			});
		}
		return tglbtnLed2;
	}

	private JToggleButton getTglbtnLed3() {
		if (tglbtnLed3 == null) {
			tglbtnLed3 = new JToggleButton("LED3");
			tglbtnLed3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getRs485Line().getTfc().setLED(2, getTglbtnLed3().getModel().isSelected());
				}
			});
		}
		return tglbtnLed3;
	}

	private JToggleButton getTglbtnLed4() {
		if (tglbtnLed4 == null) {
			tglbtnLed4 = new JToggleButton("LED4");
			tglbtnLed4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getRs485Line().getTfc().setLED(3, getTglbtnLed4().getModel().isSelected());
				}
			});
		}
		return tglbtnLed4;
	}

	private JPanel getPanel_1_2() {
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
			tglbtnPwm.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getRs485Line().getTfc()
							.MotorPWMOnOff(getTglbtnPwm().getModel().isSelected() ? (byte) 1 : ((byte) 0));
				}
			});
		}
		return tglbtnPwm;
	}

	private JToggleButton getTglbtnServo() {
		if (tglbtnServo == null) {
			tglbtnServo = new JToggleButton("Servo");
			tglbtnServo.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					getRs485Line().getTfc()
							.ServoOnOff(getTglbtnServo().getModel().isSelected() ? (byte) 1 : ((byte) 0));
				}
			});
		}
		return tglbtnServo;
	}

	private JPanel getPanel_1_3() {
		if (panelStearingTitle == null) {
			panelStearingTitle = new JPanel();
			panelStearingTitle.setPreferredSize(new Dimension(300, 300));
			panelStearingTitle.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
					"Stearing", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelStearingTitle.setLayout(new BorderLayout(0, 0));
			panelStearingTitle.add(getPanel_1_4());
		}
		return panelStearingTitle;
	}

	private JPanel getPanel_1_4() {
		if (panelStearing == null) {
			panelStearing = new JPanel();
			panelStearing.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					getRs485Line().getTfc().setServo_i(0, (short) 0);
					getRs485Line().getTfc().setMotorPWM_i((short) 0, (short) 0);
				}
			});
			panelStearing.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					double width = getPanel_1_4().getWidth();
					double height = getPanel_1_4().getHeight();
					double stearing = (e.getX() - width / 2) / (width / 2);
					double speed = (height / 2 - e.getY()) / (height / 2);
					speed = Math.min(speed, 1);
					speed = Math.max(speed, -1);
					stearing = Math.min(stearing, 1);
					stearing = Math.max(stearing, -1);
					int servoPos = (int) (stearing * Tfc.SERVO_MINMAX);
					int pwm = (int) (speed * Tfc.PWM_MINMAX);
					getRs485Line().getTfc().setServo_i(0, (short) servoPos);
					getRs485Line().getTfc().setMotorPWM_i((short) pwm, (short) pwm);
				}
			});
			panelStearing.setPreferredSize(new Dimension(300, 300));
			panelStearing.setBackground(Color.WHITE);
			panelStearing.setLayout(new GridLayout(1, 0, 0, 0));
		}
		return panelStearing;
	}

	private JPanel getPanel_1_5() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.add(getPanel());
			panel_1.add(getPanel_1_2());
		}
		return panel_1;
	}

	private JButton getBtnSetting() {
		if (btnSetting == null) {
			btnSetting = new JButton("Send Setting");
			btnSetting.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getRs485Line().getTfc().sendSetting();
				}
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
			gblPanelConnect.rowHeights = new int[] { 0, 0, 0 };
			gblPanelConnect.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gblPanelConnect.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			panelConnect.setLayout(gblPanelConnect);
			GridBagConstraints gbcLblComPort = new GridBagConstraints();
			gbcLblComPort.insets = new Insets(0, 0, 5, 5);
			gbcLblComPort.anchor = GridBagConstraints.EAST;
			gbcLblComPort.gridx = 0;
			gbcLblComPort.gridy = 0;
			panelConnect.add(getLblComPort(), gbcLblComPort);
			GridBagConstraints gbcTxtComPort = new GridBagConstraints();
			gbcTxtComPort.insets = new Insets(0, 0, 5, 0);
			gbcTxtComPort.fill = GridBagConstraints.HORIZONTAL;
			gbcTxtComPort.gridx = 1;
			gbcTxtComPort.gridy = 0;
			panelConnect.add(getTxtComPort(), gbcTxtComPort);
			GridBagConstraints gbcBtnAutodetect = new GridBagConstraints();
			gbcBtnAutodetect.insets = new Insets(0, 0, 0, 5);
			gbcBtnAutodetect.gridx = 0;
			gbcBtnAutodetect.gridy = 1;
			panelConnect.add(getBtnAutodetect(), gbcBtnAutodetect);
			GridBagConstraints gbcBtnConnect = new GridBagConstraints();
			gbcBtnConnect.fill = GridBagConstraints.HORIZONTAL;
			gbcBtnConnect.gridx = 1;
			gbcBtnConnect.gridy = 1;
			panelConnect.add(getBtnConnect(), gbcBtnConnect);
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
			txtComPort.setText("com30");
			txtComPort.setColumns(10);
		}
		return txtComPort;
	}

	private JButton getBtnConnect() {
		if (btnConnect == null) {
			btnConnect = new JButton("Connect");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getRs485Line().setComPort(getTxtComPort().getText());
					getRs485Line().startRead();
					getContentPane().setBackground(getBackground());
					getBtnConnect().setEnabled(false);
				}
			});
		}
		return btnConnect;
	}

	private JButton getBtnAutodetect() {
		if (btnAutodetect == null) {
			btnAutodetect = new JButton("Autodetect");
			btnAutodetect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(getRootPane(), "Disconect car!");
					List<String> oldNames = Arrays.asList(SerialPortList.getPortNames());
					JOptionPane.showMessageDialog(getRootPane(), "Connect car again!");
					List<String> newNames = Arrays.asList(SerialPortList.getPortNames());
					String foundName = newNames.stream().filter(name -> !oldNames.contains(name)).findFirst()
							.orElse(null);
					if (foundName != null) {
						getTxtComPort().setText(foundName);
						JOptionPane.showMessageDialog(getRootPane(), "Port sucessfully detected!");
					} else {
						JOptionPane.showMessageDialog(getRootPane(), "Port detection failed!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
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
			gblPanelRecording.rowHeights = new int[] { 0, 0, 0 };
			gblPanelRecording.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gblPanelRecording.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
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
			gbcBtnSelectFile.insets = new Insets(0, 0, 0, 5);
			gbcBtnSelectFile.gridx = 0;
			gbcBtnSelectFile.gridy = 1;
			panelRecording.add(getBtnSelectFile(), gbcBtnSelectFile);
			GridBagConstraints gbcTglbtnRecord = new GridBagConstraints();
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
				if(getTglbtnRecord().getModel().isSelected()){
					try {
						getRs485Line().startRecording(new FileOutputStream(getTxtFile().getText()));
					} catch (FileNotFoundException e1) {
						logger.info("File open error", e1);
						JOptionPane.showMessageDialog(getRootPane(), "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					getRs485Line().stopRecording();
				}
			});
		}
		return tglbtnRecord;
	}
}
