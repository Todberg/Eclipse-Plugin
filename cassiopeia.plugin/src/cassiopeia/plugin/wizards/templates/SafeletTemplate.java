package cassiopeia.plugin.wizards.templates;

import cassiopeia.plugin.wizards.templates.models.*;

public class SafeletTemplate
{
  protected static String nl;
  public static synchronized SafeletTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SafeletTemplate result = new SafeletTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "public class ";
  protected final String TEXT_3 = " implements Safelet<";
  protected final String TEXT_4 = "> {" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\t@SCJRestricted(phase = Phase.INITIALIZATION)" + NL + "\tpublic void initializeApplication() {" + NL + "\t\t// TODO Auto-generated method stub" + NL + "\t}" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\t@SCJRestricted(phase = Phase.INITIALIZATION)" + NL + "\tpublic MissionSequencer<";
  protected final String TEXT_5 = "> getSequencer() {" + NL + "\t\t// TODO Auto-generated method stub" + NL + "\t\treturn null;" + NL + "\t}" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\tpublic long immortalMemorySize() {" + NL + "\t\t// TODO Auto-generated method stub" + NL + "\t\treturn 0;" + NL + "\t}" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     SafeletModel model = (SafeletModel) argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(model.packageDeclaration);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(model.safeletTypeParameter);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(model.safeletTypeParameter);
    stringBuffer.append(TEXT_5);
    return stringBuffer.toString();
  }
}
