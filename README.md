# M2-FinalProject-Sem2-2025

Meal Planner GUI
- A Java-based desktop application that helps users plan meals, manage ingredients, track grocery needs, and organize what's currently in the fridge. Built using Swing for the graphical interface.

1. Features:
- Add Meals with a list of ingredients.
- Remove Meals (automatically updates the grocery list based on remaining needs).
- Check Off Ingredients from the grocery list once bought — moves them to the fridge.
- Visual Indicators: Meals turn green and bold if all their ingredients are already in the fridge.
- Ingredient Viewer: Select a meal to view its ingredient list.
- Save & Load your meal plans with one click using serialization.


2. How It Works
- Meals and Ingredients both inherit from the abstract class FoodItem, which defines shared behavior like getName() and toString().
- Grocery list updates dynamically based on what’s missing from the fridge for each meal.
- Serialization is used to save/load the list of meals (meals.dat) using ObjectOutputStream and ObjectInputStream.
- Exception Handling: All file operations are wrapped in try-catch blocks. If something goes wrong (e.g. file not found, wrong format), exceptions are caught and printed instead of crashing the program.

3. Files
- GuiFileAll.java: Main file containing all logic and GUI elements. It is the final and complete version of everything including file serialization, gui, and normal method handling.
- GUIManager.java: Just the GUI Layout with most methods implemented except for the files
- GUILayout.java: just the outlook and user interface for the GUI.
- meals.dat: Automatically generated when saving meals — stores your data persistently.
