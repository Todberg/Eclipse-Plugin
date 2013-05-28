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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class SharedWizardPage extends WizardPage {
	
	/* SWT text widgets */
	protected Text txtSourceFolder;
	protected Text txtPackage;
	protected Text txtName;

	/* SWT button widgets */
	protected Button btnBrowsePackage;
	protected Button btnBrowseSourceFolder;
	
	/* SWT label widgets */
	protected Label lblDefault;
	
	/* SWT group widgets */
	protected Group grpSCJSpecific;

	protected ISelection selection;
	
	public SharedWizardPage(String pageName, ISelection selection) {
		super(pageName);
		
		this.selection = selection;
	}
	
	@Override
	public void createControl(Composite composite) {	
		composite.setLayout(new GridLayout(4, false));
		
		Label lblSourceFolder = new Label(composite, SWT.NONE);
		GridData gridData = new GridData();
		lblSourceFolder.setLayoutData(gridData);
		lblSourceFolder.setText("Source folder:");
		
		txtSourceFolder = new Text(composite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		txtSourceFolder.setLayoutData(gridData);
		txtSourceFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		btnBrowseSourceFolder = new Button(composite, SWT.NONE);
		btnBrowseSourceFolder.setText("Browse...");
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		btnBrowseSourceFolder.setLayoutData(gridData);
		btnBrowseSourceFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseSourceFolder();
			}
		});
		
		Label lblPackage = new Label(composite, SWT.NONE);
		gridData = new GridData();
		lblPackage.setLayoutData(gridData);
		lblPackage.setBounds(10, 50, 59, 14);
		lblPackage.setText("Package:");
		
		txtPackage = new Text(composite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		txtPackage.setLayoutData(gridData);
		txtPackage.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		lblDefault = new Label(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.CENTER;
		lblDefault.setLayoutData(gridData);
		lblDefault.setText("(default)");
		
		btnBrowsePackage = new Button(composite, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		btnBrowsePackage.setLayoutData(gridData);
		btnBrowsePackage.setEnabled(false);
		btnBrowsePackage.setBounds(492, 46, 94, 28);
		btnBrowsePackage.setText("Browse...");
		btnBrowsePackage.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowsePackage();
			}
		});

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 4;
		label.setLayoutData(gridData);
		label.setBounds(10, 80, 570, 2);
		
		Label lblName = new Label(composite, SWT.NONE);
		gridData = new GridData();
		lblName.setLayoutData(gridData);
		lblName.setBounds(10, 105, 59, 14);
		lblName.setText("Name:");
			
		txtName = new Text(composite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		txtName.setLayoutData(gridData);
		txtName.setBounds(95, 102, 391, 19);
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		gridData = new GridData();
		new Label(composite, SWT.NULL).setLayoutData(gridData);
		gridData = new GridData();
		new Label(composite, SWT.NULL).setLayoutData(gridData);
		
		grpSCJSpecific = new Group(composite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 4;
		grpSCJSpecific.setLayoutData(gridData);
		grpSCJSpecific.setText("Safety-Critical Java Specific");
	}
	
	protected void initialize() {
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
	}
	
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
	
	protected void dialogChanged() {	
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
	
	public String getSourceFolderName() {
		return txtSourceFolder.getText();
	}
	
	public String getPackageName() {
		return txtPackage.getText();
	}
	
	public String getFileName() {
		return txtName.getText();
	}
}
