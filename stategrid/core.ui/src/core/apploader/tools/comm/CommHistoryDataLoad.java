package core.apploader.tools.comm;

import core.apploader.tools.composite.HistoryNavField;
import Siteview.Api.BusinessObject;

public class CommHistoryDataLoad {
	
	public static String BUSOBNAME="Change";
	public static BusinessObject BUSINESSOBJECT;
	
	public static HistoryNavField autoTaskNavComposite;
	
	public static HistoryNavField queryGroupNavComposite;
	
	public static HistoryNavField reportNavComposite;
	
	public static HistoryNavField dashbordNavComposite;
	
	public static void loadHistoryNav(){
		autoTaskNavComposite.loadHistoryNavData();
		queryGroupNavComposite.loadHistoryNavData();
		reportNavComposite.loadHistoryNavData();
		dashbordNavComposite.loadHistoryNavData();
	}
	
	public static void loadHistoryNav(String busObName){
		if(busObName!=null&&busObName.length()>0){
			BUSOBNAME=busObName;
		}
		if (autoTaskNavComposite!=null)
			autoTaskNavComposite.loadHistoryNavData();
		if (queryGroupNavComposite!=null)
			queryGroupNavComposite.loadHistoryNavData();
		if (reportNavComposite!=null)
			reportNavComposite.loadHistoryNavData();
		if (dashbordNavComposite!=null)
			dashbordNavComposite.loadHistoryNavData();
	}
	
	public static void loadHistoryNav(BusinessObject busnessObject){
		if(busnessObject!=null){
			BUSOBNAME=busnessObject.get_Name();
		}
		if (autoTaskNavComposite!=null)
			autoTaskNavComposite.loadHistoryNavData();
		if (queryGroupNavComposite!=null)
			queryGroupNavComposite.loadHistoryNavData();
		if (reportNavComposite!=null)
			reportNavComposite.loadHistoryNavData();
		if (dashbordNavComposite!=null)
			dashbordNavComposite.loadHistoryNavData();
	}
	
}
