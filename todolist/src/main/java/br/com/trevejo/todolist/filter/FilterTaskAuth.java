package br.com.trevejo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import at.favre.lib.crypto.bcrypt.BCrypt;

import br.com.trevejo.todolist.users.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
// "ctrl + ." to implement methods;
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var servletPath = request.getServletPath();

    if (servletPath.startsWith("/tasks/")) {

      // pegar a autenticação (user e password)
      var authorization = request.getHeader("Authorization");

      // converter para base64;
      var authEncoded = authorization.substring("Basic".length()).trim();
      // converter de base64 para byte[];
      byte[] authDecode = Base64.getDecoder().decode(authEncoded);
      // converter de byte[] para String;
      var authString = new String(authDecode);
      // vai criar um array com user e password separados pelo ":";
      String[] credentials = authString.split(":");
      String username = credentials[0];
      String password = credentials[1];

      // validar usuário
      var user = this.userRepository.findByUsername(username);
      if (user == null) {
        response.sendError(401);
      } else {
        // validar senha
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passwordVerify.verified) {
          // segue viagem
          request.setAttribute("userId", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

}
