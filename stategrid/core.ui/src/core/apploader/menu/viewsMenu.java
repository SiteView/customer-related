package core.apploader.menu;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import core.editors.DashboardPanel;

import siteview.windows.forms.ImageHelper;
import siteview.windows.forms.ImageResolver;
import system.Collections.ArrayList;
import Core.Dashboards.DashboardDef;
import Siteview.DefRequest;
import Siteview.LegalUtils;
import Siteview.Api.RoleDef;
import Siteview.Api.WorkspaceDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ImageHolder;
import Siteview.Windows.Forms.MsgBox;

public class viewsMenu extends ContributionItem {

	@Override
	public void fill(Menu menu, int index) {
		new MenuItem(menu, SWT.SEPARATOR, index);
		
		int miIndex = index;
		RoleDef currentRole = (RoleDef) ConnectionBroker.get_SiteviewApi().get_RoleManager().GetCurrentRole();
		ArrayList wks = currentRole.get_WorkspaceList();
		
		for(int i = 0; i < wks.get_Count();i++){
			String str = (String)wks.get_Item(i);
			WorkspaceDef wksd = (WorkspaceDef)  ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(WorkspaceDef.get_ClassName(), str));
			if (wksd.get_BusinessObjectName()!=null && !wksd.get_BusinessObjectName().equals("")){
				MenuItem mi = new MenuItem(menu, SWT.PUSH, ++miIndex);
				mi.setText(wksd.get_Alias());
				mi.setData("_wks",wksd);
				
				mi.setImage(ImageHelper.getImage(wksd.get_AssociatedImage(), 0x10, 0x10));
				mi.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						MenuItem mi = (MenuItem) e.getSource();
						WorkspaceDef wkd = (WorkspaceDef) mi.getData("_wks");
						if (wkd != null){
							DashboardDef def = (DashboardDef) ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary().GetDefinition(
									DefRequest.ById(wkd.get_Scope(), wkd.get_ScopeOwner(),wkd.get_LinkedTo(),DashboardDef.get_ClassName(),wkd.get_DashboardId(),wkd.get_Perspective(),false));
							if (def == null){
								MsgBox.Show(null, "没有找到仪表盘：" + wkd.get_DashboardName(), LegalUtils.get_MessageBoxCaption());
								return;
							}
							
							DashboardPanel.open(ConnectionBroker.get_SiteviewApi(), def);
						}
					}
				});
			}
		}

	}

	@Override
	public boolean isDynamic() {
		return true;
	}
	
}
