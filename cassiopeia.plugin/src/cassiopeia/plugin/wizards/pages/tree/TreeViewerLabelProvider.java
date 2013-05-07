package cassiopeia.plugin.wizards.pages.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import cassiopeia.plugin.wizards.pages.tree.Model.TreeItem;

public class TreeViewerLabelProvider extends LabelProvider {
	
	@Override
	public Image getImage(Object element) {
		TreeItem item = (TreeItem)element;
		return item.getImage();
	}

	@Override
	public String getText(Object element) {
		TreeItem item = (TreeItem)element;
		return item.getName();
	}
}
