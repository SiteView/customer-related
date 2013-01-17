package core.apploader.menu;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import core.busobmaint.BusObMaintInput;
import core.busobmaint.BusObMaintView;
import core.busobmaint.BusObNewInput;
import core.editors.DashboardInput;
import core.editors.DashboardPanel;

import system.Collections.ICollection;
import system.Collections.IEnumerator;
import Siteview.DefRequest;
import Siteview.PlaceHolder;
import Siteview.Api.BusinessObjectDef;
import Siteview.Api.ISiteviewApi;
import Siteview.Api.RoleDef;
import Siteview.Api.WorkspaceDef;
import Siteview.Windows.Forms.ConnectionBroker;
import Siteview.Xml.Scope;
import Siteview.Xml.SecurityRight;

public class busObMenu extends ContributionItem {
	private List<PlaceHolder> m_aNewBusinessObjects = null;
	
	public synchronized List<PlaceHolder> GetNewBusinessObjects(ISiteviewApi api){
		
		if (m_aNewBusinessObjects == null){
			m_aNewBusinessObjects = new Vector<PlaceHolder>();
		
			ICollection busLinks = api.get_BusObDefinitions().GetBusObPlaceHolderList();//(DefRequest.ForList(BusinessObjectDef.get_ClassName()));
			IEnumerator it = busLinks.GetEnumerator();
			while(it.MoveNext()){
				PlaceHolder ph = (PlaceHolder)it.get_Current();
				
				if (ph.HasFlag("Master") && ph.HasFlag("CommonlyUsed") && !ph.HasFlag("AllowDerivation") && api.get_SecurityService().HasBusObRight(ph.get_Name(), SecurityRight.Add)){
					m_aNewBusinessObjects.add(ph);
				}
				
			}
			Collections.sort(m_aNewBusinessObjects,new Comparator<PlaceHolder>(){
				@Override
				public int compare(PlaceHolder o1, PlaceHolder o2) {
					String s1 = o1.get_Alias();
					String s2 = o2.get_Alias();
					return s1.compareToIgnoreCase(s2);
				}});
		}
		return m_aNewBusinessObjects;
	}

	@Override
	public void fill(Menu menu, int index) {
		//new MenuItem(menu, SWT.SEPARATOR, index);
//		MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index+1);
//		menuItem.setText("业务对象");
		String curBoName  = "";
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if (editor!=null&& editor instanceof DashboardPanel){
			DashboardInput dbi = (DashboardInput) editor.getEditorInput();
			RoleDef currentRole = (RoleDef) dbi.getApi().get_RoleManager().GetCurrentRole();
			system.Collections.ArrayList wks = currentRole.get_WorkspaceList();
			for(int i = 0 ; i < wks.get_Count(); i++){
				String wkd = (String) wks.get_Item(i);
				WorkspaceDef wksd = (WorkspaceDef) dbi.getApi().get_LiveDefinitionLibrary().GetDefinition(DefRequest.ByName(WorkspaceDef.get_ClassName(), wkd));
				if (wksd != null && wksd.get_DashboardName().equals(dbi.getDashboardDef().get_Name())){
					curBoName = wksd.get_BusinessObjectName();
					break;
				}
			}	
		}else if (editor!=null&& editor instanceof BusObMaintView){
			if (editor.getEditorInput() instanceof BusObMaintInput){
				BusObMaintInput boi = (BusObMaintInput) editor.getEditorInput();
				curBoName = boi.getBusobName();
			}else if (editor.getEditorInput() instanceof BusObNewInput){
				BusObNewInput boni = (BusObNewInput) editor.getEditorInput();
				curBoName = boni.getBusobName();
			}
		}
		
		List<PlaceHolder> bobs = GetNewBusinessObjects(ConnectionBroker.get_SiteviewApi());
	
		int miIndex = index;
		
		if (curBoName !=null && ! curBoName.equals("")&& ConnectionBroker.get_SiteviewApi().get_SecurityService().HasBusObRight(curBoName, SecurityRight.Add)){
			BusinessObjectDef phs = ConnectionBroker.get_SiteviewApi().get_LiveDefinitionLibrary().GetBusinessObjectDef(curBoName);
			PlaceHolder ph = new PlaceHolder(phs);
			MenuItem mi = new MenuItem(menu, SWT.PUSH, miIndex++);
			mi.setText(ph.get_Alias());
			mi.setData("_ph",ph);
			mi.addSelectionListener(new SelectionAdapter(){

				@Override
				public void widgetSelected(SelectionEvent e) {
					MenuItem mi = (MenuItem) e.getSource();
					PlaceHolder ph = (PlaceHolder) mi.getData("_ph");
					BusObMaintView.newBusOb(ConnectionBroker.get_SiteviewApi(), ph.get_Name());
				}});
			
			new MenuItem(menu, SWT.SEPARATOR, miIndex++);
		
		}
		
		
		for(PlaceHolder ph:bobs){
			if (ph.HasFlag("CommonlyUsed") && ConnectionBroker.get_SiteviewApi().get_SecurityService().HasBusObRight(ph.get_Name(), SecurityRight.View)){
				MenuItem mi = new MenuItem(menu, SWT.PUSH, miIndex++);
				mi.setText(ph.get_Alias());
				mi.setData("_ph",ph);
				mi.addSelectionListener(new SelectionAdapter(){

					@Override
					public void widgetSelected(SelectionEvent e) {
						MenuItem mi = (MenuItem) e.getSource();
						PlaceHolder ph = (PlaceHolder) mi.getData("_ph");
						BusObMaintView.newBusOb(ConnectionBroker.get_SiteviewApi(), ph.get_Name());
					}});
			}
		}

	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean isDirty() {
		return super.isDirty();
	}
	
}
