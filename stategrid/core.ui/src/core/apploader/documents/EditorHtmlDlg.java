package core.apploader.documents;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import Siteview.Windows.Forms.BrowserEditer;


public class EditorHtmlDlg extends Dialog{

	private Browser browser;
	private String context;
	
	
	protected EditorHtmlDlg(Shell shell, String context) {
		super(shell);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setSize(700, 730);
		newShell.setLocation(300, 70);
		newShell.setText("HTML ±à¼­Æ÷");
		super.configureShell(newShell);
	}
	

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout());
		browser = new Browser(container, SWT.NONE);
		browser.setUrl(BrowserEditer.getPath());
		//browser.pack();
		if(context != null && !"".equals(context)){
			context = context.replaceAll("\r\n", "");
			if(context.indexOf("\"") != -1) context = context.replaceAll("\"", "'");
			browser.addProgressListener(new ProgressListener(){

				public void changed(ProgressEvent event) {}
				
				public void completed(ProgressEvent event) {
					browser.execute("$('#elm1').val(\"" + context + "\");");
//					Object obj=browser.evaluate("return $('#elm1').val();");
//					System.out.println(obj);
				}
			});
		}
		return container;
	}
	
	@Override
	protected void okPressed() {
		Object obj = browser.evaluate("return $('#elm1').val();");
		context = obj.toString();
//		System.out.println(context);
		super.okPressed();
	}
	
	public String getContext(){
		return context;
	}
}
