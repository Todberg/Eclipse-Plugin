package cassiopeia.plugin.natures;

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

public class SCJProjectSupport {
	
	public void createJavaProject(IProject project, IProjectDescription description, IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask("", 2000);
			
			project.create(description, monitor);
			project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));
			
			IPath srcFolderPath = new Path("src");
			IFolder srcFolder = project.getFolder(srcFolderPath);
			srcFolder.create(true, true, monitor);
			
			IPath binFolderPath = new Path("bin");
			IFolder binFolder = project.getFolder(binFolderPath);
			binFolder.create(IResource.FORCE | IResource.DERIVED, true, monitor);
			binFolder.setDerived(true, monitor);
			
			IClasspathEntry classpathEntry = JavaCore.newSourceEntry(project.getFullPath().append(srcFolderPath));
			
			IJavaProject javaProject = JavaCore.create(project);
			addJavaNature(project, monitor);
			
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			javaProject.setRawClasspath(new IClasspathEntry[] { classpathEntry }, monitor);
			
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
}
