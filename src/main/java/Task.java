import java.time.LocalDateTime;
import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Task {
  private int id;
  private String description;

  public Task(String description){
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public int getId() {
    return id;
  }

  public static List<Task> all() {
    String sql = "SELECT id, description FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  @Override
  public boolean equals(Object otherTask) {
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask =  (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) &&
             this.getId() == newTask.getId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tasks (description) VALUES (:description)";
      // con.createQuery(sql)
      //   .addParameter("description", this.description)
      //   .executeUpdate();
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description", this.description)
        .executeUpdate()
        .getKey();
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET description = :description WHERE id = :id";
      con.createQuery(sql)
        .addParameter("description", description)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String deleteQuery = "DELETE FROM tasks WHERE id = :id;";
    con.createQuery(deleteQuery)
      .addParameter("id", this.getId())
      .executeUpdate();

      String joinDeleteQuery = "DELETE FROM categories_tasks WHERE task_id = :taskId";
      con.createQuery(joinDeleteQuery)
        .addParameter("taskId", this.getId())
        .executeUpdate();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks WHERE id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
      return task;
    }
  }

  public void addCategory(Category category) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", category.getId())
        .addParameter("task_id", this.getId())
        .executeUpdate();
    }
  }

  public List<Category> getCategories() {
    try(Connection con = DB.sql2o.open()){
      String joinQuery = "SELECT category_id FROM categories_tasks WHERE task_id = :task_id";
      List<Integer> categoryIds = con.createQuery(joinQuery)
        .addParameter("task_id", this.getId())
        .executeAndFetch(Integer.class);

      List<Category> categories = new ArrayList<Category>();

      for (Integer categoryId : categoryIds) {
        String taskQuery = "Select * From categories WHERE id = :categoryId";
        Category category = con.createQuery(taskQuery)
          .addParameter("categoryId", categoryId)
          .executeAndFetchFirst(Category.class);
        categories.add(category);
      }
      return categories;
    }
  }
}
