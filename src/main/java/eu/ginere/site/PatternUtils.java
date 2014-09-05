package eu.ginere.site;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternUtils{

	public static final Pattern QUOTED_STRING=Pattern.compile("^\"(.+)\"$");
	public static final Pattern SIMPLE_QUOTED_STRING=Pattern.compile("^\'(.+)\'$");
	
	/**
	 * Remove quotes for simple a doubled quoted String. Ex: "dummy" or 'dummy'
	 * 
	 * @param value to remove quotes
	 * 
	 * @return The unquoted string
	 */
	public static String unScapeFromBathPropFiles(String value){
		if (value == null || "".equals(value)){
			return value;
		} else {
			Matcher matcher = QUOTED_STRING.matcher(value);			
			if (matcher.find()){
				return matcher.group(1);
			}
			
			matcher = SIMPLE_QUOTED_STRING.matcher(value);			
			if (matcher.find()){
				return matcher.group(1);
			}
			
			return value;
		}
	}


	// Regular Expresion for variables \|([a-zA-Z][a-zA-Z0-9_]+)\|, Variables are like |Variable|
	//	public static final Pattern VARIABLE_TOKEN_PATER=Pattern.compile("\\|([a-zA-Z][a-zA-Z0-9_]+)\\|");
	public static final Pattern VARIABLE_TOKEN_PATER=Pattern.compile("\\|([a-zA-Z0-9_]+)\\|");

	// FILES 
	//	public static final Pattern FILE_TOKEN_PATER=Pattern.compile("\\{([a-zA-Z/][a-zA-Z0-9_./-]+)\\}");
	public static final Pattern FILE_TOKEN_PATER=Pattern.compile("\\{([a-zA-Z0-9_./-]+)\\}");

	// DIRS
	//	public static final Pattern LIST_TOKEN_PATER=Pattern.compile("\\{(DIRS|DIR)(\\[([a-zA-Z/][a-zA-Z0-9_./-]+)\\])?:([a-zA-Z/][a-zA-Z0-9_./-]+)\\}");
	// Ex: {DIRS[/path]:/lib/header.html} or {DIR[/path]:/lib/header.html} ../ this will pply the lib file over the content dir.
	public static final Pattern LIST_TOKEN_PATER=Pattern.compile("\\{(DIRS|DIR|LINKS)(\\[([a-zA-Z0-9_./-]+)\\])?:([a-zA-Z/][a-zA-Z0-9_./-]+)\\}");
	public static final String DIR = "DIR";
	public static final String DIRS = "DIRS";
	public static final String LINKS = "LINKS";	
	
	// Like |DATE[ddmmYY]|
	public static final Pattern DATE_TOKEN_PATER=Pattern.compile("\\|DATE\\[([a-zA-Z0-9:\\- /]*)\\]\\|");

	
}