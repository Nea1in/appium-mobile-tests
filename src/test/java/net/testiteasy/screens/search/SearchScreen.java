package net.testiteasy.screens.search;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.back;

import io.appium.java_client.AppiumBy;

// locators below are Android-only (resource-ids / android.widget.* classes);
// IOSSearchScreen does not override them, so these steps aren't runnable on iOS yet
public class SearchScreen {

    private final By SEARCH_FIELD_PLACEHOLDER =
            By.id("org.wikipedia.dev:id/search_text_view");

    private final By SEARCH_FIELD =
            By.id("org.wikipedia.dev:id/search_src_text");

    private final By NEW_SEARCH_ONBOARDING_GET_STARTED =
            By.xpath(
                    "//androidx.compose.ui.platform.ComposeView" +
                            "/android.view.View" +
                            "/android.view.View" +
                            "/android.view.View[3]" +
                            "/android.widget.Button"
            );

    private final By YEAR_IN_REVIEW_ONBOARDING_CLOSE =
            By.xpath(
                    "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[1]/android.widget.Button"
            );

    private final By WIKIPEDIA_GAMES_POPUP_CLOSE =
            By.id("org.wikipedia.dev:id/closeButton");

    private final By SEARCH_RESULTS_INFORMATION_ICON =
            AppiumBy.accessibilityId("Information Icon");

    private By searchResult(String text) {
        return By.xpath(
                String.format("//android.widget.TextView[@text=\"%s\"]", text)
        );
    }

    private final By MORE_OPTIONS =
            AppiumBy.accessibilityId("More options");

    // delete commented. Zero tolerance to dead-code or provide a reason in a comment why it should be here
//    private final By ARTICLE_WEB_VIEW =
//            By.id("org.wikipedia.dev:id/page_web_view");

    private final By SEARCH_RESULTS =
            By.id("org.wikipedia.dev:id/fragment_search_results");

    private final By NAVIGATE_UP =
            AppiumBy.accessibilityId("Navigate up");

    public void clickOnSearchField() {
        $(SEARCH_FIELD_PLACEHOLDER)
                .shouldBe(Condition.visible)
                .click();

        closeNewSearchOnboardingIfPresent();
    }

    public void typeSearchQuery(String query) {
        $(SEARCH_FIELD)
                .shouldBe(Condition.visible)
                .setValue(query);
    }

    public void closeNewSearchOnboardingIfPresent() {

        try {
            $(NEW_SEARCH_ONBOARDING_GET_STARTED)
                    .shouldBe(Condition.visible)
                    .click();

        } catch (ElementNotFound ignored) {
            // New search onboarding is not displayed
        }
    }

    // delete commented
//    public void selectSearchResult(String itemText) {
//        $(searchResult(itemText))
//                .shouldBe(Condition.visible)
//                .click();
//    }

    public void checkArticleOpened(String articleTitle) {
        By articleTitleLocator = By.xpath(
                String.format(
                        "//android.view.View[@resource-id='pcs']" +
                                "/android.view.View[1]" +
                                "/android.widget.TextView[@text='%s']",
                        articleTitle
                )
        );
        $(articleTitleLocator).shouldBe(Condition.visible);
    }

    // dead code => delete commented
//    public void checkArticleOpened() {
//        $(ARTICLE_WEB_VIEW)
//                .shouldBe(Condition.visible);
//    }

    public void pressBack() {
        back();
    }

    public void checkSearchResultsContain(String text) {
        $$(By.className("android.widget.TextView"))
                .findBy(Condition.exactText(text))
                .shouldBe(Condition.visible);
    }

    public void selectFirstSearchResult(String itemText) {

        $(SEARCH_RESULTS)
                .shouldBe(Condition.visible); // <= here if not visible we get exception.
        // as element visibility mandatory here =>
        // wrap with "try - catch" with clear explicit exception reason
        clickOnSearchResult(itemText);

        if ($(SEARCH_RESULTS_INFORMATION_ICON).is(Condition.visible)) {
            clickOnSearchResult(itemText);
        }

        // Wikipedia shows random onboarding banners right after opening a result =>
        // dismiss anything that might cover the article before returning to the caller

        // all 3 steps could be moved to private method closeAllOnboardingBanners(), so public method will be cleaner
        closeYearInReviewOnboardingIfPresent();
        closeWikipediaGamesPopupIfPresent();
        closeToolbarOnboardingIfPresent();
    }

    // reuse searchResult(...) instead of re-building the same xpath here (was duplicated)
    private void clickOnSearchResult(String itemText) {
        $(searchResult(itemText))
                .shouldBe(Condition.visible)
                .click();
    }

    // suggested solution #1: make approach match your method closeWikipediaGamesPopupIfPresent()
    // suggested solution #2: same as closeNewSearchOnboardingIfPresent()
    // as we experimenting here - we may have 2 approaches for similar action "clickIfPresent()"

    // one more thing about naming. As in Selenide Conditions we have defined Condition.visible =>
    // it's better to use same in method's name: closeWikipediaGamesPopupIfPresent() => closeWikipediaGamesPopupIfVisible()
    // Condition.exist and Condition.visible are defined - tend to use them, rather than synonyms, to be unambiguous
    // pay attention on difference between conditions "exist" and "visible"
    public void closeYearInReviewOnboardingIfPresent() {
        SelenideElement closeButton =
            $(YEAR_IN_REVIEW_ONBOARDING_CLOSE);
//                    .shouldBe(Condition.visible); <= if element will not be visible we get an exception here
        if (closeButton.is(Condition.visible)) {
            closeButton.click();
        }
// extra line not needed here => code is read good without it
    }

    public void closeWikipediaGamesPopupIfPresent() {

        SelenideElement closeButton =
                $(WIKIPEDIA_GAMES_POPUP_CLOSE);

        if (closeButton.is(Condition.visible)) {
            closeButton.click();
        }
    }

    public void closeToolbarOnboardingIfPresent() {
        SelenideElement moreOptions = $(MORE_OPTIONS);

        if (moreOptions.is(Condition.visible)) {
            moreOptions.click();
            back();
        }
    }

    public void clickNavigateUp() {
        $(NAVIGATE_UP)
                .shouldBe(Condition.visible)
                .click();
    }
}