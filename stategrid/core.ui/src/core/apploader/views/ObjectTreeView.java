package core.apploader.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import Siteview.Windows.Forms.AggregateTreeBrowser;
import Siteview.Windows.Forms.ConnectionBroker;

public class ObjectTreeView extends ViewPart {

	public static final String ID = "core.apploader.views.ObjectTreeView"; //$NON-NLS-1$
	private AggregateTreeBrowser m_ObjectTreeBrowserApp;

	public ObjectTreeView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		container.setLayout(new FillLayout());
		
		m_ObjectTreeBrowserApp = new AggregateTreeBrowser(container, ConnectionBroker.get_SiteviewApi(), "");

		m_ObjectTreeBrowserApp.reload();
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
