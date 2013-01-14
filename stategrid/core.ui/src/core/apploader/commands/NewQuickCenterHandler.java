package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import Core.ui.dialog.Quickoperation;
import Siteview.AutoTaskDef;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ScopeUtil;

public class NewQuickCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISiteviewApi m_api=ConnectionBroker.get_SiteviewApi();
		AutoTaskDef taskdef=(AutoTaskDef) m_api.get_LiveDefinitionLibrary().GetNewDefForEditing(DefRequest.ByCategory(16, "",  AutoTaskDef.get_ClassName(), ""));;
		ScopeUtil m_ScopeUtil = new ScopeUtil(m_api, m_api.get_LiveDefinitionLibrary(),  AutoTaskDef.get_ClassName(), "Siteview.Security.AutoTasks");
		int phtype=16 ;  //全局变量
		String str="";   // 关联  如 Change
		IDefinition m_Def;
		m_Def= m_api.get_LiveDefinitionLibrary().GetNewDefForEditing(DefRequest.ByCategory(phtype, "", AutoTaskDef.get_ClassName(), ""));
		m_Def.set_Folder(str);
		taskdef=(AutoTaskDef) m_Def;
		Quickoperation callquick=new Quickoperation(null,new Shell(), m_api,  ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary(), m_ScopeUtil, taskdef, m_Def, "Change", "ADD",false);
		if(callquick.open()==Quickoperation.OK){
			
		}
		return null;
	}

}
