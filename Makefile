.PHONY: compile run test performance

JAR := ./target/ladytiger-0.0.1-SNAPSHOT.jar

compile:
	mvn package

$(JAR):
	mvn package

test:
	mvn verify

run: $(JAR)
	java -cp $(JAR) poc.TesteSimples

performance: $(JAR)
	java -cp $(JAR) poc.TestePerformance
