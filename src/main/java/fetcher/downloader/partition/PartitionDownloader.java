package fetcher.downloader.partition;

import fetcher.downloader.exception.ResponseStatusDontMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public abstract class PartitionDownloader implements Downloader {
    private static final Logger log = LoggerFactory.getLogger(PartitionDownloader.class);
    protected Map<String, String> additionalRequestProperties;
    protected int expectedStatusCode = 200;
    protected String fileName;

    public PartitionDownloader(String fileName){
        this.fileName = fileName;
    }

    protected InputStream getStreamPart(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        if(this.additionalRequestProperties != null){
            setAdditionalRequestProperties(urlConnection);
        }
        validateResponse(urlConnection);
        log.debug("Received package " + urlConnection.getContentLengthLong() + " bytes");

        return urlConnection.getInputStream();
    }

    private void validateResponse(HttpURLConnection urlConnection) throws IOException{
        int respCode = urlConnection.getResponseCode();
        if(respCode != expectedStatusCode){
            throw new ResponseStatusDontMatchException("Expected status code to be " + expectedStatusCode +
                    " but was " + respCode, respCode);
        }
    }

    private void setAdditionalRequestProperties(URLConnection urlConnection){
        this.additionalRequestProperties.keySet()
                .forEach(x -> urlConnection.setRequestProperty(x, additionalRequestProperties.get(x)));
    }

    public int getExpectedStatusCode() {
        return expectedStatusCode;
    }

    public void setExpectedStatusCode(int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
    }

    public Map<String, String> getAdditionalRequestProperties() {
        return additionalRequestProperties;
    }

    public void setAdditionalRequestProperties(Map<String, String> additionalRequestProperties) {
        this.additionalRequestProperties = additionalRequestProperties;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
