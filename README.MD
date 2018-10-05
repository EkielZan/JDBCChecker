## CheckJDBC

### What does the application is used for ?

The CheckJDBC app can help you easily validate connection to an Oracle DB trough JDBC url.

You can also validate SQL query and export the result in a file.

Through the GUI, you'll also be able to extract a CSV file of the request result.

### How to use it ?
Here is the usage
```shell
usage: java -jar CheckJDBC.jar
 -c,--config                Display Configuration.
 -d,--database <arg>        DB jdbc url.
 -f,--file <arg>            Config File.
 -g,--generate <filename>   Config File Generation
 -G,--gui                   Display the GUI.
 -h,--help                  Usage Display.
 -l,--locale <arg>          Change the language of the tools. Default: en.
 -o,--output <output>       Push output to file <output>
 -p,--password <arg>        Password
 -q,--query <arg>           SQL Query.
 -s,--silent                Be quiet.
 -u,--user <arg>            DB User Name.
 -V,--Oversion              Display Driver and DB version info
 -v,--version               Display CheckJDBC Version
```

### A little More Explanation
##### Database option:
```shell
-d,--database <arg>        DB jdbc url.
```
When using this options, you have to provide the full Oracle JDBC url like
```
jdbc:oracle:thin:@<hostname>:<port>/<service>
```
##### Version options:
The two options for version
```shell
-V,--Oversion              Display Driver and DB version info
```
and
```
 -v,--version               Display CheckJDBC Version
 ```
are not the same.
The first (upper V) is used to display the version of the target DB and of the Oracle JDBC driver used.

### What's inside
Two external libraries are used :
 - commons-cli-1.2.jar. [More info here](https://commons.apache.org/proper/commons-cli/)
 - ojdbc-6.0.12c.jar [More info here](https://www.oracle.com/technetwork/database/features/jdbc/jdbc-drivers-12c-download-1958347.html)

### License Information

Copyright 2018 DEJENEFFE Gilles

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
