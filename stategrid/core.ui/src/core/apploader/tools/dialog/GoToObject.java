package core.apploader.tools.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import core.busobmaint.BusObMaintView;
import core.busobmaint.BusObNewInput;

import Siteview.CollectionUtils;
import Siteview.ISettings;
import Siteview.IVirtualKeyList;
import Siteview.LegalUtils;
import Siteview.Operators;
import Siteview.PlaceHolder;
import Siteview.QueryInfoToGet;
import Siteview.SiteviewQuery;
import Siteview.StringUtils;
import Siteview.Api.BusinessObject;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.FieldDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ConnectionBroker;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import system.Collections.IEnumerator;

public class GoToObject extends Dialog{
	/*
	 * 标题
	 */
	private String title="转到记录";
	
	/*
	 * 定义控件
	 */
	private Combo comboSelObj;
	private Label lblInputSign;
	private Button btnOK;
	private Text txtInputSign;
	
	/*
	 * 数据接口
	 */
	private ISiteviewApi m_api;
	private ISettings settings;
	/*
	 * 构造方法
	 */
	public GoToObject(Shell shell){
		super(shell);
		m_api=ConnectionBroker.get_SiteviewApi();
		settings =m_api.get_SettingsService().GetSettings("Core.Defaults.GoToObjectSettings");
	}
	
	/*
	 * 初始化配置
	 */
	protected void configureShell(Shell newShell) {
		newShell.setSize(420,180);
		newShell.setLocation(450,280);
		newShell.setText(title);
		super.configureShell(newShell);
	}
	
	/*
	 * 创建面板元素
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite=(Composite)super.createDialogArea(parent);
		composite.setLayout(null);
		
		Label lblSelObj = new Label(composite, SWT.NONE);
		lblSelObj.setBounds(36, 25, 54, 17);
		lblSelObj.setText("\u9009\u62E9\u5BF9\u8C61:");
		
		lblInputSign = new Label(composite, SWT.NONE);
		lblInputSign.setBounds(36, 68, 131, 15);
		lblInputSign.setText("\u8F93\u5165 \u4E8B\u4EF6\u6807\u8BC6:");
		
		comboSelObj = new Combo(composite, SWT.NONE);
		comboSelObj.setBounds(158, 22, 206, 20);
		
		txtInputSign = new Text(composite, SWT.BORDER);
		txtInputSign.setBounds(207, 65, 157, 18);
		
		Label lblSelObjImage = new Label(composite, SWT.NONE);
		lblSelObjImage.setBounds(370, 22, 30, 17);
		lblSelObjImage.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[image]Siteview#Images.Icons.UserAgent.AppointmentNew.png"),0x12,0x12));
		
		loadBusObComboAndAddEvent();
		
		return composite;
	}
	
	/*
	 * 加载Combo
	 */
	public void loadBusObComboAndAddEvent(){
		m_api=ConnectionBroker.get_SiteviewApi();
		settings =m_api.get_SettingsService().GetSettings("Core.Defaults.GoToObjectSettings");
		String selectedCurBusObName = settings.GetSettingAsString("MostRecentObject");
		ICollection busObPlaceHolderList =m_api.get_BusObDefinitions().GetBusObPlaceHolderList();
		 int num = 0;
         int num2 = 0;
         for(IEnumerator ienume=busObPlaceHolderList.GetEnumerator();ienume.MoveNext();)
         {
        	 PlaceHolder holder=(PlaceHolder)ienume.get_Current();
             if (holder.HasFlag("Master") && holder.HasFlag("GoToObject"))
             {
            	 comboSelObj.add(holder.get_Alias());
            	 comboSelObj.setData(holder.get_Alias(),holder);
                 if (holder.get_Name().equals(selectedCurBusObName))
                 {
                     num2 = num;
                 }
                 num++;
             }
         }

         comboSelObj.addListener(SWT.Selection,new Listener() {
			public void handleEvent(Event event) {
				FieldDef annotatedField = null;
	            PlaceHolder holder = (PlaceHolder)comboSelObj.getData(comboSelObj.getText());
	            String selectedCurBusObName = holder.get_Name();
	            BusinessObjectDef businessObjectDef = m_api.get_LiveDefinitionLibrary().GetBusinessObjectDef(holder.get_Name());
	            if (businessObjectDef != null)
	            {
	                 annotatedField = businessObjectDef.GetAnnotatedField("GoToId");
	            }
	            if (annotatedField != null)
	            {
	                 String str = StringUtils.SetToken("输入 $$(OBJECTNAME)$$：","OBJECTNAME", annotatedField.get_Alias());
	                 lblInputSign.setText(str);
	            }
			}
		});
         
        if (comboSelObj.getItemCount()>0){
        	 comboSelObj.select(num2);
        }
	}
	
	protected void buttonPressed(int buttonId) {
		if(buttonId==IDialogConstants.OK_ID){
			 PlaceHolder holder=(PlaceHolder)comboSelObj.getData(comboSelObj.getText());
			 BusinessObjectDef businessObjectDef = m_api.get_LiveDefinitionLibrary().GetBusinessObjectDef(holder.get_Name());
			 FieldDef annotatedField = null;
			 if (businessObjectDef != null)
             {
                 annotatedField = businessObjectDef.GetAnnotatedField("GoToId");
             }
			 if (annotatedField != null)
	         {
                 SiteviewQuery query = new SiteviewQuery();
                 query.AddBusObQuery(businessObjectDef.get_Name(), QueryInfoToGet.Count);
                 query.set_BusObSearchCriteria(query.get_CriteriaBuilder().FieldAndValueExpression(annotatedField.get_Name(), Operators.Equals,txtInputSign.getText().trim()));
                 ICollection collection = m_api.get_BusObService().get_SimpleQueryResolver().ResolveQueryToBusObList(query);
                 BusinessObject m_bo =(BusinessObject)CollectionUtils.GetFirstItem(collection);
                 if (m_bo == null)
                 {
                    MessageDialog.openInformation(getShell(),"SiteView应用程序","未找到　"+businessObjectDef.get_Alias());
                 }
                 else
                 {
                	 settings.SetSetting("MostRecentObject", m_bo.get_Name());
                	 settings.Flush();
                	 m_bo.Clone();

                	 try {			
             			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new BusObNewInput(m_api,holder.get_Name(),m_bo), BusObMaintView.ID);
             		 }
                	 catch (PartInitException e) {
             			MessageDialog.openError(null, LegalUtils.get_MessageBoxCaption(), e.getMessage());
             			e.printStackTrace();
             		 }
                	 super.buttonPressed(buttonId);
                 }
	         }
		}
		else{
			super.buttonPressed(buttonId);
		}
	}
	
	/*
	 * 重写按钮描述
	 */
	protected void initializeBounds() {
		btnOK=super.getButton(IDialogConstants.OK_ID);
		super.getButton(IDialogConstants.CANCEL_ID).setText("取消");
		btnOK.setText("确定");
	}
}
