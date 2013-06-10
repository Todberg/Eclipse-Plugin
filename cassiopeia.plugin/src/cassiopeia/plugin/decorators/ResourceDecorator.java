package cassiopeia.plugin.decorators;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class ResourceDecorator implements ILabelDecorator {

	public ResourceDecorator() {
		super();
	}
	
	@Override
	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub		
	}
}
