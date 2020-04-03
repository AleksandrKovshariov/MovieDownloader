package fetcher.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MoviesSuUtils {
    private static final Logger log = LoggerFactory.getLogger(MoviesSuUtils.class);
    private MoviesSuUtils(){

    }

    public static URL constructUrl(URL url, String x) throws MalformedURLException {
        String genericUrl = url.toString().substring(0, url.toString().lastIndexOf("/"));
        return new URL(genericUrl + "/" + x);
    }

    public static List<URL> getUrlListFromDownload(String lines, URL downloadUrld){
        return lines.lines().filter(x -> !x.startsWith("#"))
                .map(x -> {
                    try {
                        return MoviesSuUtils.constructUrl(downloadUrld, x);
                    } catch (MalformedURLException e) {
                        log.error("Can't construct url", e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
