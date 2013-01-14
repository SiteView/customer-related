package core.webloader;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IWorkbenchAction saveAction;
	private IWorkbenchAction refreshAction;
	private IWorkbenchAction cutAction;
	private IWorkbenchAction copyAction;
	private IWorkbenchAction pasteAction;
	private IWorkbenchAction perferenceAction;
	
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
    	saveAction = ActionFactory.SAVE.create(window);
    	register(saveAction);
    	
    	refreshAction = ActionFactory.REFRESH.create(window);
    	register(refreshAction);
    	
    	cutAction = ActionFactory.CUT.create(window);
    	register(cutAction);
    	
    	copyAction = ActionFactory.COPY.create(window);
    	register(copyAction);
    	
    	pasteAction = ActionFactory.PASTE.create(window);
    	register(pasteAction);
    	
    	perferenceAction = ActionFactory.PREFERENCES.create(window);
    	register(perferenceAction);

    }

    protected void fillMenuBar(IMenuManager menuBar) {
    	//menuBar.add(copyAction);
    }
    
}
