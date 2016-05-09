import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class TaskTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Task_instantiatesCorrectly_true(){
    Task myTask = new Task("Mow the lawn");
    assertEquals(true, myTask instanceof Task);
  }

  @Test
  public void getDescription_taskWithDescription_String(){
    Task myTask = new Task("Mow the lawn");
    assertEquals("Mow the lawn", myTask.getDescription());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Task.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfDescriptionsAreTheSame_true() {
    Task firstTask = new Task("Mow the lawn");
    Task secondTask = new Task("Mow the lawn");
    assertTrue(firstTask.equals(secondTask));
  }

  @Test
  public void save_savesObjectIntoDatabase_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    assertTrue(Task.all().get(0).equals(myTask));
  }

  @Test
  public void save_assignsIdToObject() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(myTask.getId(), savedTask.getId());
  }

  @Test
  public void find_findsTasksInDatabase_True() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertTrue(myTask.equals(savedTask));
  }

  @Test
  public void update_updatesTaskDescription_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myTask.update("Take a nap");
    assertEquals("Take a nap", Task.find(myTask.getId()).getDescription());
  }

  @Test
  public void delete_deletesTask_true() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    int myTaskId = myTask.getId();
    myTask.delete();
    assertEquals(null, Task.find(myTaskId));
  }

  @Test
  public void addCategory_addsCategoryToTask() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myTask.addCategory(myCategory);
    Category savedCategory = myTask.getCategories().get(0);
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void getCategories_returnsAllCategories_List() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myTask.addCategory(myCategory);
    List savedCategories = myTask.getCategories();
    assertEquals(1, savedCategories.size());
  }


  @Test
  public void delete_deletesAllTasksAndCategoriesAssociations() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myTask.addCategory(myCategory);
    myTask.delete();
    assertEquals(0, myCategory.getTasks().size());
  }


  // @Test
  // public void save_savesCategoryIdIntoDB_true() {
  //   Category myCategory = new Category("Household chores");
  //   myCategory.save();
  //   Task myTask = new Task("Mow the lawn", myCategory.getId());
  //   myTask.save();
  //   Task savedTask = Task.find(myTask.getId());
  //   assertEquals(savedTask.getCategoryId(), myCategory.getId());
  // }
}
