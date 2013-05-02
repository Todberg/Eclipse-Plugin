package cassiopeia.plugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;

import cassiopeia.plugin.wizards.pages.SCJEventHandlerWizardPage;
import cassiopeia.plugin.wizards.pages.SCJEventHandlerWizardPage.EventHandlerType;
import cassiopeia.plugin.wizards.templates.AperiodicEventHandlerTemplate;
import cassiopeia.plugin.wizards.templates.PeriodicEventHandlerTemplate;
import cassiopeia.plugin.wizards.templates.models.EventHandlerModel;

public class SCJEventHandlerWizard extends SharedWizard implements ISharedWizard {
	
	private SCJEventHandlerWizardPage page;

	private EventHandlerType type;
	
	public SCJEventHandlerWizard() {
		SharedWizard.wizard = this;
	}
	
	public void addPages() {
		page = new SCJEventHandlerWizardPage(selection);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final String containerName = page.getDestination();
		final String fileName = page.getFileNameWithExtension();
		super.packageName = page.getPackageName();
		super.name = page.getFileNameWithoutExtension();
		
		type = page.getEventHandlerType();
		
		return super.performSharedFinish(containerName, fileName);
	}
	
	@Override
	public InputStream openContentStream() {
		EventHandlerModel model = new EventHandlerModel();		
		
		if(!super.packageName.isEmpty())
			model.packageDeclaration = "package " + super.packageName + ";";
		model.name = super.name;
		
		String result = null;
		switch(type) {
			case Periodic:
				model.parameters = "PeriodicParameters";
				result = new PeriodicEventHandlerTemplate().generate(model);
				break;
			case Aperiodic:
				model.parameters = "AperiodicParameters";
				result = new AperiodicEventHandlerTemplate().generate(model);
				break;
		}
		
		return new ByteArrayInputStream(result.getBytes());
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}