package core.apploader.tools.service.impl;

import Core.Dashboards.DashboardDef;
import Siteview.IDefinition;
import Siteview.Windows.Forms.ConnectionBroker;
import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.editors.DashboardPanel;

public class DashboardHistoryFieldEvent implements IHistoryFieldEvent {
	public void changeSelectEvent(IDefinition idefinition) {
		DashboardDef dashboardDef=(DashboardDef) idefinition;
		DashboardPanel.open(ConnectionBroker.get_SiteviewApi(),dashboardDef);
	}
}
