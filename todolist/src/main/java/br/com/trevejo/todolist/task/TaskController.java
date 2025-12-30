package br.com.trevejo.todolist.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.trevejo.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    taskModel.setUserId((UUID) userId);

    var currentDate = LocalDateTime.now();
    if (currentDate.isAfter(taskModel.getStartDate()) || currentDate.isAfter(taskModel.getEndDate())) {
      return ResponseEntity.status(400).body("Data de início / data de término deve ser maior que a data atual");
    }

    if (taskModel.getStartDate().isAfter(taskModel.getEndDate())) {
      return ResponseEntity.status(400).body("Data de início deve ser menor que a data de término");
    }

    var taskCreated = this.taskRepository.save(taskModel);
    return ResponseEntity.status(201).body(taskCreated);
  }

  @GetMapping("/")
  public List<TaskModel> findTasks(HttpServletRequest request) {
    var userId = request.getAttribute("userId");
    var tasks = this.taskRepository.findByUserId((UUID) userId);
    return tasks;
  }

  @PutMapping("/{id}")
  public ResponseEntity editTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

    var task = this.taskRepository.findById(id).orElse(null);

    if (task == null) {
      return ResponseEntity.status(404).body("Tarefa não encontrada");
    }

    var userId = request.getAttribute("userId");

    if (!task.getUserId().equals(userId)) {
      return ResponseEntity.status(400).body("Usuário não tem permissão para editar essa tarefa");
    }

    Utils.copyNonNullProperties(taskModel, task);
    var taskUpdated = this.taskRepository.save(task);
    return ResponseEntity.ok().body(taskUpdated);
  }

}
