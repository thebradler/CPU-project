This program is written in Java and requires the JRE to run.
To run this program from the command line, first package it with gradlew.bat build (./gradlew build on bash).
This program requires a .txt file of raw temperatures with no labels as input (See Temperatures.txt as example) and was designed for a 4 core CPU, each column in the file representing a core.
To run this program with a file as input, type: ./gradlew cmdLineJavaExec -Pargs="Inputfile.txt"
(InputFile.txt obviously being your file of choice)
