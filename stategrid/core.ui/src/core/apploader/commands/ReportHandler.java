package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import core.reporting.*;
//import core.reporting.appinterface.IRoportCenterAPI;

public class ReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		IRoportCenterAPI api = IRoportCenterAPI.getInstance();
//		api.openReportCenterMainFrame();
		ReportCenterForm rcf = new  ReportCenterForm(false,"");
		rcf.setBlockOnOpen(true);
		rcf.open();
		return null;
	}

}
