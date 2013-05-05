package cassiopeia.plugin.wizards.pages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class SCJProjectWizardPage extends WizardNewProjectCreationPage {

	public SCJProjectWizardPage(String pageName) {
		super(pageName);
		
		setTitle("Create a SCJ Project");
		setDescription("Enter a project name.");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite composite = (Composite)getControl();
		
		Group grpSCJParams = new Group(composite, SWT.BORDER);
		grpSCJParams.setText("Safety-Critical Java Specific");
		grpSCJParams.setBounds(10, 10, 505, 230);
		grpSCJParams.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
}
