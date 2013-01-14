package core.apploader.editors;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import siteview.windows.forms.ImageHelper;
import Siteview.ISecurity;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Api.ISiteviewApi;
import Siteview.Windows.Forms.ConnectionBroker;
import core.busobmaint.BusObMaintView;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;


public class ServiceCatelogEditor extends EditorPart{
	/*
	 * ID值
	 */
	public static final String ID="core.apploader.editors.ServiceCatelogEditor";
	
	/*
	 * 编辑器变量
	 */
	private boolean bDirty=false;//设置修改状态
	private boolean bSaveAsAllowed=true;//设置保存状态
	

	/*
	 * 传递参数
	 */
	private Map<String,List<BusinessObjectDef>> sc;

	/*
	 * 数据接口
	 */
	private ISiteviewApi m_api;
	private DefinitionLibrary m_lLibrary;
	private ISecurity iserISecurity;
	
	/*
	 * 构造编辑器
	 */
	public ServiceCatelogEditor(){
	}
	
	/*
	 * 初始化编辑器
	 */
	public void init(IEditorSite site, IEditorInput input)throws PartInitException {
		this.setSite(site);//设置site
		this.setInput(input);//设置输入的IEditorInput对象
		this.setPartName(input.getName());//设置编辑器上方显示的名称
		
		if(input instanceof	ServiceCatelogEditorInput){
			ServiceCatelogEditorInput serviceCatelogEditorInput=(ServiceCatelogEditorInput)input;
			sc=serviceCatelogEditorInput.getSc();
			m_api=serviceCatelogEditorInput.getMApi();
			m_lLibrary=m_api.get_LiveDefinitionLibrary();
			iserISecurity=m_api.get_SecurityService();
		}
	}
	
	/*
	 * 创建编辑器中的控件
	 */
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		ScrolledComposite scomp=new ScrolledComposite(parent,SWT.H_SCROLL|SWT.V_SCROLL);
		Composite content=new Composite(scomp,SWT.NONE);
		scomp.setContent(content);
		scomp.setExpandHorizontal(true); 
		scomp.setExpandVertical(true);
		scomp.setMinWidth(750);
		scomp.setMinHeight(600);
		GridLayout gridLayout=new GridLayout();
		gridLayout.numColumns=6;
		gridLayout.verticalSpacing=40;

		content.setLayout(gridLayout);
		
		for(java.util.Iterator<String> iteror=sc.keySet().iterator();iteror.hasNext();){
			String catelog=iteror.next();
			List<BusinessObjectDef> defList=sc.get(catelog);
			for(final BusinessObjectDef def:defList){
				CLabel lblImageAddr=new CLabel(content,SWT.NONE);
				lblImageAddr.setImage(ImageHelper.getImage(def.get_AssociatedImage(),48,48));
				
				Composite composite = new Composite(content, SWT.NONE);
				
				String alias=def.get_Alias();
				
				if(alias==null||"".equals(alias.trim())){
					alias=def.get_Name();
				}
				
				Link lblAlias = new Link(composite, SWT.NONE);
				lblAlias.setBounds(0, 10,220,15);
				lblAlias.setText(" <a>"+alias+"</a>");
				
				lblAlias.addListener(SWT.Selection,new Listener() {
					public void handleEvent(Event arg0) {
						BusObMaintView.newBusOb(ConnectionBroker.get_SiteviewApi(),def.get_Name());
					}
				});

				CLabel lblDesc = new CLabel(composite, SWT.NONE);
				lblDesc.setBounds(0,25,220,50);
				lblDesc.setText(getProcessString(def.get_Description()));
			}
		}
	}
	
	public String getProcessString(String info){
		StringBuffer strBuffer=new StringBuffer();
		int infoLength=info.length();
		int lineSize=15;
		int lineCol=infoLength%lineSize==0?infoLength/lineSize:(infoLength/lineSize+1);
		int startIndex=0;
		int endIndex=0;
		for(int i=1;i<=lineCol;i++){
			if(i!=lineCol){
				endIndex=lineSize*i;
				strBuffer.append(info.substring(startIndex,endIndex));
				strBuffer.append("\n");
				startIndex=endIndex;
			}
			else{
				strBuffer.append(info.substring(startIndex,infoLength));
			}
		}
		return strBuffer.toString();
	}
	
	/*
	 * 初始化数据
	 */
	public void initData(){

	}

	/*
	 * 编辑器关闭时，保存编辑器内容时所调用的方法
	 */
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * 另存为调用的方法
	 */
	public void doSaveAs() {
	}
	
	/*
	 * 判断是否被修改过
	 */
	public boolean isDirty() {
		return bDirty;
	}
	
	/*
	 * 是否允许保存
	 */
	public boolean isSaveAsAllowed() {
		return bSaveAsAllowed;
	}
	
	/*
	 * 设置焦点
	 */
	public void setFocus() {
	}
	
	/*
	 * 设置属性
	 */
	public void setBDirty(boolean bDirty){
		this.bDirty=bDirty;
	}
	
	public void setBSaveAsAllowed(boolean bSaveAsAllowed){
		this.bSaveAsAllowed=bSaveAsAllowed;
	}
}
