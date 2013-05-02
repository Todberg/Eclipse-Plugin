package cassiopeia.plugin.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.swt.widgets.Group;

public class SCJSafeletNewWizardPage extends WizardPage {

	private ISelection selection;
	
	private Text txtSourceFolder;
	private Text txtName;
	private Text txtPackage;
	private Button btnBrowsePackage;
	private Button btnBrowseSourceFolder;
	private Label lblDefault;
	private Button btnRadioLevel0;
	private Button btnRadioLevel1;
	private Button btnRadioLevel2;

	
	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public SCJSafeletNewWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Safelet Class");
		setDescription("Create a new implementation of the Safelet class");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		
		Label lblSourceFolder = new Label(composite, SWT.NONE);
		lblSourceFolder.setBounds(10, 17, 79, 14);
		lblSourceFolder.setText("Source folder:");
		
		txtSourceFolder = new Text(composite, SWT.BORDER);
		txtSourceFolder.setBounds(95, 14, 391, 19);
		txtSourceFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		btnBrowseSourceFolder = new Button(composite, SWT.NONE);
		btnBrowseSourceFolder.setBounds(492, 10, 94, 28);
		btnBrowseSourceFolder.setText("Browse...");
		btnBrowseSourceFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseSourceFolder();
			}
		});
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 80, 570, 2);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setBounds(10, 105, 59, 14);
		lblName.setText("Name:");
		
		txtName = new Text(composite, SWT.BORDER);
		txtName.setBounds(95, 102, 391, 19);
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		txtPackage = new Text(composite, SWT.BORDER);
		txtPackage.setBounds(95, 50, 336, 19);
		txtPackage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		btnBrowsePackage = new Button(composite, SWT.NONE);
		btnBrowsePackage.setEnabled(false);
		btnBrowsePackage.setBounds(492, 46, 94, 28);
		btnBrowsePackage.setText("Browse...");
		btnBrowsePackage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowsePackage();
			}
		});
		
		lblDefault = new Label(composite, SWT.NONE);
		lblDefault.setBounds(437, 53, 49, 14);
		lblDefault.setText("(default)");
		
		Label lblPackage = new Label(composite, SWT.NONE);
		lblPackage.setBounds(10, 50, 59, 14);
		lblPackage.setText("Package:");
		
		Group grpComplianceLevel = new Group(composite, SWT.NONE);
		grpComplianceLevel.setText("Compliance level:");
		grpComplianceLevel.setBounds(10, 143, 207, 147);
		
		btnRadioLevel0 = new Button(grpComplianceLevel, SWT.RADIO);
		btnRadioLevel0.setBounds(30, 23, 91, 18);
		btnRadioLevel0.setText("Level 0");
		
		btnRadioLevel1 = new Button(grpComplianceLevel, SWT.RADIO);
		btnRadioLevel1.setBounds(30, 57, 91, 18);
		btnRadioLevel1.setText("Level 1");
		btnRadioLevel1.setSelection(true);
		
		btnRadioLevel2 = new Button(grpComplianceLevel, SWT.RADIO);
		btnRadioLevel2.setBounds(30, 92, 91, 18);
		btnRadioLevel2.setText("Level 2");
		
		initialize();
		dialogChanged();
		setControl(composite);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */
	private void initialize() {
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object element = ssel.getFirstElement();
			
			IProject project = null;
			if (element instanceof IResource) {
		        project = ((IResource)element).getProject();
			} else if (element instanceof IJavaElement) {
				IJavaProject javaProject= ((IJavaElement)element).getJavaProject();
		        project = javaProject.getProject(); 
			}
			
			if(project != null) {
				txtSourceFolder.setText(project.getName().toString() + "/src");
			}
		}
		txtName.setText("MySafelet.java");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */
	private void handleBrowseSourceFolder() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Choose a source folder");
		
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				Path path = (Path)result[0];
				String text = path.toString();
				txtSourceFolder.setText(text);
			}
		}
	}
	
	private void handleBrowsePackage() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IContainer container = (IContainer)root.findMember(new Path(getSourceFolderName()));
	
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), 
				WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		dialog.setTitle("Package Selection");
		dialog.setMessage("Choose a package folder");

		List<IResource> packageResources = new ArrayList<IResource>();
		IResource[] members;
		try {
			members = container.members();
			for(IResource resource : members) {
				if((resource.getType() & (IResource.FOLDER)) == 2) {
					packageResources.add(resource);
				}
			}
			dialog.setElements(packageResources.toArray());
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
			
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				String text = result[0].toString();
				text = text.substring(text.lastIndexOf('/') + 1);
				txtPackage.setText(text);
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {	
		if(!validateSourceFolder())
			return;
		if(!validatePackage())
			return;
		if(!validateFileName())
			return;
		
		updateStatus(null);
	}
	
	private boolean validateSourceFolder() {
		btnBrowsePackage.setEnabled(false);
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getSourceFolderName()));
		
		if (getSourceFolderName().length() == 0) {
			updateStatus("Source folder must be specified");
			return false;
		}
		
		if (container == null || (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("Source folder must exist");
			return false;
		}
		
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return false;
		}
		
		btnBrowsePackage.setEnabled(true);
		return true;
	}
	
	private boolean validatePackage() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getSourceFolderName() + "/" + getPackageName()));
		
		if(getPackageName().length() > 0) {
			lblDefault.setText("");
			
			if (container == null
					|| (container.getType() & (IResource.FOLDER)) == 0) {
				updateStatus("Package must exist");
				return false;
			}
			
			if (!container.isAccessible()) {
				updateStatus("Package must be writable");
				return false;
			}
		} else {
			lblDefault.setText("(default)");
		}
		
		return true;
	}
	
	private boolean validateFileName() {
		String fileName = getFileName();
		
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return false;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return false;
		}
		
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("java") == false) {
				updateStatus("File extension must be \"java\"");
				return false;
			}
		}
		
		return true;
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getSourceFolderName() {
		return txtSourceFolder.getText();
	}
	
	public String getPackageName() {
		return txtPackage.getText();
	}
	
	public String getFileName() {
		return txtName.getText();
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

	public String getDestination() {
		return getSourceFolderName() + "/" + getPackageName();
	}
	
	public String getFileNameWithExtension() {
		String fileName = getFileName();
		if(fileName.lastIndexOf('.') == -1) {
			fileName += ".java";
		}
		return fileName;
	}
	
	public String getFileNameWithoutExtension() {
		String fileName = getFileName();
		int dotIndex = fileName.lastIndexOf('.');
		if(dotIndex > 0) {
			fileName = fileName.substring(0, dotIndex);
		}
		return fileName;
	}
}