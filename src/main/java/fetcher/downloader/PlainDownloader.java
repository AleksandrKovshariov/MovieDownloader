package fetcher.downloader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class PlainDownloader{

    private PlainDownloader(){

    }

    public static byte[] download(URL url) throws IOException {
        return download(url, Collections.emptyMap());
    }

    public static byte[] download(URL url, Map<String, String> properties) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        properties.keySet().forEach(k -> urlConnection.setRequestProperty(k, properties.get(k)));
        return IOUtils.toByteArray(urlConnection.getInputStream());
    }

}
