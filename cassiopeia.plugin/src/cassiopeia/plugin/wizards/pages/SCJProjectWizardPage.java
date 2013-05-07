package cassiopeia.plugin.wizards.pages;

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import cassiopeia.plugin.wizards.pages.tree.Model;
import cassiopeia.plugin.wizards.pages.tree.Model.Jar;
import cassiopeia.plugin.wizards.pages.tree.Model.LibraryCategory;
import cassiopeia.plugin.wizards.pages.tree.Model.SourceFolder;
import cassiopeia.plugin.wizards.pages.tree.TreeViewerContentProvider;
import cassiopeia.plugin.wizards.pages.tree.TreeViewerLabelProvider;

public class SCJProjectWizardPage extends WizardNewProjectCreationPage {

	private final Model model = new Model();
	
	private TreeViewer treeViewer;
	
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
		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.verticalSpacing = SWT.FILL;
		gridLayout.horizontalSpacing = SWT.FILL;
		gridLayout.numColumns = 2;
		
		grpSCJParams.setLayout(gridLayout);

		treeViewer = new TreeViewer(grpSCJParams, SWT.BORDER);
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		treeViewer.setLabelProvider(new TreeViewerLabelProvider());
		treeViewer.setContentProvider(new TreeViewerContentProvider());
		Tree tree = treeViewer.getTree();
		GridData gridData = new GridData();
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
	
	@Override
	protected boolean validatePage() {
		boolean valid = super.validatePage();
		if(!valid) {
			return false;
		}
		
		if(model.empty) {
			setErrorMessage(null);
	        setMessage("SCJ source(s) must be specified.");
	        return false;
		}
		
		setErrorMessage(null);
        setMessage(null);
        setPageComplete(true);
        return true;
	}
	
	public LibraryCategory getLibraryCategory() {
		return model.getLibraryCategory();
	}

	public List<SourceFolder> getSourceFolders() {
		return model.getSourceFolders();
	}
}
