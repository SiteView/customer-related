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
	public static int type = 0;		// 0 发布,1获取,2删除
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
    	setWindowTitle("报告发布向导");
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
		this.addPage(new PublishedReportW1_Opt(this));	// 发布选项
		this.addPage(new PublishedReportW2_Pub(this));	// 报告位置
		// this.addPage(new PublishedReportW3_Pub());	// 要发布的报告列表
		// this.addPage(new PublishedReportW2_Ret());	// 报告位置
		// this.addPage(new PublishedReportW3_Ret());	// 要检索的报告列表
		// this.addPage(new PublishedReportW2_Del());	// 要删除的报告列表
	}

	@Override
	public IWizardPage getStartingPage() {
		return this.getPage("PublishedReportW1_Opt");
	}
	
	@Override
	public boolean performFinish() {
		PublishedReportW4_Fin finishPage = (PublishedReportW4_Fin)this.getPage("PublishedReportW4_Fin");
		// 执行保存这些动作
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
	
	// 删除报告页 得到已经存在报告集合
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
	// 发布报告 增加新Def
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

	// 发布报告 创建新Def
	public IDefinition CreateDefinitionForEditing(){
		IDefinition definition = null;
		try{
			definition = this.m_Api.get_ApplicationDefinitionRepository().CreateDefinitionForEditing(Scope.Global, "GLOBAL", "PublishedReportDef");
		}catch (SiteviewException e){
			//
		}
		return definition;
	}

	// 删除报告 删除Def
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

	// 树单击事件处理
	public void checkItem(TreeItem item,List<Object> authorities){
		checkItem(item,authorities,false,false);
	}
	private void checkItem(TreeItem item, List<Object> allCheckedElements,boolean isParent,boolean isChild){
		//此处树已经构建完成，所以在判断是父节点还是子节点是直接根据树的物理结构判断，而不需要再考虑逻辑
		if(!isChild){
			if(item.getItemCount() != 0)
				isParent = true;			
		}
		
		if(item.getParentItem() != null){
			isChild = true;
		} else {
			isChild = false;
		}
		if (isParent) { // 是父节点时应该选中该选中该节点时级联选中其下的所有子节点，取消选中时同理
			TreeItem[] children = item.getItems();
			Object o = item.getData();
			if (item.getChecked()) { //选中该父节点时，它所有的子节点也要被选中
				if(!allCheckedElements.contains(o))
					allCheckedElements.add(o);// 将选中的节点放入到allCheckedElements中
				for (int i = 0; i < children.length; i++) {
					children[i].setChecked(true);
					checkItem(children[i],allCheckedElements);
				}
			} else {// 取消选中时它的所有子节点也同时被取消选中
				if(allCheckedElements.contains(o))
					allCheckedElements.remove(o);//如果选中的节点之前在allCheckedElements中，则移除
				for (int i = 0; i < children.length; i++) {
					children[i].setChecked(false);
					checkItem(children[i],allCheckedElements);
				}
			}
		} 
		if (isChild) {// 不是父节点
			TreeItem[] brotheres = item.getParentItem().getItems();// 获得它的所有的兄弟节点
			Object o = item.getData();
			if (item.getChecked()) {
				if(!allCheckedElements.contains(o))
					allCheckedElements.add(o);// 将选中的节点加入到allCheckedElements中
			} else {
				if(allCheckedElements.contains(o))//如果之前allCheckedElements中没有包含该节点则加入
					allCheckedElements.remove(o);
			}
			boolean hasBrotherChecked = false;
			for (TreeItem brother : brotheres) {// 判断它的兄弟节点中有没有被选中的
				if (brother.getChecked()) {
					hasBrotherChecked = true;
					break;
				}				
			}
			
			if(hasBrotherChecked){//如果兄弟节点中有被选中的那么父节点也被选中
				item.getParentItem().setChecked(true);
				Object po = item.getParentItem().getData();
				if(!allCheckedElements.contains(po))
					allCheckedElements.add(po);
				checkItem(item.getParentItem(),allCheckedElements,false,true);
			} else {//相么如果兄弟节点中一个都没有被选中的，那么父节点也不应被选中
				item.getParentItem().setChecked(false);
				Object po = item.getParentItem().getData();
				if(allCheckedElements.contains(po))
					allCheckedElements.remove(po);
				checkItem(item.getParentItem(),allCheckedElements,false,true);
			}			
		}
	}
	
	// 从服务器上删除文件 需重写
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
	
	// Get Set方法----------------------
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
