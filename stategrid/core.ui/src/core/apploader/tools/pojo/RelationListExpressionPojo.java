package core.apploader.tools.pojo;

import system.Xml.XmlElement;
import core.apploader.search.comm.CommEnum.LineType;

public class RelationListExpressionPojo {
	private XmlElement xmlElement;
	private String     linkInfo;
	private LineType   lineType;
	
	public RelationListExpressionPojo(XmlElement xmlElement,String linkInfo,LineType lineType){
		this.xmlElement=xmlElement;
		this.linkInfo=linkInfo;
		this.lineType=lineType;
	}
	
	public XmlElement getXmlElement(){
		return xmlElement;
	}
	
	public LineType getLineType(){
		return lineType;
	}
	
	public String getLinkInfo(){
		return linkInfo;
	}
}
