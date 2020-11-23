package Restapi.ServiceImpl;

import Restapi.Model.Category;
import Restapi.Model.Subcategory;
import Restapi.Model.Type;
import Restapi.Model.Variation;
import Restapi.Repository.CategoryRepository;
import Restapi.Repository.SubcategoryRepository;
import Restapi.Repository.TypeRepository;
import Restapi.Repository.VariationRepository;
import Restapi.Service.CollectionService;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private VariationRepository variationRepository;

    @Override
    public List<Variation> findAllByNameOrCode(String name, String code) {
        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(code)) {
            return variationRepository.findAllByNameContainingOrCodeContaining(name, code);
        } else if (StringUtils.isNotBlank(name)) {
            return variationRepository.findAllByNameContaining(name);
        } else {
            return variationRepository.findAllByCodeContaining(code);
        }
    }

    @Override
    public void startCollection() throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/home/benn/Downloads/geckodriver-v0.28.0-linux64/geckodriver");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("marionette", true);
//        firefoxOptions.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);

        String baseUrl = "https://icd.who.int/browse11/l-m/en";
        driver.manage()
                .timeouts()
                .implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='ygtvcontentel29']/a/span")));

        List<WebElement> webElementCategoryList = driver.findElementsByXPath("//div[@id='ygtvc1']//div[@class='ygtvitem']");
        long categoryCount = 0;
        long subCategoryCount = 0;
        long typeCount = 0;
        long variationCount = 0;

        String childLinkExpression = ".//a[@class='ygtvspacer']";
        String childTitleExpression = ".//a[starts-with(@class, 'ygtvlabel ')]";
        String childListExpression = ".//div[@class='ygtvchildren']//div[@class='ygtvitem']";
        String childCell = childLinkExpression + "/ancestor::td[contains(@class, 'ygtvcell')]";
        String linkAttrib = "data-id";

        for (WebElement categoryElement : webElementCategoryList) {
            WebElement linkElement = categoryElement.findElement(By.xpath(childTitleExpression));
            String categoryTitle = linkElement.getText();
            String categoryLink = linkElement.getAttribute(linkAttrib);

            WebElement webElementChildCell = categoryElement.findElement(By.xpath(childCell));

            if (!hasExpandIcon(childLinkExpression, webElementChildCell)) continue;

            List<WebElement> webElementSubcategoryList = categoryElement.findElements(By.xpath(childListExpression));

            Category category = saveCategory(categoryCount, categoryTitle, categoryLink);

            for (WebElement subcategoryElement : webElementSubcategoryList) {
                linkElement = subcategoryElement.findElement(By.xpath(childTitleExpression));
                String subcategoryTitle = linkElement.getText();
                String subcategoryLink = linkElement.getAttribute(linkAttrib);
                webElementChildCell = subcategoryElement.findElement(By.xpath(childCell));

                if (!hasExpandIcon(childLinkExpression, webElementChildCell)) continue;

                Subcategory subcategory = saveSubCategory(subCategoryCount, subcategoryTitle, subcategoryLink, category);

                List<WebElement> webElementTypeList = subcategoryElement.findElements(By.xpath(childListExpression));
                for (WebElement typeElement : webElementTypeList) {
                    linkElement = typeElement.findElement(By.xpath(childTitleExpression));
                    String typeTitle = linkElement.getText();
                    String typeLink = linkElement.getAttribute(linkAttrib);

                    webElementChildCell = typeElement.findElement(By.xpath(childCell));

                    if (!hasExpandIcon(childLinkExpression, webElementChildCell)) continue;

                    Type type = saveType(typeCount, typeTitle, typeLink, subcategory);

                    List<WebElement> webElementSubTypeList = typeElement.findElements(By.xpath(childListExpression));

                    if (webElementSubTypeList.isEmpty()) {
                        saveVariation(variationCount, typeTitle, typeLink, type);
                        continue;
                    }

                    for (WebElement subTypeElement : webElementSubTypeList) {
                        linkElement = subTypeElement.findElement(By.xpath(childTitleExpression));
                        String subTypeTitle = linkElement.getText();
                        String subTypeLink = linkElement.getAttribute(linkAttrib);

                        if (!hasExpandIcon(childLinkExpression, webElementChildCell)) continue;

                        List<WebElement> webElementVariationList = subTypeElement.findElements(By.xpath(childListExpression));

                        if (webElementVariationList.isEmpty()) {
                            saveVariation(variationCount, subTypeTitle, subTypeLink, type);
                            continue;
                        }

                        for (WebElement variationElement : webElementVariationList) {
                            linkElement = variationElement.findElement(By.xpath(childTitleExpression));
                            String variationTitle = linkElement.getText();
                            String variationLink = linkElement.getAttribute(linkAttrib);
                            saveVariation(variationCount, variationTitle, variationLink, type);
                            System.out.printf("%s %s %s %s %s%n", categoryTitle, subcategoryTitle, typeTitle, subTypeTitle, variationTitle);
                            System.out.printf("%s %s %s %s %s%n", categoryLink, subcategoryLink, typeLink, subTypeLink, variationLink);
                        }

                        System.out.printf("%s %s %s %s%n", categoryTitle, subcategoryTitle, typeTitle, subTypeTitle);
                        System.out.printf("%s %s %s %s%n", categoryLink, subcategoryLink, typeLink, subTypeLink);
                    }

                    System.out.printf("%s %s %s%n", categoryTitle, subcategoryTitle, typeTitle);
                    System.out.printf("%s %s %s%n", categoryLink, subcategoryLink, typeLink);
                }
                System.out.printf("%s %s%n", categoryTitle, subcategoryTitle);
                System.out.printf("%s %s%n", categoryLink, subcategoryLink);
            }
        }
        driver.close();
    }

    private Type saveType(long typeCount, String typeTitle, String typeLink, Subcategory subcategory) {
        long typeId = ++typeCount;
        Type type = typeRepository.findById(typeId).orElse(null);
        if (type == null) {
            type = typeRepository.save(new Type(typeId, subcategory.getId(), typeTitle, typeLink));
        } else {
            type.setName(typeTitle);
            type.setLink(typeLink);
            type.setSubcategoryId(subcategory.getId());
            typeRepository.save(type);
        }
        return type;
    }

    private void saveVariation(long variationCount, String variationTitle, String variationLink, Type type) {
        String code = variationTitle.trim().split(" ")[0];
        long variationId = ++variationCount;
        Variation variation = variationRepository.findById(variationId).orElse(null);
        String s = variationTitle.substring(code.trim().length());
        if (variation == null) {
            variationRepository.save(new Variation(variationId, code.trim(), type.getId(), s.trim(), variationLink));
        } else {
            variation.setCode(code);
            variation.setName(s.trim());
            variation.setLink(variationLink);
            variation.setTypeId(type.getId());
            variationRepository.save(variation);
        }
    }

    private Subcategory saveSubCategory(long subCategoryCount, String subcategoryTitle, String subcategoryLink, Category category) {
        long subCategoryId = ++subCategoryCount;
        Subcategory subcategory = subcategoryRepository.findById(subCategoryId).orElse(null);
        if (subcategory == null) {
            subcategory = subcategoryRepository.save(new Subcategory(subCategoryId, category.getId(), subcategoryTitle, subcategoryLink));
        } else {
            subcategory.setLink(subcategoryLink);
            subcategory.setName(subcategoryTitle);
            subcategory.setCategoryId(category.getId());
        }
        return subcategory;
    }

    private boolean hasExpandIcon(String childLinkExpression, WebElement webElementChildCell) throws InterruptedException {
        String background = webElementChildCell.getCssValue("background-image");
        if (StringUtils.isNotBlank(background)) {
            webElementChildCell.findElement(By.xpath(childLinkExpression)).click();
            Thread.sleep(2000);
            return true;
        } else {
            return false;
        }
    }

    private Category saveCategory(long categoryCount, String categoryTitle, String categoryLink) {
        long categoryId = ++categoryCount;
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            category = categoryRepository.save(new Category(categoryId, categoryTitle, categoryLink));
        } else {
            category.setLink(categoryLink);
            category.setName(categoryTitle);
            categoryRepository.save(category);
        }
        return category;
    }
}
