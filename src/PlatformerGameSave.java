import java.util.ArrayList;

import javax.json.*;

import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;

/**
 * @version 1
 * @author Evan Zhang
 * Revision history:
 *  - May 21, 2019: Created ~Evan Zhang
 *  - May 27, 2019: Commented ~Evan Zhang
 */
public class PlatformerGameSave extends GameSave {
    /** Instance variables */
    public double referencePoint;
    public Point2D player;
    public ArrayList<Point2D> removedNodes;
    public int[] scores;

    /**
     * Constructor
     * @param  referencePoint The reference point of the screen
     * @param  player         The player location
     * @param  removedNodes   The location of the removed blocks on the screen
     * @param  scores         The scores for the game level
     */
    public PlatformerGameSave(double referencePoint, Point2D player, ArrayList<Point2D> removedNodes, int[] scores) {
        this.referencePoint = referencePoint;
        this.player = player;
        this.removedNodes = removedNodes;
        this.scores = scores;
    }

    public JsonObject toJson() {
        JsonArrayBuilder jsonNodes = Json.createArrayBuilder();
        for (Point2D p : removedNodes) {
            jsonNodes = jsonNodes.add(pointToJson(p));
        }
        JsonArrayBuilder jsonScores = Json.createArrayBuilder();
        for (int s : scores) {
            jsonScores.add(s);
        }

        return Json.createObjectBuilder()
                   .add("referencePoint", referencePoint)
                   .add("player", pointToJson(player))
                   .add("scores", jsonScores.build())
                   .add("removedNodes", jsonNodes.build())
                   .build();
    }

    public static GameSave fromJson(JsonObject data) {
        double referencePoint = data.getJsonNumber("referencePoint").doubleValue();
        Point2D player = jsonToPoint(data.getJsonObject("player"));
        JsonArray jsonScores = data.getJsonArray("scores");
        int[] scores = new int[jsonScores.size()];
        for (int i = 0; i < jsonScores.size(); i++) {
            scores[i] = jsonScores.getInt(i);
        }
        ArrayList<Point2D> removedNodes = new ArrayList();
        for (JsonValue obj : data.getJsonArray("removedNodes")) {
            removedNodes.add(jsonToPoint((JsonObject)obj));
        }
        return new PlatformerGameSave(referencePoint, player, removedNodes, scores);
    }
}
