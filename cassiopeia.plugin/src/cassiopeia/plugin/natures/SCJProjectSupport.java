package cassiopeia.plugin.natures;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import cassiopeia.plugin.misc.SafeletData;
import cassiopeia.plugin.wizards.pages.tree.Model.SourceFolder;
import cassiopeia.plugin.wizards.templates.SafeletTemplate;
import cassiopeia.plugin.wizards.templates.models.SafeletModel;

public class SCJProjectSupport {
	
	private Shell shell;
	
	public static IResource safeletResource;
	
	private IProject project;
	private IProjectDescription description;
	private IProgressMonitor monitor;
	
	private List<IClasspathEntry> classpath;
	
	private IPath projectPath;
	private IPath srcFolderPath;

	public SCJProjectSupport(Shell shell, IProject project, IProjectDescription description, IProgressMonitor monitor) {
		this.shell = shell;
		this.project = project;
		this.description = description;
		this.monitor = monitor;
		classpath = new ArrayList<IClasspathEntry>();
	}
	
	public void createJavaProject(SafeletData safeletData, List<SourceFolder> folders, boolean useBundled) throws CoreException {
		try {
			monitor.beginTask("", 2000);
			
			project.create(description, monitor);
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
			
			projectPath = project.getFullPath();
			
			if(useBundled) {
				createLibrariesFolder();
				//Bundle bundle = Platform.getBundle( "cassiopeia.plugin" );
				//InputStream stream = FileLocator.openStream( bundle, "path.in.plugin", false );
				//classpath.add(JavaCore.newLibraryEntry(path, null, null));
			}
			else {
				createLinkedSourceFolders(folders);
			}
			
			createSrcFolder();
			createBinFolder();
			
			IJavaProject javaProject = JavaCore.create(project);
			addJavaNature(project, monitor);

			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			IClasspathEntry[] cp = new IClasspathEntry[classpath.size()];
			for(int i = 0; i < classpath.size(); i++) {
				cp[i] = classpath.get(i);
			}
			javaProject.setRawClasspath(cp, monitor);

			if(safeletData != null) {
				safeletResource = createSafelet(safeletData);
			}
				
		}catch(Exception e){
			IStatus status = new Status(IStatus.ERROR, "SCJ Project Wizard", IStatus.OK, e.getLocalizedMessage(), null);
            throw new CoreException(status);
		}finally {
			monitor.done();
		}
	}
	
	private void addJavaNature(IProject project, IProgressMonitor monitor) {
		try {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = JavaCore.NATURE_ID;
            
            description.setNatureIds(newNatures);
            project.setDescription(description, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void createLibrariesFolder() throws CoreException {
		IFolder iFolder = project.getFolder("Libraries");
		iFolder.create(true, true, monitor);
		
	}
	
	private void createLinkedSourceFolders(List<SourceFolder> folders) throws CoreException {
		if(folders.size() > 0) {
			for(SourceFolder linkedSourceFolder : folders) {
				IFolder iFolder = project.getFolder(linkedSourceFolder.getName());
				iFolder.createLink(linkedSourceFolder.getPath(), IResource.ALLOW_MISSING_LOCAL, monitor);
				classpath.add(JavaCore.newSourceEntry(projectPath.append(new Path(linkedSourceFolder.getName()))));
			}
		}
	}
	
	private void createSrcFolder() throws CoreException {
		srcFolderPath = new Path("src");
		IFolder srcFolder = project.getFolder(srcFolderPath);
		srcFolder.create(true, true, monitor);
		classpath.add(JavaCore.newSourceEntry(projectPath.append(srcFolderPath)));
	}
	
	private void createBinFolder() throws CoreException {
		IPath binFolderPath = new Path("bin");
		IFolder binFolder = project.getFolder(binFolderPath);
		binFolder.create(IResource.FORCE | IResource.DERIVED, true, monitor);
		binFolder.setDerived(true, monitor);
	}
	
	private IResource createSafelet(SafeletData safeletData) {
		String containerName = project.getName() + "/src/";
		String fileName = safeletData.name + ".java";

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource srcFolderResource = root.findMember(new Path(containerName));
		IContainer container = (IContainer) srcFolderResource;	
		final IFile file = container.getFile(new Path(fileName));
	
		try {
			InputStream stream = openContentStream(safeletData);
			file.create(stream, true, monitor);
			stream.close();
		} catch (IOException e1) { } 
		catch(CoreException e2) { }
		
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
		
		return root.findMember(new Path(containerName + fileName));
	}

	private InputStream openContentStream(SafeletData safeletData) {
		SafeletTemplate template = new SafeletTemplate();
		SafeletModel model = new SafeletModel();
		
		model.name = safeletData.name;
		
		switch(safeletData.complianceLevel) {
			case 0:
				model.safeletTypeParameter = "CyclicExecutive";
				break;
			case 1:
			case 2:
				model.safeletTypeParameter = "Mission";
				break;
		}
		return new ByteArrayInputStream(template.generate(model).getBytes());
	}
}
