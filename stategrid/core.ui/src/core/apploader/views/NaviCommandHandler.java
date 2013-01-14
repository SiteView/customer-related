package core.apploader.views;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

import core.apploader.commands.QuickCenterHandler;
import core.apploader.commands.SearchCenterHandler;

public class NaviCommandHandler extends SelectionAdapter {

	@Override
	public void widgetSelected(SelectionEvent e) {
		Control c = (Control)e.getSource();
		String cmd = (String) c.getData();
		System.out.println("Command:" + cmd);
		if (cmd.equals("AutoTask.List")){
			QuickCenterHandler qc = new QuickCenterHandler();
			try {
				qc.execute(null);
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if (cmd.equals("Search.List")){
			SearchCenterHandler sc = new SearchCenterHandler();
			try {
				sc.execute(null);
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if (cmd.equals("Search.List")){
			SearchCenterHandler sc = new SearchCenterHandler();
			try {
				sc.execute(null);
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
