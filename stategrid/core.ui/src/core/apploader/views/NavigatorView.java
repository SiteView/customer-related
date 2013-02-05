package core.apploader.views;

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Core.Dashboards.DashboardDef;
import Core.UI.NavigatorDef;
import Siteview.DefRequest;
import Siteview.LegalUtils;
import Siteview.MultipleBusObQueryGroupDef;
import Siteview.QueryGroupDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.RoleDef;
import Siteview.Api.WorkspaceDef;
import Siteview.Presentation.Common.CommandControlDef;
import Siteview.Presentation.Common.CommandControlGroupDef;
import Siteview.Presentation.Common.CommandControlItemDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ImageHolder;
import Siteview.Windows.Forms.MsgBox;
import Siteview.Windows.Forms.WindowsForms;
import Siteview.Xml.Scope;
import core.apploader.search.comm.CommEnum.HistoryDataType;
import core.apploader.tools.comm.CommHistoryDataLoad;
import core.apploader.tools.composite.HistoryNavField;
import core.apploader.tools.service.impl.AutotaskHistoryFieldEvent;
import core.apploader.tools.service.impl.DashboardHistoryFieldEvent;
import core.apploader.tools.service.impl.ReportingHistoryFieldEvent;
import core.apploader.tools.service.impl.SearchHistoryFieldEvent;
import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.busobmaint.BusObMaintInput;
import core.busobmaint.BusObNewInput;
import core.editors.DashboardInput;
import core.editors.DashboardPanel;
import core.search.form.composite.SearchNavField;
import core.search.form.editor.input.SearchResultEditorInput;


public class NavigatorView extends ViewPart {

	public static final String ID = "core.apploader.views.NavigatorView"; //$NON-NLS-1$

	ExpandBar expandBar = null;
	
	public NavigatorView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		expandBar = new ExpandBar(container,SWT.V_SCROLL);

		createActions();
		initializeToolBar();
		initializeMenu();
		
		initNavigator(expandBar);
		expandBar.layout(true);
		
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener(){

			@Override
			public void partActivated(IWorkbenchPart part) {
				//System.out.println("partActivated:"+ part.getTitle());
				
				if (part instanceof IEditorPart){
					IEditorPart ePart = (IEditorPart) part;
					if (ePart.getEditorInput() instanceof BusObMaintInput){
						BusObMaintInput busObInput = (BusObMaintInput) ePart.getEditorInput();
						CommHistoryDataLoad.loadHistoryNav(busObInput.getBusobName());
						System.out.println(ePart.getEditorInput());
						WindowsForms.setCurrentObName(busObInput.getBusobName());
					}
					else if (ePart.getEditorInput() instanceof BusObNewInput){
						BusObNewInput busObNewInput = (BusObNewInput) ePart.getEditorInput();
						CommHistoryDataLoad.loadHistoryNav(busObNewInput.getBusobName());
						WindowsForms.setCurrentObName(busObNewInput.getBusobName());
					}
					else if (ePart.getEditorInput() instanceof DashboardInput){
						DashboardInput dashInput = (DashboardInput) ePart.getEditorInput();
						CommHistoryDataLoad.loadHistoryNav(dashInput.getDashboardDef().get_Name());
						WindowsForms.setCurrentObName(dashInput.getDashboardDef().get_Name());
						
						//
//						if (dashInput.isHided()){
//							final DashboardPanel dp = (DashboardPanel)ePart;
//							//dp.setPartProperty("Refresh", "true");
//							
//						}
//						dashInput.setHided(false);
					}
					else if(ePart.getEditorInput() instanceof SearchResultEditorInput){
						SearchResultEditorInput dashInput = (SearchResultEditorInput) ePart.getEditorInput();
						QueryGroupDef queryGroupDef=dashInput.getQueryGroupDef();
						if(!(queryGroupDef instanceof MultipleBusObQueryGroupDef)){
							String busName=dashInput.getQueryGroupDef().get_SiteviewQuery().get_BusinessObjectName();
							if(busName!=null&&!busName.trim().equals("")){
								CommHistoryDataLoad.loadHistoryNav(busName);
								WindowsForms.setCurrentObName(busName);
							}
							
						}
						else{
							
						}
					}
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {
				//System.out.println("partBroughtToTop:"+ part.getTitle());
				
			}

			@Override
			public void partClosed(IWorkbenchPart part) {
				//System.out.println("partClosed:"+ part.getTitle());
				
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
				//System.out.println("partDeactivated:"+ part.getTitle());
				
//				if (part instanceof DashboardPanel){
//					DashboardPanel dp = (DashboardPanel)part;
//					DashboardInput dashInput = (DashboardInput) dp.getEditorInput();
//					
//					dashInput.setHided(true);
//				}
			}

			@Override
			public void partOpened(IWorkbenchPart part) {
				//System.out.println("partOpened:"+ part.getTitle());
				
			}});
		
	}
	
	private void initNavigator(Composite container) {
		
		WorkbenchWindow window = (WorkbenchWindow)Workbench.getInstance().getActiveWorkbenchWindow();
		MenuManager windowMenuManager=window.getMenuManager();
		//container.setLayout(new FillLayout());
		NavigatorDef navDef = (NavigatorDef) getApi().get_ApplicationDefinitionRepository().GetInternalDefinition("NavigatorDef", "Main");
		navDef.ExpandReferenceItems(getApi().get_LiveDefinitionLibrary());
		ICollection cmds = navDef.get_CommandControlGroup().get_Commands();
	    IEnumerator it = cmds.GetEnumerator();
	    while(it.MoveNext()){
	        CommandControlDef cmddef = (CommandControlDef)it.get_Current();
	        boolean flag = false;
	        
	        
	        
	        if (cmddef.get_IsGroup()){
	        	
//	        	if (cmddef.get_Name().equals("Searches")){
//	        		continue;
//	        	}
	        	//System.out.println(cmddef.get_Name());
	            CommandControlGroupDef groupdef = (CommandControlGroupDef)cmddef;
	            final ExpandItem expandItem = new ExpandItem((ExpandBar) container, SWT.NONE);
	            final ScrolledComposite comp1 = new ScrolledComposite(container,SWT.NONE);
	            expandItem.setControl(comp1);
	            //comp1.setLayout(new FillLayout()); 
	            expandItem.setText(groupdef.get_Alias());
	            
	            //System.out.println(groupdef.get_Alias()+":"+cmddef.get_Name());
	           
	            int height = 0;
	            
	            if (cmddef.get_Name().equals("General")){
	            	comp1.setLayout(new FillLayout());
	            	
	            	Display.getCurrent().asyncExec(new Runnable(){

						@Override
						public void run() {
							Table tb = loadworkspace(comp1);
							tb.setBackground(new Color(null,240,243,249));
							comp1.setContent(tb);
							comp1.pack();
							expandItem.setHeight(tb.getSize().y);
			            	expandItem.setExpanded(true);
						}});
	            	
	            	//comp1.setLocation(20, 0);
	            	//comp1.setMinHeight(200);
	            	
	        		continue;
	        	}
	            else if(cmddef.get_Name().equals("Searches")){
	            	Composite composite=new Composite(comp1,SWT.NONE);
//	            	SearchNavField searchField=new SearchNavField(composite,expandItem,SWT.NONE);
//	            	searchField.setLocation(0, 0);
//	            	searchField.pack();
	            	
	            	IHistoryFieldEvent ihistoryFieldEvent=new SearchHistoryFieldEvent();//查询的历史数据事件接口实现
	            	CommHistoryDataLoad.queryGroupNavComposite=new HistoryNavField(composite,SWT.FLAT,expandItem,HistoryDataType.SEARCH,ihistoryFieldEvent,0);
	            	CommHistoryDataLoad.queryGroupNavComposite.setLocation(0,0);
	            	CommHistoryDataLoad.queryGroupNavComposite.pack();
	            	composite.pack();
	            	comp1.pack();
	            	comp1.setContent(composite);
	            	expandItem.setExpanded(true);
	        		continue;
	            }
	            else if(cmddef.get_Name().equals("QuickActions")){
	            	comp1.setLayout(new FillLayout());
	            	IHistoryFieldEvent ihistoryFieldEvent= new AutotaskHistoryFieldEvent();
	            	CommHistoryDataLoad.autoTaskNavComposite=new HistoryNavField(comp1,SWT.NONE,expandItem,HistoryDataType.AUTOTASK,ihistoryFieldEvent);
	            	comp1.setContent(CommHistoryDataLoad.autoTaskNavComposite);
	            	expandItem.setExpanded(true);
	            	continue;
	            }
	            else if(cmddef.get_Name().equals("Reports")){
	            	comp1.setLayout(new FillLayout());
	            	IHistoryFieldEvent ihistoryFieldEvent= new ReportingHistoryFieldEvent();
	            	CommHistoryDataLoad.reportNavComposite=new HistoryNavField(comp1,SWT.NONE,expandItem,HistoryDataType.REPORT,ihistoryFieldEvent);
	            	comp1.setContent(CommHistoryDataLoad.reportNavComposite);
	            	expandItem.setExpanded(true);
	            	continue;
	            }
	            else if(cmddef.get_Name().equals("Dashboards")){
	            	comp1.setLayout(new FillLayout());
	            	IHistoryFieldEvent ihistoryFieldEvent=new DashboardHistoryFieldEvent();
	            	CommHistoryDataLoad.dashbordNavComposite=new HistoryNavField(comp1,SWT.NONE,expandItem,HistoryDataType.DASHBOARD,ihistoryFieldEvent);
	            	comp1.setContent(CommHistoryDataLoad.dashbordNavComposite);
	            	expandItem.setExpanded(true);
	            	continue;
	            }
	            
	            //expandItem.setHeight(50);
	           
	            IEnumerator it2 = groupdef.get_Commands().GetEnumerator();
	            while(it2.MoveNext()){
	                CommandControlDef cdf = (CommandControlDef)it2.get_Current();
	                if (!cdf.get_IsStandInItem()){
	                    cdf.ExpandReferenceItem(ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary());
	                    CommandControlItemDef ccid = (CommandControlItemDef)cdf;
	                    Composite rowComp = new Composite(comp1,SWT.NONE);
	                    try{
	                    	//System.out.println(ccid.get_NormalImage());
		                    ImageHolder ih = ImageResolver.get_Resolver().ResolveImage(ccid.get_NormalImage());
		                    Label img = new Label(rowComp,SWT.NONE);
		                    img.setLocation(0, 0);
		                    if (ih !=null && (ih.get_HasIcon() || ih.get_HasImage()))
		                    	img.setImage(SwtImageConverter.ConvertToSwtImage(ih,20,20));
		                    img.pack();
	                    }catch(Exception e){
	                    	e.printStackTrace();
	                    }
	                    
	                    Link lk = new Link(rowComp,SWT.NONE);
	                   
	                    lk.setText("<a>" +ccid.get_Alias()+ "</a>");
	                    lk.setLocation(22, 5);
	                    lk.pack();
	                    lk.setData(ccid.get_Command());
	                    lk.addSelectionListener(new NaviCommandHandler());
	                    height += 26;
	                    
	                    continue;
	                }
	                flag = true;
	            }
	            expandItem.setHeight(height+10);
	            expandItem.setExpanded(true);
	            
	            
	        }
	    }
	    
	}
	
	
	private Table loadworkspace(Composite container){
		TableViewer tvWks = new  TableViewer(container,SWT.FULL_SELECTION);
		TableViewerColumn tvc = new TableViewerColumn(tvWks,SWT.NONE);
		tvc.getColumn().setWidth(4000);
		//tvc.getColumn().setText("aaaaa");
		//tvWks.getTable().setCursor(new Cursor(Display.getCurrent(),SWT.CURSOR_HAND));

		tvc.setLabelProvider(new ColumnLabelProvider(){

			@Override
			public Image getImage(Object element) {
				WorkspaceDef wksd = (WorkspaceDef) element;
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(wksd.get_AssociatedImage()),24,24);
			}

			@Override
			public String getText(Object element) {
				WorkspaceDef wksd = (WorkspaceDef) element;
				return wksd.get_Alias();
			}

			});

		
		tvWks.setContentProvider(new IStructuredContentProvider(){

			@Override
			public void dispose() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				system.Collections.ArrayList wks = (system.Collections.ArrayList) inputElement;
				List<WorkspaceDef> lstwksd = new Vector<WorkspaceDef>();
				for(int i = 0; i < wks.get_Count();i++){
					String str = (String)wks.get_Item(i);
					WorkspaceDef wksd = (WorkspaceDef) getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(WorkspaceDef.get_ClassName(), str));
					if (wksd != null){
						lstwksd.add(wksd);
					}
				}
				return lstwksd.toArray();
			}});
//		tvWks.setLabelProvider(new LabelProvider(){
//
//			@Override
//			public Image getImage(Object element) {
//				WorkspaceDef wksd = (WorkspaceDef) element;
//				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(wksd.get_AssociatedImage()),24,24);
//			}
//
//			@Override
//			public String getText(Object element) {
//				WorkspaceDef wksd = (WorkspaceDef) element;
//				return wksd.get_Alias();
//			}});
		
		//取角色
		RoleDef currentRole = (RoleDef) getApi().get_RoleManager().GetCurrentRole();
		system.Collections.ArrayList wks = currentRole.get_WorkspaceList();
		tvWks.addSelectionChangedListener(new ISelectionChangedListener(){

			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection sel = (IStructuredSelection) e.getSelection();
				if (sel.getFirstElement()!=null){
					WorkspaceDef wkd = (WorkspaceDef) sel.getFirstElement();
					onSelectWorkSpace(wkd);
				}
				
			}});
		tvWks.setInput(wks);
		tvWks.getTable().pack();
		getSite().setSelectionProvider(tvWks);
		
		for(int i = 0 ; i < wks.get_Count(); i++){
			String wkd = (String) wks.get_Item(i);
			if (currentRole.get_DefaultWorkspace().equals(wkd)){
				tvWks.getTable().setSelection(i);
				IStructuredSelection sel = (IStructuredSelection) tvWks.getSelection();
				onSelectWorkSpace((WorkspaceDef) sel.getFirstElement());
				break;
			}
		}
		return tvWks.getTable();
		
//		Table tbWks = new Table(container,SWT.FULL_SELECTION);
//		tbWks.setHeaderVisible(false);
//		tbWks.setLinesVisible(false);
//		TableColumn tc = new TableColumn(tbWks,SWT.NONE);
//		tc.setWidth(4000);
////		TableColumn tc2 = new TableColumn(tbWks,SWT.NONE);
////		tc2.setWidth(140);
//
//		//取角色
//		RoleDef currentRole = (RoleDef) getApi().get_RoleManager().GetCurrentRole();
//		system.Collections.ArrayList wks = currentRole.get_WorkspaceList();
//		
//		for(int i = 0; i < wks.get_Count();i++){
//			String str = (String)wks.get_Item(i);
//			WorkspaceDef wksd = (WorkspaceDef) getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(WorkspaceDef.get_ClassName(), str));
//			if (wksd.get_BusinessObjectName()!=null && !wksd.get_BusinessObjectName().equals("")){
//				TableItem ti = new TableItem(tbWks,SWT.NONE);
//				ti.setText(wksd.get_Alias());
//				ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(wksd.get_AssociatedImage()),24,24));
//				//ti.setWidth(220);
//				ti.setData(wksd);
////				ti.addSelectionListener(new SelectionAdapter(){
////
////					@Override
////					public void widgetSelected(SelectionEvent e) {
////						ToolItem it = (ToolItem) e.item;
////						WorkspaceDef wkd = (WorkspaceDef) it.getData();
////						onSelectWorkSpace(wkd);
////					}});
//			}
//			
//		}
//		tbWks.pack();
//		return tbWks;

	}
	
	

	protected void onSelectWorkSpace(final WorkspaceDef wkd) {
		//Cursor oldCursor = this.getSite().getShell().getCursor();
		BusyIndicator.showWhile(Display.getDefault(), new Runnable(){

			@Override
			public void run() {
				if (wkd != null){
					//DashboardDef def = (DashboardDef) getApi().get_LiveDefinitionLibrary().GetDefinition(
					//		DefRequest.ById(wkd.get_Scope(), wkd.get_ScopeOwner(),wkd.get_LinkedTo(),DashboardDef.get_ClassName(),wkd.get_DashboardId(),wkd.get_Perspective(),false));
	
					DashboardDef def = (DashboardDef) getApi().get_LiveDefinitionLibrary().GetDefinition(
							DefRequest.ByName(Scope.User, getApi().get_SystemFunctions().get_CurrentLoginId(),
									"",DashboardDef.get_ClassName(),wkd.get_DashboardName(),"(Role)",true));
					if (def == null){
						MsgBox.Show(getSite().getShell(), "没有找到仪表盘：" + wkd.get_DashboardName(), LegalUtils.get_MessageBoxCaption());
						return;
					}
					
					DashboardPanel.open(getApi(), def);
//					try {
//
//						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new DashboardInput(getApi(),def), DashboardPanel.ID);
//						//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DashboardView.ID);
//					} catch (PartInitException err) {
//						// TODO Auto-generated catch block
//						err.printStackTrace();
//					}

				}
				
			}});
		//this.getSite().getShell().setCursor(new Cursor(Display.getCurrent(),SWT.CURSOR_WAIT));
		
		//this.getSite().getShell().setCursor(oldCursor);
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
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
	
    private ISiteviewApi getApi(){
    	return ConnectionBroker.get_SiteviewApi();
    }

}
