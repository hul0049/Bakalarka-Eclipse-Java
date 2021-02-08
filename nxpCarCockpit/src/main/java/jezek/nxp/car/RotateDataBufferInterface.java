package jezek.nxp.car;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jezek.nxp.car.RotateDataBufferInterface.RowDataInfo;

public interface RotateDataBufferInterface {

	public int[] getData();

	public RowDataInfo[] getRowDataInfos();

	public RowDataInfo getRowDataInfo(int relativeIndex);
	
	public int getLeftBorderCut();

	public int getRightBorderCut();

	public void addRow(int[] row);

	public int getAgregation();

	public void setAgregation(int agregation);

	public int getRowPosition();

	public int getWidth();

	public int getHeight();

	public int getLastRight(int rowIndex, int limit);

	public int getLastLeft(int rowIndex, int limit);

	public static class RowDataInfo {
		public int[] lineIndexs = new int[2];
		public List<Integer> candidates;
		public List<HotCanditateInfo> hotCandidates;
		public List<Integer> lines;

		
		public RowDataInfo() {
			lineIndexs[0] = -1;
			lineIndexs[1] = -1;
		}

		public int getLeftIndex() {
			return lineIndexs[0];
		}

		public int getRightIndex() {
			return lineIndexs[1];
		}

		public void setLeftIndex(int index) {
			lineIndexs[0] = index;
		}

		public void setRightIndex(int index) {
			lineIndexs[1] = index;
		}
	}

	public static class HotCanditateInfo {
		public int index;
		public int weight;

		public HotCanditateInfo(int index, int weight) {
			super();
			this.index = index;
			this.weight = weight;
		}
	}
}
