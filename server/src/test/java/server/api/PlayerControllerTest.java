package server.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  ObjectMapper mapper = new ObjectMapper();

  String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";

  @BeforeEach
  void removeAll() throws Exception {
    mockMvc.perform(delete("/api/player/removeAll")).andExpect((status().isOk()));
  }

  @Test
  void getPlayerCounterNoPlayers() throws Exception {
    mockMvc.perform(get("/api/player/playerCounter")).andDo(print()).andExpect(status().isOk())
      .andExpect(content().string("0"));
  }

  @Test
  void getPlayerCounterThreePlayers() throws Exception {
    mockMvc.perform(post("/api/player/connect")).andExpect(status().isOk());
    mockMvc.perform(post("/api/player/connect")).andExpect(status().isOk());
    mockMvc.perform(post("/api/player/connect")).andExpect(status().isOk());

    mockMvc.perform(get("/api/player/playerCounter")).andDo(print()).andExpect(status().isOk())
      .andExpect(content().string("3"));
  }

  @Test
  void addClientNoName() throws Exception {
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
  }

  @Test
  void addClientWithName() throws Exception {
    mockMvc.perform(post("/api/player/connect").queryParam("username", "name"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
  }

  @Test
  void differentNames() throws Exception {
    mockMvc.perform(post("/api/player/connect").queryParam("username", "name"))
      .andExpect(status().isOk());
    mockMvc.perform(post("/api/player/connect").queryParam("username", "other"))
      .andExpect(status().isOk());
  }

  @Test
  void conflictingName() throws Exception {
    mockMvc.perform(post("/api/player/connect").queryParam("username", "name"))
      .andExpect(status().isOk());
    mockMvc.perform(post("/api/player/connect").queryParam("username", "name"))
      .andExpect(status().isConflict())
      .andExpect(content().string("Username already taken"));
  }

  @Test
  void test() throws Exception {
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
    List<String> names = List.of("Player 1", "Player 2");
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