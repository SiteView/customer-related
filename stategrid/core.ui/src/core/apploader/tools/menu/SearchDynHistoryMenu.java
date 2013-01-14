package core.apploader.tools.menu;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.widgets.Menu;

import core.apploader.search.comm.CommEnum.HistoryDataType;
import core.apploader.search.comm.CommEnum.ShowFunctionType;
import core.apploader.tools.comm.CommHistoryDataLoad;
import core.apploader.tools.service.inter.IHistoryFieldEvent;

public class SearchDynHistoryMenu extends ContributionItem {
	
	private HistoryDataType  historyDataTpe;
	private ShowFunctionType showFunctionType;
	private IHistoryFieldEvent iHistoryFieldEvent;
	
	public  SearchDynHistoryMenu(){
		historyDataTpe=HistoryDataType.SEARCH;
		showFunctionType=ShowFunctionType.SHOW_MENU_HISTORY;
		iHistoryFieldEvent=CommHistoryDataLoad.queryGroupNavComposite.getIhistoryFieldEvent();
	}
	
	public void fill(Menu menu, int index) {
		HistoryMenuField historyMenuField=new HistoryMenuField(menu,index,historyDataTpe,iHistoryFieldEvent,showFunctionType);
		historyMenuField.fillMenu();
	}
	
}
