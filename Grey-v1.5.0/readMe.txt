-- -- -- Grey v1.5.0
New features
-- Background color of status bar can be changed via Status bar settings MenuItem via connection configuration Menu via MenuBar---
-- Foreground color of status bar can be changed via Status bar settings MenuItem via connection configuration Menu via MenuBar---
-- Status bar print delay time can be changed via Status bar settings MenuItem via connection configuration Menu via MenuBar---
	- Print delay time is in milli seconds and can only vary from 10ms - 100ms.
	
New Looks
-- New menu item under settings
-- 3 available colors each for status bar background and foreground--

UI Changes
-- Better sound when printing text on status bar--

Code changes
-- New .java file GreyStatusConfig--
-- config.properties file stores colors and delay time not just connection settings.
-- All .java files have been updated where ProjectNew.Log is called--
	- Then, time is inputted manually from code. Now, user sets it in settings and its stored in config.properties.
	- Also, we just call the time set by user.
	
-- Defaults
	- Statusbar Background = DARK BLUE-
	- Statusbar Foreground = CYAN-
	- Delay time = 10ms.