## debug
* `./mvnw exec:java -Dexec.mainClass=net.johnsonlau.jpass.App`

## package and run
* package `./mvnw clean package`
* run `java -DserverAddr=192.168.1.1 -DserverPort=22 -Dusername=root -Dpassword=123456 -DproxyPort=8119 -DlocalListening=true -jar target/jpass-ssh.jar`

## reference
* https://blog.csdn.net/dotalee/article/details/77838659

## download
* https://github.com/gatement/jpass-ssh/raw/master/target/jpass-ssh.jar
