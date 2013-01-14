package core.apploader.reporting.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Type;
import system.Collections.ArrayList;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Collections.IList;
import system.Text.RegularExpressions.Regex;
import Siteview.DefRequest;
import Siteview.DefinitionObject;
import Siteview.ExceptionUtils;
import Siteview.IDefinition;
import Siteview.MultipleBusObQueryGroupDef;
import Siteview.PlaceHolder;
import Siteview.PublishedReportDef;
import Siteview.QueryGroupDef;
import Siteview.ReportDef;
import Siteview.RptParamDefaultValDef;
import Siteview.RptParamDiscreteValDef;
import Siteview.RptParamRangeValDef;
import Siteview.RptParamValueDef;
import Siteview.RptParameterDef;
import Siteview.ScopedDefinitionObject;
import Siteview.SiteviewException;
import Siteview.SiteviewQuery;
import Siteview.StringUtils;
import Siteview.TagConversionHelper;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.FieldTag;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.PerspectiveDef;
import Siteview.Api.TagContext;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Windows.Forms.ScopingCtrl;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;
import Siteview.Xml.XmlSecurityRight;
import core.apploader.reporting.ReportCenterForm;
import core.apploader.search.comm.CommEnum.ExpressType;
import core.apploader.search.composite.AbstractExpress;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class ReportDialog extends Dialog {
	// TODO 参数定义
	private TabFolder tabFolder ;			// Tab页
	private TabItem m_RptParameters;		// 参数页
	//一般
	private Text txtName;					// 名称
	private Text txtDesc;					// 说明
	private Combo cbScope;					// 范围下拉式
	private Combo cbCategory;				// 类别下拉式
	private Combo cbOwner;					// 负责人下拉式
	private Combo cbLinkedTo;				// 关联下拉式
	private Button Checked_Dh;				// 在导航显示
	private Button Checked_Cd;				// 在菜单显示
	private Text txtAlias;					//
	private Button btn_search ;				// 浏览
	private Text txtFile_name;				// 报告文件名
	//报告参数
	private Tree parametersTree;			// 参数树
	private Button btn_del;					// 删除
	private Combo cbtype;					// 类别
	private Combo cbctype;					// 子类别
	private List tflist;					// 列表
	private Button checked_link;			// 显示链接字段
	private Button checked_hid;				// 显示隐藏字段
	
	private Button btnDefaultValued;		// 默认值选项
	private Text txtdefv;					// 默认值
	private Button btnDiscreteValue;		// 离散值选项
	private Text txtdisv;					// 离散值
	private Button btnRangeValue;			// 范围值选项
	private Text txtfrv;					// 范围值From
	private Text txttrv;					// 范围什To
	private Combo cbfbt;					// Bound Type
	private Combo cbtbt;					// Bound Type
	private Group grpvalues;				// 参数分组
	private Label lblFrom;					// From标签
	private Label lblto;					// To标签
	private Label lblboundType;				// 绑定标签
	
	private DefinitionLibrary m_Library;
	private IDefinition m_Def;
	private ScopeUtil m_ScopeUtil; 			// 暂时叫 查询范围的类
	private ISiteviewApi m_api; 			// 初始化接口
	private PerspectiveDef perspectiveDef;
	private IList m_lstSupportedScopesWithOwners;
	private ReportDef reportDef;
	private String linkstr; 				// 下拉式 选 项
	private ReportCenterForm reportcenter;
	private ArrayList m_aParamDefs = new ArrayList();
	private ArrayList m_aRootDefs = new ArrayList();
	private boolean m_bShowParameters = true;
	private boolean bNodeEmpty;
	private boolean m_bUpdateSelection;
	private PublishedReportDef m_defPubRpt;
	
	private HashSet Categoryset = new HashSet(); 	// 类别
	private int type = 1;					// 标志位 1=添加 2=编辑 3=复制
	
	private TagContext context;
	private Generalmethods generalmethods;
	
	// TODO 入口
	public ReportDialog(ReportCenterForm reportcenter,
			Shell parentShell, ReportDef reportdef, ISiteviewApi m_api,
			DefinitionLibrary m_Library, ScopeUtil m_ScopeUtil,
			IDefinition m_Def, String linkstr, int type) {
		super(parentShell);
		this.reportcenter = reportcenter;
		this.m_Library = m_Library;
		this.m_ScopeUtil = m_ScopeUtil;
		this.m_api = m_api;
		this.reportDef = reportdef;
		this.m_Def = m_Def;
		this.type = type;
		this.linkstr = linkstr;
		this.m_lstSupportedScopesWithOwners = this.m_ScopeUtil
				.GetSupportedScopesWithOwners(XmlSecurityRight.ToRight("VAED"),
						true);
		this.context = new TagContext(m_api,m_Library,m_Library.GetBusinessObjectDef(linkstr));
		this.m_aParamDefs = new ArrayList();
		this.m_aRootDefs = new ArrayList();
		this.bNodeEmpty = true;
		this.m_bUpdateSelection = true;
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(720, 640);			// 窗口大小 
		newShell.setText("新建	报告");		// 窗口标题
		this.set_Location(newShell);		// 窗口居中
		super.configureShell(newShell);
		newShell.setImage(get_image(10000)); // 窗口图标
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}
	
	// TODO 页面控件及事件
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		// container.setSize(700, 800);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(26, 20, 59, 18);
		lblNewLabel.setText("\u540D\u79F0(N):");

		Label lbla = new Label(container, SWT.NONE);
		lbla.setBounds(26, 53, 48, 18);
		lbla.setText("\u522B\u540D(A):");
		
		Label lblPic = new Label(container, SWT.NONE);
		lblPic.setBounds(640, 20, 48, 54);
		lblPic.setImage(get_image(1));

		txtName = new Text(container, SWT.BORDER);
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				bNodeEmpty = false;
				reportDef.set_Name(txtName.getText());
				Fill();
				FillParametersTree();
                ExpandParametersTree(parametersTree.getItems());
                
                UpdateValuesFromSelection();
                UpdateControls();
			}
		});
		txtName.setBounds(91, 20, 543, 18);

		txtAlias = new Text(container, SWT.BORDER);
		txtAlias.setBounds(91, 53, 543, 18);

		Group report_file_name = new Group(container, SWT.NONE);
		report_file_name.setText("\u62A5\u8868\u6587\u4EF6\u540D(&R)");
		report_file_name.setBounds(26, 82, 662, 62);
		
		txtFile_name = new Text(report_file_name, SWT.BORDER);
		txtFile_name.setEditable(false);
		txtFile_name.setBounds(10, 28, 568, 18);
		
		btn_search = new Button(report_file_name, SWT.NONE);
		btn_search.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 浏览文件
				PublishedReportCenterForm prcf = new PublishedReportCenterForm(true, "",reportDef);
				prcf.setBlockOnOpen(true);
				prcf.open();
				if(prcf.getReturnCode()==Dialog.OK){
					PlaceHolder ph = prcf.getBackselection();
					m_defPubRpt = prcf.getM_defPubRpt();
					if (txtFile_name.getText() == ""){
						txtFile_name.setText(m_defPubRpt.get_Filename());
	                }
					if(txtName.getText()==""){
						//txtName.setText(def.get_Name());
					}
					if (txtDesc.getText() == ""){
						txtDesc.setText(ph.get_Description());
					}
					m_aParamDefs = prcf.getM_aParamDefs();
				}
			}
		});
		btn_search.setText("\u6D4F\u89C8(&B)");
		btn_search.setBounds(584, 26, 68, 22);
		
		tabFolder = new TabFolder(container, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tabFolder.getSelectionIndex();
				if(index==1){
					btn_search.setEnabled(false);
					reportDef.set_Name(txtName.getText());
					FillParametersTree();
	                ExpandParametersTree(parametersTree.getItems());
	                
	                UpdateValuesFromSelection();
	                UpdateControls();
				}else{
					btn_search.setEnabled(true);
				}
			}
		});
		tabFolder.setBounds(26, 158, 662, 405);

		TabItem general_tabItem = new TabItem(tabFolder, SWT.NONE);
		general_tabItem.setText("\u4E00\u822C");
		
		Composite general = new Composite(tabFolder, SWT.NONE);
		general_tabItem.setControl(general);
		
		Label label_6 = new Label(general, SWT.NONE);
		label_6.setText("\u8BF4\u660E:");
		label_6.setBounds(10, 20, 30, 12);
		
		txtDesc = new Text(general, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		txtDesc.setBounds(47, 7, 597, 83);
		
		Label label_7 = new Label(general, SWT.NONE);
		label_7.setText("\u8303\u56F4:");
		label_7.setBounds(10, 100, 30, 12);
		
		cbScope = new Combo(general, SWT.NONE);
		cbScope.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cbOwnerdate(cbScope.getData(cbScope.getItem(cbScope
						.getSelectionIndex())), 1);
			}
		});
		cbScope.setBounds(47, 100, 262, 21);
		cbScope.select(0);
		
		Label label_8 = new Label(general, SWT.NONE);
		label_8.setText("\u8D1F\u8D23\u4EBA:");
		label_8.setBounds(322, 100, 42, 12);
		
		cbOwner = new Combo(general, SWT.NONE);
		cbOwner.setItems(new String[] {"\u5168\u5C40"});
		cbOwner.setEnabled(false);
		cbOwner.setBounds(368, 100, 276, 21);
		cbOwner.select(0);
		
		Label label_9 = new Label(general, SWT.NONE);
		label_9.setText("\u7C7B\u522B:");
		label_9.setBounds(10, 140, 30, 12);
		
		cbCategory = new Combo(general, SWT.NONE);
		cbCategory.setBounds(47, 137, 429, 21);
		
		Label label_10 = new Label(general, SWT.NONE);
		label_10.setText("\u5173\u8054:");
		label_10.setBounds(10, 177, 30, 12);
		
		cbLinkedTo = new Combo(general, SWT.NONE);
		cbLinkedTo.setItems(new String[] {"\u672A\u5173\u8054"});
		cbLinkedTo.setBounds(47, 174, 429, 21);
		cbLinkedTo.select(0);
		cbLinkedTo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				String link_s = cbLinkedTo.getData(cbLinkedTo.getItem(cbLinkedTo.getSelectionIndex())).toString();
				context = new TagContext(m_api,m_Library,m_Library.GetBusinessObjectDef(link_s));
				generalmethods = new Generalmethods(cbtype, cbctype, tflist, checked_link, checked_hid, context);
				load_tag();
			}
		});
		
		Checked_Cd = new Button(general, SWT.CHECK);
		Checked_Cd.setText("\u4F5C\u4E3A\u83DC\u5355\u9879\u663E\u793A");
		Checked_Cd.setBounds(10, 213, 105, 16);
		
		Checked_Dh = new Button(general, SWT.CHECK);
		Checked_Dh.setText("\u5728\u5BFC\u822A\u5668\u4E2D\u663E\u793A");
		Checked_Dh.setBounds(10, 243, 105, 16);
		
		m_RptParameters = new TabItem(tabFolder, SWT.NONE);
		m_RptParameters.setText("\u62A5\u8868\u53C2\u6570");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		m_RptParameters.setControl(composite);
		
		Group grps = new Group(composite, SWT.NONE);
		grps.setText("\u6807\u8BB0\u9009\u62E9\u5668(&S)");
		grps.setBounds(434, 10, 209, 359);
		
		Label label = new Label(grps, SWT.NONE);
		label.setText("\u7C7B\u578B:");
		label.setBounds(10, 20, 54, 12);
		
		cbtype = new Combo(grps, SWT.NONE);
		cbtype.setBounds(10, 38, 189, 21);
		cbtype.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				generalmethods.loadSubypedate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString());
				generalmethods.Showcheckbox_Link_Hide(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString());
				if(cbctype.getItemCount()>0){
					cbctype.select(0);
					generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), cbctype.getData(cbctype.getItem(cbctype.getSelectionIndex())).toString());
				}else{
					cbctype.select(0);
					generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), "");
				}
			}
		});
		
		Label label_1 = new Label(grps, SWT.NONE);
		label_1.setText("\u5B50\u7C7B\u578B");
		label_1.setBounds(10, 64, 54, 12);
		
		cbctype = new Combo(grps, SWT.NONE);
		cbctype.setBounds(10, 82, 189, 21);
		cbctype.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), cbctype.getData(cbctype.getItem(cbctype.getSelectionIndex())).toString());
			}
		});
		
		ListViewer listViewer = new ListViewer(grps, SWT.BORDER | SWT.V_SCROLL);
		tflist = listViewer.getList();
		tflist.setBounds(10, 108, 189, 181);
		
		checked_hid = new Button(grps, SWT.CHECK);
		checked_hid.setText("\u663E\u793A\u9690\u85CF\u5B57\u6BB5");
		checked_hid.setBounds(10, 317, 93, 16);
		checked_hid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				context.set_UseLinkFields(checked_hid.getSelection());
				generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), cbctype.getData(cbctype.getItem(cbctype.getSelectionIndex())).toString());
			}
		});
		
		checked_link = new Button(grps, SWT.CHECK);
		checked_link.setText("\u663E\u793A\u94FE\u63A5\u5B57\u6BB5");
		checked_link.setBounds(10, 295, 93, 16);
		checked_link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				context.set_UseEmbedFields(checked_link.getSelection());
				generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), cbctype.getData(cbctype.getItem(cbctype.getSelectionIndex())).toString());
			}
		});
		
		generalmethods = new Generalmethods(cbtype, cbctype, tflist, checked_link, checked_hid, context);

		CBanner banner = new CBanner(composite, 0);
		banner.setBounds(67, 92, 0, 0);
		
		grpvalues = new Group(composite, SWT.NONE);
		grpvalues.setText("\u53C2\u6570\u503C");
		grpvalues.setBounds(10, 155, 410, 214);
		grpvalues.setEnabled(true);
		
		btnDefaultValued = new Button(grpvalues, SWT.RADIO);
		btnDefaultValued.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				context.set_UseAllBusinessObjects(true);
				txtdefv.setEnabled(true);
			}
		});
		btnDefaultValued.setText("\u9ED8\u8BA4\u503C:");
		btnDefaultValued.setBounds(10, 29, 107, 16);
		
		btnDiscreteValue = new Button(grpvalues, SWT.RADIO);
		btnDiscreteValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				context.set_UseAllBusinessObjects(false);
				txtdisv.setEnabled(true);
			}
		});
		btnDiscreteValue.setText("\u79BB\u6563\u503C:");
		btnDiscreteValue.setBounds(10, 61, 107, 16);
		
		btnRangeValue = new Button(grpvalues, SWT.RADIO);
		btnRangeValue.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				context.set_UseAllBusinessObjects(false);
				txtfrv.setEnabled(true);
				txttrv.setEnabled(true);
				cbfbt.setEnabled(true);
				cbtbt.setEnabled(true);
			}
		});
		btnRangeValue.setText("\u8303\u56F4\u503C:");
		btnRangeValue.setBounds(10, 91, 107, 16);
		
		txtdefv = new Text(grpvalues, SWT.BORDER);
		txtdefv.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txt_change();
			}
		});
		txtdefv.setBounds(123, 27, 277, 21);
		
		txtdisv = new Text(grpvalues, SWT.BORDER);
		txtdisv.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txt_change();
			}
		});
		txtdisv.setBounds(123, 61, 277, 21);
		
		lblFrom = new Label(grpvalues, SWT.NONE);
		lblFrom.setAlignment(SWT.RIGHT);
		lblFrom.setText("From");
		lblFrom.setBounds(55, 126, 24, 12);
		
		txtfrv = new Text(grpvalues, SWT.BORDER);
		txtfrv.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txt_change();
			}
		});
		txtfrv.setBounds(89, 117, 181, 21);
		
		lblboundType = new Label(grpvalues, SWT.NONE);
		lblboundType.setText("\u6346\u7ED1\u7C7B\u578B");
		lblboundType.setBounds(297, 95, 103, 12);
		
		cbfbt = new Combo(grpvalues, SWT.NONE);
		cbfbt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ((cbfbt.getSelectionIndex()) == 2){
					txtfrv.setText("");
				}
				txt_change();
			}
		});
		cbfbt.setItems(new String[] {"Inclusive", "Exclusive", "None"});
		cbfbt.setBounds(276, 117, 124, 21);
		cbfbt.select(0);
		
		lblto = new Label(grpvalues, SWT.NONE);
		lblto.setAlignment(SWT.RIGHT);
		lblto.setText("To");
		lblto.setBounds(67, 152, 12, 12);
		
		txttrv = new Text(grpvalues, SWT.BORDER);
		txttrv.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				txt_change();
			}
		});
		txttrv.setBounds(89, 143, 181, 21);
		
		cbtbt = new Combo(grpvalues, SWT.NONE);
		cbtbt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ((cbtbt.getSelectionIndex()) == 2){
					txttrv.setText("");
				}
				txt_change();
			}
		});
		cbtbt.setItems(new String[] {"Inclusive", "Exclusive", "None"});
		cbtbt.setBounds(276, 143, 124, 18);
		cbtbt.select(0);
		
		btn_del = new Button(grpvalues, SWT.NONE);
		btn_del.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 删除节点
				TreeItem selectedNode = parametersTree.getSelection()[0];
	            if (selectedNode != null){
	            	TreeItem parent = selectedNode.getParentItem();
	                RptParamValueDef selectedDefinitionObject = (RptParamValueDef) GetDefinitionObject(selectedNode);//GetSelectedDefinitionObject();
	                RptParameterDef definitionObject =(RptParameterDef) GetDefinitionObject(parent);
	                if ((definitionObject != null) && (selectedDefinitionObject != null)){
	                    definitionObject.get_ValueDefinitions().Remove(selectedDefinitionObject);
	                    selectedNode.dispose();
	                    AddNewValueNodeToParameterNodeIfNecessary(definitionObject, parent);
	                }
	            }
	            UpdateValuesFromSelection();
	            UpdateControls();
			}
		});
		btn_del.setText("Delete");
		btn_del.setBounds(309, 178, 91, 26);
		
		parametersTree = new Tree(composite, SWT.BORDER);
		parametersTree.setBounds(10, 10, 410, 139);
		parametersTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UpdateValuesFromSelection();
	            UpdateControls();
			}
		});
		
		Label separator = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL | SWT.BORDER);
		separator.setBounds(426, 0, 2, 379);

		firstloaddate();
		datemove();
		return container;
	}

	// TODO 文本框变更方法
	public void txt_change(){
		UpdateSelectionFromValues();
		
		UpdateValuesFromSelection();
        UpdateControls();
	}
	
	/**
	 * TODO 第一次数据加载 范围 类别,关联 下拉式
	 */
	private void firstloaddate() {
		// 一般页面
		// 范围数据加载
		ICollection supportedScopes = m_ScopeUtil.GetSupportedScopes(SecurityRight.Add);
		IEnumerator it = supportedScopes.GetEnumerator();
		while (it.MoveNext()) {
			int ph = (Integer) it.get_Current();
			String item = m_ScopeUtil.ScopeToString(ph);
			cbScope.setData(item, ph);
			cbScope.add(item);
			if (reportDef.get_Scope() == ph) {
				cbScope.setText(item);
			}
		}
		cbScope.select(0);
		// 关联数据加载
		ICollection busLinks = m_Library.GetPlaceHolderList(DefRequest
				.ForList(BusinessObjectDef.get_ClassName()));
		IEnumerator it1 = busLinks.GetEnumerator();
		Vector<String> bobs = new Vector<String>();
		while (it1.MoveNext()) {
			PlaceHolder ph = (PlaceHolder) it1.get_Current();

			if (ph.HasFlag("Master") && !ph.HasFlag("AllowDerivation")) {
				cbLinkedTo.add(ph.get_Alias());
				cbLinkedTo.setData(ph.get_Alias(), ph.get_Name());
				if (ph.get_Name().equals(linkstr)) {
					cbLinkedTo.setText(ph.get_Alias());
					cbLinkedTo.setData(ph.get_Alias(),ph.get_Name());
					getcboQuickOblink(ph.get_Alias());
				}
			}

		}
		if ("".equals(cbLinkedTo.getText())) {
			cbLinkedTo.select(0);
		}
		
		Set<String> typeItem=new java.util.HashSet<String>();
		for(int i=0,j = reportcenter.treeRange.getItemCount();i<j;i++){
			if(reportcenter.treeRange.getItem(i).getItems().length>0){
				for(int m=0,n=reportcenter.treeRange.getItem(i).getItems().length;m<n;m++){
					typeItem.add(reportcenter.treeRange.getItem(i).getItem(m).getText());
				}
			}
		}
		String[] typeItemStrs=new String[typeItem.size()];
		typeItem.toArray(typeItemStrs);
		cbCategory.setItems(typeItemStrs);
		if(reportcenter.tabFolder.getSelectionIndex()==0){
			if(reportcenter.treeRange.getSelectionCount()>0){
				String keyData = reportcenter.treeRange.getSelection()[0].getData().toString();
				String[] paramKeyData=keyData.split("~",-1);
				if(paramKeyData[1].length()!=0){
					String folderName=paramKeyData[1];
					for(int i=0;i<cbCategory.getItemCount();i++){
						String currentName=new String(cbCategory.getItem(i).getBytes());
						if(folderName.equals(currentName)){
							cbCategory.select(i);
							break;
						}
					}
				}
			}
		}else if(reportcenter.tabFolder.getSelectionIndex()==1){
			if(reportcenter.tbCategory.getSelectionCount()>0){
				String folderName=reportcenter.tbCategory.getSelection()[0].getText().trim();
				for(int i=0;i<cbCategory.getItemCount();i++){
					String currentName=new String(cbCategory.getItem(i).getBytes());
					if(folderName.equals(currentName)){
						cbCategory.select(i);
						break;
					}
				}
			}
		}
		// 类别数据加载
		Iterator ite = Categoryset.iterator();
		while (ite.hasNext()) {
			String str = (String) ite.next();
			if (!StringUtils.IsEmpty(str)) {
				cbCategory.add(str);
				cbCategory.setData(str, str);
			}
		}
		if (reportDef != null && type!=1) {
			PublishedReportDef definition = (PublishedReportDef) m_Library.GetDefinition(DefRequest.ByName(reportDef.get_Scope(),reportDef.get_ScopeOwner(), reportDef.get_LinkedTo(), "PublishedReportDef", reportDef.get_ReportName(), false));
			if (type==2) {
				txtName.setText(reportDef.get_Name());
				txtAlias.setText(reportDef.get_Alias());
				txtFile_name.setText(definition.get_Filename());
			} else {
				txtName.setText(reportDef.get_Name() + " 的副本");
				txtAlias.setText(reportDef.get_Alias() + " 的副本");
				txtFile_name.setText(definition.get_Filename());
			}
			m_defPubRpt = definition;
			cbCategory.setText(reportDef.get_Folder());
			txtDesc.setText(reportDef.get_Description());
			Checked_Cd.setSelection(reportDef.get_ShowOnMenu());
			Checked_Dh.setSelection(reportDef.get_ShowInNavigator());
		}
		
		// TODO 参数页数据加载
		load_tag();
		// 参数值初始化
		this.grpvalues.setEnabled(false);
		this.lblFrom.setEnabled(false);
		this.lblto.setEnabled(false);
         this.lblboundType.setEnabled(false);
		btnDefaultValued.setEnabled(false);
		btnDiscreteValue.setEnabled(false);
		btnRangeValue.setEnabled(false);
		txtdefv.setEnabled(false);
		txtdisv.setEnabled(false);
		txtfrv.setEnabled(false);
		txttrv.setEnabled(false);
		cbfbt.setEnabled(false);
		cbfbt.select(0);
		cbtbt.setEnabled(false);
		cbfbt.select(0);
		btn_del.setEnabled(false);
	}
	
	private void load_tag(){
		// 加载类型
		generalmethods.loadTypedate();
		cbtype.select(5);
		// 加载 链接/隐藏 
		generalmethods.Showcheckbox_Link_Hide(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString());
		// 加载子类型
		generalmethods.loadSubypedate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString());
		cbctype.select(0);
		// 加载列表
		if(cbctype.isEnabled()){
			generalmethods.loadListdate(cbtype.getData(cbtype.getItem(cbtype.getSelectionIndex())).toString(), cbctype.getData(cbctype.getItem(cbctype.getSelectionIndex())).toString());
		}
	}
	/**
	 * TODO 范围改变负责人 加载数据
	 */
	private void cbOwnerdate(Object object, int type) {
		if (type == 0) {
			cbOwner.removeAll();
			int ob = (Integer) object;
			IList list = m_ScopeUtil.GetScopeOwners(ob, true);
			if (list.get_Count() > 0) {
				for (int i = 0; i < list.get_Count(); i++) {
					String own = (String) list.get_Item(i);
					String item = m_ScopeUtil.ScopeOwnerToString(ob, own);
					cbOwner.add(item);
					cbOwner.setData(item, own);
				}
				cbOwner.select(0);
			}
			if (cbOwner.getItemCount() > 1) {
				cbOwner.setEnabled(true);
			}
		} else if (type == 1) {
			cbOwner.removeAll();
			int ob = (Integer) object;
			IList list = m_ScopeUtil.GetScopeOwners(ob, true);
			if (list.get_Count() > 0) {
				for (int i = 0; i < list.get_Count(); i++) {
					String own = (String) list.get_Item(i);
					String item = m_ScopeUtil.ScopeOwnerToString(ob, own);
					cbOwner.add(item);
					cbOwner.setData(item, own);
				}
				cbOwner.select(0);
			}
			if (cbOwner.getItemCount() > 1) {
				cbOwner.setEnabled(true);
			}
		} else if (type == 2) {
			//
		}
	}

	/**
	 * 根据选择下拉式 装载关联的范围和类型
	 */
	public void getcboQuickOblink(String str1) {
		if (StringUtils.IsEmpty(str1))
			return;
		String strBusLink = (String) cbLinkedTo.getData(str1);
		IEnumerator it = m_lstSupportedScopesWithOwners.GetEnumerator();
		while (it.MoveNext()) {
			ScopeUtil.ScopeAndOwners owners = (ScopeUtil.ScopeAndOwners) it
					.get_Current();
			if (owners.get_Owners().get_Count() > 0
					&& owners.get_Scope() != Scope.Global) {
				for (int i = 0; i < owners.get_Owners().get_Count(); i++) {
					String str = (String) owners.get_Owners().get_Item(i);
					OrganizeListerItemsByCategory(owners.get_Scope(), str,
							strBusLink, false);
				}
			} else {
				OrganizeListerItemsByCategory(owners.get_Scope(), "",
						strBusLink, false);
			}

		}
	}

	/**
	 * 对取出来的数据进行分类 保存到 set 集合中
	 * @param s Scope
	 * @param Owner
	 * @param linkTo 关联的业务对象的名字
	 * @param bFilter 是否过滤
	 */
	private void OrganizeListerItemsByCategory(int s, String Owner,
			String linkTo, boolean bFilter) {
		String strPerspective = (this.perspectiveDef != null) ? this.perspectiveDef
				.get_Name() : "(Base)";
		ArrayList obs = (ArrayList) m_Library.GetPlaceHolderList(DefRequest
				.ForList(s, Owner, linkTo, ReportDef.get_ClassName(),
						strPerspective));
		if (obs != null) {
			for (int i = 0; i < obs.get_Count(); i++) {
				PlaceHolder holder = (PlaceHolder) obs.get_Item(i);
				if (!Categoryset.contains(holder.get_Folder())) {
					Categoryset.add(holder.get_Folder());
				}
			}
		}
	}

	// 居中.
	private void set_Location(Control c){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Point framesize = c.getSize(); 
		if (framesize.y > screensize.height) { 
			framesize.y = screensize.height; 
		} 
		if (framesize.x > screensize.width) { 
			framesize.x = screensize.width; 
		} 
		c.setLocation((screensize.width - framesize.x) / 2, (screensize.height - framesize.y) /2); 
	}
	
	// 得到参数树图片
	private Image get_image(int index){
		switch(index){
			// 窗口小图标
			case 0: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"),0x12,0x12);
			// 窗口大图标
			case 1: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"),0x36,0x36);
			// 参数图标
			case 2: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportParameter.png"));
			// 新参数图标
			case 3: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportParameterNewValue.png"));
			// 默认值图标
			case 4: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportParameterDefault.png"));
			// 离散值图标
			case 5: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportParameterDiscrete.png"));
			// 范围值图标
			case 6: 
				return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportParameterRange.png"));
		}
		return SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png"));
	}
	
	/**
     * TODO 数据拖动事件
     */
    public void datemove(){
    	DragSource ds = new DragSource(tflist,DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		ds.addDragListener(new DragSourceAdapter(){
			public void dragSetData(DragSourceEvent event){ 
				event.data = tflist.getItem(tflist.getSelectionIndex()); 
			} 
		});
		
		// 拖放到默认值中.
		DropTarget dr_def = new DropTarget(txtdefv,DND.DROP_MOVE);
		dr_def.setTransfer(new Transfer[]{TextTransfer.getInstance()}); 
		dr_def.addDropListener(new DropTargetAdapter(){
			public void drop(DropTargetEvent event){
				txt_change();
			} 
		});
		
		// 拖放到离散值中.
		DropTarget dr_dis = new DropTarget(txtdisv,DND.DROP_MOVE);
		dr_dis.setTransfer(new Transfer[]{TextTransfer.getInstance()}); 
		dr_dis.addDropListener(new   DropTargetAdapter(){
			public void drop(DropTargetEvent   event){
				txt_change();
			} 
		});
		
		// 拖放到范围To值中.
		DropTarget dr_rt = new DropTarget(txttrv,DND.DROP_MOVE);
		dr_rt.setTransfer(new Transfer[]{TextTransfer.getInstance()}); 
		dr_rt.addDropListener(new DropTargetAdapter(){
			public void drop(DropTargetEvent event){
				txt_change();
			} 
		});
    }
    
    // TODO 加载参数树
    private void FillParametersTree(){
    	this.m_aRootDefs.Clear();
    	this.m_aRootDefs.Add(this.reportDef);
    	this.Fill();
    	for(int i=0;i<this.m_aParamDefs.get_Count();i++){
    		RptParameterDef def = (RptParameterDef) this.m_aParamDefs.get_Item(i);
            TreeItem tnParam = this.FindTreeNodeByDefinitionObject(def);
            if (tnParam != null){
                this.AddNewValueNodeToParameterNodeIfNecessary(def, tnParam);
            }
        }
    }
	public TreeItem FindTreeNodeByDefinitionObject(DefinitionObject def){
	    return this.FindTreeNodeByDefinitionObjectHelper(parametersTree.getItems(), def);
	}
	
	private TreeItem FindTreeNodeByDefinitionObjectHelper(TreeItem[] aTreeNodes, DefinitionObject def){
	    TreeItem node = null;
	    for (TreeItem node2 : aTreeNodes){
	        if (node2.getData() == def){
	            return node2;
	        }
	        node = this.FindTreeNodeByDefinitionObjectHelper(node2.getItems(), def);
	        if (node != null){
	            return node;
	        }
	    }
	    return node;
	}

	private void AddNewValueNodeToParameterNodeIfNecessary(RptParameterDef defParam, TreeItem tnParam){
	    if (((defParam != null) && (tnParam != null)) && !this.ParameterNodeContainsNewValueNode(tnParam)){
	        boolean flag = this.ParameterDefContainsValueDefOfType(defParam, RptParamDiscreteValDef.class);
	        boolean flag2 = this.ParameterDefContainsValueDefOfType(defParam, RptParamRangeValDef.class);
	        if ((!flag && !flag2) || ((flag || flag2) && defParam.get_AllowMultipleValues())){
	            TreeItem node = new TreeItem(tnParam,SWT.None);
	            node.setText("(新参数)");
	            node.setData("$$(New)$$");
	            node.setImage(get_image(3));
	        }
	    }
	}
	private boolean ParameterNodeContainsNewValueNode(TreeItem tnParam){
        for (TreeItem node : tnParam.getItems()){
        	if(node.getData() instanceof String){
	            if (((String) node.getData()) == "$$(New)$$"){
	                return true;
	            }
        	}
        }
        return false;
    }
    
    public void Fill(){
    	parametersTree.removeAll();
        this.FillHelper(null,parametersTree.getItems(), this.m_aRootDefs);
    }
    private void FillHelper(TreeItem thisItem,TreeItem[] aTreeNodes, IList aDefObs){
    	for(int i= 0;i<aDefObs.get_Count();i++){
        	DefinitionObject obj2 = (DefinitionObject) aDefObs.get_Item(i);
        	if(ShowDefOb(obj2)){
        		TreeItem node = null;
        		if(thisItem==null){
        			node = new TreeItem(parametersTree,SWT.None); 
        		}else{
        			node = new TreeItem(thisItem,SWT.None);  
        		}
	            this.FillTreeNodeFromDefinitionObject(node,obj2);
	            if (obj2 instanceof ReportDef){
	                this.FillHelper(node,node.getItems(), ((ReportDef) obj2).get_ParameterDefinitions());
	            }else if (obj2 instanceof RptParameterDef){
	                this.FillHelper(node,node.getItems(), ((RptParameterDef) obj2).get_ValueDefinitions());
	            }
	            node.setExpanded(true);
        	}
        }
    }
    private boolean ShowDefOb(DefinitionObject def){
    	boolean bShowParameters = true;
        if (def instanceof RptParameterDef){
            bShowParameters = this.m_bShowParameters;
        }
        return bShowParameters;
    }
    public TreeItem FillTreeNodeFromDefinitionObject(TreeItem tn, DefinitionObject def){
        if (def instanceof ReportDef){
        	String b = def.get_Name();
        	tn.setText(b);
            tn.setImage(get_image(0));
        }else if (def instanceof RptParameterDef){
        	String b = def.get_Name();
        	tn.setText(b);
        	tn.setImage(get_image(2));
        }else if (def instanceof RptParamDefaultValDef){
        	//String str = TagConversionHelper.ConvertContentsForDisplaying(((RptParamDefaultValDef) def).get_ParameterValue());
            String str = ((RptParamDefaultValDef) def).get_ParameterValue();
            str = "[" + str.substring(str.indexOf("<DispText>")+10, str.indexOf("</DispText>")) + "]";
            tn.setText(str);
            tn.setImage(get_image(4));
        }else if (def instanceof RptParamDiscreteValDef){
        	String str2 = ((RptParamDiscreteValDef) def).get_ParameterValue();
        	str2 = "[" + str2.substring(str2.indexOf("<DispText>")+10, str2.indexOf("</DispText>")) + "]";
//            String str2 = TagConversionHelper.ConvertContentsForDisplaying(((RptParamDiscreteValDef) def).get_ParameterValue());
            tn.setText(str2);
            tn.setImage(get_image(5));
        }else if (def instanceof RptParamRangeValDef){
            RptParamRangeValDef def2 = (RptParamRangeValDef) def;
            //String str3 = TagConversionHelper.ConvertContentsForDisplaying(def2.get_FromValue());
            //String str4 = TagConversionHelper.ConvertContentsForDisplaying(def2.get_ToValue());
            String str3 = def2.get_FromValue();
            String str4 = def2.get_ToValue();
            if(str3.indexOf("<DispText>")!=-1){
	            str3 = "[" + str3.substring(str3.indexOf("<DispText>")+10, str3.indexOf("</DispText>")) + "]";
	            str4 = "[" + str4.substring(str4.indexOf("<DispText>")+10, str4.indexOf("</DispText>")) + "]";
            }
            String str5 = "(未绑定)";
            String str6 = (def2.get_FromValueBoundType()==RptParamRangeValDef.RangeValueBoundTypes.NoBound) ? str5 : ((def2.get_FromValueBoundType() == RptParamRangeValDef.RangeValueBoundTypes.Inclusive) ? ('[' + str3) : ((def2.get_FromValueBoundType() == RptParamRangeValDef.RangeValueBoundTypes.Exclusive) ? ('(' + str3) : str3));
            String str7 = (def2.get_ToValueBoundType() == RptParamRangeValDef.RangeValueBoundTypes.NoBound) ? str5 : ((def2.get_ToValueBoundType() == RptParamRangeValDef.RangeValueBoundTypes.Inclusive) ? (str4 + ']') : ((def2.get_ToValueBoundType() == RptParamRangeValDef.RangeValueBoundTypes.Exclusive) ? (str4 + ')') : str4));
            String str8 = String.format("%s - %s", str6,str7); //str6 + "-" +str7;
            tn.setText(str8);
            tn.setImage(get_image(6));
        }
        if (tn != null){
            tn.setData(def);
            tn.setText(tn.getText().trim());
        }
        return tn;
    }
    private void ExpandParametersTree(TreeItem[] items){
    	int len = items.length;
    	if(len>0){
	    	for (int i = 0; i < len; i++){
	    		items[i].setExpanded(true);
	    		ExpandParametersTree(items[i].getItems());
	    	}
    	}
    }
    // TODO 点击节点 加载数据
    private void UpdateSelectionFromValues(){
        if (this.m_bUpdateSelection){
            boolean flag = false;
            boolean flag2 = false;
            boolean flag3 = false;
            Siteview.DefinitionObject selectedDefinitionObject = null;
            if(!(parametersTree.getSelection()[0].getData() instanceof String)){
        		selectedDefinitionObject = GetDefinitionObject(parametersTree.getSelection()[0]);
        	}
            if (selectedDefinitionObject != null){
                TreeItem selectedNode = this.parametersTree.getSelection()[0];
                if (selectedDefinitionObject instanceof RptParamDefaultValDef){
                    if (this.txtdefv.getText().equals("") && (selectedNode != null)){
                        TreeItem parent = selectedNode.getParentItem();
                        RptParamValueDef def = (RptParamValueDef) GetDefinitionObject(selectedNode);
                        RptParameterDef definitionObject = (RptParameterDef) this.GetDefinitionObject(parent);
                        if ((definitionObject != null) && (def != null)){
                            if (parent.getText().toString().equals("Date Range")){
                                flag = true;
                                flag3 = true;
                            }else if (parent.getText().toString().equals("GroupBy")){
                                flag = true;
                                flag2 = true;
                            }
                            definitionObject.get_ValueDefinitions().Remove(def);
                            selectedNode.dispose();
                            this.AddNewValueNodeToParameterNodeIfNecessary(definitionObject, parent);
                        }
                    }
                    flag = true;
                    ((RptParamDefaultValDef) selectedDefinitionObject).set_ParameterValue(this.GetContentsForPersisting(((RptParamDefaultValDef) selectedDefinitionObject).get_ParameterValue()));
                    this.FillTreeNodeFromDefinitionObject(selectedNode, selectedDefinitionObject);
                }else if (selectedDefinitionObject instanceof RptParamDiscreteValDef){
                    if (this.txtdisv.getText().equals("") && (selectedNode != null)){
                        TreeItem tn = selectedNode.getParentItem();
                        RptParamValueDef def3 = (RptParamValueDef) GetDefinitionObject(selectedNode);
                        RptParameterDef defParam = (RptParameterDef) this.GetDefinitionObject(tn);
                        if ((defParam != null) && (def3 != null)){
                            if (tn.getText().toString().equals("GroupBy")){
                                flag = true;
                                flag2 = true;
                            }
                            defParam.get_ValueDefinitions().Remove(def3);
                            selectedNode.dispose();
                            this.AddNewValueNodeToParameterNodeIfNecessary(defParam, tn);
                        }
                    }
                    flag2 = true;
                    ((RptParamDiscreteValDef) selectedDefinitionObject).set_ParameterValue(this.GetContentsForPersisting(((RptParamDiscreteValDef) selectedDefinitionObject).get_ParameterValue()));
                    this.FillTreeNodeFromDefinitionObject(selectedNode, selectedDefinitionObject);
                }else if (selectedDefinitionObject instanceof RptParamRangeValDef){
                    if ((this.txtfrv.getText().equals("") && this.txttrv.getText().equals("")) && (selectedNode != null)){
                        TreeItem node4 = selectedNode.getParentItem();
                        RptParamValueDef def5 = (RptParamValueDef) GetDefinitionObject(selectedNode);
                        RptParameterDef def6 = (RptParameterDef) this.GetDefinitionObject(node4);
                        if ((def6 != null) && (def5 != null)){
                            if (node4.getText().toString().equals("Date Range")){
                                flag = true;
                                flag3 = true;
                            }
                            def6.get_ValueDefinitions().Remove(def5);
                            selectedNode.dispose();
                            this.AddNewValueNodeToParameterNodeIfNecessary(def6, node4);
                        }
                    }
                    flag3 = true;
                    RptParamRangeValDef def7 = (RptParamRangeValDef) selectedDefinitionObject;
                    def7.set_FromValue(this.GetContentsForPersisting(def7.get_FromValue()));
                    def7.set_ToValue(this.GetContentsForPersisting(def7.get_ToValue()));
                    def7.set_FromValueBoundType(this.GetRangeValueBoundType(this.cbfbt.getSelectionIndex()));
                    def7.set_ToValueBoundType(this.GetRangeValueBoundType(this.cbtbt.getSelectionIndex()));
                    this.FillTreeNodeFromDefinitionObject(selectedNode, selectedDefinitionObject);
                }
            }else if (((this.parametersTree.getSelection()[0] != null) && (((String) this.parametersTree.getSelection()[0].getData()) == "$$(New)$$")) && (this.parametersTree.getSelection()[0].getParentItem() != null)){
                TreeItem node5 = this.parametersTree.getSelection()[0];
                TreeItem node6 = node5.getParentItem();
                RptParameterDef def8 = (RptParameterDef) this.GetDefinitionObject(node6);
                if (def8 != null){
                    if (this.btnDefaultValued.getSelection()){
                        flag = true;
                        selectedDefinitionObject = (RptParamValueDef) RptParamValueDef.DeserializeCreateNewForEditing(RptParamValueDef.ValueTypes.Default);
                        ((RptParamDefaultValDef) selectedDefinitionObject).set_ParameterValue(this.GetContentsForPersisting(""));
                    }else if (this.btnDiscreteValue.getSelection()){
                        flag2 = true;
                        selectedDefinitionObject = (RptParamValueDef) RptParamValueDef.DeserializeCreateNewForEditing(RptParamValueDef.ValueTypes.Discrete);
                        ((RptParamDiscreteValDef) selectedDefinitionObject).set_ParameterValue(this.GetContentsForPersisting(""));
                    }else if (this.btnRangeValue.getSelection()){
                        flag3 = true;
                        selectedDefinitionObject = (RptParamValueDef) RptParamValueDef.DeserializeCreateNewForEditing(RptParamValueDef.ValueTypes.Range);
                        RptParamRangeValDef def9 = (RptParamRangeValDef) selectedDefinitionObject;
                        def9.set_FromValue(this.GetContentsForPersisting(""));
                        def9.set_ToValue(this.GetContentsForPersisting(""));
                        def9.set_FromValueBoundType(this.GetRangeValueBoundType(this.cbfbt.getSelectionIndex()));
                        def9.set_ToValueBoundType(this.GetRangeValueBoundType(this.cbtbt.getSelectionIndex()));
                    }
                    if (selectedDefinitionObject != null){
                        def8.get_ValueDefinitions().Add(selectedDefinitionObject);
                        this.FillTreeNodeFromDefinitionObject(node5, selectedDefinitionObject);
                        this.AddNewValueNodeToParameterNodeIfNecessary(def8, node6);
                    }
                }
            }
            this.grpvalues.setEnabled((flag || flag2) || flag3);
            this.lblFrom.setEnabled((flag || flag2) || flag3);
            this.lblto.setEnabled((flag || flag2) || flag3);
            this.lblboundType.setEnabled((flag || flag2) || flag3);
            this.btnDefaultValued.setEnabled(flag);
            this.txtdefv.setEnabled(this.btnDefaultValued.getSelection());
            this.btnDiscreteValue.setEnabled(flag2);
            this.txtdisv.setEnabled(this.btnDiscreteValue.getSelection());
            this.btnRangeValue.setEnabled(flag3);
            this.txtfrv.setEnabled(this.btnRangeValue.getSelection());
    		this.txttrv.setEnabled(this.btnRangeValue.getSelection());
    		this.cbfbt.setEnabled(this.btnRangeValue.getSelection());
    		this.cbtbt.setEnabled(this.btnRangeValue.getSelection());
        }
    }
    // TODO 参数控件初始加载
    private void UpdateValuesFromSelection(){
        this.m_bUpdateSelection = false;
        boolean flag = false;
        boolean flag2 = false;
        boolean flag3 = false;
        
        Siteview.DefinitionObject selectedDefinitionObject = null;
        if(parametersTree.getSelection().length==0){
    		parametersTree.setSelection(parametersTree.getItem(parametersTree.getItemCount()-1));
    	}
    	if(!(parametersTree.getSelection()[0].getData() instanceof String)){
    		selectedDefinitionObject = GetDefinitionObject(parametersTree.getSelection()[0]);
    	}
        if (selectedDefinitionObject != null){
        	this.btnDefaultValued.setSelection(false);
        	this.btnDiscreteValue.setSelection(false);
        	this.btnRangeValue.setSelection(false);
            if (selectedDefinitionObject instanceof RptParamDefaultValDef){
                flag = true;
                this.btnDefaultValued.setEnabled(true);
                this.btnDefaultValued.setSelection(true);
                this.txtdefv.setEnabled(true);
                String str = ((RptParamDefaultValDef) selectedDefinitionObject).get_ParameterValue();
                str = "[" + str.substring(str.indexOf("<DispText>")+10, str.indexOf("</DispText>")) + "]";
                this.txtdefv.setText(str);
            }else if (selectedDefinitionObject instanceof RptParamDiscreteValDef){
                flag2 = true;
                this.btnDiscreteValue.setEnabled(true);
                this.btnDiscreteValue.setSelection(true);
                this.txtdisv.setEnabled(true);
                String str = ((RptParamDiscreteValDef) selectedDefinitionObject).get_ParameterValue();
                str = "[" + str.substring(str.indexOf("<DispText>")+10, str.indexOf("</DispText>")) + "]";
                this.txtdisv.setText(str);
            }else if (selectedDefinitionObject instanceof RptParamRangeValDef){
                flag3 = true;
                this.btnRangeValue.setEnabled(true);
                this.btnRangeValue.setSelection(true);
                this.txtfrv.setEnabled(true);
                this.txttrv.setEnabled(true);
                this.cbfbt.setEnabled(true);
                this.cbtbt.setEnabled(true);
                RptParamRangeValDef def = (RptParamRangeValDef) selectedDefinitionObject;
                String str1 = def.get_FromValue();
                String str2 = def.get_ToValue();
                if(str1.indexOf("<DispText>")!=-1){
	                str1 = "[" + str1.substring(str1.indexOf("<DispText>")+10, str1.indexOf("</DispText>")) + "]";
	                str2 = "[" + str2.substring(str2.indexOf("<DispText>")+10, str2.indexOf("</DispText>")) + "]";
                }
                this.txtfrv.setText(str1);
                this.txttrv.setText(str2);
                this.cbfbt.select(this.GetRangeValueBoundType(def.get_FromValueBoundType()));
                this.cbtbt.select(this.GetRangeValueBoundType(def.get_ToValueBoundType()));
            }else{
            	this.btnDefaultValued.setEnabled(false);
            	this.btnDiscreteValue.setEnabled(false);
            	this.btnRangeValue.setEnabled(false);
            }
        }else if(((this.parametersTree.getSelection()[0] != null) && (((String)this.parametersTree.getSelection()[0].getData()) == "$$(New)$$")) && (this.parametersTree.getSelection()[0].getParentItem() != null)){
        	TreeItem parent = this.parametersTree.getSelection()[0].getParentItem();
            RptParameterDef definitionObject = (RptParameterDef) this.GetDefinitionObject(parent);
            if (definitionObject != null){
                boolean flag4 = this.ParameterDefContainsValueDefOfType(definitionObject, RptParamDefaultValDef.class);
                boolean flag5 = this.ParameterDefContainsValueDefOfType(definitionObject, RptParamDiscreteValDef.class);
                boolean flag6 = this.ParameterDefContainsValueDefOfType(definitionObject, RptParamRangeValDef.class);
                flag = !flag5 && !flag6;
                if (!flag4){
                    if (definitionObject.get_ParameterKind() == RptParameterDef.ParameterKinds.Discrete){
                        flag2 = true;
                    }else if (definitionObject.get_ParameterKind() == RptParameterDef.ParameterKinds.Range){
                        flag3 = true;
                    }else if (definitionObject.get_ParameterKind() == RptParameterDef.ParameterKinds.DiscreteOrRange){
                        flag2 = true;
                        flag3 = true;
                    }
                }
            }
            this.btnDefaultValued.setEnabled(false);
        	this.btnDiscreteValue.setEnabled(false);
        	this.btnRangeValue.setEnabled(false);
        	
        	this.btnDefaultValued.setSelection(false);
        	this.btnDiscreteValue.setSelection(false);
        	this.btnRangeValue.setSelection(false);
        }else{
            this.btnDefaultValued.setEnabled(false);
        	this.btnDiscreteValue.setEnabled(false);
        	this.btnRangeValue.setEnabled(false);
        }
        if (!this.btnDefaultValued.getSelection()){
            this.txtdefv.setText("");
        }
        if (!this.btnDiscreteValue.getSelection()){
        	this.txtdisv.setText("");
        }
        if (!this.btnRangeValue.getSelection()){
        	this.txtfrv.setText("");
        	this.txttrv.setText("");
        	this.cbfbt.select(0);
        	this.cbtbt.select(0);
        }
        this.grpvalues.setEnabled((flag || flag2) || flag3);
        this.lblFrom.setEnabled((flag || flag2) || flag3);
        this.lblto.setEnabled((flag || flag2) || flag3);
        this.lblboundType.setEnabled((flag || flag2) || flag3);
        this.btnDefaultValued.setEnabled(flag);
        this.txtdefv.setEnabled(this.btnDefaultValued.getSelection());
        this.btnDiscreteValue.setEnabled(flag2);
        this.txtdisv.setEnabled(this.btnDiscreteValue.getSelection());
        this.btnRangeValue.setEnabled(flag3);
        this.txtfrv.setEnabled(this.btnRangeValue.getSelection());
        this.txttrv.setEnabled(this.btnRangeValue.getSelection());
        this.cbfbt.setEnabled(this.btnRangeValue.getSelection());
        this.cbtbt.setEnabled(this.btnRangeValue.getSelection());
        this.m_bUpdateSelection = true;
    }
    // TODO 判断参数类型
    private boolean ParameterDefContainsValueDefOfType(RptParameterDef defParam, Class obj){
    	for(int i=0;i<defParam.get_ValueDefinitions().get_Count();i++){
    		RptParamValueDef def = (RptParamValueDef) defParam.get_ValueDefinitions().get_Item(i);
        	if(def.getClass() == obj){
                return true;
            }
        }
        return false;
    }
    // TODO 得到节点值
    public DefinitionObject GetDefinitionObject(TreeItem tn){
        DefinitionObject tag = null;
        if (tn != null){
            tag = (DefinitionObject) tn.getData();
        }
        return tag;
    }
    // TODO 得到绑定类型值
    private int GetRangeValueBoundType(RptParamRangeValDef.RangeValueBoundTypes strBound){
        if (strBound == RptParamRangeValDef.RangeValueBoundTypes.NoBound){
            return 2;
        }
        if (strBound == RptParamRangeValDef.RangeValueBoundTypes.Exclusive){
            return 1;
        }
        return 0;
    }
    private RptParamRangeValDef.RangeValueBoundTypes GetRangeValueBoundType(int strBound){
        if (strBound==3){
            return RptParamRangeValDef.RangeValueBoundTypes.NoBound;
        }
        if (strBound==2){
            return RptParamRangeValDef.RangeValueBoundTypes.Exclusive;
        }
        return RptParamRangeValDef.RangeValueBoundTypes.Inclusive;
    }
    // TODO 处理浏览与删除控件状态
    private void UpdateControls(){
        if (this.m_bUpdateSelection){
        	if(tabFolder.getSelection()[0] == m_RptParameters){
        		btn_search.setEnabled(false);
        		this.UpdateRptParametersPageControls();
        	}else{
        		btn_search.setEnabled(true);
        	}
        }
    }
    private void UpdateRptParametersPageControls(){
    	boolean flag = false;
    	if(parametersTree.getSelection().length==0){
    		parametersTree.setSelection(parametersTree.getItem(parametersTree.getItemCount()-1));
    	}
    	TreeItem selectedNode = parametersTree.getSelection()[0];
    	if(selectedNode.getData() instanceof RptParamValueDef){
    		flag = true;
    	}else if(selectedNode.getData() instanceof RptParamDefaultValDef){
    		flag = true;
    	}else if(selectedNode.getData() instanceof RptParamRangeValDef){
    		flag = true;
    	}else if(selectedNode.getData() instanceof RptParamDiscreteValDef){
    		flag = true;
    	}
        this.btn_del.setEnabled(flag);
    }
    // TODO 对应的参数节点格式化
    public String GetContentsForPersisting(String strDisplayText){
    	if(strDisplayText==""){
			String strCargo = TagConversionHelper.BuildXmlTag(cbtype.getData(cbtype.getText()).toString(),cbctype.getData(cbctype.getText())==null?"":cbctype.getData(cbctype.getText()).toString(),tflist.getData(tflist.getItem(tflist.getSelectionIndex())).toString());
			String s = cbtype.getText()+": "+cbctype.getText()+"."+tflist.getItem(tflist.getSelectionIndex());
			strDisplayText = "<ExprBldrLink><DispText>#1</DispText><Cargo>#2</Cargo></ExprBldrLink>";
			strDisplayText = strDisplayText.replace("#1", s);
			strDisplayText = strDisplayText.replace("#2", strCargo);
    	}else{
    		if(strDisplayText.indexOf("<DispText>")!=-1){
    			String strCargo = TagConversionHelper.BuildXmlTag(cbtype.getData(cbtype.getText()).toString(),cbctype.getData(cbctype.getText())==null?"":cbctype.getData(cbctype.getText()).toString(),tflist.getData(tflist.getItem(tflist.getSelectionIndex())).toString());
    			String s = cbtype.getText()+": "+cbctype.getText()+"."+tflist.getItem(tflist.getSelectionIndex());
    			strDisplayText = "<ExprBldrLink><DispText>#1</DispText><Cargo>#2</Cargo></ExprBldrLink>";
    			strDisplayText = strDisplayText.replace("#1", s);
    			strDisplayText = strDisplayText.replace("#2", strCargo);
    		}
    	}
		return strDisplayText;
    }
    // 得到LinkTo 对应的名字
    private String LinkToString(String str){
    	String str_change = "";
    	int i = 0;
    	ICollection busLinks = m_Library.GetPlaceHolderList(DefRequest
				.ForList(BusinessObjectDef.get_ClassName()));
		IEnumerator it1 = busLinks.GetEnumerator();
		Vector<String> bobs = new Vector<String>();
		while (it1.MoveNext()) {
			PlaceHolder ph = (PlaceHolder) it1.get_Current();
			if (ph.HasFlag("Master") && !ph.HasFlag("AllowDerivation")) {
				if(str.equalsIgnoreCase(ph.get_Name())){
					str_change = ph.get_Alias();
					break;
				}
				i++;
			}
		}
    	return str_change;
    }
    private int LinkToIndex(Combo cb,String str){
    	int rtn = 0;
    	for(int i=0;i<cb.getItemCount();i++){
    		String s = (String) cb.getData(cb.getItem(i));
    		if(s == null){
    			s="";
    		}
    		if(s.equalsIgnoreCase(str)){
    			rtn = i;
    		}
    	}
    	return rtn;
    }
    /**
	 * TODO 点击保存 的处理的事件
	 */
	protected void okPressed() {
		MessageBox msg=new MessageBox(getShell(),SWT.YES|SWT.NO|SWT.CANCEL);
		msg.setMessage("保存当前　'"+txtName.getText()+"'　报告?");
		msg.setText("SiteView应用程序");
		int result=msg.open();
		if(result==SWT.YES){
			// 报告名
			String name = txtName.getText().trim();
			// 添加
			if (StringUtils.IsEmpty(name)) {
				MessageDialog.openInformation(getParentShell(), "提示",
						"请为此 报告  输入名称。");
				txtName.forceFocus();
				return;
			} else if (type==2) {
				if (!name.equals(reportDef.get_Name())) {
					if (m_Library.IsDuplicateName(DefRequest.ForDupeCheck(
							reportDef.get_InstanceClassName(),
							reportDef.get_Id(), reportDef.get_Scope(),
							reportDef.get_ScopeOwner(), name))) {
						MessageDialog.openInformation(getParentShell(), "提示",
								"找到相同名称,请为此  报告  输入其它名称. ");
						txtName.forceFocus();
						return;
					}
				}
			} else {
				if (m_Library.IsDuplicateName(DefRequest.ForDupeCheck(
						m_Def.get_InstanceClassName(), m_Def.get_Id(),
						m_Def.get_Scope(), m_Def.get_ScopeOwner(), name))) {
					MessageDialog.openInformation(getParentShell(), "提示",
							"找到相同名称,请为此  报告  输入其它名称. ");
					txtName.forceFocus();
					return;
				}
			}
			reportDef.set_Alias(txtAlias.getText().trim());
			reportDef.set_Name(txtName.getText().trim());
			reportDef.set_Description(txtDesc.getText().trim());
			reportDef.set_Scope((Integer) cbScope.getData(cbScope.getItem(cbScope.getSelectionIndex())));
			reportDef.set_ScopeOwner(this.m_Library.AutoScopeOwner((Integer) cbScope.getData(cbScope.getItem(cbScope.getSelectionIndex()))));//(String) cbOwner.getData(cbOwner.getItem(cbOwner.getSelectionIndex())));
			if (!StringUtils.IsEmpty(cbCategory.getText())) {
				reportDef.set_Folder(cbCategory.getText());
			} else {
				reportDef.set_Folder("");
			}
			reportDef.set_Perspective("(Base)");
			String link_to = (String) cbLinkedTo.getData(cbLinkedTo.getItem(cbLinkedTo.getSelectionIndex()));
			if(link_to==null){
				link_to = ""; 
			}
			if (this.linkstr != link_to){
	            String strText = "是否切换到中心以查看与 '$$(LINKEDTO)$$' 关联的 报告?";
	            String strValue = LinkToString(link_to);
	            // 是否切换到中心以查看与 '$$(LINKEDTO)$$' 关联的 Reports？
	            if (MessageDialog.openQuestion(getParentShell(),"提示", strText.replace("$$(LINKEDTO)$$", strValue))){
	            	Combo cb = this.reportcenter.getCboQuickOblink();
	            	int index = LinkToIndex(cb,link_to);
	            	cb.select(index);
	            }
	        }
			reportDef.set_LinkedTo(link_to);
			reportDef.set_ShowOnMenu(Checked_Cd.getSelection());
			reportDef.set_ShowInNavigator(Checked_Dh.getSelection());
			
			// 参数页保存 在参数页参数拖放时已经处理
	
			this.reportDef.set_ReportType(new ReportDef().get_ReportType().Server);
			if (this.m_defPubRpt != null){
	            this.reportDef.set_ReportName(this.m_defPubRpt.get_Name());
	        }else{
	        	this.reportDef.set_ReportName(this.reportDef.get_ReportName());
	        }
			
			if (type==1) {
				m_Library.UpdateDefinition(reportDef, true);
			} else if (type==2) {
				m_Library.UpdateDefinition(reportDef, false);
			} else if(type==3) {
				reportDef.set_Id(m_Def.get_Id());
				m_Library.UpdateDefinition(reportDef, true);
			}
			super.okPressed();
			if (reportcenter != null) {
				reportcenter.Refresh();
			}
		}else if(result==SWT.NO){
			this.getShell().dispose();
		}
	}
}
