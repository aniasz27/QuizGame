package server.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping("/api/client")
public class ClientController {
  private ExecutorService clients = Executors.newFixedThreadPool(5);

  @GetMapping(path = {"", "/"})
  public DeferredResult<String> welcomeMessage() {
    DeferredResult<String> output = new DeferredResult<>(30000L);
    output.onTimeout(() -> output.setErrorResult("the server is not responding in allowed time"));
    clients.execute(() -> {
      try {
        Thread.sleep(5000);
        output.setResult("Hello and welcome");
      } catch (Exception e) {
        output.setErrorResult("Something went wrong!");
      }
    });
    return output;
  }
}
