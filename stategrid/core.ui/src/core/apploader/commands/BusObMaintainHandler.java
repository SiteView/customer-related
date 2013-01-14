package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import Siteview.QueryGroupDef;
import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.Api.GridDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.VirtualKeyList;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.MsgBox;
import Siteview.Xml.SecurityRight;
import core.apploader.tools.dialog.BussinessSelectObj;
import core.search.form.editor.SearchResultEditor;
import core.search.form.editor.input.SearchResultEditorInput;


public class BusObMaintainHandler extends AbstractHandler{
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (!ConnectionBroker.get_SiteviewApi().get_SecurityService().HasModuleItemRight("Core.Security.Maintenance.BusObMaintenance", SecurityRight.True)){
			MsgBox.ShowWarning("你没有权限：需要‘业务对象维护’权限。");
			return null;
		}
		BussinessSelectObj bussinessSelectObj=new BussinessSelectObj(new Shell());
		int result=bussinessSelectObj.open();
		if(IDialogConstants.OK_ID==result){
			try{
				int results=0;
				String msgInfo="";
				ISiteviewApi m_api=ConnectionBroker.get_SiteviewApi();
				VirtualKeyList[] virtualKeyLists=null;
				GridDef[] girdDefs=null;
				girdDefs=new GridDef[1];
				QueryGroupDef queryGroupDef=new QueryGroupDef();
				queryGroupDef.set_EditMode(true);
				SiteviewQuery siteviewQuery=new SiteviewQuery();
				siteviewQuery.set_BusinessObjectName(bussinessSelectObj.getBusObName());
				girdDefs[0]=m_api.get_Presentation().GetGridDef(bussinessSelectObj.getBusObName());
				
				siteviewQuery.set_InfoToGet(QueryInfoToGet.All);
				queryGroupDef.set_SiteviewQuery(siteviewQuery);
				
				virtualKeyLists=new VirtualKeyList[1];
				
				virtualKeyLists[0]=new VirtualKeyList(m_api,siteviewQuery);
				
				try{
					if(virtualKeyLists[0].get_Count()>0){
						results++;
					}
				}
				catch(Exception ex){
					msgInfo=ex.getMessage();
				}
				
	    		if(results==0){
	    			if(msgInfo.length()!=0){
	    				MessageBox msg=new MessageBox(new Shell(),SWT.YES);
	            		msg.setMessage(msgInfo);
	            		msg.setText("SiteView应用程序");
	            		msg.open();
	    			}
	    			else{
	    				MessageBox msg=new MessageBox(new Shell(),SWT.YES);
	            		msg.setMessage("没有找到相应的记录!");
	            		msg.setText("SiteView应用程序");
	            		msg.open();
	    			}
	    			
	    		}
	    		else{
	    			IWorkbenchPage page=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(new SearchResultEditorInput(queryGroupDef,virtualKeyLists,girdDefs,m_api),SearchResultEditor.ID);
	    		}
			}
			catch(Exception ex){
				MessageBox msg=new MessageBox(new Shell(),SWT.YES);
        		msg.setMessage("没有找到相应的记录!");
        		msg.setText(ex.getMessage());
        		msg.open();
			}
			
		}
		return null;
	}
}
