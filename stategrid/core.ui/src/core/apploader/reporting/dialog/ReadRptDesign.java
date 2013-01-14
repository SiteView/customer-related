package core.apploader.reporting.dialog;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import system.Collections.ArrayList;

public class ReadRptDesign {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String imputXml = "http://localhost:8080/birt/test.rptdesign";
		read(imputXml);
	}
	
	/**
	 * ���������ļ����� rptdesign
	 */
	public static ArrayList read(String imputXml){
		ArrayList paramet_list = new ArrayList();
		//String imputXml = "http://localhost:8080/birt/test2.rptdesign";
		SAXReader saxReader = new SAXReader();
		try {
			//File inputXml = new File(imputXml);
			Document document = saxReader.read(imputXml);
			//System.out.println(document.asXML());
			Element rootElt = document.getRootElement(); 		// ��ȡ���ڵ�           
			System.out.println("���ڵ�1��" + rootElt.getName()); 	// �õ����ڵ������
			traverseXMLFile(rootElt);
			List nodes = traverseXMLFile(rootElt);
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				//System.out.println("�ڵ㣺" + elm.getName());
				if(elm.getName().equalsIgnoreCase("parameters")){
					List nodes1 = traverseXMLFile(elm);
					for (Iterator it1 = nodes1.iterator(); it1.hasNext();) {
						rptDesign_form af = new rptDesign_form();
						Element elm1 = (Element) it1.next();
						//System.out.println("�����б�");
						if(elm1.getName().equalsIgnoreCase("scalar-parameter")){
							af.setParameter_name(elm1.attributes().get(0).getText());
							//System.out.println("����"+elm1.attributes().get(0).getName()+"\t=\t"+elm1.attributes().get(0).getText());
							//System.out.println("������������:");
							List nodes2 = traverseXMLFile(elm1);
							for (Iterator it2 = nodes2.iterator(); it2.hasNext();) {
								Element elm2 = (Element) it2.next();
								String a = elm2.attributes().get(0).getText();
								String b = elm2.getText();
								af.setValue(a, b);
								//System.out.println(elm2.attributes().get(0).getText()+"\t=\t"+elm2.getText());
							}
						}
						//System.out.println(af.getParameter_name());
						paramet_list.Add(af);
					}
				}
			}  

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return paramet_list;
	}
	
	public static List traverseXMLFile(Element rootElement){
		List<Element> elements = rootElement.elements();
	
		return elements;
	}


}
