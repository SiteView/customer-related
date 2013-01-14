package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import system.Collections.IList;
import Core.Dashboards.DashboardDef;
import Core.Dashboards.DashboardPartDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Xml.XmlSecurityRight;

import core.ui.DashBoardCenterForm;

public class DashBoardCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ScopeUtil m_ScopeUtil = new ScopeUtil(ConnectionBroker.get_SiteviewApi(), ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary(), DashboardDef.get_ClassName(), "Core.Security.Dashboards");
		m_ScopeUtil.set_CheckRights(true);
		IList m_lstSupportedScopesWithOwners = m_ScopeUtil.GetSupportedScopesWithOwners(XmlSecurityRight.ToRight("VAED"), false);
		if(m_lstSupportedScopesWithOwners.get_Count() <= 0)
		{
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "权限不足", "您没有权限进行此操作！");
			return null;
		}
		
		DashBoardCenterForm frm = new  DashBoardCenterForm(false,"",false);
		frm.setBlockOnOpen(true);
		frm.open();
		return null;
	}

}
