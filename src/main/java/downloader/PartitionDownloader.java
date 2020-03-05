package downloader;

import downloader.exception.ResponseStatusDontMatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class PartitionDownloader {
    private static final Logger log = LoggerFactory.getLogger(PartitionDownloader.class);
    private List<URL> urlList;
    private Map<String, String> additionalRequestProperties;
    private int expectedStatusCode = 200;
    private String fileName = "defaultMovieName.ts";

    private PartitionDownloader(){

    }

    public static PartitionDownloader fromList(List<URL> uriList){
        PartitionDownloader pd = new PartitionDownloader();
        pd.urlList = uriList;
        return pd;
    }

    public void download() throws IOException {

        try(BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(fileName, true))){
            for(URL url : urlList){
                try(InputStream inputStream = getStreamPart(url)){
                    inputStream.transferTo(fout);
                }
            }
        }
    }

    private InputStream getStreamPart(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        if(this.additionalRequestProperties != null){
            setAdditionalRequestProperties(urlConnection);
        }

        int respCode = urlConnection.getResponseCode();
        if(respCode != expectedStatusCode){
            throw new ResponseStatusDontMatchException("Expected status code to be " + expectedStatusCode +
                    " but was " + respCode, respCode);
        }
        log.debug("Received package " + urlConnection.getContentLengthLong() + " bytes");

        return urlConnection.getInputStream();
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
}
