## debug
* `./mvnw exec:java -Dexec.mainClass=net.johnsonlau.jproxy.App`

## package and run
* package `./mvnw clean package`
* run `java -DserverAddr=192.168.1.1 -DserverPort=22 -Dusername=root -Dpassword=123456 -DproxyPort=8119 -DlocalListening=true -jar target/jproxy-ssh.jar`

## reference
* https://blog.csdn.net/dotalee/article/details/77838659

## download
* https://github.com/gatement/jproxy-ssh/raw/master/target/jproxy-ssh.jar
