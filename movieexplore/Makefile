
all:
	mkdir -p build
	javac -Xlint -cp tests/junit.jar -d build/ src/*.java src/obj/*.java tests/acc/*.java tests/unit/*.java

t: unit acc

acc:
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore SimilarUsersTests
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore RecommendationsTests
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore MiscTests
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore StatsTests
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore UserInfoTests
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore TopTests

unit:
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore ArgParserTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore DataCenterTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore MovieTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore RatingTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore UserTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore FilterTest
	java -cp :tests/junit.jar:tests/hamcrest-core.jar:build org.junit.runner.JUnitCore UtilTest
	
clean:
	rm -r build

jar:
	echo "Main-Class: MovieExplore" > build/Manifest.txt
	cd build && jar cfm MovieExplore.jar Manifest.txt *.class
	mv build/MovieExplore.jar ./

doc:
	javadoc -source src/*.java src/obj/*.java -d html/ -private

codestats:
	cloc --by-file --no3 src/
	cloc --by-file --no3 tests/
