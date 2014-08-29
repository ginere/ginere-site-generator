//package eu.ginere.site.template;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import org.apache.log4j.Logger;
//
//import eu.ginere.base.util.file.FileUtils;
//
///**
// * Nodos para javascript.
// * 
// * @author ventura
// */
//public class JavascriptNode extends FileTemplates.Node {
//
//	static final Logger log = Logger.getLogger(JavascriptNode.class);
//
//	private final File out;
//
//	public JavascriptNode(File file) throws FileNotFoundException {
//		super(file, true);
//		this.context = new FileTemplates.ContextProperties();
//
//		this.out = new File(FileTemplates.getOutpath(this), file.getName());
//	}
//
//	static public boolean isJavascriptNode(File file) {
//		if (file == null) {
//			return false;
//		} else {
//			String fileName = file.getName();
//			String ext = FileUtils.getExtension(fileName);
//
//			if (ext != null && "js".equals(ext.toLowerCase())) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//	}
//
//	@Override
//	public long getLastModified() {
//		return file.lastModified();
//	}
//
//	@Override
//	public String getFileName() {
//		return file.getName();
//	}
//
//	@Override
//	public void generateOrUpdateDiskFile(FileTemplates.ContextProperties context)
//			throws IOException {
//		if (out.lastModified() <= file.lastModified()) {
//			// copy file
//			// FileUtils.copyFile(file, out);
//			// log.info("OK: "+out.getAbsoluteFile());
//			String compilerOptions = context.getValue("JS_COMPILER_OPTIONS");
//			try {
//				GoogleCompiler.compile(file, out);
//			}catch (IOException e) {
//				throw new IOException("While compiling in file:"+file+", out file:"+out+" compiler options:"+compilerOptions,e);
//			}
//		}
//	}
//
//	@Override
//	protected String updateContent() throws FileNotFoundException {
//		return "";
//	}
//
//	/**
//	 * Use this method to get the string to parse of this node. The list pased
//	 * in param is the list where the childs found must be added
//	 * 
//	 * @param list
//	 * @return
//	 */
//	@Override
//	protected String getStringToParse() {
//		return "";
//	}
//}