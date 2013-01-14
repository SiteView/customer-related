package core.apploader.tools.service.impl;

import Siteview.IDefinition;
import Siteview.ReportDef;
import Siteview.Windows.Forms.ConnectionBroker;
import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.editors.ReportViewPanel;

public class ReportingHistoryFieldEvent implements IHistoryFieldEvent {

	@Override
	public void changeSelectEvent(IDefinition idefinition) {
		ReportDef reportDef = (ReportDef)idefinition;
		ReportViewPanel.open(ConnectionBroker.get_SiteviewApi(),reportDef);
	}

}
