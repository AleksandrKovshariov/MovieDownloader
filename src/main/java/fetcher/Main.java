package fetcher;

import fetcher.provider.MoviesSu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main  {
    private static String CHROME_DRIVER = Main.class.getClassLoader().getResource("chromedriver").getPath();
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        MoviesSu moviesSu = new MoviesSu();
        try {
            moviesSu.downloadAndSave("https://ww.0123movies.su/movie/the-hunger-games-2012-online-123movies");
        }catch (IOException e){
            log.error("Error while downloading" + e);
        }

    }

}
