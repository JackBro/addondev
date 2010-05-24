package gef.example.helloworld.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ContentsModel extends AbstractElementModel {

	public static final String P_ADD_CHILD = "_add_child";
	public static final String P_ADD_CHILDREN = "_add_children";
	public static final String P_REMOVE_CHILD = "_remove_child";
	public static final String P_REMOVE_ALL_CHILDREN = "_remove_all_children";
	
	@Override
	public void addChild(AbstractElementModel child) {
		// TODO Auto-generated method stub
		super.addChild(child);
		firePropertyChange(P_ADD_CHILD, null, child);
	}
	@Override
	public void addChild(int index, AbstractElementModel child) {
		// TODO Auto-generated method stub
		super.addChild(index, child);
		firePropertyChange(P_ADD_CHILD, null, child);
	}
	@Override
	public void setChildren(List children) {
		// TODO Auto-generated method stub
		super.setChildren(children);
	}
	@Override
	public void removeAllChild() {
		// TODO Auto-generated method stub
		firePropertyChange(P_REMOVE_ALL_CHILDREN, null, children);
		super.removeAllChild();
	}
	@Override
	public void removeChild(AbstractElementModel child) {
		// TODO Auto-generated method stub
		
		super.removeChild(child);
		firePropertyChange(P_REMOVE_CHILD, null, child);
	}
	@Override
	public AbstractElementModel removeChild(int index) {
		// TODO Auto-generated method stub
		firePropertyChange(P_REMOVE_CHILD, null, children.indexOf(children));
		return super.removeChild(index);
	}

//	public void addChild(ElementModel child) {
//		children.add(child); // 子モデルを追加
//		// EditPartへの通知
//		//firePropertyChange(P_CHILDREN, null, null);
//		firePropertyChange(P_ADD_CHILDREN, null, child);
//	}
//
//	public void addChild(int index, ElementModel child) {
//		children.add(index, child);
//		// EditPartへの通知
//		//firePropertyChange(P_CHILDREN, null, null);
//		firePropertyChange(P_ADD_CHILDREN, null, child);
//	}
//
//	public void removeChild(Object child) {
//		// 子モデルの削除
//		children.remove(child);
//		// EditPartへの通知
//		firePropertyChange(P_REMOVE_CHILDREN, null, child);
//	}
//	
//	public void removeAllChild() {
//		// 子モデルの削除
//		children.clear();
//		// EditPartへの通知
//		firePropertyChange(P_CLEAR_CHILDREN, null, null);
//	}
	
//	public int getChildIndex(Object child) {
//		int index = -1;
//		for (Object model : children) {
//			index++;
//			if (model == child) {
//				return index;
//			}
//		}
//		return index;
//	}
}
