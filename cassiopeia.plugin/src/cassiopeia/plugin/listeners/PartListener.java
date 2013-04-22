package cassiopeia.plugin.listeners;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;

public class PartListener implements IPartListener {

	private static PartListener listener;
		
	private PartListener() { }
	
	public static PartListener getListener() {
		if(listener == null) {
			listener = new PartListener();
		}
		return listener;
	}
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if(part instanceof JavaEditor) {
			removeEditorSelectionListener((JavaEditor)part);
		}
	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		if(part instanceof JavaEditor) {
			addEditorSelectionListener((JavaEditor)part);
		}
	}
	
	/* Register the editors selection listener */
	private void addEditorSelectionListener(JavaEditor javaEditor) {
		ISelectionProvider provider = getSelectionProvider(javaEditor);
		provider.addSelectionChangedListener(SelectionListenerJavaEditor.getListener());
	}
	
	/* Unregister the editors selection listener */
	private void removeEditorSelectionListener(JavaEditor javaEditor) {
		ISelectionProvider provider = getSelectionProvider(javaEditor);
		provider.removeSelectionChangedListener(SelectionListenerJavaEditor.getListener());
	}
	
	/* Get selection listener from Java editor */
	private ISelectionProvider getSelectionProvider(JavaEditor javaEditor) {
		IEditorSite editorSite = javaEditor.getEditorSite();
		return editorSite.getSelectionProvider();
	}
}
