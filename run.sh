#!/bin/bash
if [[ $# -eq 0 || $1 != "-r" && $1 != "-c" ]]; then
	echo "Valid arguments and -r or -c"
else
	if [ $1 == "-r" ]; then
		./gradlew run
	else
		./gradlew shadowJar

		clear
		echo "=> jar file created!"
		cp build/libs/*-all.jar . # Copy jar to current location
		ls --color=auto

		# java -jar *-all.jar
	fi
fi



