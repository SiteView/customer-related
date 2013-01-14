package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import Siteview.Windows.Forms.ConnectionBroker;
import core.businessprocess.TriggerCenterForm;


public class TriggerCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TriggerCenterForm frm = new TriggerCenterForm(ConnectionBroker.get_SiteviewApi(),ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary());
		frm.setBlockOnOpen(true);
		frm.open();
		return null;
	}

}
