package server.api;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";

  @Test
  void getPlayerCounterNoPlayers() throws Exception {
    this.mockMvc.perform(get("/api/player/playerCounter")).andDo(print()).andExpect(status().isOk())
      .andExpect(content().string("0"));
  }

  @Test
  void addClientNoName() throws Exception {
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
  }

  @Test
  void keepAlive() throws Exception {
  }

  @Test
  void getPlayers() {
  }

  @Test
  void getPlayerNames() {
  }

  @Test
  void getPlayerById() {
  }
}