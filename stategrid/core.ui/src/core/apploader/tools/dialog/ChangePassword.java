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
import Siteview.SiteviewSecurityException;
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
import system.Windows.Forms.MessageBox;

public class ChangePassword extends Dialog{
	/*
	 * ����
	 */
	private String title="��������";
	
	/*
	 * ����ؼ�
	 */
	
	/*
	 * ���ݽӿ�
	 */
	private ISiteviewApi m_api;
	private Text txtLoginSign;
	private Text txtOldPwd;
	private Text txtNewPwd;
	private Text txtConfirmPwd;
	
	/*
	 * ���췽��
	 */
	public ChangePassword(Shell shell){
		super(shell);
		m_api=ConnectionBroker.get_SiteviewApi();
		
	}
	
	/*
	 * ��ʼ������
	 */
	protected void configureShell(Shell newShell) {
		newShell.setSize(430,280);
		newShell.setLocation(450,280);
		newShell.setText(title);
		super.configureShell(newShell);
	}
	
	/*
	 * �������Ԫ��
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite=(Composite)super.createDialogArea(parent);
		composite.setLayout(null);
		
		Label lblLoginSign = new Label(composite, SWT.NONE);
		lblLoginSign.setBounds(10, 10, 54, 12);
		lblLoginSign.setText("\u767B\u5F55\u6807\u8BC6:");
		
		txtLoginSign = new Text(composite, SWT.BORDER);
		txtLoginSign.setBounds(10, 25, 400, 18);
		txtLoginSign.setEnabled(false);
		
		Label lblOldPwd = new Label(composite, SWT.NONE);
		lblOldPwd.setText("\u65E7\u5BC6\u7801:");
		lblOldPwd.setBounds(10, 49, 54, 12);
		
		txtOldPwd = new Text(composite, SWT.BORDER|SWT.PASSWORD);
		txtOldPwd.setBounds(10, 67, 400, 18);
		
		Label lblNewPwd = new Label(composite, SWT.NONE);
		lblNewPwd.setText("\u65B0\u5BC6\u7801:");
		lblNewPwd.setBounds(10, 91, 54, 12);
		
		txtNewPwd = new Text(composite, SWT.BORDER|SWT.PASSWORD);
		txtNewPwd.setBounds(10, 109, 400, 18);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("\u786E\u8BA4\u65B0\u5BC6\u7801:");
		label.setBounds(10, 136, 77, 12);
		
		txtConfirmPwd = new Text(composite, SWT.BORDER|SWT.PASSWORD);
		txtConfirmPwd.setBounds(10, 151, 400, 18);
		
		txtLoginSign.setText(m_api.get_SystemFunctions().get_CurrentLoginId());

		return composite;
	}

	protected void buttonPressed(int buttonId) {
		if(buttonId==IDialogConstants.OK_ID){
			String title="SiteViewӦ�ó���";
			String message="";
			if(txtLoginSign.getText().trim().length()==0){
				message="����ָ���û�����";
			}
			else if(txtOldPwd.getText().trim().length()==0){
				message="����ָ�������롣";
			}
			else if(txtNewPwd.getText().trim().length()==0){
				message="����ָ�������롣";
			}
			else if(txtNewPwd.getText().trim().length()<5){
				message="�����볤�Ȳ�����5λ";
			}
			else if(txtConfirmPwd.getText().trim().length()==0){
				message="����ȷ��������";
			}
			else if(!txtConfirmPwd.getText().trim().equals(txtNewPwd.getText().trim())){
				message="δ��ȷȷ�������롣��ȷ���������ȷ��������ȫ��ͬ��";
			}
			
			if(message.length()!=0){
				MessageDialog.openInformation(getShell(), title, message);
				return;
			}
			
			try{
                 m_api.get_AuthenticationService().ChangePassword("User",txtLoginSign.getText().trim(),txtOldPwd.getText().trim(),txtNewPwd.getText().trim());
                 message="�޸ĳɹ�!��";
                 MessageDialog.openInformation(getShell(), title, message);
			}
            catch (SiteviewSecurityException exception){
            	 message="�û���������벻��ȷ.";
            	 MessageDialog.openInformation(getShell(), title, message);
            	 return;
            }

		}
		super.buttonPressed(buttonId);
	}
	
	/*
	 * ��д��ť����
	 */
	protected void initializeBounds() {
		super.getButton(IDialogConstants.OK_ID).setText("ȷ��");
		super.getButton(IDialogConstants.CANCEL_ID).setText("ȡ��");
	}
}
