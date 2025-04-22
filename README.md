# Spring_REST_API

# Setup

1. Download Java 17 JDK. (This step is mandatory if you don't have Java 17 already installed)

  - Recommended is OpenJDK 17 from Oracle

  - https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_windows-x64_bin.zip

<br>

2. Java 17 setup. (This is mandatory if you followed step 1 and didn't have Java 17 installed previously)

  - For this step you will need Administration rights, if you don't have Administration rights, replace location "C:\Program Files\" with "C:\Users\%YOUR_USERNAME%\"

    2.1 Extract downloaded java file

    2.2 create new folder inside "C:\Program Files\" called "OpenJDK"

    2.3 copy "jdk-17.*.*" folder into "C:\Program Files\OpenJDK"

<br>

3. Environment Variables Setup (Mandatory if you followed step 1 and didn't have Java 17 installed previously)

    3.1 Open Environment variables and under "System Variables", click "New"

    3.2 Enter the variable name as JAVA_HOME. Enter the variable value as the installation path of the JDK (appending the \bin sub-folder at the end of the path). 

    - Example: "C:\Program Files\OpenJDK\OpenJDK11U-jdk_x64_windows_hotspot_11.0.15_10\jdk-11.0.15+10\bin"

    3.3 Find and click on System variable named "Path". Click on "New" and add "%JAVA_HOME%" as variable.

    3.4 To test the Java installation, open CMD and type "java --version". If the output was the version everything is working correctly.

<br>
4. Download any Java IDE of your choice if you already don't have one.

  - I am using IntelliJ IDE Community Edition

  - https://www.jetbrains.com/idea/download/?section=windows

<br>

5. Download Maven (This step is not needed if you are using Intellij IDE because IntelliJ already comes with Bundled Maven)

  - https://dlcdn.apache.org/maven/maven-3/3.9.9/source/apache-maven-3.9.9-src.zip

    5.1 Extract the distribution archive in any directory.

    5.2 Open Environment variables and under "System Variables", click "New"

    5.3 Enter the variable name as MAVEN_HOME. Enter the variable value as the installation path of the Maven (appending the \bin sub-folder at the end of the path). 

    - Example: "C:\Users\%YOUR_USERNAME%\Desktop\apache-maven-3.9.9\bin"

    5.4 Find and click on System variable named "Path". Click on "New" and add "%MAVEN_HOME%" as variable.

    5.5 To test the Maven installation, open CMD and type "mvn -version". If the output was the version everything is working correctly.

<br>
6. Download PostgreSQL

  - https://sbp.enterprisedb.com/getfile.jsp?fileid=1259505

    6.1 During installation when asked to create superuser, use default username "postgres" and set password to "postgres"

<br>
7. Download Database Tool (Optional, While installing PostgreSQL you can also install pgAdmin4 which is another database tool)

  - I am using DBeaver Community.

  - https://dbeaver.io/download/

<br>
8. Create new database called "product"

  - This step is crucial.

    8.1 Open database tool (pgAdmin4, DBeaver etc.)
    
    8.2 Connect to local PostgreSQL server (if prompted for a password, type the password that you setup during PostgreSQL installation)
    
    8.3 Right click on "Databases" -> "Create" -> "Database..." (Example taken from pgAdmin4)
    
    8.4 Set database name as "product", and set database owner to "postgres" (if you don't see "postgres" in dropdown menu, create new user named "postgres")
    
    8.5 Validate that schema exist and schema name is "public", if schema doesn't exist, right click on "Schemas" -> "Create" -> "Schema..." (Example taken from pgAdmin4)
    

<br>
9. Edit "application.properties" in src/main/resources (Optional, only needed if you didn't do step 6)

  - spring.datasource.password=YOUR_PASSWORD (password that you set for "postgres" user)

<br>
10. Run "Clean install with tests" custom configuration (See Custom run configuration bellow)


<br>
11. Start apk by running "Start" custom configuration (See Custom run configuration bellow)


# Custom run configurations

Create new run cofigurations

1. Apk Run Configuration (Application Configuration)

  - Name: Start

  - JDK: Java 17	Main Class: SpringRestAppApplication

2. Clean install with tests (Maven Configuration)

  - Name: Clean install

  - Run: clean install

3. Clean install without tests (Maven Configuration)

  - Name: Clean install No Tests

  - Run: clean install -DskipTests=true
