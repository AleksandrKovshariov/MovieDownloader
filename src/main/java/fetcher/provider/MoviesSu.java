package fetcher.provider;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class MoviesSu {
    public static final Map<String, String> ADDITIONAL_HEADERS = Collections.singletonMap("Referer", "https://vidcloud9.com/");
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
    public void downloadAndSave(String downloadUrl) throws IOException {

        String episodeRef = downloadUrl.contains("watching") ? downloadUrl : getEpisodeRef(downloadUrl);

        webDriver.get(episodeRef);

        WebDriverWait wait = new WebDriverWait(webDriver, IFRAME_WAIT);

        wait.until((ExpectedCondition<Boolean>) driver -> {
            WebElement iframe = driver.findElement(By.id("iframe-embed"));
            return !iframe.getAttribute("src").isBlank();
        });

        WebElement source = webDriver.findElement(By.id("iframe-embed"));
        String url = source.getAttribute("src");

        webDriver.get(url);
        WebElement webElement = webDriver.findElement(By.id("myVideo"));
        webElement.click();
        //TODO refactor
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String netstat = "var network = performance.getEntries() || {}; return network;";
        String netData = ((JavascriptExecutor)webDriver).executeScript(netstat).toString();
        System.out.println(netData);

        //-1 check
        int index = netData.indexOf("https://m2x.vidcloud9.com/");
        String first = netData.substring(index, netData.indexOf(",", index));
        URL max = new MoviesSuQualityResolver(new URL(first)).getMaxQualityUrl();
        MovieSuDownloader downloader = new MovieSuDownloader("file.ts", max);
        downloader.download();

    }


}
