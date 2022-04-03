package commons;

import com.fasterxml.jackson.annotation.JsonTypeName;

@SuppressWarnings("unused")
@JsonTypeName("INTERLEADERBOARD")
public class IntermediateLeaderboardQuestion extends Question {
  /**
   * Constructor
   */
  public IntermediateLeaderboardQuestion() {
    super(Type.INTERLEADERBOARD);
  }
}
