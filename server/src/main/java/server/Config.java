/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commons.Activity;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.api.ActivityController;


@Configuration
public class Config {

  @Bean
  public Random getRandom() {
    return new Random();
  }

  /**
   * Every time the server is started, the activity db is cleared and all the activities are imported again.
   *
   * @param activityController dependency injection
   */
  @Bean
  CommandLineRunner commandLineRunner(ActivityController activityController) {
    return args -> {
      activityController.deleteAll();

      try {
        String activitiespath = "src/main/resources/JSON/activities.json";
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(activitiespath));
        List<Activity> activities = gson.fromJson(reader, new TypeToken<List<Activity>>() {
        }.getType());
        reader.close();
        activityController.saveAll(activities);
      } catch (IOException e) {
        System.out.println("Something went wrong when importing activities: " + e.getMessage());
      }
    };
  }

}