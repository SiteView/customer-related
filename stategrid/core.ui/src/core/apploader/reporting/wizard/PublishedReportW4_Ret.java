package core.apploader.reporting.wizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import system.Environment;
import core.apploader.reporting.method.PublishedReportMethod;

public class PublishedReportW4_Ret extends WizardPage {
	private static boolean canValidate = false; //第一次进入页面 不验证，点击下一步时验证
	private PublishedReportWizard publishReportWizard;
	private Text txt_status;
	private Button btn_rpr;
	private Label lblSelectWhetherYou;
	
	PublishedReportMethod prm = new PublishedReportMethod();
	private boolean toNextPage = false;
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW4_Ret() {
		super("PublishedReportW4_Ret");
		setTitle("\u68C0\u7D22\u62A5\u544A");
		setDescription("\u9009\u62E9\u201C\u68C0\u7D22\u201D\u6309\u94AE\u5F00\u59CB\u5904\u7406\u3002");
	}
	public PublishedReportW4_Ret(PublishedReportWizard prw) {
		this();
		
		publishReportWizard = prw;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		
		Group group = new Group(container, SWT.NONE);
		group.setBounds(0, 281, 533, 60);
		
		lblSelectWhetherYou = new Label(group, SWT.NONE);
		lblSelectWhetherYou.setBounds(10, 22, 271, 26);
		lblSelectWhetherYou.setText("\u9009\u62E9\u8981\u68C0\u7D22\u7684\u62A5\u544A\u3002");
		
		btn_rpr = new Button(container, SWT.NONE);
		btn_rpr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Retrieve();
			}
		});
		btn_rpr.setBounds(0, 0, 102, 23);
		btn_rpr.setText("\u83B7\u53D6\u53D1\u5E03\u7684\u62A5\u544A");
		
		txt_status = new Text(container, SWT.BORDER | SWT.WRAP);
		txt_status.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblSelectWhetherYou.setText("检索状态。");
			}
			@Override
			public void focusLost(FocusEvent e) {
				lblSelectWhetherYou.setText("\u9009\u62E9\u8981\u68C0\u7D22\u7684\u62A5\u544A\u3002");
			}
		});
		txt_status.setEditable(false);
		txt_status.setBounds(0, 29, 533, 246);
	}
	
	private void Retrieve(){
		txt_status.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
        int nOkCnt = 0;
        int nNotOkCnt = 0;
        txt_status.setText("");
        StringBuffer status_str = new StringBuffer();
        // base.m_ep.SetError(base.m_rtbStatus, "");
        for(int i=0;i<publishReportWizard.get_Reports().size();i++){
        	String str2 = (String) publishReportWizard.get_Reports().get(i);
            String str3 = null;
            String str = "正在检索 '"+ str2 +"'...";//StringUtils.SetToken(Core.Reporting.Res.Default.GetString("RetRetrievePage.RetrievingReport"), "REPORTNAME", str2);
            status_str.append(str + Environment.get_NewLine());
            txt_status.setText(status_str.toString());
            // str2 = "Reports/问题报告.rptdesign";
            String Result = prm.DownLoadFileFromServerToLocal(publishReportWizard.getM_strUrlOnServer(), str2, publishReportWizard.getM_strPath());
            String[] r_str = Result.split("~");
            str3 = r_str[1];
            if (r_str[0].equalsIgnoreCase("true")){
                str = "  已检索到文件。";// Core.Reporting.Res.Default.GetString("RetRetrievePage.FileRetrieved");
                status_str.append(str + Environment.get_NewLine());
                txt_status.setText(status_str.toString());
                nOkCnt++;
            }else if (r_str[0].equalsIgnoreCase("false")){
            	str3 = (str3 != null) ? str3 : "";
            	status_str.append("  未检索到文件。"+str3 + Environment.get_NewLine());
                txt_status.setText(status_str.toString());
                nNotOkCnt++;
            }
            status_str.append(Environment.get_NewLine());
            txt_status.setText(status_str.toString());
            txt_status.setFocus();
        }
        if (nNotOkCnt != 0){
            // base.m_ep.SetError(base.m_rtbStatus, "未检索到一个或多个报告。");
        }
        this.BuildSummary(nOkCnt, nNotOkCnt);
        txt_status.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
    }
	
	private void BuildSummary(int nOkCnt, int nNotOkCnt){
        String str;
        StringBuilder builder = new StringBuilder();
        if (nOkCnt == 1){
            str = "已成功检索到一个报告。";
            builder.append(str);
        }else if (nOkCnt > 1){
            str = "已成功检索到 "+nOkCnt+" 个报告。";
            builder.append(str);
        }
        if ((nOkCnt != 0) && (nNotOkCnt != 0)){
            builder.append(Environment.get_NewLine());
        }
        if (nNotOkCnt == 1){
            str = "未检索到一个报告。";
            builder.append(str);
        }else if (nNotOkCnt > 1){
            str = nNotOkCnt + " 个报告未检索到。";
            builder.append(str);
        }
        publishReportWizard.setM_SummaryPageHeading("摘要");
        publishReportWizard.setM_SummaryPageMessage(builder.toString());
    }
	
	private boolean validate(){
		if(publishReportWizard.getM_SummaryPageHeading().equalsIgnoreCase("")){
        	MessageDialog.openWarning(getShell(), "SiteView应用程序", "选择\"检索\"按钮开始处理。");
        	btn_rpr.setFocus();
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		canValidate = false;
		return super.getPreviousPage();
	}
	
	@Override
	public IWizardPage getNextPage() {
		IWizardPage control = null;
		if(canValidate){
			if(!validate()) {
				canValidate = true;
				return this;
			}else{
				canValidate = false;
				if(toNextPage){
					IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW4_Fin");
					if(nextPage != null){
						if (nextPage.getControl() != null){ 
							nextPage.dispose();
						}
					}
					publishReportWizard.addPage(new PublishedReportW4_Fin(publishReportWizard));
					control = publishReportWizard.getPage("PublishedReportW4_Fin");
					return control;
				}else{
					return this;
				}
			}
		}
		canValidate = true;
		return this;
	}

	@Override
	public void setVisible(boolean visible) {
		if(visible){
			toNextPage = true;
		}
		super.setVisible(visible);
	}
	
	@Override
	public void dispose() {   
		super.dispose();   
		setControl(null);   
	}
	
	public PublishedReportWizard getPublishReportWizard() {
		return publishReportWizard;
	}
	public void setPublishReportWizard(PublishedReportWizard publishReportWizard) {
		this.publishReportWizard = publishReportWizard;
	}
}
