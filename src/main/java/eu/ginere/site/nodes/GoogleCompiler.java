package eu.ginere.site.nodes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Result;

public class GoogleCompiler {

	static final Logger log = Logger.getLogger(GoogleCompiler.class);
//	private static final String COMPILED_EXTENSION = ".compiled";


	static public boolean compile(File inFile,File outFile,String charset,boolean advanced) throws FileNotFoundException, UnsupportedEncodingException {
		// The google closure compiler
		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();
		// Advanced mode is used here, but additional options could be set, too.
		if (advanced){
			CompilationLevel.ADVANCED_OPTIMIZATIONS.setOptionsForCompilationLevel(options);			
		} else {
			CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
		}
		
		// To get the complete set of externs, the logic in
		// CompilerRunner.getDefaultExterns() should be used here.
//		JSSourceFile extern = JSSourceFile.fromFile(inFile);

		
		// The imput File
		JSSourceFile input = JSSourceFile.fromFile(inFile);

		// compile() returns a Result, but it is not needed here.
		Result result=compiler.compile(JSSourceFile.fromCode("extern.js", ""), input, options);
		
		printResult(result);
		
		if (result.success){
			PrintWriter out = new PrintWriter(outFile,charset);
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
