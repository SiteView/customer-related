package core.apploader.tools.pojo;

import java.text.MessageFormat;

import system.Type;
import system.Xml.XmlElement;

import Siteview.Operators;
import Siteview.SiteviewQuery;
import Siteview.Api.RelationshipDef;
import Siteview.Xml.FunctionCategory;
import Siteview.Xml.LocalizeHelper;
import Siteview.Xml.ValueSources;

public class SimpleExpressPojo {
	/*
	 * 开始 信息 中 fieldDef 的 QualifiedField 内容
	 */
	private String strQualifiedField_start;
	
	/*
	 * 最终数值，当数值类型为字段时，作为 尾 信息 中 fieldDef 的 QualifiedField 内容
	 */
	private String strValue;
	
	/*
	 * 最终数值，当数值类型为字段时，作为 尾 信息 中 fieldDef 的 QualifiedField 内容(List类型时用)
	 */
	private String strValueCompara;
	
	/*
	 * 操作符
	 */
	private Operators    eOperator;
	
	/*
	 * 数值类型
	 */
	private ValueSources eValueType;
	
	/*
	 * 数值类型(List类型时用)
	 */
	private ValueSources eValueTypeCompara;
	
	/*
	 * 头字段信息
	 */
	private String strFieldAlias_start;
	
	/*
	 * 头关系名称
	 */
	private RelationshipDef relationShip_start;
	
	/*
	 * 头字段链接信息
	 */
	private String[] undecoratedPurposeLink_start;
	
	/*
	 * 尾字段名称信息
	 */
	private String strFieldAlias_end;
	
	/*
	 * 尾关系名称
	 */
	private RelationshipDef relationShip_end;
	
	/*
	 * 尾字段链接信息
	 */
	private String[] undecoratedPurposeLink_end;
	
	/*
	 * 尾字段名称信息(List类型时用)
	 */
	private String strFieldAlias_endCompara;
	
	/*
	 * 尾关系名称(List类型时用)
	 */
	private RelationshipDef relationShip_endCompara;
	
	/*
	 * 尾字段链接信息(List类型时用)
	 */
	private String[] undecoratedPurposeLink_endCompara;
	
	/*
	 * 标识符信息
	 */
	private String modifer;
	
	/*
	 * 初始化数据
	 */
	public SimpleExpressPojo(String strQualifiedField_start,String strFieldAlias_start,RelationshipDef relationShip_start,String[] undecoratedPurposeLink_start,Operators eOperator,ValueSources eValueType,String strValue,String strFieldAlias_end,RelationshipDef relationShip_end,String[] undecoratedPurposeLink_end,String modifer){
			this.strQualifiedField_start=strQualifiedField_start;
			this.strFieldAlias_start=strFieldAlias_start;
			this.relationShip_start=relationShip_start;
			this.undecoratedPurposeLink_start=undecoratedPurposeLink_start;
			this.eOperator=eOperator;
			this.eValueType=eValueType;
			this.strValue=strValue;
			this.strFieldAlias_end=strFieldAlias_end;
			this.relationShip_end=relationShip_end;
			this.undecoratedPurposeLink_end=undecoratedPurposeLink_end;
			this.modifer=modifer;
	}
	
	/*
	 * 初始化带比较的数据
	 */
	public SimpleExpressPojo(String strQualifiedField_start,String strFieldAlias_start,RelationshipDef relationShip_start,String[] undecoratedPurposeLink_start,Operators eOperator,ValueSources eValueType,String strValue,ValueSources eValueTypeCompara,String strValueCompara,String strFieldAlias_end,RelationshipDef relationShip_end,String[] undecoratedPurposeLink_end,String strFieldAlias_endCompara,RelationshipDef relationShip_endCompara,String[] undecoratedPurposeLink_endCompara,String modifer){
			this.strQualifiedField_start=strQualifiedField_start;
			this.strFieldAlias_start=strFieldAlias_start;
			this.relationShip_start=relationShip_start;
			this.undecoratedPurposeLink_start=undecoratedPurposeLink_start;
			this.eOperator=eOperator;
			this.eValueType=eValueType;
			this.eValueTypeCompara=eValueTypeCompara;
			this.strValue=strValue;
			this.strValueCompara=strValueCompara;
			this.strFieldAlias_end=strFieldAlias_end;
			this.relationShip_end=relationShip_end;
			this.undecoratedPurposeLink_end=undecoratedPurposeLink_end;
			this.strFieldAlias_endCompara=strFieldAlias_endCompara;
			this.relationShip_endCompara=relationShip_endCompara;
			this.undecoratedPurposeLink_endCompara=undecoratedPurposeLink_endCompara;
			this.modifer=modifer;
	}
	
	/*
	 * 头信息加工成链接字符串
	 */
	public String getStartInfoToLinKString(){
		StringBuffer startDispText=new StringBuffer();
		if(undecoratedPurposeLink_start[0]!=null&&undecoratedPurposeLink_start[0].length()!=0){
			if(relationShip_start!=null){
				startDispText.append(
					modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a>",strFieldAlias_start.substring(0,strFieldAlias_start.indexOf("(")),relationShip_start.get_Alias(),undecoratedPurposeLink_start[0])
	                        			 :
	                        			 MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a> <a href=\"modifer\">({3})</a>",strFieldAlias_start,relationShip_start.get_Alias(),undecoratedPurposeLink_start[0],modifer)
				);
			}
			else{
				startDispText.append(
					modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a>",strFieldAlias_start,undecoratedPurposeLink_start[0])
                                		:
                                        MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a> <a href=\"modifer\">({2})</a>",strFieldAlias_start,undecoratedPurposeLink_start[0],modifer)	
				);		
			}
		}
		else{
			startDispText.append(
					modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a>",strFieldAlias_start)
                    :
                    MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"modifer\">({1})</a>",strFieldAlias_start,modifer)
			);
		}
		return startDispText.toString();
	}
	
	/*
	 * 头信息加工成字符串
	 */
	public String getStartInfoToString(){
		StringBuffer startDispText=new StringBuffer();
		if(undecoratedPurposeLink_start[0]!=null&&undecoratedPurposeLink_start[0].length()!=0){
			if(relationShip_start!=null){
				startDispText.append(
					modifer.length()==0?MessageFormat.format("{0} ({1}) ({2})",strFieldAlias_start.substring(0,strFieldAlias_start.indexOf("(")),relationShip_start.get_Alias(),undecoratedPurposeLink_start[0])
	                        			 :
	                        			 MessageFormat.format("{0} ({1}) ({2}) ({3})",strFieldAlias_start,relationShip_start.get_Alias(),undecoratedPurposeLink_start[0],modifer)
				);
			}
			else{
				startDispText.append(
					modifer.length()==0?MessageFormat.format("{0} ({1})",strFieldAlias_start,undecoratedPurposeLink_start[0])
                                		:
                                        MessageFormat.format("{0} ({1}) ({2})",strFieldAlias_start,undecoratedPurposeLink_start[0],modifer)	
				);		
			}
		}
		else{
			startDispText.append(
					modifer.length()==0?MessageFormat.format("{0}",strFieldAlias_start)
                    :
                    MessageFormat.format("{0} ({1})",strFieldAlias_start,modifer)
			);
		}
		return startDispText.toString();
	}
	
	/*
	 * 尾信息加工成字符串链接
	 */
	public String getDetailInfoToLinKString(){
		StringBuffer detailDispText=new StringBuffer();
		if(eOperator==Operators.Null||eOperator==Operators.NotNull||
		   eOperator==Operators.Empty||eOperator==Operators.NotEmpty){
			detailDispText.append("");	
		}
		else{
			if(eValueType==ValueSources.Literal||eValueType==ValueSources.SearchParameter||
			   eValueType==ValueSources.ValidatedSearchParameter||eValueType==ValueSources.Rule||
			   eValueType==ValueSources.RecId||eValueType==ValueSources.ParentRecId||
			   eValueType==ValueSources.Prompt||eValueType==ValueSources.ValidatedPrompt||
			   eValueType==ValueSources.Token){
				
			   detailDispText.append(MessageFormat.format("<a href=\"fieldname\">{0}</a>","'"+strValue+"'"));
			}
			else if(eValueType==ValueSources.Function){
				String strB = LocalizeHelper.GetValue(Type.GetType(FunctionCategory.class.getName()), strValue);
				detailDispText.append(MessageFormat.format("<a href=\"\">{0}</a>",strB.length()!=0?strB :strValue));
			}
			else if(eValueType==ValueSources.Field){
				if(undecoratedPurposeLink_end[0]!=null&&undecoratedPurposeLink_end[0].length()!=0){
					if(relationShip_end!=null){
						detailDispText.append(
							modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a>",eValueType==ValueSources.Field?strFieldAlias_end:strValue,relationShip_end.get_Alias(),undecoratedPurposeLink_end[0])
			                        			 :
			                        			 MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a> <a href=\"modifer\">({3})</a>",strFieldAlias_end,relationShip_end.get_Alias(),undecoratedPurposeLink_end[0],modifer)
						);
					}
					else{
						detailDispText.append(
							modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a>",strFieldAlias_end,undecoratedPurposeLink_end)
		                                		:
		                                        MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a> <a href=\"modifer\">({2})</a>",strFieldAlias_end,undecoratedPurposeLink_end,modifer)	
						);		
					}
				}
				else{
					detailDispText.append(
							modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a>",strFieldAlias_end)
												:
												MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"modifer\">({1})</a>",strFieldAlias_end,modifer)
					);
				}
			}
			if(eOperator==Operators.Between||eOperator==Operators.NotBetween){
				detailDispText.append(" 和 ");
				if(eValueTypeCompara==ValueSources.Literal||eValueTypeCompara==ValueSources.SearchParameter||
				   eValueTypeCompara==ValueSources.ValidatedSearchParameter||eValueTypeCompara==ValueSources.Rule||
				   eValueTypeCompara==ValueSources.RecId||eValueTypeCompara==ValueSources.ParentRecId||
				   eValueTypeCompara==ValueSources.Prompt||eValueTypeCompara==ValueSources.ValidatedPrompt||
				   eValueTypeCompara==ValueSources.Token){
						   detailDispText.append(MessageFormat.format("<a href=\"fieldname\">{0}</a>",strValueCompara));
				   }
				   else if(eValueTypeCompara==ValueSources.Function){
					    String strB = LocalizeHelper.GetValue(Type.GetType(FunctionCategory.class.getName()), strValueCompara);
					    detailDispText.append(MessageFormat.format("<a href=\"\">{0}</a>",strB.length()!=0?strB :strValueCompara));
				   }
				   else if(eValueTypeCompara==ValueSources.Field){
						if(undecoratedPurposeLink_endCompara[0]!=null&&undecoratedPurposeLink_endCompara[0].length()!=0){
							if(relationShip_endCompara!=null){
								detailDispText.append(
									modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a>",eValueTypeCompara==ValueSources.Field?strFieldAlias_endCompara:strValueCompara,relationShip_endCompara.get_Alias(),undecoratedPurposeLink_endCompara[0])
					                        			:
					                        			MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"relationship\">({1})</a> <a href=\"purposeLink\">({2})</a> <a href=\"modifer\">({3})</a>",strFieldAlias_endCompara,relationShip_endCompara.get_Alias(),undecoratedPurposeLink_endCompara[0],modifer)
								);
							}
							else{
								detailDispText.append(
									modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a>",strFieldAlias_endCompara,undecoratedPurposeLink_endCompara)
				                                		:
				                                        MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"purposeLink\">({1})</a> <a href=\"modifer\">({2})</a>",strFieldAlias_endCompara,undecoratedPurposeLink_endCompara,modifer)	
								);		
							}
						}
						else{
							detailDispText.append(
									modifer.length()==0?MessageFormat.format("<a href=\"fieldname\">{0}</a>",strFieldAlias_endCompara)
														:
														MessageFormat.format("<a href=\"fieldname\">{0}</a> <a href=\"modifer\">({1})</a>",strFieldAlias_endCompara,modifer)
							);
						}
				  }
			}
		}
		
		return detailDispText.toString();
	}
	
	/*
	 * 尾信息加工成字符串
	 */
	public String getDetailInfoToString(){
		StringBuffer detailDispText=new StringBuffer();
		if(eOperator==Operators.Null||eOperator==Operators.NotNull||
		   eOperator==Operators.Empty||eOperator==Operators.NotEmpty){
			detailDispText.append("");	
		}
		else{
			if(eValueType==ValueSources.Literal||eValueType==ValueSources.SearchParameter||
			   eValueType==ValueSources.ValidatedSearchParameter||eValueType==ValueSources.Rule||
			   eValueType==ValueSources.RecId||eValueType==ValueSources.ParentRecId||
			   eValueType==ValueSources.Prompt||eValueType==ValueSources.ValidatedPrompt||
			   eValueType==ValueSources.Token){
			   detailDispText.append(MessageFormat.format("{0}","'"+strValue+"'"));
			}
			else if(eValueType==ValueSources.Function){
				String strB = LocalizeHelper.GetValue(Type.GetType(FunctionCategory.class.getName()), strValue);
				detailDispText.append(MessageFormat.format("{0}",strB.length()!=0?strB :strValue));
			}
			else if(eValueType==ValueSources.Field){
				if(undecoratedPurposeLink_end[0]!=null&&undecoratedPurposeLink_end[0].length()!=0){
					if(relationShip_end!=null){
						detailDispText.append(
							modifer.length()==0?MessageFormat.format("{0} ({1}) ({2})",eValueType==ValueSources.Field?strFieldAlias_end:strValue,relationShip_end.get_Alias(),undecoratedPurposeLink_end[0])
			                        			 :
			                        			 MessageFormat.format("{0} ({1}) ({2}) ({3})",strFieldAlias_end,relationShip_end.get_Alias(),undecoratedPurposeLink_end[0],modifer)
						);
					}
					else{
						detailDispText.append(
							modifer.length()==0?MessageFormat.format("{0} ({1})",strFieldAlias_end,undecoratedPurposeLink_end)
		                                		:
		                                        MessageFormat.format("{0} ({1}) ({2})",strFieldAlias_end,undecoratedPurposeLink_end,modifer)	
						);		
					}
				}
				else{
					detailDispText.append(
							modifer.length()==0?MessageFormat.format("{0}",strFieldAlias_end)
												:
												MessageFormat.format("{0} ({1})",strFieldAlias_end,modifer)
					);
				}
			}
			if(eOperator==Operators.Between||eOperator==Operators.NotBetween){
				detailDispText.append(" 和 ");
				if(eValueTypeCompara==ValueSources.Literal||eValueTypeCompara==ValueSources.SearchParameter||
				   eValueTypeCompara==ValueSources.ValidatedSearchParameter||eValueTypeCompara==ValueSources.Rule||
				   eValueTypeCompara==ValueSources.RecId||eValueTypeCompara==ValueSources.ParentRecId||
				   eValueTypeCompara==ValueSources.Prompt||eValueTypeCompara==ValueSources.ValidatedPrompt||
				   eValueTypeCompara==ValueSources.Token){
						   detailDispText.append(MessageFormat.format("{0}",strValueCompara));
				   }
				   else if(eValueTypeCompara==ValueSources.Function){
					    String strB = LocalizeHelper.GetValue(Type.GetType(FunctionCategory.class.getName()), strValueCompara);
					    detailDispText.append(MessageFormat.format("{0}",strB.length()!=0?strB :strValueCompara));
				   }
				   else if(eValueTypeCompara==ValueSources.Field){
						if(undecoratedPurposeLink_endCompara[0]!=null&&undecoratedPurposeLink_endCompara[0].length()!=0){
							if(relationShip_endCompara!=null){
								detailDispText.append(
									modifer.length()==0?MessageFormat.format("{0} ({1}) ({2})",eValueTypeCompara==ValueSources.Field?strFieldAlias_endCompara:strValueCompara,relationShip_endCompara.get_Alias(),undecoratedPurposeLink_endCompara[0])
					                        			:
					                        			MessageFormat.format("{0} ({1}) ({2}) ({3})",strFieldAlias_endCompara,relationShip_endCompara.get_Alias(),undecoratedPurposeLink_endCompara[0],modifer)
								);
							}
							else{
								detailDispText.append(
									modifer.length()==0?MessageFormat.format("{0} ({1})",strFieldAlias_endCompara,undecoratedPurposeLink_endCompara)
				                                		:
				                                        MessageFormat.format("{0} ({1}) ({2})",strFieldAlias_endCompara,undecoratedPurposeLink_endCompara,modifer)	
								);		
							}
						}
						else{
							detailDispText.append(
									modifer.length()==0?MessageFormat.format("{0}",strFieldAlias_endCompara)
														:
														MessageFormat.format("{0} ({1})",strFieldAlias_endCompara,modifer)
							);
						}
				  }
			}
		}
		
		return detailDispText.toString();
	}
	/*
	 * 获取完整链接信息
	 */
	public String getFinishLinkInfo(){
		String chinaName=LocalizeHelper.GetValue(Type.GetType(Operators.class.getName()),eOperator.name());
		if(eOperator==Operators.Null){
		   chinaName="为 Null";
		}
		else if(eOperator==Operators.NotNull){
			chinaName="不为 Null";
		}
		else if(eOperator==Operators.Empty){
		    chinaName="为空";	
		}
		else if(eOperator==Operators.NotEmpty){
			chinaName="不为空";
		}
		StringBuffer finishInfo=new StringBuffer();
		finishInfo.append(getStartInfoToLinKString());
		finishInfo.append(" ");
		finishInfo.append(MessageFormat.format(" <a href=\"operator\">{0}</a> ",chinaName));
		finishInfo.append(" ");
		finishInfo.append(getDetailInfoToLinKString());
		return finishInfo.toString();
	}
	
	/*
	 * 获取完整信息
	 */
	public String getFinishInfo(){
		String chinaName=LocalizeHelper.GetValue(Type.GetType(Operators.class.getName()),eOperator.name());
		if(eOperator==Operators.Null){
		   chinaName="为 Null";
		}
		else if(eOperator==Operators.NotNull){
			chinaName="不为 Null";
		}
		else if(eOperator==Operators.Empty){
		    chinaName="为空";	
		}
		else if(eOperator==Operators.NotEmpty){
			chinaName="不为空";
		}
		StringBuffer finishInfo=new StringBuffer();
		finishInfo.append(getStartInfoToString());
		finishInfo.append(" ");
		finishInfo.append(MessageFormat.format(" {0} ",chinaName));
		finishInfo.append(" ");
		finishInfo.append(getDetailInfoToString());
		return finishInfo.toString();
	}
	
	/*
	 * 获取XmlElement
	 */
	public XmlElement getXmlElement(){
		XmlElement xmlElement=null;
		SiteviewQuery query=new SiteviewQuery();
		if(Operators.Between==eOperator||Operators.NotBetween==eOperator){
			if(modifer.length()!=0&&!modifer.equals("(无)")){
				XmlElement xeSubFunction=null;
				
				if(modifer.equals("仅日期")){
					xeSubFunction=query.get_QueryFunction().SearchByDateOnly(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("仅时间")){
					xeSubFunction=query.get_QueryFunction().SearchByTimeOnly(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("年")){
					xeSubFunction=query.get_QueryFunction().GetYear(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("季")){
					xeSubFunction=query.get_QueryFunction().GetQuarter(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("月")){
					xeSubFunction=query.get_QueryFunction().GetMonth(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("小时")){
					xeSubFunction=query.get_QueryFunction().GetHour(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("分钟")){
					xeSubFunction=query.get_QueryFunction().GetMinutes(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("一年中的一周")){
					xeSubFunction=query.get_QueryFunction().GetWeekOfYear(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("一年中的一天")){
					xeSubFunction=query.get_QueryFunction().GetDayOfYear(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("一月中的一天")){
					xeSubFunction=query.get_QueryFunction().GetDayOfMonth(eValueType, strQualifiedField_start);
				}
				else if(modifer.equals("一周中的一天")){
					xeSubFunction=query.get_QueryFunction().GetDayOfWeek(eValueType, strQualifiedField_start);
				}
				
				xmlElement=query.get_CriteriaBuilder().QueryFunctionListExpression(xeSubFunction,eOperator.name());
				
				if(eValueType==ValueSources.Literal){
					query.get_CriteriaBuilder().AddLiteralValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Function){
					query.get_CriteriaBuilder().AddFunctionValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Token){
					query.get_CriteriaBuilder().AddTokenValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Field){
					query.get_CriteriaBuilder().AddFieldValue(xmlElement,strValue);
				}
				
				if(eValueTypeCompara==ValueSources.Literal){
					query.get_CriteriaBuilder().AddLiteralValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Function){
					query.get_CriteriaBuilder().AddFunctionValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Token){
					query.get_CriteriaBuilder().AddTokenValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Field){
					query.get_CriteriaBuilder().AddFieldValue(xmlElement,strValueCompara);
				}
			}
			else{
				xmlElement=query.get_CriteriaBuilder().QueryListExpression(strQualifiedField_start,eOperator.name());
				
				if(eValueType==ValueSources.Literal){
					query.get_CriteriaBuilder().AddLiteralValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Function){
					query.get_CriteriaBuilder().AddFunctionValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Token){
					query.get_CriteriaBuilder().AddTokenValue(xmlElement,strValue);
				}
				else if(eValueType==ValueSources.Field){
					query.get_CriteriaBuilder().AddFieldValue(xmlElement,strValue);
				}
				
				if(eValueTypeCompara==ValueSources.Literal){
					query.get_CriteriaBuilder().AddLiteralValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Function){
					query.get_CriteriaBuilder().AddFunctionValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Token){
					query.get_CriteriaBuilder().AddTokenValue(xmlElement,strValueCompara);
				}
				else if(eValueTypeCompara==ValueSources.Field){
					query.get_CriteriaBuilder().AddFieldValue(xmlElement,strValueCompara);
				}

			}
		}
		else if(modifer!=null&&modifer.length()!=0&&!modifer.equals("(无)")){
			XmlElement xeSubFunction=null;
			
			if(modifer.equals("仅日期")){
				xeSubFunction=query.get_QueryFunction().SearchByDateOnly(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("仅时间")){
				xeSubFunction=query.get_QueryFunction().SearchByTimeOnly(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("年")){
				xeSubFunction=query.get_QueryFunction().GetYear(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("季")){
				xeSubFunction=query.get_QueryFunction().GetQuarter(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("月")){
				xeSubFunction=query.get_QueryFunction().GetMonth(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("小时")){
				xeSubFunction=query.get_QueryFunction().GetHour(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("分钟")){
				xeSubFunction=query.get_QueryFunction().GetMinutes(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("一年中的一周")){
				xeSubFunction=query.get_QueryFunction().GetWeekOfYear(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("一年中的一天")){
				xeSubFunction=query.get_QueryFunction().GetDayOfYear(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("一月中的一天")){
				xeSubFunction=query.get_QueryFunction().GetDayOfMonth(eValueType, strQualifiedField_start);
			}
			else if(modifer.equals("一周中的一天")){
				xeSubFunction=query.get_QueryFunction().GetDayOfWeek(eValueType, strQualifiedField_start);
			}
			
			if(eValueType==ValueSources.Literal|eValueType==ValueSources.SearchParameter||eValueType==ValueSources.ValidatedSearchParameter){
				String valueToAddToExpression=strValue;
				if (((eOperator== Operators.Like)||(eOperator==Operators.NotLike))&&((valueToAddToExpression.indexOf('%') == -1) && (valueToAddToExpression.indexOf('_') == -1))){
	                 valueToAddToExpression = valueToAddToExpression + "%";
	            }
				xmlElement=query.get_CriteriaBuilder().QueryFunctionExpression(xeSubFunction,eOperator,eValueType,valueToAddToExpression);
			}
			else if(eValueType==ValueSources.Function||eValueType==ValueSources.Token){
				xmlElement=query.get_CriteriaBuilder().QueryFunctionExpression(xeSubFunction,eOperator,eValueType,strValue);
			}
			else if(eValueType==ValueSources.Field){
				xmlElement=query.get_CriteriaBuilder().QueryFunctionExpression(xeSubFunction,eOperator,eValueType,strValue);
			}
		}
		else{
			if(eValueType==ValueSources.Literal|eValueType==ValueSources.SearchParameter||eValueType==ValueSources.ValidatedSearchParameter){
				String valueToAddToExpression=strValue;
				if (((eOperator== Operators.Like)||(eOperator==Operators.NotLike))&&((valueToAddToExpression.indexOf('%') == -1) && (valueToAddToExpression.indexOf('_') == -1))){
	                 valueToAddToExpression = valueToAddToExpression + "%";
	            }
				xmlElement=query.get_CriteriaBuilder()
								.FieldAndValueExpression(strQualifiedField_start,
														 eOperator,
														 valueToAddToExpression);
			}
			else if(eValueType==ValueSources.Function){
				xmlElement=query.get_CriteriaBuilder()
								.FieldAndFunctionExpression(strQualifiedField_start,
															eOperator,
															strValue);
			}
			else if(eValueType==ValueSources.Field){
				xmlElement=query.get_CriteriaBuilder()
								.FieldAndFieldExpression(strQualifiedField_start,
														 eOperator,
														 strValue);
			}
			else if(eValueType==ValueSources.Token){
				String valueToAddToExpression=strValue;
				xmlElement=query.get_CriteriaBuilder()
								.FieldAndTokenExpression(strQualifiedField_start,
														 eOperator,
														 valueToAddToExpression);
			}
			else{
				xmlElement=query.get_CriteriaBuilder().FieldAndValueExpression(strQualifiedField_start,eOperator,"");
			}
		}
		
		return xmlElement;
	}

	/*
	 * getter and setter
	 */
	public String getStrQualifiedField_start() {
		return strQualifiedField_start;
	}

	public void setStrQualifiedField_start(String strQualifiedField_start) {
		this.strQualifiedField_start = strQualifiedField_start;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	
	public String getStrValueCompara() {
		return strValueCompara;
	}

	public void setStrValueCompara(String strValueCompara) {
		this.strValueCompara = strValueCompara;
	}

	public Operators geteOperator() {
		return eOperator;
	}

	public void seteOperator(Operators eOperator) {
		this.eOperator = eOperator;
	}

	public ValueSources geteValueType() {
		return eValueType;
	}

	public void seteValueType(ValueSources eValueType) {
		this.eValueType = eValueType;
	}
	
	public ValueSources geteValueTypeCompara() {
		return eValueTypeCompara;
	}

	public void seteValueTypeCompara(ValueSources eValueTypeCompara) {
		this.eValueTypeCompara = eValueTypeCompara;
	}

	public String getStrFieldAlias_start() {
		return strFieldAlias_start;
	}

	public void setStrFieldAlias_start(String strFieldAlias_start) {
		this.strFieldAlias_start = strFieldAlias_start;
	}

	public RelationshipDef getRelationShip_start() {
		return relationShip_start;
	}

	public void setRelationShip_start(RelationshipDef relationShip_start) {
		this.relationShip_start = relationShip_start;
	}

	public String[] getUndecoratedPurposeLink_start() {
		return undecoratedPurposeLink_start;
	}

	public void setUndecoratedPurposeLink_start(String[] undecoratedPurposeLink_start) {
		this.undecoratedPurposeLink_start = undecoratedPurposeLink_start;
	}

	public String getStrFieldAlias_end() {
		return strFieldAlias_end;
	}

	public void setStrFieldAlias_end(String strFieldAlias_end) {
		this.strFieldAlias_end = strFieldAlias_end;
	}

	public RelationshipDef getRelationShip_end() {
		return relationShip_end;
	}

	public void setRelationShip_end(RelationshipDef relationShip_end) {
		this.relationShip_end = relationShip_end;
	}

	public String[] getUndecoratedPurposeLink_end() {
		return undecoratedPurposeLink_end;
	}

	public void setUndecoratedPurposeLink_end(String[] undecoratedPurposeLink_end) {
		this.undecoratedPurposeLink_end = undecoratedPurposeLink_end;
	}
	
	public String getStrFieldAlias_endCompara() {
		return strFieldAlias_endCompara;
	}

	public void setStrFieldAlias_endCompara(String strFieldAlias_endCompara) {
		this.strFieldAlias_endCompara = strFieldAlias_endCompara;
	}

	public RelationshipDef getRelationShip_endCompara() {
		return relationShip_endCompara;
	}

	public void setRelationShip_endCompara(RelationshipDef relationShip_endCompara) {
		this.relationShip_endCompara = relationShip_endCompara;
	}

	public String[] getUndecoratedPurposeLink_endCompara() {
		return undecoratedPurposeLink_endCompara;
	}

	public void setUndecoratedPurposeLink_endCompara(String[] undecoratedPurposeLink_endCompara) {
		this.undecoratedPurposeLink_endCompara = undecoratedPurposeLink_endCompara;
	}

	public String getModifer() {
		return modifer;
	}

	public void setModifer(String modifer) {
		this.modifer = modifer;
	}
	
}
