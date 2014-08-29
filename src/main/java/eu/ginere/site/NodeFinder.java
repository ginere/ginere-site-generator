package eu.ginere.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import eu.ginere.site.nodes.Node;

/**
 * from http://docs.oracle.com/javase/tutorial/essential/io/find.html
 * 
 */

public class NodeFinder extends SimpleFileVisitor<Path> {

	static final Logger log = Logger.getLogger(SiteGenerator.class);

	private final PathMatcher matcher;
	private final String globPattern;

	// private int numMatches = 0;

	private final SiteGenerator generator;
	private final List<Node> result = new Vector<Node>();

	private NodeFinder(SiteGenerator generator, String globPattern) {
		this.generator = generator;
		this.globPattern = globPattern;
		this.matcher = FileSystems.getDefault().getPathMatcher(
				"glob:" + globPattern);
	}

	// Compares the glob pattern against
	// the file or directory name.
	private void find(Path path) {

		if (matcher.matches(path)) {
			log.info("Path:'" + path + "' match:'" + globPattern + "'");
			try {
				Node node = generator.getFileNode(path.toFile());

				result.add(node);
				log.info("Error with path:" + path);
			} catch (Exception e) {
				log.error("Error with path:" + path, e);
			}
		} else {
			log.info("Path:'" + path + "' do not match glob pattern:'"
					+ globPattern + "'");
		}
	}

	// Invoke the pattern matching
	// method on each file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		find(file);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path path, IOException exc) {
		log.error("While visiting path:" + path, exc);
		return FileVisitResult.CONTINUE;
	}
	

	public static Node[] getResultArray(final SiteGenerator generator,
			final File file, final String globPattern) throws IOException {

		Path startingDir = file.toPath();
		NodeFinder finder = new NodeFinder(generator, globPattern);

		log.debug("Startd with path:'"+startingDir+"' and globPattern:'"+globPattern+"'");
		Files.walkFileTree(startingDir, finder);

		List<Node> result = finder.result;

		return result.toArray(new Node[result.size()]);
	}
}
