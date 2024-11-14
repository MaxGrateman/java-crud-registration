package controllers;

import entities.Task;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import services.TaskService;
import services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody @Validated Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        Task newTask = taskService.addTask(task, user);
        return ResponseEntity.ok(newTask);
    }

    @GetMapping
    public ResponseEntity<?> getUserTasks(Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        return ResponseEntity.ok(taskService.getTasksByUser(user));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId, @RequestBody @Validated Task updatedTask, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        Task task = taskService.getTaskByIdAndUser(taskId, user).orElseThrow();
        return ResponseEntity.ok(taskService.updateTask(task, updatedTask));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow();
        Task task = taskService.getTaskByIdAndUser(taskId, user).orElseThrow();
        taskService.deleteTask(task);
        return ResponseEntity.ok("Task deleted!");
    }
}
