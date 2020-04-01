package fetcher.downloader.partition;

import java.io.IOException;

@FunctionalInterface
public interface Downloader {
    void download() throws IOException;
}
