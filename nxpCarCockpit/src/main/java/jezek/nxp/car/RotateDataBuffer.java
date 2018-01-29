package jezek.nxp.car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RotateDataBuffer implements RotateDataBufferInterface {

	private int width;
	private int height;
	private int[] data;
	private RowDataInfo[] rowDataInfos;
	private int agregation;
	private int rowPosition;
	private int currentAgregatin;
	private int leftBorderCut = 127 - 15;
	private int rightBorderCut = 19;

	public RotateDataBuffer(int pixeCount, int height, int agregation) {
		super();
		if (height < 1 || agregation < 1) {
			throw new IllegalArgumentException("Wrong size or agregation.");
		}
		this.width = pixeCount;
		this.height = height;
		this.agregation = agregation;
		data = new int[width * height];
		rowDataInfos = new RowDataInfo[height];
		for (int i = 0; i < rowDataInfos.length; i++) {
			rowDataInfos[i] = new RowDataInfo();
		}
		rowPosition = 0;
		currentAgregatin = 0;
	}

	public int[] getData() {
		return data;
	}

	public RowDataInfo[] getRowDataInfos() {
		return rowDataInfos;
	}

	public int getLeftBorderCut() {
		return leftBorderCut;
	}

	public int getRightBorderCut() {
		return rightBorderCut;
	}

	public void addRow(int[] row) {
		if (row.length != width) {
			throw new IllegalArgumentException("Wrong row length.");
		}
		if (currentAgregatin > 0) {
			for (int j = 0, i = rowPosition * width; i < rowPosition * width + width; i++, j++) {
				data[i] += row[j];
			}
			if (currentAgregatin + 1 == agregation) {
				for (int i = rowPosition * width; i < rowPosition * width + width; i++) {
					data[i] /= agregation;
				}
				int[] rowData = new int[width];
				System.arraycopy(data, rowPosition * width, rowData, 0, width);
				detectEdges(rowData, rowPosition);
			}
		} else {
			System.arraycopy(row, 0, data, width * rowPosition, width);
		}
		if (currentAgregatin + 1 == agregation) {
			detectEdges(row, rowPosition);
			rowPosition = (rowPosition + 1) % height;
		}
		currentAgregatin = (currentAgregatin + 1) % agregation;
	}

	private void detectEdges(int[] row, int rowIndex) {
		int[] diff = new int[leftBorderCut - rightBorderCut];
		for (int i = 0; i < leftBorderCut - rightBorderCut; i++) {
			diff[i] = row[i + rightBorderCut] - row[i + rightBorderCut + 1];
		}
		List<Integer> lineCandidate = new ArrayList<Integer>();
		for (int i = 0; i < diff.length - 1; i++) {
			if (Math.abs(diff[i] + diff[i + 1]) > 1200) {
				int move = 0;
				if (diff[i] + diff[i + 1] > 0) {
					move += 1;
				}
				if (diff[i + 1] > 1500) {
					move += 1;
				}
				lineCandidate.add(i + rightBorderCut + move);
			}
		}
		rowDataInfos[rowIndex].candidates = new ArrayList<>();
		rowDataInfos[rowIndex].candidates.addAll(lineCandidate);
		Set<Integer> group = new HashSet();
		List<HotCanditateInfo> hotCandidates = new ArrayList<>();
		List<Integer> lines = new ArrayList<>();
		int rightLastIndex = getLastRight(rowIndex, 30);
		int leftLastIndex = getLastLeft(rowIndex, 30);
		while (!lineCandidate.isEmpty()) {
			group.clear();
			int index = lineCandidate.get(0);
			group.add(index);
			group.addAll(lineCandidate.stream().filter(c -> Math.abs(c - index) < 6).collect(Collectors.toList()));
			lineCandidate.removeAll(group);
			int avg = group.stream().collect(Collectors.averagingInt(Integer::intValue)).intValue();
			int bonus = 0;
			if (rightLastIndex - avg < 10 || leftLastIndex - avg < 10) {
				bonus = 20;
			}
			hotCandidates.add(new HotCanditateInfo(avg, group.size() + bonus));
		}
		rowDataInfos[rowIndex].hotCandidates = new ArrayList<>(hotCandidates);

		// lineCandidate.stream().filter(c -> lineCandidate.stream().filter(c1 -> c-c1 <
		// 3))
		while (!hotCandidates.isEmpty()) {
			HotCanditateInfo line = hotCandidates.get(0);
			List<HotCanditateInfo> closeLines = hotCandidates.stream().filter(c -> c.index - line.index < 40)
					.collect(Collectors.toList());
			lines.add(closeLines.stream().collect(Collectors.maxBy((c1, c2) -> c1.weight - c2.weight)).get().index);
			hotCandidates.removeAll(closeLines);
		}
		rowDataInfos[rowIndex].lines = lines;
		rowDataInfos[rowIndex].setLeftIndex(-1);
		rowDataInfos[rowIndex].setRightIndex(-1);
		if (lines.size() > 1) {
			Collections.sort(lines);
			if (Math.abs(rightLastIndex - lines.get(0)) < 6) {
				rowDataInfos[rowIndex].setRightIndex(lines.get(0));
			} else if (Math.abs(leftLastIndex - lines.get(0)) < 6) {
				rowDataInfos[rowIndex].setLeftIndex(lines.get(0));
				rowDataInfos[rowIndex].setRightIndex(lines.get(1));
			} else if (Math.abs(leftLastIndex - lines.get(1)) < 6) {
				rowDataInfos[rowIndex].setRightIndex(lines.get(1));
			} else {
				rowDataInfos[rowIndex].setLeftIndex(lines.get(0));
				rowDataInfos[rowIndex].setRightIndex(lines.get(1));
			}
		} else if (!lines.isEmpty()) {
			if (rightLastIndex != -1 && leftLastIndex != -1
					&& Math.abs(rightLastIndex - lines.get(0)) <= Math.abs(leftLastIndex - lines.get(0))) {
				rowDataInfos[rowIndex].setRightIndex(lines.get(0));
			} else if (rightLastIndex != -1 && leftLastIndex != -1
					&& Math.abs(rightLastIndex - lines.get(0)) > Math.abs(leftLastIndex - lines.get(0))) {
				rowDataInfos[rowIndex].setLeftIndex(lines.get(0));
			} else if (rightLastIndex == -1 && leftLastIndex != -1 && Math.abs(leftLastIndex - lines.get(0)) < 6) {
				rowDataInfos[rowIndex].setLeftIndex(lines.get(0));
			} else if (rightLastIndex != -1 && leftLastIndex == -1 && Math.abs(rightLastIndex - lines.get(0)) < 6) {
				rowDataInfos[rowIndex].setRightIndex(lines.get(0));
			} else {
				int oldRightLastIndex = getLastRight(rowIndex, getHeight());
				int oldLeftLastIndex = getLastLeft(rowIndex, getHeight());
				int lineIndex = lines.get(0);
				if (oldRightLastIndex != -1 && oldLeftLastIndex != -1) {
					if (Math.abs(oldLeftLastIndex - lineIndex) < Math.abs(oldRightLastIndex - lineIndex)) {
						rowDataInfos[rowIndex].setLeftIndex(lineIndex);
					} else {
						rowDataInfos[rowIndex].setRightIndex(lineIndex);
					}
				} else if (oldRightLastIndex == -1 && oldLeftLastIndex != -1) {
					rowDataInfos[rowIndex].setLeftIndex(lineIndex);
				} else if (oldRightLastIndex != -1 && oldLeftLastIndex == -1) {
					rowDataInfos[rowIndex].setRightIndex(lineIndex);
				} else {
					if (lineIndex < getWidth() / 2) {
						rowDataInfos[rowIndex].setLeftIndex(lineIndex);
					} else {
						rowDataInfos[rowIndex].setRightIndex(lineIndex);
					}
				}
			}
		}
	}

	public int getLastRight(int rowIndex, int limit) {
		return getLastLineIndex(rowIndex, limit, 1);
	}

	public int getLastLeft(int rowIndex, int limit) {
		return getLastLineIndex(rowIndex, limit, 0);
	}

	public int getLastLineIndex(int rowIndex, int limit, int index) {
		for (int i = 0; i < limit; i++) {
			int prewiousRow = (rowIndex - 1 - i) % height;
			if (prewiousRow < 0) {
				prewiousRow += height;
			}
			if (rowDataInfos[prewiousRow].lineIndexs[index] != -1) {
				return rowDataInfos[prewiousRow].lineIndexs[index];
			}
		}
		return -1;
	}

	public int getAgregation() {
		return agregation;
	}

	public void setAgregation(int agregation) {
		this.agregation = agregation;
	}

	public int getRowPosition() {
		return rowPosition;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
