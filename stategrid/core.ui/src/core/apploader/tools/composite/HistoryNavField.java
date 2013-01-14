package core.apploader.tools.composite;

import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import Siteview.CollectionUtils;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Presentation.Common.CommandControlItemDef;
import Siteview.Windows.Forms.AdrDefListController;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Xml.Scope;
import core.apploader.search.comm.CommEnum.HistoryDataType;
import core.apploader.search.comm.CommEnum.ShowFunctionType;
import core.apploader.tools.comm.CommHistoryDataCreate;
import core.apploader.tools.comm.CommHistoryDataLoad;
import core.apploader.tools.service.inter.IHistoryFieldEvent;
import core.busobmaint.ScopedParameterList;
public class HistoryNavField extends Composite{
	
	/*
	 * 传递数据
	 */
	private ExpandItem expandItem;
	private int expandMargin;
	private HistoryDataType historyDataType;
	private IHistoryFieldEvent ihistoryFieldEvent;
	private Composite fatherParent;
	
	/*
	 * 页面控件
	 */
	private TableViewer tableViewer;
	private Image       showImageIcon;
	
	/*
	 * 遍历定义
	 */
	private Job loadFieldJob;
	
	/*
	 * 数据接口
	 */
	private ISiteviewApi m_api;
	private DefinitionLibrary m_Library;
	
	/*
	 * 返回参数
	 */
	public IHistoryFieldEvent getIhistoryFieldEvent(){
		return ihistoryFieldEvent;
	}
	
	/*
	 * 构造函数
	 */
	public HistoryNavField(Composite parent,int style,ExpandItem expandItem,HistoryDataType historyDataType,IHistoryFieldEvent ihistoryFieldEvent) {
		this(parent,style,expandItem,historyDataType,ihistoryFieldEvent,-10);
	} 
	
	public HistoryNavField(Composite parent,int style,ExpandItem expandItem,HistoryDataType historyDataType,IHistoryFieldEvent ihistoryFieldEvent,int expandMargin) {
		super(parent, style);
		this.fatherParent=parent;
		this.expandItem=expandItem;
		this.historyDataType=historyDataType;
		this.ihistoryFieldEvent=ihistoryFieldEvent;
		this.expandMargin=expandMargin;
		this.m_api=ConnectionBroker.get_SiteviewApi();
		this.m_Library=m_api.get_LiveDefinitionLibrary();
		loadUI();
		loadHistoryNavData();
		addTableSelectChangeEvent();
	}
	
	public void loadUI(){
		this.setLayout(new FillLayout());
		this.tableViewer = new TableViewer(this, SWT.NONE);
		Table table = tableViewer.getTable();
		table.setBackground(new Color(null,240,243,249));
		TableViewerColumn tableColumn = new TableViewerColumn(tableViewer,SWT.NONE);
		tableColumn.getColumn().setWidth(400);
		
		showImageIcon=SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(historyDataType.getImageAddr()),0x12,0x12);
		
		tableViewer.setContentProvider(new IStructuredContentProvider(){

			public void dispose() {}

			public void inputChanged(Viewer viewer, Object oldInput,Object newInput) {}

			public Object[] getElements(Object inputElement) {
				system.Collections.ArrayList commandGroupDefs = (system.Collections.ArrayList) inputElement;
				java.util.List<CommandControlItemDef> commandGroupDefList = new Vector<CommandControlItemDef>();
				for(int i = 0; i < commandGroupDefs.get_Count();i++){
					CommandControlItemDef commandGroupDef = (CommandControlItemDef)commandGroupDefs.get_Item(i);
					commandGroupDefList.add(commandGroupDef);
				}
				return commandGroupDefList.toArray();
			}
		});
		
		tableColumn.setLabelProvider(new ColumnLabelProvider(){
			public Image getImage(Object element) {
				return showImageIcon;
			}

			public String getText(Object element) {
				CommandControlItemDef commandControlItemDef = (CommandControlItemDef) element;
				return commandControlItemDef.get_Alias();
			}
		});
		
		initHistoryNavData();
	}
	
	public void initHistoryNavData(){
		loadFieldJob=new Job("loadFieldJob") {
			
			public boolean shouldSchedule() {
				boolean isContinue=!getDisplay().isDisposed();
				if(!isContinue){
					MessageDialog.openError(getShell(),"出错了","加载视图页面出现问题!");
				}
				return isContinue;
			}

			public boolean shouldRun() {
				boolean isContinue=!getDisplay().isDisposed();
				if(!isContinue){
					MessageDialog.openError(getShell(),"出错了","加载视图页面出现问题!");
				}
				return isContinue;
			}
			
			protected IStatus run(IProgressMonitor arg0) {
				final CommHistoryDataCreate historyDataCreate=new CommHistoryDataCreate(m_api,CommHistoryDataLoad.BUSOBNAME,historyDataType,ShowFunctionType.SHOW_NAVIGATOR);
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						tableViewer.setInput(historyDataCreate.getCommands());
						pack();
						fatherParent.pack();
						if(tableViewer.getTable().getItemCount()>0){
							expandItem.setHeight(getSize().y+expandMargin);
							expandItem.setExpanded(true);
						}
						else{
							expandItem.setHeight(10+expandMargin);
							expandItem.setExpanded(expandMargin>0);
						}
					}
				});
				
				return Status.OK_STATUS;
			}
		};
	}
	
	public void loadHistoryNavData(){
		loadFieldJob.schedule();
	}
	
	public void loadHistoryNavData(long waitTime){
		loadFieldJob.schedule(waitTime);
	}
	
	public void addTableSelectChangeEvent(){
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent selectionChangedEvent) {
				IStructuredSelection selection = (IStructuredSelection) selectionChangedEvent.getSelection();
				Object objValue=selection.getFirstElement();
				if(objValue!=null){
					try{
						String busObName="";
						String firstItem="";
						IDefinition idefinition=null;
						
						CommandControlItemDef commandControlItemDef=(CommandControlItemDef)objValue;
						ICollection collCommandParams=commandControlItemDef.get_Parameters();

						if(historyDataType==HistoryDataType.AUTOTASK){
							if(collCommandParams.get_Count()==1){
								firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
								if(firstItem!=null&&firstItem.length()>0){
									idefinition = GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
				                    if (idefinition != null)
				                    {
				                    	 busObName=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
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
				                idefinition = m_Library.GetDefinition(DefRequest.ById(scope[0],strOwner[0],str2[0],historyDataType.getValue()[0],str4[0],strPerspective[0],false));
							
				                busObName=str5[0];
							}
							if(collCommandParams instanceof ScopedParameterList){
								CommHistoryDataLoad.BUSINESSOBJECT=((ScopedParameterList)collCommandParams).get_BusOb();
							}
						}
						else if(historyDataType==HistoryDataType.SEARCH){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							idefinition=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
							if(idefinition==null){
								idefinition=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[1],firstItem);
							}
							if(firstItem!=null&&firstItem.length()>0){
								busObName=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
							}
						}
						else if(historyDataType==HistoryDataType.REPORT){
							firstItem=CollectionUtils.GetFirstItem(commandControlItemDef.get_Parameters()).toString();
							idefinition=GetDefinitionFromCommaDelimittedItem(historyDataType.getValue()[0],firstItem);
							if(firstItem!=null&&firstItem.length()>0){
								busObName=AdrDefListController.ParseCommaDelimittedDefInfoForLinkedTo(firstItem);
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
				                    	idefinition = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition.get_Name(), strPerspective[0], true));
				                    }
								}
								
								busObName=str5[0];
							}
							else{
								AdrDefListController.GetCommandParams(collCommandParams,scope,strOwner,str2,str3,str4,str5,str6,strPerspective);
				                IDefinition definition2 = m_Library.GetDefinition(DefRequest.ById(historyDataType.getValue()[0], str4[0]));
				                if (definition2 != null){
				                	idefinition = m_Library.GetDefinition(DefRequest.ByName(Scope.User,m_api.get_SystemFunctions().get_CurrentLoginId(),"",historyDataType.getValue()[0], definition2.get_Name(), strPerspective[0], true));
				                }
				                
				                busObName=str5[0];
							}
						}
						ihistoryFieldEvent.changeSelectEvent(idefinition);
						CommHistoryDataLoad.loadHistoryNav(busObName);
					}
					catch(Exception ex){
						MessageBox msg=new MessageBox(new Shell(),SWT.YES);
	            		msg.setMessage("没有找到相应的记录!("+ex.getMessage()+")");
	            		msg.setText("SiteView应用程序");
	            		msg.open();
					}
				}
			}
		});
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
	
}
