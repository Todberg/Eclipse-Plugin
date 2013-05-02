package cassiopeia.plugin.wizards.templates;

import cassiopeia.plugin.wizards.templates.models.*;

public class APeriodicEventHandlerTemplate
{
  protected static String nl;
  public static synchronized APeriodicEventHandlerTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    APeriodicEventHandlerTemplate result = new APeriodicEventHandlerTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "public class ";
  protected final String TEXT_2 = " extends AperiodicEventHandler  {" + NL + "" + NL + "\tpublic ";
  protected final String TEXT_3 = "(PriorityParameters priority, AperiodicParameters release, StorageParameters storage, long scopeSize) {" + NL + "\t\tsuper(priority, release, storage, scopeSize);" + NL + "\t\t// TODO Auto-generated constructor stub" + NL + "\t}" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\tpublic void handleAsyncEvent() {" + NL + "\t\t// TODO Auto-generated method stub\t" + NL + "\t}" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     EventHandlerModel model = (EventHandlerModel) argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
