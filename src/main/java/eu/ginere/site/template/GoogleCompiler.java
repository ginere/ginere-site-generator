package eu.ginere.site.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.Compiler;

public class GoogleCompiler {

	static final Logger log = Logger.getLogger(GoogleCompiler.class);
//	private static final String COMPILED_EXTENSION = ".compiled";


	static public boolean compile(File inFile,File outFile) throws FileNotFoundException, UnsupportedEncodingException {
		// The google closure compiler
		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();
		// Advanced mode is used here, but additional options could be set, too.
//		CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

		
		// To get the complete set of externs, the logic in
		// CompilerRunner.getDefaultExterns() should be used here.
//		JSSourceFile extern = JSSourceFile.fromFile(inFile);

		
		// The imput File
		JSSourceFile input = JSSourceFile.fromFile(inFile);

		// compile() returns a Result, but it is not needed here.
		Result result=compiler.compile(JSSourceFile.fromCode("extern.js", ""), input, options);
		
		printResult(result);
		
		if (result.success){
			PrintWriter out = new PrintWriter(outFile,FileTemplates.CHARSET);
			try {
				out.write(compiler.toSource());
				return true;
			}finally {
				IOUtils.closeQuietly(out);
			}
		} else {
			return false;
		}
		
	}
		/*	
	static public boolean compile(File inFile,File outFile,String options)  throws IOException {
		String command="java";
		String classpath="/Users/ventura/projects/bluetooth/compiler-web/google-compiler/compiler.jar";

		//		java -jar compiler.jar --js hello.js --js_output_file hello-compiled.js
		// java -jar /Users/ventura/projects/bluetooth/compiler-web/google-compiler/compiler.jar --js /Users/ventura/tmp/js/_bo.js --js_output_file /Users/ventura/tmp/js/toto.js
		StringBuilder buffer=new StringBuilder();

		buffer.append(command);
		buffer.append(" -jar ");
		buffer.append(classpath);
		
		if (options!=null){
			buffer.append(" --compilation_level ");
			buffer.append(options);	
		}
		buffer.append(" --js ");
		buffer.append(inFile.getAbsolutePath());
		buffer.append(" --js_output_file ");
		buffer.append(outFile.getAbsolutePath());

//		String array[]=new String[]{
//			command,
//			buffer.toString()
//		};

		log.info("Compiling files:'"+inFile.getName()+" options:"+options+" ...");
		boolean ret=compile(buffer.toString(),inFile.getName());
		log.info("FILE COMPILED :'"+inFile.getName()+"' options:"+options+".");
		
		return ret;
	}

	static private boolean compile(String command,String msg) throws IOException{
		Runtime rt = Runtime.getRuntime();
		rt.traceInstructions(true);
		rt.traceMethodCalls(true);
		
		Process pr = rt.exec(command);
		
//		if (log.isInfoEnabled()){
//			log.info("Exec command:'"+command+"' "+msg+"' ...");				
//		}



		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			log.info("Exec DONE:"+msg,e);			
		}

		IOUtils.copy(pr.getErrorStream(), System.out);
		IOUtils.copy(pr.getInputStream(), System.err);
		

//		if (log.isInfoEnabled()){
//			log.info("Exec DONE:"+msg+" status:"+pr.exitValue());
//		}

		if (pr.exitValue() == 0){
			return true;
		} else {
			return false;
		}
	}

	public static File compile(File src,String options) throws IOException {
		File outFile=new File(src.getAbsoluteFile()+COMPILED_EXTENSION);
		
		if (compile(src, outFile,options)){
			return FileUtils.getReadableFile(outFile.getAbsolutePath(), null);
		} else {
			return null;
		}
		
	}
		*/


	private static void printResult(Result result) {
		if (result.success){
			log.info("Compilation Success.");
		} else {
			log.error("COMPILATION ERROR.");			
		}
		
		if (result.errors.length == 0) {
			log.info("No errors");	
		} else {
			for (JSError error:result.errors){
//				log.error("COMPILER_ERROR:"+error.description+" "+error.lineNumber+" "+error.sourceName+" "+error);
				log.error("COMPILER_ERROR:"+error);
			}
		}

		if (result.warnings.length == 0) {
			log.info("No warn");	
		} else {
			for (JSError error:result.warnings){
				log.warn("Warning:"+error.description+" "+error.lineNumber+" "+error.sourceName+" "+error);
			}
		}
		
		//		log.warn("Resul:"+result);

	}

}
