package filesystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryNode implements Element {
    private final String name;
    private DirectoryNode parent;
    private final List<Element> children = new ArrayList<>();

    public DirectoryNode(String name) {
        this.name = name;
    }

    public void addChild(Element child) {
        if (child.getParent() != null) {
            throw new IllegalStateException("Элемент уже имеет родителя.");
        }
        if (children.stream().anyMatch(c -> c.getName().equals(child.getName()))) {
            throw new IllegalArgumentException("Элемент с таким именем уже существует в директории.");
        }
        if (child instanceof DirectoryNode && hasCycle((DirectoryNode) child)) {
            throw new IllegalArgumentException("Добавление создаст цикл в дереве.");
        }

        child.setParent(this);
        children.add(child);
    }

    private boolean hasCycle(DirectoryNode potentialChild) {
        Element current = this;
        while (current != null) {
            if (current == potentialChild) return true;
            current = current.getParent();
        }
        return false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this); // Double dispatch
    }

    @Override
    public String getName() { return name; }

    @Override
    public DirectoryNode getParent() { return parent; }

    @Override
    public void setParent(DirectoryNode parent) { this.parent = parent; }

    public List<Element> getChildren() {
        return Collections.unmodifiableList(children); // incaps
    }
}