package fetcher.downloader.partition;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class UrlListPartitionDownloader extends PartitionDownloader {
    private List<URL> urlList;

    public UrlListPartitionDownloader(List<URL> urlList, String fileName) {
        super(fileName);
        if(urlList == null || urlList.isEmpty()){
            throw new IllegalArgumentException("List must not be empty");
        }
        this.urlList = urlList;
    }

    @Override
    public void download() throws IOException {
        try(BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(fileName, true))) {
            for (URL url : urlList) {
                try (InputStream inputStream = getStreamPart(url)) {
                    inputStream.transferTo(fout);
                }
            }
        }
    }
}
