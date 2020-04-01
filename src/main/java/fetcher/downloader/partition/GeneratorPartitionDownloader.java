package fetcher.downloader.partition;

import fetcher.downloader.exception.ResponseStatusDontMatchException;
import fetcher.downloader.partition.generator.GeneratorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GeneratorPartitionDownloader extends PartitionDownloader {
    private static final Logger log = LoggerFactory.getLogger(GeneratorPartitionDownloader.class);
    private GeneratorStrategy generator;

    public GeneratorPartitionDownloader(GeneratorStrategy generator, String fileName) {
        super(fileName);
        this.generator = generator;
    }

    @Override
    public void download() throws IOException {
        try(BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(fileName, true))) {
            while (true) {
                URL url = this.generator.next();
                try (InputStream inputStream = getStreamPart(url)) {
                    inputStream.transferTo(fout);
                } catch (ResponseStatusDontMatchException e) {
                    log.debug("Finish getting packets form generator");
                    break;
                }
            }
        }
    }
}
