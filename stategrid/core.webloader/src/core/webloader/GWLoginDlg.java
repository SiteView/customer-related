package core.webloader;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import siteview.windows.forms.Branding;
import siteview.windows.forms.ImageHelper;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Security.Principal.IPrincipal;
import Siteview.ConnectionDef;
import Siteview.ConnectionDefGroup;
import Siteview.FileBasedConnectionDefMgr;
import Siteview.IConnectionDefManager;
import Siteview.IModuleManager;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ConnectionManager;

public class GWLoginDlg extends Dialog implements IRunnableWithProgress {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6861379417006618107L;

	private String loginId = "";
	private IPrincipal principal;

	public void run(IProgressMonitor monitor) throws InterruptedException {

		monitor.beginTask("µÇÂ¼ÖÐ...", 100);
		Thread.sleep(500);
		monitor.worked(10);//
		if (ConnectionBroker.getInstance().LoginToSiteview(
				cboConnection.getText(), username.getText(),
				password.getText(), monitor)) {
			monitor.worked(60);
			IModuleManager mm = ConnectionBroker.get_SiteviewApi()
					.get_ModuleManager();
			monitor.worked(10);
			mm.RegisterModule("Core", "Core.Module.Main");
			monitor.worked(10);
			mm.RegisterInstantModule("CoreAdmin", "AdminLoader");
			monitor.worked(10);
			monitor.done();
			okPressed();

		} else {
			MessageBox msgbox = new MessageBox(this.getParent());
			msgbox.setMessage("µÇÂ¼Ê§°Ü£¡");
			msgbox.setText("´íÎó");
			msgbox.open();
			throw (new InterruptedException("Login.fail"));
		}
	}

	private void okPressed() {
		if (shell!=null){
			result = 1;
			shell.dispose();
		}
	}

	private Text username;
	private Text password;
	private Combo cboConnection;
	private boolean allowSavePassword;
	private boolean isReconnectMod;

	private int result;

	private Button btLogin;

	private Button btReset;

	private Shell shell;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public GWLoginDlg(Shell parentShell) {
		super(parentShell);

	}

	public int open() {
		result = 0;
		Shell parent = getParent();
		shell = new Shell(parent, SWT.NO_TRIM);
		shell.setSize(689, 439);

		shell.setLocation(Display.getCurrent().getClientArea().width / 2
				- shell.getShell().getSize().x / 2, Display.getCurrent()
				.getClientArea().height / 2 - shell.getSize().y / 2);

		shell.setLayout(new FillLayout());

		shell.setBackgroundMode(SWT.INHERIT_FORCE);

		Composite container = new Composite(shell, SWT.NONE);
		container.setBackgroundImage(ImageHelper.LoadImage(Activator.PLUGIN_ID,
				"/html/login.png"));
		container.setBackgroundMode(SWT.INHERIT_FORCE);

		Label logo = new Label(container, SWT.NONE);
		logo.setLocation(10, 10);
		logo.setImage(ImageHelper.LoadImage(Activator.PLUGIN_ID,
				"/html/LOGO.png"));
		logo.pack();

		Label title = new Label(container, SWT.NONE);
		title.setLocation(260, 130);
		title.setImage(ImageHelper.LoadImage(Activator.PLUGIN_ID,
				"/html/login_title.png"));
		title.pack();

		//Label info = new Label(container, SWT.NONE);
		//info.setLocation(360, 220);
		//info.setText("»¶Ó­µÇÂ½£¬ÓÐÎÊÌâÇëÁªÏµ¹ÜÀíÔ±");
		//info.setFont(new Font(Display.getCurrent(), "simsun", 14, SWT.BOLD));
		//info.pack();
		
		Label lblPhone = new Label(container, SWT.NONE);
		lblPhone.setLocation(35, 280);
		lblPhone.setText("ÈÈÏß£º400-681-2186  91871-2186");
		lblPhone.setFont(new Font(Display.getCurrent(), "simsun", 12, SWT.NORMAL));
		lblPhone.pack();

		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLocation(35, 305);
		lblEmail.setText("ÓÊÏä£º92186@sgcc.com.cn");
		lblEmail.setFont(new Font(Display.getCurrent(), "simsun", 12, SWT.NORMAL));
		lblEmail.pack();

		cboConnection = new Combo(container, SWT.READ_ONLY);
		cboConnection.setBounds(80, 10, 257, 20);
		cboConnection.setVisible(false);

		Label label_1 = new Label(container, SWT.RIGHT);
		label_1.setBounds(330, 282, 60, 20);
		label_1.setText("\u7528\u6237\u540D\uFF1A");

		username = new Text(container, SWT.BORDER);
		username.setBounds(400, 280, 200, 20);
		username.setText(loginId);
		username.setFocus();

		Label label_2 = new Label(container, SWT.RIGHT);
		label_2.setBounds(330, 322, 60, 20);
		label_2.setText("\u5BC6   \u7801\uFF1A");

		password = new Text(container, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(400, 320, 200, 20);

		Composite Comp = new Composite(container, SWT.NONE);
		Comp.setBounds(300, 355, 300, 30);
		Comp.setBackgroundMode(SWT.INHERIT_DEFAULT);

		btLogin = new Button(Comp, SWT.BORDER);
		btLogin.setBounds(60, 5, 60, 20);
		btLogin.setText("µÇÂ¼");

		btLogin.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				onLogin();
				super.widgetSelected(e);
			}
		});

		btReset = new Button(Comp, SWT.BORDER);
		btReset.setBounds(180, 5, 60, 20);
		btReset.setText("ÖØÖÃ");
		
		btReset.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				onReset();
				super.widgetSelected(e);
			}});

		shell.setDefaultButton(btLogin);

		initControl();

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	protected void onReset() {
		
		username.setText("");
		password.setText("");
	}

	protected void onLogin() {
		try {
			final String connName = cboConnection.getText();
			final String userName = username.getText();
			final String userPwd = password.getText();

			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(
					this.getParent());

			progressDialog.run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

					monitor.beginTask("µÇÂ¼ÖÐ...", 100);
					Thread.sleep(500);
					monitor.worked(10);//
					if (ConnectionBroker.getInstance().LoginToSiteview(
							connName, userName, userPwd, monitor)) {
						monitor.worked(60);
						IModuleManager mm = ConnectionBroker.get_SiteviewApi()
								.get_ModuleManager();
						monitor.worked(10);
						mm.RegisterModule("Core", "Core.Module.Main");
						monitor.worked(10);
						mm.RegisterInstantModule("CoreAdmin", "AdminLoader");
						monitor.worked(10);
						monitor.done();
						setPrincipal(system.Threading.Thread
								.get_CurrentPrincipal());

					} else {
						throw (new InterruptedException("Login.fail"));
					}

				}
			});
			okPressed();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			ConnectionBroker.Close();
		} catch (InterruptedException e) {
			if (e.getMessage().equals("Login.fail")) {
				MessageBox msgbox = new MessageBox(this.getParent());
				msgbox.setMessage("µÇÂ¼Ê§°Ü£¡");
				msgbox.setText("´íÎó");
				msgbox.open();
			}
			e.printStackTrace();
			ConnectionBroker.Close();
		}
	}

	// /**
	// * Create contents of the dialog.
	// * @param parent
	// */
	// @Override
	// protected Control createDialogArea(Composite parent) {
	// Composite container = new Composite(parent, SWT.NONE);
	// container.setLayoutData(new GridData(GridData.FILL_BOTH));
	//
	// container.setBackgroundImage(ImageHelper.LoadImage(Activator.PLUGIN_ID,
	// "/html/login.png"));
	//
	// // Label label = new Label(container, SWT.RIGHT);
	// // label.setBounds(6, 13, 64, 17);
	// // label.setText("\u8FDE\u63A5\uFF1A");
	//
	// cboConnection = new Combo(container, SWT.READ_ONLY);
	// cboConnection.setBounds(80, 10, 257, 20);
	// cboConnection.setVisible(false);
	//
	// Label label_1 = new Label(container, SWT.RIGHT);
	// label_1.setBounds(10, 64, 60, 17);
	// label_1.setText("\u7528\u6237\u540D\uFF1A");
	//
	// username = new Text(container, SWT.BORDER);
	// username.setBounds(80, 61, 257, 20);
	// username.setText(loginId);
	//
	// Label label_2 = new Label(container, SWT.RIGHT);
	// label_2.setBounds(6, 105, 64, 20);
	// label_2.setText("\u5BC6\u7801\uFF1A");
	//
	// password = new Text(container, SWT.BORDER | SWT.PASSWORD);
	// password.setBounds(80, 105, 257, 20);
	//
	// // Button btConfig = new Button(container, SWT.NONE);
	// // btConfig.addSelectionListener(new SelectionAdapter() {
	// // @Override
	// // public void widgetSelected(SelectionEvent e) {
	// // doConfig();
	// // }
	// // });
	// // btConfig.setBounds(348, 8, 72, 22);
	// // btConfig.setText("\u914D\u7F6E");
	// //btConfig.setEnabled(false);
	//
	// initControl();
	//
	// return container;
	// }

	private void initControl() {
		cboConnection.removeAll();
		IConnectionDefManager cmgr = new FileBasedConnectionDefMgr();
		cmgr.LoadConnectionsForGroupIfNeeded(ConnectionDefGroup.Common);
		// cmgr.LoadConnectionsForGroupIfNeeded(ConnectionDefGroup.Personal);
		IEnumerator it;

		ICollection ccs = cmgr.ConnectionsForGroup(ConnectionDefGroup.Common);
		it = ccs.GetEnumerator();
		while (it.MoveNext()) {
			ConnectionDef cdf = (ConnectionDef) it.get_Current();
			cboConnection.add(cdf.get_Name());
		}
		String cnn = RWT.getRequest().getParameter("cstr");
		if (cnn != null && !cnn.equals("")) {
			cboConnection.setText(cnn);
			cboConnection.setEnabled(false);
		} else if (cboConnection.getItemCount() > 0)
			cboConnection.select(0);
	}

	public void set_LoginId(String get_AuthenticationId) {
		loginId = get_AuthenticationId;

	}

	public void set_IsReconnectMode(boolean b) {
		isReconnectMod = b;

	}

	public void set_AllowSavePassword(boolean b) {
		allowSavePassword = b;

	}

	public boolean get_IsSavePasswordChecked() {
		// TODO Auto-generated method stub
		return false;
	}

	public IPrincipal getPrincipal() {
		return principal;
	}

	public void setPrincipal(IPrincipal principal) {
		this.principal = principal;
	}

}
