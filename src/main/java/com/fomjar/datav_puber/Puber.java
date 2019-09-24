package com.fomjar.datav_puber;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Puber {

    private static final Logger logger = LoggerFactory.getLogger(Puber.class);

    private WebDriver driver;

    public Puber(String driver) {
        System.setProperty("webdriver.chrome.driver", driver);
        this.driver = new ChromeDriver();
    }

    public void url(String url) {
        this.driver.get(url);
        this.await(1000L);
        this.loginCheck();
        this.driver.get(url);
        this.driver.manage().window().maximize();
    }

    public void loginCheck() {
        List<WebElement> inputs = this.driver.findElements(By.tagName("input"));
        if (!inputs.isEmpty()) {
            logger.info("logging in ...");
            inputs.get(0).sendKeys("admin");
            inputs.get(1).sendKeys("hello_DataV");
            this.driver.findElement(By.className("btn")).click();
            this.await("datav-nav");
            this.await(1000L);
        }
    }

    public void quit() {
        this.driver.quit();
    }

    private void treeExpand() {
        while (true) {
            if (this.driver.findElements(By.className("icon-fold")).isEmpty()) {
                break;
            }
            this.driver.findElements(By.className("layer-manager-group")).forEach(group -> {
                // find fold class
                if (!group.findElements(By.className("icon-fold")).isEmpty()) {
                    // triggle expand
                    group.findElement(By.className("group-fold-controller")).click();

                    this.await(100L);
                }
            });
        }
    }

    private List<WebElement> treeNodes() {
        return this.driver.findElements(By.className("layer-manager-com"));
    }

    public void papi() {
        logger.info("tree expanding ...");
        this.treeExpand();
        this.treeNodes().forEach(node -> {
            String name = node.findElement(By.className("layer-item-span")).getText();
            try {
                // click node
                node.click();
                this.await(100L);
                // click data tab
                this.driver.findElement(By.className("config-manager")).findElements(By.className("datav-tabs__tab")).get(1).click();
                this.await(100L);

                // find select
                WebElement select = this.driver.findElement(By.className("datasource-select")).findElement(By.className("datav-new-select"));
                // click select
                select.click();
                this.await(100L);
                // click API (3rd)
                select.findElements(By.className("Select-option")).get(2).click();
                this.await(2000L);
                // check editor lines
                List<WebElement> lines = this.driver.findElement(By.className("api-editor")).findElements(By.className("view-line"));
                boolean hasAPI = false;
                for (WebElement line : lines) {
                    try {
                        if (line.findElement(By.className("mtk1")).getText().contains("http")) {
                            hasAPI = true;
                            break;
                        }
                    } catch (NoSuchElementException e) {
                        // empty textarea
                    }
                }
                if (!hasAPI) {
                    // click select
                    select.click();
                    this.await(100L);
                    // turn back to STATIC (1st)
                    select.findElements(By.className("Select-option")).get(0).click();
                    this.await(2000L);
                } else {
                    logger.info("published node: " + name);
                }
            } catch (Exception e) {
                logger.error("published node failed: " + name, e);
            }
        });
    }

    private void await(long time) {
        try { Thread.sleep(time); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    private void await(String className) {
        while (true) {
            if (this.driver.findElements(By.className(className)).isEmpty()) {
                this.await(500L);
            } else {
                break;
            }
        }
    }

}
