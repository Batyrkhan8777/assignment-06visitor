package filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {
    private DirectoryNode root;
    private DirectoryNode documents;
    private DirectoryNode pictures;

    @BeforeEach
    void setUp() {
        root = new DirectoryNode("root");
        documents = new DirectoryNode("docs");
        pictures = new DirectoryNode("pics");

        root.addChild(documents);
        root.addChild(pictures);

        documents.addChild(new FileNode("report.pdf", 1500));
        documents.addChild(new FileNode("budget.xlsx", 500));
        pictures.addChild(new FileNode("vacation.png", 2000));

        DirectoryNode secret = new DirectoryNode("secret");
        secret.addChild(new FileNode("passwords.txt", 100));
        documents.addChild(secret);
    }

    @Test
    void t1_t2_testTotalSizeVisitor() { // T1, T2: Проверка диспетчеризации и накопления размера
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        assertEquals(4100, sizeVisitor.getTotalSize());
    }

    @Test
    void t3_t4_testSearchVisitor() { // T3, T4: Поиск по 2 критериям, детерминированные относительные пути
        SearchVisitor searchVisitor = new SearchVisitor("t", 200);
        // Должен найти budget.xlsx, vacation.png, report.pdf, НО passwords.txt (100) не пройдет по размеру
        root.accept(searchVisitor);
        List<String> paths = searchVisitor.getFoundPaths();

        assertEquals(2, paths.size());
        assertEquals("root/docs/budget.xlsx", paths.get(0)); // Лексикографический порядок
        assertEquals("root/docs/report.pdf", paths.get(1));
    }

    @Test
    void t5_testStatisticsVisitor() { // T5: Статистика, счетчики и глубина
        StatisticsVisitor stats = new StatisticsVisitor();
        root.accept(stats);

        assertEquals(4, stats.getFileCount());
        assertEquals(4, stats.getDirectoryCount()); // root, docs, pics, secret
        assertEquals(2, stats.getMaxDepth()); // root(0) -> docs(1) -> secret(2) -> file(2)
    }

    @Test
    void t6_testEmptyDirectoryAndNoMatchSearch() { // T6: Пустые структуры и отсутствие результатов
        DirectoryNode emptyRoot = new DirectoryNode("empty");
        SearchVisitor searchVisitor = new SearchVisitor("ghost", 0);
        emptyRoot.accept(searchVisitor);
        assertTrue(searchVisitor.getFoundPaths().isEmpty());

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        emptyRoot.accept(sizeVisitor);
        assertEquals(0, sizeVisitor.getTotalSize());
    }

    @Test
    void t7_testRepeatExecutionsDoNotLeakState() { // T7: Повторный запуск не удваивает результаты
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        long firstRun = sizeVisitor.getTotalSize();

        sizeVisitor.reset(); // Очистка состояния перед новым запуском
        root.accept(sizeVisitor);
        long secondRun = sizeVisitor.getTotalSize();

        assertEquals(firstRun, secondRun);
    }

    @Test
    void t8_testInvalidMutationsRejected() { // T8: Защита от инвалидных данных и нарушения целостности
        assertThrows(IllegalArgumentException.class, () -> new FileNode("neg.txt", -50)); // Отрицательный размер
        assertThrows(IllegalArgumentException.class, () -> root.addChild(new FileNode("docs", 10))); // Дубликат имени
        assertThrows(IllegalStateException.class, () -> pictures.addChild(documents)); // Множественное родительство
        assertThrows(IllegalArgumentException.class, () -> documents.addChild(root)); // Образование цикла
    }
}