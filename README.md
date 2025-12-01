# 🧪 Nitex Admin Panel Automation (Selenium + Java)

This repository contains an end-to-end Selenium automation script for the **Nitex Admin Platform**, designed to automate the full workflow across multiple user roles — from collection creation to material booking, measurement editing, presentation uploads, and costing validation.

---

## 🚀 Overview

This automation script performs a complete functional validation of the **Nitex Admin Panel** (test environment: `https://**********/login`) by simulating real user actions.

It includes:

- ✅ Login and role validation (Designer & Costing Manager)
- ✅ Collection creation with unique name
- ✅ Image uploads (collection cover, design, presentation)
- ✅ Dropdown, checkbox, and radio button selections
- ✅ Material booking (Fabric & Trims)
- ✅ Measurement editing and validation
- ✅ Presentation uploads
- ✅ Style info updates and toast message verification
- ✅ Logout and re-login with role switch
- ✅ Costing completion through separate role by inputting value
- ✅ Validation points and assertions for every major step
---

## 🧰 Tech Stack

| Component | Technology |
|------------|------------|
| **Language** | Java 17+ |
| **Automation Framework** | Selenium WebDriver |
| **Browser Driver** | ChromeDriver |
| **Wait Handling** | Explicit & Implicit Waits (`WebDriverWait`, `ExpectedConditions`) |
| **Testing** | Standalone script (TestNG/JUnit integration optional) |
| **Tools/IDE** | IntelliJ IDEA |
| **Build Tool** | Maven |

---

## 📁 Project Structure

nitex-app-automation
│
├── src/
│ └── test/
│ └── java/
│ └── Automation/
│ └── AdminPlatform.java
│
├── pom.xml
├── README.md
---

## ⚙️ Prerequisites

Before running the script, ensure the following:

### 1. Install Required Software
- **Java JDK 17+**
- **Chrome Browser** (latest stable version)
- **ChromeDriver** — matching your Chrome version  
  [Download ChromeDriver](https://chromedriver.chromium.org/downloads)
- **IDE** (e.g., IntelliJ IDEA or Eclipse)
- **Maven** *(optional, if you plan to use dependencies via pom.xml)*

### 2. Add Dependencies (if using Maven)

```xml
<dependencies>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.38.0</version>
    </dependency>
</dependencies>

