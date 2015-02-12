package eu.ginere.site;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.site.nodes.BinaryNode;
import eu.ginere.site.nodes.GlobalPropNode;
import eu.ginere.site.nodes.GoogleCompiler;
import eu.ginere.site.nodes.JavascriptNode;
import eu.ginere.site.nodes.Node;
import eu.ginere.site.nodes.ParseableTextNode;
import eu.ginere.site.nodes.PropNode;

/**
 * @author ventura
 *
 *  -Davem.common.util.properties.GlobalFileProperties.DefaultPath=/Users/ventura/projects/INDEXER/indexer-model-mysql/conf
 *
 */
public class SiteGenerator {
	
	static final Logger log = Logger.getLogger(SiteGenerator.class);

	private static final String SLASH = "/";

	final public File outDir;
	final public File tmpDir;
	final public File contentDir;
	final public File commonDirArray[];
	final public String charset;
		
	public long timeToSleepBetweenIteration = 100; // In millis
	
	private final Map<String, File> FILE_CACHE = new Hashtable<String, File>();	
	private final Map <File,Node> nodeCache=new Hashtable<File,Node>();
	
	private final String commonDirArrayString;

	public SiteGenerator(File outDir,File contentDir,File commonDir,String charset){
		this.outDir=outDir;
		this.tmpDir=new File(System.getProperty("java.io.tmpdir"));
		this.contentDir=contentDir;
		this.commonDirArray=new File[]{commonDir};
		this.charset=charset;
		commonDirArrayString=commonDir.getAbsolutePath();
	}

	public SiteGenerator(File outDir,File contentDir,File commonDir[],String charset){
		this.outDir=outDir;
		this.tmpDir=new File(System.getProperty("java.io.tmpdir"));
		this.contentDir=contentDir;
		this.commonDirArray=commonDir;
		this.charset=charset;

		StringBuilder buffer=new StringBuilder("]");
		for (File file:commonDir){
			buffer.append(file.getAbsolutePath());
			buffer.append(",");
		}
		buffer.append("]");

		commonDirArrayString=buffer.toString();
	}


	public void generate(boolean daemon) throws FileNotFoundException{
		GlobalPropNode root=new GlobalPropNode(this,contentDir);
		root.checkForUpdates(null);

		while (daemon){
			long time=System.currentTimeMillis();
			root.checkForUpdates(null);

			try {
				Thread.sleep(timeToSleepBetweenIteration);
			} catch (InterruptedException e) {
				if (log.isDebugEnabled()){
					log.debug(e);
				}
			}			
		}

	}

	
	public File getCommonFile(String fileName){
		for (File file:commonDirArray){
			File ret=new File(file,fileName);
			if (FileUtils.canReadFile(ret)){
				return ret;
			}			
		}
		
		return null;		
	}

	public String getCommonRelativePath(File file){
		File parentFile=file.getParentFile();

		for (File common:commonDirArray){
			String relativePath=FileUtils.getRelativePath(parentFile, common,null);
			if (relativePath!=null){
				return relativePath;
			}
		}
		
		return null;
	}

	public File getFileFromFileName(String fileName){
		if (FILE_CACHE.containsKey(fileName)){
			return FILE_CACHE.get(fileName);
		} else {
			
//			File file=new File(commonDir,fileName);
			File file=getCommonFile(fileName);
			
			if (file==null || !FileUtils.canReadFile(file)){
				file=new File(contentDir,fileName);
				if (FileUtils.canReadFile(file)){
					FILE_CACHE.put(fileName,file);				
				} else {
					log.warn("File not found:'"+fileName+"'");
					return null;
				}
			} else {
				FILE_CACHE.put(fileName,file);				
			}
	
			return file;
		}
	}


	public Node getFileNode(File file) throws FileNotFoundException {
		return getFileNode(file, false);
	}

	public Node getFileNode(File file,boolean isPageFile) throws FileNotFoundException {
		if (file==null){
			return null;
		}
		
		if (nodeCache.containsKey(file)){
			Node ret=nodeCache.get(file);

			return ret;
		} else {
			Node node;

			if (file.isDirectory()){
				node = new GlobalPropNode(this,file);
			} else if (isABinaryNode(file)){
				node=new BinaryNode(this,file);
			} else if (JavascriptNode.isJavascriptNode(file)){
				node=new JavascriptNode(this,file);
			} else if (PropNode.isAPropertiesFile(file)){
				node=new PropNode(this,file,isPageFile);
			} else {
				node=new ParseableTextNode(this,file,isPageFile);			
			}

			nodeCache.put(file, node);
			
			return node;
		}		
	}

	public void writeFileContent(Node root,String content) {
		try {
			
			// Parsion javascript files based on extension
			File outFile=new File(getOutpath(root),root.getFileName());
			
			try {
				int level=JavascriptNode.getCompilerLevel(root);
				if (level>0 && root instanceof PropNode && outFile.getName().toLowerCase().endsWith(JavascriptNode.JAVA_SCRIPT_EXTENSION)){
					boolean advanced=(level==2);
					File jsTempFile=getTmpFile(root);
					IOUtils.write(content,new FileOutputStream(jsTempFile),charset);
					GoogleCompiler.compile(jsTempFile, outFile, content, advanced);
				} else {
					IOUtils.write(content,new FileOutputStream(outFile),charset);
				}
				log.info("OK: "+outFile.getAbsoluteFile());
			}catch (IOException e) {
				log.error("Writing file: "+outFile.getAbsoluteFile(),e);
			}
		}catch (FileNotFoundException e) {
			log.error("No output file for node: "+root+" ",e);
		}
	}

	/**
	 * IF the file is ../content/folder1/folder2/index.html, that will retun /folder1/forlder2
	 * @return
	 * @throws FileNotFoundException 
	 */
	public String getRelativePath(File file) throws FileNotFoundException{
		String relativePath;
//		if (file==null){
//			return "";
//		}
		if (file.isDirectory()){
			relativePath=FileUtils.getRelativePath(file, contentDir,null);
		} else {
			relativePath=FileUtils.getRelativePath(file.getParentFile(), contentDir,null);
		}
		
		if (relativePath == null){
			// relativePath=FileUtils.getRelativePath(file.getParentFile(), commonDir,null);
			relativePath=getCommonRelativePath(file);
		}
		
		if (relativePath != null){
			relativePath=relativePath.replace('\\', '/');
			return relativePath;
		} else {
			throw new FileNotFoundException("The file:'"+file.getAbsolutePath()+"' is not under the content dir["+contentDir.getAbsolutePath()+"] nor the common dir ["+commonDirArrayString+"]");
		}
	}


	public File getOutpath(Node node) throws FileNotFoundException{
		File src=node.file;
		
//		String relativePath=FileUtils.getRelativePath(src.getParentFile(), contentDir);
		String relativePath=getRelativePath(src);
		
		FileUtils.createPath(outDir, relativePath);
		return new File(outDir,relativePath);
	}
	
	public File getTmpFile(Node node) throws FileNotFoundException{
		File src=node.file;
		
		String relativePath=getRelativePath(src);
		
		FileUtils.createPath(tmpDir, relativePath);
		return new File(tmpDir,relativePath);
	}


	public String iterateOverDIRS(Node parent,
								  Node template,
								  String relativePath){

		return iterateOver(parent,template,relativePath, true,false);
		
		/*
		File dir;
		
		if (relativePath!=null){

			
//			if (relativePath.startsWith("/")){
			// tratamos todos los paths como absluto
			// TODO CAMBIARLO
				dir=new File(contentDir,relativePath);
				
				if (!dir.isDirectory()){
					log.error("While getting the path:'"+relativePath+"' does not exists:"+dir.getAbsolutePath());
					return "";
				} else if (!dir.canRead()){
					log.error("While getting the path:'"+relativePath+"' can not be readed:"+dir.getAbsolutePath());
					return "";
				} 
			
		} else {
			dir=parent.getContext().getCurrentDir();
		}
		StringBuilder buffer=new StringBuilder();
		
		File array[]=dir.listFiles(CanThreadFileFilter.FILTER);
		FileUtils.sortByName(array);
		
		
		IteratorContext iteratorContext=new IteratorContext(this);
		for (File file:array){
			try {				

				// The list already makes the call to canThreatFileOrDir
				// Use iterateOverFILES is the same
				if (file.isDirectory()){
					GlobalPropNode dirNode= new GlobalPropNode(this,file);
					if (dirNode!=null){
						dirNode.getContext().setParent(parent.getContext());
						iteratorContext.setParent(dirNode.getContext());
						iteratorContext.iterate();
						String value=template.getContent(iteratorContext);
						buffer.append(value);
					}
				}

				
			} catch (FileNotFoundException e) {
				log.error("File:"+file.getAbsolutePath(),e);
			}
			
		}

		return buffer.toString();
		*/
	}


//	public String iterateOverFILES(Node parent,
//                                   Node template,
//                                   String globPattern){
//
//		/*
//		Node array[]=NodeFinder.getResultArray(generator, this.contentDir, globPattern);
//
//        File array[]=getRelativeFiles(relativePath);
//		File array[]=dir.listFiles(CanThreadFileFilter.FILTER);
//		FileUtils.sortByName(array);
//
//		StringBuilder buffer=new StringBuilder();
//		
//		
//		
//		IteratorContext iteratorContext=new IteratorContext(this);
//		for (File file:array){
//			try {				
//				Node node=getFileNode(file);
//				if (node!=null){
//					node.getContext().setParent(parent.getContext());
//					iteratorContext.setParent(node.getContext());
//					iteratorContext.iterate();
//					String value=template.getContent(iteratorContext);
//					buffer.append(value);
//				}
//			} catch (FileNotFoundException e) {
//				log.error("File:"+file.getAbsolutePath(),e);
//			}
//			
//		}
//
//		return buffer.toString();
//		*/
//		return iterateOver(parent,template,globPattern, false,false,true);
//	}

	public String iterateOverSymbLink(Node parent,
									  Node template,
									  String globPattern){
		return iterateOver(parent,template,globPattern, false,true);
	}



	public String iterateOver(Node parent,
							  Node template,
							  String relativePath,
							  boolean dirs,
//							  boolean files, Not implemented take care to hace the same parent as the child
							  boolean symLink){
		File dir;

		if (relativePath == null){			
			dir=parent.getContext().getCurrentDir();
		} else  {
						
			if (relativePath.startsWith(SLASH)) {
				dir=new File(contentDir,relativePath) ;
			} else {
				dir=new File(parent.getContext().getCurrentDir(),relativePath);
			}

			if (!dir.isDirectory()){
				log.error("While getting the path:'"+relativePath+"' does not exists:"+dir.getAbsolutePath());
				return "";
			} else if (!dir.canRead()){
				log.error("While getting the path:'"+relativePath+"' can not be readed:"+dir.getAbsolutePath());
				return "";
			} 			
		}

		File array[]=dir.listFiles(CanThreadFileFilter.FILTER);
		FileUtils.sortByName(array);
		StringBuilder buffer=new StringBuilder();

		IteratorContext iteratorContext=new IteratorContext(this);
		for (File file:array){
			try {				
				if ( (symLink && Files.isSymbolicLink(file.toPath())) 
					 || (dirs && file.isDirectory() && !Files.isSymbolicLink(file.toPath()) ) 
//					 || (files && !file.isDirectory()) 
					 ) {

					Node node=getFileNode(file);
					if (node!=null){
						node.getContext().setParent(parent.getContext());
						iteratorContext.setParent(node.getContext());
						iteratorContext.iterate();
						String value=template.getContent(iteratorContext);
						buffer.append(value);
					} else {
						log.warn("Node is null for file:"+file.getAbsolutePath());
					}					
					
				}
			} catch (FileNotFoundException e) {
				log.error("File:"+file.getAbsolutePath(),e);
			}

		}
		
		return buffer.toString();
	}

	/*
	public String iterateOverFILES(Node parent,
                                   Node template,
                                   String relativePath){

		TODO
        File array[]=getRelativeFiles(relativePath);
		File array[]=dir.listFiles(CanThreadFileFilter.FILTER);
		FileUtils.sortByName(array);

		StringBuilder buffer=new StringBuilder();
		
		
		
		IteratorContext iteratorContext=new IteratorContext(this);
		for (File file:array){
			try {				
				Node node=getFileNode(file);
				if (node!=null){
					node.getContext().setParent(parent.getContext());
					iteratorContext.setParent(node.getContext());
					iteratorContext.iterate();
					String value=template.getContent(iteratorContext);
					buffer.append(value);
				}
			} catch (FileNotFoundException e) {
				log.error("File:"+file.getAbsolutePath(),e);
			}
			
		}

		return buffer.toString();
	}
	*/

	public String iterateOverDIR(Node parent,
								 Node template,
								 String relativePath){
		if (relativePath!=null){			
			// TODO IS /relativePath usee new File(contentDir,relativePath) 
			// TODO IS relativePath use new File(parent.context.getCurrentDir(),relativePath);

			//			File dir=new File(contentDir,relativePath);
			File dir=new File(parent.getContext().getCurrentDir(),relativePath);
				
			if (!dir.isDirectory()){
				log.error("While getting the path:'"+relativePath+"' does not exists:"+dir.getAbsolutePath());
				return "";
			} else if (!dir.canRead()){
				log.error("While getting the path:'"+relativePath+"' can not be readed:"+dir.getAbsolutePath());
				return "";
			} else {
				try {				
					IteratorContext iteratorContext=new IteratorContext(this);
					GlobalPropNode dirNode= new GlobalPropNode(this,dir);
					if (dirNode!=null){
						dirNode.getContext().setParent(parent.getContext());
						iteratorContext.setParent(dirNode.getContext());
						iteratorContext.iterate();
						String value=template.getContent(iteratorContext);
						return value;
					}
				} catch (FileNotFoundException e) {
					log.error("File:"+dir.getAbsolutePath(),e);
				}
				return "";
			}
		} else {
			log.error("relativePath :'"+relativePath+"' can not be null for iterate over DIR.");
			return "";
		}
	}

	
	public static class CanThreadFileFilter implements FileFilter{
		
		public static final CanThreadFileFilter FILTER=new CanThreadFileFilter();
		
		private CanThreadFileFilter(){
			
		}
		
		@Override
		public boolean accept(File file) {
			return canThreatFileOrDir(file);
		}
		
	}

	public static final Map<String, String> BINARY_EXTENSIONS = new Hashtable<String, String>();	
	
	static {
		BINARY_EXTENSIONS.put("map","map");
		BINARY_EXTENSIONS.put("ico","ico");
		//		BINARY_EXTENSIONS.put("js","js");
		BINARY_EXTENSIONS.put("css","css");
		BINARY_EXTENSIONS.put("png","png");
		BINARY_EXTENSIONS.put("gif","gif");
		BINARY_EXTENSIONS.put("jpg","jpg");
		BINARY_EXTENSIONS.put("jpeg","jpeg");

		BINARY_EXTENSIONS.put("webm","webm");
		BINARY_EXTENSIONS.put("mp4","mp4");
		BINARY_EXTENSIONS.put("mpeg","mpeg");
		BINARY_EXTENSIONS.put("mov","mov");
		BINARY_EXTENSIONS.put("flv","flv");
		BINARY_EXTENSIONS.put("avi","avi");
		
		// FONTS
		BINARY_EXTENSIONS.put("otf","otf");
		BINARY_EXTENSIONS.put("svg","svg");
		BINARY_EXTENSIONS.put("woff","woff");
		BINARY_EXTENSIONS.put("eot","eot");
		BINARY_EXTENSIONS.put("ttf","ttf");				
	}

	public static final String HTML = ".html";
	public static final String PROP = ".prop";
	public static final String CSS = ".css";
	public static final String JS = ".js";
	public static final String MAP = ".map";
	public static final String TILDE = "~";
	public static final String DOT = ".";

	public static boolean canThreatFileOrDir(File file) {
		
		if (file == null) {
			return false;
		} else if (!file.canRead()){
			return false;
		} 
		
		String fileName=file.getName();
		
		if (fileName.startsWith(DOT)){
			return false;
		} 

		if (file.isDirectory()){
			return true;
		} else {
			// Normal files			
			if (fileName.endsWith(TILDE)){
				return false;
			} else if (GlobalPropNode.GLOBAL_PROPERTIES_FILE_NAME.equals(fileName)){
				return false;
			} else if (fileName.endsWith(HTML)){
				return true;
			} else if (fileName.endsWith(PROP)){
				return true;
			} else if (fileName.endsWith(CSS)){
				return true;
			} else if (fileName.endsWith(JS)){
				return true;
			} else if (fileName.endsWith(MAP)){
				return true;
			} else if (isABinaryNode(file)){
				return true;
			} else {
				// log.warn("File unknown:'"+fileName+"'");
				return true;
			}
		}
	}

	static private boolean isABinaryNode(File file) {
		if (file == null){
			return false;
		} else {
			String fileName=file.getName();
			String ext=FileUtils.getExtension(fileName);
						
            if (ext == null) {
                ext="";
            } 

            return BINARY_EXTENSIONS.containsKey(ext);
		}
	}	



}
