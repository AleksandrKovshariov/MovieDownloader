package fetcher.provider;

import fetcher.downloader.PlainDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MoviesSuQualityResolver {
    private static final Logger log = LoggerFactory.getLogger(MoviesSuQualityResolver.class);
    private final URL initialUrl;
    private List<URL> qualityUrls;

    public MoviesSuQualityResolver(URL initialUrl) throws IOException{
        this.initialUrl = initialUrl;
        init();
    }

    private void init() throws IOException {
        String initial = initialUrl.toString();

        byte[] bytes = PlainDownloader.download(initialUrl, MoviesSu.ADDITIONAL_HEADERS);
        String lines = new String(bytes, StandardCharsets.UTF_8);
        this.qualityUrls = MoviesSuUtils.getUrlListFromDownload(lines, initialUrl);
    }

    public List<URL> getQualityUrls(){
        return qualityUrls;
    }

    public URL getMaxQualityUrl(){
        return qualityUrls.get(qualityUrls.size() - 1);
    }


}
