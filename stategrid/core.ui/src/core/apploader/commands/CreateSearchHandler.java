package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;

import core.search.form.dialog.Search_Definition;

public class CreateSearchHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		new Search_Definition(new Shell(),"createSearch","").open();
		return null;
	}

}
