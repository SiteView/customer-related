package core.apploader.reporting.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import system.Environment;
import system.IO.Path;
import Siteview.PublishedReportDef;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PublishedReportW3_Del extends WizardPage {
	
	private PublishedReportWizard publishReportWizard;
	private Text txt_status;
	private Button btn_rpr;
	private Label lblSelectWhetherYou;
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW3_Del() {
		super("PublishedReportW3_Del");
		setTitle("\u5220\u9664\u62A5\u544A");
		setDescription("\u9009\u62E9\u201C\u5220\u9664\u201D\u6309\u94AE\u5F00\u59CB\u5904\u7406\u3002");
	}
	public PublishedReportW3_Del(PublishedReportWizard prw) {
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
		lblSelectWhetherYou.setText("\u9009\u62E9\u8981\u5220\u9664\u7684\u62A5\u544A\u3002");
		
		btn_rpr = new Button(container, SWT.NONE);
		btn_rpr.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Delete();
			}
		});
		btn_rpr.setBounds(0, 0, 102, 23);
		btn_rpr.setText("\u5220\u9664\u53D1\u5E03\u7684\u62A5\u544A");
		
		txt_status = new Text(container, SWT.BORDER | SWT.WRAP);
		txt_status.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblSelectWhetherYou.setText("删除状态。");
			}
			@Override
			public void focusLost(FocusEvent e) {
				lblSelectWhetherYou.setText("\u9009\u62E9\u8981\u5220\u9664\u7684\u62A5\u544A\u3002");
			}
		});
		txt_status.setEditable(false);
		txt_status.setBounds(0, 29, 533, 246);
		
		
	}

    private void Delete(){
        int nOkCnt = 0;
        int nNotOkCnt = 0;
        txt_status.setText("");
        StringBuffer status_str = new StringBuffer();
        for(int i=0;i<publishReportWizard.get_Reports().size();i++){
        	String str2 = (String) publishReportWizard.get_Reports().get(i);
            String str = "正在删除 '"+ str2 +"'...";//StringUtils.SetToken(Core.Reporting.Res.Default.GetString("DelDeletePage.DeletingReport"), "REPORTNAME", str2);
            status_str.append(str + Environment.get_NewLine());
            txt_status.setText(status_str.toString());
            PublishedReportDef definition = (PublishedReportDef) publishReportWizard.GetDefinition(Path.GetFileNameWithoutExtension(str2));
            if (definition != null){
                String str3 = "";
                // 此处要调整
                if (publishReportWizard.DeleteFileFromServer(definition.get_Filename(),str3)){
                    str = "  已删除文件。";//Core.Reporting.Res.Default.GetString("DelDeletePage.FileDeleted");
                    status_str.append(str + Environment.get_NewLine());
                    txt_status.setText(status_str.toString());
                    if (publishReportWizard.DeleteDefinition(definition)){
                        str = "  已删除定义。";//Core.Reporting.Res.Default.GetString("DelDeletePage.DefinitionDeleted");
                        status_str.append(str + Environment.get_NewLine());
                        txt_status.setText(status_str.toString());
                        nOkCnt++;
                    }else{
                        str = "  未删除定义。";//Core.Reporting.Res.Default.GetString("DelDeletePage.DefinitionNotDeleted");
                        status_str.append(str + Environment.get_NewLine());
                        txt_status.setText(status_str.toString());
                        nNotOkCnt++;
                    }
                }else{
                	str3 = (str3 != null) ? str3 : "";
                    str = "  未删除文件。"+str3;//"StringUtils.SetToken(Core.Reporting.Res.Default.GetString("DelDeletePage.FileNotDeleted"), "ERRORMSG", (str3 != null) ? str3 : "");
                    status_str.append(str + Environment.get_NewLine());
                    txt_status.setText(status_str.toString());
                    nNotOkCnt++;
                }
            }else{
            	str = "  未找到报告。";
            	status_str.append(str + Environment.get_NewLine());
            	txt_status.setText(status_str.toString());
            }
            status_str.append(Environment.get_NewLine());
            txt_status.setText(status_str.toString());
            txt_status.setFocus();
        }
        if (nNotOkCnt != 0){
            //未删除一个或多个报告。base.m_ep.SetError(base.m_rtbStatus, Core.Reporting.Res.Default.GetString("DelDeletePage.ReportsNotDeleted"));
        }
        this.BuildSummary(nOkCnt, nNotOkCnt);
    }
	
    private void BuildSummary(int nOkCnt, int nNotOkCnt){
        String str;
        StringBuilder builder = new StringBuilder();
        if (nOkCnt == 1){
            str = "已成功删除一个报告。";//Core.Reporting.Res.Default.GetString("DelDeletePage.OneReportDeleted");
            builder.append(str);
        }else if (nOkCnt > 1){
            str = "已成功删除 "+nOkCnt+" 个报告。";//StringUtils.SetToken(Core.Reporting.Res.Default.GetString("DelDeletePage.NReportsDeleted"), "COUNT", nOkCnt);
            builder.append(str);
        }
        if ((nOkCnt != 0) && (nNotOkCnt != 0)){
            builder.append(Environment.get_NewLine());
        }
        if (nNotOkCnt == 1){
            str = "一个报告未删除。";//Core.Reporting.Res.Default.GetString("DelDeletePage.OneReportNotDeleted");
            builder.append(str);
        }else if (nNotOkCnt > 1){
            str = nNotOkCnt+" 个报告未删除。";//StringUtils.SetToken(Core.Reporting.Res.Default.GetString("DelDeletePage.NReportsNotDeleted"), "COUNT", nNotOkCnt);
            builder.append(str);
        }
        // ((StdSummaryPage) base.publishReportWizard.SummaryPage).SummaryPageHeading = "摘要";//Core.Reporting.Res.Default.GetString("DelDeletePage.SummaryPageHeading");
        // ((StdSummaryPage) base.publishReportWizard.SummaryPage).SummaryPageMessage = builder.toString();
    }
    
    
	public PublishedReportWizard getPublishReportWizard() {
		return publishReportWizard;
	}
	public void setPublishReportWizard(PublishedReportWizard publishReportWizard) {
		this.publishReportWizard = publishReportWizard;
	}
}
