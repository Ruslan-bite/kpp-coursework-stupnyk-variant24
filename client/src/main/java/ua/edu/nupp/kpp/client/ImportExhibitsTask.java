package ua.edu.nupp.kpp.client;

import javafx.concurrent.Task;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportExhibitsTask extends Task<Integer> {
    private static final Logger LOG = Logger.getLogger(ImportExhibitsTask.class.getName());
    private final Path file;
    private final HttpMuseumExhibitsClient api;

    public ImportExhibitsTask(Path file, HttpMuseumExhibitsClient api) {
        this.file = file;
        this.api = api;
    }

    @Override
    protected Integer call() throws Exception {
        LOG.info("Початок CSV-імпорту: " + file.getFileName());
        updateMessage("Читання CSV-файла...");
        if (!Files.isReadable(file)) {
            throw new IllegalArgumentException("CSV-файл не знайдено або недоступний: " + file);
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        String header = "inventory_code;name;material;hall;"
                + "material_sensitivity;exhibition_significance";
        if (lines.isEmpty() || !header.equals(lines.get(0).strip())) {
            throw new IllegalArgumentException("Некоректний заголовок CSV");
        }
        int total = (int) lines.stream().skip(1).filter(s -> !s.isBlank()).count();
        int processed = 0, imported = 0;
        updateProgress(0, Math.max(1, total));
        for (int i = 1; i < lines.size(); i++) {
            if (isCancelled()) break;
            if (lines.get(i).isBlank()) continue;
            String[] p = lines.get(i).split(";", -1);
            if (p.length != 6) {
                LOG.warning("Пропущено рядок " + (i + 1));
            } else if (p[0].isBlank() || p[1].isBlank() || p[2].isBlank() || p[3].isBlank()) {
                LOG.warning("Пропущено рядок " + (i + 1) + ": порожнє текстове поле");
            } else {
                try {
                    Exhibit e = new Exhibit(
                            null, p[0].strip(), p[1].strip(),
                            p[2].strip(), p[3].strip(),
                            Double.parseDouble(p[4].strip()),
                            Double.parseDouble(p[5].strip()));
                    if (!Double.isFinite(e.getMaterialSensitivity())
                            || !Double.isFinite(e.getExhibitionSignificance())) {
                        LOG.warning("NaN або Infinity у рядку " + (i + 1));
                        throw new IllegalArgumentException(
                                "Некоректне числове значення у рядку " + (i + 1));
                    }
                    try {
                        api.addExhibit(e);
                    } catch (Exception ex) {
                        LOG.log(Level.SEVERE, "Помилка HTTP під час імпорту рядка " + (i + 1), ex);
                        throw ex;
                    }
                    imported++;
                } catch (NumberFormatException ex) {
                    LOG.warning("Некоректне число у рядку " + (i + 1));
                    throw new IllegalArgumentException("Некоректне число у рядку " + (i + 1), ex);
                }
            }
            updateProgress(++processed, Math.max(1, total));
            updateMessage("Опрацьовано " + processed + " із " + total
                    + "; імпортовано " + imported);
        }
        if (total == 0) {
            updateProgress(1, 1);
            updateMessage("Немає рядків для імпорту");
        }
        LOG.info("Імпортовано: " + imported);
        return imported;
    }
}