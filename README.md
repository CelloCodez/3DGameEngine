3D Game Engine
==

Modification of BennyQBD's (his youtube: thebennybox) 3D game engine in Java.

Original tutorial found here: https://www.youtube.com/playlist?list=PLEETnX-uPtBXP_B2yupUKlflXBznWIlL5

##Libraries Used##
- LWJGL (LightWeight Java Game Library, a type of OpenGL for Java)
- JBullet (Java port of Bullet physics)
- vecmath (included with JBullet)

##Build Dependencies##
- [JAVA](https://www.java.com/en/download/)
- BUILD TOOLCHAIN (Can be any one of the following, doesn't need to be all of them)
	- [Eclipse](http://eclipse.org/)
	- [NetBeans](https://netbeans.org/)
	- [IntelliJ IDEA](http://www.jetbrains.com/idea/)

##Simple Build Instructions##
###Using IDE###
- Open your preferred Java IDE, and create a new project.
- Import everything under the src folder as source files.
- Copy the res folder into your Java IDE's folder for your project.
- Copy the lib folder into it, adding any JARs inside of it into your build path.
- Make sure you set the LWJGL native libraries path to the folder of your OS inside of lib/natives/
- Build and run

##Additional Credits##
- Etay Meiri, for http://ogldev.atspace.co.uk/ which inspired the base code for the original repository.
- Everyone who's created or contributed to issues and pull requests, which make the project better!
