package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.site.ContextProperties;
import eu.ginere.site.SiteGenerator;


public class BinaryNode extends Node {
	private final File out;

	public BinaryNode(SiteGenerator globalContext,
					  File file) throws FileNotFoundException {
		
		super(globalContext,file,true);
		this.context=new ContextProperties(globalContext);				

		this.out=new File(globalContext.getOutpath(this),file.getName());
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
	 */
	@Override
	public String getRelativePath(){
		return globalContext.getRelativePath(file);
	}

	@Override
	public void generateOrUpdateDiskFile(ContextProperties context) throws IOException {
		if (out.lastModified()<= file.lastModified() ){
			// copy file
			FileUtils.copyFile(file, out);
			log.info("OK: "+out.getAbsoluteFile());
		}

//			return out.lastModified();
	}

	@Override
	protected String updateContent() throws FileNotFoundException {
		return "";
	}
	
	/**
	 * Use this method to get the string to parse of this node.
	 * The list pased in param is the list where the childs found must be added
	 * @param list
	 * @return
	 */
	@Override
	protected String getStringToParse() {
		return "";
	}
}
	
