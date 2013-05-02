package cassiopeia.plugin.wizards.template;

public class safelettemplate
{
  protected static String nl;
  public static synchronized safelettemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    safelettemplate result = new safelettemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "Hello Safelet";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    return stringBuffer.toString();
  }
}
