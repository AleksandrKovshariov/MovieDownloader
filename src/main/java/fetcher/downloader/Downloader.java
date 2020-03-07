package fetcher.downloader;

import java.io.IOException;

@FunctionalInterface
public interface Downloader {
    void download() throws IOException;
}
