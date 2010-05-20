package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentsModel extends ElementModel {
	// 子モデルの追加又は削除によって構造が変化したことを示す文字列
	//public static final String P_CHILDREN = "_children";
	public static final String P_ADD_CHILDREN = "_add_children";
	public static final String P_REMOVE_CHILDREN = "_remove_children";
	public static final String P_CLEAR_CHILDREN = "_clear_children";

	//protected List children = new ArrayList(); // 子モデルのリスト

	public void addChild(ElementModel child) {
		children.add(child); // 子モデルを追加
		// EditPartへの通知
		//firePropertyChange(P_CHILDREN, null, null);
		firePropertyChange(P_ADD_CHILDREN, null, child);
	}
	
	public ContentsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void addChild(int index, ElementModel child) {
		children.add(index, child);
		// EditPartへの通知
		//firePropertyChange(P_CHILDREN, null, null);
		firePropertyChange(P_ADD_CHILDREN, null, child);
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public void removeChild(Object child) {
		// 子モデルの削除
		children.remove(child);
		// EditPartへの通知
		firePropertyChange(P_REMOVE_CHILDREN, null, child);
	}
	
	public void removeAllChild() {
		// 子モデルの削除
		children.clear();
		// EditPartへの通知
		firePropertyChange(P_CLEAR_CHILDREN, null, null);
	}
	
	public int getChildIndex(Object child) {
		int index = -1;
		for (Object model : children) {
			index++;
			if (model == child) {
				return index;
			}
		}
		return index;
	}
}
