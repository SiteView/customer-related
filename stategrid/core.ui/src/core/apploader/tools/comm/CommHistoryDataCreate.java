package core.apploader.tools.comm;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.demo.Demo;

import core.apploader.search.comm.CommEnum.HistoryDataType;
import core.apploader.search.comm.CommEnum.ShowFunctionType;

import system.Collections.ArrayList;
import system.Collections.CaseInsensitiveComparer;
import system.Collections.Hashtable;
import system.Collections.ICollection;
import system.Collections.IComparer;
import system.Collections.IDictionary;
import system.Collections.IEnumerator;
import system.Collections.IList;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.IMru;
import Siteview.ISecurity;
import Siteview.PlaceHolder;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Presentation.Common.CommandControlGroupDef;
import Siteview.Presentation.Common.CommandControlItemDef;
import Siteview.Presentation.Common.MenuDef;
import Siteview.Windows.Forms.AdrDefListController;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;

public class CommHistoryDataCreate {
	/*
	 * 接口定义
	 */
	private ISiteviewApi          m_api;
	private DefinitionLibrary     m_lLibrary;
	
	/*
	 * 传递参数
	 */
	private String businessObjectContext;
	private String[] strDefClassNames;
	private String strFlag;
	private String strSecurityRight;
	private String strContext;
	private String strImage;
	private String strOverlay;
	private String strCommand;
	
	/*
	 * 构造方法
	 */
	public CommHistoryDataCreate(){
		
	}
	
	public CommHistoryDataCreate(ISiteviewApi isiteViewApi,String businessObjectContext,HistoryDataType historyDataType,ShowFunctionType showFunctionType){
		this.businessObjectContext=businessObjectContext;
		this.m_api=isiteViewApi;
		this.m_lLibrary=m_api.get_LiveDefinitionLibrary();
		
		this.strDefClassNames=historyDataType.getValue();
		this.strFlag=showFunctionType.getValue();
		
		if(historyDataType==HistoryDataType.AUTOTASK){
			this.strSecurityRight="Siteview.Security.AutoTasks";
			this.strImage="[image]Core#Images.Icons.QuickAction.png";
			this.strOverlay="";
			this.strCommand="AutoTask.RUN";
			
			if(showFunctionType==ShowFunctionType.SHOW_MENU){
				this.strContext="QuickActionDIContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_NAVIGATOR){
				this.strContext="QuickActionNavContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_MENU_HISTORY){
				this.strContext="QuickActionMRUContext";
			}
		}
		else if(historyDataType==HistoryDataType.SEARCH){
			this.strSecurityRight="Siteview.Security.SearchGroups";
			this.strImage="[IMAGE]Core#Images.Icons.Search.png";
			this.strOverlay="";
			this.strCommand="Search.RUN";
			
			if(showFunctionType==ShowFunctionType.SHOW_MENU){
				this.strContext="SearchGroupsDIContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_NAVIGATOR){
				this.strContext="SearchNavContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_MENU_HISTORY){
				this.strContext="SearchMRUContext";
			}
		}
		else if(historyDataType==HistoryDataType.REPORT){
			this.strSecurityRight="Core.Security.Reports";
			this.strImage="[image]Core#Images.Icons.Report.png";
			this.strOverlay="";
			this.strCommand="Reporting.RUN";
			
			if(showFunctionType==ShowFunctionType.SHOW_MENU){
				this.strContext="ReportingDIContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_NAVIGATOR){
				this.strContext="ReportNavContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_MENU_HISTORY){
				this.strContext="ReportMRUContext";
			}
		}
		else if(historyDataType==HistoryDataType.DASHBOARD){
			this.strSecurityRight="Core.Security.Dashboards";
			this.strImage="[IMAGE]Core#Images.Icons.Column.png";
			this.strOverlay="";
			this.strCommand="Dashboard.SHOW";
			
			if(showFunctionType==ShowFunctionType.SHOW_MENU){
				this.strContext="DashboardDIContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_NAVIGATOR){
				this.strContext="DashboardNavContext";
			}
			else if(showFunctionType==ShowFunctionType.SHOW_MENU_HISTORY){
				this.strContext="DashboardMRUContext";
			}
		}
	}
	
	public ICollection getCommands(){
		if(strDefClassNames==null){
			try{
				throw new Exception("无法获取到业务对象,请确认业务对象名称或内容是否正确!");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		ICollection icoll=GetCollectionOfCommaDelimittedItemsWithFlag(businessObjectContext,strDefClassNames,strFlag);
		MenuDef def=BuildScopedMruMenu(icoll,strSecurityRight,strContext,strImage,strOverlay,strCommand,strDefClassNames[0]);
		CommandControlGroupDef commandControlGroupDef=def.get_CommandControlGroup();
		return commandControlGroupDef.get_Commands();
	}
	
	public HashMap<String,List<CommandControlItemDef>> getCommandsMap(){
		if(strDefClassNames==null){
			try{
				throw new Exception("无法获取到业务对象,请确认业务对象名称或内容是否正确!");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		ICollection icoll=GetCollectionOfCommaDelimittedItemsWithFlag(businessObjectContext,strDefClassNames,strFlag);
		return BuildScopedMruMenu2HashMap(icoll,strSecurityRight,strContext,strImage,strOverlay,strCommand,strDefClassNames[0]);
	}
	
	public ICollection getMruHistoryCommands(){
		if(strDefClassNames==null){
			try{
				throw new Exception("无法获取到业务对象,请确认业务对象名称或内容是否正确!");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}

		IMru mru = m_api.get_SettingsService().get_Mru();
		ICollection icoll=mru.GetItems(strDefClassNames[0]+".run");
		MenuDef def=BuildScopedMruMenu(icoll,strSecurityRight,strContext,strImage,strOverlay,strCommand,strDefClassNames[0]);
		CommandControlGroupDef commandControlGroupDef=def.get_CommandControlGroup();
		return commandControlGroupDef.get_Commands();
	}
	
	public ICollection getCommands(String businessObjectContext,String[] strDefClassNames,String strFlag,String strSecurityRight,String strContext,String strImage,String strOverlay,String strCommand,String strDefinitionClassName){
		ICollection icoll=GetCollectionOfCommaDelimittedItemsWithFlag(businessObjectContext,strDefClassNames,strFlag);
		MenuDef def=BuildScopedMruMenu(icoll,strSecurityRight,strContext,strImage,strOverlay,strCommand,strDefinitionClassName);
		CommandControlGroupDef commandControlGroupDef=def.get_CommandControlGroup();
		return commandControlGroupDef.get_Commands();
	}
	
	public  ICollection GetCollectionOfCommaDelimittedItemsWithFlag(String businessObjectContext,String[] strDefClassNames,String strFlag){
		Vector<ArrayList> vlist=new Vector<ArrayList>();
		ArrayList useDefList=null;
		for(String def_name:strDefClassNames){
			ArrayList defList=GetCollectionOfCommaDelimittedItemsWithFlag(businessObjectContext,def_name,strFlag);
			if(useDefList==null&&defList!=null){
				useDefList=defList;
			}
			else{
				vlist.add(defList);
			}
		}
		if(useDefList==null){
			return null;
		}
		else{
			for(ArrayList defList:vlist){
				useDefList.AddRange(defList);
			}
		}
		
		return useDefList;
	}
	
	
	public  ArrayList GetCollectionOfCommaDelimittedItemsWithFlag(String businessObjectContext,String strDefClassName,String strFlag)
    {
        ArrayList list = new ArrayList();
        ArrayList list2 = new ArrayList();
        String name = "";
        if (businessObjectContext != null&&businessObjectContext.length()!=0)
        {
            name = businessObjectContext;
        }
        else
        {
            name = "Change";
        }
        if (name!=null&&name.length()!=0)
        {
            ICollection c = m_lLibrary.GetPlaceHolderList(DefRequest.ForList(Scope.Global, "", name, strDefClassName, "(Role)"));
            if ((c != null) && (c.get_Count() > 0))
            {
                list2.AddRange(c);
            }
            c = m_lLibrary.GetPlaceHolderList(DefRequest.ForList(Scope.User,m_api.get_AuthenticationService().get_CurrentLoginId(), name, strDefClassName, "(Role)"));
            if ((c != null) && (c.get_Count() > 0))
            {
                list2.AddRange(c);
            }
        }
        ICollection placeHolderList = m_lLibrary.GetPlaceHolderList(DefRequest.ForList(Scope.Global, "","", strDefClassName, "(Role)"));
        if ((placeHolderList != null) && (placeHolderList.get_Count() > 0))
        {
            list2.AddRange(placeHolderList);
        }
        placeHolderList = m_lLibrary.GetPlaceHolderList(DefRequest.ForList(Scope.User,m_api.get_AuthenticationService().get_CurrentLoginId(),"", strDefClassName, "(Role)"));
        if ((placeHolderList != null) && (placeHolderList.get_Count() > 0))
        {
            list2.AddRange(placeHolderList);
        }
        for(IEnumerator ienum=list2.GetEnumerator();ienum.MoveNext();)
        {
        	PlaceHolder holder = (PlaceHolder)ienum.get_Current();
            if (holder.HasFlag(strFlag))
            {
                String item = AdrDefListController.BuildCommaDelimittedDefInfo(holder);
                if (!list.Contains(item))
                {
                    list.Add(item);
                }
            }
        }
        
        final CaseInsensitiveComparer m_CaseInsensitiveComparer = new CaseInsensitiveComparer();
        
        list.Sort(new IComparer(){
			public int Compare(Object x, Object y) {
				int num = 0;
	            if ((x != null) && (y != null))
	            {
	                String str = AdrDefListController.ParseCommaDelimittedDefInfoForAlias(x+"");
	                String str2 = AdrDefListController.ParseCommaDelimittedDefInfoForAlias(y+"");
	                if ((str!=null&&str.length()!=0) && (str2!=null&&str2.length()!=0))
	                {
	                    num = m_CaseInsensitiveComparer.Compare(str, str2);
	                }
	            }
	            return num;
			}
        });
        return list;
    }
	
	private MenuDef BuildScopedMruMenu(ICollection coll,String strSecurityRight,String strContext,String strImage,String strOverlay,String strCommand,String strDefinitionClassName){
		MenuDef def = MenuDef.CreateForEdit(strContext); 
		def.set_Target(strContext);
		CommandControlGroupDef def2 = CommandControlGroupDef.CreateForEdit("TopLevel");
		IDictionary folderToMenuItemsMap = GetFolderToMenuItemsMap(coll, strSecurityRight);
		
		if (folderToMenuItemsMap != null){
            ArrayList list = new ArrayList(folderToMenuItemsMap.get_Keys());
            list.Sort();
            boolean flag = true;
            for (IEnumerator ienum1=list.GetEnumerator();ienum1.MoveNext();){
            	String str=ienum1.get_Current().toString();
                IList list2 =(IList)folderToMenuItemsMap.get_Item(str);
                for (IEnumerator ienum2=list2.GetEnumerator();ienum2.MoveNext();){
                	String str2=ienum2.get_Current().toString();
                    int[] scope=new int[1];
                    String[] str3=new String[1];
                    String[] str4=new String[1];
                    String[] str5=new String[1];
                    String[] str6=new String[1];
                    String[] str7=new String[1];
                    String[] str8=new String[1];
                    String[] str9=new String[1];
                    AdrDefListController.ParseCommaDelimittedDefInfo(str2,scope,str3,str4,str5,str6,str7,str8,str9);
                    IDefinition definition = m_lLibrary.GetDefinition(DefRequest.ById(scope[0],str3[0],str4[0],strDefinitionClassName, str6[0], str9[0], false));
                    if(definition==null){
                    	definition=m_lLibrary.GetDefinition(DefRequest.ById(scope[0],str3[0],str4[0],"MultipleBusObQueryGroupDef", str6[0], str9[0], false));
                    }
                    if (definition != null)
                    {
                    	CommandControlItemDef def3 = CommandControlItemDef.CreateNewCommand(definition.get_Alias().replace("&", "&&"), strCommand, str2, strImage,"");
                        if (flag)
                        {
                            def3.set_SeparatorPreceding(true);
                            flag = false;
                        }
                        if (!strOverlay.equals(""))
                        {
                            def3.set_OverlayImage(strOverlay);
                        }
                        def2.AddCommand(def3);
                    }
                }
            }
        }
		
		def.set_CommandControlGroup(def2);
		
		return def;
	}
	
	private HashMap<String,List<CommandControlItemDef>> BuildScopedMruMenu2HashMap(ICollection coll,String strSecurityRight,String strContext,String strImage,String strOverlay,String strCommand,String strDefinitionClassName){
		
		HashMap<String,List<CommandControlItemDef>> itemDefMap=new HashMap<String, List<CommandControlItemDef>>();
		IDictionary folderToMenuItemsMap = GetFolderToMenuItemsMap(coll, strSecurityRight);
		
		if (folderToMenuItemsMap != null){
            ArrayList list = new ArrayList(folderToMenuItemsMap.get_Keys());
            list.Sort();
            boolean flag = true;
            for (IEnumerator ienum1=list.GetEnumerator();ienum1.MoveNext();){
            	String str=ienum1.get_Current().toString();
                IList list2 =(IList)folderToMenuItemsMap.get_Item(str);
                List<CommandControlItemDef> comandItems=null;
                if(itemDefMap.containsKey(str)){
                	comandItems=itemDefMap.get(str);
                }
                else{
                	comandItems=new java.util.ArrayList<CommandControlItemDef>();
                }
                for (IEnumerator ienum2=list2.GetEnumerator();ienum2.MoveNext();){
                	String str2=ienum2.get_Current().toString();
                    int[] scope=new int[1];
                    String[] str3=new String[1];
                    String[] str4=new String[1];
                    String[] str5=new String[1];
                    String[] str6=new String[1];
                    String[] str7=new String[1];
                    String[] str8=new String[1];
                    String[] str9=new String[1];
                    AdrDefListController.ParseCommaDelimittedDefInfo(str2,scope,str3,str4,str5,str6,str7,str8,str9);
                    IDefinition definition = m_lLibrary.GetDefinition(DefRequest.ById(scope[0],str3[0],str4[0],strDefinitionClassName, str6[0], str9[0], false));
                    if(definition==null){
                    	definition=m_lLibrary.GetDefinition(DefRequest.ById(scope[0],str3[0],str4[0],"MultipleBusObQueryGroupDef", str6[0], str9[0], false));
                    }
                    if (definition != null)
                    {
                    	CommandControlItemDef def3 = CommandControlItemDef.CreateNewCommand(definition.get_Alias().replace("&", "&&"), strCommand, str2, strImage,"");
                        if (flag)
                        {
                            def3.set_SeparatorPreceding(true);
                            flag = false;
                        }
                        if (!strOverlay.equals(""))
                        {
                            def3.set_OverlayImage(strOverlay);
                        }
                        comandItems.add(def3);
                    }
                }
                itemDefMap.put(str,comandItems);
            }
        }
		
		return itemDefMap;
	}
	
	private IDictionary GetFolderToMenuItemsMap(ICollection collMenuItems,String strSecurityRight){
		Hashtable hashtable = new Hashtable();
        if (collMenuItems != null)
        {
            Hashtable hashtable2 = new Hashtable();
            for (IEnumerator enume=collMenuItems.GetEnumerator();enume.MoveNext();)
            {
            	String str=enume.get_Current().toString();	
            	String start=str.substring(0,str.indexOf(","));
            	Pattern pattern = Pattern.compile("[0-9]*"); 
        	    Matcher isNum = pattern.matcher(start);
        	    if(isNum.matches() ){
        	    	int intstart=Integer.parseInt(start);
        	    	switch(intstart){
	            		case Scope.Global:start="Global";break;
	            		case Scope.User:start="User";break;
	            		case Scope.Role:start="Role";break;
	            		case Scope.SecurityGroup:start="SecurityGroup";break;
	            		case Scope.Team:start="Team";break;
	            		case Scope.Dependent:start="Dependent";break;
	            		case Scope.Internal:start="Internal";break;
        	    	}
        	    	str=start+","+str.substring(str.indexOf(",")+1,str.length());
        	    } 

                int key = AdrDefListController.ParseCommaDelimittedDefInfoForScope(str);
                if (key != Scope.Unknown)
                {
                    String str2 = AdrDefListController.ParseCommaDelimittedDefInfoForFolder(str);
                    if (!hashtable2.Contains(key))
                    {
                    	String scope="";
                    	switch(key){
                    		case Scope.Global:scope="Global";break;
                    		case Scope.User:scope="User";break;
                    		case Scope.Role:scope="Role";break;
                    		case Scope.SecurityGroup:scope="SecurityGroup";break;
                    		case Scope.Team:scope="Team";break;
                    		case Scope.Dependent:scope="Dependent";break;
                    		case Scope.Internal:scope="Internal";break;
                    	}
                        hashtable2.Add(key,m_api.get_SecurityService().HasModuleItemRight(strSecurityRight,scope,SecurityRight.View));
                    }
                    if((Boolean)hashtable2.get_Item(key))
                    {
                        if (!hashtable.Contains(str2))
                        {
                            hashtable.set_Item(str2, new ArrayList());
                        }
                        ((ArrayList)(hashtable.get_Item(str2))).Add(str);
                    }
                }
            }
        }
        return hashtable;
	}
	
	private IDefinition GetDefinitionFromCommaDelimittedItem(String strDefClassName,String strItem,ISiteviewApi m_api)
    {
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
