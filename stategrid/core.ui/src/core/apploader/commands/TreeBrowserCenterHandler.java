package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import system.Collections.IList;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Xml.XmlSecurityRight;

import core.ui.TreeBrowserCenterForm;

import Core.Dashboards.DashboardPartDef;
import Core.Presentation.TreeBrowser.TreeDef;


public class TreeBrowserCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ScopeUtil m_ScopeUtil = new ScopeUtil(ConnectionBroker.get_SiteviewApi(), ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary(), TreeDef.get_ClassName(), "Core.Security.TreeBrowser");
		m_ScopeUtil.set_CheckRights(true);
		IList m_lstSupportedScopesWithOwners = m_ScopeUtil.GetSupportedScopesWithOwners(XmlSecurityRight.ToRight("VAED"), false);
		if(m_lstSupportedScopesWithOwners.get_Count() <= 0)
		{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "权限不足", "您没有权限进行此操作！");
			return null;
		}
		
		TreeBrowserCenterForm frm = new  TreeBrowserCenterForm(TreeDef.get_ClassName(),"Core.Security.TreeBrowser","树视图中心", "" , null, null, false,false);
		frm.setBlockOnOpen(true);
		frm.open();
		return null;
	}
}
