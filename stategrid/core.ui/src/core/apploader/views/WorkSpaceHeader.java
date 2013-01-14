package core.apploader.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ArrayList;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Xml.XmlElement;
import Core.Dashboards.DashboardDef;
import Siteview.DefRequest;
import Siteview.Operators;
import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.SiteviewSecurityException;
import Siteview.Api.BusinessObject;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.ControlDef;
import Siteview.Api.EvaluationBundle;
import Siteview.Api.Field;
import Siteview.Api.FieldDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.ImageDef;
import Siteview.Api.PanelDef;
import Siteview.Api.PictureControlDef;
import Siteview.Api.RoleDef;
import Siteview.Api.RuleDef;
import Siteview.Api.WorkspaceDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ImageHolder;
import core.ui.ImageHelper;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class WorkSpaceHeader extends ViewPart {

	public static final String ID = "core.apploader.views.WorkSpaceHeader"; //$NON-NLS-1$
	private Map<String,String> m_htId2Status = new HashMap<String,String>();
	private Map<String,String> m_htStatus2Id = new HashMap<String,String>();
	private Label lblUserPresent;

//	private Action actLeave;
//	private Action actMeeting;
//	private Action actOutOffice;
//	private Action actBusy;
//	private Action actOffline;
//	private Action actOnline;
	
	public WorkSpaceHeader() {
		GetUsersStatus();
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(new Color(Display.getCurrent(),255,255,255));
		container.setBackgroundMode(SWT.INHERIT_FORCE);
		
		createActions();
		
		ImageHolder logoImageHolder = this.GetLogoImageHolder();
		Image logoImg = null;
		if (logoImageHolder == null){
			logoImg = ImageHelper.LoadImage(core.ui.Activator.PLUGIN_ID,"icons/LogoImage.png");
		}else{
			logoImg = SwtImageConverter.ConvertToSwtImage(logoImageHolder);
		}
		container.setLayout(new FormLayout());
		Label logo = new Label(container,SWT.NONE);
		FormData fd_logo = new FormData();
		fd_logo.top = new FormAttachment(0);
		fd_logo.left = new FormAttachment(0);
		logo.setLayoutData(fd_logo);
		logo.setImage(logoImg);
		logo.pack();
		
		lblUserPresent = new Label(container, SWT.NONE);
		String userPresenceStatusStr = getUserPresenceDisplayStr();
		lblUserPresent.setText(userPresenceStatusStr);
		lblUserPresent.pack();
		lblUserPresent.setCursor(new Cursor(Display.getCurrent(),SWT.CURSOR_HAND));
		
//		final MenuManager pm = new MenuManager("#PopupMenu");
//		pm.setRemoveAllWhenShown(true);

//		final Menu popMenu = pm.createContextMenu(lblUserPresent);
		final Menu popMenu = CreatePopMenu(lblUserPresent);
		lblUserPresent.setMenu(popMenu);
//		pm.addMenuListener(new IMenuListener(){
//
//			@Override
//			public void menuAboutToShow(IMenuManager manager) {
//				manager.add(actLeave);
//				manager.add(actMeeting);
//				manager.add(actOutOffice);
//				manager.add(actBusy);
//				manager.add(actOffline);
//				manager.add(actOnline);
//			}});
		
		FormData fd_userpresetn = new FormData();
		fd_userpresetn.top = new FormAttachment(2);
		fd_userpresetn.right = new FormAttachment(100,-5);
		lblUserPresent.setLayoutData(fd_userpresetn);
		
		lblUserPresent.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button ==1){
					Label t = (Label)e.widget;
					//t.setMenu(popMenu);
					String strStatus = getUserPresenceStr();
					MenuItem miSel = null;
					for(MenuItem mi : popMenu.getItems()){
						if (mi.getText().equals(strStatus)){
							miSel = mi;
							mi.setSelection(true);
							break;
						}
					}
					
					//if (miSel != null) popMenu.setDefaultItem(miSel);
					
					popMenu.setLocation(t.getParent().toDisplay(t.getLocation().x, t.getLocation().y + t.getBounds().height));
					popMenu.setVisible(true);
					
				}
			}});
		

		
		
		Label lblConnection = new Label(container, SWT.NONE);		
		String connName = getApi().get_ConnectionName();
		if (connName.contains("}")){
			connName = connName.substring(connName.indexOf("}")+1);
		}
		lblConnection.setText("数据库：" + connName);
		lblConnection.pack();
		
		FormData fd_connection = new FormData();
		fd_connection.top = new FormAttachment(2);
		fd_connection.right = new FormAttachment(lblUserPresent,-10);
		lblConnection.setLayoutData(fd_connection);
		

		ToolBar tbWks = new ToolBar(container,SWT.FLAT|SWT.RIGHT|SWT.VERTICAL);
		FormData fd_tbWks = new FormData();
		fd_tbWks.top = new FormAttachment(0, 20);
		fd_tbWks.left = new FormAttachment(0, 240);
		tbWks.setLayoutData(fd_tbWks);
		//tbWks.setLayout(new FillLayout());

		//取角色
		RoleDef currentRole = (RoleDef) getApi().get_RoleManager().GetCurrentRole();
		ArrayList wks = currentRole.get_WorkspaceList();
		
		for(int i = 0; i < wks.get_Count();i++){
			String str = (String)wks.get_Item(i);
			WorkspaceDef wksd = (WorkspaceDef) getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(WorkspaceDef.get_ClassName(), str));
			if (wksd.get_BusinessObjectName()!=null && !wksd.get_BusinessObjectName().equals("")){
				ToolItem ti = new ToolItem(tbWks,SWT.RADIO);
				ti.setText(wksd.get_Alias());
				ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(wksd.get_AssociatedImage()),32,32));
				ti.setWidth(120);
				ti.setData(wksd);
				ti.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						ToolItem it = (ToolItem) e.item;
						WorkspaceDef wkd = (WorkspaceDef) it.getData();
						onSelectWorkSpace(wkd);
					}});
			}
			
		}
		tbWks.pack();
		
		//createActions();
		//initializeToolBar();
		//initializeMenu();
	}
	
	protected void onSelectWorkSpace(WorkspaceDef wkd) {
		if (wkd != null){
			DashboardDef def = (DashboardDef) getApi().get_LiveDefinitionLibrary().GetDefinition(
					DefRequest.ById(wkd.get_Scope(), wkd.get_ScopeOwner(),wkd.get_LinkedTo(),DashboardDef.get_ClassName(),wkd.get_DashboardId(),wkd.get_Perspective(),false));
			
			
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
             return this.getApi().get_AuthenticationService().get_CurrentUserStatus();
         }
         catch (Exception ex)
         {
             if (ex instanceof Siteview.SiteviewDataProviderException)
             {
            	 MessageDialog.openError(getSite().getShell(), "错误", "数据连接异常，请检查数据库连接。");

             }
             else
             {
            	 MessageDialog.openError(getSite().getShell(), "错误", ex.getMessage());
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
            BusinessObjectDef businessObjectDef = getApi().get_LiveDefinitionLibrary().GetBusinessObjectDef("UserPresence");
            if (businessObjectDef != null)
            {
                SiteviewQuery query = new SiteviewQuery();
                query.set_CacheRequested (true);
                query.AddBusObQuery(businessObjectDef.get_Name(), QueryInfoToGet.All);
                FieldDef annotatedField = businessObjectDef.GetAnnotatedField("UserPresencePrivate");
                XmlElement element = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, "False");
                XmlElement element2 = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, "True");
                annotatedField = businessObjectDef.GetAnnotatedField("UserPresenceOwner");
                XmlElement element3 = query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_QualifiedName(), Operators.Equals, getApi().get_AuthenticationService().get_CurrentUserName());
                XmlElement element4 = query.get_CriteriaBuilder().AndExpressions(element2, element3);
                XmlElement element5 = query.get_CriteriaBuilder().OrExpressions(element, element4);
                query.set_BusObSearchCriteria (element5);
                ICollection is2 = getApi().get_BusObService().get_SimpleQueryResolver().ResolveQueryToBusObList(query);
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
		
		str4 = String.format("▼%s【%s】(%s)",getApi().get_SystemFunctions().get_CurrentUserName(),
				getApi().get_RoleManager().GetCurrentRoleName(),str4);
		return str4;
	}
	
	
	private Menu CreatePopMenu(Control c){
		Menu popMenu = new Menu(c);
		SelectionAdapter sa = new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				MenuItem mi = (MenuItem) e.widget;
				String status = (String) mi.getData();
				if (status != null)
					getApi().get_AuthenticationService().SetUserStatus(status);
				lblUserPresent.setText(getUserPresenceDisplayStr());
				lblUserPresent.pack();
			}
			
		};
		for(String str: m_htId2Status.keySet()){
			MenuItem mi = new MenuItem(popMenu,SWT.RADIO);
			mi.setText(m_htId2Status.get(str));
			mi.setData(str);
			mi.addSelectionListener(sa);
		}
		return popMenu;
	}
	
	

	/**
	 * Create the actions.
	 */
	private void createActions() {
//		actLeave = new Action("离开", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//				
//			}
//		};
//		actMeeting = new Action("会议中", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//			}
//		};
//		
//		actOutOffice = new Action("不在办公室", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//			}
//		};
//		
//		actBusy = new Action("忙碌", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//			}
//		};
//		
//		actOffline = new Action("离线", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//			}
//		};
//		
//		actOnline = new Action("在线", Action.AS_RADIO_BUTTON){
//			@Override
//			public void run() {
//			}
//		};
		
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
	
    private ImageHolder GetLogoImageHolder()
    {
        ImageHolder imageHolder = null;
        if (this.GetWorkspaceHeaderPanel() != null)
        {
        	ControlDef cdf = this.GetWorkspaceHeaderPanel().GetControlDefByName("Logo");
        	if (cdf !=null && cdf instanceof PictureControlDef){
        		PictureControlDef controlDefByName = (PictureControlDef) cdf;
        		imageHolder = this.GetImageHolder(controlDefByName.get_Image());
        	}

        }
        return imageHolder;
    }
    
    private ImageHolder GetImageHolder(ImageDef imageDef)
    {
        ImageHolder holder = null;
        if ((imageDef != null) && (imageDef.get_ImageRule() != null))
        {
            String strCategoryAndName = this.ResolveImageString(imageDef.get_ImageRule());
            holder = ImageResolver.get_Resolver().ResolveImage(strCategoryAndName);
        }
        return holder;
    }
    
    private String ResolveImageString(RuleDef rule)
    {
    	String str = "";
        if (RuleDef.RuleNotNull(rule))
        {
            try
            {
                EvaluationBundle evalBundle = new EvaluationBundle();
                str = rule.EvaluateToString(evalBundle);
            }
            catch (system.Exception e)
            {
            	e.printStackTrace();
            }
        }
        return str;
    }
	
    private PanelDef GetWorkspaceHeaderPanel()
    {
        return (PanelDef) (getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(PanelDef.get_ClassName(), "WorkspaceHeader")));
    }
    
    private ISiteviewApi getApi(){
    	return ConnectionBroker.get_SiteviewApi();
    }

}
