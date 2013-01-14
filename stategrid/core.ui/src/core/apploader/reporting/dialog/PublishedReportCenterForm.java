package core.apploader.reporting.dialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import Siteview.CrystalSettingsManager;
import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.IMru;
import Siteview.IViewOverrideInfo;
import Siteview.PlaceHolder;
import Siteview.PublishedReportDef;
import Siteview.ReportDef;
import Siteview.RptParameterDef;
import Siteview.SiteviewException;
import Siteview.StringUtils;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.PerspectiveDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Windows.Forms.ListerItem;
import Siteview.Windows.Forms.ListerItemCollection;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;
import Siteview.Xml.XmlScope;
import Siteview.Xml.XmlSecurityRight;

public class PublishedReportCenterForm extends ApplicationWindow{

	/*
	 * �������ݽӿ�
	 */
	private static String m_strDefClassName;  		// C#��ѯ����ʱ�Ĺ̶�����
	private ScopeUtil m_ScopeUtil;     				// ��ʱ��  ��ѯ��Χ����
	private static String m_moduleSecurityName;  	// C#��ѯ����ʱ�Ĺ̶�����
	private PerspectiveDef perspectiveDef;  		// ����
	private PublishedReportCenterForm quickcenter;	// �����򴰿�
	private ISiteviewApi m_api;  					// ��ʼ���ӿ�
	private IList m_lstSupportedScopesWithOwners;
	private DefinitionLibrary m_Library ;
	private IDefinition m_Def;
	private ReportDef reportDef; 
    private PublishedReportDef m_defPubRpt;
	
	private Table tbBusObList;
	private Combo cboQuickOblink;  					//���ٲ�������ʽ  
	private Combo cb;              					//�����ı�
	private String findtext;       					//��������
	private TabFolder tabFolder;   					//ѡ��ļ���
	private TabItem tabFo;							//�ļ���ѡ��ҳ
	private TabItem tbCategoryy ;					//���ѡ��ҳ
	private Table tbCategory;
	
	private Button btOK;  							//���水ť
	private HashSet<String> Categoryset = new HashSet<String>(); //����ȫ�����ļ���
	
	private ArrayList alldate = new ArrayList();   //�������Ĺ��������е�����
	private SashForm sashForm;
	
	//�����˵�
	private Action actAdd;   			//�½�
	private Action actEdit;  			//�༭
	private Action actCopy;  			//����
	private Action actDelete;   		//ɾ��
	private Action actSearch;
	private Action qrun;    			//����
	//��ͼ�˵�
	private Action qbigicon;   			//��ͼ��
	private Action qsmallicon; 			//�б�
	private Action qdetailed;  			//��ϸ�б�
	private Action qdelqueryop;  		//ɾ����ѯѡ ��
	private Action qref;   				//ˢ��
	//����
	private Action upub;  				//���淢����(&W)
	
	// ��Χ
	private Tree tree;       			//�ļ� �� ���ṹ
	private Map<String,ArrayList> htCatToListerItems = new HashMap<String,ArrayList>();
	
	private Map<String,ArrayList> findhtCatToListerItems = new HashMap<String,ArrayList>();
	//���
	private Map<String,ArrayList> httypeToListerItems = new HashMap<String,ArrayList>();
	//Folder
	private Map<String,ArrayList> folderToListerItems = new HashMap<String,ArrayList>();
	private ArrayList folderlist = new ArrayList();
	private ListerItemCollection floder_aItems = null;
	
	//����
	private Tree findtree;       		// ����  ���ṹ
	private TabItem tabFind=null;

	private boolean showButtons=false; 	// �Ƿ���ʾȷ��ȡ����ť
	private PlaceHolder backselection;	// ���صļ���
	private ArrayList backParList;		// ���صĲ�������
	
	private ArrayList m_aParamDefs;
	
	//Ĭ�ϵ�һ�β�ѯ
	private Object strcontext;
	
	/**
	 * Create the application window,
	 * @wbp.parser.constructor
	 */
	
	public PublishedReportCenterForm(boolean flag,Object strcontext,ReportDef rDef) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.MIN | SWT.RESIZE | SWT.APPLICATION_MODAL);
		quickcenter=this;
		this.strcontext = strcontext;
		createActions();
		addCoolBar(SWT.FLAT);
		showButtons=flag;
		reportDef = rDef;
		Initializeddate();
	}
	
	/**
	 * ��ʼ��һЩ��������
	 * @return
	 */
	public void Initializeddate(){
		this.m_strDefClassName = "PublishedReportDef";  				// C#��ѯ����ʱ�Ĺ̶�����
		this.m_moduleSecurityName = "Core.Security.Reports";  			// C#��ѯ����ʱ�Ĺ̶�����
		this.m_api = ConnectionBroker.get_SiteviewApi();
		this.m_Library = ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary();
		this.m_ScopeUtil = new ScopeUtil(m_api, m_Library, m_strDefClassName, m_moduleSecurityName);
		this.m_ScopeUtil.set_CheckRights(true);
		this.m_lstSupportedScopesWithOwners = this.m_ScopeUtil.GetSupportedScopesWithOwners(XmlSecurityRight.ToRight("VAED"), true);
		this.quickcenter=this;
		this.m_aParamDefs = new ArrayList();
	}
	

	/**
	 * Configure the shell.
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
		newShell.setText("�ѷ�������   ����");
		newShell.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublished.png")));
	}


	/**
	 * Create contents of the application window.
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
			btOK.setText("ȷ��");
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
					On_OK();
				}});
			
			Button btCancel = new Button(cBottom,SWT.NONE);
			btCancel.setText("ȡ��");
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
		
		tbCategoryy = new TabItem(tabFolder, SWT.NONE);
		tbCategoryy.setText("���");
		tbCategoryy.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.CategoryReport.png"),0x12,0x12));

		tabFo = new TabItem(tabFolder, SWT.NONE);
		tabFo.setText("�ļ���");
		tabFo.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png"),0x12,0x12));
		
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(htCatToListerItems.isEmpty())
				return;
				TabItem[] selected=tabFolder.getSelection();
				if(selected.length>0){
					if("���".equals(selected[0].getText())){
						TableItem[] se=tbCategory.getSelection();
						if(se.length>0){
							addlisttabledate(se[0].getData()+"");
						}else{
							addlisttabledate("");
						}
					}else if("����".equals(selected[0].getText())){
						TreeItem[] findfreeselected=findtree.getSelection();
						if(findfreeselected.length>0){
							findtree.showItem(findfreeselected[0]);
							addtabledate(findfreeselected[0].getData().toString(),"2");
						}else{
							addtabledate("16","2");
						}
					}else{
						addtreedate();
						TreeItem[] findfreeselected=tree.getSelection();
						if(findfreeselected.length>0){
							addtreetabledate(findfreeselected[0]);
						}else{
							tree.setSelection(tree.getItem(0));
							addtreetabledate(tree.getItem(0));
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
		tbBusObList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Object ob = method();
				if(ob instanceof ListerItem){
					TreeItem ti = tree.getSelection()[0];
					for(int i=0;i<ti.getItemCount();i++){
						if(ti.getItem(i).getText().trim().equalsIgnoreCase(((ListerItem) ob).get_Text().trim())){
							tree.setSelection(ti.getItem(i));
							addtreetabledate(ti.getItem(i));
						}
					}
				}else{
					On_OK();
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
						if("����".equals(selected[0].getText())){
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
							TreeItem[] rangetree=tree.getItems();
							for(int i=0;i<rangetree.length;i++){
								TreeItem[] ran=rangetree[i].getItems();
								for(int j=0;j<ran.length;j++){
									TreeItem item=ran[j];
									if(item.getData().toString().equals(str)){
										tree.setSelection(item);
										tree.showItem(item);
									}
								}
							}
							addtabledate(str,"1");
						}
					}
				}else {
					if(showButtons){
						backselection = (PlaceHolder)ob;
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
		tc1.setText("����");
		tc1.setWidth(260);
		TableColumn tc2 = new TableColumn(tbBusObList,SWT.NONE);
		tc2.setText("����");
		tc2.setWidth(300);
		TableColumn tc3 = new TableColumn(tbBusObList,SWT.NONE);
		tc3.setText("���");
		tc3.setWidth(200);
		
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
				//�Ҽ��˵� ͣ��
				
		}});
		
		firstloaddate();
		return container;
	}

	
	/**
	 * ��һ��Ĭ�ϼ��� ���� ����
	 */
	public void firstloaddate(){
		//�õ����
		getcboQuickOblink(0);
		//��������б�
		addlistdate();
		//�õ��ļ���
		floder_aItems = GetNavFolderItems();
		//�����ļ�����
		addtreedate();
		//��������Ӧ�ұ�����
		addlisttabledate("");
	}

	/**
	 * ���ؿ��ٲ������ĵ�����ʽ
	 */
	protected void loadObject() {
		ICollection busLinks = m_Library.GetPlaceHolderList(DefRequest.ForList(BusinessObjectDef.get_ClassName()));
		IEnumerator it = busLinks.GetEnumerator();
		List<String> bobs = new Vector<String>();
		bobs.add(" δ����");
		cboQuickOblink.setData(" δ����","");
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
	 *   ����ʽ����¼� �ȳ�ʼ������һЩ����
	 */
	public void cboQuickOblinkSelected() {
		htCatToListerItems.clear();
		alldate.Clear();
		httypeToListerItems.clear();
		folderToListerItems.clear();
		//�õ����
		getcboQuickOblink(0);
		//��������б�
		addlistdate();
		//�õ��ļ���
		floder_aItems = GetNavFolderItems();
		//�����ļ�����
		addtreedate();
		//��������Ӧ�ұ�����
		addlisttabledate("");
	}
	
	/**
	 * ����ѡ������ʽ  װ�ع����ķ�Χ������  
	 */
	
	public void getcboQuickOblink(int linkIndex){
		if (linkIndex < 0) return;
		String strBusLink = "";//(String) cboQuickOblink.getData(cboQuickOblink.getItem(linkIndex));
		IEnumerator it = m_lstSupportedScopesWithOwners.GetEnumerator();
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
	 * ��ȡ���������ݽ��з��� ���浽 MAP ������
	 * @param s Scope 
	 * @param Owner 
	 * @param linkTo ������ҵ����������
	 * @param bFilter �Ƿ����
	 */
	protected ArrayList GetPlaceHolders(int s, String strOwner,String linkTo){
		ArrayList placeHolderList = null;
        if ((m_api != null) && (s != Scope.Unknown)){
            try{
                String strPerspective = (this.perspectiveDef != null) ? this.perspectiveDef.get_Name() : "(Base)";
                placeHolderList=(ArrayList) m_Library.GetPlaceHolderList(DefRequest.ForList(s,strOwner,linkTo,m_strDefClassName,strPerspective));
            }catch (SiteviewException e){
            	//
            }
        }
        return placeHolderList;
    }
	
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
	
	private ListerItemCollection GetNavFolderItems(){
        ListerItemCollection items = new ListerItemCollection();
        ICollection placeHolders = this.GetPlaceHolders(Scope.Global, "GLOBAL","");
        if (placeHolders != null){
        	IEnumerator it = placeHolders.GetEnumerator();
        	while(it.MoveNext()){
        		PlaceHolder holder = (PlaceHolder) it.get_Current();
            	PublishedReportDef def = (PublishedReportDef) this.GetDefinition(holder.get_Name(), Scope.Global, "GLOBAL","");
                if (def != null){
                    ListerItemCollection aItems = items;
                    String[] strArray = def.get_Filename().split("\\\\");
                    int length = strArray.length;
                    for (int i = 0; i < length; i++){
                        String strName = strArray[i];
                        if (i != (length - 1)){
                            ListerItem item = this.ContainsFolder(aItems, strName);
                            if (item == null){
                                item = new ListerItem("Folder", strName, strName, 1, true, false, null);
                                aItems.Add(item);
                            }
                            if (item != null){
                                aItems = item.get_Items();
                            }
                        }else{
                            ListerItem item2 = new ListerItem("PlaceHolder", strName, strName, 0, false, true, holder);
                            aItems.Add(item2);
                        }
                    }
                }
            }
        }
        return items;
    }

	private ListerItem ContainsFolder(ListerItemCollection aItems, String strName){
		for(int i = 0;i < aItems.get_Count();i++){
			ListerItem item2 = (ListerItem) aItems.get_Item(i);
			if (item2.get_Text().trim().equalsIgnoreCase(strName.trim())){
                return item2;
            }
		}
        return null;
    }
	
	protected IDefinition GetDefinition(String strName, int scope, String strScopeOwner,String linkTo){
        IDefinition definition = null;
        if (scope != Scope.Unknown){
            try{
                definition = this.m_Library.GetDefinition(DefRequest.ByName(scope, strScopeOwner, linkTo, m_strDefClassName, strName, false));
            }catch (SiteviewException e){
            	//
            }
        }
        return definition;
    }
	/**
	 *  ��̬�Ĵ��� �ļ���  �� ���ṹ
	 */
	public void addtreedate(){
		tree = new Tree(tabFolder, SWT.SINGLE);
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] selected=tree.getSelection();
				if(selected.length>0){
					addtreetabledate(selected[0]);
				}
				
			}
		});
		
		FillNavTreeView(tree,null,floder_aItems);
		tabFo.setControl(tree);
	}
	
    private boolean FillNavTreeView(Tree tv, TreeItem t, ListerItemCollection aItems){
        boolean flag = false;
        IEnumerator it = aItems.GetEnumerator();
    	while(it.MoveNext()){
    		ListerItem item = (ListerItem) it.get_Current();
            if (!item.get_IsTerminal()){
            	String str = item.get_Text();
            	TreeItem treeItem = null;
            	if(t==null){
            		treeItem = new TreeItem(tv, SWT.SINGLE);
        			treeItem.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png"),0x12,0x12));
        			treeItem.setText(str);
        			treeItem.setData(item);
            	}else{
            		treeItem = new TreeItem(t, SWT.SINGLE);
        			treeItem.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png"),0x12,0x12));
        			treeItem.setText(str);
        			treeItem.setData(item);
            	}            	
                if (this.FillNavTreeView(tv, treeItem, item.get_Items())){
                	tv.setSelection(treeItem);
                }
            }
            else if (item.get_Selected())
            {
                flag = true;
            }
        }
        return flag;
    }
	/**
	 *   ��̬���ز������ṹ����
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
	 * �������ݹ���  
	 */
	public void Filterfinddate(){
		if (alldate!=null){
			findhtCatToListerItems.clear();
			for(int j=0;j<alldate.get_Count();j++){
				ArrayList obs=(ArrayList) alldate.get_Item(j);
				for(int i = 0; i < obs.get_Count();i ++){
					PlaceHolder holder = (PlaceHolder)obs.get_Item(i);
					PublishedReportDef def = (PublishedReportDef) this.GetDefinition(holder.get_Name(), holder.get_Scope(), "GLOBAL","");
					if((def.get_Filename().toUpperCase().indexOf(findtext)>=0)||(holder.get_Name().toUpperCase().indexOf(findtext)>=0)||(holder.get_Alias().toUpperCase().indexOf(findtext)>=0)||(holder.get_Description().toUpperCase().indexOf(findtext)>=0)||(holder.get_Folder().toUpperCase().indexOf(findtext))>=0){
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
	 *  
	 *  ������������
	 * 
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
				ti.setText("�������");
				ti.setData(str);
			}else
				ti.setText(str);
				ti.setData(str);
		}
	}
	
	/**
	 * ������ ����table����ʾ����ϸ��Ϣ
	 */
	public void addlisttabledate(String sel){
		tbBusObList.removeAll();
		ArrayList obs = httypeToListerItems.get(sel);
		if(obs!=null&&obs.get_Count()>0){
			for(int i = 0 ; i < obs.get_Count(); i++){
				PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
				TableItem ti = new TableItem(tbBusObList,SWT.NONE);
				ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublished.png"),0x12,0x12));
				ti.setText(new String[]{ph.get_Alias(),ph.get_Description()});
				ti.setData(ph);
			}
		}
	}

	/**
	 *   �����ʱ����table����ʾ����ϸ��Ϣ
	 *   @param type   =1 ��ʾ��Χ���������� =2 ��ʾ���ҵ����� = 3��ʾ�ļ�������
	 */
	
	public void addtabledate(String sel,String type){
		tbBusObList.removeAll();
		HashMap<String,ArrayList> dateItems=null;
	
		if("1".equals(type)){
			dateItems=(HashMap<String, ArrayList>) htCatToListerItems;
		}else if("3".equals(type)){
			dateItems=(HashMap<String, ArrayList>) folderToListerItems;
		}else if("2".equals(type)){
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
							ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublished.png"),0x12,0x12));
							ti.setText(new String[]{ph.get_Alias(),ph.get_Description(),""});
							ti.setData(ph);
						}
					}
				}else{
					String[] strsplit=str.split("~");
					if(stip[0].equals(strsplit[0])&&strsplit!=null&&strsplit.length>1){
							TableItem t2 = new TableItem(tbBusObList,SWT.NONE);
							t2.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png"),0x12,0x12));
							t2.setText(new String[]{strsplit[1],"",""});
							t2.setData(str);
					}
					
					if(stip[0].equals(strsplit[0])){
						ArrayList obs = dateItems.get(str);
						for(int i = 0 ; i < obs.get_Count(); i++){
							PlaceHolder ph = (PlaceHolder)obs.get_Item(i);
							if(StringUtils.IsEmpty(ph.get_Folder())){
								TableItem ti = new TableItem(tbBusObList,SWT.NONE);
								ti.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublished.png"),0x12,0x12));
								ti.setText(new String[]{ph.get_Alias(),ph.get_Description(),""});
								ti.setData(ph);
							}
						}
					}
					
				}
		}
	}
	
	private void addtreetabledate(TreeItem ti){
		if (ti != null){
			ListerItem  li = (ListerItem) ti.getData();
			if (li.get_TerminalsOk()){
                this.FillTargetView(ti.getText(), li.get_Items());
            }else{
            	// ����
            }
    	}
    }
	
	private void FillTargetView(String strNav, ListerItemCollection aItems){
		tbBusObList.removeAll();
        boolean flag = false;
        boolean flag2 = false;
        TableItem item = null;//new TableItem(tbBusObList,SWT.NONE);
        for(int i = 0;i<aItems.get_Count();i++){
        	ListerItem item2 = (ListerItem) aItems.get_Item(i);
            if (item2.get_IsTerminal()){
                flag = true;
                TableItem item3 = new TableItem(tbBusObList,SWT.NONE);
                item3.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublished.png"),0x12,0x12));
                item3.setData(item2);
                PlaceHolder ph = (PlaceHolder) item2.get_Tag();
                item3.setText(new String[]{ph.get_Alias(),ph.get_Description(),ph.get_Perspective().equals("(Base)")?"(һ��)":ph.get_Perspective()});
                if (item2.get_Selected()){
                    item = item3;
                }
                boolean flag3 = true;
                continue;
            }
            TableItem lvi = new TableItem(tbBusObList,SWT.NONE);
            lvi.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png"),0x12,0x12));
            lvi.setData(item2);
            lvi.setText(new String[]{item2.get_Text(),"",""});
            
            if(item2.get_Selected()){
            	flag2 = true;
            	tbBusObList.setSelection(lvi);
            	
            	//lvi.addListener(eventType, listener)
            }
        }
        if (!flag2){
            if (item != null){
            	tbBusObList.setSelection(item);
            }
        }
    }

	/**
	 * �����˵� ͣ��
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		
		MenuManager menuBar = new MenuManager();			// �����ò˵�������
		MenuManager OpMenu = new MenuManager("����(A)"); 	// �����˵���
		MenuManager ViewMenu = new MenuManager("��ͼ(v)"); 	// ��ͼ�˵���
		MenuManager UtMenu = new MenuManager("Ӧ��(U)");		// Ӧ�ò˵���
		
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
	 * ����������.
	 * @return the coolbar manager
	 */
	protected CoolBarManager createCoolBarManager(int style) {
		CoolBarManager coolBarManager = new CoolBarManager(style);
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBarManager.add(new ToolBarContributionItem(toolbar, "main"));   
//		toolbar.add(qrun);		//����
//		toolbar.add(actAdd);	//�½�
//		toolbar.add(actEdit);	//�༭
//		toolbar.add(actCopy);	//����
//		toolbar.add(actDelete);	//ɾ��
//		toolbar.add(new Separator());
		ControlContribution cc = new ControlContribution(""){
			@Override
			protected Control createControl(Composite parent) {
				Composite container = new Composite(parent,SWT.NONE);
				Label searchLabel=new Label(container,SWT.NONE);
				searchLabel.setSize(30,20);
				searchLabel.setLocation(5,5);
				searchLabel.setText("����:");
				cb = new Combo(container,SWT.NONE);
				cb.setSize(150,20);
				cb.setLocation(35,2);
				return container;
			}};
		toolbar.add(cc);
		toolbar.add(actSearch);
		
//		IToolBarManager toolbar2 = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
//		coolBarManager.add(new ToolBarContributionItem(toolbar2, "linkObject"));
//		toolbar2.add(new ControlContribution(""){
//
//			@Override
//			protected Control createControl(Composite parent) {
//				Composite container = new Composite(parent,SWT.NONE);
//				Label lblLinkTxt = new Label(container,SWT.NONE);
//				lblLinkTxt.setSize(225,20);
//				lblLinkTxt.setLocation(5,5);
//				lblLinkTxt.setText("���������¶��������   ����:");
//				
//				cboQuickOblink = new Combo(container,SWT.READ_ONLY);
//				cboQuickOblink.setSize(180, 20);
//				cboQuickOblink.setLocation(230, 0);
//				cboQuickOblink.addSelectionListener(new SelectionListener(){
//
//					@Override
//					public void widgetSelected(SelectionEvent e) {
//						cboQuickOblinkSelected();
//					}
//
//					@Override
//					public void widgetDefaultSelected(SelectionEvent e) {
//						// TODO Auto-generated method stub
//						
//					}});
//				
//				return container;
//			}});
		return coolBarManager;
	}

	
	/**
	 * ����Action 
	 */
	private void createActions(){
		actAdd = new Action("�½�(&N)"){
			@Override
			public void run() {
				TreeItem[] selected=tree.getSelection();
				
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
//				ReportDialog report = new ReportDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,m_Def, cboQuickOblink.getData(cboQuickOblink.getItem(cboQuickOblink.getSelectionIndex())).toString(), 1);
//				report.open();
			}
		};
		
		actAdd.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.NewAdd.png"),0x12,0x12).getImageData();
			}});
		actAdd.setAccelerator(SWT.CTRL + 'N');
		
		actEdit = new Action("�༭(&E)"){
			@Override
			public void run() {
				
				if (tbBusObList.getSelection().length < 1) return;
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				
				if ((defForEditing != null) && HasEditRight(defForEditing.get_Scope())){
					reportDef=(ReportDef) defForEditing;
					
//					Dialog dbPart = new DashboradDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,null, ph.get_LinkedTo(), 2);
//					dbPart.open();
				}
			}
		};
		actEdit.setAccelerator(SWT.CTRL + 'E');
		
		actEdit.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Edit.png"),0x12,0x12).getImageData();
			}});
		
		actCopy = new Action("����(&F)"){
			@Override
			public void run() {
				if (tbBusObList.getSelection().length < 1) return;
				TreeItem[] selected=tree.getSelection();
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
//				DashboradDialog dashbord=new DashboradDialog(quickcenter,getShell(), reportDef,m_api, m_Library, m_ScopeUtil,m_Def, cboQuickOblink.getData(cboQuickOblink.getItem(cboQuickOblink.getSelectionIndex())).toString(),3);
//				dashbord.open();
			}
		};
		actCopy.setAccelerator(SWT.CTRL + 'F');
		
		actCopy.setImageDescriptor(new ImageDescriptor(){
			@Override
			public ImageData getImageData() {
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[Image]Core#Images.Icons.Copy.png"),0x12,0x12).getImageData();
			}});
		
		
		actDelete = new Action("ɾ��(&D)"){
			@Override
			public void run() {


				if (tbBusObList.getSelection().length < 1) return;
				TableItem ti = tbBusObList.getSelection()[0];
				PlaceHolder ph = (PlaceHolder)ti.getData();
				IDefinition def = m_Library.GetDefForEditing(DefRequest.ByHolder(ph));
				
				if ((def != null) && HasEditRight(def.get_Scope())){
					
					RemoveDefinitionInformationFromRunMru(def);
					PerspectiveDef defForEditing=null;
					
					IViewOverrideInfo voi = null;
							
					if(StringUtils.IsEmpty(def.get_Perspective())||"(Base)".equals(def.get_Perspective())){
						
						
						IDefinition definition = null;
						
						ICollection list=m_Library.GetPlaceHolderList(DefRequest.ForList(Siteview.Api.PerspectiveDef.get_ClassName()));
						
						IEnumerator it = list.GetEnumerator();
						while(it.MoveNext()){
							PlaceHolder holder = (PlaceHolder)it.get_Current();
							defForEditing=(PerspectiveDef) m_Library.GetDefForEditing(DefRequest.ById(holder.get_DefClassName(), holder.get_Id()));
							
							if(defForEditing!=null){
								
								 voi = defForEditing.GetViewForDefinitionById(def.get_InstanceClassName(), def.get_Id());
								 
								 if ((voi != null) && !"(Base)".equals(voi.get_Perspective())){
                                   definition = m_Library.GetDefinition(DefRequest.ById(def.get_Scope(), def.get_ScopeOwner(), def.get_LinkedTo(), def.get_InstanceClassName(), def.get_Id(), defForEditing.get_Name(), false));
                                   if (definition != null){
                                       definition = definition.CloneForEdit();
                                       m_Library.MarkDefinitionForDeletion(definition);
//                                   }
                                   defForEditing.RemoveOverride(voi);
                                   m_Library.UpdateDefinition(defForEditing);
                                   }
                               }
								
							}
						}
					}else{
						 defForEditing = (PerspectiveDef) m_Library.GetDefForEditing(DefRequest.ByName(Siteview.Api.PerspectiveDef.get_ClassName(), def.get_Perspective()));
						 if (defForEditing != null){
							 voi = defForEditing.GetViewForDefinitionById(def.get_InstanceClassName(), def.get_Id());
							 defForEditing.RemoveOverride(voi);
							 m_Library.UpdateDefinition(defForEditing);
						 }
					}
					
					m_Library.MarkDefinitionForDeletion(def);
	 		
					//ˢ��
					Refresh();
					
				}
			}
		};
		qrun = new Action("����(&R)"){
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
		
		actSearch = new Action("����"){
			@Override
			public void run() {
				findtext=cb.getText().toUpperCase();
				if(StringUtils.IsEmpty(findtext))return;
				int count=tabFolder.getItemCount();
				if(count<3){
					tabFind = new TabItem(tabFolder, SWT.NONE);
					tabFolder.setSelection(tabFind);
					tabFind.setText("����");
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
		
		qbigicon = new Action("��ͼ��(&I)"){
			@Override
			public void run() {
				
			}
		};
		
		qbigicon.setAccelerator(SWT.CTRL + 'I');
		qsmallicon = new Action("�б�(&L)"){
			@Override
			public void run() {
				
			}
		};
		qsmallicon.setAccelerator(SWT.CTRL + 'L');
		qdetailed = new Action("��ϸ�б�(&Y)"){
			@Override
			public void run() {
				
			}
		};
		qdetailed.setAccelerator(SWT.CTRL + 'Y');
		qdelqueryop = new Action("ɾ����ѯѡ��(&T)"){
			@Override
			public void run() {
				
			}
		};
		qdelqueryop.setAccelerator(SWT.CTRL + 'T');
		qref = new Action("ˢ��            F5"){
			@Override
			public void run() {
				Refresh();
			}
		};
		
		qdelqueryop.setEnabled(false);
		actEdit.setEnabled(false);
		actCopy.setEnabled(false);
		actDelete.setEnabled(false);
		
		upub = new Action("���淢����(&W)"){
			@Override
			public void run() {
			}
		};
		upub.setAccelerator(SWT.CTRL + 'W');
		
	}
	
	
	/**
	 * 
	 * ACTION �򿪹ر�
	 */
	
	protected void openclosecoolbar(boolean flag) {
		actEdit.setEnabled(flag);
		actCopy.setEnabled(flag);
		actDelete.setEnabled(flag);
		qrun.setEnabled(flag);
		
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
	
	
	
	protected void RemoveDefinitionInformationFromRunMru(IDefinition def)
    {
        if (def != null)
        {
            IMru mru = m_api.get_SettingsService().get_Mru();
            if (mru != null)
            {
                String strMruId = (def.get_LinkedTo() != "") ? "AutoTaskDef.Run."+def.get_LinkedTo() : "AutoTaskDef.Run";
               
              
                mru.RemoveItem(strMruId, XmlScope.CategoryToXmlCategory(def.get_Scope())+","+def.get_ScopeOwner()+","+def.get_LinkedTo()+","+def.get_Folder()+","+def.get_Id()+","+def.get_Name()+","+def.get_Alias()+","+def.get_Perspective());
                mru.Flush(strMruId);
            }
        }
    }
	
	
	/**
	 * ˢ��
	 */
	
	public void Refresh(){
		
		htCatToListerItems.clear();
		alldate.Clear();
		httypeToListerItems.clear();
		tbBusObList.removeAll();
		getcboQuickOblink(cboQuickOblink.getSelectionIndex());
		
		TabItem[] selected=tabFolder.getSelection();
		if(selected.length>0){
			
			if("���".equals(selected[0].getText())){
				TableItem[] se=tbCategory.getSelection();
				if(se.length>0){
					addlistdate();
					addlisttabledate((String)se[0].getData());
					
				}else{
					addlistdate();
					addlisttabledate("");
				}
				
			}else if("����".equals(selected[0].getText())){
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
				
				TreeItem[] freeselected=tree.getSelection();
				if(freeselected.length>0){
					addtreedate();
					
					TreeItem[] rangetree=tree.getItems();
					for(int i=0;i<rangetree.length;i++){
						
						TreeItem[] ran=rangetree[i].getItems();
						
						for(int j=0;j<ran.length;j++){
							TreeItem item=ran[j];
							if(item.getData().equals(freeselected[0].getData())){
								tree.setSelection(item);
								tree.showItem(item);
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
	
	public AutoTaskDef getSelection() {
		IDefinition defForEditing = m_Library.GetDefForEditing(DefRequest.ByHolder(backselection));
		if ((defForEditing != null) && HasEditRight(defForEditing.get_Scope())){
//			taskdef=(AutoTaskDef) defForEditing;
//			return taskdef;
		}
		return null;
	}
		


	@Override
	public int open() {
		this.setBlockOnOpen(true);
		return super.open();
	}

	
	private Dialog getPartType(String partType){
		
		return null;
	}
	
	

	public PlaceHolder getBackselection() {
		return backselection;
	}


	public void setBackselection(PlaceHolder backselection) {
		this.backselection = backselection;
	}

	
	
	public ReportDef getReportDef() {
		return reportDef;
	}


	public void setReportDef(ReportDef reportDef) {
		this.reportDef = reportDef;
	}


	public PublishedReportDef getM_defPubRpt() {
		return m_defPubRpt;
	}


	public void setM_defPubRpt(PublishedReportDef m_defPubRpt) {
		this.m_defPubRpt = m_defPubRpt;
	}

	
	public ArrayList getBackParList() {
		return backParList;
	}


	public void setBackParList(ArrayList backParList) {
		this.backParList = backParList;
	}

	
	public ArrayList getM_aParamDefs() {
		return m_aParamDefs;
	}


	public void setM_aParamDefs(ArrayList m_aParamDefs) {
		this.m_aParamDefs = m_aParamDefs;
	}


	private void On_OK(){
		if(tbBusObList.getSelectionIndex()==-1)return;
		TableItem ti = tbBusObList.getSelection()[0];
		if (tbBusObList.getSelectionCount() < 1){
			if(!(ti.getData() instanceof PlaceHolder)){
				MessageDialog.openInformation(getShell(), "��ʾ", "��ѡ�񱨸�ģ�塣");
				return;
			}
		}else{
			backselection = (PlaceHolder) ti.getData();
			OpenReport_back(backselection);
		}
		setReturnCode(OK);
		close();
	}
	private void OpenReport_back(PlaceHolder selectedObject){
		if (selectedObject != null){
			this.reportDef.set_ReportName(selectedObject.get_Name());
            this.m_defPubRpt = null;
            this.m_defPubRpt = (PublishedReportDef) this.GetDefinition(this.reportDef.get_ReportName(), Scope.Global, "GLOBAL", "PublishedReportDef");
            if (this.reportDef.get_ReportName() != ""){
                if (this.m_defPubRpt!=null && (this.m_defPubRpt.get_Filename() != "")){
                    this.OpenReport(this.m_defPubRpt.get_Filename());
                }
            }
        }
    }

	public void OpenPublishedReportDefAndReport(){
        this.m_defPubRpt = null;
        if (this.reportDef.get_ReportName() != ""){
        	this.m_defPubRpt = (PublishedReportDef) this.GetDefinition(this.reportDef.get_ReportName(), Scope.Global, "GLOBAL", "PublishedReportDef");
            if (this.m_defPubRpt.get_Filename() != ""){
                this.OpenReport(this.m_defPubRpt.get_Filename());
            }
        }
    }
	
	private boolean OpenReport(String strFilename)
    {
        boolean flag = false;
        System.out.println(strFilename);
        System.out.println(this.reportDef);
        System.out.println(m_api.get_ReportLibrary().get_CrystalSettingsManager().get_Server());
        System.out.println(m_api.get_ReportLibrary().get_CrystalSettingsManager().get_WebServiceURL());
        String file_path = "http://localhost:8080/birt/"+strFilename.replace("\\", "/")+"design";
        System.out.println(file_path);
		String aa = "";
		try {
			aa = URLEncoder.encode(strFilename,"UTF-8");
			System.out.println(aa);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		backParList = ReadRptDesign.read(file_path);
		AddParameterDefsIfNecessary();
		System.out.println(this.reportDef.get_ParameterDefinitions().get_Count());
		System.out.println(this.m_aParamDefs.get_Count());
        // ������rptdesign XML
        //if ((this.reportDef.get_ReportType() == ReportDef.ReportTypes.Server)){
        //    CrystalSettingsManager crystalSettingsManager = this.m_api.get_ReportLibrary().get_CrystalSettingsManager();
        //    crystalSettingsManager.set_ReadWriteServerInfo(true);
        //    crystalSettingsManager.set_ReadWriteLoginInfo(false);
        //    crystalSettingsManager.ReadSettings();
		//    this.m_RptWrapper = crystalSettingsManager.get_UseRas() ? ((IReportWrapper) new CrystalRasWrapper(base.Api)) : ((IReportWrapper) new CrystalWebServiceWrapper(this.m_api));
        //}
        return flag;
    }
	
	private ArrayList GetDefsFromParameters(){
		ArrayList list = new ArrayList();
		//ParameterFields parameterFieldInfo
		for(int i=0;i<backParList.get_Count();i++){
			rptDesign_form field = (rptDesign_form) backParList.get_Item(i);
//        {
            RptParameterDef def = (RptParameterDef) RptParameterDef.DeserializeCreateNewForEditing("");
            def.set_Name(field.getParameter_name());
//            def.ParameterKind = (field.DiscreteOrRangeKind == DiscreteOrRangeKind.DiscreteValue) ? RptParameterDef.ParameterKinds.Discrete : ((field.DiscreteOrRangeKind == DiscreteOrRangeKind.RangeValue) ? RptParameterDef.ParameterKinds.Range : RptParameterDef.ParameterKinds.DiscreteOrRange);
            boolean flag = (field.getParamType().equalsIgnoreCase("multi-value")) ? true : false;
           	def.set_AllowMultipleValues(flag);
//            def.AllowMultipleValues = field.EnableAllowMultipleValue;
            list.Add(def);
        }
		return list;
	}
	
	public void AddParameterDefsIfNecessary(){
		this.m_aParamDefs.Clear();
        IList defsFromParameters = null;
        try{
            defsFromParameters = GetDefsFromParameters();
        }catch (SiteviewException exception){
            //MessageBox.Show(exception.Message, LegalUtils.MessageBoxCaption, MessageBoxButtons.OK, MessageBoxIcon.Hand);
        }
        if (defsFromParameters != null){
        	for(int i= 0;i< defsFromParameters.get_Count();i++){
        		RptParameterDef def = (RptParameterDef) defsFromParameters.get_Item(i);
                RptParameterDef def2 = this.FindParameterDefByName(def.get_Name());
                if (def2 != null){
                    def2.set_ParameterKind(def.get_ParameterKind());
                    def2.set_AllowMultipleValues(def.get_AllowMultipleValues());
                    this.m_aParamDefs.Add(def2);
                }else{
                    this.reportDef.get_ParameterDefinitions().Add(def);
                    this.m_aParamDefs.Add(def);
                }
            }
        }
    }
	private RptParameterDef FindParameterDefByName(String strName){
		for(int i= 0;i< this.reportDef.get_ParameterDefinitions().get_Count();i++){
        	RptParameterDef def2 = (RptParameterDef) this.reportDef.get_ParameterDefinitions().get_Item(i);
            if (def2.get_Name() == strName){
                return def2;
            }
        }
        return null;
    }

}
