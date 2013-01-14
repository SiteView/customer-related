package core.apploader.reporting.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Siteview.PlaceHolder;
import Siteview.PublishedReportDef;

public class PublishedReportW2_Del extends WizardPage {
	private static boolean canValidate = false; //第一次进入页面 不验证，点击下一步时验证
	private PublishedReportWizard publishReportWizard;
	private Tree file_tree;
	private boolean toNextPage = false;
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW2_Del() {
		super("PublishedReportW2_Del");
		setTitle("\u8981\u5220\u9664\u7684\u62A5\u544A");
		setDescription("\u4EE5\u4E0B\u662F\u627E\u5230\u7684\u62A5\u544A\u3002\u9009\u62E9\u8981\u5220\u9664\u7684\u62A5\u544A\u3002");
	}
	public PublishedReportW2_Del(PublishedReportWizard prw) {
		super("PublishedReportW2_Del");
		setTitle("\u8981\u5220\u9664\u7684\u62A5\u544A");
		setDescription("\u4E0B\u9762\u662F\u88AB\u53D1\u73B0\u7684\u62A5\u544A\u3002\u9009\u62E9\u60A8\u8981\u5220\u9664\u7684\u62A5\u544A\u3002");
		
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
		
		Label lblSelectWhetherYou = new Label(group, SWT.NONE);
		lblSelectWhetherYou.setBounds(10, 22, 271, 26);
		lblSelectWhetherYou.setText("\u4ECE\u53EF\u7528\u62A5\u544A\u4E2D\u9009\u62E9\u8981\u5220\u9664\u7684\u62A5\u544A\u3002");
		
		Composite composite = new Composite(container, SWT.BORDER);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 275);
		fd_composite.right = new FormAttachment(0, 533);
		fd_composite.top = new FormAttachment(0);
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
		file_tree.removeAll();
        // Cursor.Current = Cursors.WaitCursor;
		ICollection placeHolders = publishReportWizard.GetPlaceHolders();
		TreeItem parentItem = null;
        if (placeHolders != null){
            IEnumerator it = placeHolders.GetEnumerator();
            while(it.MoveNext()){
            	PlaceHolder holder = (PlaceHolder) it.get_Current();
            	PublishedReportDef definition = (PublishedReportDef) publishReportWizard.GetDefinition(holder.get_Name());
                if (definition != null){
                    TreeItem[] aTreeNodes = file_tree.getItems();
                    String[] strArray = definition.get_Filename().split("\\\\");
                    int length = strArray.length;
                    for (int i = 0; i < length; i++){
                        String strName = strArray[i];
                        if (i != (length - 1)){
                            TreeItem node = publishReportWizard.ContainsFolder(aTreeNodes, strName);
                            if (node == null){
                            	if(parentItem==null){
                            		node = new TreeItem(file_tree,SWT.None);//new TreeNode(strName, base.m_nFolderClosedImgIdx, base.m_nFolderOpenImgIdx);
                            	}else{
                            		node = new TreeItem(parentItem,SWT.None);
                            	}
                                node.setText(strName);
                                node.setData("Folder~"+definition.get_Filename());
                                node.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.FolderOpen.png")));
                            }
                            if (node != null){
                                aTreeNodes = node.getItems();
                                parentItem = node;
                            }
                            node.setExpanded(true);
                        }else{
                        	TreeItem node2 = null;
                        	if(parentItem==null){
                        		node2 = new TreeItem(file_tree,SWT.None);//new TreeNode(strName, base.m_nFolderClosedImgIdx, base.m_nFolderOpenImgIdx);
                        	}else{
                        		node2 = new TreeItem(parentItem,SWT.None);
                        	}
                        	node2.setText(strName);
                        	node2.setImage(SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#Images.Icons.Report.png")));
                            node2.setData("Report~"+definition.get_Filename());
                            node2.setExpanded(true);
                        }
                    }
                }
            }
        }
        file_tree.setCursor(new Cursor(null,SWT.CURSOR_ARROW));
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
    
	private boolean validate(){
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
				if(toNextPage){
					IWizardPage nextPage = publishReportWizard.getPage("PublishedReportW3_Del");
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
