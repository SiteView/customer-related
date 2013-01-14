package core.apploader.reporting.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class PublishedReportW4_Ver extends WizardPage {
	
	private PublishedReportWizard publishReportWizard;
	private Table table;
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW4_Ver() {
		super("PublishedReportW4_Ver");
		setTitle("\u62A5\u544A\u9A8C\u8BC1");
		setDescription("\u9A8C\u8BC1\u6BCF\u4E2A\u62A5\u544A\u540E\uFF0C\u9519\u8BEF\u548C\u8B66\u544A\u5217\u8868\u4F1A\u5728\u4E0B\u65B9\u51FA\u73B0\u3002\u5FC5\u987B\u4FEE\u6B63\u6240\u6709\u9519\u8BEF\u540E\u624D\u80FD\u7EE7\u7EED\u3002");
	}
	public PublishedReportW4_Ver(PublishedReportWizard prw) {
		this();
		
		publishReportWizard = prw;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		setControl(container);
		
		Group group = new Group(container, SWT.NONE);
		group.setBounds(0, 281, 533, 60);
		
		Label lblSelectWhetherYou = new Label(group, SWT.NONE);
		lblSelectWhetherYou.setBounds(10, 22, 271, 26);
		lblSelectWhetherYou.setText("\u9519\u8BEF\u548C/\u6216\u8B66\u544A\u5217\u8868\u3002");
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 0, 533, 275);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("\u7C7B\u578B");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(411);
		tblclmnNewColumn_1.setText("\u8BF4\u660E");
	}
	
	public PublishedReportWizard getPublishReportWizard() {
		return publishReportWizard;
	}
	public void setPublishReportWizard(PublishedReportWizard publishReportWizard) {
		this.publishReportWizard = publishReportWizard;
	}
}
