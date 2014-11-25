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


public class ExamplesTesterChildDirs extends TestCase {
	static final Logger log = Logger.getLogger(ExamplesTesterChildDirs.class);

	static final String CHARSET = "UTF-8";
	static final String DEFAULT_TEXT = "";

	@Test
	static public void testExamples() throws Exception {

		try {
			File examplesFolder=new File("examples-childs/");
			TestCase.assertTrue(FileUtils.verifyReadDir(examplesFolder,log));

			FileUtils.iterateOnChildDirs(examplesFolder, new FileIterator() {				
				@Override
				public boolean iterate(File file) {
					log.info("Testing example:"+file.getName());
					
					File common1=new File(file,"common-1");
					File common2=new File(file,"common-2");
					File content=new File(file,"content");
					File out=new File(file,"out");
					File expected=new File(file,"expected");
					FileUtils.deleteFilesOfDir(out);
					
					TestCase.assertTrue(FileUtils.verifyReadDir(common1,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(common2,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(content,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(out,log));
					TestCase.assertTrue(FileUtils.verifyReadDir(expected,log));
					
					// Executiong the test on those files
					File commonArray[]=new File[]{common1,common2};
					SiteGenerator generator=new SiteGenerator(out,content,commonArray,CHARSET);
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

	private static boolean verifyEquals(final File generatedDir, 
										final File expectedDir,
										final HashSet<File> cache) {

//		final Boolean ret=Boolean.FALSE;
		boolean ret=
		FileUtils.iterateFiles(generatedDir, new FileIterator() {				
				@Override
				public boolean iterate(File generatedFile) {
					if (cache.contains(generatedFile)){
						return true;
					} else {
						log.info("Iterating over child:"+generatedFile);

						File expectedFile=new File(expectedDir,generatedFile.getName());
						
						if (!expectedFile.exists()){
							log.error("File "+generatedFile.getAbsolutePath()+" do not exits in:"+expectedDir.getAbsolutePath());
							return false;
						}
						
						if (!expectedFile.canRead()){
							log.error("File "+generatedFile.getAbsolutePath()+" exits but can not be readed in:"+expectedDir.getAbsolutePath());
							return false;
						}


						if (generatedFile.isDirectory()){
							if (!expectedFile.isDirectory()){
								log.error("Dir "+generatedFile.getAbsolutePath()+" exits but is not a directory in:"+expectedDir.getAbsolutePath());
								return false;
							}

							boolean ret=verifyEquals(generatedFile,expectedFile,cache);

							if (ret){
								cache.add(generatedFile);
								cache.add(expectedFile);
								return true;
							} else {
								log.error("Dirs are different:"+generatedFile.getAbsolutePath()+"' dest:"+expectedFile.getAbsolutePath()+"'");

								return false;
							}
						} else {
							// IS a file 
							String generatedTex=FileUtils.getString(generatedFile,DEFAULT_TEXT,CHARSET);
							String expectedText=FileUtils.getString(expectedFile,DEFAULT_TEXT,CHARSET);

							if (generatedTex.equals(expectedText)){
								cache.add(generatedFile);
								cache.add(expectedFile);
								return true;
							} else {
								log.error("Files are different:"+generatedFile.getAbsolutePath()+"' dest:"+expectedFile.getAbsolutePath()+"'");
								log.error("Generated:["+generatedTex+"]");
								log.error("Expected:["+expectedText+"]");

								return false;
							}

						}
						
					}
				}
			});
		

		return ret;
	}

	

}
