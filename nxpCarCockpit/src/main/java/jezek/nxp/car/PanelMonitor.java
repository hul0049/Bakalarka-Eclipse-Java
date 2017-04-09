package jezek.nxp.car;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JToggleButton;

public class PanelMonitor extends JPanel {

	private Tfc tfc;
	private ImageBuffer2 imageBuffer2;
	private AnimImage anmImgMonitor;
	private UDPServer udpServer;
	private JPanel panel2;
	private JScrollBar scrollBar;
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
	private JButton btnRecreateImage;
	private JLabel lblDataToColumn;
	private JSpinner[] spinnerDataColumn = new JSpinner[6];
	private JButton btnClear;
	private JButton btnHistogram;
	private JButton btnGradient;
	private JSpinner spnHeightZoom;
	private JLabel lblHeightZoom;
	private PanelConnector panelConnector;
	private JButton btnClearSelection;
	private JButton btnInvertSelection;
	private JButton btnDeleteUnselectedsAnd;
	private JToggleButton btnInsertResponse;


	
	public PanelMonitor() {
		super();
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getAnmImgMonitor(), BorderLayout.CENTER);
		add(getPanel2(), BorderLayout.EAST);
		add(getScrollBar(), BorderLayout.WEST);
		add(getPanel3(), BorderLayout.NORTH);
	}

	private Tfc getTfc() {
		if (tfc == null) {
			tfc = new Tfc();
		}
		return tfc;
	}

	
	private AnimImage getAnmImgMonitor() {
		if (anmImgMonitor == null) {
			anmImgMonitor = new AnimImage(getImageBuffer2());
			anmImgMonitor.addMouseWheelListener(e -> getScrollBar().getModel().setValue(getScrollBar().getModel().getValue()+e.getScrollAmount()));
			anmImgMonitor.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
					if(!getBtnInsertResponse().getModel().isSelected()){
						getImageBuffer2().selectRow(e.getY());
					}
				}
			});
			anmImgMonitor.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(getBtnInsertResponse().getModel().isSelected()){
						getImageBuffer2().insertResponse(e.getX(), e.getY());
					} else {
						getImageBuffer2().selectRow(e.getY());
					}
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

	private JPanel getPanel2() {
		if (panel2 == null) {
			panel2 = new JPanel();
			GridBagLayout gblPanel2 = new GridBagLayout();
			panel2.setLayout(gblPanel2);
			GridBagConstraints gbcPanelConnector = new GridBagConstraints();
			gbcPanelConnector.gridwidth = 2;
			gbcPanelConnector.insets = new Insets(0, 0, 5, 0);
			gbcPanelConnector.fill = GridBagConstraints.BOTH;
			gbcPanelConnector.gridx = 0;
			gbcPanelConnector.gridy = 1;
			panel2.add(getPanelConnector(), gbcPanelConnector);
			GridBagConstraints gbcBtnXmlExport = new GridBagConstraints();
			gbcBtnXmlExport.insets = new Insets(0, 0, 5, 5);
			gbcBtnXmlExport.gridx = 0;
			gbcBtnXmlExport.gridy = 3;
			panel2.add(getBtnXmlExport(), gbcBtnXmlExport);
			GridBagConstraints gbcBtnXmlImport = new GridBagConstraints();
			gbcBtnXmlImport.insets = new Insets(0, 0, 5, 0);
			gbcBtnXmlImport.gridx = 1;
			gbcBtnXmlImport.gridy = 3;
			panel2.add(getBtnXmlImport(), gbcBtnXmlImport);
			GridBagConstraints gbcBtnClearSelection = new GridBagConstraints();
			gbcBtnClearSelection.insets = new Insets(0, 0, 5, 5);
			gbcBtnClearSelection.gridx = 0;
			gbcBtnClearSelection.gridy = 5;
			panel2.add(getBtnClearSelection(), gbcBtnClearSelection);
			GridBagConstraints gbcBtnInvertSelection = new GridBagConstraints();
			gbcBtnInvertSelection.insets = new Insets(0, 0, 5, 0);
			gbcBtnInvertSelection.gridx = 1;
			gbcBtnInvertSelection.gridy = 5;
			panel2.add(getBtnInvertSelection(), gbcBtnInvertSelection);
			GridBagConstraints gbcBtnDeleteUnselectedsAnd = new GridBagConstraints();
			gbcBtnDeleteUnselectedsAnd.insets = new Insets(0, 0, 5, 5);
			gbcBtnDeleteUnselectedsAnd.gridx = 0;
			gbcBtnDeleteUnselectedsAnd.gridy = 6;
			panel2.add(getBtnDeleteUnselectedsAnd(), gbcBtnDeleteUnselectedsAnd);
			GridBagConstraints gbcBtnClear = new GridBagConstraints();
			gbcBtnClear.insets = new Insets(0, 0, 5, 0);
			gbcBtnClear.gridx = 1;
			gbcBtnClear.gridy = 6;
			panel2.add(getBtnClear(), gbcBtnClear);
			GridBagConstraints gbcBtnBinExport = new GridBagConstraints();
			gbcBtnBinExport.insets = new Insets(0, 0, 5, 5);
			gbcBtnBinExport.gridx = 0;
			gbcBtnBinExport.gridy = 4;
			panel2.add(getBtnBinExport(), gbcBtnBinExport);
			GridBagConstraints gbcBtnBinImport = new GridBagConstraints();
			gbcBtnBinImport.insets = new Insets(0, 0, 5, 0);
			gbcBtnBinImport.gridx = 1;
			gbcBtnBinImport.gridy = 4;
			panel2.add(getBtnBinImport(), gbcBtnBinImport);
			GridBagConstraints gbcBtnInsertResponse = new GridBagConstraints();
			gbcBtnInsertResponse.insets = new Insets(0, 0, 5, 5);
			gbcBtnInsertResponse.gridx = 0;
			gbcBtnInsertResponse.gridy = 7;
			panel2.add(getBtnInsertResponse(), gbcBtnInsertResponse);
			GridBagConstraints gbcBtnHistogram = new GridBagConstraints();
			gbcBtnHistogram.anchor = GridBagConstraints.NORTH;
			gbcBtnHistogram.insets = new Insets(0, 0, 5, 5);
			gbcBtnHistogram.gridx = 1;
			gbcBtnHistogram.gridy = 8;
			panel2.add(getBtnHistogram(), gbcBtnHistogram);
			GridBagConstraints gbcBtnGradient = new GridBagConstraints();
			gbcBtnGradient.anchor = GridBagConstraints.NORTH;
			gbcBtnGradient.weighty = 1.0;
			gbcBtnGradient.insets = new Insets(0, 0, 0, 5);
			gbcBtnGradient.gridx = 0;
			gbcBtnGradient.gridy = 8;
			panel2.add(getBtnGradient(), gbcBtnGradient);
		}
		return panel2;
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
			gbcBtnRecreateImage.gridy = 3;
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

	private JButton getBtnRecreateImage() {
		if (btnRecreateImage == null) {
			btnRecreateImage = new JButton("Recreate Image");
			btnRecreateImage.addActionListener(evt -> {
				AnimImage animImage = getAnmImgMonitor();
				remove(animImage);
				animImage.stopAnim();
				getImageBuffer2().unregisterUpdater();
				imageBuffer2 = null;
				anmImgMonitor = null;
				add(getAnmImgMonitor(), BorderLayout.CENTER);
				revalidate();
				getAnmImgMonitor().setVisible(false);
				getAnmImgMonitor().setVisible(true);
				getAnmImgMonitor().startAnim();
				getImageBuffer2().setChanged();
				recalculateScrollbar();
				SwingUtilities.invokeLater(() -> getImageBuffer2().setChanged());
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
				JOptionPane.showMessageDialog(PanelMonitor.this, Arrays.asList(histogram).stream()
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

	private PanelConnector getPanelConnector() {
		if (panelConnector == null) {
			panelConnector = new PanelConnector(new WiFiDataTransformer(getDrivingRecord()), new OldFormatToMonitor(getDrivingRecord()));
		}
		return panelConnector;
	}
	private JButton getBtnClearSelection() {
		if (btnClearSelection == null) {
			btnClearSelection = new JButton("Clear Selection");
			btnClearSelection.addActionListener(e -> getDrivingRecord().clearSelection());
		}
		return btnClearSelection;
	}
	private JButton getBtnInvertSelection() {
		if (btnInvertSelection == null) {
			btnInvertSelection = new JButton("Invert Selection");
			btnInvertSelection.addActionListener(e -> getDrivingRecord().invertSelection());
		}
		return btnInvertSelection;
	}
	private JButton getBtnDeleteUnselectedsAnd() {
		if (btnDeleteUnselectedsAnd == null) {
			btnDeleteUnselectedsAnd = new JButton("<html>Delete UnSelecteds<p> and Missing</html>");
			btnDeleteUnselectedsAnd.addActionListener(e -> getDrivingRecord().deleteUnselectedAndMissing());
		}
		return btnDeleteUnselectedsAnd;
	}
	private JToggleButton getBtnInsertResponse() {
		if (btnInsertResponse == null) {
			btnInsertResponse = new JToggleButton("Insert Response");
		}
		return btnInsertResponse;
	}
}
