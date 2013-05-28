package cassiopeia.plugin.wizards.pages;

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import cassiopeia.plugin.misc.SafeletData;
import cassiopeia.plugin.wizards.pages.tree.Model;
import cassiopeia.plugin.wizards.pages.tree.Model.Jar;
import cassiopeia.plugin.wizards.pages.tree.Model.LibraryCategory;
import cassiopeia.plugin.wizards.pages.tree.Model.SourceFolder;
import cassiopeia.plugin.wizards.pages.tree.TreeViewerContentProvider;
import cassiopeia.plugin.wizards.pages.tree.TreeViewerLabelProvider;

public class SCJProjectWizardPage extends WizardNewProjectCreationPage {

	private final Model model = new Model();
	
	/* SWT treeViewer widget */
	private TreeViewer treeViewer;
	
	/* SWT button widgets */
	private Button btnRadioLevel0;
	private Button btnRadioLevel1;
	private Button btnRadioLevel2;
	private Button btnSafletGen;
	
	/* SWT label widget */
	private Text txtSafeletName;
	
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
		grpSCJParams.setLayoutData(new GridData(GridData.FILL_BOTH));		
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = SWT.FILL;
		gridLayout.horizontalSpacing = SWT.FILL;
		grpSCJParams.setLayout(gridLayout);

		Composite topComposite = new Composite(grpSCJParams, SWT.NULL);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		topComposite.setLayoutData(gridData);
		gridLayout = new GridLayout(6, false);
		gridLayout.horizontalSpacing = SWT.FILL;
		topComposite.setLayout(gridLayout);
		
		btnSafletGen = new Button(topComposite, SWT.CHECK);
		btnSafletGen.setText("Generate Safelet");
		btnSafletGen.setSelection(true);
		btnSafletGen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				toggleSafeletCheckBoxSelection();
				validatePage();
			}
		});
		
		gridData = new GridData();
		gridData.verticalSpan = 2;
		btnSafletGen.setLayoutData(gridData);
		
		Label seperatorVertical = new Label(topComposite, SWT.SEPARATOR | SWT.VERTICAL);
		gridData = new GridData();
		gridData.verticalSpan = 2;
		seperatorVertical.setLayoutData(gridData);
		
		Label lblComplianceLevel = new Label(topComposite, SWT.NONE);
		lblComplianceLevel.setText("Compliance level:");
		
		btnRadioLevel0 = new Button(topComposite, SWT.RADIO);
		btnRadioLevel0.setText("Level 0");
	
		btnRadioLevel1 = new Button(topComposite, SWT.RADIO);
		btnRadioLevel1.setText("Level 1");
		btnRadioLevel1.setSelection(true);
		
		btnRadioLevel2 = new Button(topComposite, SWT.RADIO);
		btnRadioLevel2.setText("Level 2");

		Label lblSafeletName = new Label(topComposite, SWT.NONE);
		lblSafeletName.setText("Safelet name:");
		
		txtSafeletName = new Text(topComposite, SWT.NULL);
		txtSafeletName.setText("MySafelet");
		txtSafeletName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validatePage();
			}
		});
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		txtSafeletName.setLayoutData(gridData);

		treeViewer = new TreeViewer(grpSCJParams, SWT.BORDER);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		treeViewer.setLabelProvider(new TreeViewerLabelProvider());
		treeViewer.setContentProvider(new TreeViewerContentProvider());
		Tree tree = treeViewer.getTree();
		gridData = new GridData();
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		tree.setLayoutData(gridData);
		tree.addSelectionListener(new SelectionAdapter() {
			  @Override
			  public void widgetSelected(SelectionEvent e) {
			    TreeItem item = (TreeItem) e.item;
			      if (item.getItemCount() > 0) {
			        item.setExpanded(!item.getExpanded());
			      }
			    }
			});	
		treeViewer.setInput(model);
		
		Composite rightComposite = new Composite(grpSCJParams, SWT.NULL);
		rightComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		rightComposite.setLayout(new GridLayout(2, false));
				
		Label lblDescription = new Label(rightComposite, SWT.NULL);
		lblDescription.setText("Select SCJ source(s):");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		lblDescription.setLayoutData(gridData);
		
		Button btnBrowseSourceFolder = new Button(rightComposite, SWT.NULL);
		btnBrowseSourceFolder.setText("Link source folder");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		btnBrowseSourceFolder.setLayoutData(gridData);
		btnBrowseSourceFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseFolder();
				validatePage();
			}
		});

		Button btnBrowseJar = new Button(rightComposite, SWT.NULL);
		btnBrowseJar.setText("Add external JAR");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		btnBrowseJar.setLayoutData(gridData);
		btnBrowseJar.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseJAR();
				validatePage();
			}
		});
		
		validatePage();
	}
	
	private void handleBrowseFolder() {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setMessage("Select SCJ source folder");
		String path = dialog.open();
		if(path != null) {
			SourceFolder folder = model.new SourceFolder();
			folder.setName(path.substring(path.lastIndexOf('/') + 1));
			folder.setPath(path);
			model.addSourceFolder(folder);
			
			treeViewer.setInput(model);
		}
	}
	
	private void handleBrowseJAR() {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setFilterExtensions(new String[] { "*.jar"} );
		String path = dialog.open();
		if(path != null) {
			Jar jar = model.new Jar();
			jar.setName(path.substring(path.lastIndexOf('/') + 1));
			jar.setPath(path);
			model.addLibrary(jar);
			
			treeViewer.setInput(model);
		}
	}
	
	private void toggleSafeletCheckBoxSelection() {
		if(btnSafletGen.getSelection()) {
			txtSafeletName.setEnabled(true);
			btnRadioLevel0.setEnabled(true);
			btnRadioLevel1.setEnabled(true);
			btnRadioLevel2.setEnabled(true);
		} else {
			txtSafeletName.setEnabled(false);
			btnRadioLevel0.setEnabled(false);
			btnRadioLevel1.setEnabled(false);
			btnRadioLevel2.setEnabled(false);
		}
	}
	
	@Override
	protected boolean validatePage() {
		boolean valid = super.validatePage();
		if(!valid) {
			return false;
		}
		
		if(!validateSafeletFileName())
			return false;
		if(!validateSources())
			return false;
			
		setErrorMessage(null);
        setMessage(null);
        setPageComplete(true);
        return true;
	}
	
	private boolean validateSources() {
		if(model.empty) {
			updateStatus("SCJ source(s) must be specified.");
			return false;
		}
		return true;
	}
	
	private boolean validateSafeletFileName() {
		if(btnSafletGen.getSelection()) {
			String fileName = txtSafeletName.getText();
			
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
		}
		
		return true;
	}
	
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
	
	public LibraryCategory getLibraryCategory() {
		return model.getLibraryCategory();
	}

	public List<SourceFolder> getSourceFolders() {
		return model.getSourceFolders();
	}
			
	public SafeletData getSafeletData() {
		if(btnSafletGen.getSelection()) {
			String name;
			final int level;
			
			if(btnRadioLevel0.getSelection())
				level = 0;
			else if(btnRadioLevel1.getSelection())
				level = 1;
			else
				level = 2;
				
			name = txtSafeletName.getText();
			if(name.lastIndexOf('.') != -1) {
				name = name.substring(0, name.indexOf('.'));
			}
			
			SafeletData safeletData = new SafeletData();
			safeletData.name = name;
			safeletData.complianceLevel = level;
			
			return safeletData;
		}
		return null;
	}
}
