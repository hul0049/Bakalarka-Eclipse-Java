package jezek.nxp.car;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class DrivingRecord {

	private List<WifiMonitorData> data = new ArrayList<>();

	@XmlTransient
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addData(WifiMonitorData value) {
		synchronized (data) {
			while (!data.isEmpty() && value.getTimestamp() - data.get(data.size() - 1).getTimestamp() > 1) {
				data.add(new WifiMonitorData(data.get(data.size() - 1).getTimestamp() + 1));
			}
		}
		data.add(value);
		propertyChangeSupport.firePropertyChange("data", null, data);
	}

	public void addPropertyDataListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener("data", listener);
	}

	public void removePropertyDataListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener("data", listener);
	}

	@XmlElement(name = "record")
	@XmlElementWrapper(name = "data")
	public List<WifiMonitorData> getData() {
		return data;
	}

	public void setData(List<WifiMonitorData> data) {
		this.data = data;
		propertyChangeSupport.firePropertyChange("data", null, data);
	}

	public void clearData() {
		synchronized (data) {
			data.clear();
			propertyChangeSupport.firePropertyChange("data", null, data);
		}
	}
}
