import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Category_instantiatesCorrectlt_true() {
    Category myCategory = new Category("Household chores");
    assertEquals(true, myCategory instanceof Category);
  }

  @Test
  public void getName_categoryInstantiatesWithName_String() {
    Category myCategory = new Category("Household chores");
    assertEquals("Household chores", myCategory.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Category.all().size(), 0);
  }

   @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Category firstCategory = new Category("Household chores");
    Category secondCategory = new Category("Household chores");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    assertTrue(Category.all().get(0).equals(myCategory));
  }

  @Test
   public void save_assignsIdToObject() {
     Category myCategory = new Category("Household chores");
     myCategory.save();
     Category savedCategory = Category.all().get(0);
     assertEquals(myCategory.getId(), savedCategory.getId());
   }

 @Test
  public void find_findCategoryInDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Category savedCategory = Category.find(myCategory.getId());
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void addTask_addsTaskToCategory_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myCategory.addTask(myTask);
    Task savedTask = myCategory.getTasks().get(0);
    assertTrue(myTask.equals(savedTask));
  }
  @Test
  public void getTasks_returnsAllTasks_List() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myCategory.addTask(myTask);
    List savedTasks = myCategory.getTasks();
    assertEquals(1, savedTasks.size());
  }

  @Test
  public void delete_deletesAllTasksAndCategoriesAssociations() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    myCategory.addTask(myTask);
    myCategory.delete();
    assertEquals(0, myTask.getCategories().size());
  }


  // @Test
  // public void getTasks_retrievesALlTasksFromDatabase_tasksList() {
  //   Category myCategory = new Category("Household chores");
  //   myCategory.save();
  //   Task firstTask = new Task("Mow the lawn", myCategory.getId());
  //   firstTask.save();
  //   Task secondTask = new Task("Do the dishes", myCategory.getId());
  //   secondTask.save();
  //   Task[] tasks = new Task[] { firstTask, secondTask };
  //   assertTrue(myCategory.getTasks().containsAll(Arrays.asList(tasks)));
  // }
}
