package core.apploader.editors;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import Siteview.QueryGroupDef;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.GridDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.VirtualKeyList;

public class ServiceCatelogEditorInput implements IEditorInput {
	/*
	 * Input变量
	 */
	private boolean         	bExists=false;//是否将编辑器保存在最近访问记录中
	private ImageDescriptor 	imageIcon;//输入内容的图标
	private String          	name="热门服务";//输入信息的名称
	private IPersistableElement iPersistableElement;//是否可以持久化该编辑器
	private String              toolTipText="";//设置编辑器标签中显示提示信息
	
	/*
	 * 传递参数
	 */
	private ISiteviewApi m_api;
	private Map<String,List<BusinessObjectDef>> sc;
	
	/*
	 * 构造Input
	 */
	public ServiceCatelogEditorInput(Map<String,List<BusinessObjectDef>> sc,ISiteviewApi m_api){
		this.m_api=m_api;
		this.sc=sc;
	}
	
	public ServiceCatelogEditorInput(){
		
	}

	/*
	 * 返回与该输入相关的类的对象
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	/*
	 * 是否将编辑器保存在最近访问记录中
	 */
	public boolean exists() {
		return bExists;
	}
	
	/*
	 * 输入内容的图标
	 */
	public ImageDescriptor getImageDescriptor() {
		return imageIcon;
	}
	
	/*
	 * 输入信息的名称
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * 是否可以持久化该编辑器
	 */
	public IPersistableElement getPersistable() {
		return iPersistableElement;
	}
	
	/*
	 * 设置编辑器标签中显示提示信息
	 */
	public String getToolTipText() {
		return toolTipText;
	}
	
	/*
	 * 设置属性
	 */
	public void setbExists(boolean bExists) {
		this.bExists = bExists;
	}

	public void setImageIcon(ImageDescriptor imageIcon) {
		this.imageIcon = imageIcon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setiPersistableElement(IPersistableElement iPersistableElement) {
		this.iPersistableElement = iPersistableElement;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	
	public ISiteviewApi getMApi(){
		return m_api;
	}
	
	public Map<String,List<BusinessObjectDef>> getSc(){
		return sc;
	}
	
	public boolean equals(Object obj) {
		if(null == obj) {
			return false;
		}
		if(!(obj instanceof ServiceCatelogEditorInput)) {
			return false;
		}
		if(!getName().equals(((ServiceCatelogEditorInput)obj).getName())){
			return false;
		}	
		
		return true;
	}
}
