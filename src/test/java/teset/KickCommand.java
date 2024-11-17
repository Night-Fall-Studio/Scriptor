/*
 * Project Name: Scriptor
 *
 * Licensed under the PolyForm Shield License 1.0.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * https://polyformproject.org/wp-content/uploads/2020/06/PolyForm-Shield-1.0.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package teset;

import com.github.nightfall.scriptor.CommandDispatcher;
import com.github.nightfall.scriptor.crafter.LiteralArgumentCrafter;
import com.github.nightfall.scriptor.exceptions.CommandSyntaxException;

import java.util.Scanner;

public class KickCommand {

    public static void main(String[] args) {
        // Create a new CommandDispatcher instance
        CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();

        // Create a new LiteralArgumentCrafter for the "kick" command
        LiteralArgumentCrafter<Object> kickCommand = LiteralArgumentCrafter.literal("kick")
                .executes(ctx -> {
                    System.out.println("Kicked player");
                    return 1;
                })
                .then(LiteralArgumentCrafter.literal("player")
                        .then(LiteralArgumentCrafter.literal("name")
                                .executes(ctx -> {
                                    // Retrieve the "name" argument as a String
                                    String playerName = ctx.getArgument("name", String.class);
                                    System.out.println("Kicked player " + playerName);
                                    return 1;
                                })
                        )
                );

        // Register the "kick" command with the dispatcher
        dispatcher.register(kickCommand);

        // Initialize Scanner to read user input
        Scanner scanner = new Scanner(System.in);

        // Start a loop that continues to prompt the user for input
        while (true) {
            System.out.print("Enter a command (type 'exit' to quit): ");
            String input = scanner.nextLine();

            // Exit condition
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting...");
                break;
            }

            // Split the input into arguments
            String[] inputArgs = input.split(" ");

            // Try to execute the command
            try {
                // Execute the full command with arguments
                dispatcher.execute(input, (Object) null); // Pass the full input here

                // If arguments are given, we process them for specific commands
                if (inputArgs.length > 1) {
                    dispatcher.execute(inputArgs[0], (Object) null); // You might want to handle arguments separately here
                }
            } catch (Exception e) {
                if (e instanceof CommandSyntaxException syntaxException) {

                    // Here, you want to make sure the exception has context and cursor
                    // In this case, it's an unrecognized command, so the error will happen at position 0
                    System.out.println("Error: " + syntaxException.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        }

        // Close the scanner
        scanner.close();
    }
}
