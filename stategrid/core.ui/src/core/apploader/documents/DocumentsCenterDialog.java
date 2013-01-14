package core.apploader.documents;


import java.util.HashSet;
import java.util.Iterator;

import javax.activation.FileTypeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


import Siteview.DefRequest;
import Siteview.IDefinition;
import Siteview.PlaceHolder;
import Siteview.StringUtils;
import Siteview.TemplateDocDef;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ScopeUtil;
import Siteview.Xml.SecurityRight;

import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Collections.IList;
import system.IO.File;


public class DocumentsCenterDialog extends Dialog{

	
	private String type;	//操作类型----1.新建 2.编辑 3.复制
	private String linkTo;  //关联下拉式 选 项
	private HashSet<String> Categoryset = new HashSet<String>(); //保存全部类别的集合
//	private OutlookCalendarPartDef calendarDef;
	private TemplateDocDef templateDocDef;
	private ScopeUtil m_ScopeUtil;
	private DefinitionLibrary m_Library;
	private ISiteviewApi m_Api;
	private IDefinition m_Def;
	
	private Text text_Name;
	private Text text_Alias;
	private Text text_Description;
	
	private CCombo combo_FormatType;
	
	private CCombo combo_Scope;
	private CCombo combo_Owner;
	private CCombo combo_Category;
	private CCombo combo_LinkTo;
	private Text text_FilePath;
	
	private Button radio_Browse;
	private Button radio_Eidtor;
	private Button button_Edit;
	private Button button_FileBrowse;
	
	private String context;
	
	/**
	 * @wbp.parser.constructor
	 */
	public DocumentsCenterDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public DocumentsCenterDialog(Shell parentShell, TemplateDocDef templateDocDef, DefinitionLibrary m_Library, ScopeUtil m_ScopeUtil, ISiteviewApi m_Api, String linkTo, HashSet<String> Categoryset, String type, IDefinition m_Def) {
		super(parentShell);
		this.templateDocDef = templateDocDef;
		this.m_Library = m_Library;
		this.m_ScopeUtil = m_ScopeUtil;
		this.m_Api = m_Api;
		this.linkTo = linkTo;
		this.Categoryset = Categoryset;
		this.type = type;
		this.m_Def = m_Def;
		setShellStyle(SWT.BORDER | SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(700, 700);
		newShell.setLocation(300, 100);
		newShell.setText(type + "  文档");
		super.configureShell(newShell);
	}
	

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
//		container.setSize(700, 800);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 31);
		fd_lblNewLabel.left = new FormAttachment(0, 26);
		fd_lblNewLabel.right = new FormAttachment(0, 85);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("\u540D\u79F0(N):");
		
		Label lbla = new Label(container, SWT.NONE);
		lbla.setText("\u522B\u540D(A):");
		FormData fd_lbla = new FormData();
		fd_lbla.top = new FormAttachment(lblNewLabel, 21);
		fd_lbla.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		lbla.setLayoutData(fd_lbla);
		
		text_Name = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(lblNewLabel, 406, SWT.RIGHT);
		fd_text.top = new FormAttachment(0, 28);
		fd_text.left = new FormAttachment(lblNewLabel, 6);
		text_Name.setLayoutData(fd_text);
		
		text_Alias = new Text(container, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(lbla, -3, SWT.TOP);
		fd_text_1.right = new FormAttachment(text_Name, 0, SWT.RIGHT);
		fd_text_1.left = new FormAttachment(text_Name, 0, SWT.LEFT);
		text_Alias.setLayoutData(fd_text_1);
		
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.top = new FormAttachment(text_Alias, 21);
		fd_tabFolder.right = new FormAttachment(lblNewLabel, 604);
		fd_tabFolder.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		fd_tabFolder.bottom = new FormAttachment(100, -40);
		tabFolder.setLayoutData(fd_tabFolder);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("\u4E00\u822C");
		
		Composite general = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(general);
		
		Label lblNewLabel_1 = new Label(general, SWT.NONE);
		lblNewLabel_1.setBounds(10, 13, 54, 21);
		lblNewLabel_1.setText("\u8BF4\u660E(D):");
		
		text_Description = new Text(general, SWT.BORDER);
		text_Description.setBounds(70, 10, 488, 43);
		
		Label lblNewLabel_2 = new Label(general, SWT.NONE);
		lblNewLabel_2.setBounds(10, 83, 54, 21);
		lblNewLabel_2.setText("\u8303\u56F4(S):");
		
		combo_Scope = new CCombo(general, SWT.BORDER);
		combo_Scope.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cbOwnerdate(combo_Scope.getData(combo_Scope.getItem(combo_Scope.getSelectionIndex())));
			}
		});
		combo_Scope.setBounds(72, 78, 133, 21);
		
		Label lblNewLabel_3 = new Label(general, SWT.NONE);
		lblNewLabel_3.setBounds(250, 83, 60, 21);
		lblNewLabel_3.setText("\u8D1F\u8D23\u4EBA(O):");
		
		combo_Owner = new CCombo(general, SWT.BORDER);
		combo_Owner.setBounds(324, 78, 158, 21);
		combo_Owner.setEnabled(false);
		
		Label lblc = new Label(general, SWT.NONE);
		lblc.setText("\u7C7B\u522B(C):");
		lblc.setBounds(10, 138, 54, 21);
		
		Label lbla_1 = new Label(general, SWT.NONE);
		lbla_1.setText("\u5173\u8054(A):");
		lbla_1.setBounds(10, 194, 54, 21);
		
		combo_Category = new CCombo(general, SWT.BORDER);
		combo_Category.setBounds(164, 131, 281, 21);
		
		combo_LinkTo = new CCombo(general, SWT.BORDER);
		combo_LinkTo.setBounds(164, 188, 281, 21);

//		------------------------------------------------------------------------
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("\u7F16\u8F91\u5668");
		
		Composite face = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(face);
		
		final Group grpt = new Group(face, SWT.NONE);
		grpt.setLocation(10, 10);
		grpt.setText("  \u9009\u62E9\u683C\u5F0F\u7C7B\u578B  ");
//		FormData fd_Group = new FormData();
//		fd_Group.top = new FormAttachment(0 , 300);
//		grpt.setLayoutData(fd_Group);
		grpt.setSize(576, 81);
		
		combo_FormatType = new CCombo(grpt, SWT.BORDER);
		combo_FormatType.setEnabled(false);
		combo_FormatType.setBounds(38, 35, 220, 21);
		
		Group group = new Group(face, SWT.NONE);
		group.setText("  \u9009\u62E9\u7C7B\u5BB9\u9009\u9879  ");
		group.setBounds(10, 109, 576, 163);
		
		radio_Eidtor = new Button(group, SWT.RADIO);
		radio_Eidtor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OnRadioEditSelect();
			}
		});
		radio_Eidtor.setBounds(46, 40, 20, 16);
		
		radio_Browse = new Button(group, SWT.RADIO);
		radio_Browse.setBounds(46, 100, 20, 16);
		
		button_Edit = new Button(group, SWT.NONE);
		button_Edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				EditorHtmlDlg heditor = new EditorHtmlDlg(getShell(), context);
				if(heditor.open() == 0) context = heditor.getContext();
				
			}
		});
		button_Edit.setBounds(90, 32, 150, 30);
		button_Edit.setText("\u4F7F\u7528\u7F16\u8F91\u5668");
		
		text_FilePath = new Text(group, SWT.BORDER);
		text_FilePath.setEditable(false);
		text_FilePath.setBounds(90, 97, 323, 20);
		
		button_FileBrowse = new Button(group, SWT.NONE);
		button_FileBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onBrowserClick();
			}
		});
		button_FileBrowse.setText("\u6D4F\u89C8...");
		button_FileBrowse.setBounds(420, 94, 80, 25);
		
		
		fillProperties();
		return container;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
	}

	
	private void initUniversalDate(){
		//范围下拉框数据加载
		ICollection supportedScopes =m_ScopeUtil.GetSupportedScopes(SecurityRight.Add);
		IEnumerator it = supportedScopes.GetEnumerator();
		while(it.MoveNext()){
			int scop_int = (Integer) it.get_Current();
			String item=m_ScopeUtil.ScopeToString(scop_int);
			combo_Scope.setData(item, scop_int);
			combo_Scope.add(item);
			if(templateDocDef.get_Scope() == scop_int){
				combo_Scope.setText(item);
			}
		}
		
		//负责人下拉框数据加载
		int ob=(Integer) templateDocDef.get_Scope();
		IList list=m_ScopeUtil.GetScopeOwners(ob, true);
		if(list.get_Count()>0){
			for(int i=0;i<list.get_Count();i++){
				String own=(String) list.get_Item(i);
				String item=m_ScopeUtil.ScopeOwnerToString(ob, own);
				combo_Owner.add(item);
				combo_Owner.setData(item, own);
			}
			combo_Owner.select(0);
		}
		if(combo_Owner.getItemCount()>1){
			combo_Owner.setEnabled(true);
		}
		
		//类别下拉框数据加载
		 Iterator<String> ite=Categoryset.iterator();
			while(ite.hasNext()){
				String str=(String) ite.next();
				if(!StringUtils.IsEmpty(str)){
					combo_Category.add(str);
					combo_Category.setData(str,str);
				}
				if(templateDocDef.get_Folder().equals(str))
					combo_Category.setText(str);
			}
			
			
		//关联下拉框数据加载
			ICollection busLinks = m_Library.GetPlaceHolderList(DefRequest.ForList(BusinessObjectDef.get_ClassName()));
			IEnumerator it1 = busLinks.GetEnumerator();
			combo_LinkTo.add(" （未关联）");
			combo_LinkTo.setData(" （未关联）","");
			if(linkTo.equals("")) combo_LinkTo.setText(" （未关联）");
			while(it1.MoveNext()){
				PlaceHolder ph = (PlaceHolder)it1.get_Current();
				if (ph.HasFlag("Master")&& !ph.HasFlag("AllowDerivation")){
					combo_LinkTo.add(ph.get_Alias());
					combo_LinkTo.setData(ph.get_Alias(), ph.get_Name());
					if( ph.get_Name().equals(linkTo)){
						combo_LinkTo.setText(ph.get_Alias());
					}
				}
			}
	}
	
	
	@Override
	protected void okPressed() {
		
		if(validation()){
			saveProperties();
			
			if ("New".equals(type)) {
				m_Library.UpdateDefinition(templateDocDef, true);
			} else if ("Edit".equals(type)) {
				m_Library.UpdateDefinition(templateDocDef, false);
			} else if("Copy".equals(type)){
				templateDocDef.set_Id(m_Def.get_Id());
				m_Library.UpdateDefinition(templateDocDef, true);
			}
			
			super.okPressed();
		}
		
	}
	
	
	private boolean validation() {
		
		String name = text_Name.getText().trim();
		
		if(StringUtils.IsEmpty(name)){
			MessageDialog.openInformation(getShell(), "提示", "请为此  文档  输入名称。");
			text_Name.forceFocus();
			return false;
		}else if ("Edit".equals(type)) {
			if (!name.equals(templateDocDef.get_Name())) {
				if (m_Library.IsDuplicateName(DefRequest.ForDupeCheck(
						templateDocDef.get_InstanceClassName(),
						templateDocDef.get_Id(), templateDocDef.get_Scope(),
						templateDocDef.get_ScopeOwner(), name))) {
					MessageDialog.openInformation(getShell(), "提示",
							"找到相同名称,请为此  文档  输入其它名称. ");
					text_Name.forceFocus();
					return false;
				}
			}
		} else {
			if (m_Library.IsDuplicateName(DefRequest.ForDupeCheck(
					templateDocDef.get_InstanceClassName(), templateDocDef.get_Id(),
					templateDocDef.get_Scope(), templateDocDef.get_ScopeOwner(), name))) {
				MessageDialog.openInformation(getShell(), "提示",
						"找到相同名称,请为此  文档  输入其它名称. ");
				text_Name.forceFocus();
				return false;
			}
		}
		return true;
		
	}
	
	private void saveProperties(){
		
		templateDocDef.set_Alias(text_Alias.getText().trim());
		templateDocDef.set_Name(text_Name.getText().trim());
		templateDocDef.set_Description(text_Description.getText().trim());
		templateDocDef.set_Scope((Integer)combo_Scope.getData(combo_Scope.getText()));
		templateDocDef.set_ScopeOwner((String)combo_Owner.getData(combo_Owner.getText()));
		templateDocDef.set_Folder(combo_Category.getText().trim());
		templateDocDef.set_LinkedTo((String)combo_LinkTo.getData(combo_LinkTo.getText()));
		
		
		String content = "";
        if (radio_Browse.getSelection()) {
            if (!"".equals(text_FilePath.getText().trim())) {
            	content = File.ReadAllText(text_FilePath.getText().trim());
            }
            
        } else if (radio_Eidtor.getSelection()) {
        	content = context;
        	
        }
        
        templateDocDef.set_TemplateContent(content);
        templateDocDef.set_TemplateContentType(combo_FormatType.getText());
		
	}

	
	protected void fillProperties(){
		
		context = templateDocDef.get_TemplateContent();
		radio_Eidtor.setSelection(true);
		text_FilePath.setEnabled(false);
		button_FileBrowse.setEnabled(false);
		combo_FormatType.setText("HTML");
		initUniversalDate();
		if(templateDocDef != null && !"New".equals(type)){
		
			if ("Edit".equals(type)) {
				text_Name.setText(templateDocDef.get_Name());
				text_Alias.setText(templateDocDef.get_Alias());
			} else {
				text_Name.setText(templateDocDef.get_Name() + "  的副本");
				text_Alias.setText(templateDocDef.get_Alias() + "  的副本");
			}
			
			text_Description.setText(templateDocDef.get_Description());
		}
		
	}
	
	public DefinitionLibrary getM_Library() {
		return m_Library;
	}
	
	private void onBrowserClick(){
		FileDialog fileBrowser = new FileDialog(getShell());
		fileBrowser.setFilterExtensions(new String[]{"*.html;*.htm;*.htt"});
		String filePath = fileBrowser.open();
		if(filePath != null) text_FilePath.setText(filePath);
		
	}
	
	private void OnRadioEditSelect(){
		if(radio_Eidtor.getSelection()){
			if(!"".equals(text_FilePath.getText().trim())){
				boolean flag = MessageDialog.openConfirm(getShell(), "FRS-警告", "继续将丢失已改变的类容选项！");
				if(flag){
					button_Edit.setEnabled(true);
					button_FileBrowse.setEnabled(false);
					text_FilePath.setText("");
					text_FilePath.setEnabled(false);
					
				}else{
					radio_Eidtor.setSelection(false);
					radio_Browse.setSelection(true);
				}
			}else{
				button_Edit.setEnabled(true);
				button_FileBrowse.setEnabled(false);
				text_FilePath.setText("");
				text_FilePath.setEnabled(false);
				
			}
		}else{
			if(!"".equals(context)){
				boolean flag = MessageDialog.openConfirm(getShell(), "FRS-警告", "继续将丢失已改变的类容选项！");
				if(flag){
					button_Edit.setEnabled(false);
					context = "";
					button_FileBrowse.setEnabled(true);
					text_FilePath.setEnabled(true);
					
				}else{
					radio_Eidtor.setSelection(true);
					radio_Browse.setSelection(false);
					
				}
			}else{
				button_Edit.setEnabled(false);
				context = "";
				button_FileBrowse.setEnabled(true);
				text_FilePath.setEnabled(true);
			}
		}
	}
	
	protected void cbOwnerdate(Object object) {
		combo_Owner.removeAll();
		int ob = (Integer) object;
		IList list = m_ScopeUtil.GetScopeOwners(ob, true);
		if (list.get_Count() > 0) {
			for (int i = 0; i < list.get_Count(); i++) {
				String own = (String) list.get_Item(i);
				String item = m_ScopeUtil.ScopeOwnerToString(ob, own);
				combo_Owner.add(item);
				combo_Owner.setData(item, own);
			}
			combo_Owner.select(0);
		}
		if (combo_Owner.getItemCount() > 1) {
			combo_Owner.setEnabled(true);
		}
	}
	
}
