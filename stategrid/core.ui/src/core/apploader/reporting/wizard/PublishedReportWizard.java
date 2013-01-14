package core.apploader.reporting.wizard;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TreeItem;

import system.Collections.ICollection;
import Siteview.CrystalSettingsManager;
import Siteview.IDefinition;
import Siteview.SiteviewException;
import Siteview.Api.ISiteviewApi;
import Siteview.Xml.Scope;

public class PublishedReportWizard extends Wizard {
	
	private ISiteviewApi m_Api;
	public static int type = 0;		// 0 ����,1��ȡ,2ɾ��
    private ArrayList m_astrReports;
    private boolean m_bOverwrite;
    private Hashtable m_htReportToDescription;
    private String m_strCategory;
    private static String m_strDefClassName = "PublishedReportDef";
    private String m_strPath;
    private String m_strPathOnServer;
    private String m_strUrlOnServer;
    private String m_SummaryPageHeading;
    private String m_SummaryPageMessage;
    private String m_Extension;
    
    public PublishedReportWizard() {
    	setWindowTitle("���淢����");
    }
    
	public PublishedReportWizard(ISiteviewApi api) {
		this();
		this.m_Api = api;
		this.m_strPath = "";
		this.m_astrReports = new ArrayList();
        this.m_bOverwrite = true;
        this.m_strCategory = "";
        this.m_strPathOnServer = "";
        this.m_SummaryPageHeading = "";
        this.m_SummaryPageMessage = "";
        this.m_Extension = ".rpt";
        
        CrystalSettingsManager crystalSettingsManager = api.get_ReportLibrary().get_CrystalSettingsManager();
        crystalSettingsManager.set_ReadWriteServerInfo(true);
        crystalSettingsManager.set_ReadWriteLoginInfo(false);
        crystalSettingsManager.ReadSettings();
        this.m_strPathOnServer = crystalSettingsManager.get_Folder();
        this.m_strUrlOnServer = "http://localhost:8080/birt/";
	}

	@Override
	public void addPages() {
		this.addPage(new PublishedReportW1_Opt(this));	// ����ѡ��
		this.addPage(new PublishedReportW2_Pub(this));	// ����λ��
		// this.addPage(new PublishedReportW3_Pub());	// Ҫ�����ı����б�
		// this.addPage(new PublishedReportW2_Ret());	// ����λ��
		// this.addPage(new PublishedReportW3_Ret());	// Ҫ�����ı����б�
		// this.addPage(new PublishedReportW2_Del());	// Ҫɾ���ı����б�
	}

	@Override
	public IWizardPage getStartingPage() {
		return this.getPage("PublishedReportW1_Opt");
	}
	
	@Override
	public boolean performFinish() {
		PublishedReportW4_Fin finishPage = (PublishedReportW4_Fin)this.getPage("PublishedReportW4_Fin");
		// ִ�б�����Щ����
		return true;
	}
	
	@Override
	public boolean canFinish() {
		if (this.getContainer().getCurrentPage() == this.getPage("PublishedReportW4_Fin")){
			return true;
		}else{
			return false;
		}
	}
	
	// ɾ������ҳ �õ��Ѿ����ڱ��漯��
	public ICollection GetPlaceHolders(){
		ICollection is2 = null;
		try{
			is2 = this.m_Api.get_ApplicationDefinitionRepository().GetDefinitionPlaceHolderList(Scope.Global, "GLOBAL", "", "PublishedReportDef");
		}catch (SiteviewException e){
			//
		}
		return is2;
	}
	public IDefinition GetDefinition(String strName){
		IDefinition definition = null;
		try{
			definition = this.m_Api.get_ApplicationDefinitionRepository().GetDefinition(Scope.Global, "GLOBAL", "", "PublishedReportDef", strName, false);
		}catch (SiteviewException e){
			//
		}
		return definition;
	}
	public TreeItem ContainsFolder(TreeItem[] aItems, String strName){
		for(int i = 0;i < aItems.length;i++){
			TreeItem item2 = (TreeItem) aItems[i];
			if (item2.getText().trim().equalsIgnoreCase(strName.trim())){
                return item2;
            }
		}
        return null;
    }
	// �������� ������Def
	public boolean AddNewDefinition(IDefinition def){
		boolean flag = false;
		if (def != null){
			try{
				flag = this.m_Api.get_ApplicationDefinitionRepository().AddNewDefinition(def);
			}catch (SiteviewException e){
				//
			}
		}
		return flag;
	}

	// �������� ������Def
	public IDefinition CreateDefinitionForEditing(){
		IDefinition definition = null;
		try{
			definition = this.m_Api.get_ApplicationDefinitionRepository().CreateDefinitionForEditing(Scope.Global, "GLOBAL", "PublishedReportDef");
		}catch (SiteviewException e){
			//
		}
		return definition;
	}

	// ɾ������ ɾ��Def
	public boolean DeleteDefinition(IDefinition def){
		boolean flag = false;
		if (def != null){
			try{
				flag = this.m_Api.get_ApplicationDefinitionRepository().DeleteDefinition(def);
			}catch (SiteviewException e){
				//
			}
		}
		return flag;
	}

	// �������¼�����
	public void checkItem(TreeItem item,List<Object> authorities){
		checkItem(item,authorities,false,false);
	}
	private void checkItem(TreeItem item, List<Object> allCheckedElements,boolean isParent,boolean isChild){
		//�˴����Ѿ�������ɣ��������ж��Ǹ��ڵ㻹���ӽڵ���ֱ�Ӹ�����������ṹ�жϣ�������Ҫ�ٿ����߼�
		if(!isChild){
			if(item.getItemCount() != 0)
				isParent = true;			
		}
		
		if(item.getParentItem() != null){
			isChild = true;
		} else {
			isChild = false;
		}
		if (isParent) { // �Ǹ��ڵ�ʱӦ��ѡ�и�ѡ�иýڵ�ʱ����ѡ�����µ������ӽڵ㣬ȡ��ѡ��ʱͬ��
			TreeItem[] children = item.getItems();
			Object o = item.getData();
			if (item.getChecked()) { //ѡ�иø��ڵ�ʱ�������е��ӽڵ�ҲҪ��ѡ��
				if(!allCheckedElements.contains(o))
					allCheckedElements.add(o);// ��ѡ�еĽڵ���뵽allCheckedElements��
				for (int i = 0; i < children.length; i++) {
					children[i].setChecked(true);
					checkItem(children[i],allCheckedElements);
				}
			} else {// ȡ��ѡ��ʱ���������ӽڵ�Ҳͬʱ��ȡ��ѡ��
				if(allCheckedElements.contains(o))
					allCheckedElements.remove(o);//���ѡ�еĽڵ�֮ǰ��allCheckedElements�У����Ƴ�
				for (int i = 0; i < children.length; i++) {
					children[i].setChecked(false);
					checkItem(children[i],allCheckedElements);
				}
			}
		} 
		if (isChild) {// ���Ǹ��ڵ�
			TreeItem[] brotheres = item.getParentItem().getItems();// ����������е��ֵܽڵ�
			Object o = item.getData();
			if (item.getChecked()) {
				if(!allCheckedElements.contains(o))
					allCheckedElements.add(o);// ��ѡ�еĽڵ���뵽allCheckedElements��
			} else {
				if(allCheckedElements.contains(o))//���֮ǰallCheckedElements��û�а����ýڵ������
					allCheckedElements.remove(o);
			}
			boolean hasBrotherChecked = false;
			for (TreeItem brother : brotheres) {// �ж������ֵܽڵ�����û�б�ѡ�е�
				if (brother.getChecked()) {
					hasBrotherChecked = true;
					break;
				}				
			}
			
			if(hasBrotherChecked){//����ֵܽڵ����б�ѡ�е���ô���ڵ�Ҳ��ѡ��
				item.getParentItem().setChecked(true);
				Object po = item.getParentItem().getData();
				if(!allCheckedElements.contains(po))
					allCheckedElements.add(po);
				checkItem(item.getParentItem(),allCheckedElements,false,true);
			} else {//��ô����ֵܽڵ���һ����û�б�ѡ�еģ���ô���ڵ�Ҳ��Ӧ��ѡ��
				item.getParentItem().setChecked(false);
				Object po = item.getParentItem().getData();
				if(allCheckedElements.contains(po))
					allCheckedElements.remove(po);
				checkItem(item.getParentItem(),allCheckedElements,false,true);
			}			
		}
	}
	
	// �ӷ�������ɾ���ļ� ����д
	public boolean DeleteFileFromServer(String strReportName, String strErrorMsg){
        boolean flag = false;
        strErrorMsg = "";
        try{
            this.m_Api.get_ReportLibrary().Delete(this.m_strPathOnServer, strReportName);
            flag = true;
        }catch (Exception exception){
            strErrorMsg = exception.getMessage();
        }
        return flag;
    }
	
	// Get Set����----------------------
	public ISiteviewApi getM_Api() {
		return m_Api;
	}

	public void setM_Api(ISiteviewApi m_Api) {
		this.m_Api = m_Api;
	}

	public static String getM_strDefClassName() {
		return m_strDefClassName;
	}

	public static void setM_strDefClassName(String m_strDefClassName) {
		PublishedReportWizard.m_strDefClassName = m_strDefClassName;
	}

	public String getM_strPath() {
		return m_strPath;
	}

	public void setM_strPath(String m_strPath) {
		this.m_strPath = m_strPath;
	}

	public ArrayList get_Reports() {
		return m_astrReports;
	}

	public void set_Reports(ArrayList m_astrReports) {
		this.m_astrReports = m_astrReports;
	}

	public boolean isM_bOverwrite() {
		return m_bOverwrite;
	}

	public void setM_bOverwrite(boolean m_bOverwrite) {
		this.m_bOverwrite = m_bOverwrite;
	}

	public Hashtable getM_htReportToDescription() {
		return m_htReportToDescription;
	}

	public void setM_htReportToDescription(Hashtable m_htReportToDescription) {
		this.m_htReportToDescription = m_htReportToDescription;
	}

	public String getM_strCategory() {
		return m_strCategory;
	}

	public void setM_strCategory(String m_strCategory) {
		this.m_strCategory = m_strCategory;
	}

	public String getM_strPathOnServer() {
		return m_strPathOnServer;
	}

	public void setM_strPathOnServer(String m_strPathOnServer) {
		this.m_strPathOnServer = m_strPathOnServer;
	}

	public String getM_strUrlOnServer() {
		return m_strUrlOnServer;
	}

	public void setM_strUrlOnServer(String m_strUrlOnServer) {
		this.m_strUrlOnServer = m_strUrlOnServer;
	}

	public String getM_SummaryPageHeading() {
		return m_SummaryPageHeading;
	}

	public void setM_SummaryPageHeading(String m_SummaryPageHeading) {
		this.m_SummaryPageHeading = m_SummaryPageHeading;
	}

	public String getM_SummaryPageMessage() {
		return m_SummaryPageMessage;
	}

	public void setM_SummaryPageMessage(String m_SummaryPageMessage) {
		this.m_SummaryPageMessage = m_SummaryPageMessage;
	}

	public String getM_Extension() {
		return m_Extension;
	}

	public void setM_Extension(String m_Extension) {
		this.m_Extension = m_Extension;
	}
}
