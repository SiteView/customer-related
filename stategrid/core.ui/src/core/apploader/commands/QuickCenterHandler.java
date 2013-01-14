package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import Siteview.AutoTaskDef;

import Core.ui.QuickCenterForm;

public class QuickCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		QuickCenterForm frm = new QuickCenterForm(false,"");
//		QuickCenterForm frm = new QuickCenterForm(AutoTaskDef.get_ClassName(),"Siteview.Security.AutoTasks","快速操作中心","",true,true);
		frm.open();
		return null;
	}

}
