package Automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AdminPlatform {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            driver.get("https://*****/login");   //our test admin platform -can't share
            driver.manage().window().maximize();

            System.out.println("The page Title is : " + driver.getTitle());
            System.out.println("The current URL is: " + driver.getCurrentUrl());

            //Verify Nitex logo
            WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pr-3")));
            if (logo.isDisplayed()) {
                System.out.println("✅ Nitex logo is displayed");
            } else {
                System.out.println("❌ Nitex logo is missing!");
            }

            //Login
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("standard-adornment-email")))
                    .sendKeys("***");  //can't share
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("standard-adornment-password")))
                    .sendKeys("***");  //can't share
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Log in')]"))).click();

            // Validation point for successful login
            try {
                // Wait for dashboard element or any post-login element
                WebElement dashboardElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(),'Collection')]")));
                assert dashboardElement != null;
                System.out.println(dashboardElement.getText());
                System.out.println("✅ Login successful — Dashboard is visible.");
            } catch (TimeoutException e) {
                System.out.println("❌ Login failed — Dashboard not visible.");

                // Optionally, take a screenshot or throw an AssertionError if using TestNG
                throw new AssertionError("Login failed — expected dashboard not found.");
            }
            //Click the Collection button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'Collection')]"))).click();

            //Upload collection cover image
            WebElement imageUpload = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("drag-upload")));
            imageUpload.sendKeys("***.jpg");  //can't share

            //Create unique collection name
            String collectionName = "Shadman_" + UUID.randomUUID().toString().substring(0, 4);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("collectionName"))).sendKeys(collectionName);
            System.out.println("Collection name: " + collectionName);

            //Dropdown: Brand
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='col-md-6 pr-1 mb-2']//div[contains(@class,'click-box')]"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[contains(text(),'GANT')]"))).click();

            //Dropdown: Season
            wait.until(ExpectedConditions.elementToBeClickable(By.id("season"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='SUMMER 26']"))).click();

            //Dropdown: Market
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'form-group')]//div[contains(@class,'click-box')]"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'single-item-focus')][normalize-space()='Bread']"))).click();

            //Radio buttons & checkboxes
            wait.until(ExpectedConditions.elementToBeClickable(By.name("isNitexCollection"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.id("willPublish"))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='isBuyerSelection']"))).click();

            //Submit
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Submit')]"))).click();
            System.out.println("Submitted to create collection: " + collectionName);

            //Validate collection creation success message
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Collection added successfully')]")));
            assert successMsg != null;
            System.out.println("✅ " + successMsg.getText());
            Thread.sleep(2000);
            //Navigate back and view the collection
            driver.navigate().back();
            Thread.sleep(2000);

            //Validate the created collection is visible
            WebElement createdCollection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'" + collectionName + "')]")));
            assert createdCollection != null;
            if (createdCollection.isDisplayed()) {
                System.out.println("✅ Collection '" + collectionName + "' appears in the list!");
            } else {
                System.out.println("❌ Collection not visible after creation!");
            }

            //Click the newly created collection
            wait.until(ExpectedConditions.elementToBeClickable(createdCollection)).click();
            System.out.println("🖱️ Opened the newly created collection: " + collectionName);

            //Add a design
            WebElement addDesignBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@data-toggle='modal' and @data-target='#add-multiple-design']")));

            //Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addDesignBtn);
            Thread.sleep(500); // brief pause to stabilize scroll

            addDesignBtn.click();
            System.out.println("✅ Clicked on 'Add Design' button successfully.");
            Thread.sleep(2000);

            //Upload a style image
            WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("style-upload")));
            uploadInput.sendKeys("****.png");  //can't share
            Thread.sleep(2000);

            //Validate image upload success
            try {
                // Option 1: Wait for preview image
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[contains(@src,'.png')]")));
                Thread.sleep(2000);
                System.out.println("✅ Image upload preview appeared successfully!");
                Thread.sleep(2000);
            } catch (TimeoutException e) {
                // Option 2: Wait for success message
                WebElement uploadMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Upload successful') or contains(text(),'Uploaded')]")));
                Thread.sleep(2000);

                assert uploadMsg != null;
                System.out.println("✅ Image upload success message: " + uploadMsg.getText());
            }
            Thread.sleep(2000);
            driver.findElement(By.xpath("//img[@class='design-image cursor-pointer']")).click();
            Thread.sleep(2000);
            //select Market dropdown
            WebElement selectMarket=wait.until(ExpectedConditions.elementToBeClickable(By.id("productGroupId")));

            assert selectMarket != null;
            Select s1=new Select(selectMarket);
            Thread.sleep(2000);
            s1.selectByValue("4");

            //Category dropdown
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class='regular-14 ']"))).click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//span[text()='JOGGER']")).click();
            Thread.sleep(2000);
            //click create button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='button w-100']"))).click();
            Thread.sleep(2000);
            //Wait for success message or confirmation element
            WebElement m1 = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='product-image']"))
            );
            //Validate that the expected message is displayed
            assert m1 != null;
            if (m1.isDisplayed()) {
                System.out.println("✅ Validation Passed: Design created successfully.");
            } else {
                System.out.println("❌ Validation Failed: Something went wrong");
            }
            //Wait for modal to disappear
            wait.until(ExpectedConditions.elementToBeClickable(By.id("add-multiple-design"))).click();

            //Get the design name
            WebElement designName= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div/div/div/section/div/div/div/div[1]/div/div/div[1]/div[2]/div/div[1]/a/h4")));
            assert designName != null;
            String x = designName.getText();
            System.out.println("The Design Name is: "+x);

            // Click the product image — opens a new tab
            WebElement productImage = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//img[@alt='product-image']")
            ));
            productImage.click();
            // Wait a moment for the new tab to open
            Thread.sleep(2000);
            // Get window handles
            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    System.out.println("🔄 Switched to new tab: " + driver.getTitle());
                    break;
                }
            }
            // Wait for navLink to be visible
            WebElement Materials = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[text()='Materials']")
            ));
            // Scroll into view to avoid being covered by a sticky header or animation
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", Materials);
            Thread.sleep(500); // small delay for smooth scroll

            // Click via JavaScript to bypass overlay issues
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Materials);
            System.out.println("✅ Clicked Materials successfully on the new tab.");
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//span[text()='FABRIC']")))).click();

            // Hover over the element that reveals the hidden element-1
            WebElement hoverTarget = driver.findElement(By.xpath("//img[@class='material-image']"));
            Actions actions = new Actions(driver);
            actions.moveToElement(hoverTarget).perform();

            // Now find and interact with the visible element
            WebElement hiddenElement = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("bookedQuantity0")));

            assert hiddenElement != null;
            hiddenElement.sendKeys("2");
            Thread.sleep(2000);
            driver.findElement(By.xpath("//button[text()='Book']")).click();
            Thread.sleep(2000);
            WebElement successMsg2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Material added')]")
            ));
            Thread.sleep(2000);
            assert successMsg2 != null;
            if (successMsg2.isDisplayed()) {
                System.out.println("✅ Material added successfully!");
            } else {
                System.out.println("❌ Material not added.");
            }
            // Example: wait until the new booked item row appears
            WebElement bookedRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//table[@class='table']")
            ));

            assert bookedRow != null;
            if (bookedRow.isDisplayed()) {
                System.out.println("✅ Fabric has been booked and is now listed.");
            } else {
                System.out.println("❌ Fabric not found in the list after booking.");
            }

            //Now adding Trims
            wait.until(ExpectedConditions.elementToBeClickable((By.xpath("//span[@class='tag false' or text()='TRIMS']")))).click();
            Thread.sleep(2000);

            // Hover over the element that reveals the hidden element-2
            WebElement h1 = driver.findElement(By.xpath("//div[@class='material-showed-items scroll-y-label false']//div[1]//div[1]//img[1]"));
            Actions a1 = new Actions(driver);
            a1.moveToElement(h1).perform();

            // Now find and interact with the visible element
            WebElement hiddenElement1 = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.name("bookedQuantity0")));

            assert hiddenElement1 != null;
            hiddenElement1.sendKeys("3");
            Thread.sleep(2000);
            driver.findElement(By.xpath("//button[text()='Book']")).click();
            Thread.sleep(2000);
            //Wait for success message
            WebElement successMsg3 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Material added')]")
            ));
            Thread.sleep(2000);
            assert successMsg3 != null;
            if (successMsg3.isDisplayed()) {
                System.out.println("✅ Material added successfully!");
            } else {
                System.out.println("❌ Material not added.");
            }
            // Example: wait until the new booked item row appears
            WebElement bookedRow1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//img[@class='cell-img pr-2']")
            ));

            assert bookedRow1 != null;
            if (bookedRow1.isDisplayed()) {
                System.out.println("✅ Trims has been booked and is now listed.");
            } else {
                System.out.println("❌ Trims not found in the list after booking.");
            }

            // Wait and click the "Measurement" tab
            WebElement measurementTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Measurement']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", measurementTab);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", measurementTab);

            // Wait until Measurement tab content appears
            WebElement measurementContent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='bg-gray']")
            ));

            assert measurementContent != null;
            if (measurementContent.isDisplayed()) {
                System.out.println("✅ Successfully switched to 'Measurement' tab (content visible).");
            } else {
                System.out.println("❌ Failed to switch to 'Measurement' tab (content not visible).");
            }


            WebElement editMeasurement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Edit Measurement']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editMeasurement);
            Thread.sleep(2000);
            System.out.println("✅ Clicked 'Edit measurement' button.");

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Add size & points']"))).click();
            // Wait for the sidebar panel to appear
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@class='pom-measurement-sidebar fixed-height']")
                ));
                Thread.sleep(2000);
                System.out.println("✅ Sidebar panel is visible.");
            } catch (TimeoutException e) {
                System.out.println("❌ Sidebar panel did not appear after clicking 'Add to this Techapck'.");
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='L']"))).click();

            //Hover over the 1st image card to reveal hidden buttons
            WebElement addSize1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='measurement-sidebar']/div/div[2]/div/div/div/div/div[2]/div[2]/div[1]/img")));
            assert addSize1 != null;
            new Actions(driver).moveToElement(addSize1).perform();
            Thread.sleep(1000);

            //Wait for "Add to this checkBox" button to appear, then click it
            WebElement checkBox1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='1153624']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox1);
            System.out.println("✅ Clicked 'checkBox-1'");

            //Hover over the 2nd image card to reveal hidden buttons
            WebElement addSize2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='measurement-sidebar']/div/div[2]/div/div/div/div/div[1]/div[2]/div[1]/img")));
            assert addSize2 != null;
            new Actions(driver).moveToElement(addSize2).perform();
            Thread.sleep(1000);

            //Wait for "Add to this checkBox" button to appear, then click it
            WebElement checkBox2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='1153623']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox2);
            System.out.println("✅ Clicked 'checkBox-2'");

            //Hover over the 2nd image card to reveal hidden buttons
            WebElement addSize3 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='measurement-sidebar']/div/div[2]/div/div/div/div/div[3]/div[2]/div[1]/img")));
            assert addSize3 != null;
            new Actions(driver).moveToElement(addSize3).perform();
            Thread.sleep(1000);

            //Wait for "Add to this checkBox" button to appear, then click it
            WebElement checkBox3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='1153625']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox3);
            System.out.println("✅ Clicked 'checkBox-3'");

            //Hover over the 2nd image card to reveal hidden buttons
            WebElement addSize4 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='measurement-sidebar']/div/div[2]/div/div/div/div/div[4]/div[2]/div[1]/img")));
            assert addSize4 != null;
            new Actions(driver).moveToElement(addSize4).perform();
            Thread.sleep(1000);

            //Wait for "Add to this checkBox" button to appear, then click it
            WebElement checkBox4 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='1153626']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox4);
            System.out.println("✅ Clicked 'checkBox-4'");

            //Hover over the 2nd image card to reveal hidden buttons
            WebElement addSize5 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='measurement-sidebar']/div/div[2]/div/div/div/div/div[5]/div[2]/div[1]/img")));
            assert addSize5 != null;
            new Actions(driver).moveToElement(addSize5).perform();
            Thread.sleep(1000);

            //Wait for "Add to this checkBox" button to appear, then click it
            WebElement checkBox5 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='1153627']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkBox5);
            System.out.println("✅ Clicked 'checkBox-5'");

            driver.findElement(By.xpath("//img[@alt='close']")).click();

//// Input Target value
//            WebElement input1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@name,'sizeValue')]")));
//            Thread.sleep(1500);
//            wait.until(ExpectedConditions.visibilityOf(input1));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input1);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value='22';", input1);
//            System.out.println("✅ Target value entered via JS");
//
//// Input Tolerance
//            WebElement input2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@name,'tolerance')]")));
//            Thread.sleep(1500);
//            wait.until(ExpectedConditions.visibilityOf(input2));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input2);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value='2';", input2);
//            System.out.println("✅ Tolerance value entered via JS");
//
//// Input Actual value
//            WebElement input3 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@name,'actualValue')]")));
//            Thread.sleep(1500);
//            wait.until(ExpectedConditions.visibilityOf(input3));
//            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", input3);
//            ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value='19';", input3);
//            System.out.println("✅ Actual value entered via JS");

            driver.findElement(By.xpath("//button[text()='Save Measurement']")).click();
            Thread.sleep(2000);

            // Wait for the modal to appear
            WebDriverWait w1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement popup = w1.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='common-popup-body']")
            ));
            System.out.println("✅ Popup is visible.");

            // Click the Yes button
            assert popup != null;
            WebElement yesButton = popup.findElement(By.xpath("//button[text()='Yes']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", yesButton);
            wait.until(ExpectedConditions.elementToBeClickable(yesButton)).click();
            System.out.println("🟢 Clicked 'Yes' button successfully.");
            Thread.sleep(1500);

            // click on the Presentation tab
            WebElement Presentation = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Presentation']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", Presentation);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Presentation);
            Thread.sleep(1000);
            System.out.println("✅ Clicked 'Presentation' tab successfully.");
            Thread.sleep(1500);

            // Ensure Presentation tab content loaded
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[contains(@class,'inline-upload')]")
            ));
            Thread.sleep(1000); // let animations finish

            By uploadLocator = By.xpath("//input[@type='file' and contains(@id,'bulk_presentation')]");
            WebElement uploadPresentation = wait.until(ExpectedConditions.presenceOfElementLocated(uploadLocator));

            // Make visible if hidden
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.display='block'; arguments[0].removeAttribute('hidden'); arguments[0].style.opacity='1';", uploadPresentation
            );
            Thread.sleep(2000);

            // Provide full paths separated by "\n"
            String image1 = "******.png"; //can't share
            String image2 = "******.png"; //can't share
            String image3 = "******.jpg"; //can't share
            String image4 = "******.jpg"; //can't share

            uploadPresentation.sendKeys(image1 + "\n" + image2 + "\n" + image3 + "\n" + image4);
            Thread.sleep(15000);
            System.out.println("✅ Image Uploaded succesfully");

            // Locator for the back button
            WebElement backBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='back']")));

            // Wait for the overlay to disappear first
            By overlayLocator = By.cssSelector("#root > div > div._loading_overlay_overlay.css-thkz1r._loading-overlay-transition-enter-done");
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.invisibilityOfElementLocated(overlayLocator));

            // Scroll to the button and click safely using JS
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", backBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backBtn);

            System.out.println("✅ Clicked back button successfully after overlay disappeared.");
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='product-image']"))).click();
            Thread.sleep(2000);
            //click on the edit info button
            WebElement editBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[normalize-space()='Edit basic info']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", editBtn);
            Thread.sleep(500); // allow scroll animation or header shift
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);
            System.out.println("✅ Clicked 'Edit basic info' button using JS safely.");
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
            Thread.sleep(2000);
            // Step 1: Expand section
            WebElement expandButton = driver.findElement(By.xpath("//button[@aria-controls='collapseOne']"));

            // Only click the expand button if the section is not already shown
            String expanded = expandButton.getAttribute("aria-expanded");
            if (expanded == null || expanded.equals("false")) {
                expandButton.click();
                Thread.sleep(1000);
            }
            // Step 2: Wait for sampleId field to appear
            WebElement SampleId = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@placeholder='Sample Id']")));
            assert SampleId != null;
            SampleId.click();
            Thread.sleep(500);
            int randomNumber = ThreadLocalRandom.current().nextInt(100000, 1000000); // 100000 inclusive, 1000000 exclusive
            SampleId.sendKeys(String.valueOf(randomNumber));
            System.out.println("✅ SampleId entered: " + randomNumber);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tbody/tr[1]/td[2]/div[1]/div[1]/p[1]"))).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='BD']"))).click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='collapseOne']/div/div/table/tbody/tr[1]/td[4]/div/div"))).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Nitex BD Dev.']"))).click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='collapseOne']/div/div/table/tbody/tr[1]/td[8]/div"))).click();
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[@class='location-colum']//li[1]//span[1]"))).click();
            Thread.sleep(1000);

            WebElement finalDropdown = driver.findElement(By.xpath("//*[@id='collapseOne']/div/div/table/tbody/tr[1]/td[11]/div/div/p"));
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", finalDropdown);
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(finalDropdown)).click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Available']"))).click();
            Thread.sleep(1000);

            WebElement updateBtn = driver.findElement(By.xpath("//button[text()='Update']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", updateBtn);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updateBtn);
            Thread.sleep(2000);

            //Wait for toast message
            WebElement toast1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Update Successful!')]")
            ));
            Thread.sleep(1000);
            assert toast1 != null;
            if (toast1.isDisplayed()) {
                System.out.println("✅ Style updated successfully!");
            } else {
                System.out.println("❌ Error occured.");
            }
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='more']"))).click();
            Thread.sleep(2000);
            System.out.println("All task completed succesfully");

            //now logout from this account
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='close']//*[name()='svg']"))).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt='profile_image']"))).click();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Logout']"))).click();
            Thread.sleep(2000);
            // Validate successful logout
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("standard-adornment-email")));
                System.out.println("✅ Successfully logged out — login page is visible again.");
            } catch (TimeoutException e) {
                System.out.println("❌ Logout validation failed — login page not detected within timeout.");
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error during logout validation: " + e.getMessage());
            }
            //Login with 'Costing Manager' account
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("standard-adornment-email")))
                    .sendKeys("****");  //can't share
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("standard-adornment-password")))
                    .sendKeys("***"); //can't share
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Log in')]"))).click();

            // Validation point for successful login
            try {
                // Wait for dashboard element or any post-login element
                WebElement dashboardElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Costing']")));
                assert dashboardElement != null;
                System.out.println(dashboardElement.getText());
                System.out.println("✅ Costing Manager Login successful — Dashboard is visible.");
            } catch (TimeoutException e) {
                System.out.println("❌ Login failed — Dashboard not visible.");
                // Optionally, take a screenshot or throw an AssertionError if using TestNG
                throw new AssertionError("Login failed — expected dashboard not found.");
            }
            // Wait until any loading overlay disappears
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'_loading_overlay_overlay')]"))
            );

            // Now click the Collection button
            WebElement collectionTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='root']/div/div/nav/div[3]/ul/li[2]/a")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", collectionTab);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", collectionTab);
            System.out.println("✅ Clicked on Collection button after overlay disappeared");

            // Wait for overlay again (page transition)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'_loading_overlay_overlay')]"))
            );

            try {
                // Wait until overlay disappears (page fully loaded)
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'_loading_overlay_overlay')]"))
                );

                // Flexible locator for Design View icon
                By designViewLocator = By.xpath("//div[@class='view-tab cursor-pointer ']//img");

                // Wait for presence, visibility, then clickability
                wait.until(ExpectedConditions.presenceOfElementLocated(designViewLocator));
                WebElement designView = wait.until(ExpectedConditions.visibilityOfElementLocated(designViewLocator));
                assert designView != null;
                wait.until(ExpectedConditions.elementToBeClickable(designView));

                // Scroll into view & click via JavaScript
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", designView);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", designView);
                System.out.println("✅ Clicked on Design View.");

            } catch (TimeoutException e) {
                System.out.println("❌ Timed out waiting for Design View to appear or become clickable.");
            } catch (Exception e) {
                System.out.println("❌ Error while clicking Design View: " + e.getMessage());
            }


            //confirm design view page
            try {
                // Wait until the 'Archived' tab is visible
                WebElement archivedTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//li[text()='Archived']")
                ));
//                System.out.println(archivedTab.getText());
                assert archivedTab != null;
                if (archivedTab.isDisplayed()) {
                    System.out.println("📁 'Archived' tab is visible on the page.");
                } else {
                    System.out.println("⚠️ 'Archived' tab element found but not displayed.");
                }

            } catch (TimeoutException e) {
                System.out.println("❌ Timed out waiting for 'Archived' tab to appear within the expected time.");
            } catch (NoSuchElementException e) {
                System.out.println("❌ 'Archived' tab element not found in the DOM.");
            } catch (Exception e) {
                System.out.println("❌ Unexpected error while waiting for 'Archived' tab: " + e.getMessage());
            }

            Thread.sleep(2000);
            //Validate the created Design is visible
            WebElement newDesign = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//a[contains(text(),'" + x + "')]")));
            assert newDesign != null;
            if (newDesign.isDisplayed()) {
                System.out.println("✅ Design '" + x + "' appears in the list!");
            } else {
                System.out.println("❌ Design is not visible!");
            }
            Thread.sleep(2000);
            //Click the newly created design
            wait.until(ExpectedConditions.elementToBeClickable(newDesign)).click();
            System.out.println("🖱️ Opened the existing Design: " + x);
            Thread.sleep(2000);

            //click on the edit info button
            WebElement editBtn1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[normalize-space()='Edit basic info']")
            ));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", editBtn1);
            Thread.sleep(500); // allow scroll animation or header shift
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn1);
            System.out.println("✅ Clicked 'Edit basic info' button.");
            Thread.sleep(1000);

            //Click on the Costing button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class='d-flex align-items-center mt-half']"))).click();
            Thread.sleep(1000);

            //Input amount
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Fabric, Trims, Print...']"))).sendKeys("1,2,1,2,1,2,1,2,1");
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='costing-items-details']"))).click();
            Thread.sleep(1000);
            // get the toast message
            WebElement pop1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[@class='toast-text']")
            ));
            System.out.println("After input: "+pop1.getText());
            Thread.sleep(2000);

            //Again Click on the Costing button
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p[@class='d-flex align-items-center mt-half']"))).click();
            Thread.sleep(1000);

            //click on Update button
            WebElement updateBtn1 = driver.findElement(By.xpath("//button[text()='Update']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'center'});", updateBtn1);
            Thread.sleep(500);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", updateBtn1);
            Thread.sleep(2000);

            //Wait for toast message
            WebElement toast2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Update Successful!')]")
            ));
            Thread.sleep(1000);
            assert toast2 != null;
            if (toast2.isDisplayed()) {
                System.out.println("✅ Costing updated successfully!");
            } else {
                System.out.println("❌ Error occured.");
            }
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@class='more']"))).click();
            Thread.sleep(2000);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Publish']"))).click();
            Thread.sleep(2000);

            // get the toast message
            WebElement pop2 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[@class='toast-text']")
            ));
            assert pop2 != null;
            System.out.println("After publish: "+pop2.getText());
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("❌ Exception occurred: " + e.getMessage());
        } finally {
            driver.quit();
            System.out.println("🚪 Browser closed.");
        }
    }
}
