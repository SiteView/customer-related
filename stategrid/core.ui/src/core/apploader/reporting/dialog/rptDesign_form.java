/**
 * 
 */
package core.apploader.reporting.dialog;

/**
 * @author Administrator
 *
 */
public class rptDesign_form{
	private String Parameter_name;
	private String scalar_parameter_name;	// ������
	private String text_property_name;		// ������ʾ��
	private String promptText;				// ��ʾ�ַ�
	private String valueType;				// �������� static
	private String dataType;				// �������� String
	private String helpText;				// ��������
	private String distinct;				// 
	private boolean isRequired;				// �Ƿ�Ϊ����
	private boolean hidden;					// �Ƿ�����
	private String controlType;				// ��ʾ����
	private String paramType;				// ��������
	private String format;					// ��ʽ��
	public String getParameter_name() {
		return Parameter_name;
	}
	public void setParameter_name(String parameter_name) {
		Parameter_name = parameter_name;
	}
	public String getScalar_parameter_name() {
		return scalar_parameter_name;
	}
	public void setScalar_parameter_name(String scalar_parameter_name) {
		this.scalar_parameter_name = scalar_parameter_name;
	}
	public String getText_property_name() {
		return text_property_name;
	}
	public void setText_property_name(String text_property_name) {
		this.text_property_name = text_property_name;
	}
	public String getPromptText() {
		return promptText;
	}
	public void setPromptText(String promptText) {
		this.promptText = promptText;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public String getDistinct() {
		return distinct;
	}
	public void setDistinct(String distinct) {
		this.distinct = distinct;
	}
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	public boolean isHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setValue(String a,String b){
		if(a.equalsIgnoreCase("scalar-parameter")){
			this.setScalar_parameter_name(b);
		}else if(a.equalsIgnoreCase("promptText")){
			this.setPromptText(b);
		}else if(a.equalsIgnoreCase("valueType")){
			this.setValueType(b);
		}else if(a.equalsIgnoreCase("isRequired")){
			if(b.equalsIgnoreCase("true")){
				this.setRequired(true);
			}else{
				this.setRequired(false);
			}
		}else if(a.equalsIgnoreCase("dataType")){
			
		}else if(a.equalsIgnoreCase("distinct")){
			
		}else if(a.equalsIgnoreCase("paramType")){
			this.setParamType(b);
		}else if(a.equalsIgnoreCase("controlType")){
			
		}else if(a.equalsIgnoreCase("format")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}else if(a.equalsIgnoreCase("")){
			
		}
		
	}
	
//	<parameters>
//	    <scalar-parameter name="test" id="176">
//	        <property name="hidden">false</property>
//	        <text-property name="helpText">11</text-property>
//	        <text-property name="promptText">�������ѯ����:</text-property>
//	        <property name="valueType">static</property>
//	        <property name="isRequired">true</property>
//	        <property name="dataType">string</property>
//	        <property name="distinct">true</property>
//	        <list-property name="selectionList">
//	            <structure>
//	                <property name="value">admin</property>
//	            </structure>
//	            <structure>
//	                <property name="value">zf</property>
//	            </structure>
//	        </list-property>
//	        <property name="paramType">simple</property>
//	        <property name="controlType">list-box</property>
//	        <property name="mustMatch">true</property>
//	        <property name="fixedOrder">true</property>
//	        <structure name="format">
//	            <property name="category">Unformatted</property>
//	        </structure>
//	    </scalar-parameter>
//	</parameters>
	
	
}
