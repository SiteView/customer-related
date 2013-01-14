package core.apploader;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import siteview.windows.forms.CommandHandlerManger;
import system.Security.Principal.IPrincipal;
import Core.AutoTasks.Api.ServerSideRuleExecutor;
import Core.ui.method.GetIeditorInput;
import Siteview.BaseFormActionEventArgs;
import Siteview.IBusObKeyList;
import Siteview.Operators;
import Siteview.QueryGroupDef;
import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.Api.ApplicationSearchRequestHandler;
import Siteview.Api.ApplicationServiceRequestHandler;
import Siteview.Api.BusinessObject;
import Siteview.Api.Field;
import Siteview.Api.FormActionRequestHandler;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.InteractionHandler;
import Siteview.Api.SearchRequestEventArgs;
import Siteview.Api.ServiceRequestEventArgs;
import Siteview.BusinessLogic.BusObKeyList;
import Siteview.Windows.Forms.AssociatedItemSelectorControlHolder;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.SiteviewPanel;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;
import Siteview.Xml.XmlScope;
import core.apploader.menu.busObMenu;
import core.apploader.views.NavigatorView;
import core.apploader.views.ObjectTreeView;
import core.apploader.views.ServiceCatelogView;
import core.search.form.dialog.SearchResult;
import core.search.form.dialog.SimpleSearchExpress;
import core.ui.EditDashboardPartCommandHandler;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		//layout.addStandaloneView(WorkSpaceHeader.ID, false, IPageLayout.TOP, 0.15f, layout.getEditorArea());
		IFolderLayout navFold = layout.createFolder("Navigator",IPageLayout.LEFT, 0.3f, layout.getEditorArea());
		navFold.addView(NavigatorView.ID);
		
		
		//navFold.addView(ServiceCatelogView.ID);
		layout.getViewLayout(NavigatorView.ID).setCloseable(false);
		
		//判断是否有权限
		if (ConnectionBroker.get_SiteviewApi().get_SecurityService().HasModuleItemRight("Core.Security.TreeBrowser", XmlScope.CategoryToXmlCategory(Scope.Global),SecurityRight.View)
				|| ConnectionBroker.get_SiteviewApi().get_SecurityService().HasModuleItemRight("Core.Security.TreeBrowser", XmlScope.CategoryToXmlCategory(Scope.User),SecurityRight.View)){
			navFold.addView(ObjectTreeView.ID);
			layout.getViewLayout(ObjectTreeView.ID).setCloseable(false);
		}
		//layout.addView(DashboardView.ID, IPageLayout.RIGHT, 0.7f, layout.getEditorArea());
		

		ApplicationServiceRequestHandler asrh = new  ApplicationServiceRequestHandler(){

			@Override
			public void Handler(InteractionHandler sender,
					ServiceRequestEventArgs e) {
				onApplicationServiceRequest(sender, e);
				
			}};
		ApplicationSearchRequestHandler asreqh = new ApplicationSearchRequestHandler(){

			@Override
			public IBusObKeyList Handler(InteractionHandler interactionHandler,
					SearchRequestEventArgs se) {
				return onApplicationSearchRequest(interactionHandler, se);
				//return null;
			}};
		ApplicationSearchRequestHandler assreqh = new ApplicationSearchRequestHandler(){

			@Override
			public IBusObKeyList Handler(InteractionHandler interactionHandler,
					SearchRequestEventArgs se) {
				return onSimpleSearchRequest(interactionHandler, se);
				//return null;
			}};
		FormActionRequestHandler farh = new FormActionRequestHandler(){

			@Override
			public void Handler(InteractionHandler interactionHandler,
					BaseFormActionEventArgs e) {
				onFormActionRequest(interactionHandler, e);
				
			}};
		ConnectionBroker.get_SiteviewApi().get_InteractionHandler().InitializeForApplication(asrh, asreqh, assreqh, farh);
		ConnectionBroker.get_SiteviewApi().get_BusObEventPublisher().Register(new ServerSideRuleExecutor(ConnectionBroker.get_SiteviewApi()));
		
	//	ConnectionBroker.get_SiteviewApi().get_InteractionHandler().DisplayListOfObjects.add(new DisplayListOfObjectsEventHandler(Type.GetType("")));
		
		//loadBackData();
		//Type.GetType(this.getClass().getName()).GetMethods("onApplicationServiceRequest");

		CommandHandlerManger.add("EditDashboardPart", new EditDashboardPartCommandHandler());
	}
	
	private void loadBackData(){
		final ISiteviewApi api = ConnectionBroker.get_SiteviewApi();
		final IPrincipal pal =system.Threading.Thread.get_CurrentPrincipal();
		Job j = new Job(""){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				
				system.Threading.Thread.set_CurrentPrincipal(pal);
				busObMenu m = new busObMenu();
				m.GetNewBusinessObjects(api);
				return Status.OK_STATUS;
			}};

		j.schedule();
		
		
	}
	
	public void onApplicationServiceRequest(Object sender, ServiceRequestEventArgs e)
    {
        System.out.println("OnApplicationServiceRequest");
    }
    
	public IBusObKeyList onApplicationSearchRequest(Object sender, SearchRequestEventArgs e)
	{
		QueryGroupDef gueryGroupDef=e.get_QueryGroupDef();
		SearchResult searchResult=null;
		int openResult=-1;
		
		SiteviewPanel siteviewPanel=null;
		
		if(e.get_Sender()!=null){
			siteviewPanel=((AssociatedItemSelectorControlHolder) e.get_Sender()).get_Owner();
		}
		
//		AssociatedItemSelectorControlHolder assch=(AssociatedItemSelectorControlHolder) e.get_Sender();
//		
//		SiteviewPanel s= assch.get_Owner();
//		assch.get_AssocItemSelectorDef().get_LinkField();
		
		if(siteviewPanel!=null){
			siteviewPanel.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
		}
		
		if(gueryGroupDef==null){
			gueryGroupDef=new QueryGroupDef();
			gueryGroupDef.set_EditMode(true);
			SiteviewQuery siteviewQuery=new SiteviewQuery();
			siteviewQuery.AddBusObQuery(e.get_BusObName(),QueryInfoToGet.All);
			if(e.get_BusObName().equals("Knowledge")){
				Object[] object = GetIeditorInput.getIeditorInput(e.get_HeldApi());
				BusinessObject busob = (BusinessObject) object[1];
				Field subject=busob.GetField("Subject");
				siteviewQuery.set_BusObSearchCriteria(siteviewQuery.get_CriteriaBuilder().FieldAndValueExpression("Knowledge.Subject",Operators.Equals,subject.get_Value().toString()));
			}
			gueryGroupDef.set_SiteviewQuery(siteviewQuery);
		}
		if(gueryGroupDef.get_SiteviewQuery().get_BusObSearchCriteria()==null){
			SimpleSearchExpress simpleSearchExpress=new SimpleSearchExpress(new Shell(), gueryGroupDef,(QueryGroupDef) gueryGroupDef.Clone(), 8);
			simpleSearchExpress.open();
			SiteviewQuery siteViewQuery=simpleSearchExpress.getBackSiteViewQuery();
			if(siteViewQuery!=null&&siteViewQuery.get_BusObSearchCriteria()!=null){
				gueryGroupDef.set_SiteviewQuery(siteViewQuery);
				BusinessObject busOb = ConnectionBroker.get_SiteviewApi().get_BusObService().GetBusinessObject(gueryGroupDef.get_SiteviewQuery());
				searchResult=new SearchResult(new Shell(),gueryGroupDef,busOb,e);
				openResult=searchResult.open();
			}
			else{
				MessageBox msg=new MessageBox(new Shell(),SWT.YES);
				msg.setMessage("没有找到任何相关的数据!");
				msg.setText("SiteView应用程序");
				msg.open();
			}
		}
		else{
			BusinessObject busOb = ConnectionBroker.get_SiteviewApi().get_BusObService().GetBusinessObject(gueryGroupDef.get_SiteviewQuery());
			searchResult=new SearchResult(Display.getCurrent().getActiveShell(),gueryGroupDef,busOb,e);
			searchResult.setQueryGroupDefDefault((QueryGroupDef)gueryGroupDef.Clone());
			openResult=searchResult.open();
		}
		
		if(siteviewPanel!=null){
			siteviewPanel.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
		}
		
		if(openResult==IDialogConstants.OK_ID){
			BusObKeyList ibusObKeyList=new BusObKeyList();
			ibusObKeyList.Add(searchResult.getBusObKey());
			ibusObKeyList.First();
			return ibusObKeyList;
		}

    	return null;
    }
    
    public IBusObKeyList onSimpleSearchRequest(Object sender, SearchRequestEventArgs e){
    	System.out.println("onSimpleSearchRequest");
    	return null;
    }
    
    public void onFormActionRequest(Object sender, BaseFormActionEventArgs e){
    	System.out.println("onFormActionRequest");
    }
}
