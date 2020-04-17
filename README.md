## debug
* `./mvnw exec:java -Dexec.mainClass=net.johnsonlau.jpass.App`

## package and run
* package `./mvnw clean package`
* run 
<pre>
java 
-DsshServer=192.168.1.1 
-DsshPort=22 
-DsshUsername=root 
-DsshPassword=123456 
-DhttpPort=8119 
-DtranPort=8119 
-DdnsPort=53
-DdnsRemoteServer=127.0.0.1
-DdnsRemotePort=53
-DserveLocalOnly=true
-jar target/jpass-ssh.jar
</pre>

## reference
* https://blog.csdn.net/dotalee/article/details/77838659

## download
* https://github.com/gatement/jpass-ssh/raw/master/target/jpass-ssh.jar
