//package eu.ginere.site.template;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Hashtable;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;
//
//import eu.ginere.base.util.file.FileUtils;
//import eu.ginere.base.util.properties.FileProperties;
//
///**
// * @author ventura
// *
// *  -Davem.common.util.properties.GlobalFileProperties.DefaultPath=/Users/ventura/projects/INDEXER/indexer-model-mysql/conf
// *
// */
//public class FileTemplates {
//	
//	static final Logger log = Logger.getLogger(FileTemplates.class);
//	
//	private static final String GLOBAL_PROPERTIES_FILE_NAME = "GlobalProperties.prop";
//	private static final String TEMPLATE_PROPERTY = "template";
//
//	private static final String CONTEXT_PROPERTY_NAME_INDEX="Index";
//	
//	private static final String HTML = ".html";
//	private static final String PROP = ".prop";
//	private static final String CSS = ".css";
//	private static final String JS = ".js";
//	private static final String MAP = ".map";
//	private static final String TILDE = "~";
//	private static final String DOT = ".";
//
//	private static final long SLEEP_TIME = 100;
//
//	
//	private static File pagesPath;
//	private static File libPath;
//	private static File outPath;
//	private static boolean daemon=false;
//	public static String CHARSET="UTF-8";  // ISO-8859-1,UTF-8,US-ASCII
//
////	public static final Pattern FILE_TOKEN_PATER=Pattern.compile("[^#]+\\{([a-zA-Z][a-zA-Z0-9_./-]+)\\}");
//	public static final Pattern FILE_TOKEN_PATER=Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_./-]+)\\}");
//	
//	private static final String DIR = "DIR";
//	private static final String DIRS = "DIRS";
//
//	public static final Pattern LIST_TOKEN_PATER=Pattern.compile("\\{(DIRS|DIR)(\\[([a-zA-Z][a-zA-Z0-9_./-]+)\\])?:([a-zA-Z][a-zA-Z0-9_./-]+)\\}");
//	
//	public static final Pattern VARIABLE_TOKEN_PATER=Pattern.compile("\\|([a-zA-Z][a-zA-Z0-9_]+)\\|");
//
//	public static final String VARIABLE_SEPARATOR="|";
//
//	private static final Map<String, String> BINARY_EXTENSIONS = new Hashtable<String, String>();	
//	static {
//		BINARY_EXTENSIONS.put("map","map");
//		BINARY_EXTENSIONS.put("ico","ico");
//		//		BINARY_EXTENSIONS.put("js","js");
//		BINARY_EXTENSIONS.put("css","css");
//		BINARY_EXTENSIONS.put("png","png");
//		BINARY_EXTENSIONS.put("gif","gif");
//		BINARY_EXTENSIONS.put("jpg","jpg");
//		BINARY_EXTENSIONS.put("jpeg","jpeg");
//
//		BINARY_EXTENSIONS.put("webm","webm");
//		BINARY_EXTENSIONS.put("mp4","mp4");
//		BINARY_EXTENSIONS.put("mpeg","mpeg");
//		BINARY_EXTENSIONS.put("mov","mov");
//		BINARY_EXTENSIONS.put("flv","flv");
//		BINARY_EXTENSIONS.put("avi","avi");
//
//	}
//	
//	private static final List<Node> EMPTY_LIST = new ArrayList<FileTemplates.Node>(0);
//	private static final Map<String, File> FILE_CACHE = new Hashtable<String, File>();	
//	private static final Map <File,Node> nodeCache=new Hashtable<File,Node>();
//
//	private static final String QUOTE = "\"";
//
//	
//	static public class ContextProperties{
//		FileProperties fileProperties;
//		protected ContextProperties parent=null;
////		final boolean isGlobalCotexts;
//		//		long lastParentUpdated=0;
//		private Node pageNode=null;
//
//		ContextProperties(File file/*,boolean isGlobalCotexts*/) {
////			this.isGlobalCotexts=isGlobalCotexts;
//			if (file!=null && FileUtils.canReadFile(file)){
//				try {
//					this.fileProperties=new FileProperties(file,CHARSET);
//				} catch (IOException e) {
//					log.error("Warning file:"+file,e);
//					this.fileProperties=null;				}
//			} else {
//				this.fileProperties=null;
//			}
//		}
//
//		ContextProperties() {
////			this.isGlobalCotexts=false;
//			this.fileProperties=null;
//		}
//
//		@Override
//		public String toString(){
//			String current=" null ";
//			if (fileProperties != null){
//				//				current=fileProperties.getFildes().getName();
//				current=FileUtils.getRelativePath(fileProperties.getFildes(), pagesPath);
//			}
//			if (parent == null){
//				return "["+current+"]";
//			} else {
//				return "["+current+parent+"]";				
//			}
//		}
//
//		public File getCurrentDir() {			
//			if (this.fileProperties != null){
//				return this.fileProperties.getFildes().getParentFile();
//			} else if (this.parent!=null){
//				return this.parent.getCurrentDir();
//			} else {
//				return null;
//			}
//		}
//
//		/**
//		 * Returns true if this context has been modified after or at this date.
//		 * 
//		 * @param lastModified
//		 * @return
//		 */
//		public boolean hasBeenModified(long lastModified) {
//			if (fileProperties != null){
//				// Update the file last modified and the properties
//				fileProperties.checkForModification();
//				if (lastModified<=fileProperties.getLastModified()){
//					return true;
//				} 
//			}
//
//			// If this node has not been modifief look into the parent one
//			if (parent!=null){
//				return parent.hasBeenModified(lastModified);
//			} else {
//				return false;
//			}
//		}
//
//		public String getValue(String propertyName) {
//			String ret=null;
//			if (this.fileProperties != null){
//				ret=this.fileProperties.getStringValue(ContextProperties.class, propertyName, null);
//			}
//			
////			if (ret==null && parent!=null && parent.isGlobalContext()){
//			// The global context properties are only used into the page root nodes
//			if (ret==null && parent!=null){
//				ret=parent.getValue(propertyName);
//				// el Global no puede devolver null;
//				if (ret==null){
//					return "";
//				}
//			}
//
//			if (ret==null){
////				if (isGlobalContext()){
////					return "";
////				} else {
////					return VARIABLE_SEPARATOR+propertyName+VARIABLE_SEPARATOR;
////				}
//				return "";
//			} else {
//				return unScapeFromBathPropFiles(ret);
//			}
//		}
//
//		public void setParent(ContextProperties parentContext) {
//			this.parent=parentContext;
//			//			this.lastParentUpdated=System.currentTimeMillis();
//		}
//
//		public ContextProperties getParent() {
//			return parent;
//		}
//
////		public boolean isGlobalContext() {
////			return isGlobalCotexts;
////		}
//
//		public String parseVariables(String stringToParse) {
//			if (stringToParse == null || "".equals(stringToParse)){
//				return "";
//			} else {
//				// First  parseamos las variables $VARIABLE$
//				StringBuffer buffer=new StringBuffer();
//				Matcher matcher = VARIABLE_TOKEN_PATER.matcher(stringToParse);
//				while (matcher.find()) {
//					String token=matcher.group(1);
//					String value=getValue(token);
//					try {
//						matcher.appendReplacement(buffer, value);
//					}catch(IllegalArgumentException e){
//						log.error("For token:"+token+" and value:"+value+"'",e);
//					}
//				}
//				matcher.appendTail(buffer);
//				return buffer.toString();
//			}
//		}
//
//		public void setPageNode(Node node) {
//			this.pageNode=node;
//			
//		}
//
//		public Node getPageNode() {
//			if (this.pageNode !=null){
//				return this.pageNode;
//			} else {
//				return getParent().getPageNode();
//			}
//		}
//	}
//	
//	static public class IteratorContext extends ContextProperties{
//		long lastIteration=0;
//		int iteration=0;
//
//		IteratorContext() {
//			super();
//		}
//
//		@Override
//		public String toString(){
//			String current=" Iterator ";
//			if (parent == null){
//				return "["+current+"]";
//			} else {
//				return "["+current+parent+"]";				
//			}
//		}
//
//		public File getCurrentDir() {			
//			return null;
//		}
//
//		public void iterate() {			
//			iteration++;
//			lastIteration=System.currentTimeMillis();
//		}
//		
//		public boolean hasBeenModified(long lastModified) {
//			if (lastIteration >= lastModified){
//				return true;
//			} else if (parent!=null){
//				return parent.hasBeenModified(lastModified);
//			} else {
//				return false;
//			}
//		}
//
//		@Override
//		public String getValue(String propertyName) {
//			if (CONTEXT_PROPERTY_NAME_INDEX.equals(propertyName)){
//				return Integer.toString(iteration);
//			} else {
//				if (parent!=null){
//					return parent.getValue(propertyName);
//				} else {
//					return null;
//				}
//			}
//		}
//
//	}
//	
//	static public abstract class Node{
//
//		final File file;
////		String content="";
//		
//		//		boolean isPropFile; // this means that the file is a property file, and has a templay property with the name of the template to aply
//		//		long lastModified;
//		protected List <Node> childs=null;
//		ContextProperties context;
//		private long lastUpdated=0;
//		protected final boolean isPageFile;
//		
//		public Node(File file,boolean isPageFile) throws FileNotFoundException {
//			this.file=file;
//			this.isPageFile=isPageFile;
//		}
//
////		public void setContent(String string) {
////			this.content=string;			
////		}
//		
//		public String getContent(ContextProperties parentContext) throws FileNotFoundException {
////			updateNodes(parentContext);
////			return content;
//
////			Este tiene que devolverlo siempre
//			this.context.setParent(parentContext);
//			return updateContent(); 
//		}
//
//		public abstract String getFileName();
//
////		public long getChildsLastModified() throws FileNotFoundException {
////			long childsLastModified=0; 
////			
////			if (childs.size()>0){				
////				// update childs
////				for (Node child:childs){
////					long curret=child.getChildsLastModified();
////					if (childsLastModified<curret){
////						childsLastModified=curret;
////					}
////				}				
////			}
////			return Math.max(childsLastModified, getLastModified());
////		}
//
//		/**
//		 * Update the dis information asociated to this node.
//		 * This aply only for page nodes.
//		 * @return
//		 * @throws FileNotFoundException
//		 */
//		public void generateOrUpdateDiskFile(ContextProperties parentContext) throws IOException {
//			if (!isPage()){
//				return ;
//			}
//			
//			createChildList();
////			log.info("+++PAGE:"+this+" date:"+new Date(getLastModified()));
//			
//			this.context.setParent(parentContext);
//			// We asume that the information is generated by the first time.
//			// then hte information has to be changed only if one of the dist files
//			// of the children has been changed or the prop nothes of the context ...
//			
//			long childsLastModified=0; 
//				
////			long childsLastModified=getChildsLastUpdated();
//			
//			if (getChildListSize()>0){				
//				// update childs
//				for (Node child:getChildList()){
//					long curret=child.getLastModified();
////					log.info("+++"+child+" date:"+new Date(curret));
//					if (childsLastModified<curret){
//						childsLastModified=curret;
//					}
//				}				
//			}
//
//			// If some child has been modified
//			// If myself is modified
//			// If the context preoperties, current or parent ones, has been modified
////			if (getLastUpdated()<childsLastModified || getLastUpdated() < getLastModified() || context.hasBeenModified(getLastUpdated()) ){
//			if (getLastUpdated()<childsLastModified){
////				log.info("Modificado debido a los hijos:"+this);
//				updateContent();
//			} else if (getLastUpdated() <= getLastModified()){
////				log.info("Modificado debido al fichero:"+this);
//				updateContent();
//			} else if (context.hasBeenModified(getLastUpdated()) ){
////				log.info("Modificado debido al contexto:"+this);
//				updateContent(); 
//			}
//			
////			return getLastUpdated();
//		}
//
//
//		protected void createChildList() {
//			context.setPageNode(this);
//			if (isPageFile){
//				if (childs==null){
//					childs=new ArrayList<Node>();
//				}
//			} else {
//				childs=null;
//			}			
//		}
//
//
//		protected List<Node> getChildList() {
//			if (childs!=null){
//				return childs;
//			} else {
//				return EMPTY_LIST;
//			}
//		}
//
//		protected int getChildListSize() {
//			if (childs!=null){
//				return childs.size();
//			} else {
//				return 0;
//			}
//		}
//		protected void clearChildList() {
//			if (childs!=null){
//				childs.clear();
//			}			
//		}
//
//
//		protected void addChild(Node child) {
//			if (childs!=null){
//				childs.add(child);
//			} else {
//				context.getPageNode().addChild(child);				
//			}
//		}
//
//		protected long getLastUpdated() {
//			return lastUpdated;
//		}
//
//		@Override
//		public String toString(){
//			//			return file.getName()+":"+context;
//			return FileUtils.getRelativePath(file, pagesPath)+":"+context;
//
//		}
//
//		
//		/**
//		 * This updates the node content, the string that represents this node
//		 * and its childs. <p>
//		 * @throws IOException 
//		 */
//		protected String updateContent() throws FileNotFoundException {
//			String ret;
//			String stringToParse;
////			List <Node> list=new ArrayList<Node>();
//			
//			clearChildList();
//			
//			// Getting the String to parse
//			stringToParse=getStringToParse();
//			
////			if (this.isPropFile() ){
////				if (! this.isDirectoryProperties()){
////		
////					String fileName=context.getValue(TEMPLATE_PROPERTY);
////					
////					if ("".equals(fileName)){
////						log.warn("No template property defined for node:"+this);					
////						stringToParse="";
////					} else {
////						Node child=getFileNode(getFileFromFileName(fileName));
////						if (child!=null){
////							stringToParse=child.getContent(this.context);
////							list.add(child);
////						} else {
////							stringToParse="";
////						}
////					}
////				} else {
////					// Global Properties file
////					stringToParse = "";
////				}
////			} else {
////				try {
////					stringToParse = new Scanner(file).useDelimiter("\\Z").next();
////				}catch(FileNotFoundException e){
////					log.error("While oppening file:"+file,e);
////					stringToParse="";
////				}
////			} 
//
//			// First the properties, because into the properties may be defined
//			// the included files. There is no problems because the variable may
//			// be resolved by the stack
//
//			Matcher matcher;
//			StringBuffer buffer;
//			
//			// First  parseamos las variables $VARIABLE$
//			buffer=new StringBuffer();
//			matcher = VARIABLE_TOKEN_PATER.matcher(stringToParse);
//			while (matcher.find()) {
//				String token=matcher.group(1);
//				String value=context.getValue(token);
//				try {
//					matcher.appendReplacement(buffer, value);
//				}catch(IllegalArgumentException e){
//					log.error("For token:"+token+" and value:"+value+"'",e);
//				}
//			}
//			matcher.appendTail(buffer);
//			stringToParse=buffer.toString();
//
//			
//			//  Last include files
//			matcher = FILE_TOKEN_PATER.matcher(stringToParse);
//			buffer=new StringBuffer();
//						
//			while (matcher.find()) {
//				String token=matcher.group(1);
//				try {
//					Node child=getFileNode(getFileFromFileName(token));
//					if (child!=null) {
//						addChild(child);
//						String value=child.getContent(this.context);
//						try {
//							matcher.appendReplacement(buffer, value);
//						}catch(IllegalArgumentException e){
//							log.error("For token:"+token+" and value:"+value,e);		
//						}
//					}
//				}catch(FileNotFoundException e){
//					log.error("While searching for file:"+token,e);
//				}
//			}			
//			matcher.appendTail(buffer);
//			
//			stringToParse=buffer.toString();
//							
//			// LISTAS
//			matcher = LIST_TOKEN_PATER.matcher(stringToParse);
//			buffer=new StringBuffer();
//						
//			while (matcher.find()) {
//				String listType=matcher.group(1);
//				String g2=matcher.group(2);
//				String relativePath=matcher.group(3);
//				String templateName=matcher.group(4);
//				
//				Node template=getFileNode(getFileFromFileName(templateName));
//				if (template!=null){
//					addChild(template);
//					String value;
//					if (DIRS.equals(listType)) {
//						value=iterateOverDIRS(this,template,relativePath/*,matcher,buffer,list*/);
//					} else if (DIR.equals(listType)) {
//						value=iterateOverDIR(this,template,relativePath/*,matcher,buffer,list*/);
//					} else {
//						log.error("Dir command:["+listType+"] unkown.");
//						value="";
//					}
//					try {
//						matcher.appendReplacement(buffer, value);
//					}catch(IllegalArgumentException e){
//						log.error("For templateName:"+templateName+" and value:"+value,e);		
//					}
//				}						
//			}			
//			matcher.appendTail(buffer);
//			
//			stringToParse=buffer.toString();
//
//
//	
//			// actualizamos la lista de hijos
////			childs=list;
//			
//			// si soy un nodo raiz me guardo
//			if (this.isPage()){
//				// One more pass is neded to parse properties inside properties of the 
//				// pages properties
//				ContextProperties currentContext=context.getParent();
//				while (currentContext!=null){
//					stringToParse=currentContext.parseVariables(stringToParse);
//					currentContext=currentContext.getParent();
//				}
//				writeFileContent(this,stringToParse);
////				setContent(stringToParse);
//				ret=stringToParse;
//
//			} else {
//				log.info("Updated:"+this);
////				setContent(stringToParse);
//				ret=stringToParse;
//			}
//			
//			lastUpdated=System.currentTimeMillis();
//			
//			return ret;
//		}
//
//		/**
//		 * Use this method to get the string to parse of this node.
//		 * The list pased in param is the list where the childs found must be added
//		 * @param list
//		 * @return
//		 */
//		protected abstract String getStringToParse();
//
//		/**
//		 * This node representas a page that have to be sored into the file system after compilation
//		 * @return
//		 */
//		protected boolean isPage() {
//			return isPageFile;
////			if (context.isGlobalContext()){
////				return false;
////			} else {
////				return context.getParent().isGlobalContext();
////			}
//		}
//
////		protected abstract boolean isDirectoryProperties();
//		
//		/**
//		 * The last time of the file disk information need to generate pages has been modified.
//		 * This is not the last time the node has been executed or generates.
//		 * 
//		 * @return
//		 */
//		protected abstract long getLastModified();
//		//		protected abstract boolean isPropFile();
//		
//	}
//
//	public static class ParseableTextNode extends Node {
//		
//		public ParseableTextNode(File file,boolean isPageFile) throws FileNotFoundException {
//			super(file,isPageFile);
//			this.context=new ContextProperties();				
//		}
//		@Override
//		public long getLastModified(){
//			return file.lastModified();
//		}
////		@Override
////		public boolean isPropFile(){
////			return false;
////		}
////		@Override
////		public boolean isDirectoryProperties(){
////			return false;
////		}
//		@Override
//		public String getFileName() {
//			return file.getName();
//		}
//		
//		/**
//		 * Use this method to get the string to parse of this node.
//		 * The list pased in param is the list where the childs found must be added
//		 * @param list
//		 * @return
//		 */
//		@Override
//		protected String getStringToParse() {
//			String stringToParse;
//			try {
//				stringToParse = new Scanner(file,CHARSET).useDelimiter("\\Z").next();
//			}catch(FileNotFoundException e){
//				log.error("While oppening file:"+file,e);
//				stringToParse="";
//			}
//			
//			return stringToParse;
//		}
//	}
//	
//	public static class BinaryNode extends Node {
//		private final File out;
//
//		public BinaryNode(File file) throws FileNotFoundException {
//			super(file,true);
//			this.context=new ContextProperties();				
//
////			this.out=getOutpath(this);
//			this.out=new File(getOutpath(this),file.getName());
//		}
//
//		@Override
//		public long getLastModified(){
//			return file.lastModified();
//		}
//
////		@Override
////		public boolean isPropFile(){
////			return false;
////		}
//
////		@Override
////		public boolean isDirectoryProperties(){
////			return false;
////		}
//		@Override
//		public String getFileName() {
//			return file.getName();
//		}
//
//		@Override
//		public void generateOrUpdateDiskFile(ContextProperties context) throws IOException {
//			if (out.lastModified()<= file.lastModified() ){
//				// copy file
//				FileUtils.copyFile(file, out);
//				log.info("OK: "+out.getAbsoluteFile());
//			}
//
////			return out.lastModified();
//		}
//
//		@Override
//		protected String updateContent() throws FileNotFoundException {
//			return "";
//		}
//		
//		/**
//		 * Use this method to get the string to parse of this node.
//		 * The list pased in param is the list where the childs found must be added
//		 * @param list
//		 * @return
//		 */
//		@Override
//		protected String getStringToParse() {
//			return "";
//		}
//	}
//	
//
//	public static class PropNode extends Node {
//		public PropNode(File file,boolean isPageFile) throws FileNotFoundException {
//			super(file,isPageFile);
//			this.context=new ContextProperties(file);
//		}
//		
//		@Override
//		public long getLastModified(){
//			return file.lastModified();
//		}
//		
////		@Override
////		public boolean isPropFile(){
////			return true;
////		}	
////		
////		@Override
////		public boolean isDirectoryProperties(){
////			return false;
////		}
//		
//		@Override
//		public String getFileName() {
//			return getHtmlFileNameFromPropFileName(file.getName());
//		}
//		
//		/**
//		 * Use this method to get the string to parse of this node.
//		 * The list pased in param is the list where the childs found must be added
//		 * @param list
//		 * @return
//		 */
//		@Override
//		protected String getStringToParse() {
//			String fileName=context.getValue(TEMPLATE_PROPERTY);
//			String stringToParse;
//			
//			if ("".equals(fileName)){
//				log.warn("No template property defined for node:"+this);					
//				stringToParse="";
//			} else {
//				try {
//					Node child=getFileNode(getFileFromFileName(fileName));
//					if (child!=null){
//						stringToParse=child.getContent(this.context);
//						addChild(child);
//					} else {
//						stringToParse="";
//					}
//				}catch (FileNotFoundException e) {
//					log.error("Error getting file:'"+fileName+"'",e);
//					stringToParse="";
//				}
//			}
//
//			return stringToParse;
//		}
//	}
//	
//
//
//	public static class GlobalPropNode extends Node {
////		private final boolean isRoot;
//		private final File dir;
//		private final File propFile;
//
//		public GlobalPropNode(File dir) throws FileNotFoundException {
//			//			super(new File(dir,GLOBAL_PROPERTIES_FILE_NAME),false);
//			super(dir,false);
//
//			File propFile=new File(dir,GLOBAL_PROPERTIES_FILE_NAME);
//			if (FileUtils.canReadFile(propFile)){
//				this.context=new ContextProperties(propFile);
//				this.propFile=propFile;
//			} else {
//				this.context=new ContextProperties();
//				this.propFile=null;
//			}
//			
//			
//			this.dir=dir;
//		}
//
//		@Override
//		public long getLastModified(){
//			if (propFile==null){
//				return file.lastModified();
//			} else {
//				return Math.max(file.lastModified(),propFile.lastModified());
//			}
//		}
//		
//		
//		@Override
//		public String getContent(ContextProperties parentContext) throws FileNotFoundException {
//			return "";
//		}
//
//		@Override
//		public String getFileName() {
//			return null;
//		}
//
//		/**
//		 * Use this method to get the string to parse of this node.
//		 * The list pased in param is the list where the childs found must be added
//		 * @param list
//		 * @return
//		 */
//		@Override
//		protected String getStringToParse() {
//			return "";
//		}
//		@Override
//		public void generateOrUpdateDiskFile(ContextProperties context) throws IOException {
//			log.warn("Trying to generate disk file information for a GlobalProperties Node:"+this);
//			return ;
//		}
//		
//		public void checkForUpdates(ContextProperties parentContext) {
////			log.info(this+":Checking for updates ... ");
//
//			context.setParent(parentContext);
//			
//			File array[]=dir.listFiles();
//			List <Node>list=new ArrayList<FileTemplates.Node>(array.length);
//			
//			for (File file:array){
//				try {				
//					if (canThreatFileOrDir(file)){
//						if (file.isDirectory()){
//							GlobalPropNode dirChildNode = (GlobalPropNode)getFileNode(file,false);
//							if (dirChildNode!=null){
//								dirChildNode.checkForUpdates(this.context);
//								list.add(dirChildNode);
////								log.info(dirChildNode+":Child DIR node added to:"+this);
//
//							}
//						} else {
//							Node fileNode = getFileNode(file,true);
//							if (fileNode!=null){
//								list.add(fileNode);
////								log.info(fileNode+":File node added to:"+this);
//							}
//						}
//					}
//				} catch (FileNotFoundException e) {
//					log.error("File:"+file.getAbsolutePath(),e);
//				}
//
//			}
//		
////			childs=list;
//
//			for (Node child:list){
//				try {
//					if (child.isPage()){
//						// solo guardamos en disco los nodo que son paginas
//						child.generateOrUpdateDiskFile(context);
//					}
//				} catch (IOException e) {
//					log.error("File:"+file.getAbsolutePath(),e);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		log.info("Process started at:"+new Date());
//		
//		if (args.length<3){
//			exitError("at least 3 args are needed,pages path, lib path, out path, and daemon true/false ");
//		}
//		
//		pagesPath=new File(args[0]);
//		libPath=new File(args[1]);
//		outPath=new File(args[2]);
//		
//		if (args.length >= 4){
//			if ("true".equals(args[3])){
//				daemon=true;
//			}
//		}
//		
//		if (args.length >= 5){
//			CHARSET=args[4];
//			log.info("USING CHARSET:"+CHARSET);
//		} else {
//			log.info("Using default charset:"+CHARSET);
//		}
//		if (! FileUtils.verifyDir(pagesPath)){
//			exitError("pagesPath:"+pagesPath);
//		}
//
//		if (! FileUtils.verifyDir(libPath)){
//			exitError("pagesPath:"+libPath);
//		}
//
//		if (! FileUtils.verifyDir(outPath)){
//			exitError("pagesPath:"+outPath);
//		}
//
//		try {
//			GlobalPropNode root=new GlobalPropNode(pagesPath);
//			root.checkForUpdates(null);
//			
//			while (daemon){
//				long time=System.currentTimeMillis();
//				root.checkForUpdates(null);
////				log.info("Iteration done in:"+(System.currentTimeMillis()-time)+" millis");
//				Thread.sleep(SLEEP_TIME);
////				log.info("Awaked.");
//
//			}
//		}catch (Exception e) {
//			log.fatal("Exception writing nodes",e);
//		}	
//		
//		log.info("Process ended.");
//	}
//	
//	private static Node getFileNode(File file) throws FileNotFoundException {
//		return getFileNode(file, false);
//	}
//	
//	private static Node getFileNode(File file,boolean isPageFile) throws FileNotFoundException {
//		if (file==null){
//			return null;
//		}
//
////		if (file.isDirectory()){
////			// To avoid cicles including childs into parent nodes
////			return new GlobalPropNode(file);
////		} 
//		
//		if (nodeCache.containsKey(file)){
//			Node ret=nodeCache.get(file);
//
//			return ret;
//		} else {
//			Node node;
//			if (file.isDirectory()){
//				node = new GlobalPropNode(file);
//			} else if (isABinaryNode(file)){
//				node=new BinaryNode(file);
//			} else if (JavascriptNode.isJavascriptNode(file)){
//				node=new JavascriptNode(file);
//			} else if (isAPropertiesFile(file)){
//				node=new PropNode(file,isPageFile);
//			} else {
//				node=new ParseableTextNode(file,isPageFile);
//			
//			}
//			nodeCache.put(file, node);
//			
//			return node;
//		}		
//	}
//	
//	private static File getFileFromFileName(String fileName){
//		if (FILE_CACHE.containsKey(fileName)){
//			return FILE_CACHE.get(fileName);
//		} else {
//			
//			File file=new File(libPath,fileName);
//			
//			if (!FileUtils.canReadFile(file)){
//				file=new File(pagesPath,fileName);
//				if (FileUtils.canReadFile(file)){
//					FILE_CACHE.put(fileName,file);				
//				} else {
//					log.warn("File not found:'"+fileName+"'");
//					return null;
//				}
//			} else {
//				FILE_CACHE.put(fileName,file);				
//			}
//	
//			return file;
//		}
//	}
//	
//	private static void writeFileContent(Node root,String content) {
//		File outFile=new File(getOutpath(root),root.getFileName());
//		try {
//			IOUtils.write(content,new FileOutputStream(outFile),CHARSET);
//			log.info("OK: "+outFile.getAbsoluteFile());
//		}catch (IOException e) {
//			log.error("Writing file: "+outFile.getAbsoluteFile(),e);
//		}
//	}
//	
//	
//	static File getOutpath(Node node){
//		File src=node.file;
//		
//		String relativePath=FileUtils.getRelativePath(src.getParentFile(), pagesPath);
//		FileUtils.createPath(outPath, relativePath);
//		
//		return new File(outPath,relativePath);
//	}
//	
//	static private boolean isABinaryNode(File file) {
//		if (file == null){
//			return false;
//		} else {
//			String fileName=file.getName();
//			String ext=FileUtils.getExtension(fileName);
//			
////			for (String binaryExtension:BINARY_EXTENSIONS){
////				if (fileName.endsWith(binaryExtension)){
////					return true;
////				}
////			}
////			return false;
//			
//			return BINARY_EXTENSIONS.containsKey(ext);
//		}
//	}	
//	static private boolean isAPropertiesFile(File file) {
//		return canThreatFileOrDir(file) && !file.isDirectory() && file.getName().endsWith(PROP);
//	}	
//
//	private static String getHtmlFileNameFromPropFileName(String fileName){
//		if (fileName==null){
//			return null;
//		} else if (fileName.length()<=PROP.length()){
//			return fileName;
//		} else {
//			String prefix=fileName.substring(0,fileName.length()-PROP.length());
//
//			return prefix+HTML;
//		}
//	}
//
//	private static boolean canThreatFileOrDir(File file) {
//		
//		if (file == null) {
//			return false;
//		} else if (!file.canRead()){
//			return false;
//		} 
//		
//		String fileName=file.getName();
//		
//		if (fileName.startsWith(DOT)){
//			return false;
//		} 
//
//		if (file.isDirectory()){
//			return true;
//		} else {
//			// Normal files			
//			if (fileName.endsWith(TILDE)){
//				return false;
//			} else if (GLOBAL_PROPERTIES_FILE_NAME.equals(fileName)){
//				return false;
//			} else if (fileName.endsWith(HTML)){
//				return true;
//			} else if (fileName.endsWith(PROP)){
//				return true;
//			} else if (fileName.endsWith(CSS)){
//				return true;
//			} else if (fileName.endsWith(JS)){
//				return true;
//			} else if (fileName.endsWith(MAP)){
//				return true;
//			} else if (isABinaryNode(file)){
//				return true;
//			} else {
//				// log.warn("File unknown:'"+fileName+"'");
//				return false;
//			}
//		}
//	}
//
//
//	public static void exitError(String error){
//		System.err.println(error);
//		log.fatal(error);
//		System.exit(1);
//	}
//
//	private static String iterateOverDIRS(Node parent,
//										  Node template,
//										  String relativePath){
//		File dir;
//		
//		if (relativePath!=null){
//
//			
////			if (relativePath.startsWith("/")){
//			// tratamos todos los paths como absluto
//			// TODO CAMBIARLO
//				dir=new File(pagesPath,relativePath);
//				
//				if (!dir.isDirectory()){
//					log.error("While getting the path:'"+relativePath+"' does not exists:"+dir.getAbsolutePath());
//					return "";
//				} else if (!dir.canRead()){
//					log.error("While getting the path:'"+relativePath+"' can not be readed:"+dir.getAbsolutePath());
//					return "";
//				} 
//			
//		} else {
//			dir=parent.context.getCurrentDir();
//		}
//		StringBuilder buffer=new StringBuilder();
//		
//		File array[]=dir.listFiles(CanThreadFileFilter.FILTER);
//		FileUtils.sortByName(array);
//		
//		
//		IteratorContext iteratorContext=new IteratorContext();
//		for (File file:array){
//			try {				
//
//				// The list already makes the call to canThreatFileOrDir
//				if (file.isDirectory()){
//					GlobalPropNode dirNode= new GlobalPropNode(file);
//					if (dirNode!=null){
//						dirNode.context.setParent(parent.context);
//						iteratorContext.setParent(dirNode.context);
//						iteratorContext.iterate();
//						String value=template.getContent(iteratorContext);
//						buffer.append(value);
//					}
//				}
//
//				
//			} catch (FileNotFoundException e) {
//				log.error("File:"+file.getAbsolutePath(),e);
//			}
//			
//		}
//
//		return buffer.toString();
//	}
//
//	private static String iterateOverDIR(Node parent,
//										 Node template,
//										 String relativePath){
//		if (relativePath!=null){			
//			// TODO IS /relativePath usee new File(pagesPath,relativePath) 
//			// TODO IS relativePath use new File(parent.context.getCurrentDir(),relativePath);
//
//			//			File dir=new File(pagesPath,relativePath);
//			File dir=new File(parent.context.getCurrentDir(),relativePath);
//				
//			if (!dir.isDirectory()){
//				log.error("While getting the path:'"+relativePath+"' does not exists:"+dir.getAbsolutePath());
//				return "";
//			} else if (!dir.canRead()){
//				log.error("While getting the path:'"+relativePath+"' can not be readed:"+dir.getAbsolutePath());
//				return "";
//			} else {
//				try {				
//					IteratorContext iteratorContext=new IteratorContext();
//					GlobalPropNode dirNode= new GlobalPropNode(dir);
//					if (dirNode!=null){
//						dirNode.context.setParent(parent.context);
//						iteratorContext.setParent(dirNode.context);
//						iteratorContext.iterate();
//						String value=template.getContent(iteratorContext);
//						return value;
//					}
//				} catch (FileNotFoundException e) {
//					log.error("File:"+dir.getAbsolutePath(),e);
//				}
//				return "";
//			}
//		} else {
//			log.error("relativePath :'"+relativePath+"' can not be null for iterate over DIR.");
//			return "";
//		}
//	}
//
//
//
//	public static final Pattern QUOTED_STRING=Pattern.compile("^\"(.+)\"$");
//	public static final Pattern SIMPLE_QUOTED_STRING=Pattern.compile("^\'(.+)\'$");
//
//	private static String unScapeFromBathPropFiles(String value){
//		if (value == null || "".equals(value)){
//			return value;
//		} else {
////			value=value.trim();
//
//			Matcher matcher = QUOTED_STRING.matcher(value);			
//			if (matcher.find()){
//				return matcher.group(1);
//			}
//			
//			matcher = SIMPLE_QUOTED_STRING.matcher(value);			
//			if (matcher.find()){
//				return matcher.group(1);
//			}
//			
//			return value;
//		}
//	}
//	
//	public static class CanThreadFileFilter implements FileFilter{
//
//		public static final CanThreadFileFilter FILTER=new CanThreadFileFilter();
//		
//		private CanThreadFileFilter(){
//			
//		}
//		
//		@Override
//		public boolean accept(File file) {
//			return canThreatFileOrDir(file);
//		}
//		
//	}
//}
