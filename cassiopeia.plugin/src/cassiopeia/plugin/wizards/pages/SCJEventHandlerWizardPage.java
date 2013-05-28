package cassiopeia.plugin.wizards.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class SCJEventHandlerWizardPage extends SharedWizardPage {
	
	/* SWT combo widget */
	private Combo combo;
	
	public enum EventHandlerType {
		Periodic,
		Aperiodic
	}
	
	public SCJEventHandlerWizardPage(ISelection selection) {
		super("EventHandlerWizardPage", selection);
		
		setTitle("Event Handler Class");
		setDescription("Create a new implementation of an event handler class.");
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		super.createControl(composite);

		GridLayout gridLayout = new GridLayout(2, false);
		grpSCJSpecific.setLayout(gridLayout);
		
		Label lblHandlerType = new Label(grpSCJSpecific, SWT.NONE);
		GridData gridData = new GridData();
		lblHandlerType.setLayoutData(gridData);
		lblHandlerType.setText("Handler type:");

		combo = new Combo(grpSCJSpecific, SWT.READ_ONLY);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.LEFT;
		combo.setLayoutData(gridData);
		combo.setItems(new String[] { 
				EventHandlerType.Periodic.toString(), 
				EventHandlerType.Aperiodic.toString() 
		});
		combo.select(0);
		
		initializePage();
		dialogChanged();
		setControl(composite);
	}

	private void initializePage() {
		super.initialize();
		txtName.setText("MyEventHandler");
	}
	
	public EventHandlerType getEventHandlerType() {
		return (combo.getSelectionIndex() == 0 ? EventHandlerType.Periodic : EventHandlerType.Aperiodic);
	}
}