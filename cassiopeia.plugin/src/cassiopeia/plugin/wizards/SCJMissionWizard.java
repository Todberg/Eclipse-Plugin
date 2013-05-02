package cassiopeia.plugin.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import cassiopeia.plugin.wizards.pages.SCJMissionWizardPage;
import cassiopeia.plugin.wizards.templates.MissionTemplate;
import cassiopeia.plugin.wizards.templates.models.MissionModel;

public class SCJMissionWizard extends SharedWizard implements ISharedWizard {
	
	private SCJMissionWizardPage page;

	public SCJMissionWizard() {
		SharedWizard.wizard = this;
	}
	
	public void addPages() {
		page = new SCJMissionWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		final String containerName = page.getDestination();
		final String fileName = page.getFileNameWithExtension();
		super.packageName = page.getPackageName();
		super.name = page.getFileNameWithoutExtension();
		
		return super.performSharedFinish(containerName, fileName);
	}
	
	@Override
	public InputStream openContentStream() {
		MissionTemplate template = new MissionTemplate();
		MissionModel model = new MissionModel();
		
		if(!super.packageName.isEmpty())
			model.packageDeclaration = "package " + super.packageName + ";";
		model.name = super.name;
		
		return new ByteArrayInputStream(template.generate(model).getBytes());
	}
}