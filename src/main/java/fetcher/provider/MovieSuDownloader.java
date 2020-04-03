package fetcher.provider;

import fetcher.downloader.PlainDownloader;
import fetcher.downloader.partition.UrlListPartitionDownloader;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MovieSuDownloader extends UrlListPartitionDownloader {
    private final URL downloadUrl;

    public MovieSuDownloader(String fileName, URL downloadUrl) {
        super(fileName);
        this.downloadUrl = downloadUrl;
        additionalRequestProperties = MoviesSu.ADDITIONAL_HEADERS;
    }

    @Override
    public void download() throws IOException {
        fillUrlList();
        super.download();
    }

    private void fillUrlList() throws IOException{
        byte[] bytes = PlainDownloader.download(downloadUrl, MoviesSu.ADDITIONAL_HEADERS);
        String lines = new String(bytes, StandardCharsets.UTF_8);
        urlList = MoviesSuUtils.getUrlListFromDownload(lines, downloadUrl);
    }
}
