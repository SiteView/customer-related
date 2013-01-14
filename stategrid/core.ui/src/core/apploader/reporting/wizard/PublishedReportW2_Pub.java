package core.apploader.reporting.wizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import system.Collections.ICollection;
import system.Collections.IEnumerator;
import system.IO.DirectoryInfo;
import system.IO.Path;
import Siteview.IMru;

public class PublishedReportW2_Pub extends WizardPage {
	private static boolean canValidate = false; //��һ�ν���ҳ�� ����֤�������һ��ʱ��֤
	private PublishedReportWizard publishReportWizard;
	
	private Label lblSelectWhetherYou;
	private Button btn_br;
	private Combo cb_foldername;
	
	private boolean m_bPathMruLoaded;
	private String m_strPageId = "";
    private static String m_strPathMruId = "PathMru";
    private static String m_strPublisherId = "Core.ReportPublisher";
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW2_Pub() {
		super("PublishedReportW2_Pub");
		setTitle("\u62A5\u544A\u7684\u4F4D\u7F6E");
		setDescription("\u62A5\u544A\u5728\u4EC0\u4E48\u4F4D\u7F6E\uFF1F");
		
		m_strPageId = "PublishedReportW2_Pub";
	}
	
	public PublishedReportW2_Pub(PublishedReportWizard prw) {
		this();
		
		publishReportWizard = prw;
		m_strPageId = "PublishedReportW2_Pub";
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
		lblSelectWhetherYou.setBounds(10, 22, 202, 13);
		lblSelectWhetherYou.setText("\u62A5\u544A\u6240\u5728\u7684\u8DEF\u5F84\u3002\u793A\u4F8B\uFF1AC:\\My Reports");
		
		cb_foldername = new Combo(container, SWT.NONE);
		cb_foldername.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				LoadPathMru();
			}
		});
		cb_foldername.setBounds(0, 10, 447, 21);
		
		btn_br = new Button(container, SWT.NONE);
		btn_br.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblSelectWhetherYou.setText("������������ڵ�·����");				
			}
			@Override
			public void focusLost(FocusEvent e) {
				lblSelectWhetherYou.setText("\u62A5\u544A\u6240\u5728\u7684\u8DEF\u5F84\u3002\u793A\u4F8B\uFF1AC:\\My Reports");
			}
		});
		btn_br.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
//		        DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
//		        directoryDialog.setFilterPath("");
//		        directoryDialog.setMessage("��ѡ�񱨸������ļ� ��");
//		        String dir = directoryDialog.open();
		        FileDialog fileBrowser = new FileDialog(getShell());
				fileBrowser.setFilterExtensions(new String[]{"*.html;*.htm;*.htt"});
				String dir = fileBrowser.open();
		        if(dir != null) {
		        	System.out.println(dir);
		        	cb_foldername.add(dir);
		        	cb_foldername.setText(dir);
		        }
			}
		});

		btn_br.setBounds(453, 8, 80, 23);
		btn_br.setText("\u6D4F\u89C8");
	}
	
	@Override
	public void dispose(){   
		super.dispose();   
		setControl(null);   
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		canValidate = false;
		setDescription("\u60A8\u7684\u62A5\u544A\u5728\u54EA\u91CC?");
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
				IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW3_Pub");
				if(nextPage != null){
					if (nextPage.getControl() != null){ 
						nextPage.dispose();
					}
				}
				publishReportWizard.addPage(new PublishedReportW3_Pub(publishReportWizard));
				control = publishReportWizard.getPage("PublishedReportW3_Pub");
				return control;
			}
		}
		canValidate = true;
		return this;
	}
	
	// ����֤
	private boolean validate() {
		boolean flag = this.SaveValues();
        if (!flag || (cb_foldername.getText().trim().equalsIgnoreCase(""))){
        	setErrorMessage("����λ���ļ��в���Ϊ��");
        	MessageDialog.openWarning(getShell(), "SiteViewӦ�ó���", "����λ���ļ��в���Ϊ��");
            return false;
        }
        String directoryName = "";
        try{
            directoryName = Path.GetDirectoryName(cb_foldername.getText());
            DirectoryInfo di = new DirectoryInfo(cb_foldername.getText());
	        if (!di.get_Exists()){
	        	MessageDialog.openWarning(getShell(), "SiteViewӦ�ó���", "��������Ч��Ŀ¼��");
	            return false;
	        }
        }catch(Exception e){
        	MessageDialog.openWarning(getShell(), "SiteViewӦ�ó���", "��������Ч��Ŀ¼��");
            return false;
        }
        if (!flag || ((directoryName != null) && !(directoryName == ""))){
        	return true;
        }
        MessageDialog.openWarning(getShell(), "SiteViewӦ�ó���", "���ܴӸ�Ŀ¼�������档");
       	//���ܴӸ�Ŀ¼�������档
		return false;
    }
	
	// ��һ��ʱ,������Ϣ
	protected boolean SaveValues(){
        boolean flag = false;
        if (cb_foldername.getText().trim() != ""){
            publishReportWizard.setM_strPath(cb_foldername.getText().trim());
            this.SavePathMru();
            flag = true;
        }
        return flag;
    }
	
	// ����������
	private void SavePathMru(){
        if ((this.m_strPageId != null) && (this.m_strPageId != "")){
            IMru mru = publishReportWizard.getM_Api().get_SettingsService().get_Mru();
            if (mru != null){
                String strMruId = m_strPublisherId + "." + m_strPathMruId + "."+this.m_strPageId;
                String text = cb_foldername.getText();
                int index = cbIndex(cb_foldername,text);
                if(index!=-1){
                	cb_foldername.remove(index);
                }
                cb_foldername.add(text, 0);
                cb_foldername.setText(text);
                int count = cb_foldername.getItemCount();
                mru.SetMaximumItemCount(strMruId, 10);
                for (int i = Math.min(10, count) - 1; i >= 0; i--){
                    mru.AddItem(strMruId, (String) cb_foldername.getItem(i));
                }
                mru.Flush(strMruId);
            }
        }
    }
	
	// �������ʱ,��������
	private void LoadPathMru(){
		cb_foldername.removeAll();
        if ((this.m_strPageId != null) && (this.m_strPageId != "")){
            IMru mru = publishReportWizard.getM_Api().get_SettingsService().get_Mru();
            if (mru != null){
                String strMruId = m_strPublisherId + "." + m_strPathMruId + "."+this.m_strPageId;
                ICollection items = mru.GetItems(strMruId);
                if (items != null){
                	IEnumerator it = items.GetEnumerator();
                	while(it.MoveNext()){
                		String str2 = (String) it.get_Current();
                		cb_foldername.add(str2);
                	}
                }
            }
            this.m_bPathMruLoaded = true;
        }
    }
	
	// �õ�������ָ���������λ��
	private int cbIndex(Combo cb,String str){
    	int rtn = -1;
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
}
