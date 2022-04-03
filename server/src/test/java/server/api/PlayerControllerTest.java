package server.api;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
  void keepAlive() throws Exception {
    var uuid =
      mockMvc.perform(post("/api/player/connect").queryParam("username", "nameTest")
          .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(matchesRegex(uuidRegex)))
        .andDo(print()).andReturn().getResponse().getContentAsString();
    mockMvc.perform(put("/api/player/keepAlive").contentType("application/json").queryParam("id", uuid)
      .content("false")).andExpect(status().isOk());
    mockMvc.perform(put("/api/player/keepAlive").contentType("application/json")
      .accept(MediaType.APPLICATION_JSON)
      .queryParam("id", uuid)
      .content("true")).andExpect(status().isOk());
  }

  @Test
  void getPlayers() throws Exception {
    var uuid1 = mockMvc.perform(post("/api/player/connect").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)))
      .andDo(print()).andReturn().getResponse().getContentAsString();
    var uuid2 =
      mockMvc.perform(post("/api/player/connect").queryParam("username", "nameTest").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(matchesRegex(uuidRegex)))
        .andDo(print()).andReturn().getResponse().getContentAsString();
    mockMvc.perform(get("/api/player/list"))
      .andExpect(jsonPath("$[*].username", Matchers.containsInAnyOrder("Player 1", "nameTest")));
  }

  @Test
  void getPlayerNames() throws Exception {
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
    mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)));
    mockMvc.perform(get("/api/player/names"))
      .andExpect(jsonPath("$", Matchers.containsInAnyOrder("Player 1", "Player 2")));
  }

  @Test
  void getPlayerById() throws Exception {
    var uuid1 = mockMvc.perform(post("/api/player/connect")
        .queryParam("username", "name"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)))
      .andReturn().getResponse().getContentAsString();
    var uuid2 = mockMvc.perform(post("/api/player/connect"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)))
      .andReturn().getResponse().getContentAsString();

    mockMvc.perform(get("/api/player/" + uuid1))
      .andExpect(content()
        .string("{\"id\":\"" + uuid1 + "\",\"username\":\"name\",\"waitingForGame\":false}"));
    mockMvc.perform(get("/api/player/" + uuid2))
      .andExpect(content()
        .string("{\"id\":\"" + uuid2 + "\",\"username\":\"Player 1\",\"waitingForGame\":false}"));
  }

  @Test
  void updateTestTimeOut() throws Exception {
    var uuid = mockMvc.perform(post("/api/player/connect")
        .queryParam("username", "name"))
      .andExpect(status().isOk())
      .andExpect(content().string(matchesRegex(uuidRegex)))
      .andReturn().getResponse().getContentAsString();
    MvcResult mvcResult = mockMvc.perform(get("/api/player/updates"))
      .andExpect(request().asyncStarted())
      .andReturn();
    mockMvc.perform(put("/api/player/keepAlive").contentType("application/json").queryParam("id", uuid)
      .content("true")).andExpect(status().isOk());
    mockMvc.perform(asyncDispatch(mvcResult))
      .andExpect(status().isOk());
  }
}