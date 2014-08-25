<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

ginere-site-generator
====================
http://ginere.eu

Ginere static site generation from templates


What it is about ?
-----------------------------------
When you create a static web site, there is a lot of work that can be
optimezed, using templates variables and common widges. This tool helps
you on that.
This a java program that parses a directory structure and generates
the html sites. It can be also executed as daemon, that means that you
can modify your flies on the fly and see instatanements the results.

How it works ?
-----------------------------------
It use two folders, one that contains your site content, and another one
that contains the common elements: widgets and templates.
The tool will parcours your content folder and will creating the pages
ussing the elements from the common folder and filling the variables.

In each folder inside your content folder you can put a properties
in a file called "GlobalProperties.prop" inside this file you can define
properties that will be used in your code. To get the value of one
propertie the program will look at the GlobalProperties.prop file of
the current folder, if that property is not here it look at the
GlobalProperties.prop file of the parent folder until it reachs the
root.

Just show me one example ?
-----------------------------------

Just create two folders, the content and common folders. Inside the content create
the index.html file

```
<html>
	{widget.html}
  </html>
```

Inside the comman filder create a file called widget.html:

```
	Hello wolrd!
```


The result will be:
...<html>
  Hello wolrd!
</html>...

See examples/example1


Let's use variables.
----------------------
The default variables for all the files of the folder are defined in a file called GlobalProperties.prop.

Create a GlobalProperties.prop inside the content folder
...
VARIABLE_NAME=This is the name of a variable.\nThis is a new Line, \
 but this is in the same line.
...

....
<html>
  {widget.html}
</br>
|VARIABLE_NAME|
</html>
...

...
<html>
  Hello wolrd!
</br>
This is the name of a variable.
This is a new Line, but this is in the same line.
</html>
....

...
<html>
  {widget.html}
</br>
This is the second file:
</br>
|VARIABLE_NAME|
</html>
...

...
<html>
  Hello wolrd!
</br>
This is the second file:
</br>
This is the name of a variable.
This is a new Line, but this is in the same line.
</html>
...

Let's use a real template.
-------------------------
Ok the previous example where right but let's make something more usefull. Lets create to files that share the same 

Let's create into the new content folder two file:
page1.prop:
...
PAGE_NAME=Page 1
PAGE_CONTENT={page1-content.html}
template=template.html
...

page2.prop:
...
PAGE_NAME=Page 2
PAGE_CONTENT={page2-content.html}
template=template.html
...

The two previous files are like the GlobalProperties.prop, that means that they are defining properties. The template propertie means 
that we are going to create a new page bases on those files ussing template.html as the template and the values of the properties of the 
template will be replaced by those values.
And lets create the new GlobalProperties.prop as :

...
SITE_DESCRIPTION=The site description
PAGE_NAME=No Name
..


Lests create a template into the common folder:
template.html
...
<!DOCTYPE html>
<html lang="|LANG|">
  {head.html}
  <body>	
	|PAGE_CONTENT|	
	{footer.html}
  </body>
</html>
...


Remarks:
		- If the property is not defined into this file, like SITE_DESCRIPTION, the GlobalProperties.prop will be used instead.
		- We can put files to be included into the Variables like : PAGE_CONTENT.
		- We can include files from another file (like in the template.html) and so on.



Multiple Folders :
------------------
Let's create Those folders:

content:
 |
 ├─> GlobalProperties.prop
 |
 ├─> page1.html
 |
 └-> folder:
 	  |
	  ├─> GlobalProperties.prop
	  |
	  └-> page2.html





Properties inside properties CREO QUE NO
Evaluate First the properties, Despues los includes







Where can I get the latest release?
-----------------------------------

Availables sources and releases:

```
	https://oss.sonatype.org/content/groups/public/eu/ginere/ginere-site-generator/
```

You can pull it from the central Maven repositories:

```xml
  <groupId>eu.ginere</groupId>
  <artifactId>ginere-site-generator</artifactId>
  <version>1.0.0</version>
```

Download snapshots and release artifacts from:

```
  https://oss.sonatype.org/content/groups/public
```

Download source from Github:

```
  https://github.com/ginere/ginere-site-generator
```

License
-------
Code is under the Apache Licence v2 license.



