package gef.example.helloworld.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

abstract public class AbstractModel {

	// リスナのリスト
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	// リスナの追加
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	// モデルの変更を通知
	public void firePropertyChange(
		String propName,
		Object oldValue,
		Object newValue) {

		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	// リスナの削除
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
}
