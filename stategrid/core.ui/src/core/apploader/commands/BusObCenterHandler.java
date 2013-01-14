package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;



import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import core.businessprocess.BusObCenterForm;
import Siteview.Windows.Forms.ConnectionBroker;
public class BusObCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		BusObCenterForm frm = new BusObCenterForm(ConnectionBroker.get_SiteviewApi(),ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary());
		frm.setBlockOnOpen(true);
		frm.open();
		return null;
	}

}
