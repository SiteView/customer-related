package core.apploader.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import core.apploader.documents.DocumentsCenterForm;


public class DocumentsCenterHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		DocumentsCenterForm frm = new  DocumentsCenterForm(false,"");
		frm.setBlockOnOpen(true);
		frm.open();
		return null;
	}
	
}
