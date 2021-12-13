package classes;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;

import java.util.Scanner;


/**
 * This class is the classes.main class for classes.Ex2 - your implementation will be tested using this class.
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
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter a location (.json) at the end");
        String str= sc.nextLine(); //reads string.
        runGUI(str);
    }

}