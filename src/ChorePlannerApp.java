
import java.util.Scanner;

/**
 * This is the application for the Roommate Chore Planner.
 *
 * @author Group 50
 * @version Oct 16, 2026
 */
public class ChorePlannerApp
{
    private Scanner input;
    private ChorePlanner planner;

    /**
     * Creates the console application.
     *
     * @param scanner the source of user input
     */
    public ChorePlannerApp(Scanner scanner)
    {
        if (scanner == null)
        {
            throw new IllegalArgumentException
              (
                "Scanner cannot be null.");
        }
        input = scanner;
        planner = new ChorePlanner();
    }


    /**
     * Runs the menu until the user exits or input ends.
     */
    public void run()
    {
        boolean running = true;

        while (running)
        {
            printMenu();
            System.out.print("Enter your choice: ");
            String line = readLine();

            if (line == null)
            {
                return;
            }

            int choice;
            try
            {
                choice = Integer.parseInt(line.trim());
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Invalid input. Please enter a number.");
                continue;
            }

            switch (choice)
            {
                case 0:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                case 1:
                    addRoommate();
                    break;
                case 2:
                    removeRoommate();
                    break;
                case 3:
                    addChore();
                    break;
                case 4:
                    removeChore();
                    break;
                case 5:
                    printLists();
                    break;
                case 6:
                    System.out.println(planner.generateSchedule());
                    break;
                case 7:
                    advanceWeek();
                    break;
                default:
                    System.out.println(
                        "Unknown choice. Please choose 0-7.");
            }
        }
    }


    /**
     * Displays the menu.
     */
    private void printMenu()
    {
        System.out.println("\n=== Roommate Chore Planner ===");
        System.out.println("1. Add roommate");
        System.out.println("2. Remove roommate");
        System.out.println("3. Add chore");
        System.out.println("4. Remove chore");
        System.out.println("5. Display roommates and chores");
        System.out.println("6. Display weekly schedule");
        System.out.println("7. Advance to next week");
        System.out.println("0. Exit");
    }


    /**
     * Reads a line, returning null when input ends.
     *
     * @return the input line, or null at EOF
     */
    private String readLine()
    {
        if (!input.hasNextLine())
        {
            return null;
        }
        return input.nextLine();
    }


    /**
     * Reads an integer and re-prompts it when input is not numeric.
     *
     * @param prompt the prompt to display
     * @return the integer, or null at EOF
     */
    private Integer readInteger(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);
            String line = readLine();

            if (line == null)
            {
                return null;
            }

            try
            {
                return Integer.valueOf(line.trim());
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Invalid input. Please enter a number.");
            }
        }
    }


    /**
     * Adds a roommate.
     */
    private void addRoommate()
    {
        Integer id = readInteger("Enter roommate ID: ");
        if (id == null)
        {
            return;
        }

        System.out.print("Enter roommate name: ");
        String name = readLine();
        if (name == null)
        {
            return;
        }

        try
        {
            planner.addRoommate(id, name);
            listsChanged();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Removes a roommate.
     */
    private void removeRoommate()
    {
        Roommate[] people = planner.getRoommates();
        if (people.length == 0)
        {
            System.out.println("There are no roommates to remove.");
            return;
        }

        for (int i = 0; i < people.length; i++)
        {
            System.out.println((i + 1) + ". " + people[i]);
        }

        Integer position =
            readInteger("Enter roommate position to remove: ");
        if (position == null)
        {
            return;
        }

        if (position < 1 || position > people.length)
        {
            System.out.println("Invalid roommate position.");
            return;
        }

        planner.removeRoommate(position - 1);
        listsChanged();
    }


    /**
     * Adds a chore.
     */
    private void addChore()
    {
        System.out.print("Enter chore name: ");
        String name = readLine();
        if (name == null)
        {
            return;
        }

        System.out.print("Enter chore description "
            + "(press Enter for none): ");
        String description = readLine();
        if (description == null)
        {
            return;
        }

        try
        {
            planner.addChore(name, description);
            listsChanged();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Removes a chore. 
     */
    private void removeChore()
    {
        Chore[] chores = planner.getChores();
        if (chores.length == 0)
        {
            System.out.println("There are no chores to remove.");
            return;
        }

        for (int i = 0; i < chores.length; i++)
        {
            System.out.println((i + 1) + ". " + chores[i]);
        }

        Integer position =
            readInteger("Enter chore position to remove: ");
        if (position == null)
        {
            return;
        }

        if (position < 1 || position > chores.length)
        {
            System.out.println("Invalid chore position.");
            return;
        }

        planner.removeChore(position - 1);
        listsChanged();
    }


    /**
     * Displays the current roommate and chore lists.
     */
    private void printLists()
    {
        System.out.println("Roommates:");
        Roommate[] people = planner.getRoommates();

        if (people.length == 0)
        {
            System.out.println("No roommates.");
        }
        for (int i = 0; i < people.length; i++)
        {
            System.out.println((i + 1) + ". " + people[i]);
        }

        System.out.println("Chores:");
        Chore[] chores = planner.getChores();

        if (chores.length == 0)
        {
            System.out.println("No chores.");
        }
        for (int i = 0; i < chores.length; i++)
        {
            System.out.println((i + 1) + ". " + chores[i]);
        }
    }


    /**
     * Advances the chores to the next week.
     */
    private void advanceWeek()
    {
        try
        {
            planner.advanceWeek();
            System.out.println("Advanced to week "
                + planner.getWeekNumber() + ".");
            System.out.println(planner.generateSchedule());
        }
        catch (IllegalStateException e)
        {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Prints the required update message and regenerated schedule.
     */
    private void listsChanged()
    {
        System.out.println(
            "Lists changed; current-week assignments "
                + "have been regenerated.");
        System.out.println(planner.generateSchedule());
    }


    /**
     * Starts the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args)
    {
        ChorePlannerApp app =
            new ChorePlannerApp(new Scanner(System.in));
        app.run();
    }
}

