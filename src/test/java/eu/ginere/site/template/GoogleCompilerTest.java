package eu.ginere.site.template;

import eu.ginere.site.template.GoogleCompiler;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Test;

public class GoogleCompilerTest extends TestCase {

	static final Logger log = Logger.getLogger(GoogleCompilerTest.class);

	@Test
	static public void testConsulta() throws Exception {
		try {
			File file=new File(".");
			log.debug(file.getAbsolutePath());

			File in=new File("src/test/resources/application.js");
			log.debug(in.getAbsolutePath());

			File out=new File("src/test/resources/application-compiled.js");
			
			GoogleCompiler.compile(in,out);


			
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
	}

}
