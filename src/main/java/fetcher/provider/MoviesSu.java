package fetcher.provider;

import fetcher.provider.exception.QualityUrlNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

public class MoviesSu {
    private static final Logger log = LoggerFactory.getLogger(MoviesSu.class);
    public static final Map<String, String> ADDITIONAL_HEADERS = Collections.singletonMap("Referer", "https://vidcloud9.com/");
    public static final String NAME_CSS_SELECTOR = "div .mvic-desc h3";
    public static final String DEFAULT_MOVIE_NAME = "DefaultMovieName";
    public static final String NETWORK_STATUS_SCRIPT = "var network = performance.getEntries() || {}; return network;";
    public static final int IFRAME_WAIT = 15;
    private WebDriver webDriver;

    public MoviesSu(){
        init();
    }

    private void init() {
        this.webDriver = new ChromeDriver();
    }

    private String getEpisodeRef(String downloadUrl){
        webDriver.get(downloadUrl);
        return webDriver.findElement(By.cssSelector(".thumb.mvi-cover")).getAttribute("href");
    }

    private String getMovieName(){
        try {
            WebElement name = webDriver.findElement(By.cssSelector(NAME_CSS_SELECTOR));
            return name.getText();
        }catch (NoSuchElementException e){
            log.error("Movie name not found using defalut name", e);
            return DEFAULT_MOVIE_NAME;
        }
    }
    public void downloadAndSave(String downloadUrl) throws IOException {

        String episodeRef = downloadUrl.contains("watching") ? downloadUrl : getEpisodeRef(downloadUrl);

        webDriver.get(episodeRef);

        String movieName = getMovieName();
        webDriver.get(getIframeLink());
        WebElement webElement = webDriver.findElement(By.id("myVideo"));
        webElement.click();
        //TODO refactor
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String netData = ((JavascriptExecutor)webDriver).executeScript(NETWORK_STATUS_SCRIPT).toString();
        System.out.println(netData);

        String qualityUrl = getQualityUrl(netData);
        URL max = new MoviesSuQualityResolver(new URL(qualityUrl)).getMaxQualityUrl();
        MovieSuDownloader downloader = new MovieSuDownloader(movieName + ".ts", max);
//        downloader.download();

    }

    private String getIframeLink(){
        WebDriverWait wait = new WebDriverWait(webDriver, IFRAME_WAIT);

        wait.until((ExpectedCondition<Boolean>) driver -> {
            WebElement iframe = driver.findElement(By.id("iframe-embed"));
            return !iframe.getAttribute("src").isBlank();
        });

        WebElement source = webDriver.findElement(By.id("iframe-embed"));
        return source.getAttribute("src");
    }

    private String getQualityUrl(String netData){
        int index = netData.indexOf("https://m2x.vidcloud9.com/");
        if(index == -1){
            throw new QualityUrlNotFoundException("Quality url not found");
        }
        return netData.substring(index, netData.indexOf(",", index));
    }


}
