package cassiopeia.plugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import cassiopeia.plugin.wizards.pages.SCJSafeletWizardPage;
import cassiopeia.plugin.wizards.templates.SafeletTemplate;
import cassiopeia.plugin.wizards.templates.models.SafeletModel;

public class SCJSafeletWizard extends SharedWizard implements ISharedWizard {
	
	private SCJSafeletWizardPage page;
	
	private int complianceLevel;

	public SCJSafeletWizard() {
		SharedWizard.wizard = this;
	}
	
	public void addPages() {
		page = new SCJSafeletWizardPage(selection);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final String containerName = page.getDestination();
		final String fileName = page.getFileNameWithExtension();
		super.packageName = page.getPackageName();
		super.name = page.getFileNameWithoutExtension();
		
		complianceLevel = page.getComplianceLevel();
		
		return super.performSharedFinish(containerName, fileName);
	}

	public InputStream openContentStream() {
		SafeletTemplate template = new SafeletTemplate();
		SafeletModel model = new SafeletModel();
		
		if(!super.packageName.isEmpty())
			model.packageDeclaration = "package " + super.packageName + ";";
		model.name = super.name;
		
		switch(complianceLevel) {
			case 0:
				model.safeletTypeParameter = "CyclicExecutive";
				break;
			case 1:
			case 2:
				model.safeletTypeParameter = "Mission";
				break;
		}

		return new ByteArrayInputStream(template.generate(model).getBytes());
	}
}