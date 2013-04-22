package cassiopeia.plugin.views;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import cassiopeia.plugin.Activator;
import cassiopeia.plugin.listeners.PartListener;
import cassiopeia.plugin.listeners.SelectionListenerJavaEditor;

import com.jopdesign.common.AppSetup;
import com.jopdesign.common.config.BooleanOption;
import com.jopdesign.common.config.Option;
import com.jopdesign.common.config.StringOption;
import com.jopdesign.dfa.DFATool;
import com.jopdesign.wcet.WCETTool;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class AnalysisView extends ViewPart {

	public static AnalysisView view;
	
	public JavaEditor javaEditor;
	public IWorkbenchPage workbenchPage;
	
	public AnalysisView() {
		registerSelectionListener();
		view = this;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		
	}

	@Override
	public void setFocus() {
		
	}
	
	public void updateViewCallback(IJavaElement element) {
		setContentDescription("IJavaElement: " + element.toString() + " Name: " + element.getElementName());
		
		IJavaProject javaProject = element.getJavaProject();
		IProject project = (IProject)((IAdaptable)javaProject).getAdapter(IProject.class);
		
		IPath projectPath = project.getLocation();

		Bundle bundle = null;
		
		StringBuilder cp = null;
		StringBuilder sp = null;
		try {
			String pluginPath = new File(".").getCanonicalPath();
			
			// Classpath
			cp = new StringBuilder();
			cp.append(projectPath + "/bin:");
	
			bundle = Platform.getBundle(Activator.PLUGIN_ID);
			URL jopJar = bundle.getEntry("libs/jop.jar");
			URL jopJarFile = org.eclipse.core.runtime.FileLocator.toFileURL(jopJar);
			cp.append(jopJarFile);
			
			org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("common"));
			
			// Sourcepath
			sp = new StringBuilder();
			
			//sp.append(pluginPath + "/common:");
			//sp.append(pluginPath + "/jdk_base:");
			//sp.append(pluginPath + "/jdk11:");
			//sp.append(pluginPath + "/rtapi:");
			//sp.append(pluginPath + "/test:");
			//sp.append(pluginPath + "/bench:");
			//sp.append(pluginPath + "/app");
			
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("common")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jdk_base")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("jdk11")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("rtapi")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("test")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("bench")) + ":");
			sp.append(org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("app")) + ":");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] args = new String[] {
			"--classpath",
			cp.toString(),
			"--sp",
			sp.toString(),
			"--target-method",
			"main",
			"-o",
			"/Users/Todberg/Desktop/out",
			"--use-dfa",
			"yes",
			"--uppaal",
			"no",
			"--uppaal-verifier",
			"verifyta",
			"--callstring-length",
			"0",
			"Program"
		};
		
		for(String arg : args) {
			System.out.println("arg: " + arg);
		}

		Properties defaultProperties = new Properties();
		defaultProperties.put("outdir", "/Users/Todberg/Desktop/out");
		AppSetup setup = new AppSetup(defaultProperties, false);
		
		setup.setVersionInfo("1.0.1");
		setup.setUsageInfo("WCETAnalysis", "WCET Analysis tool");
		
		
        WCETTool wcetTool = new WCETTool();        
        DFATool dfaTool = new DFATool();

        setup.registerTool("dfa", dfaTool, true, false);
        setup.registerTool("wcet", wcetTool);
        setup.addSourceLineOptions(false);

        
        try {
        	
        	StringOption option = new StringOption("jop-asm-file", "JOP assembly file",
					org.eclipse.core.runtime.FileLocator.toFileURL(bundle.getEntry("misc")).toString() + "jvmgen.asm");
			setup.getConfig().getOptions().getGroup("jop").addOption(option);
			
			BooleanOption boption = new BooleanOption("report-generation", "gen", false);
			setup.getConfig().getOptions().addOption(boption);
			
			for(Option opt : setup.getConfig().getOptions().getOptions()) {
				System.out.println("Key: " + opt.getKey() + " Value: " + opt.getDefaultValue());
			}
			
			
			System.out.println(setup.getConfig().getOptions().getGroup("jop").getOptionSpec("jop-asm-file").getDefaultValue());
			
			setup.getConfig().addOption(new BooleanOption("report-generation", "gen reports", false));
			//setup.getConfig().getOptions().addOption(new BooleanOption("report-generation", "generate reports", false), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        try {
        setup.initAndLoad(args, true, false, false);
        }catch(Exception e) {
        	System.out.println(setup.getConfig().getOptions().getGroup("jop").getOptionSpec("jop-asm-file").getDefaultValue());
			System.exit(1);
        }

        if (setup.useTool("dfa")) {
           wcetTool.setDfaTool(dfaTool);
        }
	}
	
	private void registerSelectionListener() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPartSite site = null;
				while (site == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					site = getSite();
				}
				IWorkbenchWindow window = site.getWorkbenchWindow();
				workbenchPage = window.getActivePage();
				workbenchPage.addPartListener(PartListener.getListener());
				
				IEditorPart editorPart = workbenchPage.getActiveEditor();	
				javaEditor = (JavaEditor)editorPart;
				IEditorSite editorSite = javaEditor.getEditorSite();
				
				if(editorSite != null) {
					editorSite.getSelectionProvider().addSelectionChangedListener(SelectionListenerJavaEditor.getListener());
				}
			}
		}).start();
	}
	
	@Override
	public void dispose() {
		javaEditor.getEditorSite().getSelectionProvider().removeSelectionChangedListener(
				SelectionListenerJavaEditor.getListener());
		workbenchPage.removePartListener(PartListener.getListener());
		super.dispose();
	}
}