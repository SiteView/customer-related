package core.apploader.reporting;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ArrayList;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Collections.IList;
import Siteview.AutoTaskDef;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.IMru;
import Siteview.IViewOverrideInfo;
import Siteview.PlaceHolder;
import Siteview.ReportDef;
import Siteview.SiteviewException;
import Siteview.StringUtils;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.PerspectiveDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;
import Siteview.Xml.XmlScope;
import Siteview.Xml.XmlSecurityRight;
import core.apploader.reporting.dialog.ReportDialog;
import core.apploader.reporting.wizard.PublishedReportWizard;

public class ReportCenterForm extends ApplicationWindow{

	/*
	 * 公共数据接口
	 */
	private static String m_strDefClassName = "ReportDef";	// C#查询数据时的固定参数
	private ScopeUtil m_ScopeUtil;     				// 暂时叫  查询范围的类
	private static String m_moduleSecurityName = "Core.Security.Reports";	// C#查询数据时的固定参数
	private PerspectiveDef perspectiveDef;  		// 不懂
	private ReportCenterForm quickcenter;			// 主程序窗口
	private ISiteviewApi m_api;  					// 初始化接口
	private IList m_lstSupportedScopesWithOwners;
	private DefinitionLibrary m_Library ;
	private IDefinition m_Def;
	private ReportDef reportDef; 
	
	private Table tbBusObList;
	private Combo cboQuickOblink;  					// 快速操作下拉式  
	private Combo cb;              					// 查找文本
	private String findtext;       					// 查找内容
	public TabFolder tabFolder;   					// 选项卡文件夹
	private TabItem tabRange;
	private TabItem tbCategoryy ;
	public Table tbCategory;
	
	private Button btOK;  							//保存按钮
	private HashSet<String> Categoryset = new HashSet<String>(); //保存全部类别的集合
	
	private ArrayList alldate = new ArrayList();		//操作中心关联的所有的数据
	private SashForm sashForm;
	
	//操作菜单
	private Action actAdd;   			// 新建
	private Action actEdit;  			// 编辑
	private Action actCopy;  			// 复制
	private Action actDelete;   		// 删除
	private Action actSearch;
	private Action qrun;    			// 运行
	//视图菜单
	private Action qbigicon;   			// 大图标
	private Action qsmallicon; 			// 列表
	private Action qdetailed;  			// 详细列表
	private Action qdelqueryop;  		// 删除查询选 项
	private Action qref;   				// 刷新
	//发布
	private Action upub;  				// 报告发布向导(&W)
	
	// 范围
	public Tree treeRange;				// 范围 树结构
	private Map<String,ArrayList> htCatToListerItems = new HashMap<String,ArrayList>();
	private Map<String,ArrayList> findhtCatToListerItems = new HashMap<String,ArrayList>();
	//类别
	private Map<String,ArrayList> httypeToListerItems = new HashMap<String,ArrayList>();
	//查找
	private Tree findtree;       		// 查找  树结构
	private TabItem tabFind=null;

	//右击菜单
	private Menu popMenu;
	
	private boolean showButtons=false;	// 是否显示确定取消按钮
	private PlaceHolder selection;
	
	//默认第一次查询
	private Object strcontext;
	
	/**
	 * TODO 入口
	 * @wbp.parser.constructor
	 */
	public ReportCenterForm(boolean flag,Object strcontext) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.APPLICATION_MODAL);
		quickcenter = this;
		this.strcontext = strcontext;
		showButtons = flag;
		createActions();
		addMenuBar();
		addCoolBar(SWT.FLAT);
		addStatusLine();
		Initializeddate();
	}
	
	
	/**
	 * TODO 初始化一些基本数据
	 * @return
	 */
	public void Initializeddate(){
		this.m_api = ConnectionBroker.get_SiteviewApi();
		this.m_Library = ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary();
		this.m_ScopeUtil = new ScopeUtil(m_api, m_Library, m_strDefClassName, m_moduleSecurityName);
		this.m_ScopeUtil.set_CheckRights(true);
		this.m_lstSupportedScopesWithOwners = this.m_ScopeUtil.GetSupportedScopesWithOwners(XmlSecurityRight.ToRight("VAED"), true);
		this.quickcenter=this;
	}

	/**
	 * TODO 设置窗口属性
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				openclosecoolbar(false);
			}
		});
		super.configureShell(newShell);
		newShell.setMaximized(!showButtons);
		if(showButtons){
			newShell.setSize(new Point(800,600));
			newShell.setLocation(240, 80);
		}
		newShell.setText("报告   中心");
		newShell.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png")));
	}


	/**
	 * TODO 创建页面控件
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout());
		
		Composite fcontainer = new Composite(container, SWT.NONE);
		fcontainer.setLayout(new FormLayout());
		
		sashForm = new SashForm(fcontainer, SWT.NONE);
		FormData fd_sash = new FormData();
		fd_sash.left = new FormAttachment(0,0);
		fd_sash.right = new FormAttachment(100,0);
		fd_sash.top = new FormAttachment(0,0);
		if (showButtons)
			fd_sash.bottom = new FormAttachment(100,-40);
		else
			fd_sash.bottom = new FormAttachment(100,0);
		
		sashForm.setLayoutData(fd_sash);
		
		if (showButtons){
			Composite cBottom = new Composite(fcontainer, SWT.NONE);
			cBottom.setLayout(new FormLayout());
			FormData fd_bottom = new FormData();
			fd_bottom.left = new FormAttachment(0,0);
			fd_bottom.right = new FormAttachment(100,0);
			fd_bottom.top = new FormAttachment(sashForm,0);
			fd_bottom.bottom = new FormAttachment(100,-5);
			cBottom.setLayoutData(fd_bottom);
			
			btOK = new Button(cBottom,SWT.NONE);
			btOK.setText("确定");
			btOK.setEnabled(false);
			FormData fd_btok = new FormData();
			fd_btok.left = new FormAttachment(100,-200);
			fd_btok.right = new FormAttachment(100,-120);
			fd_btok.top = new FormAttachment(0,10);
			fd_btok.bottom = new FormAttachment(0,30);
			btOK.setLayoutData(fd_btok);
			btOK.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(tbBusObList.getSelectionIndex()==-1)return;
					TableItem ti = tbBusObList.getSelection()[0];
					if (tbBusObList.getSelectionCount() < 1){
						if(!(ti.getData() instanceof PlaceHolder)){
							MessageDialog.openInformation(getShell(), "提示", "请选择快速操作中心。");
							return;
						}
					}else{
						selection = (PlaceHolder)ti.getData();
					}
					setReturnCode(OK);
					close();
				}});
			Button btCancel = new Button(cBottom,SWT.NONE);
			btCancel.setText("取消");
			FormData fd_btcancel = new FormData();
			fd_btcancel.left = new FormAttachment(100,-100);
			fd_btcancel.right = new FormAttachment(100,-20);
			fd_btcancel.top = new FormAttachment(0,10);
			fd_btcancel.bottom = new FormAttachment(0,30);
			btCancel.setLayoutData(fd_btcancel);
			btCancel.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					setReturnCode(CANCEL);
					close();
				
				}});
		}
		
		tabFolder = new TabFolder(sashForm, SWT.NONE);
		tabFolder.setBounds(0, 0, parent.getSize().x/3, parent.getSize().y-45);
		tabRange = new TabItem(tabFolder, SWT.NONE);
		tabRange.setText("范围");
		tabRange.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Siteview#Images.Icons.Common.Global.png"),0x12,0x12));
		tbCategoryy = new TabItem(tabFolder, SWT.NONE);
		tbCategoryy.setText("类别");
		tbCategoryy.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.CategoryReport.png"),0x12,0x12));
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(htCatToListerItems.isEmpty())
				return;
				TabItem[] selected=tabFolder.getSelection();
				if(selected.length>0){
					if("类别".equals(selected[0].getText())){
						TableItem[] se=tbCategory.getSelection();
						if(se.length>0){
							addlisttabledate(se[0].getData()+"");
						}else{
							addlisttabledate("");
						}
					}else if("查找".equals(selected[0].getText())){
						TreeItem[] findfreeselected=findtree.getSelection();
						if(findfreeselected.length>0){
							findtree.showItem(findfreeselected[0]);
							addtabledate(findfreeselected[0].getData().toString(),"2");
						}else{
							addtabledate("16","2");
						}
					}else{
						TreeItem[] findfreeselected=treeRange.getSelection();
						if(findfreeselected.length>0){
							treeRange.showItem(findfreeselected[0]);
							addtabledate(findfreeselected[0].getData().toString(),"1");
						}else{
							addtabledate("16","1");
						}
					}
				}
			}
		});
		
		tbBusObList = new Table(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
		tbBusObList.setHeaderVisible(true);
		tbBusObList.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
					openclosecoolbar(false);
				
			}
		});
		tbBusObList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object ob=method();
				if(ob instanceof PlaceHolder){
					if(showButtons){
						btOK.setEnabled(true);
					}
					openclosecoolbar(true);
				}else{
					openclosecoolbar(false);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				Object ob=method();
				if(!(ob instanceof PlaceHolder)){
					String str=(String)ob;
					TabItem[] selected=tabFolder.getSelection();
					if(selected.length>0){
						if("查找".equals(selected[0].getText())){
							TreeItem[] rangetree=findtree.getItems();
							for(int i=0;i<rangetree.length;i++){
								TreeItem[] ran=rangetree[i].getItems();
								for(int j=0;j<ran.length;j++){
									TreeItem item=ran[j];
									if(item.getData().toString().equals(str)){
										findtree.setSelection(item);
										findtree.showItem(item);
									}
								}
							}
							addtabledate(str,"2");
						}else{
							TreeItem[] rangetree=treeRange.getItems();
							for(int i=0;i<rangetree.length;i++){
								TreeItem[] ran=rangetree[i].getItems();
								for(int j=0;j<ran.length;j++){
									TreeItem item=ran[j];
									if(item.getData().toString().equals(str)){
										treeRange.setSelection(item);
										treeRange.showItem(item);
									}
								}
							}
							addtabledate(str,"1");
						}
					}
				}else{
					if(showButtons){
						selection = (PlaceHolder)ob;
						setReturnCode(OK);
						close();
					}
				}
			}
			public Object method(){
				TableItem[] selected=tbBusObList.getSelection();
				Object ob=null;
				if(selected.length>0){
					ob=selected[0].getData(); 
				}
				return ob;
			}
		});
		
		TableColumn tc1 = new TableColumn(tbBusObList,SWT.NONE);
		tc1.setText("名称");
		tc1.setWidth(260);
		TableColumn tc2 = new TableColumn(tbBusObList,SWT.NONE);
		tc2.setText("描述");
		tc2.setWidth(300);
		//TableColumn tc3 = new TableColumn(tbBusObList,SWT.NONE);
		//tc3.setText("类别");
		//tc3.setWidth(200);
		
		final MenuManager pm = new MenuManager();
		pm.setRemoveAllWhenShown(true);

		popMenu = pm.createContextMenu(tbBusObList);
		tbBusObList.setMenu(popMenu);
		sashForm.setWeights(new int[] {147, 661});
		pm.addMenuListener(new IMenuListener(){
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(new Separator());
				manager.add(qrun);
				manager.add(actAdd);
				manager.add(actEdit);
				manager.add(actCopy);
				manager.add(actDelete);
				manager.add(upub);
			}});
		
		tbBusObList.addMouseListener(new MouseListener(){
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button ==3){
					Table t = (Table)e.widget;
					if (t.getSelectionCount() >0){
						popMenu.setLocation(t.toDisplay(e.x,e.y));
						popMenu.setVisible(true);
					}
			}
		}});
		
		loadObject();
		firstloaddate();
		return container;
	}

	
	/**
	 * TODO 第一次默认加载 数据 方法
	 */
	public void firstloaddate(){
		if(!StringUtils.IsEmpty(strcontext.toString())){
			String strs[]=cboQuickOblink.getItems();
			for(int i=0;i<cboQuickOblink.getItems().length;i++){
				if(cboQuickOblink.getData(strs[i]).equals(strcontext)){
					cboQuickOblink.select(i);
					getcboQuickOblink(i);
					addtreedate();
					addlistdate();
					addtabledate("16","1");
				}
			}
		}
		if(cboQuickOblink.getSelectionIndex()==-1){
				cboQuickOblink.select(0);
				getcboQuickOblink(0);
				addtreedate();
				addlistdate();
				addtabledate("16","1");
		}
	}

	/**
	 * TODO 加载快速操作中心的下拉式
	 */
	protected void loadObject() {
		ICollection busLinks = m_Library.GetPlaceHolderList(DefRequest.ForList(BusinessObjectDef.get_ClassName()));
		IEnumerator it = busLinks.GetEnumerator();
		List<String> bobs = new Vector<String>();
		bobs.add(" 未关联");
		cboQuickOblink.setData(" 未关联","");
		while(it.MoveNext()){
			PlaceHolder ph = (PlaceHolder)it.get_Current();
			if (ph.HasFlag("Master")&& !ph.HasFlag("AllowDerivation")){
				bobs.add(ph.get_Alias());
				cboQuickOblink.setData(ph.get_Alias(), ph.get_Name());
			}
		}
		Collections.sort(bobs,new Comparator<Object>(){
			@Override
			public int compare(Object o1, Object o2) {
				String s1 = (String) o1;
				String s2 = (String) o2;
				return s1.compareToIgnoreCase(s2);
			}});
		cboQuickOblink.setItems((String[]) bobs.toArray(new String[]{}));
	}
	
	
	/**
	 * TODO 下拉式点击事件 先初始化清理一些数据
	 */
	public void cboQuickOblinkSelected() {
		htCatToListerItems.clear();
		alldate.Clear();
		httypeToListerItems.clear();
		getcboQuickOblink(cboQuickOblink.getSelectionIndex());
		addtreedate();
		addlistdate();
		addtabledate("16","1");
	}
	
	/**
	 * TODO 根据选择下拉式  装载关联的范围和类型  
	 */
	public void getcboQuickOblink(int linkIndex){
		if (linkIndex < 0) return;
		String strBusLink = (String) cboQuickOblink.getData(cboQuickOblink.getItem(linkIndex));
		IEnumerator it =m_lstSupportedScopesWithOwners.GetEnumerator();
		while(it.MoveNext()){
			ScopeUtil.ScopeAndOwners owners = (ScopeUtil.ScopeAndOwners)it.get_Current();
			if (owners.get_Owners().get_Count()>0 && owners.get_Scope() != Scope.Global){
				for(int i = 0; i < owners.get_Owners().get_Count(); i++){
					String str = (String)owners.get_Owners().get_Item(i);
					OrganizeListerItemsByCategory(htCatToListerItems,owners.get_Scope(),str,strBusLink,false);
				}
			}else{
				OrganizeListerItemsByCategory(htCatToListerItems,owners.get_Scope(),"",strBusLink,false);
			}
			
		}
	}
	
	/**
	 * TODO 对取出来的数据进行分类 保存到 MAP 集合中
	 * @param s Scope 
	 * @param Owner 
	 * @param linkTo 关联的业务对象的名字
	 * @param bFilter 是否过滤
	 */
	private void OrganizeListerItemsByCategory(Map<String,ArrayList> listItems,int s,String Owner, String linkTo,boolean bFilter){
		String strPerspective = (this.perspectiveDef != null) ? this.perspectiveDef.get_Name() : "(Base)";
		ArrayList obs=(ArrayList) m_Library.GetPlaceHolderList(DefRequest.ForList(s,Owner,linkTo,m_strDefClassName,strPerspective));
		if (obs!=null){
			alldate.Add(obs);
			for(int i = 0; i < obs.get_Count();i ++){
				PlaceHolder holder = (PlaceHolder)obs.get_Item(i);
				if(!Categoryset.contains(holder.get_Folder())){
					Categoryset.add(holder.get_Folder());
				}
				if (!listItems.containsKey(holder.get_Scope()+"~"+holder.get_Folder())){
					listItems.put(holder.get_Scope()+"~"+holder.get_Folder(), new ArrayList());
				}
				listItems.get(holder.get_Scope()+"~"+holder.get_Folder()).Add(holder);
				if (!httypeToListerItems.containsKey(holder.get_Folder())){
					httypeToListerItems.put(holder.get_Folder(), new ArrayList());
				}
				httypeToListerItems.get(holder.get_Folder()).Add(holder);
			}
		}
	}
	
	/**
	 * TODO 动态的创建 范围 文件夹  和 树结构
	 */
	public void addtreedate(){
		treeRange = new Tree(tabFolder, SWT.SINGLE);
		treeRange.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TreeItem[] selected=treeRange.getSelection();
				if(selected.length>0){
					addtabledate((selected[0].getData()+""),"1");
				}
				
			}
		});
		IEnumerator it =m_lstSupportedScopesWithOwners.GetEnumerator();
		String nodetext="";
		String images="";
		while(it.MoveNext()){
			ScopeUtil.ScopeAndOwners owners = (ScopeUtil.ScopeAndOwners)it.get_Current();
			nodetext=m_ScopeUtil.ScopeToString(owners.get_Scope());
			if(!StringUtils.IsEmpty(nodetext)){
				switch (owners.get_Scope()){
					case 1:
						images="[IMAGE]Siteview#Images.Icons.Common.User.png";
						break;
					case 4:
						images="[IMAGE]Siteview#Images.Icons.Common.Role.png";
						break;
					case 16:
						images="[IMAGE]Siteview#Images.Icons.Common.Global.png";
				}
				TreeItem treeItem = new TreeItem(treeRange, SWT.SINGLE);
				treeItem.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(images),0x12,0x12));
				treeItem.setText(nodetext);
				treeItem.setData(owners.get_Scope()+"");
				HashSet<String> set=new HashSet<String>();
				for(String str:htCatToListerItems.keySet()){
					ArrayList obs = htCatToListerItems.get(str);
					for(int i = 0 ; i < obs.get_Count(); i++){
						PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
						if(owners.get_Scope()==ph.get_Scope()){
							if(!StringUtils.IsEmpty(ph.get_Folder())){
								if(!set.contains(ph.get_Folder())){
									TreeItem  node= new TreeItem(treeItem, SWT.NONE);
									node.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.CategoryReport.png",0x12,0x12)));
									node.setText(ph.get_Folder());
									node.setData(owners.get_Scope()+"~"+ph.get_Folder());
								}
								set.add(ph.get_Folder());
							}
						}
					}
				}
			}
			
		}
		tabRange.setControl(treeRange);
	}
	
	
	/**
	 * TODO 动态加载查找树结构数据
	 */
	public void addfindtreedate(){
		findtree = new Tree(tabFolder, SWT.SINGLE);
		findtree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selected=findtree.getSelection();
				if(selected.length>0){
					addtabledate((selected[0].getData()+""),"2");
				}
			}
		});
		IEnumerator it =m_lstSupportedScopesWithOwners.GetEnumerator();
		String nodetext="";
		String images="";
		while(it.MoveNext()){
			ScopeUtil.ScopeAndOwners owners = (ScopeUtil.ScopeAndOwners)it.get_Current();
			nodetext=m_ScopeUtil.ScopeToString(owners.get_Scope());
			if(!StringUtils.IsEmpty(nodetext)){
				switch (owners.get_Scope()){
					case 1:
						images="[IMAGE]Siteview#Images.Icons.Common.User.png";
						break;
					case 4:
						images="[IMAGE]Siteview#Images.Icons.Common.Role.png";
						break;
					case 16:
						images="[IMAGE]Siteview#Images.Icons.Common.Global.png";
				}
				TreeItem treeItem = new TreeItem(findtree, SWT.NONE);
				treeItem.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage(images),0x12,0x12));
				treeItem.setText(nodetext);
				treeItem.setData(owners.get_Scope());
				HashSet<String> set=new HashSet<String>();
				for(String str:findhtCatToListerItems.keySet()){
					ArrayList obs = findhtCatToListerItems.get(str);
					for(int i = 0 ; i < obs.get_Count(); i++){
						PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
						if(owners.get_Scope()==ph.get_Scope()){
							if(!StringUtils.IsEmpty(ph.get_Folder())){
								if(!set.contains(ph.get_Folder())){
									TreeItem  node= new TreeItem(treeItem, SWT.NONE);
									node.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderClosed.png"),0x12,0x12));
									node.setText(ph.get_Folder());
									node.setData(owners.get_Scope()+"~"+ph.get_Folder());
								}
								set.add(ph.get_Folder());
							}
						}
					}
					
				}
			}
		}
		tabFind.setControl(findtree);
	}
	
	/**
	 * TODO 查找数据过滤  
	 */
	public void Filterfinddate(){
		if (alldate!=null){
			findhtCatToListerItems.clear();
			for(int j=0;j<alldate.get_Count();j++){
				ArrayList obs=(ArrayList) alldate.get_Item(j);
				for(int i = 0; i < obs.get_Count();i ++){
					PlaceHolder holder = (PlaceHolder)obs.get_Item(i);
					if((holder.get_Name().toUpperCase().indexOf(findtext)>=0)||(holder.get_Alias().toUpperCase().indexOf(findtext)>=0)||(holder.get_Description().toUpperCase().indexOf(findtext)>=0)||(holder.get_Folder().toUpperCase().indexOf(findtext))>=0){
						if (!findhtCatToListerItems.containsKey(holder.get_Scope()+"~"+holder.get_Folder())){
							findhtCatToListerItems.put(holder.get_Scope()+"~"+holder.get_Folder(), new ArrayList());
						}
						findhtCatToListerItems.get(holder.get_Scope()+"~"+holder.get_Folder()).Add(holder);
					}
				}
			}
		}
	}
	
	
	/**
	 * TODO 加载类别的数据
	 */
	public void addlistdate(){
		tbCategory = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbCategory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selected=tbCategory.getSelection();
				if(selected.length>0){
					addlisttabledate((String)selected[0].getData());
				}
			}
		});
		tbCategoryy.setControl(tbCategory);
		if (httypeToListerItems.isEmpty()) return;
		tbCategory.removeAll();
		for(String str:httypeToListerItems.keySet()){
			TableItem ti = new TableItem(tbCategory,SWT.NONE);
			ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.CategoryReport.png"),0x12,0x12));
			if (str.equals("")){
				ti.setText("（无类别）");
				ti.setData(str);
			}else
				ti.setText(str);
				ti.setData(str);
		}
	}
	
	/**
	 * TODO 点击类别 加载table里显示的详细信息
	 */
	public void addlisttabledate(String sel){
		tbBusObList.removeAll();
		ArrayList obs = httypeToListerItems.get(sel);
		if(obs!=null&&obs.get_Count()>0){
			for(int i = 0 ; i < obs.get_Count(); i++){
				PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
//				ReportDef ph_def = (ReportDef)m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
//				boolean sin = ph_def.get_ShowInNavigator();
//				boolean som  = ph_def.get_ShowOnMenu();
//				String strIcon = "[IMAGE]Core#Images.Icons.Report.png";
//				String strOverlayIcon = "";
//				ImageHolder holder =  ImageResolver.get_Resolver().ResolveImage(strIcon,0x12,0x12);
//				if(som && sin){
//					strOverlayIcon = "[IMAGE]Core#Images.Overlays.Overlay_MenuNav.png";
//					holder = ImageResolver.get_Resolver().ResolveImageWithOverlay(strIcon, strOverlayIcon, 0x12, 0x12);
//				}else if(som){
//					strOverlayIcon = "[IMAGE]Core#Images.Overlays.Overlay_OnMenu.png";
//					holder = ImageResolver.get_Resolver().ResolveImageWithOverlay(strIcon, strOverlayIcon, 0x12, 0x12);
//				}else if(sin){
//					strOverlayIcon = "[IMAGE]Core#Images.Overlays.Overlay_OnNav.png";
//					holder = ImageResolver.get_Resolver().ResolveImageWithOverlay(strIcon, strOverlayIcon, 0x12, 0x12);
//				}
				TableItem ti = new TableItem(tbBusObList,SWT.NONE);
				ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"),0x12,0x12));
				ti.setText(new String[]{ph.get_Alias(),ph.get_Description()});
				ti.setData(ph);
			}
		}
	}
	
	/**
	 * TODO 点击树时加载table里显示的详细信息
	 * @param type   =1 显示范围和类别的数据 =2 显示查找的数据
	 */
	public void addtabledate(String sel,String type){
		tbBusObList.removeAll();
		HashMap<String,ArrayList> dateItems=null;
		if("1".equals(type)){
			dateItems=(HashMap<String, ArrayList>) htCatToListerItems;
		}else{
			dateItems=(HashMap<String, ArrayList>) findhtCatToListerItems;
		}
		for(String str:dateItems.keySet()){
			String[] stip=sel.split("~");
			if(stip!=null&&stip.length>1){
				if(sel.equals(str)){
					ArrayList obs = dateItems.get(str);
					for(int i = 0 ; i < obs.get_Count(); i++){
						PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
						TableItem ti = new TableItem(tbBusObList,SWT.NONE);
						ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"),0x12,0x12));
						ti.setText(new String[]{ph.get_Alias(),ph.get_Description()});
						ti.setData(ph);
					}
				}
			}else{
				String[] strsplit=str.split("~");
				if(stip[0].equals(strsplit[0])&&strsplit!=null&&strsplit.length>1){
						TableItem t2 = new TableItem(tbBusObList,SWT.NONE);
						t2.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.CategoryReport.png"),0x12,0x12));
						t2.setText(new String[]{strsplit[1],""});
						t2.setData(str);
				}
				if(stip[0].equals(strsplit[0])){
					ArrayList obs = dateItems.get(str);
					for(int i = 0 ; i < obs.get_Count(); i++){
						PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
						if(StringUtils.IsEmpty(ph.get_Folder())){
							TableItem ti = new TableItem(tbBusObList,SWT.NONE);
							ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"),0x12,0x12));
							ti.setText(new String[]{ph.get_Alias(),ph.get_Description()});
							ti.setData(ph);
						}
					}
				}
			}
		}
	}
	
	/**
	 * TODO 创建菜单
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		
		MenuManager menuBar = new MenuManager();			// 创建得菜单栏对象
		MenuManager OpMenu = new MenuManager("操作(A)"); 	// 操作菜单项
		MenuManager ViewMenu = new MenuManager("视图(v)"); 	// 视图菜单项
		MenuManager UtMenu = new MenuManager("应用(U)");		// 应用菜单项
		
		menuBar.add(OpMenu);
		menuBar.add(ViewMenu);
		menuBar.add(UtMenu);
		
		OpMenu.add(new Separator());
		OpMenu.add(actAdd);
		OpMenu.add(actEdit);
		OpMenu.add(actCopy);
		OpMenu.add(actDelete);
		
		ViewMenu.add(qbigicon);
		ViewMenu.add(qsmallicon);
		ViewMenu.add(qdetailed);
		ViewMenu.add(new Separator());
		qdelqueryop.setEnabled(false);
		ViewMenu.add(qdelqueryop);
		ViewMenu.add(new Separator());
		ViewMenu.add(qref);
		
		UtMenu.add(upub);
		
		return menuBar;
	}

	/**
	 * TODO 创建工具栏.
	 * @return the coolbar manager
	 */
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager coolBarManager = new CoolBarManager(style);
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBarManager.add(new ToolBarContributionItem(toolbar, "main"));   
		toolbar.add(qrun);		//运行
		toolbar.add(actAdd);	//新建
		toolbar.add(actEdit);	//编辑
		toolbar.add(actCopy);	//复制
		toolbar.add(actDelete);	//删除
		toolbar.add(new Separator());
		ControlContribution cc = new ControlContribution(""){
			@Override
			protected Control createControl(Composite parent) {
				Composite container = new Composite(parent,SWT.NONE);
				Label searchLabel=new Label(container,SWT.NONE);
				searchLabel.setSize(30,20);
				searchLabel.setLocation(5,5);
				searchLabel.setText("查找:");
				cb = new Combo(container,SWT.NONE);
				cb.setSize(150,20);
				cb.setLocation(35,2);
				return container;
			}};
		toolbar.add(cc);
		toolbar.add(actSearch);
		
		IToolBarManager toolbar2 = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBarManager.add(new ToolBarContributionItem(toolbar2, "linkObject"));
		toolbar2.add(new ControlContribution(""){

			@Override
			protected Control createControl(Composite parent) {
				Composite container = new Composite(parent,SWT.NONE);
				Label lblLinkTxt = new Label(container,SWT.NONE);
				lblLinkTxt.setSize(225,20);
				lblLinkTxt.setLocation(5,5);
				lblLinkTxt.setText("查找与以下对象关联的   报告:");
				
				cboQuickOblink = new Combo(container,SWT.READ_ONLY);
				cboQuickOblink.setSize(180, 20);
				cboQuickOblink.setLocation(230, 0);
				cboQuickOblink.addSelectionListener(new SelectionListener(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						cboQuickOblinkSelected();
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}});
				
				return container;
			}});
		return coolBarManager;
	}
	
	// TODO　创建Action 
	private void createActions(){
		actAdd = new Action("新建(&N)"){
			@Override
			public void run() {
				tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				TreeItem[] selected=treeRange.getSelection();
				int scope_int=16;
				String folderName="";
				if(selected.length>0){
					String[] stip=(selected[0].getData()+"").split("~");
					if(stip.length>0){
						scope_int=Integer.parseInt(stip[0]);
						if(stip.length>1){
							folderName=stip[1];
						}
					}
				}
				m_Def =m_Library.GetNewDefForEditing(DefRequest.ByCategory(scope_int, "", ReportDef.get_ClassName(), ""));
				reportDef = (ReportDef) m_Def;
				ReportDialog report = new ReportDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,m_Def, cboQuickOblink.getData(cboQuickOblink.getItem(cboQuickOblink.getSelectionIndex())).toString(), 1);
				report.open();
				
				if(!getShell().isDisposed()){
					tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
				}
			}
		};
		
		actAdd.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.NewAdd.png"),0x12,0x12).getImageData();
			}});
		actAdd.setAccelerator(SWT.CTRL + 'N');
		
		actEdit = new Action("编辑(&E)"){
			@Override
			public void run() {
				tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				
				if (tbBusObList.getSelection().length < 1) return;
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				
				if ((defForEditing != null) && HasEditRight(defForEditing.get_Scope())){
					reportDef=(ReportDef) defForEditing;
					ReportDialog report = new ReportDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,m_Def, ph.get_LinkedTo(), 2);
					report.open();
				}
				
				if(!getShell().isDisposed()){
					tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
				}
			}
		};
		actEdit.setAccelerator(SWT.CTRL + 'E');
		
		actEdit.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Edit.png"),0x12,0x12).getImageData();
			}});
		
		actCopy = new Action("复制(&F)"){
			@Override
			public void run() {
				tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				
				if (tbBusObList.getSelection().length < 1) return;
				TreeItem[] selected=treeRange.getSelection();
				int phtype=16;
				if(selected.length>0){
					String[] stip=(selected[0].getData()+"").split("~");
					if(stip.length>0){
						phtype=Integer.parseInt(stip[0]);
					}
				}
				if (tbBusObList.getSelection().length < 1) return;
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				m_Def =m_Library.GetNewDefForEditing(DefRequest.ByCategory(phtype, "", ReportDef.get_ClassName(), ""));
				reportDef = (ReportDef)m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				ReportDialog report = new ReportDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,m_Def, cboQuickOblink.getData(cboQuickOblink.getItem(cboQuickOblink.getSelectionIndex())).toString(), 3);
				report.open();
				
				if(!getShell().isDisposed()){
					tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
				}
			}
		};
		actCopy.setAccelerator(SWT.CTRL + 'F');
		
		actCopy.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Copy.png"),0x12,0x12).getImageData();
			}});
		
		actDelete = new Action("删除(&D)"){
			@Override
			public void run() {
				tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
				if (tbBusObList.getSelection().length < 1){
					return;
				}
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition def = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				MessageBox msg=new MessageBox(getShell(),SWT.YES|SWT.NO);
				msg.setMessage("是否删除　'"+ph.get_Alias()+"' ?");
				msg.setText("SiteView应用程序");
				int result=msg.open();
				if(result==SWT.YES){
					
					if(!DeleteDefinition(def)){
						MessageBox msg1 =new MessageBox(getShell(),SWT.OK);
						msg1.setMessage("'"+ph.get_Alias()+"' 删除失败!");
						msg1.setText("SiteView应用程序"); 
						msg1.open();
					}
					Refresh();
				}
				if(!getShell().isDisposed()){
					tbBusObList.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
				}
			}
		};
		
		qrun = new Action("运行(&R)"){
			@Override
			public void run() {
				
				if (tbBusObList.getSelection().length < 1) return;
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				if ((defForEditing != null) && HasEditRight(defForEditing.get_Scope())){
					reportDef=(ReportDef) defForEditing;
//						DashboardPanel.open(m_api,rashboardDef);
						getShell().close();
				}
			}
		};
		qrun.setEnabled(false);
		actDelete.setAccelerator(SWT.CTRL + 'D');
		actDelete.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Delete.png",0x12,0x12)).getImageData();
			}});
		
		actSearch = new Action("搜索"){
			@Override
			public void run() {
				findtext=cb.getText().toUpperCase();
				if(StringUtils.IsEmpty(findtext))return;
				int count=tabFolder.getItemCount();
				if(count<3){
					tabFind = new TabItem(tabFolder, SWT.NONE);
					tabFolder.setSelection(tabFind);
					tabFind.setText("查找");
					tabFind.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Search.png"),0x10,0x10));
					Filterfinddate();
					addfindtreedate();
					addtabledate("16","2");
				}else{
					findtree.removeAll();
					Filterfinddate();
					addfindtreedate();
					addtabledate("16","2");
				}
			}
		};
		actSearch.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Siteview#Images.Icons.Common.Find.png")).getImageData();
			}});
		
		qbigicon = new Action("大图标(&I)"){
			@Override
			public void run() {
				
			}
		};
		
		qbigicon.setAccelerator(SWT.CTRL + 'I');
		qsmallicon = new Action("列表(&L)"){
			@Override
			public void run() {
				
			}
		};
		qsmallicon.setAccelerator(SWT.CTRL + 'L');
		qdetailed = new Action("详细列表(&Y)"){
			@Override
			public void run() {
				
			}
		};
		qdetailed.setAccelerator(SWT.CTRL + 'Y');
		qdelqueryop = new Action("删除查询选项(&T)"){
			@Override
			public void run() {
				
			}
		};
		qdelqueryop.setAccelerator(SWT.CTRL + 'T');
		qref = new Action("刷新            F5"){
			@Override
			public void run() {
				Refresh();
			}
		};
		
		qdelqueryop.setEnabled(false);
		actEdit.setEnabled(false);
		actCopy.setEnabled(false);
		actDelete.setEnabled(false);
		
		upub = new Action("报告发布向导(&W)"){
			@Override
			public void run() {
				PublishedReportWizard a = new PublishedReportWizard(m_api);
				new WizardDialog(getShell(),a).open();
			}
		};
		upub.setAccelerator(SWT.CTRL + 'W');
		
	}
	
	// TODO ACTION 打开关闭
	protected void openclosecoolbar(boolean flag) {
		actEdit.setEnabled(flag);
		actCopy.setEnabled(flag);
		actDelete.setEnabled(flag);
		qrun.setEnabled(flag);
		
	}
	
	// TODO 刷新
	public void Refresh(){
		htCatToListerItems.clear();
		alldate.Clear();
		httypeToListerItems.clear();
		tbBusObList.removeAll();
		getcboQuickOblink(cboQuickOblink.getSelectionIndex());
		TabItem[] selected=tabFolder.getSelection();
		if(selected.length>0){
			if("类别".equals(selected[0].getText())){
				TableItem[] se=tbCategory.getSelection();
				if(se.length>0){
					addlistdate();
					addlisttabledate((String)se[0].getData());
				}else{
					addlistdate();
					addlisttabledate("");
				}
			}else if("查找".equals(selected[0].getText())){
				TreeItem[] findfreeselected=findtree.getSelection();
				if(findfreeselected.length>0){
					addfindtreedate();
					findtree.select(findfreeselected[0]);
					TreeItem[] rangetree=findtree.getItems();
					for(int i=0;i<rangetree.length;i++){
						TreeItem[] ran=rangetree[i].getItems();
						for(int j=0;j<ran.length;j++){
							TreeItem item=ran[j];
							if(item.getData().equals(findfreeselected[0].getData())){
								findtree.setSelection(item);
								findtree.showItem(item);
							}
						}
					}
					addtabledate((String)findfreeselected[0].getData(),"2");
				}else{
					addfindtreedate();
					addtabledate("16","2");
				}
			}else{
				TreeItem[] freeselected=treeRange.getSelection();
				if(freeselected.length>0){
					addtreedate();
					TreeItem[] rangetree=treeRange.getItems();
					for(int i=0;i<rangetree.length;i++){
						TreeItem[] ran=rangetree[i].getItems();
						for(int j=0;j<ran.length;j++){
							TreeItem item=ran[j];
							if(item.getData().equals(freeselected[0].getData())){
								treeRange.setSelection(item);
								treeRange.showItem(item);
							}
						}
					}
					addtabledate((String)freeselected[0].getData(),"1");
				}else{
					addtreedate();
					addtabledate("16","1");
				}
			}
		}
	}
	
	// TODO 删除报告
	protected boolean DeleteDefinition(IDefinition def){
        boolean flag = false;
        Siteview.Api.PerspectiveDef defForEditing = null;
        IViewOverrideInfo voi = null;
        if (def != null){
            RemoveDefinitionInformationFromRunMru(def);
            try{
                if (def.get_Perspective().equalsIgnoreCase("") || (def.get_Perspective().equalsIgnoreCase("(Base)"))){
                    IDefinition definition = null;
                    ICollection list = m_Library.GetPlaceHolderList(DefRequest.ForList(Siteview.Api.PerspectiveDef.get_ClassName()));
					IEnumerator it = list.GetEnumerator();
					while(it.MoveNext()){
						PlaceHolder holder = (PlaceHolder)it.get_Current();
                        defForEditing = (PerspectiveDef) m_Library.GetDefForEditing(DefRequest.ById(holder.get_DefClassName(), holder.get_Id()));
                        if (defForEditing != null){
                            voi = defForEditing.GetViewForDefinitionById(def.get_InstanceClassName(), def.get_Id());
                            if ((voi != null) && (voi.get_Perspective().equalsIgnoreCase("(Base)"))){
                                definition = m_Library.GetDefinition(DefRequest.ById(def.get_Scope(), def.get_ScopeOwner(), def.get_LinkedTo(), def.get_InstanceClassName(), def.get_Id(), defForEditing.get_Name(), false));
                                if (definition != null){
                                    definition = definition.CloneForEdit();
                                    m_Library.MarkDefinitionForDeletion(definition);
                                }
                                defForEditing.RemoveOverride(voi);
                                m_Library.UpdateDefinition(defForEditing);
                            }
                        }
                    }
                }else{
                    defForEditing = (PerspectiveDef) m_Library.GetDefForEditing(DefRequest.ByName(Siteview.Api.PerspectiveDef.get_ClassName(), def.get_Perspective()));
                    if (defForEditing != null){
                        voi = defForEditing.GetViewForDefinitionById(def.get_InstanceClassName(), def.get_Id());
                        if (voi != null){
                            defForEditing.RemoveOverride(voi);
                            m_Library.UpdateDefinition(defForEditing);
                        }
                    }
                }
                m_Library.MarkDefinitionForDeletion(def);
                flag = true;
            }catch (SiteviewException e){
            	//
            }
        }
        return flag;
    }
	protected void RemoveDefinitionInformationFromRunMru(IDefinition def){
        if (def != null){
            IMru mru = m_api.get_SettingsService().get_Mru();
            if (mru != null){
                String strMruId = (def.get_LinkedTo() != "") ? "AutoTaskDef.Run."+def.get_LinkedTo() : "AutoTaskDef.Run";
                mru.RemoveItem(strMruId, XmlScope.CategoryToXmlCategory(def.get_Scope())+","+def.get_ScopeOwner()+","+def.get_LinkedTo()+","+def.get_Folder()+","+def.get_Id()+","+def.get_Name()+","+def.get_Alias()+","+def.get_Perspective());
                mru.Flush(strMruId);
            }
        }
    }
	
	public AutoTaskDef getSelection() {
		IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(selection));
		if ((defForEditing != null) && HasEditRight(defForEditing.get_Scope())){
			//taskdef=(AutoTaskDef) defForEditing;
			//return taskdef;
		}
		return null;
	}

	@Override
	public int open() {
		this.setBlockOnOpen(true);
		return super.open();
	}
	
	public String getM_strDefClassName() {
		return m_strDefClassName;
	}


	public ScopeUtil getM_ScopeUtil() {
		return m_ScopeUtil;
	}


	public String getM_moduleSecurityName() {
		return m_moduleSecurityName;
	}


	public ISiteviewApi getM_api() {
		return m_api;
	}


	public IList getM_lstSupportedScopesWithOwners() {
		return m_lstSupportedScopesWithOwners;
	}


	public DefinitionLibrary getM_Library() {
		return m_Library;
	}


	public Combo getCboQuickOblink() {
		return cboQuickOblink;
	}


	public Map<String, ArrayList> getHttypeToListerItems() {
		return httypeToListerItems;
	}

	
	
	private boolean HasEditRight(Integer s) {
		return (s != Scope.Unknown)&&m_api.get_SecurityService().HasModuleItemRight(m_moduleSecurityName, XmlScope.CategoryToXmlCategory(s),SecurityRight.Edit);
	}
}
