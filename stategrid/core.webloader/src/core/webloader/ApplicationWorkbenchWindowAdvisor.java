package core.webloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.HandlerUtil;

import Siteview.Operators;
import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.SiteviewSecurityException;
import Siteview.Api.BusinessObject;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.Field;
import Siteview.Api.FieldDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.WindowsForms;

import siteview.windows.forms.Branding;
import siteview.windows.forms.ImageHelper;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Xml.XmlElement;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private Composite logo;
	
    private Control toolbar;

    private Control page;

	private Label lblUserPresent;
	
	private Map<String,String> m_htId2Status = new HashMap<String,String>();
	private Map<String,String> m_htStatus2Id = new HashMap<String,String>();
    
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setTitle(Branding.getProductName()); //$NON-NLS-1$
        configurer.setShellStyle(SWT.NO_TRIM);
        configurer.setShowPerspectiveBar(true);
        configurer.setShowMenuBar(false);
        
//        IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();
//        apiStore.setValue(
//                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS,
//                false);
        
    }

	@Override
	public void postWindowOpen() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setMaximized(true);
		super.postWindowOpen();
	}

	@Override
	public void createWindowContents(Shell shell) {
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

//        Menu menu = configurer.createMenuBar();
//        shell.setMenuBar(menu);
		
		final FormLayout layout = new FormLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);
        
        this.logo = new Composite(
                getWindowConfigurer().getWindow().getShell(), SWT.NONE);
        
        this.logo.setLayout(new FormLayout());
        
        final Composite left = new Composite(this.logo, SWT.NONE);
        if (Branding.getLogo()!=null)
        	 left.setBackgroundImage(Branding.getLogo());
        else
        	left.setBackgroundImage(ImageHelper.LoadImage(Activator.PLUGIN_ID, "html/menu_l.png"));
        
        left.setLayout(null);
        left.setBackgroundMode(SWT.INHERIT_FORCE);
        Label lblEmpty = new Label(left,SWT.NONE);
        lblEmpty.setSize(222, 64);

        FormData fd = new FormData();
        fd.left = new FormAttachment(0,0);
        fd.top = new FormAttachment(0,0);
        left.setLayoutData(fd);
        left.setSize(209, 64);
        
        final Composite fill = new Composite(this.logo, SWT.NONE);
        fill.setBackgroundImage(ImageHelper.LoadImage(Activator.PLUGIN_ID, "html/menu_m.png"));

        
		this.toolbar = configurer.createCoolBarControl(fill);
       ((CoolBar) this.toolbar).setLocked(true);

        fill.setBackgroundMode(SWT.INHERIT_FORCE);
        fill.setLayout(new FormLayout());
        fd =  new FormData();
        fd.right = new FormAttachment(100,0);
        fd.left = new FormAttachment(0,0);
        fd.bottom = new FormAttachment(100,-10);
        ((CoolBar) this.toolbar).setLayoutData(fd);

        
        final Composite right = new Composite(this.logo, SWT.NONE);
        right.setBackgroundImage(ImageHelper.LoadImage(Activator.PLUGIN_ID, "html/menu_r.png"));
        right.setSize(172, 64);
        right.setLayout(null);
        
        fd =  new FormData();
        fd.right = new FormAttachment(100,0);
        fd.left = new FormAttachment(100,-172);
        fd.top = new FormAttachment(0,0);
        fd.bottom = new FormAttachment(100,0);
        right.setLayoutData(fd);
        
        
        fd =  new FormData();
        fd.left = new FormAttachment(left,0);
        fd.top = new FormAttachment(0,0);
        fd.right = new FormAttachment(right,0);
        fd.bottom = new FormAttachment(100,0);
        fill.setLayoutData(fd);
        
        right.setBackgroundMode(SWT.INHERIT_FORCE);
        
        
        GetUsersStatus();
        lblUserPresent = new Label(right, SWT.NONE);
		String userPresenceStatusStr = getUserPresenceDisplayStr();
		lblUserPresent.setText(userPresenceStatusStr);
		lblUserPresent.setForeground(new Color(Display.getCurrent(),255,255,255));
		lblUserPresent.pack();
		
		lblUserPresent.setCursor(new Cursor(Display.getCurrent(),SWT.CURSOR_HAND));
		final Menu popMenu = CreatePopMenu(lblUserPresent);
		lblUserPresent.setMenu(popMenu);
		
		lblUserPresent.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button ==1){
					Label t = (Label)e.widget;
					String strStatus = getUserPresenceStr();
					for(MenuItem mi : popMenu.getItems()){
						if (mi.getText().equals(strStatus)){
							mi.setSelection(true);
							break;
						}
					}
					popMenu.setLocation(t.getParent().toDisplay(t.getLocation().x, t.getLocation().y + t.getBounds().height));
					popMenu.setVisible(true);
					
				}
			}});
		
       ((CoolBar) this.toolbar).setBackgroundMode(SWT.INHERIT_FORCE);
        this.page = configurer.createPageComposite(shell);

        
        Control c = configurer.createStatusLineControl(shell);
        if (!configurer.getShowStatusLine())
        	c.setVisible(false);
        
        layoutNormal();

	}
	
	
	private void createCoolBar(){
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		Composite parent = ((CoolBar) this.toolbar).getParent();
		((CoolBar) this.toolbar).dispose();
			this.toolbar = configurer.createCoolBarControl(parent);
	       ((CoolBar) this.toolbar).setLocked(true);



	        FormData fd =  new FormData();
	        fd.right = new FormAttachment(100,0);
	        fd.left = new FormAttachment(0,0);
	        fd.bottom = new FormAttachment(100,-10);
	        ((CoolBar) this.toolbar).setLayoutData(fd);
	}
	
    private void layoutNormal() {
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(0, 64);
        this.logo.setLayoutData(data);
//        data = new FormData();
//        data.top = new FormAttachment(this.logo, 5, SWT.BOTTOM);
//        data.left = new FormAttachment(0, 0);
//        data.right = new FormAttachment(100, 0);
//        this.toolbar.setLayoutData(data);
        data = new FormData();
        data.top = new FormAttachment(this.logo, 2, SWT.BOTTOM);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100,0);
        this.page.setLayoutData(data);
        layout();
    }

    private void layout() {
        getWindowConfigurer().getWindow().getShell().layout(true);
        if (this.page != null) {
            ((Composite) this.page).layout(true);
        }
    }
    
	private String getUserPresenceStr(){
		String userPresenceStatusId = this.GetUserPresenceStatusId();
		String str4 = m_htId2Status.get(userPresenceStatusId);
		if (str4 == null) str4 = "";
		return str4;
	}
	
    private String GetUserPresenceStatusId() {
    	 try
         {
             return ConnectionBroker.get_SiteviewApi().get_AuthenticationService().get_CurrentUserStatus();
         }
         catch (Exception ex)
         {
             if (ex instanceof Siteview.SiteviewDataProviderException)
             {
            	 MessageDialog.openError(null, "错误", "数据连接异常，请检查数据库连接。");

             }
             else
             {
            	 MessageDialog.openError(null, "错误", ex.getMessage());
             }
             return "";
         }
	}

	private void GetUsersStatus()
    {
        String key = "";
        String str2 = "";
        this.m_htId2Status = new HashMap<String,String>();
        this.m_htStatus2Id = new HashMap<String,String>();
        try
        {
            BusinessObjectDef businessObjectDef = ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary().GetBusinessObjectDef("UserPresence");
            if (businessObjectDef != null)
            {
                SiteviewQuery query = new SiteviewQuery();
                query.set_CacheRequested (true);
                query.AddBusObQuery(businessObjectDef.get_Name(), QueryInfoToGet.All);
                FieldDef annotatedField = businessObjectDef.GetAnnotatedField("UserPresencePrivate");
                XmlElement element = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, "False");
                XmlElement element2 = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, "True");
                annotatedField = businessObjectDef.GetAnnotatedField("UserPresenceOwner");
                XmlElement element3 = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, ConnectionBroker.get_SiteviewApi().get_AuthenticationService().get_CurrentUserName());
                XmlElement element4 = query.get_CriteriaBuilder().AndExpressions(element2, element3);
                XmlElement element5 = query.get_CriteriaBuilder().OrExpressions(element, element4);
                query.set_BusObSearchCriteria (element5);
                ICollection is2 = ConnectionBroker.get_SiteviewApi().get_BusObService().get_SimpleQueryResolver().ResolveQueryToBusObList(query);
                Field field = null;
                IEnumerator it = is2.GetEnumerator();
                while(it.MoveNext()){
                	Siteview.Api.BusinessObject obj2 = (BusinessObject) it.get_Current();
                
                    field = obj2.GetAnnotatedField("RecId");
                    if (field != null)
                    {
                        key = field.GetString();
                    }
                    field = obj2.GetAnnotatedField("DisplayField");
                    if (field != null)
                    {
                        str2 = field.GetString();
                    }
                    this.m_htId2Status.put(key, str2);
                    this.m_htStatus2Id.put(str2, key);
                }
            }
        }
        catch (SiteviewSecurityException e)
        {
        	e.printStackTrace();
        }
    }
	
	private String getUserPresenceDisplayStr(){
		String userPresenceStatusId = this.GetUserPresenceStatusId();
		String str4 = m_htId2Status.get(userPresenceStatusId);
		if (str4 == null) str4 = "";
		
		str4 = String.format("▼%s【%s】(%s)",ConnectionBroker.get_SiteviewApi().get_SystemFunctions().get_CurrentUserName(),
				ConnectionBroker.get_SiteviewApi().get_RoleManager().GetCurrentRoleName(),str4);
		return str4;
	}
	
	
	//用户在线状态菜单
	private Menu CreatePopMenu(Control c){
		Menu popMenu = new Menu(c);
		SelectionAdapter sa = new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem mi = (MenuItem) e.widget;
				String status = (String) mi.getData();
				if (status!=null && status.equals("exit")){
					e.doit = onExit();
					//mi.setSelection(e.doit);
				}else{
					if (status != null)
						ConnectionBroker.get_SiteviewApi().get_AuthenticationService().SetUserStatus(status);
					lblUserPresent.setText(getUserPresenceDisplayStr());
					lblUserPresent.pack();
				}
			}
			
		};

		for(String str: m_htId2Status.keySet()){
			if (m_htId2Status.get(str).equals("在线")){
				MenuItem mi = new MenuItem(popMenu,SWT.RADIO);
				mi.setText(m_htId2Status.get(str));
				mi.setData(str);
				mi.addSelectionListener(sa);
			}
		}
		
		for(String str: m_htId2Status.keySet()){
			if (!m_htId2Status.get(str).equals("在线")){
				MenuItem mi = new MenuItem(popMenu,SWT.RADIO);
				mi.setText(m_htId2Status.get(str));
				mi.setData(str);
				mi.addSelectionListener(sa);
			}
		}
		MenuItem mi = new MenuItem(popMenu,SWT.RADIO);
		mi.setText("退出");
		mi.setData("exit");
		mi.setImage(ImageHelper.LoadImage(core.webloader.Activator.PLUGIN_ID, "html/exit.png"));
		mi.addSelectionListener(sa);
		return popMenu;
	}

	protected boolean onExit() {
//		try {
//			RWT.getResponse().getWriter().println("location.reload();");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		PlatformUI.getWorkbench().saveAllEditors(true);
		
		if (!MessageDialog.openConfirm(null, "确认", "您是否真的要退出系统？")) return false;
		
		if (ConnectionBroker.get_SiteviewApi()!=null){
			if (ConnectionBroker.get_SiteviewApi().get_LoggedIn()){
				ConnectionBroker.get_SiteviewApi().Logout();
			}
			WindowsForms.UnInitialize();
		}
		
		JSExecutor.executeJS("location.reload();");

		//PlatformUI.getWorkbench().getActiveWorkbenchWindow().close();


		return true;
	}
	
}
