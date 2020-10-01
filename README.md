# Socket echo server with  swing gui

## Linux deploy 

To run .jar file, you need to have java or jdk installed (**IntelliJ** jdk is ok).

####**IntelliJ**
~~~
$ java 
~~~

If there is no java in PATH, you should add it on your own:    

``` bash
 $ export PATH="$HOME/example/.jdks/openjdk-15/bin:$PATH" 
``` 
In my way, the **example** was empty because  .jdks lay in home dir.
 
####No **IntelliJ**
[Download Java via apt](https://opensource.com/article/19/11/install-java-linux)  
Type to check 
~~~
$ java 
~~~

### At the end

Find .jar file in main dir , and run 
``` bash  
$ java -jar socket_echo_server_swing.jar &
``` 

***

