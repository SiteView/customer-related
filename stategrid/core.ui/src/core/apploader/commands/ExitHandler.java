package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import Siteview.Windows.Forms.ConnectionBroker;


public class ExitHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (ConnectionBroker.get_SiteviewApi()!=null){
			if (ConnectionBroker.get_SiteviewApi().get_LoggedIn()){
				ConnectionBroker.get_SiteviewApi().Logout();
			}
			ConnectionBroker.get_SiteviewApi().Disconnect();
		}
		HandlerUtil.getActiveWorkbenchWindow(event).close();
		return null;
	}

}
