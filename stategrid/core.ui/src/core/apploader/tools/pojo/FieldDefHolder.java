package core.apploader.tools.pojo;

import Siteview.Api.FieldDef;
import Siteview.Api.RelationshipDef;

public class FieldDefHolder{
	
	private FieldDef 		fieldDef;
	private String   		headName;
	private String[] 		directName;
	private String   		up_fieldDef_alias="";
	private int      		level;
	private int      		fieldDefType;
	private RelationshipDef RelDef;
	
	public FieldDefHolder(){
		
	}
	
	public FieldDefHolder(FieldDef fieldDef,String headName,String[] directName,String up_fieldDef_alias,int level,RelationshipDef RelDef){
		this.fieldDef=fieldDef;
		this.headName=headName;
		this.directName=directName;
		this.up_fieldDef_alias=up_fieldDef_alias;
		this.level=level;
		this.RelDef=RelDef;
	}
	
	public FieldDef getFieldDef(){
		return fieldDef;
	}
	
	public String   getHeadName(){
		return headName;
	}
	
	public String[] getDirectName(){
		return directName;
	}
	
	public String getUp_fieldDef_alias(){
		return up_fieldDef_alias;
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getFieldDefType(){
		return fieldDef!=null?fieldDef.get_IsNumber()?3:(fieldDef.get_IsDateTime()?4:(fieldDef.get_IsLogical()?5:2)):fieldDefType;
	}

	public RelationshipDef getRelDef() {
		return RelDef;
	}

	public void setFieldDef(FieldDef fieldDef) {
		this.fieldDef = fieldDef;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public void setDirectName(String[] directName) {
		this.directName = directName;
	}

	public void setUp_fieldDef_alias(String up_fieldDef_alias) {
		this.up_fieldDef_alias = up_fieldDef_alias;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setFieldDefType(int fieldDefType) {
		
		this.fieldDefType = fieldDef!=null?getFieldDefType():0;
	}

	public void setRelDef(RelationshipDef relDef) {
		RelDef = relDef;
	}
	
}