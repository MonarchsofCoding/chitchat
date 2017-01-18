## ChitChat > Java Client

### Setup

 1. Run the following to set-up Gradle with IntelliJ IDEA
```
gradle cleanIdea
gradle idea
```
 2. Open `./ChitChatDesktop` in IntelliJ IDEA.


### Test Execution

#### With Gradle

```
cd ChitChatDesktop
gradle test
```

#### With Docker

```
invoke test
```

### Building

#### With Gradle

```
cd ChitChatDesktop
gradle build
```

If the build is successful, you can run the built `.jar` with

```
java -jar build/libs/ChitChatDesktop.jar
```

#### With Docker

```
invoke build
```
