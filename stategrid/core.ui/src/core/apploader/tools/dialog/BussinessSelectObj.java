package core.apploader.tools.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.Collections.Specialized.StringCollection;
import Siteview.DefRequest;
import Siteview.ISecurity;
import Siteview.PlaceHolder;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.SecurityService;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Xml.SecurityRight;
import org.eclipse.swt.widgets.Label;

import core.busobmaint.BusObMaintView;

public class BussinessSelectObj extends Dialog{
	/*
	 * 标题
	 */
	private String title="选择业务对象";
	private Image  titleImage=SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Siteview#Images.Icons.BusinessObject.BusinessObject16.png"),0x12,0x12);
	
	/*
	 * 数据接口
	 */
	private ISiteviewApi m_api;
	private DefinitionLibrary m_libLibrary;
	private ISecurity m_securityservice;
	
	/*
	 * 定义控件
	 */
	private Button btnGo;
	private Button btnOK;
	private Combo  busObcombo;
	private Image  busObGoImage=SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.NewItem.png"),0x12,0x12);
	
	/*
	 * 返回参数
	 */
	private String busObName;
	
	public String getBusObName(){
		return busObName;
	}
	
	/*
	 * 构造方法
	 */
	public BussinessSelectObj(Shell parentShell){
		super(parentShell);
		this.m_api=ConnectionBroker.get_SiteviewApi();
		this.m_libLibrary=m_api.get_LiveDefinitionLibrary();
		this.m_securityservice=m_api.get_SecurityService();
	}
	
	/*
	 * 初始化配置
	 */
	protected void configureShell(Shell newShell) {
		newShell.setSize(375,150);
		newShell.setLocation(400,280);
		newShell.setText(title);
		newShell.setImage(titleImage);
		super.configureShell(newShell);
	}
	
	/*
	 * 创建面板元素
	 */
	protected Control createDialogArea(Composite parent) {
		Composite container=(Composite)super.createDialogArea(parent);
		container.setLayout(null);
		
		busObcombo = new Combo(container, SWT.READ_ONLY);
		busObcombo.setBounds(10, 26, 329, 20);
		
		btnGo = new Button(container, SWT.NONE);
		btnGo.setBounds(337, 26, 26,20);
		btnGo.setImage(busObGoImage);
		
		loadCombo();
		
		addClickEvent();
		
		return container;
	}
	
	public void loadCombo(){
        ICollection placeHolderList = m_libLibrary.GetPlaceHolderList(DefRequest.ByCategory("BusinessObjectDef"));
        if (placeHolderList != null){
            for(IEnumerator ienum=placeHolderList.GetEnumerator();ienum.MoveNext();){
            	PlaceHolder holder=(PlaceHolder)ienum.get_Current();
                if ((!holder.HasFlag("Master") && !holder.HasFlag("AllowDerivation")) && (!holder.HasFlag("HideInMaintenance") &&m_securityservice.HasBusObRight(holder.get_Name(), SecurityRight.View))){
                    busObcombo.add(holder.get_Alias());
                    busObcombo.setData(holder.get_Alias(),holder.get_Name());
                }
            }
        }
        busObcombo.select(0);
	}
	
	public void addClickEvent(){
		btnGo.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				BusObMaintView.newBusOb(ConnectionBroker.get_SiteviewApi(),busObcombo.getData(busObcombo.getText()).toString());
				buttonPressed(IDialogConstants.CANCEL_ID);
			}
		});
	}
	
	/*
	 * 重写按钮描述
	 */
	protected void initializeBounds() {
		btnOK=super.getButton(IDialogConstants.OK_ID);
		super.getButton(IDialogConstants.CANCEL_ID).setText("取消");
		btnOK.setText("确定");
	}
	
	
	protected void buttonPressed(int buttonId) {
		if(buttonId==IDialogConstants.OK_ID){
			busObName=busObcombo.getData(busObcombo.getText()).toString();
		}
		super.buttonPressed(buttonId);
	}
}
