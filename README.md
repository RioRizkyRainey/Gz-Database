Gz-Database
===========

Android Database Library.
Gz-Database is a library for ease in handling create, read, update, delete in the SQLite database in android. So that the programmer does not required effortlessly in to write a query in android

## Note:
Gz-Database is still in alpha version.

Gz-Database inspired from Java Persistence and Sugar ORM.

This library facilitates you in a database CRUD on android.

## Features
 * Singleton in database
 * CRUD more easier
 * Automatic update database (just change meta-data in manifest)
 
## Coming up - 0.2.15 (insya Allah)
 * Initialization entity from JSON Object more easier
 * Auto POST to your webservice if device no internet connection 
 * Send HTTP request without call AsyncTask
 
## Installation
 * Change your AndroidManifest.xml
 specify Gz-Database as your application class in AndroidManifest.xml. You do that by changing the android:name attribute of the application tag.
    Example:
    ``` xml
    <application
        android:name="com.gz.databaselibrary.GzApplication"
        android:allowBackup="true"
        ... >
        
        ...
        <meta-data
            android:name="DATABASE"
            android:value="example" />     <!-- your database name -->
        <meta-data
            android:name="VERSION"
            android:value="1" />     <!-- your database version -->
            
            ...
        
    </application>
    ```
 * Make crebas in asset folder
create file in asset folder. File must represent number of version your database in AndroidManifest.xml

![asset](https://github.com/RioRizkyRainey/Gz-Database/blob/master/wiki/asset.PNG)

    sample file contents:
    CREATE TABLE IF NOT EXISTS user (
        name TEXT PRIMARY KEY,
        address TEXT,
        gender TEXT NOT NULL
    );
  
## Usage
### Create, Read, Update and Delete
 * **save the entity into the database without declaring the ContentValue.**
``` java
//create entity class
@Table(name = "user") //table name in database
public class User extends GzEntity {
      
      @Id                    //id or primary key
      @Column(name = "name") //column name in database
      private String name;
      
      @Column(name = "address")
      private String address;
      
      @Column(name = "gender")
      private String gender;
      
      public User() {} //please include this constructor in every class entity
      
      public User(String name, String address, String gender) {
            this.name = name;
            this.address = address;
            this.gender = gender;
      }
      
      ...
      
      public void setGender(String gender) {
            this.gender = gender;
      }
}

User jhon = new User("John", "Surabaya, Indonesia", "Male");
jhon.save(); //save entity to database

jhon.setGender("Female");
jhon.update(); //update entity to database. update based on a field that contains annotation @Id

// Read all data in table user
ArrayList<User> userList = User.toArrayList(User.class);

//delete by
GzEntity.delete(User.class, "name=?", "John"); //delete by name
```

## Contact
I am very happy if you could discuss and develop this library with me.
You can contact me via:
 * email   : [Rio Rizky Rainey Ferbiansyah](mailto:rizkyrainey@gmail.com)
 * facebook: [Rio Rizky Rainey Ferbiansyah](https://facebook.com/RioRizkyRainey)
 * twitter : [@RioRizkyRainey](https://twitter.com/RioRizkyRainey)
 * linkedin: [Rio Rizky Rainey Ferbiansyah](http://id.linkedin.com/pub/rio-rizky-rainey-ferbiansyah/aa/135/703/)

### License
    Copyright 2014 Rio Rizky Rainey Ferbiansyah
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
