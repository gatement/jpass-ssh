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
-DtranPort=8117
-DdnsPort=53
-DdnsRemoteServer=127.0.0.1
-DdnsRemotePort=53
-DserveLocalOnly=true
-jar target/jpass-ssh.jar
</pre>
<pre>
./mvnw clean package && java -DsshServer= -DsshPort=10000 -DsshPassword= -DhttpPort=9119 -DtranPort=9117 -DdnsPort=9053 -DserveLocalOnly=false -jar target/jpass-ssh.jar
</pre>

## reference
* https://blog.csdn.net/dotalee/article/details/77838659

## download
* https://github.com/gatement/jpass-ssh/raw/master/target/jpass-ssh.jar
