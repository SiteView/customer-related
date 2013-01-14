package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import core.apploader.tools.dialog.GoToObject;

public class FindBusObHandler extends AbstractHandler{
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		GoToObject goToObject=new GoToObject(Display.getCurrent().getActiveShell());
		goToObject.open();
		return null;
	}
}
