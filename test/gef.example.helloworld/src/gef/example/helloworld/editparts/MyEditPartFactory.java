package gef.example.helloworld.editparts;

import java.util.HashMap;
import java.util.Map;

import gef.example.helloworld.model.*;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class MyEditPartFactory implements EditPartFactory {
	
	private static Map<Class, Class> editpartMap = new HashMap<Class, Class>();
	static{
		editpartMap.put(ButtonModel.class, ButtonEditPart.class);
		editpartMap.put(TextBoxModel.class, TextBoxEditPart.class);
		editpartMap.put(VBoxModel.class, BoxEditPart.class);
		editpartMap.put(HBoxModel.class, BoxEditPart.class);
		editpartMap.put(ToolBoxModel.class, BoxEditPart.class);

		editpartMap.put(AnonymousModel.class, BoxEditPart.class);
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
		editpartMap.put(PreferencesModel.class, PreferencesEditPart.class);
		editpartMap.put(CheckBoxModel.class, CheckBoxEditPart.class);
		editpartMap.put(ListBoxModel.class, ListBoxEditPart.class);
		
		editpartMap.put(MenuItemModel.class, LabelEditPart.class);
		editpartMap.put(MenuModel.class, LabelEditPart.class);
		editpartMap.put(MenubarModel.class, MenuBarEditPart.class);
		
		editpartMap.put(TemplateModel.class, TemplateEditPart.class);
	}

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
		
		if(part != null){
			part.setModel(model); // モデルをEditPartに設定する
		}
		return part;
	}

}
