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
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CollectionServiceImpl implements CollectionService {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CollectionServiceImpl.class.getName());

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private VariationRepository variationRepository;

    @Override
    public void startCollection() throws InterruptedException {
        HtmlUnitDriver driver = new HtmlUnitDriver(true);
        String base_url = "https://icd.who.int/browse11/l-m/en";
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(base_url);
        System.out.println("Level 1 " + base_url);
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ygtvcontentel29\"]/a/span")));
        List<WebElement> webElementCategoryList = driver.findElementsByXPath("//div[@id='ygtvc1']//div[@class='ygtvitem']");
        long categoryCount = 0;
        long subCategoryCount = 0;
        long typeCount = 0;
        long variationCount = 0;
        Thread.sleep(2000);
        for (WebElement categoryElement : webElementCategoryList) {
            String categoryTitle = categoryElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getText();
            String categoryLink = categoryElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getAttribute("data-id");
            categoryElement.findElement(By.xpath(".//a[@class='ygtvspacer']")).click();
            List<WebElement> webElementSubcategoryList = categoryElement.findElements(By.xpath(".//div[@class='ygtvchildren']//div[@class='ygtvitem']"));
            long categoryId = ++categoryCount;
            Category category = categoryRepository.findOne(categoryId);
            if (category == null) {
                category = categoryRepository.save(new Category(categoryId, categoryTitle, categoryLink));
            } else {
                category.setLink(categoryLink);
                category.setName(categoryTitle);
                categoryRepository.save(category);
            }
            for (WebElement subcategoryElement : webElementSubcategoryList) {
                Thread.sleep(1000);
                String subcategoryTitle = subcategoryElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getText();
                String subcategoryLink = subcategoryElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getAttribute("data-id");
                subcategoryElement.findElement(By.xpath(".//a[@class='ygtvspacer']")).click();
                long subCategoryId = ++subCategoryCount;
                Subcategory subcategory = subcategoryRepository.findOne(subCategoryId);
                if (subcategory == null) {
                    subcategory = subcategoryRepository.save(new Subcategory(subCategoryId, category.getId(), subcategoryTitle, subcategoryLink));
                } else {
                    subcategory.setLink(subcategoryLink);
                    subcategory.setName(subcategoryTitle);
                    subcategory.setCategoryId(category.getId());
                }
                Thread.sleep(2000);
                List<WebElement> webElementTypeList = subcategoryElement.findElements(By.xpath(".//div[@class='ygtvchildren']//div[@class='ygtvitem']"));
                for (WebElement typeElement : webElementTypeList) {
                    Thread.sleep(1000);

                    String typeTitle = typeElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getText();
                    String typeLink = typeElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getAttribute("data-id");
                    typeElement.findElement(By.xpath(".//a[@class='ygtvspacer']")).click();
                    long typeId = ++typeCount;
                    Type type = typeRepository.findOne(typeId);
                    if (type == null) {
                        type = typeRepository.save(new Type(typeId, subcategory.getId(), typeTitle, typeLink));
                    } else {
                        type.setName(typeTitle);
                        type.setLink(typeLink);
                        type.setSubcategoryId(subcategory.getId());
                        typeRepository.save(type);
                    }
                    Thread.sleep(2000);
                    List<WebElement> webElementSubTypeList = typeElement.findElements(By.xpath(".//div[@class='ygtvchildren']//div[@class='ygtvitem']"));
                    for (WebElement subTypeElement : webElementSubTypeList) {
                        Thread.sleep(1000);
                        String subTypeTitle = subTypeElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getText();
                        String subTypeLink = subTypeElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getAttribute("data-id");
                        subTypeElement.findElement(By.xpath(".//a[@class='ygtvspacer']")).click();
                        Thread.sleep(2000);
                        List<WebElement> webElementVariationList = subTypeElement.findElements(By.xpath(".//div[@class='ygtvchildren']//div[@class='ygtvitem']"));
                        for (WebElement variationElement : webElementVariationList) {
                            Thread.sleep(1000);
                            String variationTitle = variationElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getText();
                            String variationLink = variationElement.findElement(By.xpath(".//a[starts-with(@class, 'ygtvlabel ')]")).getAttribute("data-id");
                            String code = variationTitle.trim().split(" ")[0];
                            long variationId = ++variationCount;
                            Variation variation = variationRepository.findOne(variationId);
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
                            System.out.printf("%s %s %s %s %s%n", categoryTitle, subcategoryTitle, typeTitle, subTypeTitle, variationTitle);
                            System.out.printf("%s %s %s %s %s%n", categoryLink, subcategoryLink, typeLink, subTypeLink, variationLink);
                        }
                        if (webElementVariationList.isEmpty()) {
                            String code = subTypeTitle.trim().split(" ")[0];
                            variationRepository.save(new Variation(++variationCount, code.trim(), type.getId(), subTypeTitle.substring(code.trim().length()).trim(), subTypeLink));
                        }
                        System.out.printf("%s %s %s %s%n", categoryTitle, subcategoryTitle, typeTitle, subTypeTitle);
                        System.out.printf("%s %s %s %s%n", categoryLink, subcategoryLink, typeLink, subTypeLink);
                    }

                    if (webElementSubTypeList.isEmpty()) {
                        String code = typeTitle.trim().split(" ")[0];
                        long variationId = ++variationCount;
                        Variation variation = variationRepository.findOne(variationId);
                        String s = typeTitle.substring(code.trim().length()).trim();
                        if (variation == null) {
                            variationRepository.save(new Variation(variationId, code.trim(), type.getId(), s, typeLink));
                        } else {
                            variation.setCode(code);
                            variation.setName(s);
                            variation.setLink(typeLink);
                            variation.setTypeId(type.getId());
                            variationRepository.save(variation);
                        }
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
}
