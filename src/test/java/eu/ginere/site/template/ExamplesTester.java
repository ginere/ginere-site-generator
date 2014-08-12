package eu.ginere.site.template;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.base.util.file.FileUtils.FileIterator;
import eu.ginere.site.SiteGenerator;


public class ExamplesTester extends TestCase {
	static final Logger log = Logger.getLogger(ExamplesTester.class);

	static final String CHARSET = "UTF-8";
	static final String DEFAULT_TEXT = "";

	@Test
	static public void testExamples() throws Exception {

		try {
			File examplesFolder=new File("examples/");
			TestCase.assertTrue(FileUtils.verifyReadDir(examplesFolder,log));

			FileUtils.iterateOnChildDirs(examplesFolder, new FileIterator() {				
				@Override
				public boolean iterate(File file) {
					log.info("Testing example:"+file.getName());
					
					File common=new File(file,"common");
					File content=new File(file,"content");
					File out=new File(file,"out");
					File expected=new File(file,"expected");
					FileUtils.deleteFilesOfDir(out);
					
					TestCase.assertTrue(FileUtils.verifyReadDir(common,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(content,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(out,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(expected,log));
					
					// Executiong the test on those files
					
					SiteGenerator generator=new SiteGenerator(out,content,common,CHARSET);
					try {
						generator.generate(false);
						TestCase.assertTrue("Are not equals:"+file.getName(),verifyEqualsBoth(out,expected));
						return true;

					} catch (FileNotFoundException e) {
						log.error("",e);
						return false;
					}
					
				}
			});
			
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}		

	static public boolean verifyEqualsBoth(File outDir,File expedted) {
		HashSet<File> cache=new HashSet<File>();

		if (verifyEquals(outDir,expedted,cache)){
			return verifyEquals(expedted,outDir,cache);
		} else {
			return false;
		}
	}

	private static boolean verifyEquals(final File org, 
										final File target,
										final HashSet<File> cache) {

//		final Boolean ret=Boolean.FALSE;
		boolean ret=
		FileUtils.iterateFiles(org, new FileIterator() {				
				@Override
				public boolean iterate(File file) {
					if (cache.contains(file)){
						return true;
					} else {
						log.info("Iterating over child:"+file);

						File equivalent=new File(target,file.getName());
						
						if (!equivalent.exists()){
							log.error("File "+file.getAbsolutePath()+" do not exits in:"+target.getAbsolutePath());
							return false;
						}
						
						if (!equivalent.canRead()){
							log.error("File "+file.getAbsolutePath()+" exits but can not be readed in:"+target.getAbsolutePath());
							return false;
						}


						if (file.isDirectory()){
							if (!equivalent.isDirectory()){
								log.error("Dir "+file.getAbsolutePath()+" exits but is not a directory in:"+target.getAbsolutePath());
								return false;
							}

							boolean ret=verifyEquals(file,equivalent,cache);

							if (ret){
								cache.add(file);
								cache.add(equivalent);
								return true;
							} else {
								log.error("Dirs are different:"+file.getAbsolutePath()+"' dest:"+equivalent.getAbsolutePath()+"'");

								return false;
							}
						} else {
							// IS a file 
							String originalText=FileUtils.getString(file,DEFAULT_TEXT,CHARSET);
							String targetText=FileUtils.getString(equivalent,DEFAULT_TEXT,CHARSET);

							if (originalText.equals(targetText)){
								cache.add(file);
								cache.add(equivalent);
								return true;
							} else {
								log.error("Files are different:"+file.getAbsolutePath()+"' dest:"+equivalent.getAbsolutePath()+"'");
								log.error("["+originalText+"]");
								log.error("["+targetText+"]");

								return false;
							}

						}
						
					}
				}
			});
		

		return ret;
	}

	

}
