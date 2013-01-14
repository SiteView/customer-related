package core.apploader.tools.pojo;

import java.text.MessageFormat;

import core.apploader.search.comm.CommConvert;

import system.Xml.XmlElement;
import Siteview.SearchCriteria;
import Siteview.SiteviewQuery;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.DefinitionLibrary;
import Siteview.Windows.Forms.ConnectionBroker;

public class RelationExpressPojo {
	private String relationNumber;
	private BusinessObjectDef businessObjectDef;
	private String  current_relationshipDef_name;
	private XmlElement searchXmlElement;
	
	public RelationExpressPojo(String relationNumber,BusinessObjectDef businessObjectDef,String  current_relationshipDef_name,XmlElement searchXmlElement){
		this.relationNumber=relationNumber;
		this.businessObjectDef=businessObjectDef;
		this.current_relationshipDef_name=current_relationshipDef_name;
		this.searchXmlElement=searchXmlElement;
	}
	
	public RelationExpressPojo(XmlElement xmlElement){
		SearchCriteria searchCriteria=new SiteviewQuery().get_CriteriaBuilder();

		String[]     strType=new String[1];
		Integer[]    nOccurrences=new Integer[1];
		String[]     strBusOb=new String[1];
		String[]     strRelationshipName=new String[1];
		String[]     strLinkFieldOrPurpose=new String[1];
		XmlElement[] xeSearchCriteria=new XmlElement[1];
		
		searchCriteria.GetInfoFromOccurrencesExpression(xmlElement, strType, nOccurrences, strBusOb, strRelationshipName, strLinkFieldOrPurpose, xeSearchCriteria);
	
		 DefinitionLibrary m_Library=ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary();
		 
		 businessObjectDef=m_Library.GetBusinessObjectDef(strBusOb[0]);
		 
		 relationNumber=CommConvert.getRelactionExpressNumberChinaDes(strType[0]);
		 
		 current_relationshipDef_name=strRelationshipName[0];
		 
		 searchXmlElement=xeSearchCriteria[0];
	}
	
	public XmlElement getXmlElement(){
		XmlElement occurrencesXmlElement=null;
		SearchCriteria searchCriteria=new SiteviewQuery().get_CriteriaBuilder();
		
		if(relationNumber.equals("至少一个")){
			occurrencesXmlElement=searchCriteria.BusObAtLeastNumOccurrences(1, businessObjectDef.get_Name(),current_relationshipDef_name, "",searchXmlElement);
		}
		else if(relationNumber.equals("全部")){
			occurrencesXmlElement=searchCriteria.BusObAllOccurrences(businessObjectDef.get_Name(), current_relationshipDef_name, "", searchXmlElement);
		}
		else if(relationNumber.equals("链接到")){
			occurrencesXmlElement=searchCriteria.BusObAddLink( businessObjectDef.get_Name(),current_relationshipDef_name,"", searchXmlElement);
		}
		else if(relationNumber.equals("零")){
			occurrencesXmlElement=searchCriteria.BusObNoOccurrences(businessObjectDef.get_Name(),current_relationshipDef_name,"",searchXmlElement);
		}
		return occurrencesXmlElement;
	}
	
	public RelationExpressPojo(SiteviewQuery query,XmlElement xmlElement){
		SearchCriteria searchCriteria=query.get_CriteriaBuilder();

		String[]     strType=new String[1];
		Integer[]    nOccurrences=new Integer[1];
		String[]     strBusOb=new String[1];
		String[]     strRelationshipName=new String[1];
		String[]     strLinkFieldOrPurpose=new String[1];
		XmlElement[] xeSearchCriteria=new XmlElement[1];
		
		searchCriteria.GetInfoFromOccurrencesExpression(xmlElement, strType, nOccurrences, strBusOb, strRelationshipName, strLinkFieldOrPurpose, xeSearchCriteria);
	
		 DefinitionLibrary m_Library=ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary();
		 
		 businessObjectDef=m_Library.GetBusinessObjectDef(strBusOb[0]);
		 
		 relationNumber=CommConvert.getRelactionExpressNumberChinaDes(strType[0]);
		 
		 current_relationshipDef_name=strRelationshipName[0];
		 
		 searchXmlElement=xeSearchCriteria[0];
	}
	
	public XmlElement getXmlElement(SiteviewQuery query){
		XmlElement occurrencesXmlElement=null;
		SearchCriteria searchCriteria=query.get_CriteriaBuilder();
		
		if(relationNumber.equals("至少一个")){
			occurrencesXmlElement=searchCriteria.BusObAtLeastNumOccurrences(1, businessObjectDef.get_Name(),current_relationshipDef_name, "",searchXmlElement);
		}
		else if(relationNumber.equals("全部")){
			occurrencesXmlElement=searchCriteria.BusObAllOccurrences(businessObjectDef.get_Name(), current_relationshipDef_name, "", searchXmlElement);
		}
		else if(relationNumber.equals("链接到")){
			occurrencesXmlElement=searchCriteria.BusObAddLink( businessObjectDef.get_Name(),current_relationshipDef_name,"", searchXmlElement);
		}
		else if(relationNumber.equals("零")){
			occurrencesXmlElement=searchCriteria.BusObNoOccurrences(businessObjectDef.get_Name(),current_relationshipDef_name,"",searchXmlElement);
		}
		
		String[]     strType=new String[1];
		Integer[]    nOccurrences=new Integer[1];
		String[]     strBusOb=new String[1];
		String[]     strRelationshipName=new String[1];
		String[]     strLinkFieldOrPurpose=new String[1];
		XmlElement[] xeSearchCriteria=new XmlElement[1];
		
		searchCriteria.GetInfoFromOccurrencesExpression(occurrencesXmlElement, strType, nOccurrences, strBusOb, strRelationshipName, strLinkFieldOrPurpose, xeSearchCriteria);
		
		return occurrencesXmlElement;
	}
	
	
	
	/*
	 * 获取完整链接信息
	 */
	public String getFinishLinkInfo(){
		return	MessageFormat.format(
                    "<a href=\"relationNumber\">{0}</a> 关联 <a href=\"relationName\">{1}</a> 存在 其中以下 <a href=\"connectOperator\">{2}</a> 应用",
					relationNumber,
					businessObjectDef.get_Alias(),
				    "全部"
		    );
	}
	
	public String getFinishInfo(){
		return	MessageFormat.format(
                    "{0} 关联 {1} 存在 其中以下 {2} 应用",
					relationNumber,
					businessObjectDef.get_Alias(),
				    "全部"
		    );
	}
	
	public void setSearchXmlElement(XmlElement searchXmlElement){
		this.searchXmlElement=searchXmlElement;
	}
	
	public XmlElement getSearchXmlElement(){
		return searchXmlElement;
	}
	
	public String getRelationNumber(){
		return relationNumber;
	}
	
	public BusinessObjectDef getBusinessObjectDef(){
		return businessObjectDef;
	}
	
	public String getCurrent_relationshipDef_name(){
		return current_relationshipDef_name;
	}
}
