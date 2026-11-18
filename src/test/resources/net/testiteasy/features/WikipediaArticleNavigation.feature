@regression
  # please check RunCucumberTest.java
  # it contains filter "@smoke AND @regression"
  # it was OK when running tests individually locally
  # just be aware that the scope will be filtered when launching tests with Runner class or on CI
  # with added @smoke all test will be started
@smoke
@navigation
Feature: Wikipedia article navigation

  As a user I want to be able to open an article from search results.

  Scenario: Open article from search results
    Given user open application
    When welcome screen ready
    When user tap on the search field
    When user type in search line "Appium"
    When user select first search result "Appium"
    Then article "Appium" is opened