import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

import java.util.ArrayList;

public class App {

  public static void main (String[] args){
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //view categories
    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("categories", Category.all());
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //view tasks
    get("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("tasks", Task.all());
      model.put("template", "templates/tasks.vtl");
      return new ModelAndView(model, layout);
    }, new  VelocityTemplateEngine());

    //add new category
    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    //add new task
    post("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      newTask.save();
      response.redirect("/tasks");
      return null;
    });

    //view specific category
    get("/categories/:id", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));
      model.put("category", category);
      model.put("allTasks", Task.all());
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //view specific task
    get("/tasks/:id", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      model.put("task", task);
      model.put("allCategories", Category.all());
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //edit page for a specific category
    get("/categories/:id/edit", (request,response) ->{
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));
      model.put("category", category);
      model.put("template", "templates/category-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //edit page for a specific task
    get("/tasks/:id/edit", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      model.put("task", task);
      model.put("template", "templates/task-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //changing the category name
    post("/categories/:id", (request,response) ->{
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      String newName = request.queryParams("name");
      Category category = Category.find(categoryId);
      category.update(newName);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    //changing the task description
    post("/tasks/:id", (request,response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      String newDescription = request.queryParams("description");
      Task task = Task.find(taskId);
      task.update(newDescription);
      response.redirect("/tasks/" + taskId);
      return null;
    });

    //deleting the category
    post("/categories/:id/delete", (request,response) ->{
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      category.delete();
      response.redirect("/categories/");
      return null;
    });

    //deleting the task
    post("/tasks/:id/delete", (request,response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      Task task = Task.find(taskId);
      task.delete();
      response.redirect("/tasks/");
      return null;
    });

    //add task to a specific category
    post("/add_tasks", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      category.addTask(task);
      response.redirect("/categories/" + categoryId);
      return null;
    });

    //add category to a specific task
    post("/add_categories", (request, response) -> {
      int taskId = Integer.parseInt(request.queryParams("task_id"));
      int categoryId = Integer.parseInt(request.queryParams("category_id"));
      Category category = Category.find(categoryId);
      Task task = Task.find(taskId);
      task.addCategory(category);
      response.redirect("/tasks/" + taskId);
      return null;
    });
  }
}
