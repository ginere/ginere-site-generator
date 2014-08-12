package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.site.ContextProperties;
import eu.ginere.site.SiteGenerator;


public class GlobalPropNode extends Node {

	static final Logger log = Logger.getLogger(GlobalPropNode.class);

	public static final String GLOBAL_PROPERTIES_FILE_NAME = "GlobalProperties.prop";

	private final File dir;
	private final File propFile;

	public GlobalPropNode(SiteGenerator globalContext,File dir) throws FileNotFoundException {
		super(globalContext,dir,false);

		File propFile=new File(dir,GLOBAL_PROPERTIES_FILE_NAME);
		if (FileUtils.canReadFile(propFile)){
			this.context=new ContextProperties(globalContext,propFile);
			this.propFile=propFile;
		} else {
			this.context=new ContextProperties(globalContext);
			this.propFile=null;
		}
		
		
		this.dir=dir;
	}

	@Override
	public long getLastModified(){
		if (propFile==null){
			return file.lastModified();
		} else {
			return Math.max(file.lastModified(),propFile.lastModified());
		}
	}
	
	
	@Override
	public String getContent(ContextProperties parentContext) throws FileNotFoundException {
		return "";
	}

	@Override
	public String getFileName() {
		return null;
	}
	
	/**
	 * IF the file is ../content/folder1/folder2/index.html, that will retun /folder1/forlder2
	 * @return
	 */
	@Override
	public String getRelativePath(){
		return null;
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
	@Override
	public void generateOrUpdateDiskFile(ContextProperties context) throws IOException {
		log.warn("Trying to generate disk file information for a GlobalProperties Node:"+this);
		return ;
	}
	
	public void checkForUpdates(ContextProperties parentContext) {
//			log.info(this+":Checking for updates ... ");

		context.setParent(parentContext);
		
		File array[]=dir.listFiles();
		List <Node>list=new ArrayList<Node>(array.length);
		
		for (File file:array){
			try {				
				if (SiteGenerator.canThreatFileOrDir(file)){
					if (file.isDirectory()){
						GlobalPropNode dirChildNode = (GlobalPropNode)globalContext.getFileNode(file,false);
						if (dirChildNode!=null){
							dirChildNode.checkForUpdates(this.context);
							list.add(dirChildNode);
//								log.info(dirChildNode+":Child DIR node added to:"+this);

						}
					} else {
						Node fileNode = globalContext.getFileNode(file,true);
						if (fileNode!=null){
							list.add(fileNode);
//								log.info(fileNode+":File node added to:"+this);
						}
					}
				}
			} catch (FileNotFoundException e) {
				log.error("File:"+file.getAbsolutePath(),e);
			}

		}
	
//			childs=list;

		for (Node child:list){
			try {
				if (child.isPage()){
					// solo guardamos en disco los nodo que son paginas
					child.generateOrUpdateDiskFile(context);
				}
			} catch (IOException e) {
				log.error("File:"+file.getAbsolutePath(),e);
			}
		}
	}
}
