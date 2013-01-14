package core.webloader;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import Siteview.Windows.Forms.ConnectionManager;

import siteview.windows.forms.Branding;

public class ServerConfig implements IEntryPoint {

	public ServerConfig() {
	}

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		Branding.init();
		
		ConnectionManager cm = new ConnectionManager(display.getActiveShell());
		cm.open();
		return 0;
	}

}
