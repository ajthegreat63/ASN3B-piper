# Checkers Move Generator
This program represents a move generator for the checkers game. Checkers is a two player game where each player's goal is to capture the other player's pieces.

You can find the rules [here](https://officialgamerules.org/game-rules/checkers/)

Note: The rulebook mentions that "if you have a jump, you have no choice but to take it." For our checkers, you are not forced to take a jump if you don't want to

In our program, red moves down, and black moves up.

The goal of this program is to get all possible moves for a piece. This component can then be integrated into a full checkers game

# Usage

Compile the program and run using the following usage:  
`java Main <filename> <piece> <black_turn>`

* `filename` represents the board file you want to use

* `piece` represents the square of a piece you want to move in the format "A1", where the letter represents column, and the number represents the row. The letters and numbers move to the right and down respectively. The highest column is H, and the highest row is 8.

* `black_turn` represents whether the turn is black or red, where "1" represents black's turn, and "0" represents red's turn.

# Assumptions
We assume the file will be perfectly formatted, as in a full checkers game program, this component would use the previous gamestate as input, which should be correctly formatted. The file will fail to be parsed if it is not perfectly formatted. Additionally, the arguments the user inputs do not have error checking. Invalid user input not being handled should not be considered a bug for our program, as the way the values will be input in a real checkers game program will be different.

# Examples

The examples folder contains a few examples that can be used for testing purposes

Here is an example runthrough of the program:

`java Main example0 E5 0`

Output:
```
 A B C D E F G H
  ┌───────────────┐
1 │ │ │ │ │ │ │ │ │
2 │ │○│ │ │ │ │ │ │
3 │●│ │ │ │ │ │ │ │
4 │ │ │ │ │ │ │ │ │
5 │ │ │ │ │ │ │ │ │
6 │ │ │ │♛│ │ │ │ │
7 │ │ │ │ │ │ │ │ │
8 │ │ │ │ │ │ │ │ │
  └───────────────┘

   A B C D E F G H
  ┌───────────────┐
1 │ │ │ │ │ │ │ │ │
2 │ │○│ │ │ │ │ │ │
3 │●│ │ │ │ │ │ │ │
4 │ │ │ │ │ │ │ │ │
5 │ │ │ │ │ │ │ │ │
6 │ │ │ │ │ │♛│ │ │
7 │ │ │ │ │ │ │ │ │
8 │ │ │ │ │ │ │ │ │
  └───────────────┘

   A B C D E F G H
  ┌───────────────┐
1 │ │ │ │ │ │ │ │ │
2 │ │○│ │ │ │ │ │ │
3 │●│ │ │ │ │ │ │ │
4 │ │ │ │ │ │♛│ │ │
5 │ │ │ │ │ │ │ │ │
6 │ │ │ │ │ │ │ │ │
7 │ │ │ │ │ │ │ │ │
8 │ │ │ │ │ │ │ │ │
  └───────────────┘

   A B C D E F G H
  ┌───────────────┐
1 │ │ │ │ │ │ │ │ │
2 │ │○│ │ │ │ │ │ │
3 │●│ │ │ │ │ │ │ │
4 │ │ │ │♛│ │ │ │ │
5 │ │ │ │ │ │ │ │ │
6 │ │ │ │ │ │ │ │ │
7 │ │ │ │ │ │ │ │ │
8 │ │ │ │ │ │ │ │ │
  └───────────────┘
```

As we see, this shows all possible moves the red king piece can make
