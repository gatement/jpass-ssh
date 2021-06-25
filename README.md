## debug
* `./mvnw exec:java -Dexec.mainClass=net.johnsonlau.jpass.App`

## package and run
* package `./mvnw clean package`
* run 
<pre>
java 
-DsshServer=
-DsshPort=22 
-DsshUsername=root 
-DsshPassword= 
-DhttpPort=8119 
-DserveLocalOnly=true
-jar target/jpass-ssh.jar
</pre>
<pre>
./mvnw clean package && java -DsshServer= -DsshPort=10000 -DsshUsername= -DsshPassword= -DhttpPort=9119 -DserveLocalOnly=false -jar target/jpass-ssh.jar
</pre>

## reference
* https://blog.csdn.net/dotalee/article/details/77838659

## download
* https://github.com/gatement/jpass-ssh/raw/master/target/jpass-ssh.jar
