package filesystem;

public class FileNode implements Element {
    private final String name;
    private final long size;
    private DirectoryNode parent;

    public FileNode(String name, long size) {
        if (size < 0) {
            throw new IllegalArgumentException("Размер файла не может быть отрицательным.");
        }
        this.name = name;
        this.size = size;
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

    public long getSize() { return size; }
}