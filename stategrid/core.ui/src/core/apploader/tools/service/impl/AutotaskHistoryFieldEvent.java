package core.apploader.tools.service.impl;


import org.eclipse.swt.widgets.Shell;

import Core.AutoTasks.RunAutoTasks;
import Core.ui.method.GetIeditorInput;
import Siteview.AutoTaskDef;
import Siteview.IDefinition;
import Siteview.Api.BusinessObject;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ConnectionBroker;
import core.apploader.tools.comm.CommHistoryDataLoad;
import core.apploader.tools.service.inter.IHistoryFieldEvent;

public class AutotaskHistoryFieldEvent implements IHistoryFieldEvent{
	
	private ISiteviewApi m_api;
	private DefinitionLibrary m_Library;
	public AutotaskHistoryFieldEvent(){
		this.m_api=ConnectionBroker.get_SiteviewApi();
		this.m_Library= ConnectionBroker.get_SiteviewApi()
				.get_LiveDefinitionLibrary();
	}
	
	public void changeSelectEvent(IDefinition idefinition){
		// 加载首页的值
		Object[] object = GetIeditorInput.getIeditorInput(m_api);
		BusinessObject busob = (BusinessObject) object[1];
		RunAutoTasks.RunAtuotasts((AutoTaskDef)idefinition, m_Library, m_api, busob);
		CommHistoryDataLoad.loadHistoryNav(busob);
	}

}
