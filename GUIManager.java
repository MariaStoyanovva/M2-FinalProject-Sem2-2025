import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class Ingredient {
    String name;

    Ingredient(String name) {
        this.name = name.toLowerCase();
    }

    public String toString() {
        return name;
    }
}

class Meal {
    String name;
    List<Ingredient> ingredients;

    Meal(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String toString() {
        return name;
    }

    public boolean isReady(Set<String> fridge) {  // do we have all needed ingredients for the meal in the fridge
        for (Ingredient ing : ingredients) {
            if (!fridge.contains(ing.name)) return false;
        }
        return true;
    }
}

public class GUIManager {
    private static DefaultListModel<Meal> mealModel = new DefaultListModel<>();
    private static DefaultListModel<String> groceryModel = new DefaultListModel<>();
    private static DefaultListModel<String> fridgeModel = new DefaultListModel<>();
    private static JList<Meal> mealList = new JList<>(mealModel);
    private static JTextArea ingredientArea = new JTextArea();
    private static JList<String> groceryList = new JList<>(groceryModel);
    private static JList<String> fridgeList = new JList<>(fridgeModel);
    private static Set<String> fridgeSet = new HashSet<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Meal Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // LEFT PANEL: Meals
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Meals"), BorderLayout.NORTH);
        mealList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Meal) {
                    Meal meal = (Meal) value;
                    if (meal.isReady(fridgeSet)) {
                        label.setForeground(Color.GREEN.darker());
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    } else {
                        label.setForeground(Color.BLACK);
                    }
                }
                return label;
            }
        });
        leftPanel.add(new JScrollPane(mealList), BorderLayout.CENTER);
        JButton addMealButton = new JButton("Add Meal");
        leftPanel.add(addMealButton, BorderLayout.SOUTH);

        // CENTER PANEL: Ingredients
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Ingredients"), BorderLayout.NORTH);
        ingredientArea.setEditable(false);
        centerPanel.add(new JScrollPane(ingredientArea), BorderLayout.CENTER);

        // RIGHT PANEL: Grocery List and Fridge
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel groceryPanel = new JPanel(new BorderLayout());
        groceryPanel.add(new JLabel("Grocery List"), BorderLayout.NORTH);
        groceryPanel.add(new JScrollPane(groceryList), BorderLayout.CENTER);
        JButton checkOffButton = new JButton("Check Off");
        groceryPanel.add(checkOffButton, BorderLayout.SOUTH);
        groceryPanel.setPreferredSize(new Dimension(200, 200));

        JPanel fridgePanel = new JPanel(new BorderLayout());
        fridgePanel.add(new JLabel("Fridge"), BorderLayout.NORTH);
        fridgePanel.add(new JScrollPane(fridgeList), BorderLayout.CENTER);
        fridgePanel.setPreferredSize(new Dimension(200, 200));

        rightPanel.add(groceryPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(fridgePanel);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        // EVENT HANDLERS
        mealList.addListSelectionListener(e -> {
            Meal selected = mealList.getSelectedValue();
            if (selected != null) {
                ingredientArea.setText("");
                for (Ingredient ing : selected.ingredients) {
                    ingredientArea.append(ing + "\n");
                }
            }
        });

        addMealButton.addActionListener(e -> {
            String mealName = JOptionPane.showInputDialog("Enter meal name:");
            if (mealName == null || mealName.trim().isEmpty()) return;

            List<Ingredient> ingList = new ArrayList<>();
            while (true) {
                String ing = JOptionPane.showInputDialog("Enter ingredient (or blank to finish):");
                if (ing == null || ing.trim().isEmpty()) break;
                ingList.add(new Ingredient(ing));
            }
            Meal meal = new Meal(mealName, ingList);
            mealModel.addElement(meal);
            for (Ingredient i : ingList) {
                if (!fridgeSet.contains(i.name) && !groceryModel.contains(i.name)) {
                    groceryModel.addElement(i.name);
                }
            }
            mealList.repaint();
        });

        checkOffButton.addActionListener(e -> {
            List<String> selected = groceryList.getSelectedValuesList();
            for (String item : selected) {
                if (!fridgeSet.contains(item)) {
                    fridgeSet.add(item);
                    fridgeModel.addElement(item);
                }
                groceryModel.removeElement(item);
            }
            mealList.repaint();
        });

        frame.setVisible(true);
    }
}
