package cassiopeia.plugin.wizards;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import cassiopeia.plugin.misc.SafeletData;
import cassiopeia.plugin.natures.SCJProjectSupport;
import cassiopeia.plugin.wizards.pages.SCJProjectWizardPage;
import cassiopeia.plugin.wizards.pages.tree.Model.LibraryCategory;
import cassiopeia.plugin.wizards.pages.tree.Model.SourceFolder;

public class SCJProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private SCJProjectWizardPage page;

	private IWorkbench workbench;
	private IConfigurationElement config;
	private IStructuredSelection selection;
	
	public SCJProjectWizard() {
		super();
	}
	
	@Override
	public void addPages() {
		page = new SCJProjectWizardPage("SCJProjectWizard");
		addPage(page);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {	
		final IProject project = page.getProjectHandle();
		
		final LibraryCategory libraryCategory = page.getLibraryCategory();
		final List<SourceFolder> folders = page.getSourceFolders();
		final SafeletData safeletData = page.getSafeletData();
		final boolean useBundled = page.getUseBundled();
		
		URI projectLocation = (!page.useDefaults() ? page.getLocationURI() : null);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProjectDescription description = workspace.newProjectDescription(project.getName());
		description.setLocationURI(projectLocation);
		
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            protected void execute(IProgressMonitor monitor) throws CoreException {
            	 SCJProjectSupport support = new SCJProjectSupport(getShell(), project, description, monitor);
                 support.createJavaProject(safeletData, folders, useBundled);
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        
        BasicNewProjectResourceWizard.updatePerspective(config);
        if(SCJProjectSupport.safeletResource != null) {
        	BasicNewProjectResourceWizard.selectAndReveal(SCJProjectSupport.safeletResource, workbench.getActiveWorkbenchWindow());
        	SCJProjectSupport.safeletResource = null;
        }
        else
        	BasicNewProjectResourceWizard.selectAndReveal(project, workbench.getActiveWorkbenchWindow());

        return true;
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		this.config = config;
	}
}
