package cassiopeia.plugin.listeners;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import cassiopeia.plugin.views.AnalysisView;

public class SelectionListenerJavaEditor implements ISelectionChangedListener {
	
	private AnalysisView analysisView;
	private static SelectionListenerJavaEditor listener;
		
	private SelectionListenerJavaEditor() { 
		analysisView = AnalysisView.view;
	}
	
	public static SelectionListenerJavaEditor getListener() {
		if(listener == null) {
			listener = new SelectionListenerJavaEditor();
		}
		return listener;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart activeEditor = page.getActiveEditor();
		
		ICompilationUnit root = (ICompilationUnit)EditorUtility.getEditorInputJavaElement(activeEditor, false);
		TextSelection textSelection = (TextSelection)event.getSelection();
		try {
			IJavaElement element = root.getElementAt(textSelection.getOffset());
			if(element != null)
				analysisView.updateViewCallback(element);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
}
