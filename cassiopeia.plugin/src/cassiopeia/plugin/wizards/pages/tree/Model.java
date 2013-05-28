package cassiopeia.plugin.wizards.pages.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

public class Model {
	
	private LibraryCategory libraryCategory;
	private List<SourceFolder> folders;

	public boolean empty;
	
	public Model() {
		libraryCategory = new LibraryCategory();
		folders = new ArrayList<SourceFolder>();
		
		empty = true;
	}
	
	public void addSourceFolder(SourceFolder folder) {
		for(SourceFolder f : folders) {
			if(f.getName().equals(folder.getName()))
				return;
		}
		empty = false;
		folders.add(folder);
	}
	
	public void addLibrary(Jar jar) {
		for(Jar j : libraryCategory.children) {
			if(j.getName().equals(jar.getName()))
				return;
		}
		empty = false;
		libraryCategory.children.add(jar);
	}
	
	public LibraryCategory getLibraryCategory() {
		return libraryCategory;
	}
	
	public List<SourceFolder> getSourceFolders() {
		return folders;
	}
	
	
	public List<TreeItem> getElements() {
		List<TreeItem> elements = new ArrayList<TreeItem>();
		elements.addAll(folders);
		elements.add(libraryCategory);
		
		return elements;
	}
	
	public class TreeItem {
		private String name;
		private Image image;
		private IPath path;
		
		public Image getImage() {
			return image;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
		
		public String getName() {
		    return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void setPath(String path) {
			this.path = new Path(path);
		}
		
		public IPath getPath() {
			return path;
		}
	}
	
	public class Jar extends TreeItem {
		public Jar() {
			ISharedImages images = JavaUI.getSharedImages();
			Image img = images.getImage(ISharedImages.IMG_OBJS_JAR);
			super.setImage(img);
		}
	}
	
	public class SourceFolder extends TreeItem {
		public SourceFolder() {
			//ISharedImages images = JavaUI.getSharedImages();
			org.eclipse.ui.ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
			Image img = images.getImage(org.eclipse.ui.ISharedImages.IMG_OBJ_FOLDER);
			super.setImage(img);
		}
	}

	public class Category<T extends TreeItem> extends TreeItem {
		public List<T> children;
		
		public Category(ArrayList<T> children) {
			this.children = children;
		}
	}
	
	public class LibraryCategory extends Category<Jar> {
		public LibraryCategory() {
			super(new ArrayList<Jar>());
		}
	}
}