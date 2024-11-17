# Scriptor 

## Overview

The Scriptor Command Framework is a Java-based library designed to facilitate the creation, parsing, and execution of commands in a command-line interface (CLI) environment. It provides a flexible and extensible architecture for managing commands, arguments, and their execution logic.

## Features

- **Command Registration**: Easily register commands with various argument types and execution logic.
- **Command Parsing**: Parse command strings into executable command structures.
- **Command Execution**: Execute parsed commands with support for custom execution contexts.
- **Error Handling**: Comprehensive error handling with custom exception types for different error conditions.
- **Suggestions**: Provide auto-completion suggestions for command arguments.
- **Redirection and Forking**: Support for command redirection and forking to handle complex command flows.

## Installation

To use Scriptor in your project, add the following Gradle dependency:

```groovy

	dependencies {
	        implementation 'com.github.Night-Fall-Studio:Scriptor:1.0'
	}
	
```

## Components

### CommandDispatcher

The `CommandDispatcher` class is the core component responsible for registering, parsing, and executing commands. It manages a tree of command nodes and facilitates the handling of command execution logic.

### Command Nodes

- **RootCommandNode**: The root node of the command tree.
- **LiteralCommandNode**: Represents a literal command argument.
- **ArgumentCommandNode**: Represents a command node that takes a single argument.

### Exception Types

- **CommandSyntaxException**: Represents syntax-related exceptions during command execution.
- **SimpleCommandExceptionType**: Represents simple command exceptions with a message.
- **DynamicCommandExceptionType**: Represents dynamic command exceptions with custom error messages.

### Argument Crafters

- **ArgumentCrafter**: Abstract base class for crafting command arguments.
- **LiteralArgumentCrafter**: Crafter for literal command arguments.
- **RequiredArgumentCrafter**: Crafter for required command arguments.

### Context and Parsing

- **CommandContext**: Represents the context in which a command is executed.
- **CommandContextBuilder**: Builds the command context during parsing.
- **ParseResults**: Holds the results of parsing a command string.

### Suggestions

- **Suggestions**: Represents auto-completion suggestions for command arguments.
- **SuggestionsBuilder**: Builds suggestions based on the current input.

## Usage

### Registering Commands

To register a command, create an instance of `CommandDispatcher` and use the `register` method to add commands.

```java
CommandDispatcher<Object> dispatcher = new CommandDispatcher<>();
dispatcher.register(new LiteralArgumentCrafter<>("example")
    .executes(context -> {
        // Command execution logic
        return 1;
    }));
```

### Parsing and Executing Commands

To parse and execute a command, use the `execute` method of `CommandDispatcher`.

```java
try {
    int result = dispatcher.execute("example", new Object());
    System.out.println("Command executed with result: " + result);
} catch (CommandSyntaxException e) {
    System.err.println("Command syntax error: " + e.getMessage());
}
```

### Providing Suggestions

To provide auto-completion suggestions, implement the `SuggestionProvider` interface and use it in your command nodes.

```java
SuggestionProvider<Object> suggestionProvider = (context, builder) -> {
    builder.suggest("suggestion1");
    builder.suggest("suggestion2");
    return builder.buildFuture();
};

dispatcher.register(new ArgumentCommandNode<>("arg", ArgumentType.string(), null, null, null, null, false, suggestionProvider));
```

## License

This project is licensed under the Polyform Shield License. See the `LICENSE` file for details.

## References:

- [Introduction to Parsing Techniques](https://file.fouladi.ir/courses/compiler/books/theory_of_parsing_vol1.pdf)
- [Parsing Techniques](https://staff.polito.it/silvano.rivoira/LingTrad/ParsingTechniques/ParsingTechniques.pdf)
- [A Command-line Argument Parser](https://www.jamesshore.com/downloads/Clean_Code_Args.pdf)
- [Error Recovery](https://www.cs.princeton.edu/courses/archive/spr04/cos320/notes/error-recovery.pdf)