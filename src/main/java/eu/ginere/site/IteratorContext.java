package eu.ginere.site;

import java.io.File;

import eu.ginere.site.nodes.Node;


public class IteratorContext extends ContextProperties{
	long lastIteration=0;
	int iteration=0;

	private static final String CONTEXT_PROPERTY_NAME_INDEX="Index";

	IteratorContext(SiteGenerator globalContext) {
		super(globalContext);
	}

	@Override
	public String toString(){
		String current=" Iterator ";
		if (parent == null){
			return "["+current+"]";
		} else {
			return "["+current+parent+"]";				
		}
	}

	public File getCurrentDir() {			
		return parent.getCurrentDir();
	}

	public void iterate() {			
		iteration++;
		lastIteration=System.currentTimeMillis();
	}
	
	public boolean hasBeenModified(long lastModified) {
		if (lastIteration >= lastModified){
			return true;
		} else if (parent!=null){
			return parent.hasBeenModified(lastModified);
		} else {
			return false;
		}
	}

	@Override
	public String getValue(String propertyName,Node currentNode) {
		if (CONTEXT_PROPERTY_NAME_INDEX.equals(propertyName)){
			return Integer.toString(iteration);
		} else {
			if (parent!=null){
				return parent.getValue(propertyName,currentNode);
			} else {
				return null;
			}
		}
	}

}
	
