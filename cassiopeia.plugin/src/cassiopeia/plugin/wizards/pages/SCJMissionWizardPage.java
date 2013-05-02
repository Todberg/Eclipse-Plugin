package cassiopeia.plugin.wizards.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class SCJMissionWizardPage extends SharedWizardPage {
	
	/* SWT ? widgets */

	public SCJMissionWizardPage(ISelection selection) {
		super("EventHandlerNewWizardPage", selection);
		
		setTitle("Mission Class");
		setDescription("Create a new implementation of the Mission class.");
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		super.createControl(composite);
		
		initializePage();
		dialogChanged();
		setControl(composite);
	}

	private void initializePage() {
		super.initialize();
		txtName.setText("MyMission");
	}
}