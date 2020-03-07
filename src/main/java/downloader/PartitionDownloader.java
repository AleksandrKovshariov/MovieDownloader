package downloader;

import downloader.exception.ResponseStatusDontMatchException;
import downloader.generator.GeneratorStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class PartitionDownloader implements Downloader {
    private static final Logger log = LoggerFactory.getLogger(PartitionDownloader.class);
    private List<URL> urlList;
    private Map<String, String> additionalRequestProperties;
    private int expectedStatusCode = 200;
    private GeneratorStrategy generator;
    private String fileName = "defaultMovieName.ts";

    private PartitionDownloader(){

    }

    public static PartitionDownloader fromUrlList(List<URL> uriList){
        PartitionDownloader pd = new PartitionDownloader();
        pd.urlList = uriList;
        return pd;
    }

    public static PartitionDownloader fromGenerator(GeneratorStrategy generatorStrategy){
        PartitionDownloader pd = new PartitionDownloader();
        pd.generator = generatorStrategy;
        return pd;
    }

    @Override
    public void download() throws IOException {

        try(BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(fileName, true))){
            if(urlList != null){
                downloadFromUrlList(fout);
            }else if(generator != null){
                downloadWithGenerator(fout);
            }else {
                log.error("Critical error urlList or generatorStategy must be provided");
            }
        }
    }

    private void downloadWithGenerator(OutputStream outputStream) throws IOException{
        while (true) {
            URL url = this.generator.next();
            try (InputStream inputStream = getStreamPart(url)) {
                inputStream.transferTo(outputStream);
            } catch (ResponseStatusDontMatchException e) {
                log.debug("Finish getting packets form generator");
                break;
            }
        }

    }

    private void downloadFromUrlList(OutputStream outputStream) throws IOException {
        for(URL url : urlList){
            try(InputStream inputStream = getStreamPart(url)) {
                inputStream.transferTo(outputStream);
            }
        }
    }

    //TODO add retrieving from stategy or list
    private InputStream getStreamPart(URL url) throws IOException {
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
}
