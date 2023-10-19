#!/bin/bash
if [[ $# -eq 0 || $1 != "-r" && $1 != "-c" ]]; then
	echo "Valid arguments and -r or -c"
else
	if [ $1 == "-r" ]; then
		./gradlew run
	else
		./gradlew shadowJar

		# Because of the way the Textures are loaded, this is necessary (STBI fault, shame on it)
		jar -uf *-all.jar src/main/resources/textures

		clear
		echo "=> jar file created!"
		cp build/libs/*-all.jar . # Copy jar to current location
		ls --color=auto
	fi
fi



