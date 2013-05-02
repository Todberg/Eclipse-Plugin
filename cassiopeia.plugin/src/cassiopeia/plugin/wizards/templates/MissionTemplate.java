package cassiopeia.plugin.wizards.templates;

import cassiopeia.plugin.wizards.templates.models.*;

public class MissionTemplate
{
  protected static String nl;
  public static synchronized MissionTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    MissionTemplate result = new MissionTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "public class ";
  protected final String TEXT_3 = " extends Mission {" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed(Level.SUPPORT)" + NL + "\tprotected void initialize() {" + NL + "\t\t// TODO Auto-generated method stub" + NL + "\t}" + NL + "" + NL + "\t@Override" + NL + "\t@SCJAllowed" + NL + "\tpublic long missionMemorySize() {" + NL + "\t\t// TODO Auto-generated method stub" + NL + "\t\treturn 0;" + NL + "\t}" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     MissionModel model = (MissionModel) argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(model.packageDeclaration);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(model.name);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
