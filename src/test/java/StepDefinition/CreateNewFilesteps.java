package StepDefinition;

import Pages.CreateNewFilePage;
import io.cucumber.java.en.*;

public class CreateNewFilesteps {

	public CreateNewFilePage filePage = new CreateNewFilePage();
	
	@Given("User click on {int} pages")
	public void user_click_on_pages(Integer int1) {
		filePage.click100Pages();		
	}

	@When("User have list of link having notice of assessment")
	public void user_have_list_of_link_having_notice_of_assessment() {
//		filePage.createFolder();
	}

	@Then("User click on list of link having notice of assessment")
	public void user_click_on_list_of_link_having_notice_of_assessment() {
		filePage.noticeOfAssessment();
	}

	@Then("User move this files to new folder")
	public void user_move_this_files_to_new_folder() {
//		filePage.moveDownloadedFileToFolder();
	}

}
