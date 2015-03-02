package eu.ginere.site.template;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.ginere.base.util.file.FileUtils;
import eu.ginere.site.NodeFinder;
import eu.ginere.site.SiteGenerator;
import eu.ginere.site.nodes.Node;

public class NodeFinderTest {

	static final Logger log = Logger.getLogger(NodeFinderTest.class);
	
	static final String CHARSET = "UTF-8";

	@Test
	public void mainTest() throws Exception {
		try {
            Node[] ret;

            File file=new File("examples/example1");
            File common=new File(file,"common");
            File content=new File(file,"content");
            File out=new File(file,"out");
            File expected=new File(file,"expected");
			SiteGenerator generator=new SiteGenerator(out,content,common,CHARSET);

			TestCase.assertTrue(FileUtils.verifyReadDir(content,log));
			
			ret=NodeFinder.getResultArray(generator, content, "**");
			
			TestCase.assertTrue(ret.length==1);
			
			
//			ret=NodeFinder.getResultArray(generator, new File("."), "**");
			ret=NodeFinder.getResultArray(generator, content, "**");
			
			
			
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}
	
//	@Test
//	public void extensionTest() throws Exception {
//		try {
//            Node[] ret;
//
//            File file=new File("examples/example1");
//            File common=new File(file,"common");
//            File content=new File(file,"content");
//            File out=new File(file,"out");
//            File expected=new File(file,"expected");
//			SiteGenerator generator=new SiteGenerator(out,content,common,CHARSET);
//
//			TestCase.assertTrue(FileUtils.verifyReadDir(content,log));
//			
//			ret=NodeFinder.getResultArray(generator, content, "**");
//			
//			TestCase.assertTrue(ret.length==1);
//			
//			
////			ret=NodeFinder.getResultArray(generator, new File("."), "**");
//			ret=NodeFinder.getResultArray(generator, content, "**");
//			
//			
//			
//		} catch (Exception e) {
//			log.error("", e);
//			throw e;
//		}
//	}
}
