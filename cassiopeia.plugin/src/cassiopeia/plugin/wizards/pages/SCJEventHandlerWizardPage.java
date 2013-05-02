package cassiopeia.plugin.wizards.pages;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
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
		
		Label lblHandlerType = new Label(composite, SWT.NONE);
		lblHandlerType.setBounds(24, 180, 79, 14);
		lblHandlerType.setText("Handler type:");
		
		combo = new Combo(composite, SWT.READ_ONLY);
		combo.setBounds(109, 176, 385, 22);
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
		EventHandlerType type = null;
		
		int selectionIndex = combo.getSelectionIndex();
		switch(selectionIndex) {
			case 0:
				type = EventHandlerType.Periodic;
				break;
			case 1:
				type = EventHandlerType.Aperiodic;
				break;
		}
		
		return type;
	}
}