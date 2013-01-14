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
	 * Input����
	 */
	private boolean         	bExists=false;//�Ƿ񽫱༭��������������ʼ�¼��
	private ImageDescriptor 	imageIcon;//�������ݵ�ͼ��
	private String          	name="���ŷ���";//������Ϣ������
	private IPersistableElement iPersistableElement;//�Ƿ���Գ־û��ñ༭��
	private String              toolTipText="";//���ñ༭����ǩ����ʾ��ʾ��Ϣ
	
	/*
	 * ���ݲ���
	 */
	private ISiteviewApi m_api;
	private Map<String,List<BusinessObjectDef>> sc;
	
	/*
	 * ����Input
	 */
	public ServiceCatelogEditorInput(Map<String,List<BusinessObjectDef>> sc,ISiteviewApi m_api){
		this.m_api=m_api;
		this.sc=sc;
	}
	
	public ServiceCatelogEditorInput(){
		
	}

	/*
	 * �������������ص���Ķ���
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	/*
	 * �Ƿ񽫱༭��������������ʼ�¼��
	 */
	public boolean exists() {
		return bExists;
	}
	
	/*
	 * �������ݵ�ͼ��
	 */
	public ImageDescriptor getImageDescriptor() {
		return imageIcon;
	}
	
	/*
	 * ������Ϣ������
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * �Ƿ���Գ־û��ñ༭��
	 */
	public IPersistableElement getPersistable() {
		return iPersistableElement;
	}
	
	/*
	 * ���ñ༭����ǩ����ʾ��ʾ��Ϣ
	 */
	public String getToolTipText() {
		return toolTipText;
	}
	
	/*
	 * ��������
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
