package core.apploader.reporting.wizard;

import java.util.Hashtable;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;


public class PublishedReportW1_Opt extends WizardPage {
	
	private PublishedReportWizard publishReportWizard;
	
	private Label lb_pr;
	private Button rbtn_pr;
	private Label lb_rr;
	private Button rbtn_rr;
	private Label lb_dr;
	private Button rbtn_dr;
	private Hashtable m_htTypeToControl = new Hashtable();
     
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW1_Opt() {
		super("PublishedReportW1_Opt");
		setTitle("\u53D1\u5E03\u9009\u9879");
		setDescription("\u60A8\u8981\u5E72\u4EC0\u4E48\uFF1F");
	}
	
	public PublishedReportW1_Opt(PublishedReportWizard prw) {
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
		
		Label lblSelectWhetherYou = new Label(group, SWT.NONE);
		lblSelectWhetherYou.setBounds(10, 22, 374, 26);
		lblSelectWhetherYou.setText("\u9009\u62E9\u662F\u5426\u8981\u53D1\u5E03\u3001\u68C0\u7D22\u6216\u5220\u9664\u670D\u52A1\u5668\u4E0A\u7684\u62A5\u544A\u3002");
		
		lb_pr = new Label(container, SWT.NONE);
		lb_pr.setBounds(69, 79, 48, 48);
		lb_pr.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportPublish.png"),48,48));
		
		rbtn_pr = new Button(container, SWT.RADIO);
		rbtn_pr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				getNextPage();
			}
		});
		rbtn_pr.setBounds(59, 133, 69, 16);
		rbtn_pr.setText("\u53D1\u5E03\u62A5\u544A");
		rbtn_pr.setData("PublishedReportW2_Pub");
		
		lb_rr = new Label(container, SWT.NONE);
		lb_rr.setBounds(185, 79, 48, 48);
		lb_rr.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportRetrieve.png"),48,48));
		
		
		rbtn_rr = new Button(container, SWT.RADIO);
		rbtn_rr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				getNextPage();
			}
		});
		rbtn_rr.setBounds(175, 133, 69, 16);
		rbtn_rr.setText("\u68C0\u7D22\u62A5\u544A");//ªÒ»°\u68C0\u7D22\
		rbtn_rr.setData("PublishedReportW2_Ret");
		
		lb_dr = new Label(container, SWT.NONE);
		lb_dr.setBounds(297, 79, 48, 48);
		lb_dr.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.ReportDelete.png"),48,48));
		
		
		rbtn_dr = new Button(container, SWT.RADIO);
		rbtn_dr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				getNextPage();
			}
		});
		rbtn_dr.setBounds(287, 133, 69, 16);
		rbtn_dr.setText("\u5220\u9664\u62A5\u544A");
		rbtn_dr.setData("PublishedReportW2_Del");
		
	}
	
	@Override
	public IWizardPage getNextPage() {
		boolean pr_b = rbtn_pr.getSelection();
		boolean rr_b = rbtn_rr.getSelection();
		boolean dr_b = rbtn_dr.getSelection();
		IWizardPage control = null;
		Object tag = null;
		if(pr_b){
			tag = rbtn_pr.getData();
			IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW2_Pub");
			if(nextPage != null){
				if (nextPage.getControl() != null){ 
					nextPage.dispose();
				}
			}
			control = publishReportWizard.getPage("PublishedReportW2_Pub");
			// m_htTypeToControl.put(tag, control);
		}else if(rr_b){
			tag = rbtn_rr.getData();
			IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW2_Ret");
			if(nextPage != null){
				if (nextPage.getControl() != null){ 
					nextPage.dispose();
				}
			}
			publishReportWizard.addPage(new PublishedReportW2_Ret(publishReportWizard));
			control = publishReportWizard.getPage("PublishedReportW2_Ret");
		}else if(dr_b){
			tag = rbtn_dr.getData();
			IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW2_Del");
			if(nextPage != null){
				if (nextPage.getControl() != null){ 
					nextPage.dispose();
				}
			}
			publishReportWizard.addPage(new PublishedReportW2_Del(publishReportWizard));
			control = publishReportWizard.getPage("PublishedReportW2_Del");
			// m_htTypeToControl.put(tag, control);
		}else{
			control = super.getNextPage();
		}
		return control;
	}
}
