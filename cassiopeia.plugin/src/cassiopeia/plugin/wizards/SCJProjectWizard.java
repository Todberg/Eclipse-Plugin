package cassiopeia.plugin.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class SCJProjectWizard extends Wizard implements INewWizard {

	private WizardNewProjectCreationPage pageOne;
	
	public SCJProjectWizard() {
		setWindowTitle("New SCJ Project");
	}
	
	@Override
	public void addPages() {
		super.addPages();
		
		pageOne = new WizardNewProjectCreationPage("Page one");
		pageOne.setTitle("Create a SCJ Project");
		pageOne.setDescription("Enter a project name.");
		
		addPage(pageOne);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
