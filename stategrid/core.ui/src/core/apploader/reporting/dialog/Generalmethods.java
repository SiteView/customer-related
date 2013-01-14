package core.apploader.reporting.dialog;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Siteview.DisplayInfo;
import Siteview.StringUtils;
import Siteview.TagConversionHelper;
import Siteview.TagItemInfo;
import Siteview.Api.TagContext;

public class Generalmethods {

	private Combo cbType;
	private Combo cbSubtype;
	private List listField ;
	private TagContext context;
	private Button Checkbox_Link;
	private Button Checkbox_Hide;
	
	public Generalmethods(Combo cbType,Combo cbSubtype,List listField,Button Checkbox_Link,Button Checkbox_Hide,TagContext context) {
		
		this.cbType=cbType;
		this.cbSubtype=cbSubtype;
		this.listField=listField;
		this.context=context;
		this.Checkbox_Hide=Checkbox_Hide;
		this.Checkbox_Link=Checkbox_Link;
	}
	
	
	/**
	 * 右边标志选择器类型数据加载
	 */
	public void loadTypedate(){
		cbType.removeAll();
		//类型 数据加载 
		ICollection type=context.GetTagCategories();
		if(type==null){
			return;
		}
		IEnumerator it =type.GetEnumerator();
 		while(it.MoveNext()){
 			DisplayInfo info=(DisplayInfo) it.get_Current();
 			cbType.add(info.get_LocalizedName());
 			cbType.setData(info.get_LocalizedName(), info.get_Name());
 		}
	}
	

	/**
	 * 右边标志选择器子类型数据加载
	 */
	public void loadSubypedate(String cbtype){
		cbSubtype.removeAll();
		listField.removeAll();
		//子类型  数据加载 
 		if(cbtype==null)cbtype="";
 		ICollection subtype=context.GetTagSubcategories(cbtype);
 		if(subtype==null){
 			cbSubtype.setEnabled(false);
 			return;
 		}
 		cbSubtype.setEnabled(true);
 		IEnumerator it1=subtype.GetEnumerator();
 		while(it1.MoveNext()){
 			DisplayInfo info=(DisplayInfo) it1.get_Current();
 			if(!StringUtils.IsEmpty(info.get_LocalizedName())){
 				cbSubtype.add(info.get_LocalizedName());
 	 			cbSubtype.setData(info.get_LocalizedName(), info.get_Name());
 			}
 		}
	}
		
	
	
	/**
	 * 右边标志选择器子类型数据加载
	 */
	public void loadListdate(String cbtype,String cbsubtype){
		// 字段加载 
		listField.removeAll();
		if(cbsubtype==null)cbsubtype="";
		ICollection icsubtype=context.GetTagItems(cbtype, cbsubtype);
		if(icsubtype==null){
			return;
		}
		IEnumerator it2=icsubtype.GetEnumerator();
		while(it2.MoveNext()){
		 	TagItemInfo info=(TagItemInfo) it2.get_Current();
		 	if(!StringUtils.IsEmpty(info.get_Alias())){
		 		listField.add(info.get_Alias());
		 	 	listField.setData(info.get_Alias(), info.get_Name());
		 	}
		}
	}
	
	/**
	 * 是否能编辑显示链接字段 和 显不隐藏字段 
	 */
	public void Showcheckbox_Link_Hide(String str){
		if("Field".equals(str)){
			Checkbox_Link.setEnabled(true);
			Checkbox_Hide.setEnabled(true);
		}else{
			Checkbox_Link.setEnabled(false);
			Checkbox_Hide.setEnabled(false);
		}
	}
	
	/**
	 *  拖动处理事件
	 */
	public void movelister(TreeItem treeitem,Text cbValue,String p_type){
		String svalue= "["+cbType.getText()+": "+cbSubtype.getText()+"."+listField.getItem(listField.getSelectionIndex())+"]";
		cbValue.setText(svalue); 
		//cbValue.setData("")
		String strDisplayText="";
//		ArrayList<Object> list=(ArrayList<Object>) treeitem.getData();
//		if(!StringUtils.IsEmpty(cbSubtype.getText())){
		String strCargo = TagConversionHelper.BuildXmlTag(cbType.getData(cbType.getText()).toString(),cbSubtype.getData(cbSubtype.getText())==null?"":cbSubtype.getData(cbSubtype.getText()).toString(),listField.getData(listField.getItem(listField.getSelectionIndex())).toString());
		System.out.println(strCargo);
		String s = cbType.getText()+": "+cbSubtype.getText()+"."+listField.getItem(listField.getSelectionIndex());
		System.out.println(s);
		strDisplayText="<ExprBldrLink><DispText>#1</DispText><Cargo>#2</Cargo></ExprBldrLink>";
		strDisplayText=strDisplayText.replace("#1", s);
		strDisplayText=strDisplayText.replace("#2", strCargo);
//		}else{
//			strDisplayText=svalue;
//		}
		cbValue.setData(svalue, strDisplayText);
//		list.set(3, list.get(3)+strDisplayText);
		treeitem.setText(svalue);
		treeitem.setData(p_type+"~"+s);
//		if(StringUtils.IsEmpty(treeitem.getText(2))){
//			treeitem.setText(2, "是");
//		}
		
	//	cbValue.select(cbValue.getItemCount());
	}
	public void movelister1(TreeItem treeitem,Text FromValue,Text ToValue,String p_type,int i){
		String svalue = "";				// 文本框显示值
		String treeitem_str = "";		// 树显示值
		String strDisplayText="";
		String strCargo = TagConversionHelper.BuildXmlTag(cbType.getData(cbType.getText()).toString(),cbSubtype.getData(cbSubtype.getText())==null?"":cbSubtype.getData(cbSubtype.getText()).toString(),listField.getData(listField.getItem(listField.getSelectionIndex())).toString());
		System.out.println(strCargo);
		String s = cbType.getText()+": "+cbSubtype.getText()+"."+listField.getItem(listField.getSelectionIndex());
		System.out.println(s);
		strDisplayText="<ExprBldrLink><DispText>#1</DispText><Cargo>#2</Cargo></ExprBldrLink>";
		strDisplayText=strDisplayText.replace("#1", s);
		strDisplayText=strDisplayText.replace("#2", strCargo);
		if(i == 0){
			svalue = "["+cbType.getText()+": "+cbSubtype.getText()+"."+listField.getItem(listField.getSelectionIndex())+"]";
			FromValue.setText(svalue); 
			FromValue.setData(svalue, strDisplayText);
			if(!StringUtils.IsEmpty(ToValue.getText())){
				treeitem_str = "["+svalue + " - "+ ToValue.getText()+"]";
			}else{
				treeitem_str = "["+svalue+" - ]";
			}
		}else{
			svalue= "["+cbType.getText()+": "+cbSubtype.getText()+"."+listField.getItem(listField.getSelectionIndex())+"]";
			ToValue.setText(svalue); 
			ToValue.setData(svalue, strDisplayText);
			if(!StringUtils.IsEmpty(FromValue.getText())){
				System.out.println(FromValue.getText());
				treeitem_str = "["+FromValue.getText()+ " - "+ svalue+"]";
			}else{
				treeitem_str = "[ - "+svalue +"]";
			}
		}
		treeitem.setText(treeitem_str);
		treeitem.setData(p_type+"~"+treeitem_str);
	}
	
	public void movelister2(TreeItem treeitem,Combo cbf,Combo cbt,Text FromValue,Text ToValue,String p_type,int i){
		// 0 From 1 To
		String new_value = "";
		String old_value = treeitem.getText();
		String cbf_str = cbf.getText();
		String cbt_str = cbt.getText();
		String left_mark = "[";
		String right_mark = "]";
		boolean none_flag = false;
		if(i==0){
			if(cbt_str.equalsIgnoreCase("none")){
				none_flag = true;
			}else if(cbt_str.equalsIgnoreCase("Exclusive")){
				left_mark = "[";
				right_mark = ")";
			}else if(cbt_str.equalsIgnoreCase("Inclusive")){
				left_mark = "[";
				right_mark = "]";
			}
			if(cbf_str.equalsIgnoreCase("none")){
				if(none_flag){
					treeitem.dispose();
				}else{
					FromValue.setText("");
					FromValue.setData("");
					new_value = "(未绑定) - " + ToValue.getText() + "]";
				}
			}else if(cbf_str.equalsIgnoreCase("Exclusive")){
				left_mark = "(";
				new_value = left_mark+FromValue.getText() + " - " + ToValue.getText() + right_mark;
			}else if(cbf_str.equalsIgnoreCase("Inclusive")){
				new_value = left_mark+FromValue.getText() + " - " + ToValue.getText() + right_mark;
			}
		}else{
			if(cbf_str.equalsIgnoreCase("none")){
				
			}else if(cbf_str.equalsIgnoreCase("Exclusive")){
				left_mark = "(";
				right_mark = "]";
			}else if(cbf_str.equalsIgnoreCase("Inclusive")){
				left_mark = "[";
				right_mark = "]";
			}
			if(cbt_str.equalsIgnoreCase("none")){
				ToValue.setText("");
				ToValue.setData("");
				new_value = "["+FromValue.getText() + " - (未绑定)";
			}else if(cbt_str.equalsIgnoreCase("Exclusive")){
				right_mark = ")";
				new_value = left_mark+FromValue.getText() + " - " + ToValue.getText() + right_mark;
			}else if(cbt_str.equalsIgnoreCase("Inclusive")){
				new_value = left_mark+FromValue.getText() + " - " + ToValue.getText() + right_mark;
			}
		}
		System.out.println(new_value);
		treeitem.setText(new_value);
	}
}


