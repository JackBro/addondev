package gef.example.helloworld;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.jface.action.IToolBarManager;

public class MyContributor extends ActionBarContributor {

	public MyContributor() {
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		// 元に戻す・アクションの作成
		addRetargetAction(new UndoRetargetAction());
		// やり直し・アクションの作成
		addRetargetAction(new RedoRetargetAction());
		
		// 再ターゲット可能な’削除’アクションを追加
		addRetargetAction(new DeleteRetargetAction());
	}

	/* (非 Javadoc)
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {

	}

	/* (非 Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		// 削除アクションの追加
		toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.DELETE));
		// 元に戻すアクションの追加
		toolBarManager.add(
			getActionRegistry().getAction(GEFActionConstants.UNDO));

		// やり直しアクションの追加
		toolBarManager.add(
			getActionRegistry().getAction(GEFActionConstants.REDO));
	}
}
