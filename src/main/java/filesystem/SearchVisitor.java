
package filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class SearchVisitor implements Visitor {
    private final String nameCriterion;
    private final long minSizeThreshold;
    private final Stack<String> pathStack = new Stack<>();
    private final List<String> foundPaths = new ArrayList<>();

    public SearchVisitor(String nameCriterion, long minSizeThreshold) {
        this.nameCriterion = nameCriterion;
        this.minSizeThreshold = minSizeThreshold;
    }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(nameCriterion) && file.getSize() >= minSizeThreshold) {
            String fullPath = String.join("/", pathStack) + "/" + file.getName();
            foundPaths.add(fullPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        pathStack.push(directory.getName());
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        pathStack.pop();
    }

    public List<String> getFoundPaths() {
        List<String> sortedPaths = new ArrayList<>(foundPaths);
        Collections.sort(sortedPaths); // Лексикографическая сортировка
        return sortedPaths;
    }

    public void reset() {
        pathStack.clear();
        foundPaths.clear();
    }
}