package ua.edu.nupp.kpp.client;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Logger;

public class ImportExhibitsTask {
    private static final Logger LOG=Logger.getLogger(ImportExhibitsTask.class.getName());
    private final Path file;
    private final HttpMuseumExhibitsClient api;
    public ImportExhibitsTask(Path file,HttpMuseumExhibitsClient api) { this.file=file; this.api=api; }


    public Integer call() throws Exception {
        List<String> lines=Files.readAllLines(file);
        int imported=0;
        for(int i=1;i<lines.size() && imported<1;i++) {
            String[] p=lines.get(i).split(";");
            if(p.length!=6) { LOG.warning("Пропущено рядок "+(i+1)); continue; }
            Exhibit e=new Exhibit(null,p[0],p[1],p[2],p[3],Double.parseDouble(p[4]),Double.parseDouble(p[5]));
            api.addExhibit(e); imported++;
            LOG.info("Імпортовано тестовий рядок: "+p[0]);
        }
        return imported;
    }
}