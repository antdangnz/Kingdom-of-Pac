COMPILING
---------
To compile this project on the Terminal:

1. Change directories to the src folder in the project folder.
2. Run the following command:

    javac $(find . -name '*.java')
    
    This should compile all java files in the current directory
3. Run the following command to start the game:

    java KingdomOfPac
4. Have fun!

NOTE: Due to an issue with the reading of .txt files, the game may print errors 
regarding files not existing, because it will search for the current folder it is in
(i.e project/src/src/...).