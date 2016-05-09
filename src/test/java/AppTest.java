import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Todo List!");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Categories"));
    fill("#name").with("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Tasks"));
    fill("#description").with("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void categoryShowPageDisplaysName() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
    goTo(url);
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskShowPageDisplaysDescription() {
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
    goTo(url);
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void taskIsAddedToCategory() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
    goTo(url);
    fillSelect("#task_id").withText("Mow the lawn");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Mow the lawn");
  }

  @Test
  public void categoryIsAddedToTask() {
    Category testCategory = new Category("Household chores");
    testCategory.save();
    Task testTask = new Task("Mow the lawn");
    testTask.save();
    String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
    goTo(url);
    fillSelect("#category_id").withText("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("<li>");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
  public void taskUpdate() {
    Task myTask = new Task("Clean");
    myTask.save();
    String taskPath = String.format("http://localhost:4567/tasks/%d/edit", myTask.getId());
    goTo(taskPath);
    fill("#description").with("Dance");
    click("button", withText("Submit new name"));
    assertThat(pageSource()).contains("Dance");
  }

  @Test
  public void categoryUpdate() {
    Category myCategory = new Category("Tuesday");
    myCategory.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d/edit", myCategory.getId());
    goTo(categoryPath);
    fill("#name").with("Wednesday");
    click("button", withText("Submit new name"));
    assertThat(pageSource()).contains("Wednesday");
  }

  @Test
  public void taskDelete() {
    Task myTask = new Task("Clean");
    myTask.save();
    String taskPath = String.format("http://localhost:4567/tasks/%d/edit", myTask.getId());
    goTo(taskPath);
    click("button", withText("Delete this task"));
    assertEquals(0, Task.all().size());
  }

  @Test
  public void categoryDelete() {
    Category myCategory = new Category("Clean");
    myCategory.save();
    String categoryPath = String.format("http://localhost:4567/categories/%d/edit", myCategory.getId());
    goTo(categoryPath);
    click("button", withText("Delete this category"));
    assertEquals(0, Category.all().size());
  }

  // @Test
  // public void taskDelete() {
  //   Category myCategory = new Category("Home");
  //   myCategory.save();
  //   Task myTask = new Task("Clean", myCategory.getId());
  //   myTask.save();
  //   String taskPath = String.format("http://localhost:4567/categories/%d/tasks/%d", myCategory.getId(), myTask.getId());
  //   goTo(taskPath);
  //   submit("#delete-task");
  //   assertEquals(0, Task.all().size());
  // }
}
//   @Test
//   public void rootTest() {
//     goTo("http://localhost:4567/");
//     assertThat(pageSource()).contains("Todo List!", "View Category List", "Add a New Category");
//   }
//
//   @Test
//   public void categoryIsCreatedTest() {
//     goTo("http://localhost:4567/");
//     click("a", withText("Add a New Category"));
//     fill("#name").with("Household chores");
//     submit(".btn");
//     assertThat(pageSource()).contains("Your category has been saved.");
//   }
//
//   @Test
//   public void categoryIsDisplayedTest() {
//     Category myCategory = new Category("Household chores");
//     myCategory.save();
//     String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
//     goTo(categoryPath);
//     assertThat(pageSource()).contains("Household chores");
//   }
//
//   @Test
//   public void categoryShowPageDisplaysName() {
//     goTo("http://localhost:4567/categories/new");
//     fill("#name").with("Household chores");
//     submit(".btn");
//     click("a", withText("View Categories"));
//     click("a", withText("Household chores"));
//     assertThat(pageSource()).contains("Household chores");
//   }
//
//   @Test
//   public void categoryTasksFormIsDisplayed() {
//     goTo("http://localhost:4567/categories/new");
//     fill("#name").with("Shopping");
//     submit(".btn");
//     click("a", withText("View Categories"));
//     click("a", withText("Shopping"));
//     click("a", withText("Add a new task"));
//     assertThat(pageSource()).contains("Add a task to Shopping");
//   }
//
//   @Test
//   public void tasksIsAddedAndDisplayed() {
//     goTo("http://localhost:4567/categories/new");
//     fill("#name").with("Banking");
//     submit(".btn");
//     click("a", withText("View Categories"));
//     click("a", withText("Banking"));
//     click("a", withText("Add a new task"));
//     fill("#description").with("Deposit paycheck");
//     submit(".btn");
//     click("a", withText("View Categories"));
//     click("a", withText("Banking"));
//     assertThat(pageSource()).contains("Deposit paycheck");
//   }
//
//   @Test
//   public void taskShowPage() {
//     Category myCategory = new Category("Home");
//     myCategory.save();
//     Task myTask = new Task("Clean", myCategory.getId());
//     myTask.save();
//     String categoryPath = String.format("http://localhost:4567/categories/%d", myCategory.getId());
//     goTo(categoryPath);
//     click("a", withText("Clean"));
//     assertThat(pageSource()).contains("Clean");
//     assertThat(pageSource()).contains("Return to Home");
//   }
//

// }
