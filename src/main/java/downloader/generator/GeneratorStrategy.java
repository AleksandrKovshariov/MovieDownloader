package downloader.generator;

import java.io.IOException;
import java.net.URL;

@FunctionalInterface
public interface GeneratorStrategy {
    URL next() throws IOException;
}
