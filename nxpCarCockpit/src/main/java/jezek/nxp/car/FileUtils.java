/*
 * Copyright 2015 David Ježek
 * 
 * This file is part of JMTP.
 * 
 * JTMP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or any later version.
 * 
 * JMTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU LesserGeneral Public 
 * License along with JMTP. If not, see <http://www.gnu.org/licenses/>.
 */

package jezek.nxp.car;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


/**
 * @author David Ježek
 *
 */
public class FileUtils {

	public static void selectFileAndWrite(String defaultDir, boolean filesOnly, FileFilter fileFilter, JComponent parent,
			Consumer<File> storeSelectedFile, Function<File, File> preprocesFile, Function<File, Boolean> writeFile) {
		JFileChooser fileChooser = new JFileChooser(defaultDir);
		if (filesOnly) {
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		fileChooser.setFileFilter(fileFilter);
		boolean done = false;
		boolean writeToFile = false;
		while (!done) {
			int dialogResult = fileChooser.showSaveDialog(parent);
			if (JFileChooser.APPROVE_OPTION == dialogResult) {
				File selectedFile = fileChooser.getSelectedFile();
				storeSelectedFile.accept(selectedFile);
				selectedFile = preprocesFile.apply(selectedFile);
				if (selectedFile.exists()) {
					int confirmResult = JOptionPane.showConfirmDialog(parent, "File already exist. Replace?", 
							"Warning", 
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					switch (confirmResult) {
					case JOptionPane.YES_OPTION:
						done = true;
						writeToFile = true;
						break;
					case JOptionPane.CANCEL_OPTION:
						break;
					case JOptionPane.NO_OPTION:
					default:
						done = true;
						break;
					}
				} else {
					writeToFile = true;
				}
				if (writeToFile) {
					done = writeFile.apply(selectedFile);
				}
			} else {
				done = true;
			}
		}
	}
}
