package core.apploader.reporting.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import siteview.windows.forms.ImageResolver;
import siteview.windows.forms.SwtImageConverter;


public class PublishedReportW4_Fin extends WizardPage {
	
	private PublishedReportWizard publishReportWizard;
	
	private Label lblsubHeader;
	private Label lblsubMessage;
	
	
	/**
	 * Create the wizard.
	 * @wbp.parser.constructor
	 */
	public PublishedReportW4_Fin() {
		super("PublishedReportW4_Fin");
		setTitle("\u6458\u8981");
		setDescription("");
	}
	public PublishedReportW4_Fin(PublishedReportWizard prw) {
		this();
		
		publishReportWizard = prw;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		// parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = new Composite(parent, SWT.NONE);
		// container.setBackground(new Color(null,255,255,255));
		container.setBounds(0, 0, parent.getSize().x, parent.getSize().y);
		setControl(container);
		
		lblsubHeader = new Label(container, SWT.NONE);
		// lblsubHeader.setBackground(new Color(null,255,255,255));
		lblsubHeader.setBounds(134, 9, 388, 39);
		lblsubHeader.setFont(new Font(null,"",18, SWT.BOLD));
		
		final Canvas canvas1 = new Canvas(container, SWT.NONE);
		canvas1.setBackground(new Color(null,255,255,255));
		canvas1.setBounds(72, 9, 41, 39);
		canvas1.setLayout(new FillLayout());
		canvas1.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image1 = SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#pictureBox1.Image.png"));//ResourceManager.getPluginImage("core.ui", "icons/pictureBox1.Image.png");
				ImageData data=image1.getImageData();
				ImageData data1=data.scaledTo(canvas1.getSize().x, canvas1.getSize().y);
				Image im1=new Image(e.display,data1);
				e.gc.drawImage(im1,0,0);
				im1.dispose();
			}
		});
		
		final Canvas canvas = new Canvas(container, SWT.NONE);
		// canvas.setBackground(new Color(null,255,255,255));
		canvas.setBounds(0, 0, 120, 341);
		canvas.setLayout(new FillLayout());
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image= SwtImageConverter.ConvertToSwtImage(ImageResolver.get_Resolver().ResolveImage("[IMAGE]Core#BigPictureBox.Image.png"));//ResourceManager.getPluginImage("core.ui", "icons/BigPictureBox.Image.png");
				ImageData data=image.getImageData();
				ImageData data1=data.scaledTo(canvas.getSize().x, canvas.getSize().y);
				Image im=new Image(e.display,data1);
				e.gc.drawImage(im,0,0);
				im.dispose();
			}
		});
		
		lblsubMessage = new Label(container, SWT.WRAP);
		// lblsubMessage.setBackground(new Color(null,255,255,255));
		lblsubMessage.setBounds(134, 59, 388, 272);
		
		loadData();
	}
	
	public void loadData(){
		this.setTitle(publishReportWizard.getM_SummaryPageHeading());
		this.setDescription(publishReportWizard.getM_SummaryPageMessage());
		this.lblsubHeader.setText(publishReportWizard.getM_SummaryPageHeading());
		this.lblsubMessage.setText(publishReportWizard.getM_SummaryPageMessage());
		
	}
	public PublishedReportWizard getPublishReportWizard() {
		return publishReportWizard;
	}
	public void setPublishReportWizard(PublishedReportWizard publishReportWizard) {
		this.publishReportWizard = publishReportWizard;
	}
}
