package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.site.ContextProperties;
import eu.ginere.site.SiteGenerator;

/**
 * Nodos para javascript.
 * 
 * @author ventura
 */
public class JavascriptNode extends ParseableTextNode {

	static final Logger log = Logger.getLogger(JavascriptNode.class);

	private static final String NONE = "none";

	private static final String ADVANCED = "advanced";
	public static final String JAVA_SCRIPT_EXTENSION = ".js";

	private final File out;

	public JavascriptNode(SiteGenerator globalContext,
                          File file) throws FileNotFoundException {
		super(globalContext,file, true);
		this.context = new ContextProperties(globalContext);

		this.out = new File(globalContext.getOutpath(this), file.getName());
	}

	static public boolean isJavascriptNode(File file) {
		if (file == null) {
			return false;
		} else {
			String fileName = file.getName();
			String ext = FileUtils.getExtension(fileName);

			if (ext != null && "js".equals(ext.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		}
	}


	@Override
	public void generateOrUpdateDiskFile(ContextProperties context) throws IOException {
		if (out.lastModified() <= file.lastModified()) {

//			// copy file
//			// FileUtils.copyFile(file, out);
//			// log.info("OK: "+out.getAbsoluteFile());
//			String compilerOptions = context.getValue("JS_COMPILER_OPTIONS",this);
//			log.error("COMPILER:OPTIONS:"+compilerOptions);
//			if (NONE.equals(compilerOptions)){
//				// copy file
//				FileUtils.copyFile(file, out);
//				log.info("OK: "+out.getAbsoluteFile());
//			} else {
//				boolean advanced=false;
//				if (ADVANCED.equals(compilerOptions)){
//					advanced=true;
//				}
//				try {
//					GoogleCompiler.compile(file, out,context.getCharSet(),advanced);
//				}catch (IOException e) {
//					throw new IOException("While compiling in file:"+file+", out file:"+out+" compiler options:"+compilerOptions,e);
//				}
//			}
			int level=getCompilerLevel(this);
			if (level==0){
				// copy file
				FileUtils.copyFile(file, out);
				log.info("OK: "+out.getAbsoluteFile());
			} else {
				boolean advanced=(level==2);
				try {
					GoogleCompiler.compile(file, out,context.getCharSet(),advanced);
				}catch (IOException e) {
					throw new IOException("While compiling in file:"+file+", out file:"+out+" compiler options:"+level,e);
				}
			}
		}
	}

	@Override
	protected String updateContent() throws FileNotFoundException {
		return "";
	}

	/**
	 * Use this method to get the string to parse of this node. The list pased
	 * in param is the list where the childs found must be added
	 * 
	 * @param list
	 * @return
	 */
	@Override
	protected String getStringToParse() {
		return "";
	}

	public static int getCompilerLevel(Node node) {
		String compilerOptions = node.context.getValue("JS_COMPILER_OPTIONS",node);
		compilerOptions=compilerOptions.toLowerCase();
		
		log.error("COMPILER:OPTIONS:"+compilerOptions);
		if (NONE.equals(compilerOptions)){
			return 0;
		} else {
			if (ADVANCED.equals(compilerOptions)){
				return 2;
			} else {
				return 1;
			}				
		}
	}
}
