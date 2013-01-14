package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import core.busobmaint.BusObMaintView;
import core.editors.DashboardPanel;



public class RefreshHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor instanceof DashboardPanel){
			((DashboardPanel)editor).Refresh();
		}else if (editor instanceof BusObMaintView){
			((BusObMaintView)editor).Refresh();
		}
		return null;
	}

}
