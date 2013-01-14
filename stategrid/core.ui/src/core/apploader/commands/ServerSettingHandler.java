package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.MsgBox;
import Siteview.Xml.SecurityRight;

public class ServerSettingHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if (!ConnectionBroker.get_SiteviewApi().get_SecurityService().HasModuleItemRight("Siteview.Security.System.ServerSettings", SecurityRight.True)){
			MsgBox.ShowWarning("你没有权限：需要‘服务器设置’权限。");
			return null;
		}
		IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
		try {
			handlerService.executeCommand("org.eclipse.ui.window.preferences",null);
		} catch (NotDefinedException e) {
			e.printStackTrace();
		} catch (NotEnabledException e) {
			e.printStackTrace();
		} catch (NotHandledException e) {
			e.printStackTrace();
		}
		return null;
	}
}
