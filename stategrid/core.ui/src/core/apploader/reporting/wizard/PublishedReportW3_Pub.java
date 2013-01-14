package core.apploader.reporting.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.IO.DirectoryInfo;
import system.IO.FileInfo;

public class PublishedReportW3_Pub extends WizardPage {
	private static boolean canValidate = false; //第一次进入页面 不验证，点击下一步时验证
	private PublishedReportWizard publishReportWizard;
	
	private Tree file_tree;
	private Label lblSelectWhetherYou;
	
	protected boolean m_bIgnoreCheck;
	private boolean toNextPage = false;
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW3_Pub() {
		super("PublishedReportW3_Pub");
		setTitle("\u8981\u53D1\u5E03\u7684\u62A5\u544A");
		setDescription("\u4EE5\u4E0B\u662F\u627E\u5230\u7684\u62A5\u544A\u3002\u9009\u62E9\u8981\u53D1\u5E03\u7684\u62A5\u544A\u3002");
	}
	public PublishedReportW3_Pub(PublishedReportWizard prw) {
		this();
		
//		setErrorMessage("测试出错信息");
		publishReportWizard = prw;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		container.setLayout(new FormLayout());
		
		Group group = new Group(container, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 341);
		fd_group.right = new FormAttachment(0, 533);
		fd_group.top = new FormAttachment(0, 281);
		fd_group.left = new FormAttachment(0);
		group.setLayoutData(fd_group);
		
		lblSelectWhetherYou = new Label(group, SWT.NONE);
		lblSelectWhetherYou.setBounds(10, 22, 271, 26);
		lblSelectWhetherYou.setText("\u4ECE\u53EF\u7528\u62A5\u544A\u4E2D\u9009\u62E9\u8981\u53D1\u5E03\u7684\u62A5\u544A\u3002");
		
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setSelection(true);
		btnCheckButton.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				lblSelectWhetherYou.setText("如果选择此选项，将会取代服务器上与这些选项相符的报告。");
			}
			@Override
			public void focusLost(FocusEvent e) {
				lblSelectWhetherYou.setText("\u4ECE\u53EF\u7528\u62A5\u544A\u4E2D\u9009\u62E9\u8981\u53D1\u5E03\u7684\u62A5\u544A\u3002");
			}
		});
		FormData fd_btnCheckButton = new FormData();
		fd_btnCheckButton.top = new FormAttachment(0);
		fd_btnCheckButton.left = new FormAttachment(0);
		btnCheckButton.setLayoutData(fd_btnCheckButton);
		btnCheckButton.setText("\u53D6\u4EE3\u670D\u52A1\u5668\u4E0A\u7684\u73B0\u6709\u62A5\u544A");
		
		Composite composite = new Composite(container, SWT.BORDER);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(0, 533);
		fd_composite.bottom = new FormAttachment(0, 275);
		fd_composite.top = new FormAttachment(0, 22);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		file_tree = new Tree(composite,SWT.CHECK);
		file_tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final TreeItem item = (TreeItem) e.item;
				file_tree.setSelection(item); 
				List authorities = new ArrayList<Object>();  
				publishReportWizard.checkItem(item,authorities);  
			}
		});
		
		loadData();
	}

	public void loadData(){
		FillTreeView();
	}
	private void FillTreeView(){
		file_tree.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
		String filePath = publishReportWizard.getM_strPath();
		file_tree.removeAll();
        this.FillTreeView(file_tree, filePath ,null);
        if(file_tree.getItemCount()>0){
        	file_tree.getItem(0).setExpanded(true);
        }else{
        	ExpandParametersTree(file_tree.getItems());
        }
        
        file_tree.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
    }
	
	private void FillTreeView(Tree aTree, String strName,TreeItem rootItem){
        DirectoryInfo di = new DirectoryInfo(strName);
        if (di.get_Exists()){
            if (this.DirectoryTreeHasReports(di)){
            	TreeItem node = null;
            	if(rootItem==null){
            		node = new TreeItem(aTree,SWT.NONE);
            	}else{
            		node = new TreeItem(rootItem,SWT.NONE);
            	}
                node.setText(di.get_Name());
                node.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png")));
                node.setData("Folder~"+di.get_Name());
                for (DirectoryInfo info2 : di.GetDirectories()){
                    this.FillTreeView(aTree, info2.get_FullName(),node);
                }
                for (FileInfo info3 : di.GetFiles()){
                    this.FillTreeView(aTree, info3.get_FullName(),node);
                }
                node.setExpanded(true);
            }
        }else{
            FileInfo info4 = new FileInfo(strName);
            if (info4.get_Exists() && (info4.get_Extension().equalsIgnoreCase(publishReportWizard.getM_Extension()))){
            	TreeItem node2 = null;
            	if(rootItem==null){
            		node2 = new TreeItem(aTree,SWT.NONE);
            	}else{
            		node2 = new TreeItem(rootItem,SWT.NONE);
            	}
            	node2.setText(info4.get_Name());
            	node2.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png")));
            	node2.setData("Report~"+info4.get_Name());
            }
        }
    }
	
	private boolean DirectoryTreeHasReports(DirectoryInfo di){
        boolean flag = false;
        for (FileInfo info : di.GetFiles()){
            if (info.get_Extension().equalsIgnoreCase(publishReportWizard.getM_Extension())){
                flag = true;
                break;
            }
        }
        if (!flag){
            for(DirectoryInfo info2 : di.GetDirectories()){
                if (this.DirectoryTreeHasReports(info2)){
                    return true;
                }
            }
        }
        return flag;
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
	
	protected boolean SaveValues(){
		file_tree.setCursor(new Cursor(null,SWT.CURSOR_WAIT));
        publishReportWizard.get_Reports().clear();
        this.SaveValues(file_tree.getItems(), publishReportWizard);
        file_tree.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
        
        return (publishReportWizard.get_Reports().size() != 0);
    }

    protected void SaveValues(TreeItem[] aTreeNodes, PublishedReportWizard f){
        for (TreeItem node : aTreeNodes){
        	String[] strArray = ((String) node.getData()).split("~");
            if ((strArray[0].equalsIgnoreCase("Report")) && node.getChecked()){
                f.get_Reports().add(strArray[1]);
            }else if (strArray[0].equalsIgnoreCase("Folder")){
                this.SaveValues(node.getItems(), f);
            }
        }
    }
    
	public boolean validate(){
    	boolean flag = this.SaveValues();
    	if(!flag){
    		MessageDialog.openWarning(getShell(), "SiteView应用程序", "请选择一个或更多个报告。");
    		file_tree.setFocus();
    		return flag;
    	}
    	return flag;
    }
	
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
				if(SaveValues()){
					if(toNextPage){
						IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW4_Ver");
						if(nextPage != null){
							if (nextPage.getControl() != null){ 
								nextPage.dispose();
							}
						}
						publishReportWizard.addPage(new PublishedReportW4_Ver(publishReportWizard));
						control = publishReportWizard.getPage("PublishedReportW4_Ver");
						return control;
					}else{
						return this;
					}
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
}
