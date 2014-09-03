package eu.ginere.site;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.base.util.properties.FileProperties;
import eu.ginere.site.nodes.Node;

/**
 * @author Ginere.eu
 *
 * Correspont width a nothe into a evaluation tree.
 * The parent may change ...
 * puede ser un root Folder -> folde -> file.prop - > file.prop
 */
public class ContextProperties{
	static Logger log = Logger.getLogger(ContextProperties.class);

	public static final String PAGE_NAME="pageName";
	public static final String PAGE_PATH="pagePath";

	public static final String FILE_NAME="fileName";
	public static final String FILE_PATH="filePath";

	public static final String CONTEXT_PATH="contextPath";

	protected final SiteGenerator globalContext;

	// The GlobalFileProperties 
	FileProperties fileProperties;
	
	// The parent context, correspond with the context of the parent folder if any.
	protected ContextProperties parent=null;

	// ????
	private Node pageNode=null;

	public ContextProperties(SiteGenerator globalContext,File file) {
		this.globalContext=globalContext;

		if (!FileUtils.canReadFile(file,log)){
			this.fileProperties=null;			
		} else {
			try {
				this.fileProperties=new FileProperties(file,globalContext.charset);
			} catch (IOException e) {
				log.error("Warning file:"+file,e);
				this.fileProperties=null;				
			}
		}
	}

	public ContextProperties(SiteGenerator globalContext) {
		this.globalContext=globalContext;

		this.fileProperties=null;
	}

	@Override
	public String toString(){
		String current;
		
		if (fileProperties != null){
			current=FileUtils.getRelativePath(fileProperties.getFildes(), globalContext.contentDir,"Not In Context");
		} else {
			current=" --- ";
		}
		if (parent == null){
			return "["+current+"]";
		} else {
			return "["+current+parent+"]";				
		}
	}

	public void setPageNode(Node node) {
		this.pageNode=node;		
	}
	
	/**
	 * Returns the first page nothe on the hirarhie of that context
	 * @return
	 */
	public Node getPageNode() {
		if (this.pageNode !=null){
			return this.pageNode;
		} else {
			return getParent().getPageNode();
		}
	}


	public File getCurrentDir() {			

		// TODO verificar que es esto ...
		if (this.fileProperties != null && this.fileProperties.getFildes() != null){
			return this.fileProperties.getFildes().getParentFile();
		} else if (this.parent!=null){
			return this.parent.getCurrentDir();
		} else {
			log.error("RETURNING NULL from context:"+this);
			return null;
		}
	}

	/**
	 * Returns true if this context has been modified after or at this date.
	 * 
	 * @param lastModified
	 * @return
	 */
	public boolean hasBeenModified(long lastModified) {
		if (fileProperties != null){
			// Update the file last modified and the properties
			fileProperties.checkForModification();
			if (lastModified<=fileProperties.getLastModified()){
				return true;
			} 
		}

		// If this node has not been modifief look into the parent one
		if (parent!=null){
			return parent.hasBeenModified(lastModified);
		} else {
			return false;
		}
	}

	public String getValue(String propertyName,Node currentNode) {
		String ret=null;

		if (PAGE_NAME.equals(propertyName)){
			Node pageNode=getPageNode();
			if (pageNode!=null){
				return pageNode.getFileName();
			}
		}

		if (PAGE_PATH.equals(propertyName)){
			Node pageNode=getPageNode();
			if (pageNode!=null){
				return pageNode.getRelativePath(null);
			}
		}

		if (FILE_NAME.equals(propertyName)){
			return currentNode.getFileName();
		}

		
		if (FILE_PATH.equals(propertyName)){
			return currentNode.getRelativePath(null);
		}

		if (CONTEXT_PATH.equals(propertyName)){
			try {
				return globalContext.getRelativePath(getCurrentDir());
			} catch (FileNotFoundException e) {
				log.error("Getting context path for:"+getCurrentDir(),e);
				return null;
			}
		}

		if (this.fileProperties != null){
			ret=this.fileProperties.getStringValue(ContextProperties.class, propertyName, null);
		}
		
		// The global context properties are only used into the page root nodes
		if (ret==null && parent!=null){
			
			AQUI entra en un bucle recursivo ....
			ret=parent.getValue(propertyName,currentNode);
			// el Global no puede devolver null;
			if (ret==null){
				return "";
			}
		}
		
		if (ret==null){
			return "";
		} else {
			return PatternUtils.unScapeFromBathPropFiles(ret);
		}
	}

	public void setParent(ContextProperties parentContext) {
		this.parent=parentContext;
	}

	public ContextProperties getParent() {
		return parent;
	}

	/**
	 * Parse the string passed in parameter and replace the variables found by its values.
	 * 
	 * @param stringToParse
	 * 
	 * @return The original String widht the values of the variables replaced
	 */
	public String parseVariables(String stringToParse,Node currentNode) {
		if (stringToParse == null || "".equals(stringToParse)){
			return "";
		} else {
			// First  parseamos las variables $VARIABLE$
			StringBuffer buffer=new StringBuffer();
			Matcher matcher = PatternUtils.VARIABLE_TOKEN_PATER.matcher(stringToParse);
			while (matcher.find()) {
				// returns the nave of the variable
				String token=matcher.group(1);
				
				// returns the name
				String value=getValue(token,currentNode);
				try {
					matcher.appendReplacement(buffer, value);
				}catch(IllegalArgumentException e){
					log.error("For token:"+token+" and value:"+value+"'",e);
				}

			}
			matcher.appendTail(buffer);
			
//			// then parse the global variables...
//			matcher = PatternUtils.DATE_TOKEN_PATER.matcher(buffer);
//			while (matcher.find()) {
//				// returns the nave of the variable
//				String format=matcher.group(1);
////				String format=token.substring(5,token.length()-2);
//				log.debug("Format:"+format);
//				
//				String value;
//				Date now=new Date();
//				
//				if (format == null || "".equals(format)){
//					value=now.toString();
//				} else {
//					try {
//						SimpleDateFormat sdf=new SimpleDateFormat(format);
//						value=sdf.format(now);
//					}catch(Exception e){
//						value="";
//						log.error("Format:"+format+" and date:"+now+"'",e);
//					}
//				}
//					
//				try {
//					matcher.appendReplacement(buffer, value);
//				}catch(IllegalArgumentException e){
//					log.error("For token:"+format+" and value:"+value+"'",e);
//				}	
//			}			
//			matcher.appendTail(buffer);
			
			return buffer.toString();
		}
	}

	public String parseGlobalVariables(String stringToParse,Node currentNode) {
		if (stringToParse == null || "".equals(stringToParse)){
			return "";
		} else {
			// then parse the global variables...
			StringBuffer buffer=new StringBuffer();
			Matcher matcher = PatternUtils.DATE_TOKEN_PATER.matcher(stringToParse);
			while (matcher.find()) {
				// returns the nave of the variable
				String format=matcher.group(1);
//					String format=token.substring(5,token.length()-2);
				log.debug("Format:"+format);
				
				String value;
				Date now=new Date();
				
				if (format == null || "".equals(format)){
					value=now.toString();
				} else {
					try {
						SimpleDateFormat sdf=new SimpleDateFormat(format);
						value=sdf.format(now);
					}catch(Exception e){
						value="";
						log.error("Format:"+format+" and date:"+now+"'",e);
					}
				}
					
				try {
					matcher.appendReplacement(buffer, value);
				}catch(IllegalArgumentException e){
					log.error("For token:"+format+" and value:"+value+"'",e);
				}	
			}			
			matcher.appendTail(buffer);
			
			return buffer.toString();
		}
	}

	public String getCharSet() {		
		return globalContext.charset;
	}
}
