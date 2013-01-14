package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import core.search.form.SearchCenterForm;

/*
 * ËÑË÷ÖÐÐÄHandler 
 */
public class SearchCenterHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		SearchCenterForm searchCenterForm=new SearchCenterForm();
		searchCenterForm.open();
		return null;
	}

}
