package core.apploader.tools.service.impl;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.search.form.dialog.SearchParamDialog;
import core.search.form.editor.SearchResultEditor;
import core.search.form.editor.input.SearchResultEditorInput;

import system.Collections.ArrayList;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.MultipleBusObQueryGroupDef;
import Siteview.QueryGroupDef;
import Siteview.SiteviewQuery;
import Siteview.Api.GridDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.VirtualKeyList;
import Siteview.Windows.Forms.ConnectionBroker;

public class SearchHistoryFieldEvent implements IHistoryFieldEvent{
	
	private ISiteviewApi m_api;
	
	public SearchHistoryFieldEvent(){
		this.m_api=ConnectionBroker.get_SiteviewApi();
	}
	
	public void changeSelectEvent(IDefinition idefinition){
		int results=0;
		String msgInfo="";
		VirtualKeyList[] virtualKeyLists=null;
		GridDef[] girdDefs=null;
		
		try{
			if(idefinition instanceof MultipleBusObQueryGroupDef){
				MultipleBusObQueryGroupDef mulQueryGroupDef=(MultipleBusObQueryGroupDef)idefinition;
				ArrayList siteViewQueryList= mulQueryGroupDef.get_SiteviewQueryArray();
				
				virtualKeyLists=new VirtualKeyList[siteViewQueryList.get_Count()];
				girdDefs=new GridDef[siteViewQueryList.get_Count()];
				
				for(int i=0,j=siteViewQueryList.get_Count();i<j;i++){
					SiteviewQuery getSiteViewQuery=(SiteviewQuery)siteViewQueryList.get_Item(i);
					virtualKeyLists[i]=new VirtualKeyList(m_api,getSiteViewQuery);
					girdDefs[i]=m_api.get_Presentation().GetGridDef(getSiteViewQuery.get_BusinessObjectName());
					
					try{
						if(virtualKeyLists[i].get_Count()>0){
							results++;
						}
					}
					catch(Exception ex){
						msgInfo=ex.getMessage();
					}
					
				}
			}
			else{
				QueryGroupDef queryGroupDef=(QueryGroupDef)idefinition;
				SiteviewQuery getSiteViewQuery=queryGroupDef.get_SiteviewQuery();
				
				if(getSiteViewQuery.get_SearchParameters()!=null&&getSiteViewQuery.get_SearchParameters().get_Count()>0){
					
					SearchParamDialog searchParamDialog = new SearchParamDialog(Display.getCurrent().getActiveShell(),queryGroupDef);
					int result = searchParamDialog.open();
					if(result != Dialog.OK){
						return;
					}
				}
				
				virtualKeyLists=new VirtualKeyList[1];
				girdDefs=new GridDef[1];
				
				virtualKeyLists[0]=new VirtualKeyList(m_api,getSiteViewQuery);
				girdDefs[0]=(GridDef) m_api.get_LiveDefinitionLibrary().GetDefinition(DefRequest.ById(GridDef.get_ClassName(),queryGroupDef.get_DefaultGridDefId()));
				if(girdDefs[0]==null){
					girdDefs[0]=m_api.get_Presentation().GetGridDef(getSiteViewQuery.get_BusinessObjectName());
				}
				try{
					if(virtualKeyLists[0].get_Count()>0){
						results++;
					}
				}
				catch(Exception ex){
					msgInfo=ex.getMessage();
				}
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
				page.openEditor(new SearchResultEditorInput((QueryGroupDef)idefinition,virtualKeyLists,girdDefs,m_api),SearchResultEditor.ID);
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
