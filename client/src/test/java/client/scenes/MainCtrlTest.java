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

package client.scenes;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.utils.ServerUtils;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainCtrlTest {

  private MainCtrl sut;
  private ServerUtils server;

  @BeforeEach
  public void setup() {
    sut = new MainCtrl(server);
  }

  @Test
  public void getPointsTest() {
    assertEquals(0, sut.getPoints());
  }

  @Test
  public void addPointsTest() {
    int initial = sut.getPoints();
    int toAdd = 100;
    sut.addPoints(toAdd);
    assertEquals(initial + toAdd, sut.getPoints());
  }

  @Test
  public void getPointsOffsetTest() {
    assertEquals(0, sut.getPoints());
  }

  @Test
  public void pointsTimerTest() {
    assertDoesNotThrow(() -> sut.startPointsTimer());
    assertDoesNotThrow(() -> sut.stopPointsTimer());
  }

  @Test
  public void resetTest() {
    sut.keepAliveExec = Executors.newSingleThreadScheduledExecutor();
    sut.reset();

    assertTrue(
      sut.serverIp == null
        && sut.clientId == null
        && sut.gameId == null
        && !sut.waitingForGame
        && sut.questionNumber == 0
        && sut.getPoints() == 0
        && sut.keepAliveExec.isShutdown()
    );
  }


}