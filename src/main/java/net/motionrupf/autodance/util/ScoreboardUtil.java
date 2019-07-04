package net.motionrupf.autodance.util;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import java.util.function.Predicate;

public class ScoreboardUtil {
    public static boolean testSidebarTitle(Scoreboard scoreboard, Predicate<String> tester) {
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if(objective != null) {
            return scoreboard.getScores().stream()
                    .filter(s -> s.getObjective().getName().equals(objective.getName()))
                    .map(s -> s.getObjective().getDisplayName())
                    .anyMatch(tester);
        }

        return false;
    }
}
