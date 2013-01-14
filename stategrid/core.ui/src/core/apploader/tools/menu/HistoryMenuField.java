package core.apploader.tools.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Siteview.CollectionUtils;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Presentation.Common.CommandControlItemDef;
import Siteview.Windows.Forms.AdrDefListController;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;
import Siteview.Xml.XmlScope;
import core.apploader.search.comm.CommEnum.HistoryDataType;
import core.apploader.search.comm.CommEnum.ShowFunctionType;
import core.apploader.tools.comm.CommHistoryDataCreate;
import core.apploader.tools.comm.CommHistoryDataLoad;
import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.busobmaint.ScopedParameterList;

public class HistoryMenuField{
	/*
	 * 传递参数
	 */
	private Menu menu;
	private int  index;
	private HistoryDataType historyDataType;
	private ShowFunctionType showFunctionType;
	private IHistoryFieldEvent iHistoryFieldEvent;
	
	/*
	 * 定义参数
	 */
	private Image imageAddr;
	private Image folderImageAddr;
	
	/*
	 * 数据接口 
	 */
	private ISiteviewApi m_api;
	private DefinitionLibrary m_Library;
	
	public HistoryMenuField(Menu menu,int index,HistoryDataType historyDataType,IHistoryFieldEvent iHistoryFieldEvent,ShowFunctionType showFunctionType){
		this.menu=menu;
		this.index=index;
		this.historyDataType=historyDataType;
		this.showFunctionType=showFunctionType;
		this.iHistoryFieldEvent=iHistoryFieldEvent;
		this.m_api=ConnectionBroker.get_SiteviewApi();
		this.m_Library=m_api.get_LiveDefinitionLibrary();
		this.imageAddr=SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(historyDataType.getImageAddr()),0x12,0x12);
		this.folderImageAddr=SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(historyDataType.getFolderImageAddr()),0x12,0x12);
	}
	
	public void fillMenu(){
		
		CommHistoryDataCreate commHistoryDataCreate=new CommHistoryDataCreate(m_api,CommHistoryDataLoad.BUSOBNAME,historyDataType,showFunctionType);
		if(showFunctionType==ShowFunctionType.SHOW_MENU_HISTORY){
			ICollection commandColl=commHistoryDataCreate.getMruHistoryCommands();
			if(commandColl.get_Count()>0){
				try{
					if(showFunctionType!=ShowFunctionType.SHOW_MENU_HISTORY){
						new MenuItem(menu,SWT.SEPARATOR,index++);
					}
					
					for(IEnumerator ienumCommand=commandColl.GetEnumerator();ienumCommand.MoveNext();){
						final String[] busObName=new String[1];
						String firstItem="";
						final IDefinition[] idefinition=new IDefinition[1];
						
						CommandControlItemDef commandControlItemDef=(CommandControlItemDef)ienumCommand.get_Current();
						ICollection collCommandParams=commandControlItemDef.get_Parameters();

						if(historyDataType==HistoryDataType.AUTOTASK){
							if(collCommandParams.get_Count()==1){
								firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
								if(firstItem!=null&&firstItem.length()>0){
									idefinition[0] = GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
				                    if (idefinition != null)
				                    {
				                    	 busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
				                    }
								}
							}
							else{
								int[] scope=new int[1];
								String[] strOwner=new String[1];
								String[] str2=new String[1];
								String[] str3=new String[1];
								String[] str4=new String[1];
								String[] str5=new String[1];
								String[] str6=new String[1];
								String[] strPerspective=new String[1];

				                AdrDefListController.GetCommandParams(collCommandParams,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
				                idefinition[0] = m_Library.GetDefinition(DefRequest.ById(scope[0],strOwner[0],str2[0],historyDataType.getValue()[0],str4[0],strPerspective[0],false));
							
				                busObName[0]=str5[0];
				                
				                if(collCommandParams instanceof ScopedParameterList){
									CommHistoryDataLoad.BUSINESSOBJECT=((ScopedParameterList)collCommandParams).get_BusOb();
								}
							}
						}
						else if(historyDataType==HistoryDataType.SEARCH){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
							if(idefinition==null){
								idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[1],firstItem);
							}
							if(firstItem!=null&&firstItem.length()>0){
								busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
							}
						}
						else if(historyDataType==HistoryDataType.REPORT){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
							if(firstItem!=null&&firstItem.length()>0){
								busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
							}
						}
						else if(historyDataType==HistoryDataType.DASHBOARD){
							int[] scope=new int[1];
							String[] strOwner=new String[1];
							String[] str2=new String[1];
							String[] str3=new String[1];
							String[] str4=new String[1];
							String[] str5=new String[1];
							String[] str6=new String[1];
							String[] strPerspective=new String[1];
							
							if(collCommandParams.get_Count()==1){
								firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
								if(firstItem!=null&&firstItem.length()>0){
									AdrDefListController.ParseCommaDelimittedDefInfo(firstItem,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
				                    IDefinition definition = m_Library.GetDefinition(DefRequest.ById(historyDataType.getValue()[0], str4[0]));
				                    if (definition != null)
				                    {
				                    	idefinition[0] = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition.get_Name(), strPerspective[0], true));
				                    }
								}
								
								busObName[0]=str5[0];
							}
							else{
								AdrDefListController.GetCommandParams(collCommandParams,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
				                IDefinition definition2 = m_Library.GetDefinition(DefRequest.ById(historyDataType.getValue()[0], str4[0]));
				                if (definition2 != null){
				                	idefinition[0] = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition2.get_Name(), strPerspective[0], true));
				                }
				                
				                busObName[0]=str5[0];
							}
						}
						
						MenuItem menuItem=new MenuItem(menu,SWT.NONE,index++);
						menuItem.setText(commandControlItemDef.get_Alias());
						menuItem.setImage(imageAddr);
						
						menuItem.addListener(SWT.Selection,new Listener() {
							public void handleEvent(Event event) {
								try{
									iHistoryFieldEvent.changeSelectEvent(idefinition[0]);
									CommHistoryDataLoad.loadHistoryNav(busObName[0]);
								}
								catch(Exception ex){
									MessageBox msg=new MessageBox(new Shell(),SWT.YES);
				            		msg.setMessage("没有找到相应的记录!("+ex.getMessage()+")");
				            		msg.setText("SiteView应用程序");
				            		msg.open();
								}
							}
						});
					}
					if(showFunctionType!=ShowFunctionType.SHOW_MENU_HISTORY){
						new MenuItem(menu,SWT.SEPARATOR,index++);
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		else{
			HashMap<String,List<CommandControlItemDef>> commandsDefMap=commHistoryDataCreate.getCommandsMap();
			if(commandsDefMap.size()>0){
				new MenuItem(menu,SWT.SEPARATOR,index++);
			}
			for(Iterator<String> commandsIter=commandsDefMap.keySet().iterator();commandsIter.hasNext();){
				String folder=commandsIter.next();
				Menu folderMenu=null;
				if(folder.length()>0){
					MenuItem folderMenuItem=new MenuItem(menu,SWT.CASCADE,index++);
					folderMenuItem.setText(folder);
					folderMenuItem.setImage(folderImageAddr);
					folderMenu=new Menu(folderMenuItem);
					folderMenuItem.setMenu(folderMenu);
				}
				List<CommandControlItemDef> commandControlItemDefs=commandsDefMap.get(folder);
				for(CommandControlItemDef commandControlItemDef:commandControlItemDefs){
					final String[] busObName=new String[1];
					String firstItem="";
					final IDefinition[] idefinition=new IDefinition[1];

					ICollection collCommandParams=commandControlItemDef.get_Parameters();

					if(historyDataType==HistoryDataType.AUTOTASK){
						if(collCommandParams.get_Count()==1){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							if(firstItem!=null&&firstItem.length()>0){
								idefinition[0] = GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
			                    if (idefinition != null)
			                    {
			                    	 busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
			                    }
							}
						}
						else{
							int[] scope=new int[1];
							String[] strOwner=new String[1];
							String[] str2=new String[1];
							String[] str3=new String[1];
							String[] str4=new String[1];
							String[] str5=new String[1];
							String[] str6=new String[1];
							String[] strPerspective=new String[1];

			                AdrDefListController.GetCommandParams(collCommandParams,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
			                idefinition[0] = m_Library.GetDefinition(DefRequest.ById(scope[0],strOwner[0],str2[0],historyDataType.getValue()[0],str4[0],strPerspective[0],false));
						
			                busObName[0]=str5[0];
			                
			                if(collCommandParams instanceof ScopedParameterList){
								CommHistoryDataLoad.BUSINESSOBJECT=((ScopedParameterList)collCommandParams).get_BusOb();
							}
						}
					}
					else if(historyDataType==HistoryDataType.SEARCH){
						firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
						idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
						if(idefinition[0]==null){
							idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[1],firstItem);
						}
						if(firstItem!=null&&firstItem.length()>0){
							busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
						}
					}
					else if(historyDataType==HistoryDataType.REPORT){
						firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
						idefinition[0]=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
						if(firstItem!=null&&firstItem.length()>0){
							busObName[0]=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
						}
					}
					else if(historyDataType==HistoryDataType.DASHBOARD){
						int[] scope=new int[1];
						String[] strOwner=new String[1];
						String[] str2=new String[1];
						String[] str3=new String[1];
						String[] str4=new String[1];
						String[] str5=new String[1];
						String[] str6=new String[1];
						String[] strPerspective=new String[1];
						
						if(collCommandParams.get_Count()==1){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							if(firstItem!=null&&firstItem.length()>0){
								AdrDefListController.ParseCommaDelimittedDefInfo(firstItem,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
			                    IDefinition definition = m_Library.GetDefinition(DefRequest.ById(historyDataType.getValue()[0], str4[0]));
			                    if (definition != null)
			                    {
			                    	idefinition[0] = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition.get_Name(), strPerspective[0], true));
			                    }
							}
							
							busObName[0]=str5[0];
						}
						else{
							AdrDefListController.GetCommandParams(collCommandParams,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
			                IDefinition definition2 = m_Library.GetDefinition(DefRequest.ById(historyDataType.getValue()[0], str4[0]));
			                if (definition2 != null){
			                	idefinition[0] = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition2.get_Name(), strPerspective[0], true));
			                }
			                
			                busObName[0]=str5[0];
						}
					}
					
					MenuItem menuItem=null;
					if(folderMenu==null){
						menuItem=new MenuItem(menu,SWT.NONE,index++);
					}
					else{
						menuItem=new MenuItem(folderMenu,SWT.NONE);
					}
					menuItem.setText(commandControlItemDef.get_Alias());
					menuItem.setImage(imageAddr);
					
					menuItem.addListener(SWT.Selection,new Listener() {
						public void handleEvent(Event event) {
							try{
								iHistoryFieldEvent.changeSelectEvent(idefinition[0]);
								CommHistoryDataLoad.loadHistoryNav(busObName[0]);
							}
							catch(Exception ex){
								MessageBox msg=new MessageBox(new Shell(),SWT.YES);
			            		msg.setMessage("没有找到相应的记录!("+ex.getMessage()+")");
			            		msg.setText("SiteView应用程序");
			            		msg.open();
							}
						}
					});
				}
			}
			if(commandsDefMap.size()>0){
				new MenuItem(menu,SWT.SEPARATOR,index++);
			}
		}

		
	}
	
	private IDefinition GetDefinitionFromCommaDelimittedItem(String strDefClassName,String strItem){
        int[] scope=new int[1];
        String[] str=new String[1];
        String[] str2=new String[1];
        String[] str3=new String[1];
        String[] str4=new String[1];
        String[] str5=new String[1];
        String[] str6=new String[1];
        String[] strPerspective=new String[1];
        AdrDefListController.ParseCommaDelimittedDefInfo(strItem,scope,str,str2,str3,str4,str5,str6,strPerspective);
        return m_api.get_LiveDefinitionLibrary().GetDefinition(DefRequest.ById(scope[0],str[0],str2[0],strDefClassName,str4[0],strPerspective[0], false));
    }
	
	/*
	 * 权限组-添加
	 */
	public boolean HasAddRight(Integer s) {
		if (s != Scope.Unknown){
			return m_api.get_SecurityService().HasModuleItemRight(historyDataType.getSecurity(), XmlScope.CategoryToXmlCategory(s),SecurityRight.Add);
		}
		return false;
	}
}
