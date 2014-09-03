package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;

import eu.ginere.site.ContextProperties;
import eu.ginere.site.SiteGenerator;


public class PropNode extends Node {

	public static final String TEMPLATE_PROPERTY = "template";

	public PropNode(SiteGenerator globalContext,
					File file,
					boolean isPageFile) throws FileNotFoundException {
		
		super(globalContext,file,isPageFile);
		this.context=new ContextProperties(globalContext,file);
	}
	
	@Override
	public long getLastModified(){
		return file.lastModified();
	}
		
	@Override
	public String getFileName() {
		return getHtmlFileNameFromPropFileName(file.getName());
	}
	
	/**
	 * IF the file is ../content/folder1/folder2/index.html, that will retun /folder1/forlder2
	 * @return
	 * @throws FileNotFoundException 
	 */
	@Override
	public String getRelativePath() throws FileNotFoundException{
		return globalContext.getRelativePath(file);
	}
	
	/**
	 * Use this method to get the string to parse of this node.
	 * The list pased in param is the list where the childs found must be added
	 * @param list
	 * @return
	 */
	@Override
	protected String getStringToParse() {
		String fileName=context.getValue(TEMPLATE_PROPERTY,this);
		String stringToParse;
		
		if ("".equals(fileName)){
			log.warn("No template property defined for node:"+this);					
			stringToParse="";
		} else {
			try {
				Node child=globalContext.getFileNode(globalContext.getFileFromFileName(fileName));
				if (child!=null){
					stringToParse=child.getContent(this.context);
					addChild(child);
				} else {
					stringToParse="";
				}
			}catch (FileNotFoundException e) {
				log.error("Error getting file:'"+fileName+"'",e);
				stringToParse="";
			}
		}

		return stringToParse;
	}

	static public boolean isAPropertiesFile(File file) {
		return SiteGenerator.canThreatFileOrDir(file) && !file.isDirectory() && file.getName().endsWith(SiteGenerator.PROP);
	}	


	private static String getHtmlFileNameFromPropFileName(String fileName){
		if (fileName==null){
			return null;
		} else if (fileName.length()<=SiteGenerator.PROP.length()){
			return fileName;
		} else {
			String prefix=fileName.substring(0,fileName.length()-SiteGenerator.PROP.length());
			
			return prefix+SiteGenerator.HTML;
		}
	}
}

