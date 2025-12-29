package br.com.trevejo.todolist.users;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data; // Lombok - biblioteca que cria getters and setters automaticamente;

@Data
@Entity(name = "tb_users") // Define que essa classe é uma entidade do banco de dados;
public class UserModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  // qnd tiver o @Getter ou o @Setter sobre o atributo que está logo abaixo, o
  // lombok só formata o getter ou o setter daquele atributo;
  @Column(unique = true)
  private String username;  // para acessar uma informação privada, precisamos criar um método público (getters and setters);
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime creationDate;
}
