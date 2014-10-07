package eu.ginere.site;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.ginere.base.util.file.FileUtils;

/**
 * @author ventura
 *
 *  -Davem.common.util.properties.GlobalFileProperties.DefaultPath=/Users/ventura/projects/INDEXER/indexer-model-mysql/conf
 *
 */
public class Main {
	
	static final Logger log = Logger.getLogger(Main.class);
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("Process started at:"+new Date());
		
		if (args.length<3){
			exitError("Args: content_folder common_folder out_folder [daemon true/false [charset] ]");
		}
		
		File contentDir=new File(args[0]);
		//		File commanDir=new File(args[1]);
		String commanDirs=args[1];
		File outDir=new File(args[2]);

		// Optional args
		boolean daemon=false;		
		if (args.length >= 4){
			if ("true".equals(args[3])){
				daemon=true;
			}
		}
		
		String charset="UTF-8";
		if (args.length >= 5){
			charset=args[4];
			if (log.isInfoEnabled()){
				log.info("Using passed charset:"+charset);
			}
		} else {
			if (log.isInfoEnabled()){
				log.info("Using default charset:"+charset);
			}
		}
		
		// Verify paths
		if (! FileUtils.verifyDir(outDir,log)){
			exitError("Out path:"+outDir.getAbsolutePath());
		}

		if (! FileUtils.verifyReadDir(contentDir,log)){
			exitError("Content dir:"+contentDir.getAbsolutePath());
		}

//		if (! FileUtils.verifyReadDir(commanDir,log)){
//			exitError("Common dir:"+commanDir.getAbsolutePath());
//		}

		try {
			File commonArray[]=getArray(commanDirs);
			SiteGenerator template=new SiteGenerator(outDir,contentDir,commonArray,charset);

			template.generate(daemon);
			log.info("Process ended OK.");
			
			System.exit(0);
		}catch (Exception e) {
			log.fatal("Exception while generate sir",e);
			e.printStackTrace(System.err);
			exitError("Exception error:"+e.getMessage());			
		}	
		
	}
	
	public static void exitError(String error){
		System.err.println(error);
		log.fatal(error);
		System.exit(1);
	}

	public static File[] getArray(String paths){
		String array[]=StringUtils.split(paths, ',');
		File ret[]=new File[array.length];
		
		for (int i=0;i<array.length;i++){
			File file=new File(array[i]);
			ret[i]=file;
			
			if ( ! FileUtils.verifyReadDir(file,log) ){
				exitError("Content dir:"+file.getAbsolutePath());
			}			
		}
		
		return ret;
	}

}
