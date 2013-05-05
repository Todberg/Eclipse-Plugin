package cassiopeia.plugin.wizards.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SCJSafeletWizardPage extends SharedWizardPage {

	/* SWT button widgets */
	private Button btnRadioLevel0;
	private Button btnRadioLevel1;
	private Button btnRadioLevel2;
	private Label lblComplianceLevel;

	public SCJSafeletWizardPage(ISelection selection) {
		super("SafeletWizardPage", selection);
		
		setTitle("Safelet Class");
		setDescription("Create a new implementation of the Safelet class");
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		super.createControl(composite);
		
		GridLayout gridLayout = new GridLayout(4, false);
		grpSCJSpecific.setLayout(gridLayout);
		
		lblComplianceLevel = new Label(grpSCJSpecific, SWT.NONE);
		GridData gridData = new GridData();
		lblComplianceLevel.setLayoutData(gridData);
		lblComplianceLevel.setText("Compliance level:");
		
		btnRadioLevel0 = new Button(grpSCJSpecific, SWT.RADIO);
		gridData = new GridData();
		btnRadioLevel0.setLayoutData(gridData);
		btnRadioLevel0.setText("Level 0");
			
		btnRadioLevel1 = new Button(grpSCJSpecific, SWT.RADIO);
		gridData = new GridData();
		btnRadioLevel1.setLayoutData(gridData);
		btnRadioLevel1.setText("Level 1");
		btnRadioLevel1.setSelection(true);
		
		btnRadioLevel2 = new Button(grpSCJSpecific, SWT.RADIO);
		gridData = new GridData();
		btnRadioLevel2.setLayoutData(gridData);
		btnRadioLevel2.setText("Level 2");
			
		initializePage();
		dialogChanged();
		setControl(composite);
	}

	private void initializePage() {
		super.initialize();
		txtName.setText("MySafelet");
	}

	public int getComplianceLevel() {
		int level;
		
		if(btnRadioLevel0.getSelection())
			level = 0;
		else if(btnRadioLevel1.getSelection())
			level = 1;
		else
			level = 2;
		
		return level;
	}
}