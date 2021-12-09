import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.GeoLocation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        MyGraph ans = new MyGraph();
        MyAlgo a = new MyAlgo();
        a.load(json_file);
        return a.getGraph();
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        MyAlgo ans = new MyAlgo();
        ans.load(json_file);
        return ans;
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file)
    {
        DirectedWeightedGraphAlgorithms alg = getGrapgAlgo(json_file);

        GUI_MyFrame ex = new GUI_MyFrame((MyGraph) alg.getGraph());
        ex.repaint();
        //ex.setVisible(true);
        /**
         * בעיות:
         * לא מצייר את התמונות
         * יש לתת מיקום
         * אולי הגודל לא פרופורציונלי
         * צריך לראות את ההרצאה הקודמת של בועז הקטע של בgui
         * צריך לראות את המדריך השלם לgui
         */

    }
    public static void main(String[] args)
    {
        runGUI("C:\\Users\\Liavm\\Desktop\\Ex2_v2\\data\\G1.json");
    }

}