@regression
  # please check RunCucumberTest.java
  # it contains filter "@smoke AND @regression"
  # it was OK when running tests individually locally
  # just be aware that the scope will be filtered when launching tests with Runner class or on CI
  # with added @smoke all test will be started
@smoke
@navigation
Feature: Search navigation

  Scenario: Return to search results from opened article
    Given user open application
    When welcome screen ready
    When user tap on the search field
    When user type in search line "Appium"
    And user select first search result "Appium"
    And user go back
    Then search results contain "Appium"