import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

abstract class FoodItem implements Serializable{
    protected String name;

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return name;
    }
}

class Ingredient extends FoodItem{
    Ingredient(String name){
        this.name = name.toLowerCase();
    }
}

class Meal extends FoodItem{
    List<Ingredient> ingredients;

    Meal(String name, List<Ingredient> ingredients){
        this.name = name;
        this.ingredients = ingredients;
    }

    public boolean isReady(Set<String> fridge){
        for(Ingredient ing : ingredients){
            if(!fridge.contains(ing.getName())) return false;
        }
        return true;
    }
}

public class GuiFileAll {
    private static List<Meal> meals = new ArrayList<>();
    private static List<String> groceryItems = new ArrayList<>();
    private static List<String> fridgeItems = new ArrayList<>();
    private static Set<String> fridgeSet = new HashSet<>();

    private static JList<Meal> mealList = new JList<>();
    private static JTextArea ingredientArea = new JTextArea();
    private static JList<String> groceryList = new JList<>();
    private static JList<String> fridgeList = new JList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Meal Planner");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Meals"), BorderLayout.NORTH);
        mealList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus){
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if(value instanceof Meal){
                    Meal meal=(Meal) value;
                    if(meal.isReady(fridgeSet)){
                        label.setForeground(Color.GREEN.darker());
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    }else{
                        label.setForeground(Color.BLACK);
                    }
                }
                return label;
            }
        });
        leftPanel.add(new JScrollPane(mealList), BorderLayout.CENTER);

        JPanel mealButtonsPanel = new JPanel();
        JButton addMealButton = new JButton("Add Meal");
        JButton removeMealButton = new JButton("Remove Meal");
        mealButtonsPanel.add(addMealButton);
        mealButtonsPanel.add(removeMealButton);
        leftPanel.add(mealButtonsPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Ingredients"), BorderLayout.NORTH);
        ingredientArea.setEditable(false);
        centerPanel.add(new JScrollPane(ingredientArea), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel groceryPanel = new JPanel(new BorderLayout());
        groceryPanel.add(new JLabel("Grocery List"), BorderLayout.NORTH);
        groceryPanel.add(new JScrollPane(groceryList), BorderLayout.CENTER);
        JButton checkOffButton = new JButton("Check Off");
        JButton removeGroceryButton = new JButton("Remove Item");
        JPanel groceryButtons = new JPanel();
        groceryButtons.add(checkOffButton);
        groceryButtons.add(removeGroceryButton);
        groceryPanel.add(groceryButtons, BorderLayout.SOUTH);
        groceryPanel.setPreferredSize(new Dimension(200, 200));

        JPanel fridgePanel = new JPanel(new BorderLayout());
        fridgePanel.add(new JLabel("Fridge"), BorderLayout.NORTH);
        fridgePanel.add(new JScrollPane(fridgeList), BorderLayout.CENTER);
        fridgePanel.setPreferredSize(new Dimension(200, 200));

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JPanel fileButtons = new JPanel();
        fileButtons.add(saveButton);
        fileButtons.add(loadButton);

        rightPanel.add(groceryPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(fridgePanel);
        rightPanel.add(fileButtons);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        mealList.addListSelectionListener(e -> {
            Meal selected = mealList.getSelectedValue();
            if(selected != null){
                ingredientArea.setText("");
                for (Ingredient ing : selected.ingredients) {
                    ingredientArea.append(ing + "\n");
                }
            }
        });

        addMealButton.addActionListener(e -> {
            String mealName = JOptionPane.showInputDialog("Enter meal name:");
            if(mealName == null || mealName.trim().isEmpty()) return;

            List<Ingredient> ingList = new ArrayList<>();
            while(true){
                String ing = JOptionPane.showInputDialog("Enter ingredient (or blank to finish):");
                if (ing == null || ing.trim().isEmpty()) break;
                ingList.add(new Ingredient(ing));
            }
            Meal meal = new Meal(mealName, ingList);
            meals.add(meal);
            for(Ingredient i : ingList){
                if(!fridgeSet.contains(i.getName()) && !groceryItems.contains(i.getName())){
                    groceryItems.add(i.getName());
                }
            }
            refreshAll();
        });

        removeMealButton.addActionListener(e -> {
            Meal selected = mealList.getSelectedValue();
            if(selected != null){
                meals.remove(selected);
                ingredientArea.setText("");

                // Remove grocery ingredients only if no other meal needs them
                for(Ingredient ing : selected.ingredients){
                    String ingName = ing.getName();
                    boolean stillNeeded = false;
                    for(Meal meal : meals){
                        for(Ingredient otherIng : meal.ingredients){
                            if(otherIng.getName().equals(ingName)){
                                stillNeeded = true;
                                break;
                            }
                        }
                        if(stillNeeded) break;
                    }
                    if(!stillNeeded){
                        groceryItems.remove(ingName);
                    }
                }

                refreshAll();
            }
        });

        checkOffButton.addActionListener(e -> {
            List<String> selected = groceryList.getSelectedValuesList();
            for(String item : selected){
                if(!fridgeSet.contains(item)){
                    fridgeSet.add(item);
                    fridgeItems.add(item);
                }
                groceryItems.remove(item);
            }
            refreshAll();
        });

        removeGroceryButton.addActionListener(e -> {
            List<String> selected = groceryList.getSelectedValuesList();
            groceryItems.removeAll(selected);
            refreshAll();
        });

        saveButton.addActionListener(e -> {
            try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("meals.dat"))){
                oos.writeObject(meals);
            }catch (IOException ex){
                ex.printStackTrace();
            }
        });

        loadButton.addActionListener(e -> {
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("meals.dat"))){
                List<Meal> loadedMeals = (List<Meal>) ois.readObject();
                meals.clear();
                meals.addAll(loadedMeals);
                refreshAll();
            }catch (IOException | ClassNotFoundException ex){
                ex.printStackTrace();
            }
        });

        refreshAll();
        frame.setVisible(true);
    }

    private static void refreshAll(){
        mealList.setListData(meals.toArray(new Meal[0]));
        groceryList.setListData(groceryItems.toArray(new String[0]));
        fridgeList.setListData(fridgeItems.toArray(new String[0]));
    }
}
