package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;

import core.apploader.tools.dialog.ChangePassword;

public class ChangePasswordHandler extends AbstractHandler{
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ChangePassword goToObject=new ChangePassword(Display.getCurrent().getActiveShell());
		goToObject.open();
		return null;
	}
}
