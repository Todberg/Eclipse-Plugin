package cassiopeia.plugin.wizards.templates;

import cassiopeia.plugin.wizards.templates.models.*;

public class PeriodicEventHandlerTemplate
{
  protected static String nl;
  public static synchronized PeriodicEventHandlerTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    PeriodicEventHandlerTemplate result = new PeriodicEventHandlerTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "public class ";
  protected final String TEXT_2 = " extends PeriodicEventHandler  {" + NL + "" + NL + "\tpublic ";
  protected final String TEXT_3 = "(PriorityParameters priority, PeriodicParameters release, StorageParameters storage, long scopeSize, String name) {" + NL + "\t\tsuper(priority, release, storage, scopeSize, name);" + NL + "\t\t// TODO Auto-generated constructor stub" + NL + "\t}" + NL + "\t" + NL + "\tpublic ";
  protected final String TEXT_4 = "(PriorityParameters priority, PeriodicParameters release, StorageParameters storage, long scopeSize) {" + NL + "\t\tsuper(priority, release, storage, scopeSize);" + NL + "\t\t// TODO Auto-generated constructor stub" + NL + "\t}" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\t// TODO Auto-generated method stub\t" + NL + "\t}" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     EventHandlerModel model = (EventHandlerModel) argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
