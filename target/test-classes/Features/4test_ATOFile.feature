@order4

Feature: Verify User is able to download the files

  Scenario: user is on list of link
    Given User click on 100 pages
    When User have list of link having notice of assessment
    Then User click on list of link having notice of assessment
    And User move this files to new folder  
    
    Given I want to move recent files
    When I execute the file move operation
    Then The recent files should be moved successfully  