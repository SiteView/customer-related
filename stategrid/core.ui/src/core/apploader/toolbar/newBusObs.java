package core.apploader.toolbar;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class newBusObs extends ContributionItem {

	@Override
	public void fill(ToolBar parent, int index) {
		ToolItem ti = new ToolItem(parent,SWT.NONE, index);
		ti.setText("ÐÂ½¨");
	}


}
