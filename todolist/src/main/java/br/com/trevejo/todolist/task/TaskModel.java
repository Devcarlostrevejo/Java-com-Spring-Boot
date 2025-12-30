package br.com.trevejo.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * ID
 * Usuário (ID_USER)
 * Título
 * Descrição
 * Prioridade
 * Data de Início
 * Data de Término
 */

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

  @Id
  @GeneratedValue(generator = "UUID") 
  private UUID id;

  @Column(length = 50)
  private String title;
  private String description;
  private String priority;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private UUID userId;

  @CreationTimestamp
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if(title.length() > 50) {
      throw new Exception("Título deve ter no máximo 50 caracteres");
    }
    this.title = title;
  }
}
