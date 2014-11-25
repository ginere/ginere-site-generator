package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import eu.ginere.site.ContextProperties;
import eu.ginere.site.SiteGenerator;


public class ParseableTextNode extends Node {
	
	public ParseableTextNode(SiteGenerator globalContext,
							 File file,
							 boolean isPageFile) throws FileNotFoundException {
		
		super(globalContext,file,isPageFile);
		this.context=new ContextProperties(globalContext);				
	}

	@Override
	public long getLastModified(){
		return file.lastModified();
	}

	@Override
	public String getFileName() {
		return file.getName();
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
//		String stringToParse;
		try {
//			stringToParse = new Scanner(file,globalContext.charset).useDelimiter("\\Z").next();
			Scanner scanner=new Scanner(file,globalContext.charset);
			try {
				String stringToParse=scanner.useDelimiter("\\Z").next();
				
				return stringToParse;
			}catch(Throwable e){
				log.error("While oppening file:"+file+"["+file.getAbsolutePath()+"]",e);
				return "";				
			}finally{
				scanner.close();
			}
		}catch(FileNotFoundException e){
			log.error("While oppening file:"+file,e);
			return "";
		}
		
//		return stringToParse;
	}
}

