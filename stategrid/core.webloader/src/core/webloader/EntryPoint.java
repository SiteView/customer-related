package core.webloader;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.widgets.DialogCallback;
import org.eclipse.rwt.widgets.DialogUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import siteview.windows.forms.Branding;
import siteview.windows.forms.ImageResolver;
import system.Collections.IEnumerator;

import Siteview.DefRequest;
import Siteview.ISecurityGroup;
import Siteview.Api.RoleDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.LoginDlg;
import Siteview.Windows.Forms.RoleSelectDlg;
import Siteview.Windows.Forms.WindowsForms;
import Siteview.Xml.ApplicationType;

public class EntryPoint implements IEntryPoint {

	public EntryPoint() {
	}

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();

		Branding.init();
		
		try {
			if (Branding.getDefaultIcon()!=null){
				Image icoImage = new Image(display,Branding.getDefaultIcon().getImageData().scaledTo(16,16));
				Dialog.setDefaultImage(icoImage);
			}
			Shell sh = new Shell(display);
			
			sh.setLocation(Display.getCurrent().getClientArea().width / 2
					- sh.getShell().getSize().x / 2, Display.getCurrent()
					.getClientArea().height / 2 - sh.getSize().y / 2);
			
			GWLoginDlg dlg = new GWLoginDlg(sh);
			if (dlg.open() != 1){
				return IApplication.EXIT_OK;
			}
			
			sh.dispose();
			
			
			WindowsForms.Initialize(ConnectionBroker.get_SiteviewApi());
			
			

			system.Threading.Thread.set_CurrentPrincipal(dlg.getPrincipal());
			
//			BPSetting bps = new BPSetting(null);
//			if (bps.needConfig()){
//				bps.open();
//			}
			
			
			String strSecurityName = ConnectionBroker.get_SiteviewApi().get_AuthenticationService().get_CurrentSecurityGroup();
			ISecurityGroup securityGroup = ConnectionBroker.get_SiteviewApi().get_SecurityService().GetSecurityGroupCopyByName(strSecurityName);
			
			if (securityGroup.get_RoleList().get_Count()>1){
				RoleSelectDlg rDlg = new RoleSelectDlg(display.getActiveShell(),ApplicationType.MainApplication);
				if (rDlg.open() != Dialog.OK){
					return IApplication.EXIT_OK;
				}
			}else{
				IEnumerator it = securityGroup.get_RoleList().GetEnumerator();
				it.MoveNext();
				String rName = (String) it.get_Current();
				RoleDef role = (RoleDef) ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(RoleDef.get_ClassName(),rName));
				ConnectionBroker.get_SiteviewApi().get_RoleManager().SetCurrentRole(role);
			}
			
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			//if (returnCode == PlatformUI.RETURN_RESTART)
				return returnCode;
			//else
			//	return IApplication.EXIT_OK;
		} finally {
			ConnectionBroker.Close();
			WindowsForms.UnInitialize();
			//display.dispose();
		}
		
	}

	protected void onExit() {
		System.out.println("===User Exit=====");
		ConnectionBroker.Close();
		WindowsForms.UnInitialize();
	}

}
