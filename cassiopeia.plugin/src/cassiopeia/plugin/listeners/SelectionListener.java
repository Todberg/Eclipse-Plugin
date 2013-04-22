package cassiopeia.plugin.listeners;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import cassiopeia.plugin.views.AnalysisView;

public class SelectionListener implements ISelectionListener {
	
	private AnalysisView analysisView;
	private static SelectionListener listener;
		
	private SelectionListener() { 
		analysisView = AnalysisView.view;
	}
	
	public static SelectionListener getListener() {
		if(listener == null) {
			listener = new SelectionListener();
		}
		return listener;
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		System.out.println("INVOKE " + part.toString());
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		
		//TextEditor e;
		//IWorkbenchPartSite site = part.getSite();
		
		
		
		IEditorPart activeEditor = page.getActiveEditor();
				
		if(activeEditor instanceof JavaEditor) {
			if(selection instanceof TextSelection) {
				ICompilationUnit root = (ICompilationUnit)EditorUtility.getEditorInputJavaElement(activeEditor, false);
				TextSelection textSelection = (TextSelection)selection;
				try {
					IJavaElement element = root.getElementAt(textSelection.getOffset());
					if(element != null)
						analysisView.updateViewCallback(element);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
