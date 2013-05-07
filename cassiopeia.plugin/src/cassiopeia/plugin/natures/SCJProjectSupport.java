package cassiopeia.plugin.natures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import cassiopeia.plugin.wizards.pages.tree.Model.SourceFolder;

public class SCJProjectSupport {
	
	private IProject project;
	private IProjectDescription description;
	private IProgressMonitor monitor;
	
	private List<IClasspathEntry> classpath;
	
	private IPath projectPath;
	private IPath srcFolderPath;
	
	public SCJProjectSupport(IProject project, IProjectDescription description, IProgressMonitor monitor) {
		this.project = project;
		this.description = description;
		this.monitor = monitor;
		classpath = new ArrayList<IClasspathEntry>();
	}
	
	public void createJavaProject(List<SourceFolder> folders) throws CoreException {
		try {
			monitor.beginTask("", 2000);
			
			project.create(description, monitor);
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
			
			projectPath = project.getFullPath();
			
			createLinkedSourceFolders(folders);
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
}
