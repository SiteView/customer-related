package core.apploader.views;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import core.apploader.editors.ServiceCatelogEditor;
import core.apploader.editors.ServiceCatelogEditorInput;
import core.busobmaint.BusObMaintView;

import siteview.windows.forms.ImageHelper;
import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ConnectionBroker;

public class ServiceCatelogView extends ViewPart{

	public static final String ID = "core.apploader.views.ServiceCatelogView"; //$NON-NLS-1$
	
	private  TreeViewer treeviwer;
	
	private  Map<String,List<BusinessObjectDef>> sc;
	
	private  ISiteviewApi api;
	
	private  ServiceCatelogEditorInput sci;

	private TreeItem hoteItem;

	public ServiceCatelogView() {
	}
	
	

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		api = ConnectionBroker.get_SiteviewApi();
		container.setLayout(new FillLayout());
		
		sc= new HashMap<String,List<BusinessObjectDef>>();
		
		ICollection busLinks = api.get_BusObDefinitions().get_BusinessObjectNames();//(DefRequest.ForList(BusinessObjectDef.get_ClassName()));
		IEnumerator it = busLinks.GetEnumerator();
		while(it.MoveNext()){
			String boName = (String)it.get_Current();
			
			BusinessObjectDef boDef = api.get_BusObDefinitions().GetBusinessObjectDef(boName);
			if(boDef.get_Annotations().get_Item("SERVICE")!=null){
				String category = "未分类";
				if (boDef.get_Annotations().get_Item("SERVICE_CATEGORY")!=null){
					category = (String) boDef.get_Annotations().get_Item("SERVICE_CATEGORY");
				}
				
				if (sc.get(category)!=null){
					sc.get(category).add(boDef);
				}else{
					sc.put(category, new Vector<BusinessObjectDef>());
					sc.get(category).add(boDef);
				}
			}
			
		}
		
		treeviwer = new TreeViewer(container,SWT.FULL_SELECTION);
		TreeViewerColumn tvc = new TreeViewerColumn(treeviwer,SWT.NONE);
		tvc.getColumn().setWidth(200);
	
		treeviwer.setContentProvider(new ITreeContentProvider(){

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				
			}

			@Override
			public Object[] getElements(Object inputElement) {
				Map<String,List<?>> sc = (Map<String, List<?>>) inputElement;
				return sc.entrySet().toArray();
				
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof Entry){
					Entry<String,List<BusinessObjectDef>> entry = (Entry<String, List<BusinessObjectDef>>) parentElement;
					return entry.getValue().toArray();
				}
				return null;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof Entry){
					Entry<String,List<BusinessObjectDef>> entry = (Entry<String, List<BusinessObjectDef>>) element;
					return entry.getValue().size()>0;
				}
				return false;
			}});
		
		treeviwer.setLabelProvider(new LabelProvider(){

			@Override
			public Image getImage(Object element) {
				if (element instanceof BusinessObjectDef){
					BusinessObjectDef boDef = (BusinessObjectDef) element;
					return ImageHelper.getImage(boDef.get_AssociatedImage(),18,18);
				}else if (element instanceof Entry){
					return ImageHelper.LoadImage(core.ui.Activator.PLUGIN_ID, "/icons/folder.png");
				}
				return super.getImage(element);
			}

			@Override
			public String getText(Object element) {
				if (element instanceof BusinessObjectDef){
					BusinessObjectDef boDef = (BusinessObjectDef) element;
					return boDef.get_Alias();
				}else if (element instanceof Entry){
					Entry<String,List<BusinessObjectDef>> entry = (Entry<String, List<BusinessObjectDef>>) element;
					return entry.getKey();
				}
				return super.getText(element);
			}});
		
		treeviwer.setAutoExpandLevel(2);
		treeviwer.getTree().setLinesVisible(false);
		treeviwer.addDoubleClickListener(new IDoubleClickListener(){

			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection iss = (IStructuredSelection) event.getSelection();
				if (iss.getFirstElement() !=null && iss.getFirstElement() instanceof BusinessObjectDef){
					BusinessObjectDef bo = (BusinessObjectDef)iss.getFirstElement();
					BusObMaintView.newBusOb(ConnectionBroker.get_SiteviewApi(), bo.get_Name());
				}
				else{
					if(treeviwer.getTree().getSelection()[0].getText().trim().equals("热门服务")){
						try {
							IWorkbenchPage page=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
							page.openEditor(new ServiceCatelogEditorInput(sc,api),ServiceCatelogEditor.ID);
							
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				}
			}});
		treeviwer.setInput(sc);
		//treeviwer.getTree().pack();
		createActions();
		initializeToolBar();
		initializeMenu();
		
		
		sci=new ServiceCatelogEditorInput(sc,api);
		
		hoteItem = new TreeItem(treeviwer.getTree(),SWT.NONE,0); 
		hoteItem.setText("热门服务");
		hoteItem.setImage(ImageHelper.LoadImage(core.ui.Activator.PLUGIN_ID, "/icons/folder.png"));
		
		
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart()==this){
			this.treeviwer.getTree().setSelection(this.hoteItem);
			Display.getCurrent().asyncExec(new Runnable(){
	
				@Override
				public void run() {
					try {
					IWorkbenchPage page=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(sci,ServiceCatelogEditor.ID);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}});
		}
	}
}
