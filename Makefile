upload:
	git checkout master
	./gradlew okvolley:bintrayUpload

build:
	./gradlew assembleDebug
