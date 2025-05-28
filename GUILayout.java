import javax.swing.*;
import java.awt.*;

public class GUILayout {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Meal Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        //LEFT PANEL: Meals
        JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(new JLabel("Meals"), BorderLayout.NORTH);
        JList<String> mealList = new JList<>(new DefaultListModel<>());
            leftPanel.add(new JScrollPane(mealList), BorderLayout.CENTER);
        JButton addMealButton = new JButton("Add Meal");
            leftPanel.add(addMealButton, BorderLayout.SOUTH);

        //CENTER PANEL: Ingredients
        JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(new JLabel("Ingredients"), BorderLayout.NORTH);
        JTextArea ingredientArea = new JTextArea();
        ingredientArea.setEditable(false);
            centerPanel.add(new JScrollPane(ingredientArea), BorderLayout.CENTER);

        //RIGHT PANEL: Grocery List and Fridge
        JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));


        //GROCERY LIST
        JPanel groceryPanel = new JPanel(new BorderLayout());
            groceryPanel.add(new JLabel("Grocery List"), BorderLayout.NORTH);

        JList<String> groceryList = new JList<>(new DefaultListModel<>());
            groceryPanel.add(new JScrollPane(groceryList), BorderLayout.CENTER);

        JButton checkOffButton = new JButton("Check Off");
            groceryPanel.add(checkOffButton, BorderLayout.SOUTH);
            groceryPanel.setPreferredSize(new Dimension(200, 200));

        //FRIDGE
        JPanel fridgePanel = new JPanel(new BorderLayout());
            fridgePanel.add(new JLabel("Fridge"), BorderLayout.NORTH);
        JList<String> fridgeList = new JList<>(new DefaultListModel<>());
            fridgePanel.add(new JScrollPane(fridgeList), BorderLayout.CENTER);
            fridgePanel.setPreferredSize(new Dimension(200, 200));

        //add both to rightPanel
        rightPanel.add(groceryPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacer
        rightPanel.add(fridgePanel);

        //add panels to frame
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);

    }
}
