# Java Compiler

This program is a simple compiler that tokenizes code in a programming language and displays the resulting tokens in a table using a graphical user interface (GUI). The program is written in Java and can be run from the command line.

The code file to be compiled is specified in the Compiler class as the FILE_PATH constant. The default value is "p.txt", but this can be changed to the file path of your choice.

The Compiler class contains several private static methods for tokenizing the code and creating individual tokens. The tokenize method takes a list of code lines as input and returns a list of tokens. The resulting tokens are then displayed in a table using the displayTokensInTable method.

To run the program, simply compile the Compiler class and run it with no arguments. The program will read the code file specified in FILE_PATH, tokenize the code, and display the resulting tokens in a table.

Note that this compiler is very simple and only supports a limited set of keywords and token types. It is intended as a starting point for more complex compiler projects, and can be extended to support additional features as needed.

## Screenshot

![image](https://user-images.githubusercontent.com/37311945/228672250-4238ad35-c81f-440d-a051-b6dc870bbc25.png)
