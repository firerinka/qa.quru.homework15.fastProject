package jetbrains.products.tests;

import com.codeborne.selenide.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.itemWithText;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductPageTests extends TestBase {
    private final String URL_PART = "products/";

    @Test
    @DisplayName("Главное меню содержит верный набор элементов")
    public void headerMenuHasCorrectItemsTest() {
        List<String> expectedItems = List.of("Разработчикам",
                "Командам",
                "Для обучения",
                "Решения",
                "Поддержка",
                "Магазин");

        open(URL_PART);
        $$("[data-test=main-menu-item-action]")
                .shouldHave(CollectionCondition.texts(expectedItems));
    }

    @Test
    @DisplayName("На странице продуктов отображаются карточки при выключенных фильтрах")
    public void productPageWithoutFiltersHasProductCardsTest() {
        open(URL_PART);
        $$("[data-test=cardSection]").shouldHave(CollectionCondition.sizeGreaterThan(0));
    }

    @Test
    @DisplayName("По клику на карточку продукта происходит переход на страницу продукта")
    public void clickOnProductCardOpensProductPageTest() {
        final String PRODUCT_URL_PART = "idea/";
        open(URL_PART);
        $$("[data-test=cardSection] a")
                .filter(Condition.exactText("IntelliJ IDEA"))
                .first()
                .click();
        Assertions.assertTrue(Selenide.webdriver().driver().url().contains(Configuration.baseUrl + PRODUCT_URL_PART));
    }

    @Test
    @DisplayName("Поиск по языкам ищет элементы фильтра по названию")
    public void languageSearchFilteringElementsByNameTest() {
        open(URL_PART);
        SelenideElement item = $$("[data-test=accordion] [data-test=accordion-item]").first();
        item.$("input").setValue("Java");
        item.$$(".filter__list label")
                .filter(visible)
                .shouldHave(itemWithText("Java"));
    }

    @Test
    @DisplayName("Поле поиска фильтров очищается по нажатию на Х")
    public void searchFiltersFieldIsCleanedByX() {
        open(URL_PART);
        SelenideElement item =
                $$("[data-test=accordion] [data-test=accordion-item] [class*=accordion-item__content-container]").first();
        SelenideElement input = item.$("input");
        input.setValue("Java");

        SelenideElement clearingIcon = item.$("svg");
        clearingIcon.shouldBe(visible);
        clearingIcon.click();
        input.shouldBe(empty);
    }

}
