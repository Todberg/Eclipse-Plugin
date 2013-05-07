package cassiopeia.plugin.wizards.pages.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import cassiopeia.plugin.wizards.pages.tree.Model.Category;

public class TreeViewerContentProvider implements ITreeContentProvider {

	private Model model;
	
	@Override
	public void dispose() {	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
		this.model = (Model)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getElements().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Category<?>) {
			Category<?> category = (Category<?>)parentElement;
			return category.children.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof Category<?> ? true : false);
	}
}	
