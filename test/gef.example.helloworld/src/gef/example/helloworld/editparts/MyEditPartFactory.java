package gef.example.helloworld.editparts;

import java.util.HashMap;
import java.util.Map;

import gef.example.helloworld.model.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class MyEditPartFactory implements EditPartFactory {
	
	private static Map<Class, Class> editpartMap = new HashMap<Class, Class>();
	static{
		editpartMap.put(TextBoxModel.class, TextBoxEditPart.class);
		editpartMap.put(VBoxModel.class, BoxEditPart.class);
		editpartMap.put(HBoxModel.class, BoxEditPart.class);
		editpartMap.put(GroupBoxModel.class, GroupBoxEditPart.class);
		editpartMap.put(LabelModel.class, LabelEditPart.class);
		editpartMap.put(GridModel.class, GridEditPart.class);
		editpartMap.put(WindowModel.class, WindowEditPart.class);
		editpartMap.put(RootModel.class, RootEditPart.class);
		editpartMap.put(RadioModel.class, RadioEditPart.class);
		editpartMap.put(RadioGroupModel.class, RadioGroupEditPart.class);
		editpartMap.put(TabBoxModel.class, TabBoxEditPart.class);
		editpartMap.put(TabPanelModel.class, TabPanelEditPart.class);
		editpartMap.put(MenuListModel.class, MenuListEditPart.class);
		editpartMap.put(PrefwindowModel.class, PrefwindowEditPart.class);
		editpartMap.put(PrefpaneModel.class, TabPanelEditPart.class);
		editpartMap.put(XULPartModel.class, XULPartEditPart.class);
		editpartMap.put(XULRootModel.class, XULRootEditPart.class);
		editpartMap.put(OverlayModel.class, OverlayEditPart.class);
		editpartMap.put(StatusbarModel.class, StatusbarEditPart.class);
		editpartMap.put(MenuPopupModel.class, MenuPopupEditPart.class);
		editpartMap.put(MenuPopupBoxModel.class, MenuPopupEditPart.class);
	}
	/* (非 Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart part = null;
		
		if(editpartMap.containsKey(model.getClass())){
			try {
				part = (EditPart) editpartMap.get(model.getClass()).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		if(model instanceof TextBoxModel)
//			part = new TextBoxEditPart();	
//		if(model instanceof VBoxModel)
//			part = new BoxEditPart();
//		else if(model instanceof HBoxModel)
//			part = new BoxEditPart();
//		if(model instanceof GroupBoxModel)
//			part = new GroupBoxEditPart();
//		else if(model instanceof LabelModel)
//			part = new LabelEditPart();
//		else if(model instanceof GridModel)
//			part = new GridEditPart();		
//		else if(model instanceof WindowModel)
//			part = new WindowEditPart();		
//		else if(model instanceof RootModel)
//			part = new RootEditPart();
//		else if(model instanceof RadioModel)
//			part = new RadioEditPart();
//		else if(model instanceof RadioGroupModel)
//			part = new RadioGroupEditPart();
//		else if(model instanceof TabBoxModel)
//			part = new TabBoxEditPart();
//		else if(model instanceof TabPanelModel)
//			part = new TabPanelEditPart();
//		else if(model instanceof MenuListModel)
//			part = new MenuListEditPart();
//		else if(model instanceof PrefwindowModel)
//			part = new PrefwindowEditPart();
//		else if(model instanceof PrefpaneModel)
//			part = new TabPanelEditPart();
//		else if(model instanceof XULPartModel)
//			part = new XULPartEditPart();
//		else if(model instanceof XULRootModel)
//			part = new XULRootEditPart();
		
		part.setModel(model); // モデルをEditPartに設定する
		return part;
	}

}
